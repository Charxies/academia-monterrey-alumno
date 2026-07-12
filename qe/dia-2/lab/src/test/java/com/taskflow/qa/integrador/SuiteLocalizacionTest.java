package com.taskflow.qa.integrador;

import com.taskflow.qa.utils.Paginas;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

// Imports que vas a necesitar (descoméntalos al usarlos):
// import org.openqa.selenium.By;
// import org.openqa.selenium.WebElement;
// import static org.junit.jupiter.api.Assertions.*;

/**
 * INTEGRADOR D2 — Suite de localización (15 objetivos). ESTE es tu entregable del día.
 *
 * Corre SIEMPRE contra las páginas locales vía Paginas.local("archivo.html"): no dependas
 * del flag -Dpaginas.local (los 2 objetivos con data-testid y la estabilidad lo exigen).
 *
 * Reglas del entregable:
 *   - Cada objetivo = 1 @Test que LOCALIZA y VALIDA algo del elemento (no basta "lo encontré").
 *   - TODO locator pasa por $$('css')/$x('xpath') en la Console (debe dar lo esperado) ANTES
 *     de escribirse en Java.
 *   - Cero XPath absolutos; cero Thread.sleep; nada de índices mágicos donde el objetivo
 *     dice "por contenido".
 *   - Llena la fila del README (# | Elemento | Locator | Estrategia | Por qué) al terminar
 *     CADA objetivo, no al final. La columna "por qué" se evalúa igual que el test verde.
 *
 * Entregable: push  feat: suite de localizacion d2 - 15 locators documentados
 */
class SuiteLocalizacionTest {

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

    // ---------------- login.html ----------------

    @Test
    @DisplayName("Obj 1 — username por By.id (id estable puesto por el dev)")
    void obj01_usernamePorId() {
        // TODO: driver.get(Paginas.local("login.html")); localiza el username con By.id
        //       y asserta isDisplayed().
    }

    @Test
    @DisplayName("Obj 2 — password por By.name (su type es 'password')")
    void obj02_passwordPorName() {
        // TODO: By.name("password"); asserta getAttribute("type") == "password".
    }

    @Test
    @DisplayName("Obj 3 — botón submit por CSS de atributo (no tiene id)")
    void obj03_botonSubmitPorCss() {
        // TODO: By.cssSelector("button[type='submit']"); asserta el texto del botón.
    }

    @Test
    @DisplayName("Obj 4 — #flash por By.id (su class trae el estado success/error)")
    void obj04_flashPorId() {
        // TODO: By.id("flash"); asserta que getAttribute("class") contiene "success" o "error".
    }

    // ---------------- checkboxes.html ----------------

    @Test
    @DisplayName("Obj 5 — AMBOS checkboxes por CSS de atributo + findElements (size==2)")
    void obj05_ambosCheckboxes() {
        // TODO: findElements(By.cssSelector("#checkboxes input[type='checkbox']")).size() == 2.
    }

    @Test
    @DisplayName("Obj 6 — SOLO el 2º checkbox por :nth-of-type(2) (viene marcado)")
    void obj06_segundoCheckbox() {
        // TODO: By.cssSelector("#checkboxes input[type='checkbox']:nth-of-type(2)");
        //       asserta isSelected() == true.
    }

    // ---------------- tabla.html ----------------

    @Test
    @DisplayName("Obj 7 — header 'Email' por CSS th:nth-child(3)")
    void obj07_headerEmail() {
        // TODO: By.cssSelector("#table1 thead th:nth-child(3)"); asserta getText() == "Email".
    }

    @Test
    @DisplayName("Obj 8 — celda del email exacto por XPath //td[text()='...']")
    void obj08_celdaEmailExacto() {
        // TODO: By.xpath("//td[text()='jsmith@gmail.com']"); asserta getText() igual al email.
    }

    @Test
    @DisplayName("Obj 9 — el 'Due' de la fila de jsmith por eje following-sibling")
    void obj09_duePorFollowingSibling() {
        // TODO: By.xpath("//td[text()='jsmith@gmail.com']/following-sibling::td[1]");
        //       asserta getText() == "$50.00".
    }

    @Test
    @DisplayName("Obj 10 — el 'delete' de la fila del apellido 'Doe' por ancestor::tr")
    void obj10_deletePorAncestorTr() {
        // TODO: By.xpath("//td[text()='Doe']/ancestor::tr//a[text()='delete']");
        //       asserta que getAttribute("href") NO está vacío.
    }

    @Test
    @DisplayName("Obj 11 — última fila por CSS tbody tr:last-child (apellido 'Conway')")
    void obj11_ultimaFila() {
        // TODO: By.cssSelector("#table1 tbody tr:last-child td:nth-child(1)");
        //       asserta getText() == "Conway".
    }

    // ---------------- dinamicos.html ----------------

    @Test
    @DisplayName("Obj 12 — botón 'Add Element' (sin id) por XPath de texto -> habilitado")
    void obj12_botonAddHabilitado() {
        // TODO: By.xpath("//button[text()='Add Element']"); asserta isEnabled().
    }

    @Test
    @DisplayName("Obj 13 — botones 'Delete': size==0 antes, size==3 tras 3 clicks en Add")
    void obj13_conteoBotonesDelete() {
        // TODO: asserta size()==0 de By.cssSelector("#elements button"); haz 3 clicks en Add;
        //       asserta size()==3 (DOM síncrono: sin waits).
    }

    @Test
    @DisplayName("Obj 14 — contador por data-testid (arranca en '0')")
    void obj14_contadorPorDataTestid() {
        // TODO: By.cssSelector("[data-testid='contador-elementos']"); asserta getText() == "0".
    }

    // ---------------- perfil-v1.html ----------------

    @Test
    @DisplayName("Obj 15 — link de contacto por By.linkText (su href es mailto)")
    void obj15_linkContactoPorLinkText() {
        // TODO: By.linkText("Contactar a Ana"); asserta getAttribute("href") == "mailto:ana.garcia@taskflow.mx".
    }

    // ---------------- STRETCH (opcional) ----------------
    // (1) método estático assertLocatorUnico(WebDriver, By) con findElements().size()==1.
    // (2) reescribe los objetivos 8-10 en CSS o justifica en el README por qué es imposible.
    // (3) record Objetivo(String nombre, String pagina, By locator) + UN test que recorre la
    //     lista de los objetivos de match único (streams + assertAll) verificando unicidad de golpe.
    //     En D4, cuando veas @ParameterizedTest, este es el candidato natural a revisitar.
}
