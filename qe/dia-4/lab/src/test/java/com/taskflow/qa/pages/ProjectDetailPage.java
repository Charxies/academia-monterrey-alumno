package com.taskflow.qa.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.Select;

import java.util.List;

/**
 * ProjectDetailPage (MP-5) — detalle de un proyecto (project.html?id={id}).
 *
 * Tabla de tareas, filtro (select nativo), orden (dropdown CUSTOM), cambio de estado por fila
 * (select nativo) y el diálogo de confirmación de borrado (solo aparece aquí → métodos de esta
 * clase, no un page object aparte). Los ids se LEEN del DOM (task-row-{id}); nunca se hardcodean.
 *
 * Completa los cuerpos con TODO. Pistas: Select (clase de Selenium) para los &lt;select&gt;
 * nativos; secuencia click→presence→click para el dropdown custom.
 */
public class ProjectDetailPage extends BasePage {

    private static final By TASK_TABLE = byTestId("task-table");
    private static final By PROJECT_NAME = byTestId("project-name");
    private static final By DROPDOWN_SORT = byTestId("dropdown-sort");
    private static final By DROPDOWN_SORT_LIST = byTestId("dropdown-sort-list");
    private static final By FILTER_STATUS = byTestId("select-filter-status");
    private static final By BTN_NEW_TASK = byTestId("btn-new-task");
    private static final By DIALOG_CONFIRM = byTestId("dialog-confirm");
    private static final By CONFIRM_TEXT = byTestId("confirm-text");
    private static final By BTN_CONFIRM_YES = byTestId("btn-confirm-yes");
    private static final By BTN_CONFIRM_NO = byTestId("btn-confirm-no");
    private static final By EMPTY_STATE = byTestId("empty-state");
    private static final By TASK_ROWS = By.cssSelector("[data-testid^='task-row-']");

    // Hueco intencional: link de vuelta a la lista (sin testid) → By.linkText.
    private static final By BACK_LINK = By.linkText("← Proyectos");

    public ProjectDetailPage(WebDriver driver) {
        super(driver);
        // TODO MP-5: landmark = waitVisible(TASK_TABLE); luego waitSpinnerGone().
    }

    // ------------------------------------------------------------------
    // Lectura (consultas → dato; nunca un page object)
    // ------------------------------------------------------------------

    public String projectName() {
        // TODO MP-5: return waitVisible(PROJECT_NAME).getText();
        throw new UnsupportedOperationException("TODO MP-5");
    }

    /** Contador «N tareas»: hueco intencional → following-sibling::span del h1 project-name. */
    public String taskCounter() {
        // TODO MP-5: XPath following-sibling::span del project-name.
        throw new UnsupportedOperationException("TODO MP-5");
    }

    /** Lee los ids de las filas presentes (parsea task-row-{id}). */
    public List<Integer> taskRowIds() {
        // TODO MP-5: findElements(TASK_ROWS) → substring("task-row-") → Integer.parseInt.
        throw new UnsupportedOperationException("TODO MP-5");
    }

    /** Texto de una celda por columna (hueco: td:nth-child; 1=Título .. 6=Acciones). */
    public String cellText(int id, int col) {
        // TODO MP-5: waitVisible(By.cssSelector("[data-testid='task-row-"+id+"'] td:nth-child("+col+")")).getText()
        throw new UnsupportedOperationException("TODO MP-5");
    }

    /** Estado de la fila leído por la CLASE del badge (hueco): todo | in-progress | done. */
    public String statusBadge(int id) {
        // TODO MP-5: leer la clase del .badge de la celda 2 y devolver el sufijo badge-*.
        throw new UnsupportedOperationException("TODO MP-5");
    }

    // ------------------------------------------------------------------
    // Acciones
    // ------------------------------------------------------------------

    @Step("Abrir el modal de nueva tarea")
    public TaskModal openNewTaskModal() {
        // TODO MP-5/MP-6: click(BTN_NEW_TASK); return new TaskModal(driver);
        throw new UnsupportedOperationException("TODO MP-6");
    }

    @Step("Editar la tarea {id}")
    public TaskModal openEditTaskModal(int id) {
        // TODO stretch: click(byTestId("btn-edit-"+id)); return new TaskModal(driver);
        throw new UnsupportedOperationException("TODO stretch edición");
    }

    /** Cambia el estado con el select NATIVO (clase Select). PERMANECE → this. */
    @Step("Cambiar el estado de la tarea {id} a {status}")
    public ProjectDetailPage changeStatus(int id, String status) {
        // TODO MP-5: new Select(waitVisible(byTestId("select-status-"+id))).selectByValue(status); return this;
        throw new UnsupportedOperationException("TODO MP-5");
    }

    /**
     * Ordena con el dropdown CUSTOM (NO es select). Secuencia: click botón → wait PRESENCE de
     * la lista → click opción → la lista se ELIMINA. option ∈ {title, priority, duedate}.
     */
    @Step("Ordenar por {option}")
    public ProjectDetailPage sortBy(String option) {
        // TODO MP-5: click(DROPDOWN_SORT); waitPresent(DROPDOWN_SORT_LIST);
        //            click(byTestId("sort-option-"+option)); waitGone(DROPDOWN_SORT_LIST); return this;
        throw new UnsupportedOperationException("TODO MP-5");
    }

    /** Filtra por estado con el select NATIVO. value ∈ ALL|TODO|IN_PROGRESS|DONE. */
    @Step("Filtrar por estado {value}")
    public ProjectDetailPage filterByStatus(String value) {
        // TODO MP-5: new Select(waitVisible(FILTER_STATUS)).selectByValue(value); return this;
        throw new UnsupportedOperationException("TODO MP-5");
    }

    /** Pide borrar → se INSERTA el diálogo de confirmación propio. PERMANECE → this. */
    @Step("Solicitar borrar la tarea {id}")
    public ProjectDetailPage deleteTask(int id) {
        // TODO MP-5: click(byTestId("btn-delete-"+id)); waitVisible(DIALOG_CONFIRM); return this;
        throw new UnsupportedOperationException("TODO MP-5");
    }

    public String confirmText() {
        // TODO MP-5: return waitVisible(CONFIRM_TEXT).getText();
        throw new UnsupportedOperationException("TODO MP-5");
    }

    @Step("Confirmar el borrado")
    public ProjectDetailPage confirmDelete() {
        // TODO MP-5: click(BTN_CONFIRM_YES); waitGone(DIALOG_CONFIRM); return this;
        throw new UnsupportedOperationException("TODO MP-5");
    }

    public ProjectDetailPage cancelDelete() {
        // TODO MP-5: click(BTN_CONFIRM_NO); waitGone(DIALOG_CONFIRM); return this;
        throw new UnsupportedOperationException("TODO MP-5");
    }

    public boolean waitTaskRowGone(int id) {
        // TODO MP-5: return waitGone(byTestId("task-row-"+id));
        throw new UnsupportedOperationException("TODO MP-5");
    }

    public boolean emptyStateVisible() {
        // TODO MP-5: findElements(EMPTY_STATE) ... anyMatch(isDisplayed)
        throw new UnsupportedOperationException("TODO MP-5");
    }

    /** Espera una fila NUEVA respecto a los ids conocidos y devuelve su id. */
    @Step("Esperar la fila nueva")
    public int waitNewRow(List<Integer> idsConocidos) {
        // TODO MP-5: wait.until(d -> hay algún id no conocido) y devolverlo.
        throw new UnsupportedOperationException("TODO MP-5");
    }

    /** Vuelve a la lista con «← Proyectos» (hueco → By.linkText) → NAVEGA. */
    @Step("Volver a la lista de proyectos")
    public ProjectsPage backToProjects() {
        // TODO integrador: click(BACK_LINK); return new ProjectsPage(driver);
        throw new UnsupportedOperationException("TODO integrador");
    }
}
