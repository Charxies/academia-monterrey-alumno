package com.taskflow.controller;

import com.taskflow.exception.TaskValidationException;
import com.taskflow.model.Priority;
import com.taskflow.model.Project;
import com.taskflow.model.Role;
import com.taskflow.model.Task;
import com.taskflow.model.TaskStatus;
import com.taskflow.model.User;
import com.taskflow.service.ProjectService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * ProjectControllerTest — la capa web del lado Project (integrador). Mismo patrón que
 * TaskControllerTest: @WebMvcTest(ProjectController.class) + @MockitoBean ProjectService.
 *
 * Cubre la distinción clave del enunciado en GET /projects/{id}/tasks:
 * "no existe el proyecto" (404) NO es lo mismo que "proyecto sin tareas" (200 con []).
 */
@WebMvcTest(ProjectController.class)
class ProjectControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProjectService projectService;

    private static Project proyecto(long id, String name) {
        User owner = new User(1L, "ana", "ana@taskflow.dev", Role.ADMIN);
        return new Project(id, name, "desc", owner, LocalDate.now().minusDays(10));
    }

    private static Task tarea(Long id, String title) {
        try {
            return new Task(id, title, "desc", TaskStatus.TODO, Priority.MED, 1L, 7L, null);
        } catch (TaskValidationException e) {
            throw new IllegalStateException("Datos de test inválidos", e);
        }
    }

    @Test
    void getProjects_retorna200YLista() throws Exception {
        when(projectService.listar()).thenReturn(List.of(
                proyecto(1L, "Plataforma TaskFlow"),
                proyecto(2L, "App Móvil")));

        mockMvc.perform(get("/projects"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Plataforma TaskFlow"));
    }

    @Test
    void getTareasDeProyecto_proyectoInexistente_retorna404() throws Exception {
        when(projectService.buscarPorId(999L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/projects/999/tasks"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getTareasDeProyecto_proyectoExistenteConTareas_retorna200() throws Exception {   // STRETCH (caso feliz)
        when(projectService.buscarPorId(1L)).thenReturn(Optional.of(proyecto(1L, "Plataforma TaskFlow")));
        when(projectService.tareasDe(1L)).thenReturn(List.of(tarea(1L, "Diseñar esquema de BD")));

        mockMvc.perform(get("/projects/1/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].title").value("Diseñar esquema de BD"));
    }

    @Test
    void getTareasDeProyecto_proyectoExistenteSinTareas_retorna200ConListaVacia() throws Exception {
        when(projectService.buscarPorId(3L)).thenReturn(Optional.of(proyecto(3L, "Migración Legacy")));
        when(projectService.tareasDe(3L)).thenReturn(List.of());

        mockMvc.perform(get("/projects/3/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    // ================================================================================================
    // TODO (MP-6 + integrador, paso 7): MIGRA esta clase igual que TaskControllerTest (@SpringBootTest
    // + @AutoConfigureMockMvc + @BeforeEach de limpieza/resiembra), adapta los GET a la semilla (3
    // proyectos; proyecto 1 con 5 tareas; proyecto 3 con 0) e implementa los 3 tests de escritura
    // (hoy cuerpos vacíos que "pasan"):
    // ================================================================================================

    @Test
    void postProject_valido_devuelve201ConLocation() {
        // TODO: POST /projects con {"name":"...","description":"..."} -> 201, Location .*/projects/\\d+.
    }

    @Test
    void postProject_nombreEnBlanco_devuelve400ConDetalleDeCampo() {
        // TODO: POST /projects con {"name":"","description":"..."} -> 400 y $.errors[0] contiene "name"
        //   (ahora sí el detalle por campo: el advice ya existe).
    }

    @Test
    void deleteProject_existente_devuelve204() {
        // TODO: DELETE /projects/2 -> 204 (y su cascada borra las tareas del proyecto 2).
    }
}
