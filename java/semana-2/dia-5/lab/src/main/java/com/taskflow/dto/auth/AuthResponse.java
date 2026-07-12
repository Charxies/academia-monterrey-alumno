package com.taskflow.dto.auth;

/**
 * AuthResponse — la respuesta de POST /auth/login: SOLO el token JWT. El cliente lo guarda y lo manda
 * en el header 'Authorization: Bearer <token>' en cada request siguiente. (En MP-5 el login devuelve
 * el stub AuthResponse("pendiente-jwt"); MP-7 conecta el token real.)
 *
 * Este record NO necesita validación (es SALIDA): va completo tal cual.
 */
public record AuthResponse(String token) {
}
