package com.taskflow.qa.esperas;

import com.taskflow.qa.utils.Paginas;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * MP-8 — presence vs visibility, invisibility del spinner, elementToBeClickable y el Stale (20 min).
 *
 * En carne propia la diferencia que decide el error nº 1 de las esperas:
 *   - presenceOfElementLocated -> pasa con el elemento AÚN OCULTO (existe en el DOM).
 *   - visibilityOfElementLocated -> espera a que además sea VISIBLE.
 * Más: invisibilityOfElementLocated para que un spinner se VAYA, y elementToBeClickable
 * (visible != habilitado).
 *
 * ERROR INTENCIONAL 3: cachear un WebElement y reusarlo tras un re-render ->
 * StaleElementReferenceException (la referencia apunta a un nodo que ya no existe). Regla:
 * NUNCA cachear elementos entre mutaciones del DOM; re-localizar "cerca de donde usas".
 *
 * Cierre del gancho de MP-2: arreglar toast.html esperando la invisibilidad del toast.
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
        driver.get(Paginas.cargaDinamica("oculto"));

        // #finish YA está en el DOM pero oculto: presence pasa YA, pero NO está desplegado.
        WebElement oculto = wait.until(
                ExpectedConditions.presenceOfElementLocated(By.id("finish")));
        assertFalse(oculto.isDisplayed(), "presence pasa aunque el elemento esté oculto");

        driver.findElement(By.cssSelector("#start button")).click();

        // Ahora sí esperamos a que se VEA (tras el retraso real de la carga).
        WebElement visible = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.id("finish")));
        assertEquals("Hello World!", visible.getText());
    }

    @Test
    @DisplayName("El spinner DESAPARECE: invisibilityOfElementLocated(#loading)")
    void esperaLaInvisibilidadDelSpinner() {
        driver.get(Paginas.cargaDinamica("oculto"));

        driver.findElement(By.cssSelector("#start button")).click();
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("loading")));

        // Cuando el spinner se fue, el resultado ya está visible.
        assertTrue(driver.findElement(By.id("finish")).isDisplayed());
    }

    @Test
    @DisplayName("Ej. 2 (inexistente): el elemento NO existe hasta terminar la carga")
    void ejemplo2ElementoInexistenteHastaLaCarga() {
        driver.get(Paginas.cargaDinamica("inexistente"));

        // Antes de arrancar, ni siquiera está en el DOM.
        assertTrue(driver.findElements(By.id("finish")).isEmpty(),
                "en el ejemplo 2 el elemento no existe todavía");

        driver.findElement(By.cssSelector("#start button")).click();
        WebElement finish = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.id("finish")));
        assertEquals("Hello World!", finish.getText());
    }

    @Test
    @DisplayName("elementToBeClickable: el input se habilita tras el retraso (visible != habilitado)")
    void esperaLaHabilitacionDelInput() {
        driver.get(Paginas.controlesDinamicos());

        driver.findElement(By.cssSelector("#input-example button")).click();   // "Enable"

        // El input EXISTE y es visible desde el inicio, pero está DESHABILITADO:
        // elementToBeClickable espera a que además quede habilitado.
        WebElement input = wait.until(ExpectedConditions.elementToBeClickable(
                By.cssSelector("#input-example input[type='text']")));
        assertTrue(input.isEnabled(), "el input debería quedar habilitado");
    }

    @Test
    @DisplayName("STALE: reusar un WebElement cacheado tras el re-render -> StaleElementReferenceException")
    void reusarElementoCacheadoLanzaStale() {
        driver.get(Paginas.controlesDinamicos());

        // Cacheamos la referencia ANTES de mutar el DOM (el anti-patrón).
        WebElement checkbox = driver.findElement(By.cssSelector("#checkbox-example input"));

        driver.findElement(By.cssSelector("#checkbox-example button")).click();   // "Remove"
        wait.until(ExpectedConditions.stalenessOf(checkbox));   // el nodo se elimina del DOM

        // La referencia ahora apunta a un nodo muerto: cualquier uso revienta.
        assertThrows(StaleElementReferenceException.class, checkbox::isSelected,
                "un WebElement cacheado a través de un re-render queda stale");

        // La cura: re-localizar. Tras Remove, el mensaje confirma el nuevo estado.
        WebElement mensaje = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.id("message")));
        assertEquals("It's gone!", mensaje.getText());
    }

    @Test
    @DisplayName("Cierre del gancho MP-2: esperar invisibilityOf del toast y luego clickear")
    void cierreDelGanchoToastConInvisibility() {
        driver.get(Paginas.toast());

        driver.findElement(By.id("btn-guardar")).click();
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("toast")));
        driver.findElement(By.id("btn-continuar")).click();

        assertEquals("Continuaste al siguiente paso",
                driver.findElement(By.id("resultado")).getText());
    }
}
