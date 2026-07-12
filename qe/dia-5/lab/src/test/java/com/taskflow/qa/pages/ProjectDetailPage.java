package com.taskflow.qa.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * ProjectDetailPage — detalle de un proyecto (project.html?id={id}): tabla de tareas,
 * filtro (select nativo), orden (dropdown CUSTOM), cambio de estado por fila (select nativo)
 * y el diálogo de confirmación de borrado (que SOLO aparece aquí → son métodos de esta clase,
 * no un page object aparte).
 *
 * Los ids de las filas se LEEN del DOM (task-row-{id}); NUNCA se hardcodean (H2 los regenera).
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
        waitVisible(TASK_TABLE);   // landmark
        waitSpinnerGone();         // esperar el fetch inicial de proyecto + tareas
    }

    // ------------------------------------------------------------------
    // Lectura (consultas → dato; nunca un page object)
    // ------------------------------------------------------------------

    /** Nombre del proyecto (h1). */
    public String projectName() {
        return waitVisible(PROJECT_NAME).getText();
    }

    /** Contador «N tareas»: hueco intencional, span hermano ADYACENTE del h1 → following-sibling. */
    public String taskCounter() {
        return waitVisible(By.xpath(
                "//*[@data-testid='project-name']/following-sibling::span")).getText();
    }

    /** Lee los ids de las filas presentes (parsea task-row-{id}). */
    public List<Integer> taskRowIds() {
        return driver.findElements(TASK_ROWS).stream()
                .map(row -> row.getDomAttribute("data-testid").substring("task-row-".length()))
                .map(Integer::parseInt)
                .toList();
    }

    /** Texto de una celda por columna (hueco: td:nth-child; 1=Título .. 6=Acciones). */
    public String cellText(int id, int col) {
        return waitVisible(By.cssSelector(
                "[data-testid='task-row-" + id + "'] td:nth-child(" + col + ")")).getText();
    }

    /** Estado de la fila leído por la CLASE del badge (hueco): todo | in-progress | done. */
    public String statusBadge(int id) {
        String clases = waitVisible(By.cssSelector(
                "[data-testid='task-row-" + id + "'] td:nth-child(2) .badge")).getAttribute("class");
        for (String c : clases.split("\\s+")) {
            if (c.startsWith("badge-")) {
                return c.substring("badge-".length());
            }
        }
        return "";
    }

    // ------------------------------------------------------------------
    // Acciones
    // ------------------------------------------------------------------

    /** Abre el modal de crear tarea → NAVEGA de contexto al componente TaskModal. */
    @Step("Abrir el modal de nueva tarea")
    public TaskModal openNewTaskModal() {
        click(BTN_NEW_TASK);
        return new TaskModal(driver);
    }

    /** Abre el modal de editar tarea (prellenado) → TaskModal. */
    @Step("Editar la tarea {id}")
    public TaskModal openEditTaskModal(int id) {
        click(byTestId("btn-edit-" + id));
        return new TaskModal(driver);
    }

    /**
     * Cambia el estado de una fila usando el select NATIVO (clase Select de Selenium).
     * PERMANECE en la página → this. (El badge/toast llegan tras el delay; el test los espera.)
     */
    @Step("Cambiar el estado de la tarea {id} a {status}")
    public ProjectDetailPage changeStatus(int id, String status) {
        new Select(waitVisible(byTestId("select-status-" + id))).selectByValue(status);
        return this;
    }

    /**
     * Ordena con el dropdown CUSTOM (NO es select: la clase Select no aplica).
     * Secuencia obligatoria: click en el botón → wait PRESENCE de la lista (se INSERTA) →
     * click en la opción → la lista se ELIMINA del DOM. option ∈ {title, priority, duedate}.
     */
    @Step("Ordenar por {option}")
    public ProjectDetailPage sortBy(String option) {
        click(DROPDOWN_SORT);
        waitPresent(DROPDOWN_SORT_LIST);
        click(byTestId("sort-option-" + option));
        waitGone(DROPDOWN_SORT_LIST);
        return this;
    }

    /** Filtra por estado con el select NATIVO (en cliente, instantáneo). value ∈ ALL|TODO|IN_PROGRESS|DONE. */
    @Step("Filtrar por estado {value}")
    public ProjectDetailPage filterByStatus(String value) {
        new Select(waitVisible(FILTER_STATUS)).selectByValue(value);
        return this;
    }

    /** Pide borrar la tarea → se INSERTA el diálogo de confirmación propio. PERMANECE → this. */
    @Step("Solicitar borrar la tarea {id}")
    public ProjectDetailPage deleteTask(int id) {
        click(byTestId("btn-delete-" + id));
        waitVisible(DIALOG_CONFIRM);
        return this;
    }

    /** Texto del diálogo de confirmación (¿Eliminar la tarea «{title}»?). */
    public String confirmText() {
        return waitVisible(CONFIRM_TEXT).getText();
    }

    /** Confirma el borrado → el diálogo se ELIMINA del DOM. PERMANECE → this. */
    @Step("Confirmar el borrado")
    public ProjectDetailPage confirmDelete() {
        click(BTN_CONFIRM_YES);
        waitGone(DIALOG_CONFIRM);
        return this;
    }

    /** Cancela el borrado → el diálogo se ELIMINA sin efecto. PERMANECE → this. */
    public ProjectDetailPage cancelDelete() {
        click(BTN_CONFIRM_NO);
        waitGone(DIALOG_CONFIRM);
        return this;
    }

    /** Espera a que una fila desaparezca del DOM (tras confirmar el borrado + delay). */
    public boolean waitTaskRowGone(int id) {
        return waitGone(byTestId("task-row-" + id));
    }

    /** ¿Se muestra el estado vacío «Sin tareas» (sin tareas o el filtro deja 0 filas)? */
    public boolean emptyStateVisible() {
        return driver.findElements(EMPTY_STATE).stream().anyMatch(WebElement::isDisplayed);
    }

    /**
     * Espera a que aparezca una fila NUEVA respecto a los ids conocidos y devuelve su id.
     * (Compara taskRowIds() antes/después con un wait; el id nace en el API, se lee del DOM.)
     */
    @Step("Esperar la fila nueva")
    public int waitNewRow(List<Integer> idsConocidos) {
        Set<Integer> conocidos = new HashSet<>(idsConocidos);
        wait.until(d -> taskRowIds().stream().anyMatch(id -> !conocidos.contains(id)));
        return taskRowIds().stream()
                .filter(id -> !conocidos.contains(id))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("no apareció ninguna fila nueva"));
    }

    /** Vuelve a la lista con el link «← Proyectos» (hueco → By.linkText) → NAVEGA. */
    @Step("Volver a la lista de proyectos")
    public ProjectsPage backToProjects() {
        click(BACK_LINK);
        return new ProjectsPage(driver);
    }
}
