package com.taskflow.qa.practicas;

import com.taskflow.qa.utils.Paginas;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

// import org.openqa.selenium.By;
// import org.openqa.selenium.WebElement;
// import static org.junit.jupiter.api.Assertions.*;

/**
 * MP-3 — ERROR INTENCIONAL 1: el locator ambiguo (10 min).
 *
 * login.html tiene DOS &lt;input&gt;. Un locator laxo (By.tagName("input")) matchea a los
 * dos, pero findElement devuelve EL PRIMERO en silencio (el username): un test que
 * "quería" el password pasa escribiendo en el campo equivocado (verde mentiroso).
 *
 * La cura: MEDIR la unicidad con findElements(...).size() y afinar el locator hasta 1.
 * Moraleja: la unicidad se VERIFICA, no se asume ($$('input').length en la Console).
 */
class MP03LocatorAmbiguoTest {

    private WebDriver driver;

    @BeforeEach
    void abrirNavegador() {
        driver = new ChromeDriver();
        driver.get(Paginas.login());
    }

    @AfterEach
    void cerrarNavegador() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    @DisplayName("El locator ambiguo By.tagName('input') matchea 2 elementos: unicidad rota")
    void elLocatorAmbiguoMatcheaDos() {
        // TODO: cuenta driver.findElements(By.tagName("input")).size() y asserta que es 2.
    }

    @Test
    @DisplayName("findElement del locator ambiguo devuelve el PRIMERO (username), no el password")
    void findElementAmbiguoDevuelveElPrimero() {
        // TODO: findElement(By.tagName("input")) devuelve el primer match. Asserta que su
        //       getAttribute("id") == "username" (¡no es el password!).
    }

    @Test
    @DisplayName("Locator AFINADO By.name('password') -> matchea 1 y es el campo correcto")
    void elLocatorAfinadoEsUnicoYCorrecto() {
        // TODO: asserta que findElements(By.name("password")).size() == 1 y que el elemento
        //       tiene getAttribute("type") == "password".
    }
}
