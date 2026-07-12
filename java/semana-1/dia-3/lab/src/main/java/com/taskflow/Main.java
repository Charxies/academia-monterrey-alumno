package com.taskflow;

import com.taskflow.exception.TaskValidationException;
import com.taskflow.model.Priority;
import com.taskflow.model.Task;
import com.taskflow.model.TaskStatus;
import com.taskflow.repository.InMemoryTaskRepository;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

/**
 * Integrador D3 — TaskFlow CLI v2 (STARTER).
 *
 * Evoluciona la CLI v1 de ayer: los datos dejan de vivir en el main y pasan a
 * InMemoryTaskRepository (Map&lt;Long,Task&gt; + ids autoincrementales). El menú NUNCA toca el
 * Map: TODO pasa por save / findById / findAll / deleteById.
 *
 * Ya viene resuelto: el ciclo del menú, la precarga (por repo.save), y las utilidades de
 * lectura robusta (leerEntero / leerFecha, de D2 MP-9). Tu trabajo son los 5 TODO de las
 * operaciones + primero completar InMemoryTaskRepository.
 *
 * El esqueleto COMPILA tal cual (las operaciones son stubs). Correr:
 *   mvn -q compile exec:java
 */
public class Main {

    // Proyecto demo: el capstone exige "no hay Task sin Project". En D3 el proyecto es solo un id.
    private static final Long PROYECTO_DEMO = 1L;
    private static final Long USUARIO_DEMO = 1L;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        InMemoryTaskRepository repo = new InMemoryTaskRepository();
        precargar(repo);

        int opcion;
        do {
            mostrarMenu();
            opcion = leerEntero(scanner, "Elige una opción: ");
            switch (opcion) {
                case 1 -> agregarTarea(scanner, repo);
                case 2 -> listarTareas(repo);
                case 3 -> completarPorId(scanner, repo);
                case 4 -> eliminarPorId(scanner, repo);
                case 5 -> resumenPorEstado(repo);
                case 6 -> System.out.println("¡Hasta mañana! Recuerda: commit y push.");
                default -> System.out.println(
                        "Opción inválida: [" + opcion + "]. Elige una del menú.");
            }
        } while (opcion != 6);

        scanner.close();
    }

    /**
     * Precarga 5 tareas por el REPOSITORIO (repo.save), con el constructor de rehidratación.
     * Entran sin id; save les asignará 1L..5L (cuando lo implementes). Semilla lista para
     * probar el menú: la #3 está vencida (dueDate de ayer y no DONE); 1,2,5 traen assigneeId
     * y 3,4 no (para probar "Completar" con y sin responsable).
     */
    private static void precargar(InMemoryTaskRepository repo) {
        try {
            repo.save(new Task(null, "Diseñar modelo de dominio", "Entidades User, Project, Task",
                    TaskStatus.DONE, Priority.HIGH, PROYECTO_DEMO, USUARIO_DEMO, LocalDate.now().minusDays(10)));
            repo.save(new Task(null, "Configurar repositorio", "Repo y CI base",
                    TaskStatus.IN_PROGRESS, Priority.MED, PROYECTO_DEMO, USUARIO_DEMO, null));
            repo.save(new Task(null, "Implementar menú de consola", "CLI v2 con repositorio",
                    TaskStatus.IN_PROGRESS, Priority.HIGH, PROYECTO_DEMO, null, LocalDate.now().minusDays(1)));
            repo.save(new Task(null, "Escribir tests del dominio", "JUnit para Task",
                    TaskStatus.TODO, Priority.LOW, PROYECTO_DEMO, null, LocalDate.now().plusDays(5)));
            repo.save(new Task(null, "Desplegar API en la nube", "Deploy con CI/CD",
                    TaskStatus.TODO, Priority.MED, PROYECTO_DEMO, USUARIO_DEMO, null));
        } catch (TaskValidationException e) {
            throw new IllegalStateException("Semilla inválida (bug del programador): " + e.getMessage(), e);
        }
    }

    private static void mostrarMenu() {
        System.out.println("""

                === TaskFlow CLI v2 ===
                1) Agregar tarea
                2) Listar tareas
                3) Completar por id
                4) Eliminar por id
                5) Resumen por estado
                6) Salir""");
    }

    /**
     * TODO 1 — Agregar tarea:
     *   - Pide título (sc.nextLine()), prioridad (usa leerPrioridad -> también TODO) y
     *     dueDate con leerFecha(sc, "...").
     *   - Task.crear(title, "", priority, dueDate, PROYECTO_DEMO, null) dentro de try/catch
     *     de TaskValidationException (si truena una regla: mensaje claro y volver al menú).
     *   - repo.save(nueva) asigna el id; muestra "Creada tarea #" + nueva.getId().
     */
    private static void agregarTarea(Scanner sc, InMemoryTaskRepository repo) {
        // TODO 1
        System.out.println("(pendiente: agregar tarea)");
    }

    /**
     * TODO 2 — Listar tareas:
     *   - List<Task> tareas = repo.findAll(); si isEmpty() -> mensaje y return.
     *   - Ordena la COPIA con un Comparator: prioridad HIGH->LOW y a igualdad dueDate asc nulls last:
     *       Comparator.comparing(Task::getPriority, Comparator.reverseOrder())
     *           .thenComparing(Task::getDueDate, Comparator.nullsLast(Comparator.naturalOrder()))
     *     (el reverse va POR CRITERIO, no con reversed() al final; nullsLast evita el NPE).
     *   - Imprime tabla printf con getId/getTitle/getStatus().getEtiqueta()/getPriority().getEtiqueta()
     *     y marca " *" si estaVencida().
     */
    private static void listarTareas(InMemoryTaskRepository repo) {
        // TODO 2
        System.out.println("(pendiente: listar tareas)");
    }

    /**
     * TODO 3 — Completar por id:
     *   - Lee el id (leerEntero); Task t = repo.findById(id); si null -> "No existe tarea con id X".
     *   - Si existe: t.setStatus(TaskStatus.DONE) en try/catch de TaskValidationException
     *     (la regla "no DONE sin assignee" avisa sin crashear).
     */
    private static void completarPorId(Scanner sc, InMemoryTaskRepository repo) {
        // TODO 3
        System.out.println("(pendiente: completar por id)");
    }

    /**
     * TODO 4 — Eliminar por id:
     *   - Lee el id (leerEntero); usa el boolean de repo.deleteById(id):
     *     true -> "Tarea X eliminada"; false -> "No existe tarea con id X". No crashea con no-numérico.
     */
    private static void eliminarPorId(Scanner sc, InMemoryTaskRepository repo) {
        // TODO 4
        System.out.println("(pendiente: eliminar por id)");
    }

    /**
     * TODO 5 — Resumen por estado:
     *   - Map<TaskStatus,Integer> con getOrDefault(k,0)+1 recorriendo repo.findAll().
     *   - Imprime "TODO: n | IN_PROGRESS: n | DONE: n".
     */
    private static void resumenPorEstado(InMemoryTaskRepository repo) {
        // TODO 5
        System.out.println("(pendiente: resumen por estado)");
    }

    /**
     * TODO 6 — leerPrioridad: lee un número con leerEntero (1=LOW, 2=MED, 3=HIGH) y devuelve el
     * Priority; repregunta si es inválido. (Úsala en agregarTarea.)
     */
    // private static Priority leerPrioridad(Scanner sc) { ... }

    // ============ Utilidades de lectura robusta (de D2 MP-9, ya resueltas) ============

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
