package com.taskflow.config;

import com.taskflow.exception.TaskValidationException;
import com.taskflow.model.Priority;
import com.taskflow.model.Project;
import com.taskflow.model.Role;
import com.taskflow.model.Task;
import com.taskflow.model.TaskStatus;
import com.taskflow.model.User;
import com.taskflow.repository.InMemoryProjectRepository;
import com.taskflow.repository.TaskRepository;
import org.springframework.boot.CommandLineRunner;
// TODO (MP-4): descomenta este import junto con la anotación @Component de abajo.
// import org.springframework.stereotype.Component;

import java.time.LocalDate;

/**
 * DataSeeder — la NUEVA "semilla" del proyecto. PROVISTO HOY (S2D2). Reemplaza al runner/SeedRunner.
 *
 * ===================== QUÉ HACER EN MP-4 (Paso 0) =====================
 *   1) Descomenta el import de @Component y la anotación @Component sobre la clase.
 *   2) BORRA el archivo runner/SeedRunner.java (ya nadie imprime por consola: desde hoy la salida
 *      es HTTP). Si dejas los DOS activos, ambos siembran y tendrás datos duplicados.
 * =====================================================================
 *
 * NOTA (red de seguridad): este archivo es la versión FINAL del seeder — inyecta AMBOS repos
 * (TaskRepository + InMemoryProjectRepository) y siembra 3 proyectos + 9 tareas. El handout
 * (alumno.md) lo construye POR ETAPAS: en MP-4 solo las tareas (constructor con TaskRepository
 * a secas) y en el integrador (Paso 3b) añade los proyectos e InMemoryProjectRepository. Los
 * DATOS (títulos, usuarios, proyectos) son idénticos por ambos caminos, así que el estado final
 * coincide con esta versión.
 *
 * Diferencia clave con el SeedRunner de D1: aquí sembramos por el CONSTRUCTOR de REHIDRATACIÓN
 * (new Task(id, ...)), no por la factory Task.crear(...). Eso nos deja meter datos que la factory de
 * negocio prohíbe: tareas VENCIDAS (dueDate en el pasado) y estados variados. Es el mismo permiso que
 * usó SeedData en S1D4. Las reglas del capstone se dividen así:
 *   - creación (Task.crear): "dueDate no en el pasado" -> por eso NO se puede sembrar una vencida ahí.
 *   - rehidratación (constructor): reconstruye datos que YA existían -> una vencida es un dato válido.
 *
 * La semilla cumple, a propósito, lo que mañana (D3) alimenta la matriz del 422 y hoy los endpoints:
 *   - 2-3 proyectos, y UNO SIN tareas (para probar el 200 con [] en GET /projects/{id}/tasks).
 *   - >=2 tareas por estado (ahora sí hay DONE que filtrar en ?status=DONE).
 *   - >=1 tarea vencida (dueDate pasado y no DONE).
 *   - >=1 tarea con assigneeId y >=1 sin (mañana: la regla "no DONE sin assignee").
 */
// TODO (MP-4): añade @Component aquí (y borra runner/SeedRunner.java).
// @Component
public class DataSeeder implements CommandLineRunner {

    private final TaskRepository taskRepository;
    private final InMemoryProjectRepository projectRepository;

    public DataSeeder(TaskRepository taskRepository, InMemoryProjectRepository projectRepository) {
        this.taskRepository = taskRepository;
        this.projectRepository = projectRepository;
    }

    @Override
    public void run(String... args) throws TaskValidationException {
        sembrarProyectos();
        sembrarTareas();
    }

    /** 2 usuarios dueños + 3 proyectos. El proyecto 3 se queda SIN tareas (caso 200 con []). */
    private void sembrarProyectos() {
        User ana = new User(1L, "ana", "ana@taskflow.dev", Role.ADMIN);
        User beto = new User(2L, "beto", "beto@taskflow.dev", Role.USER);

        projectRepository.save(new Project(1L, "Plataforma TaskFlow",
                "El backend REST del capstone", ana, LocalDate.now().minusDays(30)));
        projectRepository.save(new Project(2L, "App Móvil",
                "Cliente móvil que consume la API", beto, LocalDate.now().minusDays(20)));
        projectRepository.save(new Project(3L, "Migración Legacy",
                "Aún sin tareas cargadas", ana, LocalDate.now().minusDays(5)));
    }

    /**
     * 9 tareas por el constructor de rehidratación (id explícito -> el repo hace upsert y avanza la
     * secuencia). Reparto: proyecto 1 = tareas 1,2,3,4,9; proyecto 2 = tareas 5,6,7,8; proyecto 3 = 0.
     */
    private void sembrarTareas() throws TaskValidationException {
        // --- Proyecto 1: Plataforma TaskFlow ---
        taskRepository.save(new Task(1L, "Diseñar esquema de BD", "Tablas Project/Task/User",
                TaskStatus.TODO, Priority.HIGH, 1L, 1L, LocalDate.now().plusDays(5)));
        taskRepository.save(new Task(2L, "Configurar Spring Boot", "Initializr + starter-web",
                TaskStatus.DONE, Priority.MED, 1L, 1L, LocalDate.now().minusDays(2)));   // pasada pero DONE -> NO vencida
        taskRepository.save(new Task(3L, "Endpoint de login", "POST /auth/login con JWT",
                TaskStatus.IN_PROGRESS, Priority.HIGH, 1L, 2L, LocalDate.now().plusDays(3)));
        taskRepository.save(new Task(4L, "Escribir tests MockMvc", "Capa web aislada",
                TaskStatus.TODO, Priority.MED, 1L, null, LocalDate.now().plusDays(7)));  // SIN assignee
        taskRepository.save(new Task(9L, "Optimizar consultas", "Índices y paginación",
                TaskStatus.TODO, Priority.LOW, 1L, 1L, null));

        // --- Proyecto 2: App Móvil ---
        taskRepository.save(new Task(5L, "Corregir bug de fechas", "Zona horaria en el cliente",
                TaskStatus.IN_PROGRESS, Priority.LOW, 2L, 2L, LocalDate.now().minusDays(1)));  // VENCIDA (pasada y no DONE)
        taskRepository.save(new Task(6L, "Publicar en la tienda", "Alta en App Store / Play",
                TaskStatus.TODO, Priority.HIGH, 2L, null, LocalDate.now().plusDays(10)));  // SIN assignee
        taskRepository.save(new Task(7L, "Integrar pasarela de pago", "Checkout con Stripe",
                TaskStatus.DONE, Priority.HIGH, 2L, 1L, LocalDate.now().minusDays(5)));   // pasada pero DONE -> NO vencida
        taskRepository.save(new Task(8L, "Documentar API con Swagger", "OpenAPI 3",
                TaskStatus.IN_PROGRESS, Priority.MED, 2L, 2L, null));
    }
}
