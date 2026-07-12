package com.taskflow.dto.auth;

// TODO T5: importa jakarta.validation.constraints.NotBlank si lo usas.

/**
 * LoginRequest — cuerpo de POST /auth/login (T5). Solo username + password; el service delega en el
 * AuthenticationManager. Credenciales malas -> BadCredentialsException -> 401 vía el advice.
 */
public record LoginRequest(

        // TODO T5: @NotBlank
        String username,

        // TODO T5: @NotBlank
        String password
) {
}
