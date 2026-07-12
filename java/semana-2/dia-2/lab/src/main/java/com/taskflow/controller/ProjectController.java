package com.taskflow.controller;

import com.taskflow.service.ProjectService;
// TODO (MP-2): descomenta estos imports tras agregar spring-boot-starter-web.
// import com.taskflow.model.Project;
// import com.taskflow.model.Task;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.PathVariable;
// import org.springframework.web.bind.annotation.RestController;
// import java.util.List;

/**
 * ProjectController — la puerta HTTP de los proyectos. Esqueleto del integrador de hoy.
 *
 * Igual que TaskController: controller delgado, delega en ProjectService, inyección por constructor,
 * cero acceso al repositorio. La distinción importante vive en GET /projects/{id}/tasks:
 * "no existe el proyecto" (404) NO es lo mismo que "el proyecto existe pero no tiene tareas" (200 con []).
 */
// TODO (integrador): añade @RestController sobre la clase.
// @RestController
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    // TODO (integrador): GET /projects -> projectService.listar() (array JSON).
    // @GetMapping("/projects")
    // public List<Project> getProjects() {
    //     return projectService.listar();
    // }

    // TODO (integrador): GET /projects/{id}/tasks
    //   - si el proyecto NO existe (projectService.buscarPorId(id) vacío) -> 404.
    //   - si existe (aunque no tenga tareas) -> 200 con projectService.tareasDe(id) (puede ser []).
    // @GetMapping("/projects/{id}/tasks")
    // public ResponseEntity<List<Task>> getTareasDeProyecto(@PathVariable("id") Long id) {
    //     if (projectService.buscarPorId(id).isEmpty()) {
    //         return ResponseEntity.notFound().build();
    //     }
    //     return ResponseEntity.ok(projectService.tareasDe(id));
    // }
}
