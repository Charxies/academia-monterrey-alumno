package com.taskflow.runner;

import com.taskflow.exception.TaskValidationException;
import com.taskflow.model.Priority;
import com.taskflow.model.Task;
import com.taskflow.service.TaskService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

/**
 * SeedRunner — la "UI" de hoy: siembra 5 tareas al arrancar y las lista en consola.
 *
 * @Component + implements CommandLineRunner: Spring detecta el bean y ejecuta run(...) UNA vez, al
 * terminar de levantar el contexto. CommandLineRunner es una interfaz funcional (una más, como los
 * Comparator/Predicate de S1). El constructor recibe TaskService: NADIE hace 'new TaskService' —
 * el contenedor lo construye, lo cablea y nos lo entrega. Eso es Inyección de Dependencias.
 *
 * Nota del día: SIN spring-boot-starter-web no hay servidor; el proceso levanta el contexto, corre
 * este runner y TERMINA con exit 0. Es el comportamiento correcto de hoy. Mañana (D2) el starter web
 * deja el proceso vivo escuchando el 8080 y el SeedRunner deja de ser la UI (nace TaskController).
 */
@Component
public class SeedRunner implements CommandLineRunner {

    private final TaskService taskService;

    // STRETCH: configuración externa. taskflow.seed.enabled (application.yml) permite saltarse la
    // siembra sin tocar código; @Value la lee, con default true si la propiedad no estuviera.
    private final boolean seedEnabled;

    public SeedRunner(TaskService taskService,
                      @Value("${taskflow.seed.enabled:true}") boolean seedEnabled) {
        this.taskService = taskService;
        this.seedEnabled = seedEnabled;
    }

    @Override
    public void run(String... args) {
        if (!seedEnabled) {                                        // STRETCH
            System.out.println("(taskflow.seed.enabled=false -> no se siembra)");
            return;
        }
        sembrar();
        imprimirTabla(taskService.listar());
    }

    /**
     * Siembra 5 tareas vía taskService.crear(...) (que por dentro pasa por la factory Task.crear).
     * Hoy NO hay vencidas en la semilla: entran por la factory de negocio y la regla "dueDate no en
     * el pasado" lo impide. Las vencidas regresan en D4, cuando los datos entren por la BD. Uso de
     * LocalDate.now().plusDays(...) para que las fechas sean siempre futuras. La checked
     * TaskValidationException se maneja en un try/catch ÚNICO: una semilla inválida sería un bug del
     * material, no un error del usuario.
     */
    private void sembrar() {
        try {
            taskService.crear("Configurar taskflow-api", "Arranque desde Initializr", Priority.HIGH,
                    LocalDate.now().plusDays(3));
            taskService.crear("Copiar dominio de S1", "model + exception, sin Describible", Priority.MED,
                    LocalDate.now().plusDays(7));
            taskService.crear("Anotar el repositorio", "InMemoryTaskRepository como @Repository",
                    Priority.HIGH, null);
            taskService.crear("Escribir TaskService", "crear / listar / completar inyectado",
                    Priority.LOW, LocalDate.now().plusDays(1));
            taskService.crear("Sembrar datos demo", "este SeedRunner como CommandLineRunner",
                    Priority.MED, null);
        } catch (TaskValidationException e) {
            // Semilla inválida = bug del material (mismo patrón que SeedData de S1D4).
            System.out.println("No se pudo sembrar (bug de la semilla): " + e.getMessage());
        }
    }

    /** Tabla printf de las tareas ya ordenadas por urgencia (formato de S1D1). */
    private void imprimirTabla(List<Task> tareas) {
        System.out.println();
        System.out.println("=== taskflow-api v0.1 — tareas sembradas (orden: urgencia) ===");
        System.out.printf("%-4s %-30s %-12s %-10s %-12s%n",
                "ID", "TÍTULO", "ESTADO", "PRIORIDAD", "VENCE");
        System.out.println("---- ------------------------------ ------------ ---------- ------------");
        for (Task t : tareas) {
            String vence = (t.getDueDate() == null) ? "sin fecha" : t.getDueDate().toString();
            System.out.printf("%-4d %-30s %-12s %-10s %-12s%n",
                    t.getId(),
                    t.getTitle(),
                    t.getStatus().getEtiqueta(),
                    t.getPriority().getEtiqueta(),
                    vence);
        }
    }
}
