package com.taskflow.qa.interaccion;

import com.taskflow.qa.utils.Paginas;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

// Imports que probablemente necesites (descoméntalos al usarlos):
// import org.openqa.selenium.Alert;
// import org.openqa.selenium.By;
// import org.openqa.selenium.UnhandledAlertException;
// import static org.junit.jupiter.api.Assertions.*;

/**
 * MP-4 — Alerts NATIVOS: accept/dismiss/getText/sendKeys + la UnhandledAlert provocada (20 min).
 *
 * Los alerts nativos viven FUERA del DOM: se manejan con driver.switchTo().alert()
 * (accept/dismiss/getText/sendKeys). Un alert abierto y NO manejado revienta el SIGUIENTE
 * comando con UnhandledAlertException. POR ESTO la UI de TaskFlow (D4) usa modales propios.
 * @AfterEach descarta defensivamente cualquier alert huérfano.
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
            // Defensivo (punto de dolor 8): cerrar cualquier alert pendiente antes de quit().
            try {
                driver.switchTo().alert().dismiss();
            } catch (NoAlertPresentException ignorado) {
                // lo normal: no había alert pendiente
            }
            driver.quit();
        }
    }

    @Test
    @DisplayName("alert() simple: getText + accept y assert de #result")
    void alertSimpleSeLeeYSeAcepta() {
        // TODO: click //button[text()='Click for JS Alert']; alerta=switchTo().alert();
        //       asserta getText()=="I am a JS Alert"; accept();
        //       asserta #result == "You successfully clicked an alert".
    }

    @Test
    @DisplayName("confirm() aceptado -> #result 'You clicked: Ok'")
    void confirmAceptar() {
        // TODO: click //button[text()='Click for JS Confirm']; switchTo().alert().accept();
        //       asserta #result == "You clicked: Ok".
    }

    @Test
    @DisplayName("confirm() cancelado -> #result 'You clicked: Cancel'")
    void confirmCancelar() {
        // TODO: mismo botón que arriba pero dismiss(); asserta #result == "You clicked: Cancel".
    }

    @Test
    @DisplayName("prompt(): sendKeys al alert antes de accept -> #result con el texto")
    void promptEnviaTexto() {
        // TODO: click //button[text()='Click for JS Prompt']; alerta.sendKeys("QE Academy");
        //       accept(); asserta #result == "You entered: QE Academy".
    }

    @Test
    @DisplayName("Alert sin manejar -> el SIGUIENTE comando lanza UnhandledAlertException")
    void alertNoManejadaRompeElSiguienteComando() {
        // TODO: click al botón del alert simple y, SIN manejarlo, usa assertThrows(
        //       UnhandledAlertException.class, () -> driver.findElement(By.id("result")).getText()).
    }
}
