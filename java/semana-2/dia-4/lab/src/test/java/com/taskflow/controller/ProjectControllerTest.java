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

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.matchesPattern;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * ProjectControllerTest — MIGRADO en MP-6 al contexto completo (@SpringBootTest +
 * @AutoConfigureMockMvc), igual que TaskControllerTest, y ampliado en el integrador con los tests de
 * escritura de Projects.
 *
 * Conserva la distinción de D2 en GET /projects/{id}/tasks: "no existe el proyecto" (404) NO es lo
 * mismo que "proyecto sin tareas" (200 con []). Los asserts de listas evitan asumir el orden (el
 * repositorio en memoria devuelve por orden de HashMap): se afirma el TAMAÑO, no la posición.
 */
@SpringBootTest
@AutoConfigureMockMvc
class ProjectControllerTest {

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
        dataSeeder.run();
    }

    // ==================== GET migrados de D2 ====================

    @Test
    void getProjects_retorna200YLista() throws Exception {
        // 3 proyectos sembrados. No asumimos orden (HashMap): solo el tamaño.
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
        // Proyecto 1 tiene 5 tareas (ids 1,2,3,4,9).
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

    // ==================== Tests NUEVOS de escritura (integrador) ====================

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
                .andExpect(jsonPath("$.ownerId").value(1));
    }

    @Test
    void postProject_nombreEnBlanco_devuelve400ConDetalleDeCampo() throws Exception {
        // Ahora sí el DETALLE por campo: el advice ya existe. errors[0] menciona el campo "name".
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
        // Proyecto 2 existe; borrarlo arrastra sus tareas (cascada). 204 sin cuerpo.
        mockMvc.perform(delete("/projects/2"))
                .andExpect(status().isNoContent());
    }
}
