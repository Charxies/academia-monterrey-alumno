package com.taskflow.qa.utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

/**
 * DriverFactory — el "cómo corre" del framework: crea el WebDriver según Config.
 *
 * Un solo lugar decide navegador y headed/headless. Selenium Manager (integrado en Selenium 4)
 * resuelve el driver binario solo: no se descarga nada a mano.
 *
 * Headless fija --window-size=1920,1080 (la UI está verificada a esa resolución): así "pasa en
 * headed, falla en headless" deja de ocurrir por viewport. Probar headless HOY (-Dheadless=true)
 * porque mañana el CI corre así.
 */
public final class DriverFactory {

    private DriverFactory() {
        // Clase de utilería: no se instancia.
    }

    public static WebDriver create() {
        String browser = Config.browser();
        boolean headless = Config.headless();
        WebDriver driver;

        if ("firefox".equalsIgnoreCase(browser)) {
            FirefoxOptions options = new FirefoxOptions();
            if (headless) {
                options.addArguments("--headless");
                options.addArguments("--width=1920", "--height=1080");
            }
            driver = new FirefoxDriver(options);
        } else {
            ChromeOptions options = new ChromeOptions();
            if (headless) {
                options.addArguments("--headless=new");
                options.addArguments("--window-size=1920,1080");
            }
            driver = new ChromeDriver(options);
        }

        // SIN espera implícita a propósito: NO se mezcla con la espera explícita (produce
        // timeouts impredecibles). Nunca se activa la espera implícita del WebDriver.
        if (!headless) {
            driver.manage().window().maximize();
        }
        return driver;
    }
}
