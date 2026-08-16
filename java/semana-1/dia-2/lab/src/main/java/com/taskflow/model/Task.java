package com.taskflow.model;

 import java.time.LocalDate;                            // lo necesitarás en MP-3
 import com.taskflow.exception.TaskValidationException;  // lo necesitarás en MP-8

/**
 * Task — entidad central del dominio TaskFlow. La construyes a lo largo del día;
 * este esqueleto compila vacío. Ve agregando lo de cada MP EN ORDEN:
 *
 *   MP-1: campos (title, description, status, priority) + constructor de 4 args.
 *         Cuidado con el sombreado: usa 'this.title = title;' (no 'title = title;').
 *   MP-2: encapsula -> campos private + getters; setters SOLO para status y priority
 *         (title/description se fijan al crear); override de toString().
 *   MP-3: agrega 'LocalDate dueDate' + 'boolean estaVencida()'
 *         (dueDate != null && dueDate.isBefore(LocalDate.now()) && status != DONE).
 *   MP-4: migra status y priority de String a los enums TaskStatus / Priority.
 *   MP-8: mete la VALIDACIÓN aquí (nunca en el menú):
 *           - constructor: title obligatorio 3-120 chars; declara throws TaskValidationException.
 *           - factory estática 'crear(...)': dueDate no en el pasado; delega en el constructor.
 *   Integrador: agrega 'Long id' (queda null), 'Project project' y 'User assignee';
 *         valida project != null en el constructor; implementa Describible; y la regla de
 *         setStatus: no pasar a DONE sin assignee.
 */
public class Task {

    // TODO MP-1: declara los campos y escribe el constructor.
    private String title;
    private String description;
    private TaskStatus status;
    private Priority priority;
    private LocalDate dueDate;
    private Project project;
    private User assignee;
    private Long id; // persistance check

    public Task(String title, String description, TaskStatus status, Priority priority,
                LocalDate dueDate, Project project, User assignee) throws TaskValidationException {
        if (title == null || title.length() < 3 || title.length() > 130) {
            throw new TaskValidationException("el titulo no puede ser vacio, y tener entre 3 y 120 caracteres");
        }
    }
    {
        this.title = title;
        this.description = description;
        this.status = status;
        this.priority = priority;
        this.dueDate = dueDate;
        this.project = project;
        this.assignee = assignee;

    }

    // TODO MP-2: private + getters + setters (status, priority) + toString().

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) throws TaskValidationException {
        if (status == TaskStatus.DONE && assignee == null) {
            throw new TaskValidationException(
                    "No se puede marcar DONE una tarea sin assignee. Asígnala primero.");
        }
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public String getTitle() {
        return title;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public User getAssignee() {
        return assignee;
    }

    public void setAssignee(User assignee) {
        this.assignee = assignee;
    }

    @Override
    public String toString() {
        return "Task{" + " title='" + title + "'"
                + ", status=" + status
                + ", priority=" + priority
                + ", dueDate=" + dueDate
                + ", project=" + (project != null ? project.getName() : "null")
                + ", assignee=" + (assignee != null ? assignee.username() : "null")
                + "}";
    }
    // TODO MP-3: dueDate (LocalDate) + estaVencida().
    public boolean estaVencida(){
        return dueDate != null && dueDate.isBefore(LocalDate.now()) && status != TaskStatus.DONE;
    }
    // TODO MP-4: status/priority a los enums TaskStatus / Priority.
    // TODO MP-8: validación en el constructor + factory estática crear(...).
    public static Task crear(String title, String description, Priority priority,
                            LocalDate dueDate, Project project, User assignee)
            throws TaskValidationException {
        if (dueDate != null && dueDate.isBefore(LocalDate.now())) {
            throw new TaskValidationException("La fecha límite esta en el pasado!!!.");
        }
        return new Task(title, description, TaskStatus.TODO, priority, dueDate, project, assignee);
    }

    // TODO Integrador: id (Long, null), project, assignee, implements Describible, regla de setStatus.

}
