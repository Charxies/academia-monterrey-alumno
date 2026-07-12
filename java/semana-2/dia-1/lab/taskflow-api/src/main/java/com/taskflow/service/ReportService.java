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
 * ReportService — los reportes con Streams, copiados de S1 (D4/D5) al proyecto Spring.
 *
 * TODO integrador paso 2: anota esta clase con @Service (import org.springframework.stereotype.Service)
 *   para que el contenedor la registre como bean. Su constructor ya recibe la INTERFAZ
 *   TaskRepository -> Spring la inyectará por constructor sin tocar el cuerpo.
 *
 * NOTA: al anotar @Service, esta clase pedirá un bean TaskRepository. Si aún no anotaste
 *   @Repository en InMemoryTaskRepository, verás el mismo checkpoint-error #3a que TaskService.
 */
// TODO paso 2: @Service
public class ReportService {

    private final TaskRepository repo;

    public ReportService(TaskRepository repo) {
        this.repo = repo;
    }

    /** Una tarea está pendiente mientras NO esté DONE. '!=' entre enums es seguro. */
    public static final Predicate<Task> ES_PENDIENTE = t -> t.getStatus() != TaskStatus.DONE;

    /** Está sin asignar si no tiene responsable. */
    public static final Predicate<Task> SIN_ASIGNAR = t -> t.getAssigneeId() == null;

    /** Tareas por estado: agrupa por su estado (Map<TaskStatus, List<Task>>). */
    public Map<TaskStatus, List<Task>> tareasPorEstado() {
        return repo.findAll().stream()
                .collect(Collectors.groupingBy(Task::getStatus));
    }

    /**
     * MP-3 — Pendientes ordenadas con la ESTRATEGIA que se le pase (Strategy parametrizado).
     *
     * TODO MP-3: filtra las no-DONE (ES_PENDIENTE) y ordénalas con 'orden' (sorted(orden)); luego
     *   haz que pendientesPorFecha() DELEGUE aquí con TaskOrders.POR_FECHA.
     */
    public List<Task> pendientes(Comparator<Task> orden) {
        // TODO MP-3: reemplazar el placeholder por filter(ES_PENDIENTE).sorted(orden)
        return List.of();
    }

    /** Pendientes ordenadas por fecha (S1D4): dueDate ascendente, sin fecha al final. */
    public List<Task> pendientesPorFecha() {
        return repo.findAll().stream()
                .filter(ES_PENDIENTE)
                .sorted(Comparator.comparing(Task::getDueDate,
                        Comparator.nullsLast(Comparator.naturalOrder())))
                .collect(Collectors.toList());
    }

    /** % completadas: cuántas DONE respecto al total. El *100.0 evita la división entera de D1. */
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

    /** STRETCH — Tareas por prioridad: mismo patrón que "por estado". */
    public Map<Priority, List<Task>> tareasPorPrioridad() {
        return repo.findAll().stream()
                .collect(Collectors.groupingBy(Task::getPriority));
    }
}
