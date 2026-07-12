package com.taskflow.qa.e2e;

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
// import org.openqa.selenium.WebElement;
// import org.openqa.selenium.support.ui.ExpectedConditions;
// import java.util.List;
// import static org.junit.jupiter.api.Assertions.*;

/**
 * INTEGRADOR — Flujo E2E de la tienda (saucedemo / tienda local) con explicit waits (y cero sleeps).
 *
 * TU ENTREGABLE del día. 4 tests INDEPENDIENTES: driver nuevo en @BeforeEach, quit() en
 * @AfterEach. TODA espera con WebDriverWait + ExpectedConditions; CERO sleep fijo; CERO
 * implicit wait. Planos SIN POM a propósito: los locators de login se REPITEN en cada test
 * (ese es el dolor que D4 resuelve con Page Object Model — no lo evites hoy).
 *
 * Credenciales: standard_user / locked_out_user / performance_glitch_user + secret_sauce.
 * Corre con -Dpaginas.local=true (tienda/ local) o sin el flag (saucedemo.com): mismos locators.
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
        // TODO: caso A locked_out_user/secret_sauce -> h3[data-test='error'] ==
        //       "Epic sadface: Sorry, this user has been locked out." (espera con
        //       visibilityOfElementLocated). Caso B standard_user + password mala ->
        //       "Epic sadface: Username and password do not match any user in this service".
        //       Re-navega (driver.get) entre casos para limpiar el formulario.
    }

    @Test
    @DisplayName("2 — login válido entra al inventario (título 'Products' + 6 items)")
    void loginValidoEntraAlInventario() {
        // TODO: login standard_user/secret_sauce; wait textToBe([data-test='title'], "Products");
        //       asserta que findElements([data-test='inventory-item']).size() == 6.
    }

    @Test
    @DisplayName("3 — agrega 2 productos: badge '2' y ambos nombres en el carrito")
    void agregarDosProductosYVerificarCarrito() {
        // TODO: login; add #add-to-cart-sauce-labs-backpack y #add-to-cart-sauce-labs-bike-light;
        //       wait textToBe([data-test='shopping-cart-badge'], "2"); click [data-test='shopping-cart-link'];
        //       wait title "Your Cart"; con streams sobre [data-test='inventory-item-name'] asserta
        //       que están "Sauce Labs Backpack" y "Sauce Labs Bike Light".
    }

    @Test
    @DisplayName("4 — checkout hasta confirmación con assert del total CALCULADO")
    void checkoutHastaConfirmacion() {
        // TODO 1: login; agrega 2 productos; ve al carrito; click #checkout;
        //         wait title "Checkout: Your Information"; llena #first-name/#last-name/#postal-code;
        //         click #continue; wait title "Checkout: Overview".
        // TODO 2: suma los [data-test='inventory-item-price'] y compáralos con el importe de
        //         [data-test='subtotal-label'] (assertEquals con delta) — NO hardcodees "$xx.xx".
        // TODO 3: click #finish; wait textToBePresentInElementLocated([data-test='complete-header'],
        //         "Thank you for your order!").
    }
}
