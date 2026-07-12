package com.taskflow.qa.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * ProjectsPage — lista de proyectos (projects.html).
 *
 * Ejercita huecos de localización intencionales: el h3 y la descripción de cada card NO
 * tienen data-testid; se localizan de forma RELATIVA a project-card-{id} (lo aprendido en D2).
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
        // Landmark: el contenedor de la lista. Luego esperamos que el spinner de carga se vaya.
        waitVisible(PROJECT_LIST);
        waitSpinnerGone();
    }

    /** Consulta → dato: el username del navbar. */
    public String navUsername() {
        return waitVisible(NAV_USERNAME).getText();
    }

    /**
     * Crea un proyecto y PERMANECE en la lista (regla de retorno 2 → this).
     * La card nueva aparece SIN recargar: la esperamos por el texto de su h3 (hueco → XPath).
     */
    @Step("Crear proyecto «{name}»")
    public ProjectsPage createProject(String name, String description) {
        click(BTN_NEW_PROJECT);
        waitVisible(MODAL_PROJECT);
        type(INPUT_PROJECT_NAME, name);
        type(INPUT_PROJECT_DESCRIPTION, description);
        click(BTN_SAVE_PROJECT);
        waitGone(MODAL_PROJECT);      // el modal se ELIMINA del DOM al guardar
        waitVisible(cardByName(name)); // la card nueva se pinta tras el re-fetch
        return this;
    }

    /** Abre un proyecto por su nombre → NAVEGA al detalle (regla de retorno 1). */
    @Step("Abrir proyecto «{name}»")
    public ProjectDetailPage openProject(String name) {
        // Localizar la card por su h3 (hueco) y clickear su link-project-{id} vía ancestor::
        By link = By.xpath(
                "//h3[normalize-space()='" + name + "']"
              + "/ancestor::*[starts-with(@data-testid,'project-card-')]"
              + "//a[starts-with(@data-testid,'link-project-')]");
        click(link);
        return new ProjectDetailPage(driver);
    }

    /** Consulta → lista con los nombres de proyecto visibles. */
    public List<String> projectNames() {
        return driver.findElements(CARD_TITLES).stream()
                .map(WebElement::getText)
                .toList();
    }

    /** ¿Se muestra el estado vacío «No hay proyectos»? */
    public boolean emptyStateVisible() {
        return driver.findElements(EMPTY_STATE).stream().anyMatch(WebElement::isDisplayed);
    }

    /** Logout → NAVEGA de vuelta al login (regla de retorno 1). */
    @Step("Cerrar sesión")
    public LoginPage logout() {
        click(BTN_LOGOUT);
        return new LoginPage(driver);
    }

    /** XPath de una card por el texto de su h3 (hueco de localización intencional). */
    private static By cardByName(String name) {
        return By.xpath("//*[starts-with(@data-testid,'project-card-')]"
                + "[.//h3[normalize-space()='" + name + "']]");
    }
}
