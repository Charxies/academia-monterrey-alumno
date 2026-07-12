package com.taskflow.qa.interaccion;

import com.taskflow.qa.utils.Paginas;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * MP-1 — Texto: sendKeys/clear, el bug de NO limpiar, submit() vs click (20 min).
 *
 * Ayer ENCONTRAMOS elementos; hoy los USAMOS. Selenium dispara eventos reales de usuario
 * (no manipula el DOM a mano). El patrón canónico para un campo de texto es SIEMPRE:
 *   clear()  ->  sendKeys(texto)
 * Si te saltas clear() sobre un campo precargado, el texto se CONCATENA (bug clásico) —
 * y solo se ve leyendo getAttribute("value"), no getText().
 *
 * Sin esperas: login.html y formulario.html responden de forma síncrona (las esperas son
 * el tema de la tarde). Setup del driver duplicado a propósito (se extrae en D4).
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
        driver.get(Paginas.login());

        WebElement usuario = driver.findElement(By.id("username"));
        usuario.clear();                       // el campo viene vacío, pero clear() es el hábito
        usuario.sendKeys("tomsmith");

        WebElement clave = driver.findElement(By.name("password"));
        clave.clear();
        clave.sendKeys("SuperSecretPassword!");

        driver.findElement(By.cssSelector("button[type='submit']")).click();

        WebElement flash = driver.findElement(By.id("flash"));
        assertTrue(flash.isDisplayed(), "el flash debería mostrarse tras el submit");
        assertTrue(flash.getText().contains("secure area"),
                "el flash de éxito debería hablar del 'secure area', fue: " + flash.getText());
    }

    @Test
    @DisplayName("element.submit() envía el form igual que el click al botón")
    void submitFuncionaIgualQueClickAlBoton() {
        driver.get(Paginas.login());

        WebElement usuario = driver.findElement(By.id("username"));
        usuario.clear();
        usuario.sendKeys("tomsmith");
        WebElement clave = driver.findElement(By.name("password"));
        clave.clear();
        clave.sendKeys("SuperSecretPassword!");

        // submit() sobre cualquier elemento del form dispara el envío del FORM: mismo
        // resultado que hacer click al botón (el handler vive en el evento submit).
        clave.submit();

        WebElement flash = driver.findElement(By.id("flash"));
        assertTrue(flash.getText().contains("secure area"),
                "submit() debería producir el mismo flash de éxito que el click");
    }

    @Test
    @DisplayName("BUG: sendKeys sin clear() sobre un campo precargado CONCATENA")
    void bugDeNoLimpiarConcatenaElTexto() {
        driver.get(Paginas.formulario());

        WebElement nombre = driver.findElement(By.id("nombre"));   // precargado: "Texto de ejemplo"
        // A PROPÓSITO sin clear(): reproducimos el bug. El texto nuevo se pega al viejo.
        nombre.sendKeys("Ana");

        // getText() NO lo revela (los inputs no exponen su valor como texto): hay que leer value.
        assertEquals("Texto de ejemploAna", nombre.getAttribute("value"),
                "sin clear() el valor queda concatenado — ese es el bug didáctico de MP-1");
    }

    @Test
    @DisplayName("clear() -> sendKeys() escribe limpio y el resultado es el esperado")
    void clearAntesDeSendKeysEscribeLimpio() {
        driver.get(Paginas.formulario());

        WebElement nombre = driver.findElement(By.id("nombre"));
        nombre.clear();                        // la cura del bug de arriba
        nombre.sendKeys("Ana");
        driver.findElement(By.id("edad")).sendKeys("30");

        driver.findElement(By.cssSelector("[data-testid='btn-enviar']")).click();

        assertEquals("Registrado: Ana (30)",
                driver.findElement(By.id("resultado")).getText());
    }
}
