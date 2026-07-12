package com.taskflow.qa.api;

/**
 * AuthClient — el helper de autenticación de la capa API (MP-4). Encapsula register -> login -> token.
 *
 * OBJETIVO (MP-4): que cada clase de test obtenga un token FRESCO de un usuario ÚNICO. Ésa es la
 * semilla del aislamiento de datos que salva el CI: ningún test asume estado previo.
 *
 * PISTAS:
 *   - registrar(...): POST /auth/register con RegisterRequest -> 201 (body: id/username/email/role).
 *   - login(...):     POST /auth/login con LoginRequest -> 200; extrae el token con
 *                     extract().path("token").
 *   - cuentaNueva():  arma un username ÚNICO (sufijo UUID: java.util.UUID.randomUUID()), registra y
 *                     loguea; devuelve username + password + token (la capa UI necesita las credenciales).
 *   - tokenNuevo():   atajo que solo devuelve el token de cuentaNueva().
 *   - Usa given().spec(ApiSpecs.base())... y .log().ifValidationFails() (estándar del framework).
 */
public class AuthClient {

    /** Password por defecto de los usuarios de prueba (>= 6 chars: contrato de la API). */
    private static final String PASSWORD = "Passw0rd!";

    /** Credenciales + token de un usuario recién creado (lo que la capa UI necesita para loguear). */
    public record Cuenta(String username, String password, String token) {
    }

    /** TODO (MP-4): POST /auth/register (201) y devolver el username creado. */
    public String registrar(String username, String email, String password) {
        // TODO: given().spec(ApiSpecs.base()).body(new RegisterRequest(...)).when().post("/auth/register")
        //       .then().log().ifValidationFails().statusCode(201);
        return null;
    }

    /** TODO (MP-4): POST /auth/login (200) y extraer el token. */
    public String login(String username, String password) {
        // TODO: ...post("/auth/login").then().statusCode(200).extract().path("token");
        return null;
    }

    /** TODO (MP-4): username único (UUID) -> registrar -> login -> devolver Cuenta(username, password, token). */
    public Cuenta cuentaNueva() {
        // TODO: String username = "qa_" + UUID.randomUUID().toString().replace("-", "");
        return null;
    }

    /** TODO (MP-4): atajo -> cuentaNueva().token(). */
    public String tokenNuevo() {
        // TODO: return cuentaNueva().token();
        return null;
    }
}
