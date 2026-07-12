package com.taskflow.controller;

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
 * ProjectControllerTest — REGRESIÓN de D2/D3 contra JPA. Mismos cambios de S2D4 que TaskControllerTest:
 * @ActiveProfiles("test") (H2 en memoria) + @Transactional (rollback por test; la semilla queda estable).
 *
 * Conserva la distinción de D2 en GET /projects/{id}/tasks: "no existe el proyecto" (404) NO es lo
 * mismo que "proyecto sin tareas" (200 con []). Los asserts de listas afirman el TAMAÑO, no la posición.
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class ProjectControllerTest {

    @Autowired
    private MockMvc mockMvc;

    // ==================== GET ====================

    @Test
    void getProjects_retorna200YLista() throws Exception {
        // 3 proyectos sembrados (ids 1..3).
        mockMvc.perform(get("/projects"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3));
    }

    @Test
    void getTareasDeProyecto_proyectoInexistente_retorna404() throws Exception {
        mockMvc.perform(get("/projects/999/tasks"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    void getTareasDeProyecto_proyectoExistenteConTareas_retorna200() throws Exception {
        // Proyecto 1 tiene 5 tareas (ids 1..5).
        mockMvc.perform(get("/projects/1/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(5));
    }

    @Test
    void getTareasDeProyecto_proyectoExistenteSinTareas_retorna200ConListaVacia() throws Exception {
        // Proyecto 3 existe pero no tiene tareas -> 200 con [].
        mockMvc.perform(get("/projects/3/tasks"))
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
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", matchesPattern(".*/projects/\\d+")))
                .andExpect(jsonPath("$.name").value("Nuevo proyecto de prueba"))
                .andExpect(jsonPath("$.ownerId").value(1));       // owner semilla = ana (id 1)
    }

    @Test
    void postProject_nombreEnBlanco_devuelve400ConDetalleDeCampo() throws Exception {
        // El DETALLE por campo del advice: errors[0] menciona el campo "name".
        String body = """
                { "name": "", "description": "sin nombre" }
                """;

        mockMvc.perform(post("/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0]").value(containsString("name")));
    }

    @Test
    void deleteProject_existente_devuelve204() throws Exception {
        // Proyecto 2 existe; borrarlo arrastra sus tareas (cascada manual). 204 sin cuerpo.
        mockMvc.perform(delete("/projects/2"))
                .andExpect(status().isNoContent());
    }
}
