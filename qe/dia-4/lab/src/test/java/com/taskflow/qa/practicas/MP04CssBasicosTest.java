package com.taskflow.qa.practicas;

import com.taskflow.qa.utils.Paginas;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * MP-4 — CSS básicos: atributos, combinaciones y el data-testid de oro (20 min).
 *
 * La habilidad central del día. Cuatro selectores CSS sobre checkboxes.html y login.html:
 *   - input[type='checkbox']            (atributo)          -> findElements, size()==2
 *   - button.radius[type='submit']      (tag.class[attr])   -> combinación en un solo elemento
 *   - #flash                            (id)                -> leer getAttribute("class")
 *   - [data-testid='grupo-checkboxes']  (atributo estable)  -> el selector de oro (contrato con devs)
 *
 * Cada selector se prueba primero con $$('css') en la Console. NOTA: los data-testid
 * viven en las páginas LOCALES (espejo del contrato que verán en la UI de TaskFlow en D4);
 * por eso esta clase se corre con -Dpaginas.local=true.
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
        driver.get(Paginas.checkboxes());
        int cuantos = driver.findElements(By.cssSelector("input[type='checkbox']")).size();
        assertEquals(2, cuantos, "checkboxes.html tiene exactamente 2 checkboxes");
    }

    @Test
    @DisplayName("Combinación tag.class[attr]: button.radius[type='submit'] -> texto 'Login'")
    void encuentraBotonPorCombinacion() {
        driver.get(Paginas.login());
        // tag + clase (sin espacio) + atributo, todo en un mismo elemento.
        WebElement boton = driver.findElement(By.cssSelector("button.radius[type='submit']"));
        assertEquals("Login", boton.getText());
    }

    @Test
    @DisplayName("#flash por id -> su atributo class incluye el estado 'success'")
    void leeLasClasesDeEstadoDelFlash() {
        driver.get(Paginas.login());
        // #flash existe en el DOM desde la carga (aunque oculto): getAttribute funciona
        // aunque el elemento no esté visible. Su class de arranque es "flash success".
        String clases = driver.findElement(By.cssSelector("#flash")).getAttribute("class");
        assertTrue(clases.contains("success") || clases.contains("error"),
                "el #flash debe traer una clase de estado (success/error); vino: " + clases);
    }

    @Test
    @DisplayName("data-testid de oro: [data-testid='grupo-checkboxes'] -> el form de checkboxes")
    void encuentraPorDataTestid() {
        driver.get(Paginas.checkboxes());
        // El selector estable por contrato: no depende de tag, orden ni clases.
        WebElement grupo = driver.findElement(By.cssSelector("[data-testid='grupo-checkboxes']"));
        // Validamos que el data-testid apunta al form correcto.
        assertEquals("checkboxes", grupo.getAttribute("id"));
    }
}
