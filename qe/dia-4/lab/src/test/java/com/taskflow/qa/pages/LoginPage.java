package com.taskflow.qa.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * LoginPage (MP-2) — página de login (index.html).
 *
 * Regla de oro: los asserts NO viven aquí (el page object informa, el test juzga). ÚNICA
 * excepción: el constructor verifica el landmark (input-username visible) — es un guard de
 * navegación, no un assert de negocio.
 *
 * Los data-testid ya están puestos (se copian de la tabla del contrato, no se adivinan).
 * Completa los cuerpos marcados con TODO.
 */
public class LoginPage extends BasePage {

    private static final By INPUT_USERNAME = byTestId("input-username");
    private static final By INPUT_PASSWORD = byTestId("input-password");
    private static final By BTN_LOGIN = byTestId("btn-login");
    private static final By LOGIN_ERROR = byTestId("login-error");
    private static final By SESSION_EXPIRED = byTestId("session-expired-msg");

    public LoginPage(WebDriver driver) {
        super(driver);
        // TODO MP-2: verificar el landmark (waitVisible(INPUT_USERNAME)) — si no aparece,
        //            TimeoutException clara: "no estás en el login".
    }

    /**
     * Punto de ENTRADA: navega al login y devuelve una LoginPage ya verificada.
     * Es estático porque el login no tiene una acción previa que lo abra.
     */
    public static LoginPage open(WebDriver driver) {
        // TODO MP-2: driver.get(Config.baseUrl() + "/index.html"); return new LoginPage(driver);
        throw new UnsupportedOperationException("TODO MP-2");
    }

    /** Login exitoso → NAVEGA a proyectos (regla de retorno 1: devuelve el destino). */
    @Step("Iniciar sesión como {username}")
    public ProjectsPage loginAs(String username, String password) {
        // TODO MP-2: type(INPUT_USERNAME, ...); type(INPUT_PASSWORD, ...); click(BTN_LOGIN);
        //            return new ProjectsPage(driver);
        throw new UnsupportedOperationException("TODO MP-2");
    }

    /** Login que se espera que FALLE → permanece en el login (regla de retorno 2: this). */
    @Step("Intentar login inválido como {username}")
    public LoginPage loginExpectingError(String username, String password) {
        // TODO MP-2: llenar credenciales, click y return this;
        throw new UnsupportedOperationException("TODO MP-2");
    }

    /** login-error NO existe hasta el fallo; aparece tras la respuesta + delay → PRESENCIA. */
    public String errorMessage() {
        // TODO MP-2: return waitPresent(LOGIN_ERROR).getText();
        throw new UnsupportedOperationException("TODO MP-2");
    }

    /** ¿Está visible el banner de sesión expirada? (stretch E9). */
    public boolean sessionExpiredBannerVisible() {
        // TODO stretch E9: driver.findElements(SESSION_EXPIRED) ... anyMatch(isDisplayed)
        throw new UnsupportedOperationException("TODO stretch E9");
    }
}
