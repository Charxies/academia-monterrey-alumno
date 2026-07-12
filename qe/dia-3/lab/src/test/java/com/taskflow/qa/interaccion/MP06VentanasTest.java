package com.taskflow.qa.interaccion;

import com.taskflow.qa.utils.Paginas;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.time.Duration;

// Imports que probablemente necesites (descoméntalos al usarlos):
// import org.openqa.selenium.By;
// import org.openqa.selenium.support.ui.ExpectedConditions;
// import org.openqa.selenium.support.ui.WebDriverWait;
// import static org.junit.jupiter.api.Assertions.*;

/**
 * MP-6 — Ventanas/pestañas: window handles, switch, close y volver a un handle vivo (15 min).
 * (Recortable de AM-2 si hay atascos; el PM de esperas jamás se recorta.)
 *
 * Cada pestaña tiene un window handle (id string). getWindowHandle() da el actual;
 * getWindowHandles() da TODOS. Cambiar: switchTo().window(handle). Tras cerrar la secundaria
 * hay que volver a un handle VIVO. Usa una espera para "aparece la nueva pestaña"
 * (numberOfWindowsToBe): abrir una ventana es asíncrono.
 */
class MP06VentanasTest {

    private static final Duration TIMEOUT = Duration.ofSeconds(10);

    private WebDriver driver;

    @BeforeEach
    void abrirNavegador() {
        driver = new ChromeDriver();
        driver.get(Paginas.ventanas());
    }

    @AfterEach
    void cerrarNavegador() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    @DisplayName("Abrir pestaña nueva, asertar su título, cerrarla y volver a la original")
    void abreNuevaPestanaLeeSuTituloYVuelve() {
        // TODO 1: original = driver.getWindowHandle();
        // TODO 2: click By.linkText("Click Here"); wait numberOfWindowsToBe(2).
        // TODO 3: recorre getWindowHandles(); al que != original, switchTo().window(handle).
        // TODO 4: asserta getTitle()=="New Window" y el h3=="New Window".
        // TODO 5: driver.close(); switchTo().window(original);
        //         asserta que el h3 original == "Opening a new window" (seguimos operando).
    }
}
