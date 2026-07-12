package com.taskflow.dto.auth;

// TODO T5: importa las anotaciones de Bean Validation que uses (jakarta.validation.constraints.*).

/**
 * RegisterRequest — cuerpo de POST /auth/register (T5). El password en CLARO viaja UNA vez y muere
 * aquí: el service lo hashea con BCrypt y jamás se devuelve. Reusa Bean Validation de D3 (@Valid en el
 * controller -> 400 con detalle por campo si falla).
 */
public record RegisterRequest(

        // TODO T5: @NotBlank + @Size(min = 3, max = 50) con mensajes en español
        String username,

        // TODO T5: @NotBlank + @Email
        String email,

        // TODO T5: @NotBlank + @Size(min = 6, max = 100)
        String password
) {
}
