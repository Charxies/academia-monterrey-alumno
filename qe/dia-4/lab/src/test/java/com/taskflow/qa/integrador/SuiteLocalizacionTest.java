package com.taskflow.qa.integrador;

import com.taskflow.qa.utils.Paginas;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * INTEGRADOR D2 — Suite de localización (15 objetivos).
 *
 * Corre SIEMPRE contra las páginas locales vía Paginas.local(...): no depende del flag
 * -Dpaginas.local. Motivo: estabilidad y data-testid garantizados (the-internet cubre
 * 13/15 y queda como espejo opcional de práctica; 2 objetivos exigen data-testid).
 *
 * Cada objetivo = 1 @Test que LOCALIZA y VALIDA algo del elemento (no basta "lo encontré").
 * Regla de oro: todo locator se probó con $$('css')/$x('xpath') en la Console y devolvió
 * lo esperado ANTES de escribirse aquí. Cero XPath absolutos, cero sleep fijo.
 *
 * La justificación "por qué esta estrategia y no otra" de cada locator va en el README.
 *
 * Setup del driver duplicado a propósito (se extrae a BaseTest/DriverFactory en D4).
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

    // ==================================================================
    // login.html
    // ==================================================================

    @Test
    @DisplayName("Obj 1 — username por By.id (id estable puesto por el dev)")
    void obj01_usernamePorId() {
        driver.get(Paginas.local("login.html"));
        WebElement username = driver.findElement(By.id("username"));
        assertTrue(username.isDisplayed(), "el campo username debería estar visible");
    }

    @Test
    @DisplayName("Obj 2 — password por By.name (su type es 'password')")
    void obj02_passwordPorName() {
        driver.get(Paginas.local("login.html"));
        WebElement password = driver.findElement(By.name("password"));
        assertEquals("password", password.getAttribute("type"));
    }

    @Test
    @DisplayName("Obj 3 — botón submit por CSS de atributo (no tiene id)")
    void obj03_botonSubmitPorCss() {
        driver.get(Paginas.local("login.html"));
        WebElement boton = driver.findElement(By.cssSelector("button[type='submit']"));
        assertEquals("Login", boton.getText());
    }

    @Test
    @DisplayName("Obj 4 — #flash por By.id (su class trae el estado success/error)")
    void obj04_flashPorId() {
        driver.get(Paginas.local("login.html"));
        String clases = driver.findElement(By.id("flash")).getAttribute("class");
        assertTrue(clases.contains("success") || clases.contains("error"),
                "el #flash debe traer una clase de estado; vino: " + clases);
    }

    // ==================================================================
    // checkboxes.html
    // ==================================================================

    @Test
    @DisplayName("Obj 5 — AMBOS checkboxes por CSS de atributo + findElements (size==2)")
    void obj05_ambosCheckboxes() {
        driver.get(Paginas.local("checkboxes.html"));
        int cuantos = driver.findElements(By.cssSelector("#checkboxes input[type='checkbox']")).size();
        assertEquals(2, cuantos, "checkboxes.html tiene exactamente 2 checkboxes");
    }

    @Test
    @DisplayName("Obj 6 — SOLO el 2º checkbox por :nth-of-type(2) (viene marcado)")
    void obj06_segundoCheckbox() {
        driver.get(Paginas.local("checkboxes.html"));
        WebElement segundo = driver.findElement(
                By.cssSelector("#checkboxes input[type='checkbox']:nth-of-type(2)"));
        assertTrue(segundo.isSelected(), "el 2º checkbox viene pre-marcado");
    }

    // ==================================================================
    // tabla.html
    // ==================================================================

    @Test
    @DisplayName("Obj 7 — header 'Email' por CSS th:nth-child(3)")
    void obj07_headerEmail() {
        driver.get(Paginas.local("tabla.html"));
        WebElement header = driver.findElement(By.cssSelector("#table1 thead th:nth-child(3)"));
        assertEquals("Email", header.getText());
    }

    @Test
    @DisplayName("Obj 8 — celda del email exacto por XPath //td[text()='...']")
    void obj08_celdaEmailExacto() {
        driver.get(Paginas.local("tabla.html"));
        WebElement celda = driver.findElement(By.xpath("//td[text()='jsmith@gmail.com']"));
        assertEquals("jsmith@gmail.com", celda.getText());
    }

    @Test
    @DisplayName("Obj 9 — el 'Due' de la fila de jsmith por eje following-sibling")
    void obj09_duePorFollowingSibling() {
        driver.get(Paginas.local("tabla.html"));
        WebElement due = driver.findElement(
                By.xpath("//td[text()='jsmith@gmail.com']/following-sibling::td[1]"));
        assertEquals("$50.00", due.getText());
    }

    @Test
    @DisplayName("Obj 10 — el 'delete' de la fila del apellido 'Doe' por ancestor::tr")
    void obj10_deletePorAncestorTr() {
        driver.get(Paginas.local("tabla.html"));
        WebElement delete = driver.findElement(
                By.xpath("//td[text()='Doe']/ancestor::tr//a[text()='delete']"));
        String href = delete.getAttribute("href");
        assertTrue(href != null && !href.isBlank(), "el link delete debe tener href no vacío");
    }

    @Test
    @DisplayName("Obj 11 — última fila por CSS tbody tr:last-child (apellido 'Conway')")
    void obj11_ultimaFila() {
        driver.get(Paginas.local("tabla.html"));
        WebElement apellido = driver.findElement(
                By.cssSelector("#table1 tbody tr:last-child td:nth-child(1)"));
        assertEquals("Conway", apellido.getText());
    }

    // ==================================================================
    // dinamicos.html
    // ==================================================================

    @Test
    @DisplayName("Obj 12 — botón 'Add Element' (sin id) por XPath de texto -> habilitado")
    void obj12_botonAddHabilitado() {
        driver.get(Paginas.local("dinamicos.html"));
        WebElement add = driver.findElement(By.xpath("//button[text()='Add Element']"));
        assertTrue(add.isEnabled(), "el botón Add Element debe estar habilitado");
    }

    @Test
    @DisplayName("Obj 13 — botones 'Delete': size==0 antes, size==3 tras 3 clicks en Add")
    void obj13_conteoBotonesDelete() {
        driver.get(Paginas.local("dinamicos.html"));
        By botonesDelete = By.cssSelector("#elements button");

        assertEquals(0, driver.findElements(botonesDelete).size(), "antes de agregar: 0 botones");

        WebElement add = driver.findElement(By.xpath("//button[text()='Add Element']"));
        for (int i = 0; i < 3; i++) {
            add.click(); // DOM síncrono: el botón Delete existe inmediatamente, sin waits
        }
        assertEquals(3, driver.findElements(botonesDelete).size(), "tras 3 clicks: 3 botones Delete");
    }

    @Test
    @DisplayName("Obj 14 — contador por data-testid (arranca en '0')")
    void obj14_contadorPorDataTestid() {
        driver.get(Paginas.local("dinamicos.html"));
        WebElement contador = driver.findElement(By.cssSelector("[data-testid='contador-elementos']"));
        assertEquals("0", contador.getText(), "el contador arranca en 0");
    }

    // ==================================================================
    // perfil-v1.html
    // ==================================================================

    @Test
    @DisplayName("Obj 15 — link de contacto por By.linkText (su href es mailto)")
    void obj15_linkContactoPorLinkText() {
        driver.get(Paginas.local("perfil-v1.html"));
        WebElement link = driver.findElement(By.linkText("Contactar a Ana"));
        assertEquals("mailto:ana.garcia@taskflow.mx", link.getAttribute("href"));
    }

    // ==================================================================
    // STRETCH 1 — utilidad assertLocatorUnico (semilla del framework de D4)
    // STRETCH 3 — record Objetivo + UN test que barre todos los de match único
    // ==================================================================

    /** Verifica que un locator matchee EXACTAMENTE 1 elemento (la regla de oro, en código). */
    static void assertLocatorUnico(WebDriver driver, By locator) {
        int n = driver.findElements(locator).size();
        assertEquals(1, n, "el locator " + locator + " debería ser único, pero matcheó " + n);
    }

    /** Un objetivo de localización de match único: su página local y su locator. */
    record Objetivo(String nombre, String pagina, By locator) {}

    @Test
    @DisplayName("Stretch — todos los objetivos de match único son ÚNICOS (barrido con assertAll)")
    void stretch_todosLosObjetivosUnicos() {
        // Solo los de match único (se excluyen obj 5 =2 y obj 13 =variable, que son de conteo).
        List<Objetivo> objetivos = List.of(
                new Objetivo("obj1 username",     "login.html",     By.id("username")),
                new Objetivo("obj2 password",     "login.html",     By.name("password")),
                new Objetivo("obj3 submit",       "login.html",     By.cssSelector("button[type='submit']")),
                new Objetivo("obj4 flash",        "login.html",     By.id("flash")),
                new Objetivo("obj6 checkbox2",    "checkboxes.html", By.cssSelector("#checkboxes input[type='checkbox']:nth-of-type(2)")),
                new Objetivo("obj7 header email", "tabla.html",     By.cssSelector("#table1 thead th:nth-child(3)")),
                new Objetivo("obj8 celda email",  "tabla.html",     By.xpath("//td[text()='jsmith@gmail.com']")),
                new Objetivo("obj9 due",          "tabla.html",     By.xpath("//td[text()='jsmith@gmail.com']/following-sibling::td[1]")),
                new Objetivo("obj10 delete Doe",  "tabla.html",     By.xpath("//td[text()='Doe']/ancestor::tr//a[text()='delete']")),
                new Objetivo("obj11 ultima fila", "tabla.html",     By.cssSelector("#table1 tbody tr:last-child td:nth-child(1)")),
                new Objetivo("obj12 add",         "dinamicos.html", By.xpath("//button[text()='Add Element']")),
                new Objetivo("obj14 contador",    "dinamicos.html", By.cssSelector("[data-testid='contador-elementos']")),
                new Objetivo("obj15 link",        "perfil-v1.html", By.linkText("Contactar a Ana"))
        );

        // assertAll: reporta TODOS los fallos de golpe, no solo el primero (ya lo dominan de JUnit).
        assertAll(objetivos.stream().map(o -> () -> {
            driver.get(Paginas.local(o.pagina()));
            assertLocatorUnico(driver, o.locator());
        }));
    }
}
