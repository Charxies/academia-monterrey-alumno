package com.taskflow.qa.tests.practicas;

import com.taskflow.qa.utils.Paginas;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * MP-7 — Ciclo de vida del driver, con dolor medible (15 min).
 *
 * Regla del framework que sale de aquí: DRIVER FRESCO POR TEST
 * (@BeforeEach crea, @AfterEach hace quit()).
 *
 * Los dos anti-patrones de la spec viven abajo como @Disabled: la suite de
 * referencia queda VERDE, y el instructor los habilita SOLO durante la demo
 * para que el grupo mida el dolor (procesos zombie / flakiness).
 */
class MP07CicloDriverTest {

    private WebDriver driver;

    @BeforeEach
    void nuevoDriver() {
        driver = new ChromeDriver();
    }

    @AfterEach
    void cerrarDriver() {
        if (driver != null) {
            driver.quit();   // mata la sesión W3C + el proceso; corre aunque el test falle
        }
    }

    // ---------------------------------------------------------------------
    // PATRÓN CORRECTO: cada test arranca con un driver limpio, sin importar
    // el orden en que Surefire los ejecute. Ninguno hereda estado del otro.
    // ---------------------------------------------------------------------

    @Test
    @DisplayName("Driver fresco #1: navega a login y NO arrastra estado de otro test")
    void driverFrescoNavegaALogin() {
        driver.get(Paginas.login());
        assertTrue(driver.getCurrentUrl().contains("login"));
    }

    @Test
    @DisplayName("Driver fresco #2: navega a la home y NO arrastra estado de otro test")
    void driverFrescoNavegaAHome() {
        driver.get(Paginas.home());
        assertEquals("The Internet", driver.getTitle());
    }

    // ---------------------------------------------------------------------
    // ANTI-PATRONES (demo). @Disabled para que la suite de referencia sea verde.
    // ---------------------------------------------------------------------

    /**
     * ERROR INTENCIONAL 2 — sin quit(): procesos chrome zombie.
     * Habilita este test, córrelo 3 veces y abre el Monitor de Actividad
     * (macOS) o el Administrador de tareas (Windows): cuenta los procesos
     * "chrome"/"chromedriver" acumulados y su RAM. Cada corrida deja uno vivo
     * porque NUNCA se llama quit(). Este driver es local a propósito: no lo
     * limpia el @AfterEach de la clase.
     */
    @Test
    @Disabled("anti-patrón (error 2): sin quit() deja procesos chrome zombie — habilitar solo en demo")
    @DisplayName("ANTI-PATRÓN: driver sin quit() -> zombies acumulados")
    void antipatronSinQuitDejaZombies() {
        WebDriver huerfano = new ChromeDriver();
        huerfano.get(Paginas.home());
        assertEquals("The Internet", huerfano.getTitle());
        // A PROPÓSITO no hay huerfano.quit(): ese es el bug que se mide en la demo.
    }

    /**
     * ERROR INTENCIONAL 3 — driver static compartido sin limpiar estado = FLAKY.
     * Los dos tests de abajo comparten el mismo WebDriver static. Uno deja el
     * navegador en /login; el otro ASUME estar en la home. Según el orden en que
     * corran, el segundo pasa o falla: eso es un test flaky. La cura es la regla
     * del framework: driver fresco por test (los @Test verdes de arriba).
     */
    private static WebDriver compartido;

    @Test
    @Disabled("anti-patrón (error 3): driver static compartido sin limpiar estado = flaky — habilitar solo en demo")
    @DisplayName("ANTI-PATRÓN A: deja el navegador en /login")
    void antipatronCompartidoDejaEnLogin() {
        if (compartido == null) {
            compartido = new ChromeDriver();
        }
        compartido.get(Paginas.login());
        assertTrue(compartido.getCurrentUrl().contains("login"));
    }

    @Test
    @Disabled("anti-patrón (error 3): driver static compartido sin limpiar estado = flaky — habilitar solo en demo")
    @DisplayName("ANTI-PATRÓN B: asume la home, pero el test A lo dejó en /login")
    void antipatronCompartidoAsumeHome() {
        if (compartido == null) {
            compartido = new ChromeDriver();
        }
        // Nunca navega a la home: confía en un estado que otro test debía dejar.
        // Si A corrió antes, seguimos en /login y este assert falla = flaky.
        assertEquals("The Internet", compartido.getTitle());
    }

    // Bonus (spec MP-7): close() cierra la ventana ACTUAL (el proceso puede seguir
    // vivo si quedan más ventanas); quit() termina la sesión y mata los procesos.
    // En el framework SIEMPRE quit().
}
