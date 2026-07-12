package com.taskflow.qa.esperas;

import com.taskflow.qa.utils.Paginas;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

// Imports que probablemente necesites (descoméntalos al usarlos):
// import org.openqa.selenium.By;
// import org.openqa.selenium.support.ui.ExpectedConditions;
// import static org.junit.jupiter.api.Assertions.*;

/**
 * MP-9 — Flujo corto de la tienda con esperas, para VER las waits trabajar (15 min).
 *
 * El MISMO test corre con standard_user y con performance_glitch_user: el código NO cambia,
 * solo TARDA más con el usuario lento (el espejo local retrasa 4 s en vez de 0.8 s por página).
 * Esa es la elegancia del explicit wait. @ParameterizedTest ya lo vieron en S3D1 (refuerzo).
 *
 * Flujo: login -> agregar 1 producto -> carrito -> checkout (datos) -> resumen, con assert del
 * TOTAL CALCULADO (nunca hardcodeado): sumar los precios de la página y comparar contra "Item total".
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
        // TODO 1: driver.get(Paginas.tienda()); login con {usuario}/secret_sauce (#user-name,
        //         #password, #login-button); wait textToBe([data-test='title'], "Products").
        // TODO 2: click #add-to-cart-sauce-labs-backpack; wait badge [data-test='shopping-cart-badge'] == "1".
        // TODO 3: click [data-test='shopping-cart-link']; wait title "Your Cart";
        //         click #checkout; wait title "Checkout: Your Information".
        // TODO 4: llena #first-name/#last-name/#postal-code; click #continue; wait title "Checkout: Overview".
        // TODO 5: suma los [data-test='inventory-item-price'] y compáralos con [data-test='subtotal-label']
        //         (parsea el "$"): assertEquals(suma, itemTotal, 0.001). NO hardcodees el monto.
    }
}
