package com.taskflow.qa.api.dto;

/**
 * LoginRequest — cuerpo de POST /auth/login. Solo username + password (contrato S2D5).
 * La respuesta trae el token (AuthResponse.token) que se pega en 'Authorization: Bearer ...'.
 */
public record LoginRequest(String username, String password) {
}
