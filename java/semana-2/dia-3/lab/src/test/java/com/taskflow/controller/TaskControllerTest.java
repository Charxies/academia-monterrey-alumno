package com.taskflow.controller;

import com.taskflow.exception.TaskValidationException;
import com.taskflow.model.Priority;
import com.taskflow.model.Task;
import com.taskflow.model.TaskStatus;
import com.taskflow.service.TaskService;
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
 * TaskControllerTest — tests de la capa web SIN levantar servidor ni curl a mano (T8).
 *
 * @WebMvcTest(TaskController.class) levanta SOLO la capa web (el service real NO existe en ese
 * contexto) y el service se sustituye con @MockitoBean — la anotación CANÓNICA del curso (en
 * tutoriales/Stack Overflow verás @MockBean: es lo mismo con nombre viejo, deprecado desde Boot 3.4).
 * Aquí los mocks entran con motivación real: aislar la capa web. Mockito mínimo: when(...).thenReturn(...).
 *
 * Naming metodo_escenario_resultado (S1D5). AAA.
 */
@WebMvcTest(TaskController.class)
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TaskService taskService;

    /** Helper: construye una tarea de test. Los datos son válidos, así que la checked no ocurre. */
    private static Task tarea(Long id, String title, TaskStatus status, Priority priority) {
        try {
            return new Task(id, title, "desc", status, priority, 1L, 7L, LocalDate.now().plusDays(3));
        } catch (TaskValidationException e) {
            throw new IllegalStateException("Datos de test inválidos", e);
        }
    }

    @Test
    void getTasks_sinFiltro_retorna200YLista() throws Exception {
        when(taskService.listar()).thenReturn(List.of(
                tarea(1L, "Diseñar esquema de BD", TaskStatus.TODO, Priority.HIGH),
                tarea(2L, "Configurar Spring Boot", TaskStatus.DONE, Priority.MED)));

        mockMvc.perform(get("/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].title").value("Diseñar esquema de BD"));
    }

    @Test
    void getTasks_conFiltroStatus_retornaSoloEseEstado() throws Exception {
        when(taskService.porEstado(TaskStatus.DONE)).thenReturn(List.of(
                tarea(2L, "Configurar Spring Boot", TaskStatus.DONE, Priority.MED)));

        mockMvc.perform(get("/tasks").param("status", "DONE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].status").value("DONE"));
    }

    @Test
    void getTaskPorId_existente_retorna200ConElTitulo() throws Exception {
        when(taskService.buscarPorId(1L)).thenReturn(
                Optional.of(tarea(1L, "Diseñar esquema de BD", TaskStatus.TODO, Priority.HIGH)));

        mockMvc.perform(get("/tasks/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Diseñar esquema de BD"));
    }

    @Test
    void getTaskPorId_inexistente_retorna404() throws Exception {
        when(taskService.buscarPorId(999L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/tasks/999"))
                .andExpect(status().isNotFound());
    }

    // ================================================================================================
    // TODO (MP-6): MIGRA esta clase del slice @WebMvcTest a CONTEXTO COMPLETO. Reemplaza la anotación
    // de arriba (@WebMvcTest(TaskController.class)) por:
    //
    //     @SpringBootTest
    //     @AutoConfigureMockMvc
    //     class TaskControllerTest {
    //         @Autowired private MockMvc mockMvc;
    //         @Autowired private TaskRepository taskRepository;
    //         @Autowired private ProjectRepository projectRepository;   // tras extraer la interfaz
    //         @Autowired private DataSeeder dataSeeder;
    //
    //         @BeforeEach
    //         void limpiarYResembrar() throws Exception {
    //             taskRepository.findAll().forEach(t -> taskRepository.deleteById(t.getId()));
    //             projectRepository.findAll().forEach(p -> projectRepository.deleteById(p.getId()));
    //             dataSeeder.run();   // independencia de tests: repos singleton (punto de dolor 9)
    //         }
    //         ...
    //
    // Borra el @MockitoBean y los when(...); adapta los asserts de los GET a la SEMILLA del DataSeeder
    // (p. ej. 9 tareas, la vencida "Corregir bug de fechas" de primera por POR_URGENCIA; 2 DONE).
    // Luego implementa los 3 tests de escritura de abajo (hoy son cuerpos vacíos que "pasan": el test
    // verde mentiroso de S1D5 — no valen hasta que los llenes).
    // ================================================================================================

    @Test
    void postTask_valida_devuelve201ConLocation() {
        // TODO (MP-6): POST /projects/1/tasks con body JSON válido (text block) ->
        //   status 201, header Location con patrón .*/tasks/\\d+, jsonPath $.title y $.status = "TODO".
    }

    @Test
    void postTask_tituloCorto_devuelve400() {
        // TODO (MP-6): POST /projects/1/tasks con {"title":"ab","priority":"LOW"} -> status 400.
        //   (Por ahora asserta solo el status; el detalle por campo se prueba en ProjectControllerTest.)
    }

    @Test
    void deleteTask_inexistente_devuelve404() {
        // TODO (integrador, paso 7): DELETE /tasks/999 -> status 404 (uniforme, del advice).
    }
}
