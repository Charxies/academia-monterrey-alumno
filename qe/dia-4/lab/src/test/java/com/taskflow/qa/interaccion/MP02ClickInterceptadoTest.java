package com.taskflow.qa.interaccion;

import com.taskflow.qa.utils.Paginas;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * MP-2 — Anatomía de un click que falla + su cierre en el PM (15 min AM, se completa en MP-8).
 *
 * ERROR INTENCIONAL 1: click en «Guardar» dispara un toast (overlay de pantalla completa)
 * que TAPA «Continuar» durante 2.5 s. Clickear de inmediato lanza:
 *
 *   org.openqa.selenium.ElementClickInterceptedException:
 *     element click intercepted: Element <button id="btn-continuar"> is not clickable at
 *     point (x, y). Other element would receive the click: <div id="toast">...
 *
 * Leer ESE mensaje (dice QUIÉN intercepta) es la habilidad. PROHIBIDO taparlo con un click
 * de JavascriptExecutor: esconde un bug real de UX. La cura correcta es una espera de
 * INVISIBILIDAD del interceptor (segundo test) — el gancho que cierra el PM (MP-8/T9).
 */
class MP02ClickInterceptadoTest {

    /** Un solo lugar por clase para el timeout (D4 lo centraliza; hoy duplicado a propósito). */
    private static final Duration TIMEOUT = Duration.ofSeconds(10);

    private WebDriver driver;

    @BeforeEach
    void abrirNavegador() {
        driver = new ChromeDriver();
        driver.get(Paginas.toast());
    }

    @AfterEach
    void cerrarNavegador() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    @DisplayName("Click inmediato mientras el toast tapa -> ElementClickInterceptedException")
    void clickInmediatoEsInterceptado() {
        driver.findElement(By.id("btn-guardar")).click();   // levanta el toast (2.5 s)

        // Sin esperar a que el toast se vaya: el click es interceptado. Capturamos la
        // excepción para VERLA en verde (el error como herramienta didáctica del día).
        assertThrows(ElementClickInterceptedException.class,
                () -> driver.findElement(By.id("btn-continuar")).click(),
                "clickear a través del overlay debe lanzar ElementClickInterceptedException");
    }

    @Test
    @DisplayName("Esperando invisibilityOf del toast -> el click a Continuar sí procede")
    void esperarInvisibilidadDelToastPermiteClick() {
        WebDriverWait wait = new WebDriverWait(driver, TIMEOUT);

        driver.findElement(By.id("btn-guardar")).click();

        // La cura: esperar a que el interceptor DESAPAREZCA (justo lo que haría un usuario).
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("toast")));

        driver.findElement(By.id("btn-continuar")).click();

        assertEquals("Continuaste al siguiente paso",
                driver.findElement(By.id("resultado")).getText());
    }
}
