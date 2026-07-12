package com.taskflow.service;

import com.taskflow.model.Priority;
import com.taskflow.model.Task;
import com.taskflow.model.TaskStatus;
import com.taskflow.repository.TaskRepository;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * ReportService — TODOS los reportes del CLI, resueltos con Streams (heredado de D4, PROVISTO).
 *
 * Cambio de hoy (S1D5): el constructor recibe la INTERFAZ {@link TaskRepository}, no la clase
 * concreta. Es el pago de extraer el contrato en el integrador: da igual si detrás hay un repo
 * en memoria o uno con archivo — el servicio solo llama a findAll(). Por eso mañana, cuando en
 * S2 el repo sea JPA, ReportService no cambia.
 *
 * Reglas de arquitectura (siguen valiendo):
 *   - Cada método RETORNA DATOS (List / Map / double). El menú (Main) es quien imprime.
 *     HOY estos métodos se testean con JUnit SIN tocar la consola (ReportServiceTest).
 *   - CERO for / while: los reportes son pipelines de Stream.
 *   - Predicados NOMBRADOS y reutilizados, no lambdas gigantes inline.
 */
public class ReportService {

    private final TaskRepository repo;

    public ReportService(TaskRepository repo) {
        this.repo = repo;
    }

    // ---- Predicados nombrados (se leen como reglas de negocio y se reutilizan) ----

    /** Una tarea está pendiente mientras NO esté DONE. '!=' entre enums es seguro. */
    public static final Predicate<Task> ES_PENDIENTE = t -> t.getStatus() != TaskStatus.DONE;

    /** Está sin asignar si no tiene responsable. */
    public static final Predicate<Task> SIN_ASIGNAR = t -> t.getAssigneeId() == null;

    // ==================== Reportes ====================

    /**
     * Tareas por estado: agrupa las tareas POR su estado.
     * groupingBy(clasificador) devuelve Map&lt;K, List&lt;T&gt;&gt;: aquí Map&lt;TaskStatus, List&lt;Task&gt;&gt;.
     */
    public Map<TaskStatus, List<Task>> tareasPorEstado() {
        return repo.findAll().stream()
                .collect(Collectors.groupingBy(Task::getStatus));
    }

    /**
     * MP-3 — Pendientes ordenadas con la ESTRATEGIA que se le pase (Strategy parametrizado).
     *
     * TODO MP-3: filtra las no-DONE (usa el predicado ES_PENDIENTE) y ordénalas con 'orden'
     *   (sorted(orden)). Cuando lo tengas, haz que pendientesPorFecha() DELEGUE aquí con
     *   TaskOrders.POR_FECHA (el refactor no debe cambiar el resultado: mvn test sigue verde).
     */
    public List<Task> pendientes(Comparator<Task> orden) {
        // TODO MP-3: reemplazar este placeholder por el pipeline filter(ES_PENDIENTE).sorted(orden)
        return List.of();
    }

    /**
     * Pendientes ordenadas por fecha: filtra las no-DONE y las ordena por dueDate ascendente,
     * con las que NO tienen fecha (dueDate null) al FINAL. nullsLast(naturalOrder()) evita el
     * NullPointerException al comparar una dueDate null.
     */
    public List<Task> pendientesPorFecha() {
        return repo.findAll().stream()
                .filter(ES_PENDIENTE)
                .sorted(Comparator.comparing(Task::getDueDate,
                        Comparator.nullsLast(Comparator.naturalOrder())))
                .collect(Collectors.toList());
    }

    /**
     * Buscar por título: las tareas cuyo título CONTIENE el texto q, sin distinguir mayúsculas.
     * Puede devolver lista vacía (sin coincidencias): el menú lo maneja con un mensaje, no crashea.
     */
    public List<Task> buscarPorTitulo(String q) {
        String needle = q.toLowerCase();
        return repo.findAll().stream()
                .filter(t -> t.getTitle().toLowerCase().contains(needle))
                .collect(Collectors.toList());
    }

    /**
     * % completadas: cuántas DONE respecto al total, en porcentaje.
     *
     * TRAMPA DE D1 (división entera): 'done / total' con dos long da 0 (trunca). El truco es
     * multiplicar por 100.0 (un double) ANTES de dividir: done * 100.0 / total.
     * El if de lista vacía evita un 0/0 = NaN.
     */
    public double porcentajeCompletadas() {
        List<Task> todas = repo.findAll();
        if (todas.isEmpty()) {
            return 0.0;
        }
        long completadas = todas.stream()
                .filter(t -> t.getStatus() == TaskStatus.DONE)
                .count();
        return completadas * 100.0 / todas.size();
    }

    /**
     * Tareas por asignado: agrupa por assigneeId.
     * OJO: groupingBy lanza NullPointerException si el clasificador devuelve null, así que
     * PRIMERO filtramos las que sí tienen responsable. Las restantes se cuentan con sinAsignar().
     */
    public Map<Long, List<Task>> tareasPorAsignado() {
        return repo.findAll().stream()
                .filter(SIN_ASIGNAR.negate())      // el negado de "sin asignar" = "con responsable"
                .collect(Collectors.groupingBy(Task::getAssigneeId));
    }

    /** Cuántas tareas están sin asignar (complemento del reporte anterior). */
    public long sinAsignar() {
        return repo.findAll().stream()
                .filter(SIN_ASIGNAR)
                .count();
    }

    // ==================== STRETCH ====================

    /** STRETCH — Tareas por prioridad: mismo patrón que "por estado", clasificando por Priority. */
    public Map<Priority, List<Task>> tareasPorPrioridad() {
        return repo.findAll().stream()
                .collect(Collectors.groupingBy(Task::getPriority));
    }

    /**
     * STRETCH — Top 3 urgentes: prioridad de mayor a menor y, a igualdad, fecha más próxima
     * primero (nulls al final); nos quedamos con las 3 primeras con limit(3).
     */
    public List<Task> top3Urgentes() {
        return repo.findAll().stream()
                .sorted(Comparator.comparing(Task::getPriority, Comparator.reverseOrder())
                        .thenComparing(Task::getDueDate,
                                Comparator.nullsLast(Comparator.naturalOrder())))
                .limit(3)
                .collect(Collectors.toList());
    }

    /** STRETCH — Exportar títulos a una sola línea CSV con Collectors.joining(", "). */
    public String titulosCsv() {
        return repo.findAll().stream()
                .map(Task::getTitle)
                .collect(Collectors.joining(", "));
    }

    /**
     * STRETCH — Particionar vencidas / no vencidas: partitioningBy con un Predicate devuelve
     * SIEMPRE un Map con las dos claves (true y false), aunque una lista quede vacía.
     */
    public Map<Boolean, List<Task>> vencidasVsNoVencidas() {
        return repo.findAll().stream()
                .collect(Collectors.partitioningBy(Task::estaVencida));
    }
}
