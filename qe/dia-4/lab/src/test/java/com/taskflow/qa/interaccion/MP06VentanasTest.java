package com.taskflow.qa.interaccion;

import com.taskflow.qa.utils.Paginas;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * MP-6 — Ventanas/pestañas: window handles, switch, close y volver a un handle vivo (15 min).
 * (Este MP es el recortable de AM-2 si hay atascos; el PM de esperas jamás se recorta.)
 *
 * Cada pestaña tiene un window handle (un id string). getWindowHandle() da el actual;
 * getWindowHandles() da TODOS. Para cambiar: switchTo().window(handle). Tras cerrar la
 * secundaria hay que volver a un handle VIVO o el driver queda apuntando a la nada.
 *
 * Usamos una espera explícita para el "aparece la nueva pestaña" (numberOfWindowsToBe):
 * abrir una ventana es asíncrono y anticipamos aquí la herramienta que el PM formaliza.
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
        WebDriverWait wait = new WebDriverWait(driver, TIMEOUT);

        String original = driver.getWindowHandle();   // guardamos el handle actual

        driver.findElement(By.linkText("Click Here")).click();   // abre pestaña nueva
        wait.until(ExpectedConditions.numberOfWindowsToBe(2));

        // Saltamos al handle que NO es el original.
        for (String handle : driver.getWindowHandles()) {
            if (!handle.equals(original)) {
                driver.switchTo().window(handle);
                break;
            }
        }

        assertEquals("New Window", driver.getTitle());
        assertEquals("New Window", driver.findElement(By.tagName("h3")).getText());

        driver.close();                          // cierra SOLO la secundaria
        driver.switchTo().window(original);      // volvemos a un handle vivo

        // Y podemos seguir operando en la original.
        assertEquals("Opening a new window",
                driver.findElement(By.tagName("h3")).getText());
    }
}
