package com.taskflow;

import com.taskflow.exception.TaskValidationException;
import com.taskflow.model.Priority;
import com.taskflow.model.Task;
import com.taskflow.model.TaskStatus;
import com.taskflow.repository.InMemoryTaskRepository;
import com.taskflow.service.ReportService;
import com.taskflow.util.SeedData;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

/**
 * Integrador D4 — TaskFlow CLI v3 (STARTER).
 *
 * Parte de tu CLI v2 de ayer (repositorio en memoria con CRUD). Lo que VIENE RESUELTO:
 *   - El ciclo del menú, la carga de la semilla (SeedData), y las utilidades de lectura robusta.
 *   - Las operaciones CRUD de v2: agregar, listar, completar (versión de D3), eliminar.
 *
 * TU TRABAJO HOY (los TODO):
 *   1) Submenú "Reportes" delegando en ReportService (que también completas): que cada reporte
 *      RETORNE datos y el menú los imprima. Sin for/while en la lógica de reportes: son streams.
 *   2) CIRUGÍA DE OPTIONAL: cambia repo.findById a Optional<Task> (MP-8) y actualiza los callers:
 *      "Buscar por id" (nueva de hoy) con ifPresentOrElse, y "Completar por id" con
 *      orElseThrow(() -> new TaskNotFoundException(id)). Cero null, cero .get().
 *
 * El esqueleto COMPILA tal cual (las piezas nuevas son stubs). Correr:
 *   mvn -q compile exec:java
 */
public class Main {

    // El capstone exige "no hay Task sin Project": en S1 el proyecto es solo un id.
    private static final Long PROYECTO_DEMO = 1L;

    /**
     * Orden del listado (opción 2), heredado de D3 y que HOY por fin entendemos: prioridad
     * HIGH->LOW y, a igualdad, fecha ascendente con las tareas sin fecha al final. El reverse
     * va POR CRITERIO (no con reversed() al final); nullsLast evita el NPE.
     */
    private static final Comparator<Task> ORDEN_LISTADO =
            Comparator.comparing(Task::getPriority, Comparator.reverseOrder())
                    .thenComparing(Task::getDueDate, Comparator.nullsLast(Comparator.naturalOrder()));

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        InMemoryTaskRepository repo = new InMemoryTaskRepository();
        // Carga la semilla por el repositorio (repo::save como Consumer<Task>).
        SeedData.tareas().forEach(repo::save);
        ReportService reportes = new ReportService(repo);

        int opcion;
        do {
            mostrarMenu();
            opcion = leerEntero(scanner, "Elige una opción: ");
            switch (opcion) {
                case 1 -> agregarTarea(scanner, repo);
                case 2 -> listarTareas(repo);
                case 3 -> completarPorId(scanner, repo);
                case 4 -> eliminarPorId(scanner, repo);
                case 5 -> buscarPorId(scanner, repo);
                case 6 -> menuReportes(scanner, reportes);
                case 7 -> System.out.println("¡Hasta mañana! Recuerda: commit y push.");
                default -> System.out.println(
                        "Opción inválida: [" + opcion + "]. Elige una del menú.");
            }
        } while (opcion != 7);

        scanner.close();
    }

    private static void mostrarMenu() {
        System.out.println("""

                === TaskFlow CLI v3 ===
                1) Agregar tarea
                2) Listar tareas
                3) Completar por id
                4) Eliminar por id
                5) Buscar por id
                6) Reportes
                7) Salir""");
    }

    // ==================== Operaciones CRUD (de v2, ya resueltas) ====================

    /** Opción 1: agrega una tarea con Task.crear en try/catch. El id lo asigna save. */
    private static void agregarTarea(Scanner sc, InMemoryTaskRepository repo) {
        System.out.print("Título: ");
        String title = sc.nextLine();
        Priority priority = leerPrioridad(sc);
        LocalDate dueDate = leerFecha(sc, "Fecha límite (yyyy-MM-dd, vacío = sin fecha): ");
        try {
            Task nueva = Task.crear(title, "", priority, dueDate, PROYECTO_DEMO, null);
            repo.save(nueva);
            System.out.println("Creada tarea #" + nueva.getId() + ": " + nueva.getTitle());
        } catch (TaskValidationException e) {
            System.out.println("No se pudo crear la tarea: " + e.getMessage());
        }
    }

    /** Opción 2: findAll() ordenado con ORDEN_LISTADO; tabla printf; vacío -> mensaje. */
    private static void listarTareas(InMemoryTaskRepository repo) {
        List<Task> tareas = repo.findAll();
        if (tareas.isEmpty()) {
            System.out.println("(sin tareas todavía)");
            return;
        }
        tareas.sort(ORDEN_LISTADO);
        System.out.printf("%-4s %-34s %-12s %-10s %-14s%n",
                "ID", "TÍTULO", "ESTADO", "PRIORIDAD", "RESPONSABLE");
        System.out.println("---- ---------------------------------- ------------ ---------- --------------");
        for (Task t : tareas) {
            String marca = t.estaVencida() ? " *" : "";
            String responsable = (t.getAssigneeId() == null) ? "-" : ("#" + t.getAssigneeId());
            System.out.printf("%-4d %-34s %-12s %-10s %-14s%n",
                    t.getId(), t.getTitle() + marca,
                    t.getStatus().getEtiqueta(), t.getPriority().getEtiqueta(), responsable);
        }
        System.out.println("(*) tarea vencida");
    }

    /**
     * Opción 3: completar por id. VERSIÓN DE D3 (findById devuelve Task/null).
     *
     * TODO (cirugía de Optional): cuando cambies repo.findById a Optional<Task> (MP-8), este
     * método dejará de compilar. Reescríbelo así:
     *     Task t = repo.findById(id).orElseThrow(() -> new TaskNotFoundException(id));
     * dentro de try/catch de TaskNotFoundException (mensaje claro, sin stack trace) y de
     * TaskValidationException (la regla "no DONE sin assignee"). Importa TaskNotFoundException.
     */
    private static void completarPorId(Scanner sc, InMemoryTaskRepository repo) {
        long id = leerEntero(sc, "Id de la tarea a completar: ");
        Task t = repo.findById(id);
        if (t == null) {
            System.out.println("No existe tarea con id " + id + ".");
            return;
        }
        try {
            t.setStatus(TaskStatus.DONE);
            System.out.println("Tarea #" + id + " completada: " + t.getTitle());
        } catch (TaskValidationException e) {
            System.out.println("No se pudo completar: " + e.getMessage());
        }
    }

    /** Opción 4: elimina por id usando el boolean de deleteById. Entrada no numérica NO crashea. */
    private static void eliminarPorId(Scanner sc, InMemoryTaskRepository repo) {
        long id = leerEntero(sc, "Id de la tarea a eliminar: ");
        if (repo.deleteById(id)) {
            System.out.println("Tarea " + id + " eliminada.");
        } else {
            System.out.println("No existe tarea con id " + id + ".");
        }
    }

    /**
     * Opción 5 (NUEVA hoy) — Buscar por id.
     *
     * TODO (cirugía de Optional): tras cambiar findById a Optional<Task>, impleméntalo con
     *     repo.findById(id).ifPresentOrElse(
     *         t -> System.out.println("Encontrada: " + t),
     *         () -> System.out.println("No existe tarea con id " + id + "."));
     * Cero null, cero stack trace, cero .get().
     */
    private static void buscarPorId(Scanner sc, InMemoryTaskRepository repo) {
        long id = leerEntero(sc, "Id de la tarea a buscar: ");
        // TODO: implementar con Optional.ifPresentOrElse (ver MP-8/MP-9).
        System.out.println("(pendiente: buscar por id " + id + ")");
    }

    // ==================== Submenú de Reportes (TU TRABAJO: delega en ReportService) ====================

    private static void menuReportes(Scanner sc, ReportService reportes) {
        int op;
        do {
            System.out.println("""

                    --- Reportes ---
                    1) Tareas por estado
                    2) Pendientes ordenadas por fecha
                    3) Buscar por título
                    4) % completadas
                    5) Tareas por asignado
                    0) Volver""");
            op = leerEntero(sc, "Reporte: ");
            switch (op) {
                case 1 -> reporteTareasPorEstado(reportes);
                case 2 -> reportePendientesPorFecha(reportes);
                case 3 -> reporteBuscarPorTitulo(sc, reportes);
                case 4 -> reportePorcentajeCompletadas(reportes);
                case 5 -> reporteTareasPorAsignado(reportes);
                case 0 -> System.out.println("Volviendo al menú principal...");
                default -> System.out.println("Opción inválida: [" + op + "].");
            }
        } while (op != 0);
    }

    /**
     * TODO A — imprime reportes.tareasPorEstado() : Map<TaskStatus, List<Task>>.
     * Sugerencia: map.forEach((estado, tareas) -> imprime encabezado con estado.getEtiqueta()
     * y tareas.size(), luego cada título).
     */
    private static void reporteTareasPorEstado(ReportService reportes) {
        // TODO A
        System.out.println("(pendiente: tareas por estado)");
    }

    /** TODO B — imprime reportes.pendientesPorFecha() como tabla printf (dueDate null = "sin fecha"). */
    private static void reportePendientesPorFecha(ReportService reportes) {
        // TODO B
        System.out.println("(pendiente: pendientes por fecha)");
    }

    /** TODO C — pide un texto, imprime reportes.buscarPorTitulo(q); lista vacía -> mensaje claro. */
    private static void reporteBuscarPorTitulo(Scanner sc, ReportService reportes) {
        // TODO C
        System.out.println("(pendiente: buscar por título)");
    }

    /** TODO D — imprime reportes.porcentajeCompletadas() con "%.1f%%". */
    private static void reportePorcentajeCompletadas(ReportService reportes) {
        // TODO D
        System.out.println("(pendiente: % completadas)");
    }

    /**
     * TODO E — imprime reportes.tareasPorAsignado() : Map<Long, List<Task>> y, aparte,
     * reportes.sinAsignar() como "Sin asignar: n".
     */
    private static void reporteTareasPorAsignado(ReportService reportes) {
        // TODO E
        System.out.println("(pendiente: tareas por asignado)");
    }

    // ==================== Utilidades de lectura robusta (de D2 MP-9, ya resueltas) ====================

    /** Lee la prioridad como número (1=LOW, 2=MED, 3=HIGH); repregunta si es inválida. */
    private static Priority leerPrioridad(Scanner sc) {
        while (true) {
            int p = leerEntero(sc, "Prioridad (1=LOW, 2=MED, 3=HIGH): ");
            Priority pr = switch (p) {
                case 1 -> Priority.LOW;
                case 2 -> Priority.MED;
                case 3 -> Priority.HIGH;
                default -> null;
            };
            if (pr != null) {
                return pr;
            }
            System.out.println("Prioridad inválida: [" + p + "]. Usa 1, 2 o 3.");
        }
    }

    /** Reintenta hasta leer un entero válido. Entrada no numérica NO crashea: repregunta. */
    private static int leerEntero(Scanner sc, String prompt) {
        while (true) {
            System.out.print(prompt);
            String entrada = sc.nextLine();
            try {
                return Integer.parseInt(entrada.trim());
            } catch (NumberFormatException e) {
                System.out.println("Eso no es un número entero: [" + entrada + "]. Intenta de nuevo.");
            }
        }
    }

    /** Reintenta hasta leer una fecha ISO (yyyy-MM-dd) válida; vacío = null (sin fecha). */
    private static LocalDate leerFecha(Scanner sc, String prompt) {
        while (true) {
            System.out.print(prompt);
            String entrada = sc.nextLine().trim();
            if (entrada.isEmpty()) {
                return null; // vacío = sin fecha
            }
            try {
                return LocalDate.parse(entrada);
            } catch (DateTimeParseException e) {
                System.out.println("Fecha inválida: [" + entrada + "]. Usa el formato yyyy-MM-dd.");
            }
        }
    }
}
