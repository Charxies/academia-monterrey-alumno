package com.taskflow.dto;

import com.taskflow.model.Priority;

import java.time.LocalDate;

/**
 * TaskRequest — ESQUELETO (MP-2). El CONTRATO de ENTRADA para crear (POST) y reemplazar (PUT) una
 * tarea. Es un record: inmutable, constructor canónico; Jackson lo entiende nativo.
 *
 * Qué NO viaja aquí (decisiones de API fijadas por la spec):
 *   - 'id':        lo asigna el repositorio.
 *   - 'status':    la tarea nace en TODO; solo cambia por PATCH /tasks/{id}/status.
 *   - 'projectId': viene del PATH al crear (POST /projects/{projectId}/tasks).
 *
 * TODO (MP-4): valida en la frontera con Bean Validation (solo las reglas de FORMA):
 *   - title:    @NotBlank + @Size(min = 3, max = 120) con message en español.
 *   - priority: @NotNull.
 *   - assigneeId y dueDate: SIN anotaciones (opcionales; la regla "dueDate no en el pasado" es del
 *     dominio y solo AL CREAR — por eso NO va @FutureOrPresent).
 */
public record TaskRequest(
        String title,
        String description,
        Priority priority,
        Long assigneeId,
        LocalDate dueDate) {
}
