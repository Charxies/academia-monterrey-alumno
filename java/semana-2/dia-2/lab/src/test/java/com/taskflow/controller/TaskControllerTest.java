package com.taskflow.controller;

// TODO (MP-2): tras agregar spring-boot-starter-web, descomenta TODO este bloque de imports.
// import com.taskflow.model.Priority;
// import com.taskflow.model.Task;
// import com.taskflow.model.TaskStatus;
// import com.taskflow.service.TaskService;
// import org.junit.jupiter.api.Test;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
// import org.springframework.test.context.bean.override.mockito.MockitoBean;
// import org.springframework.test.web.servlet.MockMvc;
// import java.util.List;
// import java.util.Optional;
// import static org.mockito.Mockito.when;
// import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * TaskControllerTest — tests MockMvc de la capa web (MP-9 + integrador).
 *
 * PATRÓN (T8): @WebMvcTest(TaskController.class) levanta SOLO la capa web (no el service real) y el
 * service se sustituye con @MockitoBean (anotación CANÓNICA del curso; en tutoriales viejos verás
 * @MockBean, deprecado desde Boot 3.4 — misma idea). Con Mockito mínimo: when(...).thenReturn(...).
 *
 * Cómo activarlo: tras MP-2, descomenta los imports, la anotación @WebMvcTest sobre la clase, el
 * MockMvc, el @MockitoBean, y escribe cada método con su @Test.
 */
// @WebMvcTest(TaskController.class)
class TaskControllerTest {

    // @Autowired
    // private MockMvc mockMvc;

    // @MockitoBean
    // private TaskService taskService;

    // TODO (MP-9): mock con 2 tareas (when(taskService.listar()).thenReturn(...)); perform get("/tasks");
    //   andExpect status().isOk(), jsonPath("$.length()").value(2), jsonPath("$[0].title")...
    void getTasks_conTareas_retorna200YJson() {
        // TODO (MP-9)
    }

    // TODO (MP-9): when(taskService.buscarPorId(999L)).thenReturn(Optional.empty());
    //   perform get("/tasks/999"); andExpect status().isNotFound().
    // ERROR INTENCIONAL #3: escríbelo primero con typo /task/999 -> esperas 200, recibe 404;
    //   agrega .andDo(print()) y lee el MvcResult (la URI que SÍ se pidió vs el @GetMapping que existe).
    void getTaskPorId_inexistente_retorna404() {
        // TODO (MP-9)
    }

    // TODO (integrador): sin filtro -> lista completa.
    void getTasks_sinFiltro_retorna200YLista() {
        // TODO (integrador)
    }

    // TODO (integrador): ?status=DONE -> solo tareas DONE (when(taskService.porEstado(TaskStatus.DONE))...).
    void getTasks_conFiltroStatus_retornaSoloEseEstado() {
        // TODO (integrador)
    }

    // TODO (integrador): /tasks/{id} existente -> 200 y jsonPath("$.title") es el esperado.
    void getTaskPorId_existente_retorna200ConElTitulo() {
        // TODO (integrador)
    }
}
