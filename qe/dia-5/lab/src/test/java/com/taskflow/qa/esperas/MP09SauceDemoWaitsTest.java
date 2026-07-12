package com.taskflow.qa.esperas;

import com.taskflow.qa.utils.Paginas;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * MP-9 — Flujo corto de la tienda con esperas, para VER las waits trabajar (15 min).
 *
 * El MISMO test corre con standard_user y con performance_glitch_user. El código NO cambia:
 * solo TARDA más con el usuario lento (cada página del espejo local retrasa 4 s en vez de
 * 0.8 s). Esa es la elegancia del explicit wait: sigue al instante en que el elemento está
 * listo, sin tocar un solo timeout. @ParameterizedTest ya lo vieron en S3D1: hoy es refuerzo.
 *
 * Flujo: login -> agregar 1 producto -> ir al carrito -> checkout (datos) -> resumen, con
 * assert del TOTAL CALCULADO (nunca hardcodeado): se suman los precios de la página y se
 * comparan contra el "Item total" mostrado.
 */
class MP09SauceDemoWaitsTest {

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

    @ParameterizedTest(name = "flujo con esperas — usuario: {0}")
    @ValueSource(strings = {"standard_user", "performance_glitch_user"})
    void flujoCompletoConEsperas(String usuario) {
        // --- login (locators duplicados a propósito: el dolor que D4 resuelve con POM) ---
        driver.get(Paginas.tienda());
        driver.findElement(By.id("user-name")).sendKeys(usuario);
        driver.findElement(By.id("password")).sendKeys("secret_sauce");
        driver.findElement(By.id("login-button")).click();
        wait.until(ExpectedConditions.textToBe(By.cssSelector("[data-test='title']"), "Products"));

        // --- agregar 1 producto y esperar el badge ---
        driver.findElement(By.id("add-to-cart-sauce-labs-backpack")).click();
        wait.until(ExpectedConditions.textToBe(
                By.cssSelector("[data-test='shopping-cart-badge']"), "1"));

        // --- ir al carrito ---
        driver.findElement(By.cssSelector("[data-test='shopping-cart-link']")).click();
        wait.until(ExpectedConditions.textToBe(By.cssSelector("[data-test='title']"), "Your Cart"));

        // --- checkout: datos de envío ---
        driver.findElement(By.id("checkout")).click();
        wait.until(ExpectedConditions.textToBe(
                By.cssSelector("[data-test='title']"), "Checkout: Your Information"));
        driver.findElement(By.id("first-name")).sendKeys("Ada");
        driver.findElement(By.id("last-name")).sendKeys("Lovelace");
        driver.findElement(By.id("postal-code")).sendKeys("64000");
        driver.findElement(By.id("continue")).click();

        // --- resumen: assert del total CALCULADO desde los precios de la página ---
        wait.until(ExpectedConditions.textToBe(
                By.cssSelector("[data-test='title']"), "Checkout: Overview"));

        double sumaPrecios = driver.findElements(
                        By.cssSelector("[data-test='inventory-item-price']")).stream()
                .mapToDouble(e -> parseDinero(e.getText()))
                .sum();

        String itemTotalLabel = driver.findElement(
                By.cssSelector("[data-test='subtotal-label']")).getText();   // "Item total: $29.99"
        double itemTotal = parseDinero(itemTotalLabel);

        assertEquals(sumaPrecios, itemTotal, 0.001,
                "el 'Item total' debe ser la suma de los precios mostrados (usuario " + usuario + ")");
    }

    /** Extrae el importe numérico de un texto como "$29.99" o "Item total: $29.99". */
    private static double parseDinero(String texto) {
        return Double.parseDouble(texto.replaceAll("[^0-9.]", ""));
    }
}
