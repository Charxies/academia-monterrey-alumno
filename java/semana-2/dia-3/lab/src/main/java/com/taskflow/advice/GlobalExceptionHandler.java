package com.taskflow.advice;

import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * GlobalExceptionHandler — ESQUELETO (MP-7). El INTERCEPTOR GLOBAL de excepciones de la API.
 * Centraliza en un solo lugar lo que hoy está disperso: el 404 a mano en dos controllers, el 400
 * default feo de Boot y la TaskValidationException dando 500.
 *
 * @RestControllerAdvice = @ControllerAdvice + @ResponseBody. Cada @ExceptionHandler ataja un tipo (o
 * una lista) y lo traduce al ErrorResponse uniforme con su status. Spring elige el handler MÁS
 * ESPECÍFICO declarado.
 *
 * TODO (MP-7): añade los handlers (con un helper privado que construya el ErrorResponse):
 *   - @ExceptionHandler({TaskNotFoundException.class, ProjectNotFoundException.class}) -> 404
 *   - @ExceptionHandler(TaskValidationException.class)                                 -> 400
 *   - @ExceptionHandler(MethodArgumentNotValidException.class)                         -> 400 con
 *       errors[] por campo: ex.getBindingResult().getFieldErrors() + stream map a "campo: mensaje".
 *   - @ExceptionHandler(HttpMessageNotReadableException.class)                         -> 400
 *       ("JSON malformado o valor ilegible").
 *   - @ExceptionHandler(TaskStateException.class)                                      -> 422 (MP-9)
 *
 * Error intencional #5 (el advice tragón): NO declares un handler de Exception.class "por si acaso"
 * — se tragaría los 400/404 que Spring ya lanza y los volvería 500. Maneja lo que CONOCES.
 *
 * STRETCH: @ExceptionHandler(MethodArgumentTypeMismatchException.class) -> 400 (GET /tasks/abc).
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
}
