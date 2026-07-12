package com.taskflow.qa.interaccion;

import com.taskflow.qa.utils.Paginas;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

// Imports que probablemente necesites (descoméntalos al usarlos):
// import org.openqa.selenium.By;
// import org.openqa.selenium.NoSuchElementException;
// import org.openqa.selenium.WebElement;
// import static org.junit.jupiter.api.Assertions.*;

/**
 * MP-5 — Frames/iframes: el NoSuchElement "mentiroso" + switch simétrico + anidados (20 min).
 *
 * El driver ve UN documento a la vez. Un elemento DENTRO de un <iframe> no se alcanza desde
 * el documento externo, aunque DevTools lo muestre.
 *
 * ERROR INTENCIONAL 2: buscar #tinymce SIN switchTo().frame() -> NoSuchElementException.
 * Diagnóstico: ¿está en un iframe? -> switchTo().frame(...), operar, defaultContent() de vuelta.
 * Simetría OBLIGATORIA frame -> defaultContent. Frames anidados = switch en cadena (top -> middle).
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
        // TODO: driver.get(Paginas.frames()); assertThrows(NoSuchElementException.class,
        //       () -> driver.findElement(By.id("tinymce"))) — vive dentro del iframe.
    }

    @Test
    @DisplayName("switch al iframe: clear + escribir, y defaultContent() para volver afuera")
    void escribeEnElIframeYRegresaAlDocumentoExterno() {
        // TODO 1: driver.get(Paginas.frames()); switchTo().frame("mce_0_ifr").
        // TODO 2: editor=By.id("tinymce"); editor.clear(); editor.sendKeys("Hola desde el frame");
        //         asserta que getText() contiene el texto.
        // TODO 3: switchTo().defaultContent(); asserta que el h3 externo contiene "iFrame".
    }

    @Test
    @DisplayName("Frames anidados: switch en cadena top -> middle para leer 'MIDDLE'")
    void framesAnidadosLleganAlMiddle() {
        // TODO: driver.get(Paginas.framesAnidados()); switchTo().frame("frame-top");
        //       switchTo().frame("frame-middle"); asserta By.id("content").getText()=="MIDDLE";
        //       switchTo().defaultContent().
    }
}
