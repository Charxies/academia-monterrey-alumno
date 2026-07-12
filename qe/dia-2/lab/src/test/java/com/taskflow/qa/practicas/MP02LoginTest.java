package com.taskflow.qa.practicas;

import com.taskflow.qa.utils.Paginas;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

// Imports que probablemente necesites (descoméntalos al usarlos):
// import org.openqa.selenium.By;
// import org.openqa.selenium.WebElement;
// import org.openqa.selenium.NoSuchElementException;
// import static org.junit.jupiter.api.Assertions.*;

/**
 * MP-2 — Primer test de localización sobre login.html (25 min).
 *
 * La jerarquía práctica en acción: id > name > CSS.
 *   - username tiene id estable puesto por el dev  -> By.id
 *   - password se localiza por name                -> By.name
 *   - el botón NO tiene id (a propósito)           -> CSS de atributo button[type='submit']
 *
 * Regla de oro: prueba CADA locator con $$('css')/$x('xpath') en la Console (debe dar 1)
 * ANTES de escribirlo en Java. Cada test valida ALGO del elemento, no solo su presencia.
 *
 * Setup del driver DUPLICADO a propósito en cada clase MP (se extrae en D4).
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
        // TODO: localiza el username con By.id("username") y asserta que isDisplayed().
    }

    @Test
    @DisplayName("password por By.name -> su type es 'password'")
    void encuentraPasswordPorName() {
        // TODO: localiza el password con By.name("password") y asserta
        //       que getAttribute("type") == "password".
    }

    @Test
    @DisplayName("botón submit por CSS de atributo -> su texto es 'Login'")
    void encuentraBotonSubmitPorCss() {
        // TODO: el botón no tiene id. Localízalo con By.cssSelector("button[type='submit']")
        //       y asserta que getText() == "Login".
    }

    @Test
    @DisplayName("findElements de un id inexistente -> lista VACÍA (sin excepción)")
    void findElementsDeLocatorInexistenteDevuelveListaVacia() {
        // TODO: asserta que driver.findElements(By.id("no-existe")) está vacío
        //       (findElements NUNCA lanza; devuelve []).
    }

    @Test
    @DisplayName("findElement del mismo id inexistente -> NoSuchElementException")
    void findElementDeLocatorInexistenteLanzaExcepcion() {
        // TODO: usa assertThrows(NoSuchElementException.class,
        //       () -> driver.findElement(By.id("no-existe"))).
    }
}
