package com.taskflow.controller;

// TODO (MP-2): tras agregar spring-boot-starter-web, descomenta TODO este bloque de imports.
// import com.taskflow.model.Project;
// import com.taskflow.model.Role;
// import com.taskflow.model.User;
// import com.taskflow.service.ProjectService;
// import org.junit.jupiter.api.Test;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
// import org.springframework.test.context.bean.override.mockito.MockitoBean;
// import org.springframework.test.web.servlet.MockMvc;
// import java.time.LocalDate;
// import java.util.List;
// import java.util.Optional;
// import static org.mockito.Mockito.when;
// import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * ProjectControllerTest — tests MockMvc del lado Project (integrador). Mismo patrón que
 * TaskControllerTest: @WebMvcTest(ProjectController.class) + @MockitoBean ProjectService.
 */
// @WebMvcTest(ProjectController.class)
class ProjectControllerTest {

    // @Autowired
    // private MockMvc mockMvc;

    // @MockitoBean
    // private ProjectService projectService;

    // TODO (integrador): when(projectService.listar()).thenReturn(...); perform get("/projects");
    //   andExpect status().isOk() + jsonPath("$.length()") / "$[0].name".
    void getProjects_retorna200YLista() {
        // TODO (integrador)
    }

    // TODO (integrador): when(projectService.buscarPorId(999L)).thenReturn(Optional.empty());
    //   perform get("/projects/999/tasks"); andExpect status().isNotFound().
    void getTareasDeProyecto_proyectoInexistente_retorna404() {
        // TODO (integrador)
    }
}
