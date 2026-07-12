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

/**
 * MP-5 — CSS de relación sobre tabla y dinámicos (25 min).
 *
 * Relaciones estructurales SIN XPath:
 *   - descendiente (espacio) vs hijo directo (&gt;)
 *   - :nth-child(n) para coordenada fila/columna
 *   - :last-child para la última fila
 *   - findElements para CONTAR (0, 3, 2) tras mutar el DOM
 *
 * dinamicos.html muta el DOM de forma SÍNCRONA a propósito: los botones Delete existen
 * inmediatamente tras cada click, sin waits (eso se resuelve bien en D3; hoy CERO sleep fijo).
 */
class MP05CssTablaTest {

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
    @DisplayName("Header por nth-child: th:nth-child(3) -> 'Email'")
    void encuentraHeaderPorNthChild() {
        driver.get(Paginas.tabla());
        WebElement header = driver.findElement(By.cssSelector("#table1 thead th:nth-child(3)"));
        assertEquals("Email", header.getText());
    }

    @Test
    @DisplayName("Celda por coordenada (fila 1, col 3) con hijo directo '>' -> jsmith@gmail.com")
    void encuentraCeldaPorCoordenada() {
        driver.get(Paginas.tabla());
        // tr:nth-child(1) es la primera fila del tbody; > td:nth-child(3) su 3ª celda (Email).
        WebElement celda = driver.findElement(
                By.cssSelector("#table1 tbody tr:nth-child(1) > td:nth-child(3)"));
        assertEquals("jsmith@gmail.com", celda.getText());
    }

    @Test
    @DisplayName("Última fila con :last-child -> su 1ª celda (apellido) es 'Conway'")
    void encuentraUltimaFilaConLastChild() {
        driver.get(Paginas.tabla());
        WebElement apellido = driver.findElement(
                By.cssSelector("#table1 tbody tr:last-child > td:nth-child(1)"));
        assertEquals("Conway", apellido.getText());
    }

    @Test
    @DisplayName("Dinámicos: 0 al inicio -> 3 tras 3 clicks -> 2 tras borrar uno")
    void cuentaBotonesDeleteTrasMutarElDom() {
        driver.get(Paginas.dinamicos());

        // El botón Add no tiene id: CSS de atributo por su onclick (estable en esta página).
        By botonAdd = By.cssSelector("button[onclick='addElement()']");
        By botonesDelete = By.cssSelector("#elements button");

        // Estado inicial: el contenedor #elements está vacío.
        assertEquals(0, driver.findElements(botonesDelete).size(),
                "al inicio no hay botones Delete");

        // 3 clicks en Add -> 3 botones Delete (DOM síncrono, sin waits).
        for (int i = 0; i < 3; i++) {
            driver.findElement(botonAdd).click();
        }
        assertEquals(3, driver.findElements(botonesDelete).size(),
                "tras 3 clicks debe haber 3 botones Delete");

        // Borrar uno (click en el primer Delete) -> quedan 2.
        driver.findElements(botonesDelete).get(0).click();
        assertEquals(2, driver.findElements(botonesDelete).size(),
                "tras borrar uno deben quedar 2");
    }
}
