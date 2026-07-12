package com.taskflow.exception;

/**
 * ProjectNotFoundException — ESQUELETO (integrador, paso 4). Se lanza cuando se busca un proyecto por
 * id y NO existe. El GlobalExceptionHandler la mapeará a 404, en el MISMO handler que
 * TaskNotFoundException (un @ExceptionHandler con dos tipos).
 *
 * Gemela de TaskNotFoundException (S1D4): UNCHECKED (ya extiende RuntimeException).
 *
 * TODO (integrador, paso 4): copia la forma de TaskNotFoundException —
 *   - un campo 'private final Long id;'
 *   - constructor: super("No existe proyecto con id " + id + ".") y guarda el id
 *   - getter getId()
 * La usarán ProjectService (GET/PUT/DELETE) y el cableo POST /projects/{id}/tasks (paso 5).
 */
public class ProjectNotFoundException extends RuntimeException {
}
