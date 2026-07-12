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
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Integrador D1 — suite smoke sobre el sitio de práctica (3 tests).
 *
 * Es el entregable del día: arranca el proyecto taskflow-qa que crece toda la
 * semana. Los 3 tests deben pasar en DOS modos sin tocar un locator:
 *   - mvn test                        -> the-internet.herokuapp.com (externo)
 *   - mvn test -Dpaginas.local=true   -> espejos file:// de src/test/resources
 *
 * @Tag("smoke") permite correr solo esta suite con:  mvn test -Dgroups=smoke
 *
 * Requisitos del día: driver fresco por test (@BeforeEach) y quit() garantizado
 * (@AfterEach); asserts de JUnit 5; CERO Thread.sleep; URLs solo vía Paginas.
 */
@Tag("smoke")
class PracticeSiteSmokeTest {

    private WebDriver driver;

    @BeforeEach
    void abrirNavegador() {
        // TODO 1: driver = new ChromeDriver();
    }

    @AfterEach
    void cerrarNavegador() {
        // TODO 2: if (driver != null) driver.quit();
    }

    @Test
    @DisplayName("Smoke 1 — el título de la home es 'The Internet'")
    void laHomeTieneElTituloCorrecto() {
        // TODO 3: driver.get(Paginas.home());
        //         assertEquals("The Internet", driver.getTitle());
        fail("TODO 3: implementa el smoke de título y borra este fail");
    }

    @Test
    @DisplayName("Smoke 2 — el encabezado h1.heading está visible y con su texto")
    void laHomeMuestraElEncabezadoDeBienvenida() {
        // TODO 4: driver.get(Paginas.home());
        //         WebElement encabezado = driver.findElement(By.className("heading"));
        //         assertTrue(encabezado.isDisplayed());
        //         assertEquals("Welcome to the-internet", encabezado.getText());
        fail("TODO 4: implementa el smoke de encabezado y borra este fail");
    }

    @Test
    @DisplayName("Smoke 3 — click en 'Form Authentication' navega al login")
    void clickEnFormAuthenticationNavegaAlLogin() {
        // TODO 5: driver.get(Paginas.home());
        //         driver.findElement(By.linkText("Form Authentication")).click();
        //   Verifica la navegación con DOS asserts:
        //     assertTrue(driver.getCurrentUrl().contains("login"));
        //     assertEquals("Login Page", driver.findElement(By.tagName("h2")).getText());
        fail("TODO 5: implementa el smoke de click y navegación, y borra este fail");
    }
}
