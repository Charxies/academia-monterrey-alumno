package com.taskflow.service;

import com.taskflow.model.Priority;
import com.taskflow.model.Task;
import com.taskflow.model.TaskStatus;
import com.taskflow.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * ReportService — los reportes con Streams, copiados de S1 (D4/D5) al proyecto Spring.
 *
 * Novedad de S2D1 (paso 2 del integrador): lleva @Service, así el contenedor lo registra como bean.
 * Su constructor ya recibía la INTERFAZ TaskRepository (así lo construía el Main de la consola) —
 * ahora Spring lo INYECTA por constructor sin que cambie una línea del cuerpo. Por eso, cuando el
 * repo sea JPA en D4, este servicio no se toca.
 *
 * Nota didáctica: ReportService y TaskService piden AMBOS el mismo bean TaskRepository; por eso el
 * checkpoint-error #3a (antes de anotar @Repository) puede nombrar a cualquiera de los dos — la
 * lectura del reporte de Spring es idéntica.
 *
 * Reglas de arquitectura de S1 (siguen valiendo): cada método RETORNA DATOS (List/Map/double);
 * cero for/while (pipelines de Stream); predicados NOMBRADOS y reutilizados.
 */
@Service
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
     * Pendientes ordenadas con la ESTRATEGIA que se le pase (MP-3, Strategy parametrizado).
     * Filtra SIEMPRE las no-DONE; el ORDEN es intercambiable: quien llama decide con cuál de las
     * estrategias de {@link TaskOrders} (POR_FECHA / POR_TITULO / POR_URGENCIA) quiere el resultado.
     */
    public List<Task> pendientes(Comparator<Task> orden) {
        return repo.findAll().stream()
                .filter(ES_PENDIENTE)
                .sorted(orden)
                .collect(Collectors.toList());
    }

    /**
     * Pendientes ordenadas por fecha: la de siempre (S1D4). Tras MP-3 queda DELEGANDO en la
     * sobrecarga parametrizada con la estrategia POR_FECHA — el refactor no cambia su resultado.
     */
    public List<Task> pendientesPorFecha() {
        return pendientes(TaskOrders.POR_FECHA);
    }

    /**
     * Buscar por título: las tareas cuyo título CONTIENE el texto q, sin distinguir mayúsculas.
     * Puede devolver lista vacía (sin coincidencias): quien lo use lo maneja con un mensaje.
     */
    public List<Task> buscarPorTitulo(String q) {
        String needle = q.toLowerCase();
        return repo.findAll().stream()
                .filter(t -> t.getTitle().toLowerCase().contains(needle))
                .collect(Collectors.toList());
    }

    /**
     * % completadas: cuántas DONE respecto al total, en porcentaje.
     * TRAMPA DE D1 (división entera): multiplicar por 100.0 (double) ANTES de dividir.
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
     * Tareas por asignado: agrupa por assigneeId. groupingBy lanza NullPointerException si el
     * clasificador devuelve null, así que PRIMERO filtramos las que sí tienen responsable.
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

    // ==================== STRETCH (heredados de S1D4/D5) ====================

    /** STRETCH — Tareas por prioridad: mismo patrón que "por estado", clasificando por Priority. */
    public Map<Priority, List<Task>> tareasPorPrioridad() {
        return repo.findAll().stream()
                .collect(Collectors.groupingBy(Task::getPriority));
    }

    /** STRETCH — Exportar títulos a una sola línea CSV con Collectors.joining(", "). */
    public String titulosCsv() {
        return repo.findAll().stream()
                .map(Task::getTitle)
                .collect(Collectors.joining(", "));
    }
}
