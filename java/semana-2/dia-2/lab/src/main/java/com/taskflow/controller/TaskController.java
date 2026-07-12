package com.taskflow.controller;

import com.taskflow.service.TaskService;
// TODO (MP-2): descomenta estos imports tras agregar spring-boot-starter-web.
// import com.taskflow.model.Task;
// import com.taskflow.model.TaskStatus;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.PathVariable;
// import org.springframework.web.bind.annotation.RequestParam;
// import org.springframework.web.bind.annotation.RestController;
// import java.util.List;

/**
 * TaskController — la PUERTA HTTP de las tareas. Esqueleto de hoy (MP-4 / MP-5 / MP-7 + integrador).
 *
 * Contrato de responsabilidades (T6): el controller NO piensa. Traduce HTTP <-> dominio (rutas,
 * params, códigos) y delega TODO al TaskService. Nunca habla con el repositorio (grep repository en
 * controller/ debe dar 0). Inyección por constructor, igual que ayer (no re-explicamos DI).
 *
 * El archivo compila hoy con solo el campo + constructor; los métodos que usan spring-web están
 * comentados hasta MP-2.
 */
// TODO (MP-4): añade @RestController sobre la clase.
// @RestController
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    // TODO (MP-4): GET /tasks -> taskService.listar() (array JSON con la semilla del DataSeeder).
    // TODO (MP-5): filtro opcional ?status=. El controller DECIDE: si status es null -> listar();
    //             si trae valor -> taskService.porEstado(status). La conversión String->enum es
    //             automática (?status=DONE -> TaskStatus.DONE) y un valor que no matchea da 400.
    // @GetMapping("/tasks")
    // public List<Task> getTasks(@RequestParam(name = "status", required = false) TaskStatus status) {
    //     if (status == null) {
    //         return taskService.listar();
    //     }
    //     return taskService.porEstado(status);
    // }

    // TODO (MP-7): GET /tasks/{id} con ResponseEntity y códigos correctos.
    //   200 con la tarea si existe, 404 LIMPIO si no. El Optional del service obliga a decidir:
    //   return taskService.buscarPorId(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    //   (Prohibido Optional.get().)
    // @GetMapping("/tasks/{id}")
    // public ResponseEntity<Task> getTaskPorId(@PathVariable("id") Long id) {
    //     return taskService.buscarPorId(id)
    //             .map(ResponseEntity::ok)
    //             .orElse(ResponseEntity.notFound().build());
    // }
}
