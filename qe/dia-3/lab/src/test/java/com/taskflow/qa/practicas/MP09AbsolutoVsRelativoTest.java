package com.taskflow.qa.practicas;

import com.taskflow.qa.utils.Paginas;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
 * DoD del día: CERO XPath absolutos ACTIVOS. Por eso el único test con absoluto queda
 * @Disabled como EVIDENCIA: el instructor lo habilita en la demo para ver el rojo; en el
 * repo del alumno queda desactivado (la suite de referencia es verde).
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
        driver.get(Paginas.perfilV1());
        assertEquals(EMAIL,
                driver.findElement(By.xpath("//p[@data-testid='email-perfil']")).getText());
    }

    @Test
    @DisplayName("Relativo por data-testid: el MISMO locator sobrevive en v2 (el div extra no lo afecta)")
    void relativoSobreviveEnV2() {
        driver.get(Paginas.perfilV2());
        assertEquals(EMAIL,
                driver.findElement(By.xpath("//p[@data-testid='email-perfil']")).getText());
    }

    /**
     * ANTI-PATRÓN (error intencional 2). @Disabled para que la suite de referencia sea verde.
     *
     * En perfil-v1 el absoluto /html/body/div/div/p[1] SÍ apunta al email. En perfil-v2 el
     * &lt;div&gt; contenedor extra corre la ruta y el MISMO absoluto apunta al SUBTÍTULO
     * ("QA Engineer en TaskFlow"): el assert del email revienta. Habilita este test contra
     * v2 en la demo y observa el rojo: esa es la evidencia de por qué el absoluto es frágil.
     */
    @Test
    @Disabled("demo de fragilidad (error 2): el XPath absoluto se rompe en v2 — habilitar solo en la demo")
    @DisplayName("ANTI-PATRÓN: XPath absoluto — correcto en v1, roto en v2")
    void xpathAbsolutoSeRompeEnV2() {
        driver.get(Paginas.perfilV2());
        String texto = driver.findElement(By.xpath("/html/body/div/div/p[1]")).getText();
        assertEquals(EMAIL, texto,
                "el absoluto YA NO apunta al email en v2: ahora apunta a «" + texto + "»");
    }
}
