package com.taskflow.exception;

/**
 * TaskStateException — ESQUELETO (MP-9). El request está bien formado, pero el ESTADO del dominio no
 * permite la operación ("pasar a DONE una tarea sin responsable"). El GlobalExceptionHandler la
 * mapeará a 422 Unprocessable Entity.
 *
 * Es UNCHECKED (ya extiende RuntimeException): sube sola hasta el advice sin ensuciar firmas.
 *
 * TODO (MP-9): añade el constructor que recibe el mensaje y llama a super(mensaje):
 *     public TaskStateException(String mensaje) {
 *         super(mensaje);
 *     }
 * Luego, en TaskService.cambiarStatus, CAPTURA la checked TaskValidationException que lanza
 * setStatus y lánzala TRADUCIDA a esta excepción (sin tocar el dominio).
 */
public class TaskStateException extends RuntimeException {
}
