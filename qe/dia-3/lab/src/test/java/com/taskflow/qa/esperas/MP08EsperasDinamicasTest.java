package com.taskflow.qa.esperas;

import com.taskflow.qa.utils.Paginas;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

// Imports que probablemente necesites (descoméntalos al usarlos):
// import org.openqa.selenium.By;
// import org.openqa.selenium.StaleElementReferenceException;
// import org.openqa.selenium.WebElement;
// import org.openqa.selenium.support.ui.ExpectedConditions;
// import static org.junit.jupiter.api.Assertions.*;

/**
 * MP-8 — presence vs visibility, invisibility del spinner, elementToBeClickable y el Stale (20 min).
 *
 * En carne propia el error nº 1 de las esperas:
 *   - presenceOfElementLocated   -> pasa con el elemento AÚN OCULTO (existe en el DOM).
 *   - visibilityOfElementLocated -> espera a que además sea VISIBLE.
 * Más: invisibilityOfElementLocated (que un spinner se VAYA) y elementToBeClickable (visible
 * != habilitado).
 *
 * ERROR INTENCIONAL 3: cachear un WebElement y reusarlo tras un re-render ->
 * StaleElementReferenceException. Regla: NUNCA cachear elementos entre mutaciones del DOM.
 * Cierre del gancho MP-2: arreglar toast.html esperando la invisibilidad del toast.
 */
class MP08EsperasDinamicasTest {

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
            driver.quit();
        }
    }

    @Test
    @DisplayName("Ej. 1 (oculto): presence pasa con el elemento oculto; visibility espera")
    void presenceVsVisibilityConElementoOculto() {
        // TODO 1: driver.get(Paginas.cargaDinamica("oculto"));
        // TODO 2: wait.until(presenceOfElementLocated(By.id("finish"))); asserta !isDisplayed().
        // TODO 3: click "#start button"; wait.until(visibilityOfElementLocated(#finish));
        //         asserta getText()=="Hello World!".
    }

    @Test
    @DisplayName("El spinner DESAPARECE: invisibilityOfElementLocated(#loading)")
    void esperaLaInvisibilidadDelSpinner() {
        // TODO: modo "oculto"; click "#start button"; wait invisibilityOfElementLocated(#loading);
        //       asserta que #finish está desplegado.
    }

    @Test
    @DisplayName("Ej. 2 (inexistente): el elemento NO existe hasta terminar la carga")
    void ejemplo2ElementoInexistenteHastaLaCarga() {
        // TODO: driver.get(Paginas.cargaDinamica("inexistente"));
        //       asserta findElements(#finish).isEmpty(); click start;
        //       wait visibilityOfElementLocated(#finish); asserta "Hello World!".
    }

    @Test
    @DisplayName("elementToBeClickable: el input se habilita tras el retraso (visible != habilitado)")
    void esperaLaHabilitacionDelInput() {
        // TODO: driver.get(Paginas.controlesDinamicos()); click "#input-example button" (Enable);
        //       wait elementToBeClickable("#input-example input[type='text']"); asserta isEnabled().
    }

    @Test
    @DisplayName("STALE: reusar un WebElement cacheado tras el re-render -> StaleElementReferenceException")
    void reusarElementoCacheadoLanzaStale() {
        // TODO 1: controlesDinamicos(); checkbox = findElement("#checkbox-example input") (cachear).
        // TODO 2: click "#checkbox-example button" (Remove); wait stalenessOf(checkbox).
        // TODO 3: assertThrows(StaleElementReferenceException.class, checkbox::isSelected).
        // TODO 4: re-localiza: wait visibilityOfElementLocated(#message); asserta "It's gone!".
    }

    @Test
    @DisplayName("Cierre del gancho MP-2: esperar invisibilityOf del toast y luego clickear")
    void cierreDelGanchoToastConInvisibility() {
        // TODO: driver.get(Paginas.toast()); click #btn-guardar;
        //       wait invisibilityOfElementLocated(#toast); click #btn-continuar;
        //       asserta #resultado == "Continuaste al siguiente paso".
    }
}
