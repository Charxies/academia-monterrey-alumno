package com.taskflow.mapper;

/**
 * TaskMapper — ESQUELETO (MP-2). El puente entre el CONTRATO (DTOs) y el DOMINIO (entidad Task).
 * Mapeo A MANO con métodos estáticos: barato, visible, testeable. Clase de utilidades: constructor
 * privado (no se instancia). Mención 1 min: MapStruct automatiza esto en el mundo real.
 *
 * TODO (MP-2 y MP-3): implementa estos métodos estáticos —
 *   1) aEntidadNueva(TaskRequest req, Long projectId) throws TaskValidationException
 *        -> Task.crear(req.title(), req.description(), req.priority(), req.dueDate(), projectId, req.assigneeId())
 *        (pasa por la FACTORY: status=TODO, id=null, regla "dueDate no en el pasado").
 *   2) aReemplazo(Long id, TaskStatus statusActual, Long projectIdActual, TaskRequest req) throws TaskValidationException
 *        -> new Task(id, req.title(), req.description(), statusActual, req.priority(), projectIdActual, req.assigneeId(), req.dueDate())
 *        (CONSTRUCTOR de rehidratación: conserva id, status y projectId actuales).
 *   3) aResponse(Task t)
 *        -> new TaskResponse(t.getId(), t.getTitle(), t.getDescription(), t.getStatus(),
 *                            t.getPriority(), t.getProjectId(), t.getAssigneeId(), t.getDueDate())
 */
public final class TaskMapper {

    private TaskMapper() {
        // no instanciable
    }
}
