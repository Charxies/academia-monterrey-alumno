package com.taskflow.qa.e2e;

import com.taskflow.qa.utils.Paginas;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * INTEGRADOR — Extras de the-internet: alert nativo e iframe con explicit waits (2 tests).
 *
 * Cierra los dos temas de AM-2 que no viven en la tienda: un alert NATIVO (que se maneja con
 * switchTo().alert()) y un iframe (que exige switchTo().frame() y su defaultContent() de
 * vuelta). Mismos requisitos: driver por test, cero sleeps, waits explícitas.
 */
class TheInternetExtrasTest {

    private static final Duration TIMEOUT = Duration.ofSeconds(10);

    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeEach
    void abrirNavegador() {
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, TIMEOUT);
    }

    @AfterEach
    void cerrarNavegador() {
        if (driver != null) {
            // Defensivo: nunca dejar un alert abierto que contamine la siguiente corrida.
            try {
                driver.switchTo().alert().dismiss();
            } catch (NoAlertPresentException ignorado) {
                // lo normal
            }
            driver.quit();
        }
    }

    @Test
    @DisplayName("5 — maneja un alert nativo (confirm): getText, accept y assert de #result")
    void manejaAlertNativo() {
        driver.get(Paginas.alertas());

        driver.findElement(By.xpath("//button[text()='Click for JS Confirm']")).click();

        Alert alerta = wait.until(ExpectedConditions.alertIsPresent());
        assertEquals("I am a JS Confirm", alerta.getText());
        alerta.accept();

        assertEquals("You clicked: Ok",
                driver.findElement(By.id("result")).getText());
    }

    @Test
    @DisplayName("6 — escribe en un iframe y vuelve al documento externo")
    void escribeEnIframe() {
        driver.get(Paginas.frames());

        // Entramos al iframe para alcanzar su documento interno.
        wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt("mce_0_ifr"));

        WebElement editor = driver.findElement(By.id("tinymce"));
        editor.clear();
        editor.sendKeys("Automatización con Selenium");
        assertTrue(editor.getText().contains("Automatización con Selenium"),
                "el editor debería contener el texto escrito, fue: " + editor.getText());

        // Salimos del iframe y probamos que sí volvimos: el h3 externo es alcanzable.
        driver.switchTo().defaultContent();
        assertTrue(driver.findElement(By.tagName("h3")).getText().contains("iFrame"),
                "tras defaultContent() debe verse el encabezado del documento externo");
    }
}
