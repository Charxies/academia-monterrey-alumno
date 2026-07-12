package com.taskflow.service;

import com.taskflow.model.Task;
import com.taskflow.model.TaskStatus;
import com.taskflow.repository.InMemoryTaskRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ReportService — los reportes del CLI v3 (ESQUELETO — tu trabajo del integrador).
 *
 * Reglas de arquitectura del día (respétalas):
 *   - Cada método RETORNA DATOS (List / Map / double). El menú (Main) es quien imprime.
 *     Mañana (D5) estos métodos se testean con JUnit SIN tocar la consola: por eso no llevan
 *     System.out dentro.
 *   - CERO for / while: reescribe los reportes como pipelines de Stream
 *     (filter / map / sorted / collect / count / groupingBy). Al terminar, no debe quedar
 *     ningún bucle for/while en este archivo (0 resultados).
 *   - Predicados NOMBRADOS y reutilizados (Predicate<Task>), no lambdas gigantes inline.
 *
 * El esqueleto COMPILA (los cuerpos devuelven valores neutros). Tu trabajo: llenar los TODO.
 */
public class ReportService {

    private final InMemoryTaskRepository repo;

    public ReportService(InMemoryTaskRepository repo) {
        this.repo = repo;
    }

    // TODO (sugerido) — declara aquí tus Predicate<Task> reutilizables, por ejemplo:
    //   public static final Predicate<Task> ES_PENDIENTE = t -> t.getStatus() != TaskStatus.DONE;
    //   public static final Predicate<Task> SIN_ASIGNAR  = t -> t.getAssigneeId() == null;

    /**
     * TODO 1 — Tareas por estado:
     *   repo.findAll().stream().collect(Collectors.groupingBy(Task::getStatus))
     *   Devuelve Map<TaskStatus, List<Task>> (agrupa las tareas POR estado).
     */
    public Map<TaskStatus, List<Task>> tareasPorEstado() {
        // TODO 1
        return new HashMap<>();
    }

    /**
     * TODO 2 — Pendientes ordenadas por fecha:
     *   filter(status != DONE)
     *   .sorted(Comparator.comparing(Task::getDueDate,
     *           Comparator.nullsLast(Comparator.naturalOrder())))   // nullsLast: hay 1 sin fecha
     *   .collect(Collectors.toList())
     */
    public List<Task> pendientesPorFecha() {
        // TODO 2
        return new ArrayList<>();
    }

    /**
     * TODO 3 — Buscar por título (case-insensitive):
     *   String needle = q.toLowerCase();
     *   filter(t -> t.getTitle().toLowerCase().contains(needle)).collect(toList())
     *   Puede devolver lista vacía; el menú lo maneja sin crashear.
     */
    public List<Task> buscarPorTitulo(String q) {
        // TODO 3
        return new ArrayList<>();
    }

    /**
     * TODO 4 — % completadas:
     *   count() de las DONE * 100.0 / total.
     *   CUIDADO (trampa de D1): 'done / total' con dos long da 0 (división entera). Multiplica
     *   por 100.0 ANTES de dividir. Considera el caso lista vacía (evitar 0/0 = NaN).
     */
    public double porcentajeCompletadas() {
        // TODO 4
        return 0.0;
    }

    /**
     * TODO 5 — Tareas por asignado:
     *   PRIMERO filter(t -> t.getAssigneeId() != null)  (groupingBy revienta con clave null),
     *   luego groupingBy(Task::getAssigneeId) -> Map<Long, List<Task>>.
     *   Las sin asignar van aparte: ver sinAsignar().
     */
    public Map<Long, List<Task>> tareasPorAsignado() {
        // TODO 5
        return new HashMap<>();
    }

    /** TODO 6 — sinAsignar: count() de las tareas con assigneeId == null. */
    public long sinAsignar() {
        // TODO 6
        return 0;
    }

    // ==================== STRETCH (opcionales) ====================
    // - tareasPorPrioridad(): groupingBy(Task::getPriority).
    // - top3Urgentes(): sorted(prioridad desc, luego fecha) + limit(3).
    // - titulosCsv(): map(Task::getTitle).collect(Collectors.joining(", ")).
    // - vencidasVsNoVencidas(): partitioningBy(Task::estaVencida).
}
