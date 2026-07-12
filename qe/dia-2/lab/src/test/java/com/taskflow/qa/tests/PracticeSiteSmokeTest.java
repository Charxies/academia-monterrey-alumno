package com.taskflow.qa.tests;

import com.taskflow.qa.utils.Paginas;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Integrador D1 — suite smoke sobre el sitio de práctica (3 tests).
 *
 * Es el entregable del día: arranca el proyecto taskflow-qa que crece toda la
 * semana. Los 3 tests pasan en DOS modos sin tocar un locator:
 *   - mvn test                        -> the-internet.herokuapp.com (externo)
 *   - mvn test -Dpaginas.local=true   -> espejos file:// de src/test/resources
 *
 * @Tag("smoke") permite correr solo esta suite con:  mvn test -Dgroups=smoke
 *
 * Requisitos del día: driver fresco por test (@BeforeEach) y quit() garantizado
 * (@AfterEach); asserts de JUnit 5; CERO Thread.sleep (las páginas son estáticas);
 * URLs solo vía Paginas. El setup duplicado respecto a las clases MP está BIEN hoy:
 * se extrae a BaseTest/DriverFactory en D4.
 */
@Tag("smoke")
class PracticeSiteSmokeTest {

    private WebDriver driver;

    @BeforeEach
    void abrirNavegador() {
        driver = new ChromeDriver();
    }

    @AfterEach
    void cerrarNavegador() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    @DisplayName("Smoke 1 — el título de la home es 'The Internet'")
    void laHomeTieneElTituloCorrecto() {
        driver.get(Paginas.home());
        assertEquals("The Internet", driver.getTitle());
    }

    @Test
    @DisplayName("Smoke 2 — el encabezado h1.heading está visible y con su texto")
    void laHomeMuestraElEncabezadoDeBienvenida() {
        driver.get(Paginas.home());
        WebElement encabezado = driver.findElement(By.className("heading"));
        assertTrue(encabezado.isDisplayed(), "el h1.heading debería estar visible");
        assertEquals("Welcome to the-internet", encabezado.getText());
    }

    @Test
    @DisplayName("Smoke 3 — click en 'Form Authentication' navega al login")
    void clickEnFormAuthenticationNavegaAlLogin() {
        driver.get(Paginas.home());

        // El link se localiza por su texto exacto (By.linkText); su href apunta al
        // login (externo: /login ; local: login.html).
        driver.findElement(By.linkText("Form Authentication")).click();

        // Assert 1: la URL cambió y contiene "login" (verdadero en externo Y local).
        assertTrue(driver.getCurrentUrl().contains("login"),
                "la URL tras el click debería contener 'login'");
        // Assert 2: el h2 de la página de login dice "Login Page".
        assertEquals("Login Page", driver.findElement(By.tagName("h2")).getText());
    }
}
