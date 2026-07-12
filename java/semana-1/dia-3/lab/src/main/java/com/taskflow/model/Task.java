package com.taskflow.model;

import com.taskflow.exception.TaskValidationException;

import java.time.LocalDate;

/**
 * Task — entidad central del dominio TaskFlow. Viene del Día 2 YA RESUELTA y con el
 * puente objeto->id YA APLICADO (forma canónica S1): lo que ayer eran los objetos
 * 'Project project' y 'User assignee' hoy son 'Long projectId' y 'Long assigneeId'.
 * Frase ancla del warm-up: "el repositorio guarda ids, como hará la BD en S2".
 *
 * NO reconstruyas esta clase: es la base del día. Le faltan DOS bloques que añades hoy:
 * el de MP-6 (equals/hashCode por id) y el de MP-7 (implements Comparable + compareTo). El
 * resto está completo (validación en constructor + factory crear + setStatus, igual que ayer).
 *
 *   TODO MP-6: añade equals(Object) y hashCode() por 'id' (identidad de ENTIDAD, como hará
 *              JPA en S2D4). Úsalo así (o el generador de IntelliJ, ⌘N / Alt+Insert):
 *                @Override public boolean equals(Object o) { ... Objects.equals(id, otra.id) ... }
 *                @Override public int hashCode() { return Objects.hash(id); }
 *              Recuerda importar java.util.Objects. Sin hashCode, un HashSet "pierde" tareas.
 *
 *   TODO MP-7: haz que la clase implemente Comparable<Task> (cambia la firma a
 *              'public class Task implements Comparable<Task>') y añade el orden natural por
 *              prioridad:
 *                @Override public int compareTo(Task otra) { return this.priority.compareTo(otra.priority); }
 *              Habilita list.sort(null); el integrador ordena con Comparator, no con este orden.
 */
public class Task {

    // Reglas de longitud del título (del capstone).
    private static final int TITULO_MIN = 3;
    private static final int TITULO_MAX = 120;

    // id nace null; lo asigna el REPOSITORIO en save(...) (como hará la BD en S2). Ver setId.
    private Long id;
    private final String title;         // se fija al crear (no hay setTitle)
    private final String description;   // se fija al crear
    private TaskStatus status;          // muta (setStatus, con regla)
    private Priority priority;          // muta (setPriority)
    private final Long projectId;       // se fija al crear: una tarea no existe sin proyecto
    private Long assigneeId;            // muta (setAssigneeId); opcional, puede ser null
    private final LocalDate dueDate;    // opcional (null = sin fecha)

    /**
     * Constructor de REHIDRATACIÓN: reconstruye una tarea que YA existía (precargas de hoy,
     * semilla de D4, CSV de D5). Valida las INVARIANTES que siempre deben cumplirse (título
     * y proyecto), pero NO la regla temporal de dueDate: una tarea vencida es un dato válido
     * que hay que poder releer.
     */
    public Task(Long id, String title, String description, TaskStatus status,
                Priority priority, Long projectId, Long assigneeId, LocalDate dueDate)
            throws TaskValidationException {
        // Invariante 1: título obligatorio, 3-120 chars.
        if (title == null || title.isBlank()) {
            throw new TaskValidationException("El título es obligatorio (no puede ir vacío).");
        }
        if (title.length() < TITULO_MIN || title.length() > TITULO_MAX) {
            throw new TaskValidationException(
                    "El título debe tener entre " + TITULO_MIN + " y " + TITULO_MAX
                            + " caracteres; recibí " + title.length() + ": \"" + title + "\".");
        }
        // Invariante 2: una tarea no puede existir sin proyecto (regla del capstone).
        if (projectId == null) {
            throw new TaskValidationException(
                    "Una tarea no puede existir sin proyecto (projectId == null).");
        }
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
        this.priority = priority;
        this.projectId = projectId;
        this.assigneeId = assigneeId;
        this.dueDate = dueDate;
    }

    /**
     * Factory de CREACIÓN de negocio: la usa el menú al "Agregar tarea". Añade la regla
     * temporal (dueDate no en el pasado) y delega en el constructor. id nace null,
     * status nace TODO. assigneeId es opcional (null = sin asignar).
     *
     * Firma canónica S1 (apéndice del CAPSTONE-SPEC): la usan D4 y D5 tal cual.
     */
    public static Task crear(String title, String description, Priority priority,
                             LocalDate dueDate, Long projectId, Long assigneeId)
            throws TaskValidationException {
        if (dueDate != null && dueDate.isBefore(LocalDate.now())) {
            throw new TaskValidationException(
                    "La fecha límite no puede estar en el pasado: " + dueDate + ".");
        }
        return new Task(null, title, description, TaskStatus.TODO, priority, projectId, assigneeId, dueDate);
    }

    // ---- Comportamiento: vive CON los datos ----

    /** true si tiene fecha, ya pasó, y la tarea aún no está terminada. */
    public boolean estaVencida() {
        // '!=' entre enums es SEGURO (contraste con el trap de equals de los String de D1).
        return dueDate != null && dueDate.isBefore(LocalDate.now()) && status != TaskStatus.DONE;
    }

    // ---- Getters ----

    public Long getId() {
        return id;
    }

    /**
     * Setea el id. Uso EXCLUSIVO del repositorio para asignar el id autoincremental en save(...).
     * Nadie más lo llama a mano; en S2 este trabajo lo hará JPA por debajo.
     */
    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public Priority getPriority() {
        return priority;
    }

    public Long getProjectId() {
        return projectId;
    }

    public Long getAssigneeId() {
        return assigneeId;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    // ---- Setters SOLO donde el dominio muta ----

    /**
     * Cambia el estado aplicando la regla del capstone: no se puede pasar a DONE una
     * tarea sin responsable (assigneeId). La regla vive AQUÍ, no en el menú.
     */
    public void setStatus(TaskStatus status) throws TaskValidationException {
        if (status == TaskStatus.DONE && assigneeId == null) {
            throw new TaskValidationException(
                    "No se puede marcar como " + TaskStatus.DONE
                            + " una tarea sin responsable: \"" + title + "\".");
        }
        this.status = status;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public void setAssigneeId(Long assigneeId) {
        this.assigneeId = assigneeId;
    }

    // TODO MP-6: equals(Object) y hashCode() por 'id' van AQUÍ (ver la nota del encabezado).

    // TODO MP-7: el compareTo(Task) por prioridad va AQUÍ, y 'implements Comparable<Task>'
    //            en la firma de la clase (ver la nota del encabezado).

    @Override
    public String toString() {
        return "Task{id=" + id
                + ", title='" + title + '\''
                + ", status=" + status
                + ", priority=" + priority
                + ", assigneeId=" + (assigneeId == null ? "sin asignar" : assigneeId)
                + ", dueDate=" + (dueDate == null ? "sin fecha" : dueDate)
                + (estaVencida() ? " *VENCIDA*" : "")
                + '}';
    }
}
