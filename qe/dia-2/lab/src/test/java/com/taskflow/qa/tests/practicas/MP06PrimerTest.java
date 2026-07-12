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
 * NOTA del driver: NO se descarga ningún binario a mano. Al crear el ChromeDriver,
 * Selenium Manager (integrado en Selenium 4.6+) resuelve y descarga el chromedriver
 * correcto SOLO — mira el log de la primera corrida.
 *
 * El setup del driver (@BeforeEach/@AfterEach) está DUPLICADO en cada clase MP a
 * propósito: en D4 se extrae a BaseTest/DriverFactory. Hoy está bien así.
 */
class MP06PrimerTest {

    private WebDriver driver;

    @BeforeEach
    void abrirNavegador() {
        // Selenium Manager resuelve el driver aquí, temprano y solo.
        driver = new ChromeDriver();
    }

    @AfterEach
    void cerrarNavegador() {
        // quit() vive en @AfterEach para que corra AUNQUE el test falle
        // (si estuviera al final del método, un assert roto se lo saltaría).
        // El guard evita NullPointerException si @BeforeEach nunca creó el driver.
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    @DisplayName("La home abre, el título es 'The Internet' y el encabezado h1.heading está visible")
    void abreLaHomeYVerificaTituloYEncabezado() {
        // Act: navegar (la URL sale de Paginas, nunca literal en el test)
        driver.get(Paginas.home());

        // Assert 1: título exacto.
        // ERROR INTENCIONAL 1 (demo): si aquí escribes "The internet" (i minúscula),
        // el test falla; LEE el diff expected/actual de JUnit y copia el valor real
        // impreso por getTitle(), NUNCA lo transcribas de ojo.
        assertEquals("The Internet", driver.getTitle());

        // Act + Assert 2: localizar el h1 por su clase y leerlo.
        WebElement encabezado = driver.findElement(By.className("heading"));
        assertTrue(encabezado.isDisplayed(), "el h1.heading debería estar visible");
        assertEquals("Welcome to the-internet", encabezado.getText());
    }
}
