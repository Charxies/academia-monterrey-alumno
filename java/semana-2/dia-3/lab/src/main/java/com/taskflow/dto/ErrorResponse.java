package com.taskflow.dto;

import java.time.Instant;
import java.util.List;

/**
 * ErrorResponse — ESQUELETO (MP-7). El cuerpo JSON UNIFORME de todos los errores de la API. Un solo
 * formato para 400/404/422:
 *
 *   { "timestamp": "...", "status": 400, "message": "...", "errors": ["campo: motivo", ...] }
 *
 * 'errors' lleva el detalle POR CAMPO de Bean Validation; en los errores sin detalle de campo (404,
 * 422, JSON malformado) va como lista vacía.
 *
 * Los 4 componentes ya están declarados: tu trabajo es CONSTRUIR este record en cada handler del
 * GlobalExceptionHandler. (Awareness 1 min: Spring trae ProblemDetail/RFC 9457; aquí practicamos el
 * nuestro a mano.)
 */
public record ErrorResponse(
        Instant timestamp,
        int status,
        String message,
        List<String> errors) {
}
