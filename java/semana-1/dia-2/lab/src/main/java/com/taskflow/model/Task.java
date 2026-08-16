package com.taskflow.model;

// import java.time.LocalDate;                            // lo necesitarás en MP-3
// import com.taskflow.exception.TaskValidationException;  // lo necesitarás en MP-8

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

    public Task(String title, String description, TaskStatus status, Priority priority) {
        this.title = title;
        this.description = description;
        this.status = status;
        this.priority = priority;
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

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public String toString() {
        return super.toString();
    }
    // TODO MP-3: dueDate (LocalDate) + estaVencida().
    public boolean estaVencida(){
        return dueDate != null && dueDate.isBefore(LocalDate.now()) && status != TaskStatus.DONE;
    }
    // TODO MP-4: status/priority a los enums TaskStatus / Priority.
    // TODO MP-8: validación en el constructor + factory estática crear(...).
    // TODO Integrador: id (Long, null), project, assignee, implements Describible, regla de setStatus.

}
