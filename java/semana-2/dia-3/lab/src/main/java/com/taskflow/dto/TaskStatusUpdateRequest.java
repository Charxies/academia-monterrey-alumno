package com.taskflow.dto;

import com.taskflow.model.TaskStatus;

/**
 * TaskStatusUpdateRequest — ESQUELETO (MP-9). El cuerpo del PATCH /tasks/{id}/status. Un DTO por
 * OPERACIÓN: el único campo que este endpoint puede tocar es el estado.
 *
 * TODO (MP-9): valida 'status' con @NotNull (message en español). OJO: {"status":"FINISHED"} (valor
 * que no matchea el enum) NUNCA llega a @NotNull; truena antes, en el parseo de Jackson
 * (HttpMessageNotReadableException -> 400).
 */
public record TaskStatusUpdateRequest(
        TaskStatus status) {
}
