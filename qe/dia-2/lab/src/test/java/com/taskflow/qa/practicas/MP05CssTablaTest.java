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
 * MP-5 — CSS de relación sobre tabla y dinámicos (25 min).
 *
 * Relaciones estructurales SIN XPath:
 *   - descendiente (espacio) vs hijo directo (&gt;)
 *   - :nth-child(n) para coordenada fila/columna
 *   - :last-child para la última fila
 *   - findElements para CONTAR (0, 3, 2) tras mutar el DOM
 *
 * dinamicos.html muta el DOM de forma SÍNCRONA: los Delete existen inmediatamente tras
 * cada click, sin waits (eso se resuelve en D3; hoy CERO Thread.sleep).
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
        // TODO: driver.get(Paginas.tabla()); localiza By.cssSelector("#table1 thead th:nth-child(3)")
        //       y asserta getText() == "Email".
    }

    @Test
    @DisplayName("Celda por coordenada (fila 1, col 3) con hijo directo '>' -> jsmith@gmail.com")
    void encuentraCeldaPorCoordenada() {
        // TODO: driver.get(Paginas.tabla()); localiza
        //       By.cssSelector("#table1 tbody tr:nth-child(1) > td:nth-child(3)")
        //       y asserta getText() == "jsmith@gmail.com".
    }

    @Test
    @DisplayName("Última fila con :last-child -> su 1ª celda (apellido) es 'Conway'")
    void encuentraUltimaFilaConLastChild() {
        // TODO: driver.get(Paginas.tabla()); localiza
        //       By.cssSelector("#table1 tbody tr:last-child > td:nth-child(1)")
        //       y asserta getText() == "Conway".
    }

    @Test
    @DisplayName("Dinámicos: 0 al inicio -> 3 tras 3 clicks -> 2 tras borrar uno")
    void cuentaBotonesDeleteTrasMutarElDom() {
        // TODO:
        //  1. driver.get(Paginas.dinamicos());
        //  2. asserta que findElements(By.cssSelector("#elements button")).size() == 0.
        //  3. haz click 3 veces en By.cssSelector("button[onclick='addElement()']").
        //  4. asserta que ahora size() == 3.
        //  5. borra uno (click en el primer #elements button) y asserta size() == 2.
    }
}
