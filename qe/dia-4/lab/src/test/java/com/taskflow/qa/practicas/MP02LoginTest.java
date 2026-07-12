package com.taskflow.qa.practicas;

import com.taskflow.qa.utils.Paginas;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * MP-2 — Primer test de localización sobre login.html (25 min).
 *
 * La jerarquía práctica en acción: id > name > CSS.
 *   - username tiene id estable puesto por el dev  -> By.id
 *   - password se localiza por name                -> By.name
 *   - el botón NO tiene id (a propósito)           -> CSS de atributo button[type='submit']
 *
 * Cada elemento con UN assert significativo (no basta "lo encontré"): isDisplayed,
 * getAttribute("type"), getText. Y el par de tests negativos que separan findElement
 * de findElements: lista vacía vs NoSuchElementException.
 *
 * Regla de oro del día: todo locator se prueba con $$('css')/$x('xpath') en la Console
 * y devuelve EXACTAMENTE 1 antes de escribirse aquí en Java.
 *
 * Setup del driver DUPLICADO a propósito en cada clase MP: se extrae a BaseTest/DriverFactory
 * en D4. Hoy no se crea ninguna clase base.
 */
class MP02LoginTest {

    private WebDriver driver;

    @BeforeEach
    void abrirNavegador() {
        driver = new ChromeDriver();
        driver.get(Paginas.login());
    }

    @AfterEach
    void cerrarNavegador() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    @DisplayName("username por By.id -> está visible")
    void encuentraUsernamePorId() {
        WebElement username = driver.findElement(By.id("username"));
        assertTrue(username.isDisplayed(), "el campo username debería estar visible");
    }

    @Test
    @DisplayName("password por By.name -> su type es 'password'")
    void encuentraPasswordPorName() {
        WebElement password = driver.findElement(By.name("password"));
        // Validamos ALGO del elemento, no solo su presencia: el type real.
        assertEquals("password", password.getAttribute("type"));
    }

    @Test
    @DisplayName("botón submit por CSS de atributo -> su texto es 'Login'")
    void encuentraBotonSubmitPorCss() {
        // El botón no tiene id: se localiza por un atributo estable de su rol.
        WebElement boton = driver.findElement(By.cssSelector("button[type='submit']"));
        assertEquals("Login", boton.getText());
    }

    @Test
    @DisplayName("findElements de un id inexistente -> lista VACÍA (sin excepción)")
    void findElementsDeLocatorInexistenteDevuelveListaVacia() {
        // findElements NUNCA lanza: devuelve [] si no hay match. Ideal para asserts
        // de existencia/conteo.
        assertTrue(driver.findElements(By.id("no-existe")).isEmpty(),
                "findElements de un id inexistente debe devolver lista vacía");
    }

    @Test
    @DisplayName("findElement del mismo id inexistente -> NoSuchElementException")
    void findElementDeLocatorInexistenteLanzaExcepcion() {
        // findElement SÍ lanza si no hay match. El otro lado de la moneda de arriba.
        assertThrows(NoSuchElementException.class,
                () -> driver.findElement(By.id("no-existe")));
    }
}
