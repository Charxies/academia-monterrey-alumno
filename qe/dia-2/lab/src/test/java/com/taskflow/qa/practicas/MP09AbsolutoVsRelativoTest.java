package com.taskflow.qa.practicas;

import com.taskflow.qa.utils.Paginas;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

// import org.openqa.selenium.By;
// import static org.junit.jupiter.api.Assertions.*;

/**
 * MP-9 — ERROR INTENCIONAL 2: XPath absoluto vs relativo (15 min).
 *
 * Dos formas de localizar el MISMO email en perfil-v1.html:
 *   - ABSOLUTO   /html/body/div/div/p[1]      (lo que escupe "Copy XPath" de DevTools)
 *   - RELATIVO   //p[@data-testid='email-perfil']
 *
 * Se "despliega la v2" (perfil-v2.html): un &lt;div&gt; contenedor de layout extra envuelve
 * la tarjeta. El absoluto se CORRE y ahora apunta al subtítulo, no al email -> revienta.
 * El relativo por data-testid sobrevive intacto.
 *
 * DoD del día: CERO XPath absolutos ACTIVOS. El único test con absoluto queda @Disabled
 * como EVIDENCIA (el instructor lo habilita en la demo para ver el rojo).
 */
class MP09AbsolutoVsRelativoTest {

    /** El email real, ancla de todas las aserciones. */
    private static final String EMAIL = "ana.garcia@taskflow.mx";

    private WebDriver driver;

    @BeforeEach
    void abrirNavegador() {
        driver = new ChromeDriver();
    }

    @AfterEach
    void cerrarNavegador() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    @DisplayName("Relativo por data-testid: localiza el email en v1")
    void relativoLocalizaEmailEnV1() {
        // TODO: driver.get(Paginas.perfilV1()); localiza By.xpath("//p[@data-testid='email-perfil']")
        //       y asserta getText() == EMAIL.
    }

    @Test
    @DisplayName("Relativo por data-testid: el MISMO locator sobrevive en v2 (el div extra no lo afecta)")
    void relativoSobreviveEnV2() {
        // TODO: driver.get(Paginas.perfilV2()); con el MISMO locator relativo, asserta getText() == EMAIL.
    }

    /**
     * ANTI-PATRÓN (error intencional 2). Déjalo @Disabled: la suite de referencia queda verde.
     * En la DEMO, el instructor lo habilita contra v2 para ver el rojo: el absoluto
     * /html/body/div/div/p[1] apunta al email en v1 pero al SUBTÍTULO en v2.
     */
    @Test
    @Disabled("demo de fragilidad (error 2): el XPath absoluto se rompe en v2 — habilitar solo en la demo")
    @DisplayName("ANTI-PATRÓN: XPath absoluto — correcto en v1, roto en v2")
    void xpathAbsolutoSeRompeEnV2() {
        // TODO (solo demo): driver.get(Paginas.perfilV2());
        //   String texto = driver.findElement(By.xpath("/html/body/div/div/p[1]")).getText();
        //   assertEquals(EMAIL, texto);   // <- revienta en v2: 'texto' es el subtítulo
    }
}
