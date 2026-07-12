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
 * MP-8 — XPath por EJES: navegar la fila desde un dato (20 min).
 *
 * El caso canónico de QA: "encuentro la celda por su CONTENIDO y navego a OTRA celda o
 * botón de SU MISMA fila". Sin índices mágicos: el ancla es el dato estable (el email).
 *   - //td[text()='X']/following-sibling::td[1]      -> la celda siguiente (Due)
 *   - //td[text()='X']/ancestor::tr//a[text()='delete'] -> el delete de esa misma fila
 *
 * Este patrón reaparece TAL CUAL en D4 contra la tabla de tareas de su propia TaskFlow
 * (localizar una fila por su título y accionar su botón de la columna Acciones).
 */
class MP08XPathEjesTest {

    private WebDriver driver;

    @BeforeEach
    void abrirNavegador() {
        driver = new ChromeDriver();
        driver.get(Paginas.tabla());
    }

    @AfterEach
    void cerrarNavegador() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    @DisplayName("following-sibling: el 'Due' de la fila de jsmith@gmail.com -> $50.00")
    void obtieneElDuePorFollowingSibling() {
        // Ancla: la celda del email. La columna Due es su siguiente td.
        WebElement due = driver.findElement(
                By.xpath("//td[text()='jsmith@gmail.com']/following-sibling::td[1]"));
        assertEquals("$50.00", due.getText());
    }

    @Test
    @DisplayName("ancestor::tr + descendiente: el 'delete' de la fila de jsmith -> href no vacío")
    void obtieneElDeletePorAncestorTr() {
        // Desde el email, subo a su <tr> y bajo al link delete de ESA fila.
        WebElement delete = driver.findElement(
                By.xpath("//td[text()='jsmith@gmail.com']/ancestor::tr//a[text()='delete']"));
        String href = delete.getAttribute("href");
        assertTrue(href != null && !href.isBlank(), "el link delete debe tener href");
        assertTrue(href.endsWith("#delete"), "el href del delete termina en #delete; vino: " + href);
    }

    @Test
    @DisplayName("Otra fila por el mismo patrón: el 'Due' de jdoe@hotmail.com -> $100.00")
    void obtieneElDueDeOtraFila() {
        // El patrón no depende del orden: cambia solo el dato ancla.
        WebElement due = driver.findElement(
                By.xpath("//td[text()='jdoe@hotmail.com']/following-sibling::td[1]"));
        assertEquals("$100.00", due.getText());
    }
}
