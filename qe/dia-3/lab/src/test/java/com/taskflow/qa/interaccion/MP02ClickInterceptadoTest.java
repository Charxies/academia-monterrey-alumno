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
// import org.openqa.selenium.ElementClickInterceptedException;
// import org.openqa.selenium.support.ui.ExpectedConditions;
// import org.openqa.selenium.support.ui.WebDriverWait;
// import static org.junit.jupiter.api.Assertions.*;

/**
 * MP-2 — Anatomía de un click que falla + su cierre en el PM (15 min AM, se completa en MP-8).
 *
 * ERROR INTENCIONAL 1: click en «Guardar» levanta un toast (overlay de pantalla completa) que
 * TAPA «Continuar» durante 2.5 s. Clickear de inmediato lanza ElementClickInterceptedException
 * (el mensaje nombra al div#toast como interceptor — LÉELO). PROHIBIDO taparlo con
 * JavascriptExecutor. La cura correcta (segundo test) es esperar la INVISIBILIDAD del toast.
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
        // TODO 1: click en By.id("btn-guardar") (levanta el toast).
        // TODO 2: usa assertThrows(ElementClickInterceptedException.class,
        //         () -> driver.findElement(By.id("btn-continuar")).click()) y LEE el mensaje.
    }

    @Test
    @DisplayName("Esperando invisibilityOf del toast -> el click a Continuar sí procede")
    void esperarInvisibilidadDelToastPermiteClick() {
        // TODO 1: crea WebDriverWait(driver, TIMEOUT); click en #btn-guardar.
        // TODO 2: wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("toast"))).
        // TODO 3: click en #btn-continuar; asserta #resultado == "Continuaste al siguiente paso".
    }
}
