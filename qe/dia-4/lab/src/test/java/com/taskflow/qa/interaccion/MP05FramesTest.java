package com.taskflow.qa.interaccion;

import com.taskflow.qa.utils.Paginas;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * MP-5 — Frames/iframes: el NoSuchElement "mentiroso" + switch simétrico + anidados (20 min).
 *
 * El driver ve UN documento a la vez. Un elemento DENTRO de un <iframe> no se alcanza desde
 * el documento externo, aunque DevTools lo muestre.
 *
 * ERROR INTENCIONAL 2: buscar #tinymce SIN switchTo().frame() -> NoSuchElementException
 * "mentiroso". Diagnóstico: ¿está dentro de un iframe? -> switchTo().frame(...), operar, y
 * volver con defaultContent(). Simetría OBLIGATORIA: si entras a un frame, sales de él.
 * Frames anidados = switch en cadena (top -> middle).
 */
class MP05FramesTest {

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
    @DisplayName("Buscar el editor SIN switch -> NoSuchElementException (aunque DevTools lo muestre)")
    void buscarDentroDelIframeSinSwitchLanzaNoSuchElement() {
        driver.get(Paginas.frames());

        // #tinymce vive dentro del iframe: desde el documento externo NO existe.
        assertThrows(NoSuchElementException.class,
                () -> driver.findElement(By.id("tinymce")),
                "sin switchTo().frame() el elemento del iframe no se alcanza");
    }

    @Test
    @DisplayName("switch al iframe: clear + escribir, y defaultContent() para volver afuera")
    void escribeEnElIframeYRegresaAlDocumentoExterno() {
        driver.get(Paginas.frames());

        driver.switchTo().frame("mce_0_ifr");    // entramos al documento del iframe (id/name)

        WebElement editor = driver.findElement(By.id("tinymce"));
        editor.clear();
        editor.sendKeys("Hola desde el frame");
        assertTrue(editor.getText().contains("Hola desde el frame"),
                "el editor debería contener el texto escrito, fue: " + editor.getText());

        driver.switchTo().defaultContent();      // salimos: sin esto, el h3 externo no se ve

        // Prueba de que SÍ salimos: el título de la página exterior es alcanzable de nuevo.
        assertTrue(driver.findElement(By.tagName("h3")).getText().contains("iFrame"),
                "tras defaultContent() debe verse el h3 del documento externo");
    }

    @Test
    @DisplayName("Frames anidados: switch en cadena top -> middle para leer 'MIDDLE'")
    void framesAnidadosLleganAlMiddle() {
        driver.get(Paginas.framesAnidados());

        driver.switchTo().frame("frame-top");     // primer nivel
        driver.switchTo().frame("frame-middle");  // segundo nivel (relativo al top)

        assertEquals("MIDDLE", driver.findElement(By.id("content")).getText());

        driver.switchTo().defaultContent();       // simetría: volvemos a la raíz
    }
}
