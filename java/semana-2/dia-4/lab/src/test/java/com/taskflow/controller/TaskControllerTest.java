package com.taskflow.controller;

import com.taskflow.config.DataSeeder;
import com.taskflow.repository.ProjectRepository;
import com.taskflow.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.matchesPattern;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * TaskControllerTest — MIGRADO en MP-6 del slice @WebMvcTest de D2 a CONTEXTO COMPLETO
 * (@SpringBootTest + @AutoConfigureMockMvc). El valor de hoy está en el FLUJO entero: controller +
 * advice + repo real + seeder, no en la capa web aislada. El slice + @MockitoBean de ayer queda para
 * cuando se quiere AISLAR una capa (Mockito a fondo llega en S3D1 — ya usaron un slice).
 *
 * Independencia de tests (punto de dolor 9): el InMemoryTaskRepository es un bean SINGLETON, así que
 * los datos de un test contaminan al siguiente. @BeforeEach limpia AMBOS repos y resiembra la semilla
 * del DataSeeder (eco de la regla de independencia de S1D5, ahora con beans).
 *
 * Naming: metodo_escenario_resultado (S1D5). AAA.
 */
@SpringBootTest
@AutoConfigureMockMvc
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private DataSeeder dataSeeder;

    @BeforeEach
    void limpiarYResembrar() throws Exception {
        taskRepository.findAll().forEach(t -> taskRepository.deleteById(t.getId()));
        projectRepository.findAll().forEach(p -> projectRepository.deleteById(p.getId()));
        dataSeeder.run();   // vuelve a la semilla conocida (3 proyectos, 9 tareas)
    }

    // ==================== GET migrados de D2 (ahora contra la semilla real) ====================

    @Test
    void getTasks_sinFiltro_retorna200YLista() throws Exception {
        // 9 tareas sembradas; POR_URGENCIA deja la única VENCIDA (tarea 5) de primera.
        mockMvc.perform(get("/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(9))
                .andExpect(jsonPath("$[0].title").value("Corregir bug de fechas"));
    }

    @Test
    void getTasks_conFiltroStatus_retornaSoloEseEstado() throws Exception {
        // En la semilla hay 2 tareas DONE (tareas 2 y 7).
        mockMvc.perform(get("/tasks").param("status", "DONE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].status").value("DONE"));
    }

    @Test
    void getTaskPorId_existente_retorna200ConElTitulo() throws Exception {
        mockMvc.perform(get("/tasks/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Diseñar esquema de BD"))
                .andExpect(jsonPath("$.projectId").value(1));
    }

    @Test
    void getTaskPorId_inexistente_retorna404() throws Exception {
        // Ahora el 404 es UNIFORME (ErrorResponse del advice), no un body vacío.
        mockMvc.perform(get("/tasks/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    // ==================== Tests NUEVOS de escritura (MP-6 + integrador) ====================

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
        // Título de 2 chars: @Size(min=3) falla -> 400. (El detalle por campo se asserta en Projects.)
        String body = """
                { "title": "ab", "priority": "LOW" }
                """;

        mockMvc.perform(post("/projects/1/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deleteTask_inexistente_devuelve404() throws Exception {
        mockMvc.perform(delete("/tasks/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    // ==================== STRETCH ====================

    /** STRETCH — la regla de ESTADO en HTTP: PATCH a DONE sin responsable (tarea 6) -> 422. */
    @Test
    void patchStatus_sinAssignee_devuelve422() throws Exception {
        String body = """
                { "status": "DONE" }
                """;

        mockMvc.perform(patch("/tasks/6/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.status").value(422));
    }
}
