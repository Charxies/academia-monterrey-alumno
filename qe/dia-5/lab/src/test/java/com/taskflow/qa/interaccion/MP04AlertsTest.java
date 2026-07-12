package com.taskflow.qa.interaccion;

import com.taskflow.qa.utils.Paginas;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.UnhandledAlertException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * MP-4 — Alerts NATIVOS: accept/dismiss/getText/sendKeys + la UnhandledAlert provocada (20 min).
 *
 * Los alerts nativos (alert/confirm/prompt) viven FUERA del DOM: no hay locator que los
 * alcance. Se manejan con driver.switchTo().alert():
 *   accept()  -> "OK"      dismiss() -> "Cancel"
 *   getText() -> su mensaje  sendKeys() -> escribe en el prompt
 *
 * Un alert abierto y NO manejado revienta el SIGUIENTE comando con UnhandledAlertException
 * (y el mensaje culpa al comando equivocado). POR ESTO la UI de TaskFlow (D4) usa modales
 * propios en vez de nativos. @AfterEach descarta defensivamente cualquier alert huérfano.
 */
class MP04AlertsTest {

    private WebDriver driver;

    @BeforeEach
    void abrirNavegador() {
        driver = new ChromeDriver();
        driver.get(Paginas.alertas());
    }

    @AfterEach
    void cerrarNavegador() {
        if (driver != null) {
            // Defensivo (punto de dolor 8): si un test dejó un alert abierto, lo cerramos
            // antes de quit() para que no contamine al siguiente.
            try {
                driver.switchTo().alert().dismiss();
            } catch (NoAlertPresentException ignorado) {
                // lo normal: no había ningún alert pendiente
            }
            driver.quit();
        }
    }

    @Test
    @DisplayName("alert() simple: getText + accept y assert de #result")
    void alertSimpleSeLeeYSeAcepta() {
        driver.findElement(By.xpath("//button[text()='Click for JS Alert']")).click();

        Alert alerta = driver.switchTo().alert();
        assertEquals("I am a JS Alert", alerta.getText());
        alerta.accept();

        assertEquals("You successfully clicked an alert",
                driver.findElement(By.id("result")).getText());
    }

    @Test
    @DisplayName("confirm() aceptado -> #result 'You clicked: Ok'")
    void confirmAceptar() {
        driver.findElement(By.xpath("//button[text()='Click for JS Confirm']")).click();
        driver.switchTo().alert().accept();

        assertEquals("You clicked: Ok",
                driver.findElement(By.id("result")).getText());
    }

    @Test
    @DisplayName("confirm() cancelado -> #result 'You clicked: Cancel'")
    void confirmCancelar() {
        driver.findElement(By.xpath("//button[text()='Click for JS Confirm']")).click();
        driver.switchTo().alert().dismiss();

        assertEquals("You clicked: Cancel",
                driver.findElement(By.id("result")).getText());
    }

    @Test
    @DisplayName("prompt(): sendKeys al alert antes de accept -> #result con el texto")
    void promptEnviaTexto() {
        driver.findElement(By.xpath("//button[text()='Click for JS Prompt']")).click();

        Alert alerta = driver.switchTo().alert();
        alerta.sendKeys("QE Academy");   // se escribe SOBRE el alert, no en el DOM
        alerta.accept();

        assertEquals("You entered: QE Academy",
                driver.findElement(By.id("result")).getText());
    }

    @Test
    @DisplayName("Alert sin manejar -> el SIGUIENTE comando lanza UnhandledAlertException")
    void alertNoManejadaRompeElSiguienteComando() {
        driver.findElement(By.xpath("//button[text()='Click for JS Alert']")).click();

        // Dejamos el alert ABIERTO y ejecutamos otro comando: Chrome lo descarta y avisa.
        assertThrows(UnhandledAlertException.class,
                () -> driver.findElement(By.id("result")).getText(),
                "un alert abierto debe reventar el siguiente comando con UnhandledAlertException");
    }
}
