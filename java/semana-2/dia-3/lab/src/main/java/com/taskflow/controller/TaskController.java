package com.taskflow.controller;

import com.taskflow.model.Priority;
import com.taskflow.model.Task;
import com.taskflow.model.TaskStatus;
import com.taskflow.service.TaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * TaskController — la PUERTA HTTP de las tareas (endpoints de lectura de S2D2).
 *
 * Contrato de responsabilidades (T6): el controller NO piensa. Traduce HTTP <-> dominio (rutas,
 * params, códigos) y delega TODO al TaskService; nunca habla con el repositorio (grep repository en
 * controller/ = 0). Inyección por constructor, como ayer.
 *
 * ESTADO DE PARTIDA (D2): estos GET exponen la ENTIDAD Task tal cual (el JSON depende de los getters).
 * HOY se cobra ese error sembrado: la entidad deja de salir por la puerta y aparecen los verbos de
 * escritura.
 *
 * TODO (MP-2..MP-9): completa el CRUD y refactoriza a DTOs —
 *   - Inyecta también ProjectService (para validar que el proyecto exista al crear una tarea).
 *   - Refactoriza los GET a List<TaskResponse> / TaskResponse (usa TaskMapper.aResponse; el 404 de
 *     GET /tasks/{id} pasa a orElseThrow(() -> new TaskNotFoundException(id)) -> advice).
 *   - POST /projects/{projectId}/tasks  -> 201 + header Location a /tasks/{id}
 *       (ServletUriComponentsBuilder.fromCurrentContextPath().path("/tasks/{id}")). @Valid en el body.
 *       Valida que el proyecto exista (404 si no). La checked TaskValidationException se DECLARA con
 *       throws y sube al advice (400) — no la atrapes aquí.
 *   - PUT /tasks/{id}                   -> 200 con TaskResponse (reemplazo completo).
 *   - DELETE /tasks/{id}                -> 204 No Content (204; inexistente -> 404 vía advice).
 *   - PATCH /tasks/{id}/status          -> 200; a DONE sin assignee -> 422 (MP-9).
 *   - Añade @Tag(name="Tasks") y @Operation en los endpoints clave cuando actives springdoc (MP-8).
 */
@RestController
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    /**
     * GET /tasks — todas las tareas, o filtradas por ?status= / ?priority= (ambos opcionales).
     *
     * La conversión String->enum es automática (?status=DONE -> TaskStatus.DONE); un valor que no
     * matchea el enum es case-sensitive y da 400 (el mensaje "bonito" llega en D3 con @ControllerAdvice).
     * El controller solo DECIDE a qué método del service llamar; el filtro (la lógica) vive en el service.
     */
    @GetMapping("/tasks")
    public List<Task> getTasks(
            @RequestParam(name = "status", required = false) TaskStatus status,
            @RequestParam(name = "priority", required = false) Priority priority) {   // priority: STRETCH
        if (status != null) {
            return taskService.porEstado(status);
        }
        if (priority != null) {                    // STRETCH: mismo patrón que ?status=
            return taskService.porPrioridad(priority);
        }
        return taskService.listar();
    }

    /**
     * GET /tasks/{id} — 200 con la tarea si existe, 404 LIMPIO si no.
     *
     * El Optional del service se traduce a HTTP con map/orElse: si hay tarea, ResponseEntity.ok(tarea)
     * (200); si está vacío, ResponseEntity.notFound().build() (404, sin stack trace). Prohibido .get().
     */
    @GetMapping("/tasks/{id}")
    public ResponseEntity<Task> getTaskPorId(@PathVariable("id") Long id) {
        return taskService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
