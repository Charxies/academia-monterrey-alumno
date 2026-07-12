package com.taskflow.model;

import com.taskflow.exception.TaskValidationException;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Task — entidad central del dominio TaskFlow. ESTADO FINAL del Día 3.
 *
 * FORMA CANÓNICA S1 (apéndice del CAPSTONE-SPEC), YA con el puente objeto->id aplicado:
 * lo que en D2 eran los objetos 'Project project' y 'User assignee' hoy son 'Long projectId'
 * y 'Long assigneeId'. Frase ancla: "el repositorio guarda ids, como hará la BD en S2".
 * Estos 8 campos son las 8 columnas del CSV que aparece en S1D5.
 *
 * Novedad del Día 3 respecto al Task de ayer:
 *   - project/assignee OBJETO  ->  projectId/assigneeId (Long).   (puente del warm-up)
 *   - equals()/hashCode() por id (MP-6): identidad de ENTIDAD, la misma decisión que
 *     tomará JPA en S2D4. Sin esto, un HashSet/HashMap "pierde" tareas que sí están.
 *   - implements Comparable<Task> + compareTo por prioridad (MP-7): orden natural de Task.
 *     Los órdenes alternativos (fecha, título) los da un Comparator externo (MP-8/integrador).
 *
 * Reglas de negocio (todas viven DENTRO de Task; el menú NO valida nada):
 *   - title obligatorio, 3-120 chars.           -> constructor (invariante)
 *   - un Task no existe sin proyecto.            -> constructor (invariante: projectId != null)
 *   - dueDate no en el pasado AL CREAR.          -> factory estática crear(...)
 *   - no pasar a DONE sin assignee.              -> setStatus(...)  (assigneeId != null)
 *
 * ============================================================================================
 * TODO (S2D4) — CONVERTIR ESTA CLASE EN UNA @Entity JPA. La entidad ES esta clase; se ANOTA, no se
 * reescribe. Pasos:
 *   MP-2: @Entity + @Table(name = "tasks") sobre la clase; @Id + @GeneratedValue(strategy = IDENTITY)
 *         sobre 'id'; añadir un constructor 'protected Task() {}' (JPA rehidrata por reflexión — sin
 *         él: "No default constructor for entity"). Quitar 'final' de los campos persistentes (JPA
 *         los escribe por reflexión: title/description/projectId/dueDate ya no pueden ser final).
 *   MP-3: @Column(nullable = false, length = 120) en 'title'; @Enumerated(EnumType.STRING) en 'status'
 *         y 'priority' (NUNCA el default ORDINAL: guardaría 0/1/2 y corrompería al reordenar el enum).
 *   MP-8: la relación @ManyToOne de SOLO LECTURA hacia Project (la columna la manda 'projectId'):
 *         @ManyToOne(fetch = FetchType.LAZY)
 *         @JoinColumn(name = "project_id", insertable = false, updatable = false)
 *         private Project project;   // + su getter. "La columna manda; el objeto navega".
 * Nota: Task.crear(...) sigue siendo el ÚNICO camino de creación de negocio; JPA solo rehidrata.
 * ============================================================================================
 */
public class Task implements Comparable<Task> {

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
     * que hay que poder releer (por eso estaVencida() puede dar true en una tarea construible).
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

    // ---- MP-6: equals/hashCode por id (identidad de ENTIDAD) ----

    /**
     * Dos tareas son "la misma" si comparten id (identidad de entidad, como hará JPA en S2D4).
     * Regla de oro del contrato: si a.equals(b) es true, a.hashCode() == b.hashCode() SIEMPRE
     * (por eso ambos delegan en id). Sin hashCode, un HashSet/HashMap las pondría en cubetas
     * distintas y "no encontraría" un duplicado que sí existe.
     *
     * Matiz honesto: si id es null (tarea recién creada aún no guardada), dos tareas distintas
     * se considerarían iguales. Por eso esta identidad tiene sentido para tareas YA guardadas
     * (con id asignado por el repositorio) — que es como las usa el CLI. JPA carga el mismo matiz.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Task otra = (Task) o;
        return Objects.equals(id, otra.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    // ---- MP-7: orden natural por prioridad (Comparable) ----

    /**
     * Orden NATURAL de Task: por prioridad, delegando en el orden de declaración del enum
     * (LOW, MED, HIGH -> ese es el orden ascendente). Habilita list.sort(null) y
     * Collections.sort(...). Una clase tiene UN SOLO orden natural; los órdenes alternativos
     * e intercambiables (por fecha, por título, prioridad descendente) los da un Comparator
     * externo -> ver MP-8 y el ORDEN_LISTADO del integrador (que NO usa este orden natural).
     */
    @Override
    public int compareTo(Task otra) {
        return this.priority.compareTo(otra.priority);
    }

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
