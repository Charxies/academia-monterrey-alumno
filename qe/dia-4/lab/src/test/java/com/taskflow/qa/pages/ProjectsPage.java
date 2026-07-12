package com.taskflow.qa.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.util.List;

/**
 * ProjectsPage (MP-4) — lista de proyectos (projects.html).
 *
 * Ejercita huecos intencionales: el h3 de cada card NO tiene testid; se localiza de forma
 * RELATIVA a project-card-{id} (lo de D2). Completa los cuerpos con TODO.
 */
public class ProjectsPage extends BasePage {

    private static final By PROJECT_LIST = byTestId("project-list");
    private static final By NAV_USERNAME = byTestId("nav-username");
    private static final By BTN_NEW_PROJECT = byTestId("btn-new-project");
    private static final By BTN_LOGOUT = byTestId("btn-logout");
    private static final By MODAL_PROJECT = byTestId("modal-project");
    private static final By INPUT_PROJECT_NAME = byTestId("input-project-name");
    private static final By INPUT_PROJECT_DESCRIPTION = byTestId("input-project-description");
    private static final By BTN_SAVE_PROJECT = byTestId("btn-save-project");
    private static final By EMPTY_STATE = byTestId("empty-state");

    // Los h3 de las cards son hueco intencional: se localizan por relación con la card.
    private static final By CARD_TITLES = By.cssSelector("[data-testid^='project-card-'] h3");

    public ProjectsPage(WebDriver driver) {
        super(driver);
        // TODO MP-2/MP-4: landmark = waitVisible(PROJECT_LIST); luego waitSpinnerGone().
    }

    /** Consulta → dato: el username del navbar. */
    public String navUsername() {
        // TODO MP-2: return waitVisible(NAV_USERNAME).getText();
        throw new UnsupportedOperationException("TODO MP-2");
    }

    /**
     * Crea un proyecto y PERMANECE en la lista (regla de retorno 2 → this).
     * La card nueva aparece SIN recargar: espérala por el texto de su h3 (hueco → XPath).
     */
    @Step("Crear proyecto «{name}»")
    public ProjectsPage createProject(String name, String description) {
        // TODO MP-4: click(BTN_NEW_PROJECT); waitVisible(MODAL_PROJECT); llenar name/desc;
        //            click(BTN_SAVE_PROJECT); waitGone(MODAL_PROJECT);
        //            waitVisible(cardByName(name)); return this;
        throw new UnsupportedOperationException("TODO MP-4");
    }

    /** Abre un proyecto por su nombre → NAVEGA al detalle (regla de retorno 1). */
    @Step("Abrir proyecto «{name}»")
    public ProjectDetailPage openProject(String name) {
        // TODO MP-4: localizar la card por su h3 y clickear su link-project-{id} vía ancestor::
        //            return new ProjectDetailPage(driver);
        throw new UnsupportedOperationException("TODO MP-4");
    }

    /** Consulta → lista con los nombres de proyecto visibles. */
    public List<String> projectNames() {
        // TODO MP-4: findElements(CARD_TITLES).stream().map(getText).toList();
        throw new UnsupportedOperationException("TODO MP-4");
    }

    /** ¿Se muestra el estado vacío «No hay proyectos»? */
    public boolean emptyStateVisible() {
        // TODO MP-4: findElements(EMPTY_STATE) ... anyMatch(isDisplayed)
        throw new UnsupportedOperationException("TODO MP-4");
    }

    /** Logout → NAVEGA de vuelta al login (regla de retorno 1). */
    @Step("Cerrar sesión")
    public LoginPage logout() {
        // TODO integrador: click(BTN_LOGOUT); return new LoginPage(driver);
        throw new UnsupportedOperationException("TODO integrador");
    }

    /** XPath de una card por el texto de su h3 (hueco de localización intencional). */
    private static By cardByName(String name) {
        return By.xpath("//*[starts-with(@data-testid,'project-card-')]"
                + "[.//h3[normalize-space()='" + name + "']]");
    }
}
