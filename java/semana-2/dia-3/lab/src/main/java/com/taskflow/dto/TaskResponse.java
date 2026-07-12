package com.taskflow.dto;

import com.taskflow.model.Priority;
import com.taskflow.model.TaskStatus;

import java.time.LocalDate;

/**
 * TaskResponse — ESQUELETO (MP-2). El CONTRATO de SALIDA de una tarea (lo que la API devuelve). Es un
 * record; Jackson lo serializa por sus componentes.
 *
 * Trae los 8 campos de la tarea. "¿Si son los mismos que la entidad, para qué?" — hoy son casi
 * iguales; el valor es el DESACOPLE: en D4 la entidad gana anotaciones JPA y en D5 aparecen campos
 * que jamás deben salir (el passwordHash de User). El contrato de salida no cambia por eso.
 *
 * Los 8 componentes ya están declarados: tu trabajo es MAPEAR la entidad a este record en TaskMapper.
 */
public record TaskResponse(
        Long id,
        String title,
        String description,
        TaskStatus status,
        Priority priority,
        Long projectId,
        Long assigneeId,
        LocalDate dueDate) {
}
