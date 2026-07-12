package com.taskflow.controller;

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
 * TaskControllerTest — REGRESIÓN de D2/D3, ahora contra JPA. Cambios de S2D4:
 *   - @ActiveProfiles("test"): la suite arrastra JPA (@SpringBootTest levanta todo), así que corre
 *     contra la H2 EN MEMORIA del perfil test (application-test.yml). NO escribe en data/taskflow.mv.db.
 *   - @Transactional: cada test corre en una transacción que hace ROLLBACK al terminar. Por eso ya no
 *     hace falta el @BeforeEach que limpiaba y resembraba a mano (S2D3): el DataSeeder siembra UNA vez
 *     al arrancar el contexto (ids 1..9 estables) y cada test deshace sus cambios. Con IDENTITY, borrar
 *     y resembrar por test NO devolvería los mismos ids — el rollback sí.
 *
 * Naming: metodo_escenario_resultado (S1D5). AAA.
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    // ==================== GET (contra la semilla real: ids 1..9) ====================

    @Test
    void getTasks_sinFiltro_retorna200YLista() throws Exception {
        // 9 tareas sembradas; POR_URGENCIA deja la única VENCIDA (tarea 7) de primera.
        mockMvc.perform(get("/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(9))
                .andExpect(jsonPath("$[0].title").value("Corregir bug de fechas"));
    }

    @Test
    void getTasks_conFiltroStatus_retornaSoloEseEstado() throws Exception {
        // En la semilla hay 2 tareas DONE (tareas 2 y 8).
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
        mockMvc.perform(get("/tasks/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
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

    /** La regla de ESTADO en HTTP: PATCH a DONE sin responsable (tarea 6) -> 422. */
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
