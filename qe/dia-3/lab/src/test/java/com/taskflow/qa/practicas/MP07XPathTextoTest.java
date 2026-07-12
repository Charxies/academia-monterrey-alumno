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
 * MP-7 — XPath por TEXTO: lo único que CSS no puede (20 min).
 *
 * CSS no tiene selector de texto. XPath sí:
 *   - //td[text()='jsmith@gmail.com']         (texto exacto)
 *   - //a[text()='delete']                    (¿cuántos? $x(...).length primero)
 *   - //td[normalize-space()='Frank']         (espacios traicioneros: text() falla, normalize-space rescata)
 *   - //button[contains(text(),'Add')]        (subcadena)
 *
 * Regla del día: XPath SOLO para texto y ejes; todo lo demás, CSS. Y ojo con las
 * mayúsculas: en XPath el texto es case-sensitive siempre ('delete' != 'Delete').
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
        driver.get(Paginas.tabla());
        WebElement celda = driver.findElement(By.xpath("//td[text()='jsmith@gmail.com']"));
        assertEquals("jsmith@gmail.com", celda.getText());
    }

    @Test
    @DisplayName("//a[text()='delete'] -> matchea los 4 links delete de la tabla")
    void cuentaLinksDeletePorTexto() {
        driver.get(Paginas.tabla());
        // Antes de confiar: en la Console $x("//a[text()='delete']").length da 4.
        int cuantos = driver.findElements(By.xpath("//a[text()='delete']")).size();
        assertEquals(4, cuantos, "hay un link 'delete' por cada una de las 4 filas");
    }

    @Test
    @DisplayName("normalize-space() rescata 'Frank' (viene con espacios), text() no lo encuentra")
    void normalizeSpaceRescataElTextoConEspacios() {
        driver.get(Paginas.tabla());
        // La celda de Frank trae espacios a propósito: text()='Frank' NO matchea...
        assertTrue(driver.findElements(By.xpath("//td[text()='Frank']")).isEmpty(),
                "text()='Frank' no matchea porque la celda tiene espacios alrededor");
        // ...pero normalize-space() colapsa los espacios y SÍ lo encuentra.
        WebElement celda = driver.findElement(By.xpath("//td[normalize-space()='Frank']"));
        assertEquals("Frank", celda.getText().trim());
    }

    @Test
    @DisplayName("contains(text(),'Add'): el botón Add Element (sin id) -> habilitado")
    void localizaBotonPorContainsText() {
        driver.get(Paginas.dinamicos());
        WebElement boton = driver.findElement(By.xpath("//button[contains(text(),'Add')]"));
        assertTrue(boton.isEnabled(), "el botón Add Element debe estar habilitado");
    }
}
