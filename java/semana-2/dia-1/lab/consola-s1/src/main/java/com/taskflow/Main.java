package com.taskflow;

import com.taskflow.exception.TaskNotFoundException;
import com.taskflow.exception.TaskValidationException;
import com.taskflow.model.Priority;
import com.taskflow.model.Task;
import com.taskflow.model.TaskStatus;
import com.taskflow.persistence.FileTaskRepository;
import com.taskflow.repository.TaskRepository;
import com.taskflow.service.ReportService;

import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * Integrador S1 — TaskFlow CLI v1.0 FINAL — SOLUCIÓN DE REFERENCIA
 *
 * Cierra la Semana 1: el CLI v3 de ayer (menú + reportes con streams + Optional) AHORA persiste.
 * v0->v3 fueron incrementos diarios; hoy la bautizamos v1.0 porque es la primera versión COMPLETA
 * de TaskFlow: funcional + persistente + probada. El renombre es narrativa, no errata.
 *
 * Lo NUEVO de hoy respecto a v3:
 *   - El repositorio es un FileTaskRepository (implements TaskRepository, compone InMemory + CSV).
 *   - Al ARRANCAR: repo.load() lee data/tasks.csv (o arranca vacío sin crashear si no existe).
 *   - Al SALIR: repo.save() escribe el snapshot completo al CSV y confirma "N tareas guardadas".
 *   - La IOException se maneja DENTRO de la capa de persistencia; este menú nunca muere por un archivo.
 *
 * Prueba de fuego (DoD): crear una tarea -> Salir -> volver a abrir -> la tarea sigue ahí.
 *
 * Menú principal:
 *   1) Agregar tarea   3) Completar por id   5) Buscar por id
 *   2) Listar tareas   4) Eliminar por id    6) Reportes         7) Salir (guarda)
 */
public class Main {

    // El capstone exige "no hay Task sin Project": en S1 el proyecto es solo un id.
    private static final Long PROYECTO_DEMO = 1L;

    // Ruta RELATIVA a la raíz del proyecto (regla del curso: se corre desde ahí; ver MP-1).
    private static final Path RUTA_CSV = Path.of("data", "tasks.csv");

    /**
     * Orden del listado (opción 2): prioridad de mayor a menor (HIGH->LOW) y, a igualdad, fecha
     * ascendente con las tareas SIN fecha al final. nullsLast evita el NPE al comparar fechas null.
     */
    private static final Comparator<Task> ORDEN_LISTADO =
            Comparator.comparing(Task::getPriority, Comparator.reverseOrder())
                    .thenComparing(Task::getDueDate, Comparator.nullsLast(Comparator.naturalOrder()));

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // --- Persistencia: el repo con archivo, cargado al arrancar ---
        FileTaskRepository repo = new FileTaskRepository(RUTA_CSV);
        int cargadas = repo.load();
        if (cargadas > 0) {
            System.out.println("Cargadas " + cargadas + " tareas desde " + RUTA_CSV + ".");
        }
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
                case 7 -> salirGuardando(repo);
                default -> System.out.println(
                        "Opción inválida: [" + opcion + "]. Elige una del menú.");
            }
        } while (opcion != 7);

        scanner.close();
    }

    private static void mostrarMenu() {
        System.out.println("""

                === TaskFlow CLI v1.0 ===
                1) Agregar tarea
                2) Listar tareas
                3) Completar por id
                4) Eliminar por id
                5) Buscar por id
                6) Reportes
                7) Salir (guarda a disco)""");
    }

    /**
     * Opción 7 — Salir: escribe el snapshot completo al CSV y confirma. Toda la lógica de archivo
     * (y su IOException) vive en repo.save(); aquí solo mostramos el resultado.
     */
    private static void salirGuardando(FileTaskRepository repo) {
        int guardadas = repo.save();
        if (guardadas >= 0) {
            System.out.println(guardadas + " tareas guardadas en " + RUTA_CSV + ".");
        }
        System.out.println("¡Hasta mañana! Recuerda: commit "
                + "\"feat: taskflow cli v1.0 - persistencia y tests\" y push.");
    }

    // ==================== Operaciones CRUD ====================

    /** Opción 1: agrega una tarea con Task.crear en try/catch. El id lo asigna save. */
    private static void agregarTarea(Scanner sc, TaskRepository repo) {
        System.out.print("Título: ");
        String title = sc.nextLine();
        System.out.print("Descripción (sin comas; vacío = sin descripción): ");
        String description = sc.nextLine();
        Priority priority = leerPrioridad(sc);
        LocalDate dueDate = leerFecha(sc, "Fecha límite (yyyy-MM-dd, vacío = sin fecha): ");
        try {
            // El menú SIEMPRE pasa por crear (creación de negocio); projectId = demo, assignee null.
            Task nueva = Task.crear(title, description, priority, dueDate, PROYECTO_DEMO, null);
            repo.save(nueva); // aquí nace el id
            System.out.println("Creada tarea #" + nueva.getId() + ": " + nueva.getTitle());
        } catch (TaskValidationException e) {
            System.out.println("No se pudo crear la tarea: " + e.getMessage());
        }
    }

    /** Opción 2: findAll() ordenado con ORDEN_LISTADO vía Stream; tabla printf; vacío -> mensaje. */
    private static void listarTareas(TaskRepository repo) {
        List<Task> tareas = repo.findAll();   // copia defensiva: ordenarla NO toca el repositorio
        if (tareas.isEmpty()) {
            System.out.println("(sin tareas todavía)");
            return;
        }
        System.out.printf("%-4s %-34s %-12s %-10s %-14s%n",
                "ID", "TÍTULO", "ESTADO", "PRIORIDAD", "RESPONSABLE");
        System.out.println("---- ---------------------------------- ------------ ---------- --------------");
        tareas.stream()
                .sorted(ORDEN_LISTADO)         // ordena un stream de la COPIA, no la fuente
                .forEach(Main::imprimirFila);  // method reference: por cada tarea, imprime su fila
        System.out.println("(*) tarea vencida");
    }

    /** Imprime una fila de la tabla de tareas (usada como Consumer<Task> en el forEach de arriba). */
    private static void imprimirFila(Task t) {
        String marca = t.estaVencida() ? " *" : "";
        String responsable = (t.getAssigneeId() == null) ? "-" : ("#" + t.getAssigneeId());
        System.out.printf("%-4d %-34s %-12s %-10s %-14s%n",
                t.getId(),
                t.getTitle() + marca,
                t.getStatus().getEtiqueta(),   // etiqueta del enum, no el valor crudo
                t.getPriority().getEtiqueta(),
                responsable);
    }

    /**
     * Opción 3: completar por id. findById(id).orElseThrow(...) trueca el viejo "if == null" por
     * un fallo EXPLÍCITO con mensaje de negocio. La regla "no DONE sin assignee" vive en setStatus.
     */
    private static void completarPorId(Scanner sc, TaskRepository repo) {
        long id = leerEntero(sc, "Id de la tarea a completar: ");
        try {
            Task t = repo.findById(id).orElseThrow(() -> new TaskNotFoundException(id));
            t.setStatus(TaskStatus.DONE);
            System.out.println("Tarea #" + id + " completada: " + t.getTitle());
        } catch (TaskNotFoundException e) {
            System.out.println(e.getMessage());               // "No existe tarea con id X."
        } catch (TaskValidationException e) {
            System.out.println("No se pudo completar: " + e.getMessage());
        }
    }

    /** Opción 4: elimina por id usando el boolean de deleteById. Entrada no numérica NO crashea. */
    private static void eliminarPorId(Scanner sc, TaskRepository repo) {
        long id = leerEntero(sc, "Id de la tarea a eliminar: ");
        if (repo.deleteById(id)) {
            System.out.println("Tarea " + id + " eliminada.");
        } else {
            System.out.println("No existe tarea con id " + id + ".");
        }
    }

    /** Opción 5: buscar por id con ifPresentOrElse (sin null y sin stack trace, ni un .get()). */
    private static void buscarPorId(Scanner sc, TaskRepository repo) {
        long id = leerEntero(sc, "Id de la tarea a buscar: ");
        repo.findById(id).ifPresentOrElse(
                t -> System.out.println("Encontrada: " + t),
                () -> System.out.println("No existe tarea con id " + id + "."));
    }

    // ==================== Submenú de Reportes (imprime lo que ReportService retorna) ====================

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
                    6) Tareas por prioridad      (STRETCH)
                    7) Top 3 urgentes            (STRETCH)
                    8) Exportar títulos a CSV     (STRETCH)
                    9) Vencidas vs no vencidas    (STRETCH)
                    0) Volver""");
            op = leerEntero(sc, "Reporte: ");
            switch (op) {
                case 1 -> reporteTareasPorEstado(reportes);
                case 2 -> reportePendientesPorFecha(reportes);
                case 3 -> reporteBuscarPorTitulo(sc, reportes);
                case 4 -> reportePorcentajeCompletadas(reportes);
                case 5 -> reporteTareasPorAsignado(reportes);
                case 6 -> reporteTareasPorPrioridad(reportes);      // STRETCH
                case 7 -> reporteTop3Urgentes(reportes);            // STRETCH
                case 8 -> reporteTitulosCsv(reportes);              // STRETCH
                case 9 -> reporteVencidas(reportes);               // STRETCH
                case 0 -> System.out.println("Volviendo al menú principal...");
                default -> System.out.println("Opción inválida: [" + op + "].");
            }
        } while (op != 0);
    }

    /** Reporte 1: imprime cada grupo de estado con su conteo (size de la lista). */
    private static void reporteTareasPorEstado(ReportService reportes) {
        Map<TaskStatus, List<Task>> porEstado = reportes.tareasPorEstado();
        System.out.println("== Tareas por estado ==");
        porEstado.forEach((estado, tareas) -> {
            System.out.println(estado.getEtiqueta() + " (" + tareas.size() + "):");
            tareas.forEach(t -> System.out.println("   - " + t.getTitle()));
        });
    }

    /** Reporte 2: tabla printf de las pendientes ordenadas por fecha (nulls al final). */
    private static void reportePendientesPorFecha(ReportService reportes) {
        List<Task> pendientes = reportes.pendientesPorFecha();
        System.out.println("== Pendientes ordenadas por fecha ==");
        if (pendientes.isEmpty()) {
            System.out.println("(no hay tareas pendientes)");
            return;
        }
        System.out.printf("%-4s %-34s %-12s%n", "ID", "TÍTULO", "FECHA");
        System.out.println("---- ---------------------------------- ------------");
        pendientes.forEach(t -> {
            String fecha = (t.getDueDate() == null) ? "sin fecha" : t.getDueDate().toString();
            System.out.printf("%-4d %-34s %-12s%n", t.getId(), t.getTitle(), fecha);
        });
    }

    /** Reporte 3: busca por texto; lista vacía -> mensaje claro (no crashea). */
    private static void reporteBuscarPorTitulo(Scanner sc, ReportService reportes) {
        System.out.print("Texto a buscar en el título: ");
        String q = sc.nextLine();
        List<Task> encontradas = reportes.buscarPorTitulo(q);
        if (encontradas.isEmpty()) {
            System.out.println("(sin coincidencias para \"" + q + "\")");
            return;
        }
        System.out.println("Coincidencias para \"" + q + "\":");
        encontradas.forEach(t -> System.out.println("   #" + t.getId() + " " + t.getTitle()));
    }

    /** Reporte 4: porcentaje de completadas con 1 decimal. */
    private static void reportePorcentajeCompletadas(ReportService reportes) {
        System.out.printf("Tareas completadas: %.1f%%%n", reportes.porcentajeCompletadas());
    }

    /** Reporte 5: grupos por responsable + las "Sin asignar: n" aparte. */
    private static void reporteTareasPorAsignado(ReportService reportes) {
        Map<Long, List<Task>> porAsignado = reportes.tareasPorAsignado();
        System.out.println("== Tareas por asignado ==");
        porAsignado.forEach((assigneeId, tareas) -> {
            System.out.println("Responsable #" + assigneeId + " (" + tareas.size() + "):");
            tareas.forEach(t -> System.out.println("   - " + t.getTitle()));
        });
        System.out.println("Sin asignar: " + reportes.sinAsignar());
    }

    // ---- STRETCH ----

    private static void reporteTareasPorPrioridad(ReportService reportes) {
        System.out.println("== Tareas por prioridad (STRETCH) ==");
        reportes.tareasPorPrioridad().forEach((prioridad, tareas) -> {
            System.out.println(prioridad.getEtiqueta() + " (" + tareas.size() + "):");
            tareas.forEach(t -> System.out.println("   - " + t.getTitle()));
        });
    }

    private static void reporteTop3Urgentes(ReportService reportes) {
        System.out.println("== Top 3 urgentes (STRETCH) ==");
        reportes.top3Urgentes().forEach(t ->
                System.out.println("   [" + t.getPriority().getEtiqueta() + "] " + t.getTitle()));
    }

    private static void reporteTitulosCsv(ReportService reportes) {
        System.out.println("== Títulos en CSV (STRETCH) ==");
        System.out.println(reportes.titulosCsv());
    }

    private static void reporteVencidas(ReportService reportes) {
        System.out.println("== Vencidas vs no vencidas (STRETCH) ==");
        Map<Boolean, List<Task>> particion = reportes.vencidasVsNoVencidas();
        System.out.println("Vencidas: " + particion.get(true).size());
        System.out.println("No vencidas: " + particion.get(false).size());
    }

    // ==================== Utilidades de lectura robusta (de D2 MP-9, tal cual) ====================

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
