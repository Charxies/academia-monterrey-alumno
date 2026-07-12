package com.taskflow.qa.interaccion;

import com.taskflow.qa.utils.Paginas;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * MP-3 — Selects nativos, dropdown custom y checkboxes/radios idempotentes (20 min).
 *
 * Tres controles, tres técnicas:
 *   - <select> NATIVO  -> la clase Select (selectByVisibleText/Value/Index, getFirstSelectedOption,
 *                          getOptions + streams para asertar la lista, que ya dominan).
 *   - dropdown CUSTOM  -> NO existe Select: patrón de 2 pasos (abrir + click a la opción por texto).
 *   - checkbox / radio -> click CONDICIONAL idempotente: if (!chk.isSelected()) chk.click().
 *                          "asegurar marcado" != "toggle": un toggle ciego rompe si cambia el estado inicial.
 *
 * Sin esperas (páginas síncronas). Setup del driver duplicado a propósito (se extrae en D4).
 */
class MP03SelectsCheckboxesTest {

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
    @DisplayName("Select nativo: selectByVisibleText / selectByValue / selectByIndex")
    void selectNativoPorTextoValueEIndice() {
        driver.get(Paginas.dropdowns());
        Select combo = new Select(driver.findElement(By.id("dropdown")));

        combo.selectByVisibleText("Option 1");
        assertEquals("Option 1", combo.getFirstSelectedOption().getText());

        combo.selectByValue("2");
        assertEquals("Option 2", combo.getFirstSelectedOption().getText());

        combo.selectByIndex(1);   // índice 0 = placeholder deshabilitado; 1 = "Option 1"
        assertEquals("Option 1", combo.getFirstSelectedOption().getText());
    }

    @Test
    @DisplayName("Select nativo: getOptions con streams para asertar la lista completa")
    void opcionesDelSelectConStreams() {
        driver.get(Paginas.dropdowns());
        Select combo = new Select(driver.findElement(By.id("dropdown")));

        // Streams (ya los dominan) para pasar de List<WebElement> a los textos visibles.
        List<String> textos = combo.getOptions().stream()
                .map(WebElement::getText)
                .toList();

        assertTrue(textos.contains("Option 1"), "faltó Option 1 en: " + textos);
        assertTrue(textos.contains("Option 2"), "faltó Option 2 en: " + textos);
    }

    @Test
    @DisplayName("Dropdown CUSTOM: patrón de 2 pasos (abrir + click a la opción por texto)")
    void dropdownCustomDosPasos() {
        driver.get(Paginas.dropdowns());

        // Paso 1: abrir la lista.
        driver.findElement(By.id("custom-trigger")).click();
        // Paso 2: click a la opción, localizada por su TEXTO (no hay value ni Select aquí).
        driver.findElement(By.xpath("//ul[@id='custom-opciones']/li[text()='Verde']")).click();

        assertEquals("Seleccionaste: Verde",
                driver.findElement(By.id("custom-seleccion")).getText());
        // La selección también queda reflejada en el texto del trigger.
        assertEquals("Verde", driver.findElement(By.id("custom-trigger")).getText());
    }

    @Test
    @DisplayName("Checkboxes: click condicional idempotente (asegurar marcado != toggle)")
    void checkboxConClickCondicionalIdempotente() {
        driver.get(Paginas.checkboxes());
        List<WebElement> checks = driver.findElements(
                By.cssSelector("#checkboxes input[type='checkbox']"));

        WebElement primero = checks.get(0);   // viene DESmarcado
        WebElement segundo = checks.get(1);   // viene marcado (checked)

        // "Asegurar marcado": solo hago click si NO está seleccionado. Idempotente:
        // corra el estado inicial que corra, el resultado final es "marcado".
        if (!primero.isSelected()) { primero.click(); }
        if (!segundo.isSelected()) { segundo.click(); }

        assertTrue(primero.isSelected(), "el 1º debería quedar marcado");
        assertTrue(segundo.isSelected(), "el 2º ya estaba marcado y NO se tocó");
    }

    @Test
    @DisplayName("Radios: seleccionar uno deselecciona al del grupo (misma idea idempotente)")
    void radioSeleccionaUnoDelGrupo() {
        driver.get(Paginas.checkboxes());

        WebElement pro = driver.findElement(By.id("radio-pro"));            // checked inicial
        WebElement enterprise = driver.findElement(By.id("radio-enterprise"));

        if (!enterprise.isSelected()) { enterprise.click(); }

        assertTrue(enterprise.isSelected(), "enterprise debería quedar seleccionado");
        assertFalse(pro.isSelected(), "al ser el mismo grupo (name=plan), pro se deselecciona");
    }
}
