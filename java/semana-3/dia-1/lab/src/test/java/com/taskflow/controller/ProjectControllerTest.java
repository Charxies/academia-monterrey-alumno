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

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.matchesPattern;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * ProjectControllerTest — REGRESIÓN de D2/D3, REPARADA para el lockdown de D5. El 401 masivo que el
 * security metió NO es un bug: es la feature. La reparación canónica (integrador) = TOKEN REAL en el
 * setup: se hace login vía MockMvc contra los usuarios semilla y se inyecta el "Bearer <token>" en
 * CADA request. PROHIBIDO "arreglarla" con permitAll("/**").
 *
 * Se loguean dos usuarios: ana (USER, owner del proyecto 1) para lo normal, y admin (ADMIN) para el
 * DELETE (ana no es owner del proyecto 2, sería 403; ADMIN borra cualquiera).
 *
 * ====================== TODO MP-7: MIGRAR A SLICE (@WebMvcTest) ======================
 * MISMO tratamiento canónico que TaskControllerTest (va solo, en pares — el bloque ya lo conoces):
 *   @WebMvcTest(ProjectController.class)
 *   @AutoConfigureMockMvc(addFilters = false)
 *   @MockitoBean ProjectService projectService;
 *   @MockitoBean JwtAuthenticationFilter jwtAuthenticationFilter;
 * Notas propias de este controller:
 *   - GET/POST/PUT se prueban con when(...) sobre projectService; el DELETE (regla owner/ADMIN) NO se
 *     prueba aquí: la seguridad vive en integration/SecurityRulesTest.
 *   - createProject lee el username del Authentication. Con addFilters=false no hay filtro que lo ponga:
 *     inyéctalo directo en el request con  .principal(new UsernamePasswordAuthenticationToken("ana", null, List.of()))
 *     y stubbea  when(projectService.crear(any(), eq("ana"))).thenReturn(proyecto).
 *     (@WithMockUser sería la alternativa cuando quieres method security en el slice.)
 * Luego: mvn test -Dtest=ProjectControllerTest (re-medir) y llenar la tabla de tiempos del README (MP-7).
 * ====================================================================================
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class ProjectControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private String tokenAna;
    private String tokenAdmin;

    @BeforeEach
    void login() throws Exception {
        tokenAna = tokenDe("ana", "ana123");
        tokenAdmin = tokenDe("admin", "admin123");
    }

    /** Login REAL contra los usuarios semilla; devuelve el JWT del AuthResponse. */
    private String tokenDe(String username, String password) throws Exception {
        String body = """
                { "username": "%s", "password": "%s" }
                """.formatted(username, password);
        String json = mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        return JsonPath.read(json, "$.token");
    }

    // ==================== GET ====================

    @Test
    void getProjects_retorna200YLista() throws Exception {
        mockMvc.perform(get("/projects").header("Authorization", "Bearer " + tokenAna))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3));
    }

    @Test
    void getTareasDeProyecto_proyectoInexistente_retorna404() throws Exception {
        mockMvc.perform(get("/projects/999/tasks").header("Authorization", "Bearer " + tokenAna))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    void getTareasDeProyecto_proyectoExistenteConTareas_retorna200() throws Exception {
        // Proyecto 1 tiene 5 tareas (ids 1..5).
        mockMvc.perform(get("/projects/1/tasks").header("Authorization", "Bearer " + tokenAna))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(5));
    }

    @Test
    void getTareasDeProyecto_proyectoExistenteSinTareas_retorna200ConListaVacia() throws Exception {
        // Proyecto 3 existe pero no tiene tareas -> 200 con [].
        mockMvc.perform(get("/projects/3/tasks").header("Authorization", "Bearer " + tokenAna))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    // ==================== Escritura (rollback por @Transactional) ====================

    @Test
    void postProject_valido_devuelve201ConLocation() throws Exception {
        String body = """
                {
                  "name": "Nuevo proyecto de prueba",
                  "description": "Creado desde el test"
                }
                """;

        mockMvc.perform(post("/projects")
                        .header("Authorization", "Bearer " + tokenAna)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", matchesPattern(".*/projects/\\d+")))
                .andExpect(jsonPath("$.name").value("Nuevo proyecto de prueba"))
                .andExpect(jsonPath("$.ownerId").value(1));       // owner = ana (id 1), el usuario del token
    }

    @Test
    void postProject_nombreEnBlanco_devuelve400ConDetalleDeCampo() throws Exception {
        // El DETALLE por campo del advice: errors[0] menciona el campo "name". Autenticado -> pasa
        // la seguridad y la validación @Valid dispara el 400.
        String body = """
                { "name": "", "description": "sin nombre" }
                """;

        mockMvc.perform(post("/projects")
                        .header("Authorization", "Bearer " + tokenAna)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0]").value(containsString("name")));
    }

    @Test
    void postProject_sinToken_devuelve401() throws Exception {
        // El lockdown en acción: sin token, ni siquiera se llega a la validación -> 401.
        String body = """
                { "name": "Sin token", "description": "no debería pasar" }
                """;

        mockMvc.perform(post("/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void deleteProject_comoAdmin_devuelve204() throws Exception {
        // Proyecto 2 existe (owner luis). ana NO es owner -> sería 403; ADMIN borra cualquiera -> 204.
        mockMvc.perform(delete("/projects/2").header("Authorization", "Bearer " + tokenAdmin))
                .andExpect(status().isNoContent());
    }
}
