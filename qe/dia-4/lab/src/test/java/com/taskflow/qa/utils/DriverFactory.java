package com.taskflow.qa.utils;

import org.openqa.selenium.WebDriver;

/**
 * DriverFactory (MP-7) — el "cómo corre" del framework: crea el WebDriver según Config.
 *
 * Selenium Manager (integrado en Selenium 4) resuelve el driver binario solo.
 *
 * TODO MP-7:
 *   - chrome/firefox según Config.browser();
 *   - headless → ChromeOptions "--headless=new" + "--window-size=1920,1080" (la UI está
 *     verificada a esa resolución); headed → maximize();
 *   - SIN espera implícita a propósito: NUNCA activarla en el WebDriver
 *     (no se mezcla con la espera explícita).
 */
public final class DriverFactory {

    private DriverFactory() {
        // Clase de utilería: no se instancia.
    }

    public static WebDriver create() {
        // TODO MP-7
        throw new UnsupportedOperationException("TODO MP-7");
    }
}
