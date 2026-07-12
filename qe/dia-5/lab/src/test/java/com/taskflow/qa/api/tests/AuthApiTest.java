package com.taskflow.qa.api.tests;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.fail;

// TODO (MP-8): etiqueta esta clase con @Tag("ci") para que corra en el pipeline (mvn test -Dgroups=ci).
//             Imports que vas a necesitar:
//               import static io.restassured.RestAssured.given;
//               import static org.hamcrest.Matchers.*;   // equalTo, notNullValue, ...
//               import com.taskflow.qa.api.ApiSpecs;  com.taskflow.qa.api.AuthClient;
//               import com.taskflow.qa.api.dto.RegisterRequest;  com.taskflow.qa.api.dto.LoginRequest;

/**
 * AuthApiTest — el flujo de autenticación por API (MP-2 y MP-4). Register (201), login OK (200 +
 * token) y login con password mala (401). Usa siempre given().spec(ApiSpecs.base()) y
 * .log().ifValidationFails().
 */
class AuthApiTest {

    /** TODO (MP-4): POST /auth/register con un username único (UUID) -> 201; body id notNullValue,
     *  username equalTo(el que mandaste). */
    @Test
    @DisplayName("register: 201 y devuelve el usuario creado (sin password)")
    void registerDevuelve201() {
        fail("TODO MP-4: POST /auth/register -> 201");
    }

    /** TODO (MP-4): crea una cuenta (AuthClient.cuentaNueva) y haz POST /auth/login -> 200; el
     *  body debe traer token notNullValue(). */
    @Test
    @DisplayName("login OK: 200 y token presente")
    void loginOkDevuelveToken() {
        fail("TODO MP-4: POST /auth/login -> 200 + token");
    }

    /** TODO (MP-2/MP-6): POST /auth/login con la password equivocada -> 401 (el primer assert de
     *  seguridad del día: tu filtro JWT de S2D5 funcionando). */
    @Test
    @DisplayName("login con password mala: 401")
    void loginPasswordMalaDa401() {
        fail("TODO MP-6: login con password mala -> 401");
    }
}
