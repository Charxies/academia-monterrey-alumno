package com.taskflow.qa.esperas;

import com.taskflow.qa.utils.Paginas;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * MP-7 — Demo MEDIBLE del anti-patrón Thread.sleep vs explicit wait (15 min).
 *
 * PROVISTA COMPLETA y @Disabled a PROPÓSITO: es la ÚNICA clase donde Thread.sleep está
 * permitido (excepción documentada del grep de la rúbrica). El instructor la habilita SOLO
 * en este bloque para cronometrar en vivo, y la vuelve a dejar @Disabled.
 *
 * Mismo escenario /dynamic_loading (retraso real ~2.5 s) resuelto de dos formas:
 *   - conSleepFijo:     Thread.sleep(3000) -> SIEMPRE gasta 3 s, aunque el elemento llegue antes
 *                       (y aun así fallaría si un día tardara 3.1 s).
 *   - conExplicitWait:  WebDriverWait -> retorna en el MILISEGUNDO en que el elemento está listo
 *                       (~2.5 s) y solo agota el timeout cuando de verdad falla.
 * Aritmética de pizarra: 10 sleeps de 3 s = 30 s desperdiciados SIEMPRE; con explicit waits,
 * la suite va tan rápido como el sistema lo permita.
 */
@Disabled("Demo medible del anti-patrón: habilitar SOLO en el bloque MP-7 para cronometrar")
class MP07AntiPatronSleepTest {

    private static final Duration TIMEOUT = Duration.ofSeconds(10);

    private WebDriver driver;

    @BeforeEach
    void abrirNavegador() {
        driver = new ChromeDriver();
        driver.get(Paginas.cargaDinamica("oculto"));
    }

    @AfterEach
    void cerrarNavegador() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    @DisplayName("ANTI-PATRÓN: Thread.sleep(3000) fijo — gasta 3 s pase lo que pase")
    void conSleepFijo() throws InterruptedException {
        long t0 = System.nanoTime();

        driver.findElement(By.cssSelector("#start button")).click();
        Thread.sleep(3000);   // <-- el anti-patrón: dormir "por si acaso"
        WebElement finish = driver.findElement(By.id("finish"));

        long ms = Duration.ofNanos(System.nanoTime() - t0).toMillis();
        System.out.printf("[MP-7] Thread.sleep(3000)  -> %4d ms | texto=%s%n", ms, finish.getText());
        assertEquals("Hello World!", finish.getText());
    }

    @Test
    @DisplayName("ESTÁNDAR: WebDriverWait — retorna en cuanto el elemento está visible (~2.5 s)")
    void conExplicitWait() {
        long t0 = System.nanoTime();

        driver.findElement(By.cssSelector("#start button")).click();
        WebElement finish = new WebDriverWait(driver, TIMEOUT)
                .until(ExpectedConditions.visibilityOfElementLocated(By.id("finish")));

        long ms = Duration.ofNanos(System.nanoTime() - t0).toMillis();
        System.out.printf("[MP-7] WebDriverWait(10s)  -> %4d ms | texto=%s%n", ms, finish.getText());
        assertEquals("Hello World!", finish.getText());
    }
}
