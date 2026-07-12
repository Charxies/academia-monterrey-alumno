package com.taskflow.qa.tests.practicas;

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
import static org.junit.jupiter.api.Assertions.fail;

/**
 * MP-6 — El primer test Selenium (25 min).
 *
 * Mismo AAA de sus unit tests, pero el SUT ahora es un navegador real:
 *   Arrange  -> new ChromeDriver()  (en @BeforeEach)
 *   Act      -> driver.get(...) / findElement(...)
 *   Assert   -> assertEquals / assertTrue sobre lo que devuelve el navegador
 *
 * Anatomía a memorizar:  driver -> get -> findElement -> acción/lectura -> assert.
 *
 * NOTA del driver: NO descargues ningún binario a mano. Al crear el ChromeDriver,
 * Selenium Manager (integrado en Selenium 4.6+) resuelve y descarga el chromedriver
 * correcto SOLO — mira el log de la primera corrida.
 */
class MP06PrimerTest {

    private WebDriver driver;

    @BeforeEach
    void abrirNavegador() {
        // TODO 1: crea el driver -> driver = new ChromeDriver();
        //   Observa en el log cómo Selenium Manager resuelve el chromedriver solo.
    }

    @AfterEach
    void cerrarNavegador() {
        // TODO 6: cierra la sesión con driver.quit().
        //   Va en @AfterEach (no al final del @Test) para que corra AUNQUE el test falle.
        //   Protege con un guard por si @BeforeEach no alcanzó a crear el driver:
        //   if (driver != null) driver.quit();
    }

    @Test
    @DisplayName("La home abre, el título es 'The Internet' y el encabezado h1.heading está visible")
    void abreLaHomeYVerificaTituloYEncabezado() {
        // TODO 2: navega a la home -> driver.get(Paginas.home());
        //   (la URL SIEMPRE sale de Paginas, nunca la escribas literal en el test)

        // TODO 3: verifica el título con assertEquals("The Internet", driver.getTitle());
        //   ERROR INTENCIONAL 1: primero escríbelo mal ("The internet", i minúscula),
        //   corre, LEE el diff expected/actual de JUnit y corrige copiando el valor real
        //   impreso por getTitle() — nunca lo transcribas de ojo.

        // TODO 4: localiza el h1 -> WebElement encabezado = driver.findElement(By.className("heading"));
        // TODO 5: verifica assertTrue(encabezado.isDisplayed()) y
        //   assertEquals("Welcome to the-internet", encabezado.getText());

        fail("TODO 2-5: implementa el primer test Selenium y borra este fail");
    }
}
