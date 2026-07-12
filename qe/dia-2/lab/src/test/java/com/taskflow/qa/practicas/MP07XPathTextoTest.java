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
 * MP-7 — XPath por TEXTO: lo único que CSS no puede (20 min).
 *
 * CSS no tiene selector de texto. XPath sí:
 *   - //td[text()='jsmith@gmail.com']    (texto exacto)
 *   - //a[text()='delete']               (¿cuántos? $x(...).length primero)
 *   - //td[normalize-space()='Frank']    (espacios traicioneros: text() falla, normalize-space rescata)
 *   - //button[contains(text(),'Add')]   (subcadena)
 *
 * XPath es case-sensitive siempre ('delete' != 'Delete'). Prueba con $x(...) en la Console.
 */
class MP07XPathTextoTest {

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
    @DisplayName("text() exacto: //td[text()='jsmith@gmail.com'] -> esa celda")
    void localizaCeldaPorTextoExacto() {
        // TODO: driver.get(Paginas.tabla()); localiza By.xpath("//td[text()='jsmith@gmail.com']")
        //       y asserta getText() == "jsmith@gmail.com".
    }

    @Test
    @DisplayName("//a[text()='delete'] -> matchea los 4 links delete de la tabla")
    void cuentaLinksDeletePorTexto() {
        // TODO: driver.get(Paginas.tabla()); asserta que
        //       findElements(By.xpath("//a[text()='delete']")).size() == 4.
    }

    @Test
    @DisplayName("normalize-space() rescata 'Frank' (viene con espacios), text() no lo encuentra")
    void normalizeSpaceRescataElTextoConEspacios() {
        // TODO: driver.get(Paginas.tabla());
        //  1. asserta que findElements(By.xpath("//td[text()='Frank']")) está VACÍO.
        //  2. localiza By.xpath("//td[normalize-space()='Frank']") y asserta que su
        //     getText().trim() == "Frank".
    }

    @Test
    @DisplayName("contains(text(),'Add'): el botón Add Element (sin id) -> habilitado")
    void localizaBotonPorContainsText() {
        // TODO: driver.get(Paginas.dinamicos()); localiza
        //       By.xpath("//button[contains(text(),'Add')]") y asserta isEnabled().
    }
}
