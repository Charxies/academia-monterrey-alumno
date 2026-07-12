package com.taskflow.qa.pages;

import com.taskflow.qa.utils.Config;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * LoginPage — página de login (index.html) de TaskFlow.
 *
 * Métodos públicos = ACCIONES de usuario con nombre de negocio (loginAs, no
 * fillUsernameAndClick). Los asserts NO viven aquí (el page object informa, el test juzga).
 *
 * Excepción a la regla de oro (discutida en MP-2): el CONSTRUCTOR verifica el landmark
 * (input-username visible). No es un assert de negocio: es un guard de navegación que
 * falla rápido con TimeoutException clara ("no estás en la página que crees").
 */
public class LoginPage extends BasePage {

    private static final By INPUT_USERNAME = byTestId("input-username");
    private static final By INPUT_PASSWORD = byTestId("input-password");
    private static final By BTN_LOGIN = byTestId("btn-login");
    private static final By LOGIN_ERROR = byTestId("login-error");
    private static final By SESSION_EXPIRED = byTestId("session-expired-msg");

    public LoginPage(WebDriver driver) {
        super(driver);
        // Landmark: si input-username no aparece, no estamos en el login.
        waitVisible(INPUT_USERNAME);
    }

    /**
     * Punto de ENTRADA al framework: navega al login y devuelve una LoginPage ya verificada.
     *
     * Es estático a propósito: el resto de páginas se construyen tras una acción que YA
     * navegó (por eso su landmark en el constructor pasa), pero el login no tiene acción
     * previa — alguien tiene que abrir la puerta. Tras un logout/redirect, donde el driver
     * YA está en index.html, se puede usar 'new LoginPage(driver)' directo.
     */
    public static LoginPage open(WebDriver driver) {
        driver.get(Config.baseUrl() + "/index.html");
        return new LoginPage(driver);
    }

    /** Login exitoso → NAVEGA a proyectos: devuelve el page object DESTINO (regla de retorno 1). */
    @Step("Iniciar sesión como {username}")
    public ProjectsPage loginAs(String username, String password) {
        type(INPUT_USERNAME, username);
        type(INPUT_PASSWORD, password);
        click(BTN_LOGIN);
        return new ProjectsPage(driver);
    }

    /** Login que se espera que FALLE → permanece en el login: devuelve this (regla de retorno 2). */
    @Step("Intentar login inválido como {username}")
    public LoginPage loginExpectingError(String username, String password) {
        type(INPUT_USERNAME, username);
        type(INPUT_PASSWORD, password);
        click(BTN_LOGIN);
        return this;
    }

    /** login-error NO existe en el DOM hasta el fallo; aparece tras la respuesta + delay.
     *  Por eso se espera su PRESENCIA (no visibilidad). Consulta → devuelve dato (regla 3). */
    public String errorMessage() {
        return waitPresent(LOGIN_ERROR).getText();
    }

    /** ¿Está visible el banner de sesión expirada? (solo si la URL trae ?expired=1). */
    public boolean sessionExpiredBannerVisible() {
        return driver.findElements(SESSION_EXPIRED).stream().anyMatch(WebElement::isDisplayed);
    }
}
