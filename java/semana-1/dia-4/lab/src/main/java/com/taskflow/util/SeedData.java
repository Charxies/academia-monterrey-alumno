package com.taskflow.util;

import com.taskflow.exception.TaskValidationException;
import com.taskflow.model.Priority;
import com.taskflow.model.Task;
import com.taskflow.model.TaskStatus;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * SeedData — datos semilla del día (PROVISTO, no lo tocas).
 *
 * Devuelve 9 tareas pensadas para ejercitar TODOS los reportes de hoy:
 *   - >=2 tareas por estado (TODO / IN_PROGRESS / DONE).
 *   - >=1 VENCIDA (dueDate en el pasado y estado != DONE): #3 y #6.
 *   - EXACTAMENTE 1 con dueDate null (sin fecha): #7  -> dispara el nullsLast del reporte 2.
 *   - >=1 SIN assigneeId: #5 y #7  -> alimentan el "Sin asignar: n" del reporte 5.
 *   - Prioridades variadas y >=2 títulos que comparten la palabra "API" (#3 y #4) para probar
 *     la búsqueda por título case-insensitive.
 *
 * Detalles canónicos (apéndice del CAPSTONE-SPEC):
 *   - Se construye con el CONSTRUCTOR DE REHIDRATACIÓN (new Task(id, ...)), NO con Task.crear:
 *     una tarea vencida es un dato LEGAL que hay que poder releer; la regla "dueDate no en el
 *     pasado" vive solo en la factory crear(...), no en la rehidratación.
 *   - Fechas RELATIVAS (LocalDate.now().minusDays/plusDays) para que el material no caduque.
 *   - La checked TaskValidationException se maneja en UN SOLO try/catch y se re-lanza como
 *     IllegalStateException: si la semilla fuera inválida, es un bug del MATERIAL, no del alumno.
 */
public final class SeedData {

    // El capstone exige "no hay Task sin Project": en S1 el proyecto es solo un id.
    private static final Long PROYECTO_DEMO = 1L;

    private SeedData() {
        // Clase de utilería: no se instancia.
    }

    /** Las 9 tareas semilla, con ids 1..9 ya asignados (rehidratación). */
    public static List<Task> tareas() {
        try {
            List<Task> tareas = new ArrayList<>();
            // id, título, descripción, estado, prioridad, projectId, assigneeId, dueDate
            tareas.add(new Task(1L, "Diseñar modelo de dominio", "Entidades User, Project, Task",
                    TaskStatus.DONE, Priority.HIGH, PROYECTO_DEMO, 1L, LocalDate.now().minusDays(20)));
            tareas.add(new Task(2L, "Configurar repositorio y CI", "Repo base y pipeline",
                    TaskStatus.DONE, Priority.MED, PROYECTO_DEMO, 2L, LocalDate.now().minusDays(15)));
            // VENCIDA: dueDate en el pasado y estado != DONE.
            tareas.add(new Task(3L, "Implementar API de tareas", "Endpoints CRUD de Task",
                    TaskStatus.IN_PROGRESS, Priority.HIGH, PROYECTO_DEMO, 1L, LocalDate.now().minusDays(2)));
            tareas.add(new Task(4L, "Documentar API REST", "Swagger y ejemplos",
                    TaskStatus.IN_PROGRESS, Priority.MED, PROYECTO_DEMO, 3L, LocalDate.now().plusDays(5)));
            // SIN assignee (null): no se podrá completar hasta asignarla (regla del capstone).
            tareas.add(new Task(5L, "Escribir tests del dominio", "JUnit para Task",
                    TaskStatus.TODO, Priority.LOW, PROYECTO_DEMO, null, LocalDate.now().plusDays(10)));
            // VENCIDA #2: dueDate de ayer, estado TODO.
            tareas.add(new Task(6L, "Desplegar a staging", "Primer deploy de prueba",
                    TaskStatus.TODO, Priority.HIGH, PROYECTO_DEMO, 2L, LocalDate.now().minusDays(1)));
            // ÚNICA con dueDate null (sin fecha) y SIN assignee.
            tareas.add(new Task(7L, "Revisar backlog", "Priorizar pendientes",
                    TaskStatus.TODO, Priority.LOW, PROYECTO_DEMO, null, null));
            tareas.add(new Task(8L, "Optimizar consultas", "Índices y perfiles",
                    TaskStatus.IN_PROGRESS, Priority.MED, PROYECTO_DEMO, 3L, LocalDate.now().plusDays(3)));
            tareas.add(new Task(9L, "Preparar demo para stakeholders", "Guion y datos de muestra",
                    TaskStatus.TODO, Priority.MED, PROYECTO_DEMO, 1L, LocalDate.now().plusDays(7)));
            return tareas;
        } catch (TaskValidationException e) {
            // Semilla inválida = bug del material, no del alumno: revienta fuerte y claro.
            throw new IllegalStateException("Semilla inválida (bug del material): " + e.getMessage(), e);
        }
    }
}
