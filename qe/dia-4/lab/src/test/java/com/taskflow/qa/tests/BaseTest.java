package com.taskflow.qa.tests;

import com.taskflow.qa.utils.ScreenshotOnFailure;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.WebDriver;

/**
 * BaseTest (MP-7) — el ciclo de vida común de las clases de tests de UI.
 *
 * @BeforeEach crea el driver con DriverFactory (centraliza la creación inline de D3);
 * @AfterEach lo cierra. @ExtendWith(ScreenshotOnFailure.class) engancha el screenshot al fallo
 * (MP-9). getDriver() es PÚBLICO porque esa extensión lo consume por reflexión.
 */
@ExtendWith(ScreenshotOnFailure.class)
public abstract class BaseTest {

    private WebDriver driver;

    @BeforeEach
    void abrirNavegador() {
        // TODO MP-7: driver = DriverFactory.create();
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
