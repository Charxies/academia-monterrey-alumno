package com.taskflow.qa.tests;

import com.taskflow.qa.pages.LoginPage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * LoginTest — el POM en acción: el test se lee como el caso de uso.
 *
 * Los asserts viven AQUÍ (el page object informa, el test juzga). Compara contra textos
 * EXACTOS del contrato de la UI (nav-username = el usuario; login-error = «Credenciales
 * inválidas»).
 */
class LoginTest extends BaseTest {

    @Test
    @DisplayName("login OK: entra a proyectos y el navbar muestra al usuario")
    void loginOk() {
        String usuario = LoginPage.open(getDriver())
                .loginAs("demo", "Demo123!")
                .navUsername();

        assertEquals("demo", usuario, "el navbar debería mostrar el usuario logueado");
    }

    /**
     * @ParameterizedTest + @CsvSource — refuerzo de Java S3D1, ahora sobre UI (MP-8).
     * Mismo patrón, otro sujeto de prueba: 3 formas de credencial inválida, un único mensaje.
     */
    @ParameterizedTest(name = "login inválido: {0}")
    @CsvSource({
            "demo,                password-incorrecta",
            "usuario-inexistente, Demo123!",
            "demo,                DEMO123!"
    })
    @DisplayName("login inválido muestra el mensaje exacto «Credenciales inválidas»")
    void loginInvalido(String usuario, String password) {
        String error = LoginPage.open(getDriver())
                .loginExpectingError(usuario, password)
                .errorMessage();

        assertEquals("Credenciales inválidas", error);
    }
}
