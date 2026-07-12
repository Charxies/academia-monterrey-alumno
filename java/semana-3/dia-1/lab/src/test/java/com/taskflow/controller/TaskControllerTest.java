package com.taskflow.controller;

import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.matchesPattern;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * TaskControllerTest — REGRESIÓN de D2/D3, REPARADA para el lockdown de D5. Los endpoints de /tasks y
 * /projects/{id}/tasks exigen autenticación (no owner): con el token de ana (USER) alcanza. Mismo
 * patrón que ProjectControllerTest: login real en el setup + "Bearer <token>" en cada request.
 * Sin token -> 401 (se verifica explícitamente); nada de permitAll de conveniencia.
 *
 * ====================== TODO MP-6: MIGRAR A SLICE (@WebMvcTest) ======================
 * Hoy esta clase arranca TODO el contexto (JPA + seguridad + seeder) para probar un contrato web. La
 * migración medida:
 *   (0) LÍNEA BASE en tu máquina (anótala en la tabla del README):
 *         mvn test                              (tiempo total + # arranques de contexto)
 *         mvn test -Dtest=TaskControllerTest    (la clase SOLA — el ciclo de dev, 40×/día)
 *   (1) Sustituir las 4 anotaciones de arriba por las 3 CANÓNICAS del slice:
 *         @WebMvcTest(TaskController.class)
 *         @AutoConfigureMockMvc(addFilters = false)   // la seguridad se prueba en integration/
 *         class TaskControllerTest {
 *             @Autowired MockMvc mockMvc;
 *             @MockitoBean TaskService taskService;
 *             @MockitoBean ProjectService projectService;      // TaskController también lo inyecta
 *             @MockitoBean JwtAuthenticationFilter jwtAuthenticationFilter;  // <- ver tropiezo (a)
 *       - Tropiezo (a): sin el @MockitoBean del filtro, el slice escanea los Filter @Component y arrastra
 *         JwtService -> "No qualifying bean of type 'JwtService'". Mockear el filtro corta ese arrastre.
 *       - Tropiezo (b): sin addFilters=false, el slice aplica la seguridad DEFAULT de Boot y TODO da 401.
 *   (2) Los asserts contra la SEMILLA vuelven a when(...) (el músculo de S2D2 reactivado). Los write-tests
 *       CONSERVAN sus jsonPath (la validación y el advice SÍ viven en el slice). El @BeforeEach del login
 *       y el "Bearer <token>" DESAPARECEN: ya no hay filtro que exija el token. Ejemplos:
 *         when(taskService.listar()).thenReturn(List.of(...));                 // getTasks
 *         when(taskService.buscarPorId(1L)).thenReturn(Optional.of(tarea));    // getTaskPorId existente
 *         when(taskService.buscarPorId(999L)).thenReturn(Optional.empty());    // 404 vía advice
 *         when(projectService.buscarPorId(1L)).thenReturn(Optional.of(proj));  // POST: el proyecto existe
 *         when(taskService.crear(any(), eq(1L))).thenReturn(tareaConId);       // POST 201 + Location
 *         when(taskService.cambiarStatus(6L, DONE)).thenThrow(new TaskStateException("..."));  // 422
 *         doThrow(new TaskNotFoundException(999L)).when(taskService).eliminar(999L);           // DELETE 404
 *   (3) RE-MEDIR la clase sola -> la diferencia se ve a ojo. Anótala en el README.
 *   (4) Integrador (refactor b): parametrizar los títulos inválidos -> 400 con @ValueSource/@CsvSource.
 * ====================================================================================
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private String token;

    @BeforeEach
    void login() throws Exception {
        String body = """
                { "username": "ana", "password": "ana123" }
                """;
        String json = mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        token = JsonPath.read(json, "$.token");
    }

    private String bearer() {
        return "Bearer " + token;
    }

    // ==================== GET (contra la semilla real: ids 1..9) ====================

    @Test
    void getTasks_sinFiltro_retorna200YLista() throws Exception {
        // 9 tareas sembradas; POR_URGENCIA deja la única VENCIDA (tarea 7) de primera.
        mockMvc.perform(get("/tasks").header("Authorization", bearer()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(9))
                .andExpect(jsonPath("$[0].title").value("Corregir bug de fechas"));
    }

    @Test
    void getTasks_conFiltroStatus_retornaSoloEseEstado() throws Exception {
        // En la semilla hay 2 tareas DONE (tareas 2 y 8).
        mockMvc.perform(get("/tasks").param("status", "DONE").header("Authorization", bearer()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].status").value("DONE"));
    }

    @Test
    void getTaskPorId_existente_retorna200ConElTitulo() throws Exception {
        mockMvc.perform(get("/tasks/1").header("Authorization", bearer()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Diseñar esquema de BD"))
                .andExpect(jsonPath("$.projectId").value(1));
    }

    @Test
    void getTaskPorId_inexistente_retorna404() throws Exception {
        mockMvc.perform(get("/tasks/999").header("Authorization", bearer()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    void getTasks_sinToken_retorna401() throws Exception {
        // El lockdown: sin token, 401 (feature del día, no bug).
        mockMvc.perform(get("/tasks"))
                .andExpect(status().isUnauthorized());
    }

    // ==================== Escritura (rollback por @Transactional) ====================

    @Test
    void postTask_valida_devuelve201ConLocation() throws Exception {
        String body = """
                {
                  "title": "Redactar el informe trimestral",
                  "description": "Con las métricas del quarter",
                  "priority": "HIGH",
                  "assigneeId": 1,
                  "dueDate": "2099-12-31"
                }
                """;

        mockMvc.perform(post("/projects/1/tasks")
                        .header("Authorization", bearer())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", matchesPattern(".*/tasks/\\d+")))
                .andExpect(jsonPath("$.title").value("Redactar el informe trimestral"))
                .andExpect(jsonPath("$.status").value("TODO"))     // nace TODO
                .andExpect(jsonPath("$.projectId").value(1));
    }

    @Test
    void postTask_tituloCorto_devuelve400() throws Exception {
        // Título de 2 chars: @Size(min=3) falla -> 400.
        String body = """
                { "title": "ab", "priority": "LOW" }
                """;

        mockMvc.perform(post("/projects/1/tasks")
                        .header("Authorization", bearer())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deleteTask_inexistente_devuelve404() throws Exception {
        mockMvc.perform(delete("/tasks/999").header("Authorization", bearer()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    /** La regla de ESTADO en HTTP: PATCH a DONE sin responsable (tarea 6) -> 422. */
    @Test
    void patchStatus_sinAssignee_devuelve422() throws Exception {
        String body = """
                { "status": "DONE" }
                """;

        mockMvc.perform(patch("/tasks/6/status")
                        .header("Authorization", bearer())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.status").value(422));
    }
}
