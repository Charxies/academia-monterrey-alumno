package com.taskflow.qa.api.dto;

/**
 * RegisterRequest — cuerpo de POST /auth/register (lado CLIENTE de la prueba).
 *
 * Es un record de Java usado como DTO de test: sus records de S2 pagan otra vez, ahora del lado
 * del que CONSUME la API. RestAssured lo serializa a JSON con Jackson (por eso jackson-databind
 * está en el pom: aquí NO hay Spring que lo arrastre). Anti-patrón que NO usamos: concatenar el
 * JSON a mano ("{\"username\":...}").
 *
 * Campos EXACTOS del contrato de la API (S2D5 RegisterRequest): username, email, password.
 */
public record RegisterRequest(String username, String email, String password) {
}
