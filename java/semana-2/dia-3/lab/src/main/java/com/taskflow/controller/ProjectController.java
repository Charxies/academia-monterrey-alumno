package com.taskflow.controller;

import com.taskflow.model.Project;
import com.taskflow.model.Task;
import com.taskflow.service.ProjectService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * ProjectController — la puerta HTTP de los proyectos (integrador S2D2).
 *
 * Mismo contrato que TaskController: delgado, delega en ProjectService, inyección por constructor,
 * cero acceso al repositorio. La sutileza vive en GET /projects/{id}/tasks (ver abajo).
 *
 * TODO (integrador): completa el CRUD y refactoriza a DTOs (repetición del patrón de TaskController) —
 *   - Refactoriza GET /projects a List<ProjectResponse> y GET /projects/{id}/tasks a
 *     List<TaskResponse> (ProjectMapper/TaskMapper). El 404 pasa a orElseThrow(new
 *     ProjectNotFoundException(id)) -> advice; conserva la distinción 404 vs [] (200).
 *   - GET /projects/{id}     -> 200 con ProjectResponse (404 si no existe).
 *   - POST /projects         -> 201 + Location a /projects/{id}. @Valid en el body.
 *   - PUT /projects/{id}     -> 200 con ProjectResponse (reemplazo completo).
 *   - DELETE /projects/{id}  -> 204 (cascada manual a sus tareas en el service).
 *   - Añade @Tag(name="Projects") y @Operation cuando actives springdoc (MP-8).
 */
@RestController
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    /** GET /projects — todos los proyectos (array JSON). */
    @GetMapping("/projects")
    public List<Project> getProjects() {
        return projectService.listar();
    }

    /**
     * GET /projects/{id}/tasks — las tareas de un proyecto.
     *
     * Distinción explícita del enunciado ("no hay proyecto" != "proyecto vacío"):
     *   - el proyecto NO existe          -> 404 (buscarPorId vacío).
     *   - el proyecto existe, sin tareas -> 200 con [] (tareasDe devuelve lista vacía).
     */
    @GetMapping("/projects/{id}/tasks")
    public ResponseEntity<List<Task>> getTareasDeProyecto(@PathVariable("id") Long id) {
        if (projectService.buscarPorId(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(projectService.tareasDe(id));
    }
}
