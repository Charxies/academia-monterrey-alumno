package com.taskflow.qa.practicas;

import com.taskflow.qa.utils.Paginas;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

// import org.openqa.selenium.By;
// import static org.junit.jupiter.api.Assertions.*;

/**
 * MP-6 — ERROR INTENCIONAL 3: la clase de framework que se regenera (10 min).
 *
 * perfil-v1.html tiene el botón con class="css-a1b2c3" (clase AUTOGENERADA estilo
 * styled-components/emotion) Y data-testid="btn-guardar". Se "despliega la v2"
 * (perfil-v2.html): MISMA UI, pero la clase se regeneró a css-x9y8z7. El selector por
 * clase REVIENTA; el [data-testid='btn-guardar'] sobrevive en ambas.
 *
 * Moraleja: las clases de framework NO son tuyas; el data-testid SÍ (contrato con el dev).
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
        // TODO: driver.get(Paginas.perfilV1()); localiza By.cssSelector(".css-a1b2c3")
        //       y asserta getText() == "Guardar cambios".
    }

    @Test
    @DisplayName("En v2 la MISMA clase css-a1b2c3 ya no existe: selector roto")
    void laClaseDeFrameworkSeRompeEnV2() {
        // TODO: driver.get(Paginas.perfilV2()); asserta que
        //       findElements(By.cssSelector(".css-a1b2c3")) está VACÍO (la clase se regeneró).
    }

    @Test
    @DisplayName("[data-testid='btn-guardar'] sobrevive en v1 Y en v2")
    void elDataTestidSobreviveEnAmbasVersiones() {
        // TODO: con el MISMO By.cssSelector("[data-testid='btn-guardar']"):
        //       - en Paginas.perfilV1() asserta getText() == "Guardar cambios"
        //       - en Paginas.perfilV2() asserta getText() == "Guardar cambios"
    }
}
