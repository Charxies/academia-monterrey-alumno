package com.taskflow.qa.practicas;

import com.taskflow.qa.utils.Paginas;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * MP-6 — ERROR INTENCIONAL 3: la clase de framework que se regenera (10 min).
 *
 * perfil-v1.html tiene el botón con class="css-a1b2c3" (clase AUTOGENERADA estilo
 * styled-components/emotion) Y data-testid="btn-guardar". Se "despliega la v2"
 * (perfil-v2.html): MISMA UI, pero la clase se regeneró a css-x9y8z7. El selector por
 * clase REVIENTA; el [data-testid='btn-guardar'] sobrevive en ambas.
 *
 * Moraleja: las clases de framework NO son tuyas (cambian en cada build); el data-testid
 * SÍ es tuyo (contrato con el dev). Este contraste es el corazón del "selector de oro".
 */
class MP06ClaseFrameworkTest {

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
    @DisplayName("En v1 la clase css-a1b2c3 localiza el botón (todavía)")
    void laClaseDeFrameworkFuncionaEnV1() {
        driver.get(Paginas.perfilV1());
        assertEquals("Guardar cambios",
                driver.findElement(By.cssSelector(".css-a1b2c3")).getText());
    }

    @Test
    @DisplayName("En v2 la MISMA clase css-a1b2c3 ya no existe: selector roto")
    void laClaseDeFrameworkSeRompeEnV2() {
        driver.get(Paginas.perfilV2());
        // La clase se regeneró a css-x9y8z7: el selector viejo matchea 0 elementos.
        assertTrue(driver.findElements(By.cssSelector(".css-a1b2c3")).isEmpty(),
                "en v2 la clase autogenerada cambió: .css-a1b2c3 ya no matchea nada");
    }

    @Test
    @DisplayName("[data-testid='btn-guardar'] sobrevive en v1 Y en v2")
    void elDataTestidSobreviveEnAmbasVersiones() {
        By botonEstable = By.cssSelector("[data-testid='btn-guardar']");

        driver.get(Paginas.perfilV1());
        assertEquals("Guardar cambios", driver.findElement(botonEstable).getText(),
                "el data-testid debe localizar el botón en v1");

        driver.get(Paginas.perfilV2());
        assertEquals("Guardar cambios", driver.findElement(botonEstable).getText(),
                "el MISMO data-testid debe localizar el botón en v2 (contrato estable)");
    }
}
