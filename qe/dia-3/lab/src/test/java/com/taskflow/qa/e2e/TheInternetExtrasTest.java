package com.taskflow.qa.e2e;

import com.taskflow.qa.utils.Paginas;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

// Imports que probablemente necesites (descoméntalos al usarlos):
// import org.openqa.selenium.Alert;
// import org.openqa.selenium.By;
// import org.openqa.selenium.WebElement;
// import org.openqa.selenium.support.ui.ExpectedConditions;
// import static org.junit.jupiter.api.Assertions.*;

/**
 * INTEGRADOR — Extras de the-internet: alert nativo e iframe con explicit waits (2 tests).
 *
 * Cierra los dos temas de AM-2 que no viven en la tienda: un alert NATIVO
 * (switchTo().alert()) y un iframe (switchTo().frame() + defaultContent() de vuelta).
 * Driver por test, cero sleeps, waits explícitas.
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
        // TODO: driver.get(Paginas.alertas()); click //button[text()='Click for JS Confirm'];
        //       alerta = wait.until(ExpectedConditions.alertIsPresent());
        //       asserta getText()=="I am a JS Confirm"; accept();
        //       asserta By.id("result").getText()=="You clicked: Ok".
    }

    @Test
    @DisplayName("6 — escribe en un iframe y vuelve al documento externo")
    void escribeEnIframe() {
        // TODO 1: driver.get(Paginas.frames());
        //         wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt("mce_0_ifr")).
        // TODO 2: editor = By.id("tinymce"); editor.clear(); editor.sendKeys("...");
        //         asserta que getText() contiene el texto.
        // TODO 3: switchTo().defaultContent(); asserta que el h3 externo contiene "iFrame".
    }
}
