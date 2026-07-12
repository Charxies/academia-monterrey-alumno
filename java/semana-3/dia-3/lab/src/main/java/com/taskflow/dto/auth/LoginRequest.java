package com.taskflow.dto.auth;

import jakarta.validation.constraints.NotBlank;

/**
 * LoginRequest — cuerpo de POST /auth/login (T5). Solo username + password; el service delega en el
 * AuthenticationManager. Credenciales malas -> BadCredentialsException -> 401 vía el advice.
 */
public record LoginRequest(

        @NotBlank(message = "El username es obligatorio.")
        String username,

        @NotBlank(message = "El password es obligatorio.")
        String password
) {
}
