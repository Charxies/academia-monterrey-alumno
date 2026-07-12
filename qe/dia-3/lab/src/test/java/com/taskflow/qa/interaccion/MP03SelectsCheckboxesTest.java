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
// import org.openqa.selenium.WebElement;
// import org.openqa.selenium.support.ui.Select;
// import java.util.List;
// import static org.junit.jupiter.api.Assertions.*;

/**
 * MP-3 — Selects nativos, dropdown custom y checkboxes/radios idempotentes (20 min).
 *
 * Tres controles, tres técnicas:
 *   - <select> NATIVO  -> la clase Select (selectByVisibleText/Value/Index, getFirstSelectedOption,
 *                          getOptions + streams para asertar la lista).
 *   - dropdown CUSTOM  -> NO hay Select: patrón de 2 pasos (abrir + click a la opción por texto).
 *   - checkbox / radio -> click CONDICIONAL idempotente: if (!chk.isSelected()) chk.click().
 *                          "asegurar marcado" != "toggle".
 *
 * Sin esperas (páginas síncronas).
 */
class MP03SelectsCheckboxesTest {

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
    @DisplayName("Select nativo: selectByVisibleText / selectByValue / selectByIndex")
    void selectNativoPorTextoValueEIndice() {
        // TODO: driver.get(Paginas.dropdowns()); new Select(By.id("dropdown")).
        //       selectByVisibleText("Option 1") -> getFirstSelectedOption().getText()=="Option 1";
        //       selectByValue("2") -> "Option 2"; selectByIndex(1) -> "Option 1".
    }

    @Test
    @DisplayName("Select nativo: getOptions con streams para asertar la lista completa")
    void opcionesDelSelectConStreams() {
        // TODO: Select.getOptions().stream().map(WebElement::getText).toList();
        //       asserta que contiene "Option 1" y "Option 2".
    }

    @Test
    @DisplayName("Dropdown CUSTOM: patrón de 2 pasos (abrir + click a la opción por texto)")
    void dropdownCustomDosPasos() {
        // TODO 1: click en By.id("custom-trigger") (abre la lista).
        // TODO 2: click en By.xpath("//ul[@id='custom-opciones']/li[text()='Verde']").
        // TODO 3: asserta que #custom-seleccion == "Seleccionaste: Verde".
    }

    @Test
    @DisplayName("Checkboxes: click condicional idempotente (asegurar marcado != toggle)")
    void checkboxConClickCondicionalIdempotente() {
        // TODO: driver.get(Paginas.checkboxes()); findElements("#checkboxes input[type='checkbox']");
        //       para cada uno: if (!chk.isSelected()) chk.click(); asserta ambos isSelected()==true
        //       (el 1º viene desmarcado, el 2º ya marcado -> no se toca).
    }

    @Test
    @DisplayName("Radios: seleccionar uno deselecciona al del grupo (misma idea idempotente)")
    void radioSeleccionaUnoDelGrupo() {
        // TODO: en checkboxes.html, if(!#radio-enterprise.isSelected()) click; asserta que
        //       enterprise queda seleccionado y #radio-pro (checked inicial) deja de estarlo.
    }
}
