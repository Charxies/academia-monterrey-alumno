package com.taskflow.qa.pages;

import com.taskflow.qa.utils.Config;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * BasePage — la raíz del framework POM. TODA página hereda de aquí.
 *
 * Concentra lo común: el driver, el WebDriverWait, el helper de localización por
 * data-testid y los waits/acciones que se repetían en cada test suelto de D1-D3.
 *
 * Reglas del framework que viven aquí:
 *   - Locators por data-testid vía {@link #byTestId(String)} (el "testid se copia, no se
 *     adivina"): centraliza en UN lugar el error de un testid mal escrito.
 *   - Los campos de localizador de las páginas hijas son 'private static final By' (LAZY:
 *     se buscan al usarse). NUNCA se guarda un WebElement en un campo (muere con cada
 *     re-render → StaleElementReferenceException).
 *   - Toda espera es EXPLÍCITA (WebDriverWait + ExpectedConditions). Implicit wait sigue
 *     DESACTIVADO (no se mezcla). Cero esperas fijas de tiempo (sin sleep a mano).
 *
 * El timeout se toma de Config (MP-7): mismo framework, otro ambiente, sin recompilar.
 */
public abstract class BasePage {

    protected final WebDriver driver;
    protected final WebDriverWait wait;

    // Localizadores globales (viven en páginas autenticadas): spinner y toast.
    private static final By SPINNER = byTestId("spinner");
    private static final By TOAST = byTestId("toast");

    protected BasePage(WebDriver driver) {
        this.driver = driver;
        // Timeout desde config (antes hardcodeado a 10 s en MP-1). El explicit wait retorna
        // en el instante en que se cumple: un timeout generoso no hace lenta la suite.
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(Config.timeoutSeconds()));
    }

    // ------------------------------------------------------------------
    // Localización
    // ------------------------------------------------------------------

    /** By por data-testid: el contrato de la UI. By.cssSelector("[data-testid='...']"). */
    protected static By byTestId(String testId) {
        return By.cssSelector("[data-testid='" + testId + "']");
    }

    // ------------------------------------------------------------------
    // Waits / acciones base (elegidas por lo que se HACE con el elemento)
    // ------------------------------------------------------------------

    /** Espera a que el elemento sea VISIBLE y lo devuelve (para leer/interactuar). */
    protected WebElement waitVisible(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    /** Espera solo la PRESENCIA en el DOM (aún invisible). Para elementos que nacen ocultos
     *  o que solo importan por existir (p.ej. login-error, que se INSERTA tras el delay). */
    protected WebElement waitPresent(By locator) {
        return wait.until(ExpectedConditions.presenceOfElementLocated(locator));
    }

    /** Espera a que el elemento sea CLICKEABLE y hace click. */
    protected void click(By locator) {
        wait.until(ExpectedConditions.elementToBeClickable(locator)).click();
    }

    /** Limpia y escribe (el bug clásico de D3: escribir sin limpiar concatena). */
    protected void type(By locator, String text) {
        WebElement el = waitVisible(locator);
        el.clear();
        el.sendKeys(text);
    }

    /** Espera a que el elemento DESAPAREZCA (invisibilidad / ausencia del DOM). */
    protected boolean waitGone(By locator) {
        return wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    // ------------------------------------------------------------------
    // Helpers globales: spinner y toasts (viven en BasePage porque son de TODA página)
    // ------------------------------------------------------------------

    /** El spinner se INSERTA al disparar el fetch y se ELIMINA al render: invisibility lo cubre
     *  (si ya no está, invisibilityOfElementLocated retorna true al instante). */
    public void waitSpinnerGone() {
        waitGone(SPINNER);
    }

    /** Espera a que el toast aparezca y devuelve su texto (visibility → asertar rápido: el
     *  toast se elimina solo del DOM a los 3 s). */
    @Step("Esperar el toast y leer su mensaje")
    public String waitToastShown() {
        return waitVisible(TOAST).getText();
    }

    /** ¿El toast visible es de error? (clase toast-error). */
    public boolean toastIsError() {
        String clases = waitVisible(TOAST).getAttribute("class");
        return clases != null && clases.contains("toast-error");
    }

    /** Espera a que el toast desaparezca del DOM (visibility → invisibility, E8). Útil antes
     *  de encadenar otra mutación: un toast vivo puede interceptar el siguiente click. */
    public void waitToastGone() {
        waitGone(TOAST);
    }
}
