package com.taskflow.qa.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.Select;

/**
 * TaskModal — page object de COMPONENTE: el modal de crear/editar tarea (mismo modal, el
 * título cambia). Merece clase propia porque es un formulario con vida propia; el diálogo de
 * confirmación, en cambio, vive en ProjectDetailPage (solo aparece ahí).
 *
 * Setters FLUENT (→ this): se encadenan para armar el formulario. save() cierra el flujo.
 */
public class TaskModal extends BasePage {

    private static final By MODAL_TASK = byTestId("modal-task");
    private static final By MODAL_TITLE = byTestId("modal-task-title");
    private static final By INPUT_TITLE = byTestId("input-task-title");
    private static final By INPUT_DESCRIPTION = byTestId("input-task-description");
    private static final By SELECT_PRIORITY = byTestId("select-task-priority");
    private static final By INPUT_DUEDATE = byTestId("input-task-duedate");
    private static final By INPUT_ASSIGNEE = byTestId("input-task-assignee");
    private static final By BTN_SAVE = byTestId("btn-save-task");
    private static final By BTN_CANCEL = byTestId("btn-cancel-task");
    private static final By MODAL_ERROR = byTestId("modal-task-error");

    public TaskModal(WebDriver driver) {
        super(driver);
        waitVisible(MODAL_TASK);   // landmark: el modal se INSERTA al abrir
    }

    /** Consulta → dato: «Nueva tarea» | «Editar tarea» según el modo. */
    public String modalTitle() {
        return waitVisible(MODAL_TITLE).getText();
    }

    // --- Setters fluent (permanecen en el modal → this) ---

    @Step("Escribir el título «{title}»")
    public TaskModal title(String title) {
        type(INPUT_TITLE, title);
        return this;
    }

    public TaskModal description(String description) {
        type(INPUT_DESCRIPTION, description);
        return this;
    }

    /** Prioridad con la clase Select (select nativo). priority ∈ LOW|MED|HIGH. */
    public TaskModal priority(String priority) {
        new Select(waitVisible(SELECT_PRIORITY)).selectByValue(priority);
        return this;
    }

    /** Fecha límite opcional (formato yyyy-MM-dd). */
    public TaskModal dueDate(String yyyyMmDd) {
        type(INPUT_DUEDATE, yyyyMmDd);
        return this;
    }

    /** Asignado opcional (id numérico de usuario; el API canónico no tiene GET /users). */
    public TaskModal assignee(String userId) {
        type(INPUT_ASSIGNEE, userId);
        return this;
    }

    /** Guarda con éxito → el modal se ELIMINA del DOM → NAVEGA de vuelta al detalle. */
    @Step("Guardar la tarea")
    public ProjectDetailPage save() {
        click(BTN_SAVE);
        waitGone(MODAL_TASK);
        return new ProjectDetailPage(driver);
    }

    /**
     * Guarda esperando un error 400 del API → el modal PERMANECE abierto con modal-task-error.
     * Así se asertan las reglas de negocio del capstone (title 3-120, dueDate, etc.) vía UI.
     */
    @Step("Guardar esperando error de validación")
    public TaskModal saveExpectingError() {
        click(BTN_SAVE);
        waitVisible(MODAL_ERROR);
        return this;
    }

    /** Mensaje de error del API mostrado dentro del modal. */
    public String errorMessage() {
        return waitVisible(MODAL_ERROR).getText();
    }

    /** Cancela → el modal se ELIMINA sin llamar al API → NAVEGA de vuelta al detalle. */
    public ProjectDetailPage cancel() {
        click(BTN_CANCEL);
        waitGone(MODAL_TASK);
        return new ProjectDetailPage(driver);
    }
}
