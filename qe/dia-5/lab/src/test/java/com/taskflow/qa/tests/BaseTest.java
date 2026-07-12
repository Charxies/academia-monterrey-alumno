package com.taskflow.qa.tests;

import com.taskflow.qa.utils.DriverFactory;
import com.taskflow.qa.utils.ScreenshotOnFailure;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.WebDriver;

/**
 * BaseTest — el ciclo de vida común de las clases de tests de UI.
 *
 * @BeforeEach crea el driver con DriverFactory (headed/headless según Config); @AfterEach lo
 * cierra. La creación inline de ChromeDriver que se repetía en D3 quedó centralizada aquí.
 *
 * @ExtendWith(ScreenshotOnFailure.class): captura un screenshot en el instante del fallo y lo
 * adjunta a Allure. getDriver() es público porque esa extensión lo consume por reflexión.
 */
@ExtendWith(ScreenshotOnFailure.class)
public abstract class BaseTest {

    private WebDriver driver;

    @BeforeEach
    void abrirNavegador() {
        driver = DriverFactory.create();
    }

    @AfterEach
    void cerrarNavegador() {
        if (driver != null) {
            driver.quit();
        }
    }

    /** Público a propósito: lo lee ScreenshotOnFailure para capturar al fallo. */
    public WebDriver getDriver() {
        return driver;
    }
}
