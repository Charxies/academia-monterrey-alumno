package com.taskflow.qa.practicas;

import com.taskflow.qa.utils.Paginas;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

// import org.openqa.selenium.By;
// import org.openqa.selenium.WebElement;
// import static org.junit.jupiter.api.Assertions.*;

/**
 * MP-4 — CSS básicos: atributos, combinaciones y el data-testid de oro (20 min).
 *
 * La habilidad central del día. Cuatro selectores CSS sobre checkboxes.html y login.html:
 *   - input[type='checkbox']            (atributo)          -> findElements, size()==2
 *   - button.radius[type='submit']      (tag.class[attr])   -> combinación en un elemento
 *   - #flash                            (id)                -> leer getAttribute("class")
 *   - [data-testid='grupo-checkboxes']  (atributo estable)  -> el selector de oro
 *
 * Prueba cada selector con $$('css') en la Console primero. Corre esta clase con
 * -Dpaginas.local=true (los data-testid viven en las páginas locales).
 */
class MP04CssBasicosTest {

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
    @DisplayName("CSS de atributo: input[type='checkbox'] -> exactamente 2 checkboxes")
    void encuentraAmbosCheckboxesPorAtributo() {
        // TODO: driver.get(Paginas.checkboxes()); cuenta findElements(By.cssSelector(
        //       "input[type='checkbox']")).size() y asserta que es 2.
    }

    @Test
    @DisplayName("Combinación tag.class[attr]: button.radius[type='submit'] -> texto 'Login'")
    void encuentraBotonPorCombinacion() {
        // TODO: driver.get(Paginas.login()); localiza By.cssSelector(
        //       "button.radius[type='submit']") y asserta getText() == "Login".
    }

    @Test
    @DisplayName("#flash por id -> su atributo class incluye el estado 'success'")
    void leeLasClasesDeEstadoDelFlash() {
        // TODO: driver.get(Paginas.login()); lee getAttribute("class") de By.cssSelector("#flash")
        //       y asserta que contiene "success" o "error".
    }

    @Test
    @DisplayName("data-testid de oro: [data-testid='grupo-checkboxes'] -> el form de checkboxes")
    void encuentraPorDataTestid() {
        // TODO: driver.get(Paginas.checkboxes()); localiza By.cssSelector(
        //       "[data-testid='grupo-checkboxes']") y asserta que su getAttribute("id") == "checkboxes".
    }
}
