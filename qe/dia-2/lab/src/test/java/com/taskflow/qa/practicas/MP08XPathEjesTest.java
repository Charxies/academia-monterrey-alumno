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
 * MP-8 — XPath por EJES: navegar la fila desde un dato (20 min).
 *
 * El caso canónico de QA: "encuentro la celda por su CONTENIDO y navego a OTRA celda o
 * botón de SU MISMA fila". Sin índices mágicos: el ancla es el dato estable (el email).
 *   - //td[text()='X']/following-sibling::td[1]         -> la celda siguiente (Due)
 *   - //td[text()='X']/ancestor::tr//a[text()='delete'] -> el delete de esa misma fila
 *
 * Este patrón reaparece TAL CUAL en D4 contra la tabla de tareas de tu propia TaskFlow.
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
        // TODO: localiza By.xpath("//td[text()='jsmith@gmail.com']/following-sibling::td[1]")
        //       y asserta getText() == "$50.00".
    }

    @Test
    @DisplayName("ancestor::tr + descendiente: el 'delete' de la fila de jsmith -> href no vacío")
    void obtieneElDeletePorAncestorTr() {
        // TODO: localiza By.xpath("//td[text()='jsmith@gmail.com']/ancestor::tr//a[text()='delete']"),
        //       lee getAttribute("href") y asserta que NO está vacío (termina en "#delete").
    }

    @Test
    @DisplayName("Otra fila por el mismo patrón: el 'Due' de jdoe@hotmail.com -> $100.00")
    void obtieneElDueDeOtraFila() {
        // TODO: mismo patrón, cambia solo el email ancla: jdoe@hotmail.com -> "$100.00".
    }
}
