package com.taskflow.qa.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * BasePage — la raíz del framework POM (se construye en MP-1). TODA página hereda de aquí.
 *
 * Se te entrega COMPLETA como referencia: es la plomería sobre la que escribirás LoginPage,
 * ProjectsPage, ProjectDetailPage y TaskModal. Estúdiala; el único TODO es el de MP-7.
 *
 * Reglas del framework que viven aquí:
 *   - Locators por data-testid vía {@link #byTestId(String)} (el "testid se copia, no se
 *     adivina"): centraliza en UN lugar el error de un testid mal escrito.
 *   - Los campos de localizador de las páginas hijas son 'private static final By' (LAZY:
 *     se buscan al usarse). NUNCA se guarda un WebElement en un campo (muere con cada
 *     re-render → StaleElementReferenceException).
 *   - Toda espera es EXPLÍCITA (WebDriverWait + ExpectedConditions). Implicit wait sigue
 *     DESACTIVADO (no se mezcla). Cero esperas fijas de tiempo (sin sleep a mano).
 */
public abstract class BasePage {

    protected final WebDriver driver;
    protected final WebDriverWait wait;

    // Localizadores globales (viven en páginas autenticadas): spinner y toast.
    private static final By SPINNER = byTestId("spinner");
    private static final By TOAST = byTestId("toast");

    protected BasePage(WebDriver driver) {
        this.driver = driver;
        // TODO MP-7: sacar este 10 a config (Config.timeoutSeconds()) — "en PM lo sacamos a
        //            config". Por ahora, hardcodeado como en MP-1.
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
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

    /** El spinner se INSERTA al disparar el fetch y se ELIMINA al render: invisibility lo cubre. */
    public void waitSpinnerGone() {
        waitGone(SPINNER);
    }

    /** Espera a que el toast aparezca y devuelve su texto (se elimina solo a los 3 s). */
    @Step("Esperar el toast y leer su mensaje")
    public String waitToastShown() {
        return waitVisible(TOAST).getText();
    }

    /** ¿El toast visible es de error? (clase toast-error). */
    public boolean toastIsError() {
        String clases = waitVisible(TOAST).getAttribute("class");
        return clases != null && clases.contains("toast-error");
    }

    /** Espera a que el toast desaparezca del DOM (visibility → invisibility, E8). */
    public void waitToastGone() {
        waitGone(TOAST);
    }
}
