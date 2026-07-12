package com.taskflow.qa.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.Select;

/**
 * TaskModal (MP-6) — page object de COMPONENTE: el modal de crear/editar tarea.
 *
 * Setters FLUENT (→ this) que se encadenan; save() cierra el flujo (→ ProjectDetailPage).
 * Completa los cuerpos con TODO.
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
        // TODO MP-6: landmark = waitVisible(MODAL_TASK) (el modal se INSERTA al abrir).
    }

    public String modalTitle() {
        // TODO MP-6: return waitVisible(MODAL_TITLE).getText();
        throw new UnsupportedOperationException("TODO MP-6");
    }

    @Step("Escribir el título «{title}»")
    public TaskModal title(String title) {
        // TODO MP-6: type(INPUT_TITLE, title); return this;
        throw new UnsupportedOperationException("TODO MP-6");
    }

    public TaskModal description(String description) {
        // TODO MP-6: type(INPUT_DESCRIPTION, description); return this;
        throw new UnsupportedOperationException("TODO MP-6");
    }

    /** Prioridad con la clase Select (select nativo). priority ∈ LOW|MED|HIGH. */
    public TaskModal priority(String priority) {
        // TODO MP-6: new Select(waitVisible(SELECT_PRIORITY)).selectByValue(priority); return this;
        throw new UnsupportedOperationException("TODO MP-6");
    }

    public TaskModal dueDate(String yyyyMmDd) {
        // TODO MP-6: type(INPUT_DUEDATE, yyyyMmDd); return this;
        throw new UnsupportedOperationException("TODO MP-6");
    }

    public TaskModal assignee(String userId) {
        // TODO MP-6: type(INPUT_ASSIGNEE, userId); return this;
        throw new UnsupportedOperationException("TODO MP-6");
    }

    /** Guarda con éxito → el modal se ELIMINA del DOM → NAVEGA de vuelta al detalle. */
    @Step("Guardar la tarea")
    public ProjectDetailPage save() {
        // TODO MP-6: click(BTN_SAVE); waitGone(MODAL_TASK); return new ProjectDetailPage(driver);
        throw new UnsupportedOperationException("TODO MP-6");
    }

    /** Guarda esperando un error 400 → el modal PERMANECE con modal-task-error. → this. */
    @Step("Guardar esperando error de validación")
    public TaskModal saveExpectingError() {
        // TODO MP-6: click(BTN_SAVE); waitVisible(MODAL_ERROR); return this;
        throw new UnsupportedOperationException("TODO MP-6");
    }

    public String errorMessage() {
        // TODO MP-6: return waitVisible(MODAL_ERROR).getText();
        throw new UnsupportedOperationException("TODO MP-6");
    }

    /** Cancela → el modal se ELIMINA sin llamar al API → NAVEGA de vuelta al detalle. */
    public ProjectDetailPage cancel() {
        // TODO MP-6: click(BTN_CANCEL); waitGone(MODAL_TASK); return new ProjectDetailPage(driver);
        throw new UnsupportedOperationException("TODO MP-6");
    }
}
