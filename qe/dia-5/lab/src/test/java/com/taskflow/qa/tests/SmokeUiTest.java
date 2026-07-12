package com.taskflow.qa.tests;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.fail;

// TODO (MP-8): etiqueta esta clase con @Tag("ci") (corre headless en CI, headed local).

/**
 * SmokeUiTest — clase NUEVA de hoy (no existe en D4). El smoke UI mínimo del pipeline, derivado del
 * LoginTest de D4: login por UI y verificar que el navbar muestra al usuario.
 *
 * LA SINERGIA DE LAS DOS CAPAS (integrador req. 2): en vez de depender de la semilla (seed.sh) como
 * el resto de la suite UI de D4, SIEMBRA su usuario POR API con AuthClient en un @BeforeAll y luego
 * hace login POR UI con ESE usuario. Por eso funciona contra la H2 vacía del CI. Extiende BaseTest
 * (mismo ciclo de vida del driver que D4); el @BeforeAll es estático y siembra ANTES de crear el
 * navegador (la siembra es 100% por API, sin driver).
 *
 * PISTAS:
 *   - @BeforeAll static: AuthClient.Cuenta cuenta = new AuthClient().cuentaNueva(); guarda
 *     username y password en campos static.
 *   - En el test: LoginPage.open(getDriver()).loginAs(username, password).navUsername()
 *     y assertEquals(username, mostrado).
 */
class SmokeUiTest extends BaseTest {

    // TODO (integrador): private static String username; private static String password;

    // TODO (integrador): @BeforeAll static void sembrarUsuarioPorApi() { ... cuentaNueva() ... }

    @Test
    @DisplayName("smoke UI: login por UI con un usuario sembrado por API")
    void loginPorUiConUsuarioSembradoPorApi() {
        fail("TODO integrador: sembrar usuario por API (@BeforeAll) y hacer login por UI");
    }
}
