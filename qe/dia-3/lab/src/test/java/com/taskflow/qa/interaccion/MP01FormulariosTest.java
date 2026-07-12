package com.taskflow.qa.interaccion;

import com.taskflow.qa.utils.Paginas;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

// Imports que probablemente necesites (descoméntalos al usarlos):
// import org.openqa.selenium.By;
// import org.openqa.selenium.WebElement;
// import static org.junit.jupiter.api.Assertions.*;

/**
 * MP-1 — Texto: sendKeys/clear, el bug de NO limpiar, submit() vs click (20 min).
 *
 * Ayer ENCONTRAMOS elementos; hoy los USAMOS. El patrón canónico de un campo de texto es
 * SIEMPRE clear() -> sendKeys(texto). Saltarte clear() sobre un campo precargado CONCATENA
 * el texto (bug clásico), visible solo con getAttribute("value").
 *
 * Sin esperas (login.html y formulario.html son síncronos). Setup del driver duplicado a
 * propósito en cada clase MP (se extrae a BaseTest/DriverFactory en D4).
 */
class MP01FormulariosTest {

    private WebDriver driver;

    @BeforeEach
    void abrirNavegador() {
        driver = new ChromeDriver();
    }

    @AfterEach
    void cerrarNavegador() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    @DisplayName("clear() + sendKeys() en el login -> flash de éxito")
    void loginConClearYSendKeysMuestraExito() {
        // TODO 1: driver.get(Paginas.login()).
        // TODO 2: localiza username (By.id) y password (By.name); en cada uno clear() y sendKeys()
        //         (tomsmith / SuperSecretPassword!).
        // TODO 3: click al botón By.cssSelector("button[type='submit']").
        // TODO 4: asserta que By.id("flash").getText() contiene "secure area".
    }

    @Test
    @DisplayName("element.submit() envía el form igual que el click al botón")
    void submitFuncionaIgualQueClickAlBoton() {
        // TODO: repite el login pero en vez de click al botón, llama a password.submit();
        //       verifica el mismo flash de éxito (el handler vive en el evento submit del form).
    }

    @Test
    @DisplayName("BUG: sendKeys sin clear() sobre un campo precargado CONCATENA")
    void bugDeNoLimpiarConcatenaElTexto() {
        // TODO: driver.get(Paginas.formulario()); localiza By.id("nombre") (precargado
        //       "Texto de ejemplo"); SIN clear() haz sendKeys("Ana") y asserta que
        //       getAttribute("value") == "Texto de ejemploAna" (ese es el bug).
    }

    @Test
    @DisplayName("clear() -> sendKeys() escribe limpio y el resultado es el esperado")
    void clearAntesDeSendKeysEscribeLimpio() {
        // TODO: en formulario.html, clear()+sendKeys("Ana") en #nombre, sendKeys("30") en #edad,
        //       click en [data-testid='btn-enviar'] y asserta que #resultado == "Registrado: Ana (30)".
    }
}
