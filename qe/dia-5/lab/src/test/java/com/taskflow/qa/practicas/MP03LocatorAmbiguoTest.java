package com.taskflow.qa.practicas;

import com.taskflow.qa.utils.Paginas;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * MP-3 — ERROR INTENCIONAL 1: el locator ambiguo (10 min).
 *
 * login.html tiene DOS &lt;input&gt;. Un locator laxo (By.tagName("input")) matchea a los
 * dos, pero findElement devuelve EL PRIMERO en silencio (el username). Un test que
 * "quería" el password escribe en el campo equivocado y aun así pasa: verde mentiroso.
 *
 * La cura no es adivinar: se MIDE la unicidad con findElements(...).size() y se afina el
 * locator hasta que sea 1. Moraleja del día: la unicidad se VERIFICA, no se asume
 * (en la Console: $$('input').length antes de copiar nada a Java).
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
        // Así se DETECTA la trampa antes de confiar en findElement: contar.
        int cuantos = driver.findElements(By.tagName("input")).size();
        assertEquals(2, cuantos,
                "By.tagName('input') es ambiguo en esta página: matchea username Y password");
    }

    @Test
    @DisplayName("findElement del locator ambiguo devuelve el PRIMERO (username), no el password")
    void findElementAmbiguoDevuelveElPrimero() {
        // findElement no falla: toma el primer match en silencio. Aquí lo demostramos:
        // el "input" que devuelve es el username (id=username), NO el password.
        WebElement primero = driver.findElement(By.tagName("input"));
        assertEquals("username", primero.getAttribute("id"),
                "findElement devuelve el PRIMER match: el username, aunque quisiéramos otro");
    }

    @Test
    @DisplayName("Locator AFINADO By.name('password') -> matchea 1 y es el campo correcto")
    void elLocatorAfinadoEsUnicoYCorrecto() {
        // La corrección: un locator único. Se verifica que su size sea 1...
        assertEquals(1, driver.findElements(By.name("password")).size(),
                "el locator afinado debe ser único (size == 1)");
        // ...y que apunte al elemento que de verdad queríamos.
        WebElement password = driver.findElement(By.name("password"));
        assertEquals("password", password.getAttribute("type"));
    }
}
