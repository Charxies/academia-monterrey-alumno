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
import static org.junit.jupiter.api.Assertions.fail;

/**
 * MP-7 — Ciclo de vida del driver, con dolor medible (15 min).
 *
 * Regla del framework que vas a descubrir aquí: DRIVER FRESCO POR TEST
 * (@BeforeEach crea, @AfterEach hace quit()).
 *
 * Los dos anti-patrones de la clase quedan @Disabled: se habilitan SOLO en la
 * demo del instructor para medir el dolor (procesos zombie / flakiness).
 */
class MP07CicloDriverTest {

    private WebDriver driver;

    @BeforeEach
    void nuevoDriver() {
        // TODO 1: driver = new ChromeDriver();
    }

    @AfterEach
    void cerrarDriver() {
        // TODO 2: if (driver != null) driver.quit();
        //   quit() mata la sesión W3C + el proceso, y corre aunque el test falle.
    }

    @Test
    @DisplayName("Driver fresco #1: navega a login y NO arrastra estado de otro test")
    void driverFrescoNavegaALogin() {
        // TODO 3: driver.get(Paginas.login());
        //         assertTrue(driver.getCurrentUrl().contains("login"));
        fail("TODO 3: implementa el test y borra este fail");
    }

    @Test
    @DisplayName("Driver fresco #2: navega a la home y NO arrastra estado de otro test")
    void driverFrescoNavegaAHome() {
        // TODO 4: driver.get(Paginas.home());
        //         assertEquals("The Internet", driver.getTitle());
        fail("TODO 4: implementa el test y borra este fail");
    }

    // ---------------------------------------------------------------------
    // ANTI-PATRONES (demo del instructor). @Disabled = la suite queda verde.
    // ---------------------------------------------------------------------

    /**
     * ERROR INTENCIONAL 2 — sin quit(): procesos chrome zombie.
     * En la demo: habilitar, correr 3 veces y contar los procesos chrome vivos
     * en el Monitor de Actividad / Administrador de tareas. Cada corrida deja uno.
     */
    @Test
    @Disabled("anti-patrón (error 2): sin quit() deja procesos chrome zombie — habilitar solo en demo")
    @DisplayName("ANTI-PATRÓN: driver sin quit() -> zombies acumulados")
    void antipatronSinQuitDejaZombies() {
        // DEMO: crear un ChromeDriver local, navegar, aserter el título y NO llamar quit().
        WebDriver huerfano = new ChromeDriver();
        huerfano.get(Paginas.home());
        assertEquals("The Internet", huerfano.getTitle());
        // A PROPÓSITO no hay huerfano.quit(): ese es el bug que se mide.
    }

    /**
     * ERROR INTENCIONAL 3 — driver static compartido sin limpiar estado = FLAKY.
     * Los dos tests comparten este WebDriver static. Uno deja el navegador en
     * /login; el otro asume estar en la home. Según el orden, el segundo pasa o
     * falla. La cura: driver fresco por test (los @Test de arriba).
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
        assertEquals("The Internet", compartido.getTitle());
    }

    // Bonus (spec MP-7): close() cierra la ventana ACTUAL (el proceso puede seguir
    // vivo); quit() termina la sesión y mata los procesos. En el framework SIEMPRE quit().
}
