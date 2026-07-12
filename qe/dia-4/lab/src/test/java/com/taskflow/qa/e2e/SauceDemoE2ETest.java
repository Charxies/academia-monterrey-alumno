package com.taskflow.qa.e2e;

import com.taskflow.qa.utils.Paginas;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.time.Duration;

/**
 * INTEGRADOR — Flujo E2E de la tienda (saucedemo / tienda local) con explicit waits (y cero sleeps).
 *
 * 4 tests INDEPENDIENTES: cada uno abre su propio ChromeDriver en @BeforeEach y hace quit()
 * en @AfterEach. TODA espera es WebDriverWait + ExpectedConditions; cero sleep fijo; cero
 * implicit wait. Planos SIN POM a propósito: los locators de login se REPITEN en cada test —
 * ese es exactamente el dolor que D4 resuelve con Page Object Model.
 *
 * El TIMEOUT es una constante por clase (sí, duplicada respecto a las otras clases: parte del
 * dolor que D4 centraliza en un DriverFactory).
 */
class SauceDemoE2ETest {

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
    @DisplayName("1 — login inválido muestra el mensaje de error exacto")
    void loginInvalidoMuestraError() {
        By error = By.cssSelector("h3[data-test='error']");

        // Caso A: usuario bloqueado.
        driver.get(Paginas.tienda());
        driver.findElement(By.id("user-name")).sendKeys("locked_out_user");
        driver.findElement(By.id("password")).sendKeys("secret_sauce");
        driver.findElement(By.id("login-button")).click();
        assertEquals("Epic sadface: Sorry, this user has been locked out.",
                wait.until(ExpectedConditions.visibilityOfElementLocated(error)).getText());

        // Caso B: password incorrecta (re-navegamos para limpiar el formulario).
        driver.get(Paginas.tienda());
        driver.findElement(By.id("user-name")).sendKeys("standard_user");
        driver.findElement(By.id("password")).sendKeys("clave-incorrecta");
        driver.findElement(By.id("login-button")).click();
        assertEquals("Epic sadface: Username and password do not match any user in this service",
                wait.until(ExpectedConditions.visibilityOfElementLocated(error)).getText());
    }

    @Test
    @DisplayName("2 — login válido entra al inventario (título 'Products' + 6 items)")
    void loginValidoEntraAlInventario() {
        driver.get(Paginas.tienda());
        driver.findElement(By.id("user-name")).sendKeys("standard_user");
        driver.findElement(By.id("password")).sendKeys("secret_sauce");
        driver.findElement(By.id("login-button")).click();

        wait.until(ExpectedConditions.textToBe(By.cssSelector("[data-test='title']"), "Products"));
        assertEquals(6, driver.findElements(By.cssSelector("[data-test='inventory-item']")).size(),
                "el inventario debería mostrar los 6 productos");
    }

    @Test
    @DisplayName("3 — agrega 2 productos: badge '2' y ambos nombres en el carrito")
    void agregarDosProductosYVerificarCarrito() {
        driver.get(Paginas.tienda());
        driver.findElement(By.id("user-name")).sendKeys("standard_user");
        driver.findElement(By.id("password")).sendKeys("secret_sauce");
        driver.findElement(By.id("login-button")).click();
        wait.until(ExpectedConditions.textToBe(By.cssSelector("[data-test='title']"), "Products"));

        driver.findElement(By.id("add-to-cart-sauce-labs-backpack")).click();
        driver.findElement(By.id("add-to-cart-sauce-labs-bike-light")).click();
        wait.until(ExpectedConditions.textToBe(
                By.cssSelector("[data-test='shopping-cart-badge']"), "2"));

        driver.findElement(By.cssSelector("[data-test='shopping-cart-link']")).click();
        wait.until(ExpectedConditions.textToBe(By.cssSelector("[data-test='title']"), "Your Cart"));

        // Streams sobre la lista de items del carrito (ya los dominan).
        List<String> nombres = driver.findElements(
                        By.cssSelector("[data-test='inventory-item-name']")).stream()
                .map(WebElement::getText)
                .toList();

        assertAll("los dos productos agregados están en el carrito",
                () -> assertTrue(nombres.contains("Sauce Labs Backpack"), "faltó Backpack: " + nombres),
                () -> assertTrue(nombres.contains("Sauce Labs Bike Light"), "faltó Bike Light: " + nombres));
    }

    @Test
    @DisplayName("4 — checkout hasta confirmación con assert del total CALCULADO")
    void checkoutHastaConfirmacion() {
        // login
        driver.get(Paginas.tienda());
        driver.findElement(By.id("user-name")).sendKeys("standard_user");
        driver.findElement(By.id("password")).sendKeys("secret_sauce");
        driver.findElement(By.id("login-button")).click();
        wait.until(ExpectedConditions.textToBe(By.cssSelector("[data-test='title']"), "Products"));

        // 2 productos al carrito
        driver.findElement(By.id("add-to-cart-sauce-labs-backpack")).click();
        driver.findElement(By.id("add-to-cart-sauce-labs-bike-light")).click();
        wait.until(ExpectedConditions.textToBe(
                By.cssSelector("[data-test='shopping-cart-badge']"), "2"));

        // carrito -> checkout: datos
        driver.findElement(By.cssSelector("[data-test='shopping-cart-link']")).click();
        wait.until(ExpectedConditions.textToBe(By.cssSelector("[data-test='title']"), "Your Cart"));
        driver.findElement(By.id("checkout")).click();
        wait.until(ExpectedConditions.textToBe(
                By.cssSelector("[data-test='title']"), "Checkout: Your Information"));
        driver.findElement(By.id("first-name")).sendKeys("Ada");
        driver.findElement(By.id("last-name")).sendKeys("Lovelace");
        driver.findElement(By.id("postal-code")).sendKeys("64000");
        driver.findElement(By.id("continue")).click();

        // resumen: TOTAL CALCULADO (no hardcodeado). Se leen los precios y se suman.
        wait.until(ExpectedConditions.textToBe(
                By.cssSelector("[data-test='title']"), "Checkout: Overview"));
        double sumaPrecios = driver.findElements(
                        By.cssSelector("[data-test='inventory-item-price']")).stream()
                .mapToDouble(e -> parseDinero(e.getText()))
                .sum();
        double itemTotal = parseDinero(driver.findElement(
                By.cssSelector("[data-test='subtotal-label']")).getText());
        assertEquals(sumaPrecios, itemTotal, 0.001,
                "el 'Item total' debe igualar la suma de los precios de los items");

        // Finish -> confirmación
        driver.findElement(By.id("finish")).click();
        wait.until(ExpectedConditions.textToBePresentInElementLocated(
                By.cssSelector("[data-test='complete-header']"), "Thank you for your order!"));
    }

    /** Extrae el importe numérico de un texto como "$29.99" o "Item total: $39.98". */
    private static double parseDinero(String texto) {
        return Double.parseDouble(texto.replaceAll("[^0-9.]", ""));
    }
}
