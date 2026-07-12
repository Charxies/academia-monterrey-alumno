package com.taskflow.config;

import com.taskflow.exception.TaskValidationException;
import com.taskflow.model.Priority;
import com.taskflow.model.Project;
import com.taskflow.model.Role;
import com.taskflow.model.Task;
import com.taskflow.model.TaskStatus;
import com.taskflow.model.User;
import com.taskflow.repository.ProjectRepository;
import com.taskflow.repository.TaskRepository;
import com.taskflow.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

/**
 * DataSeeder — la semilla del proyecto (MP-9). Ahora escribe en la BD (H2 archivo en runtime) a
 * través de los repositorios JPA.
 *
 * CONDICIONADO a count() == 0 (MP-9): con ddl-auto=update la tabla y los datos SOBREVIVEN al
 * reinicio, así que sembrar en CADA arranque duplicaría todo. Solo sembramos si la tabla está vacía
 * (primer arranque). Por eso NO usamos data.sql como camino principal: data.sql corre en cada arranque
 * y con 'update' acumula duplicados (además exige defer-datasource-initialization por el orden con JPA).
 *
 * IDs: el DataSeeder construye TODO con id = null y la BD los asigna (IDENTITY). La secuencia manual
 * de S1 está MUERTA. En una BD fresca el orden de inserción fija los ids: users ana(1)/luis(2);
 * projects 1..3; tasks 1..9. El ORDEN users -> projects -> tasks lo OBLIGA la FK task.project_id.
 *
 * ana (id 1) y luis (id 2) son los usuarios canónicos que D5 completa (les añade passwordHash y suma
 * a 'admin'; no se re-crean). El proyecto 1 tiene ownerId = ana: D5 lo usa para la regla "solo owner
 * o ADMIN borra el proyecto".
 *
 * La semilla cumple, a propósito, lo que alimentan los endpoints y los tests: 3 proyectos (el 3 SIN
 * tareas -> 200 con []), >=2 DONE, >=1 vencida (no DONE), >=1 con y >=1 sin assignee, y "API" repetida
 * en títulos (para el buscarPorTitulo case-insensitive). La checked TaskValidationException se ENVUELVE
 * en IllegalStateException: si la semilla no compila el dominio, es un bug de arranque, no algo a manejar.
 */
@Component
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;

    public DataSeeder(UserRepository userRepository, ProjectRepository projectRepository,
                      TaskRepository taskRepository) {
        this.userRepository = userRepository;
        this.projectRepository = projectRepository;
        this.taskRepository = taskRepository;
    }

    @Override
    public void run(String... args) {
        if (taskRepository.count() > 0) {
            return;   // ya hay datos (arranque posterior con ddl-auto=update): no re-sembrar
        }
        try {
            sembrarUsuarios();
            sembrarProyectos();
            sembrarTareas();
        } catch (TaskValidationException e) {
            // La semilla es válida por construcción: si falla, es un error de programación, no de negocio.
            throw new IllegalStateException("La semilla inicial no pudo construirse: " + e.getMessage(), e);
        }
    }

    /**
     * Estado D4: ana (id 1) y luis (id 2), SIN passwordHash.
     *
     * TODO MP-3: hazlos los 3 usuarios CANÓNICOS del día, CON passwordHash (usa el PasswordEncoder
     * INYECTADO — decisión canónica; hashes siempre válidos y el código documenta los passwords):
     *   - inyecta PasswordEncoder en el constructor de este DataSeeder;
     *   - ana   / ana123   / USER  (id 1)  <- OJO: cambia de ADMIN a USER (owner del proyecto 1)
     *   - luis  / luis123  / USER  (id 2)
     *   - admin / admin123 / ADMIN (id 3)  <- NUEVO
     *   con new User(null, username, passwordEncoder.encode("<pass>"), email, role).
     * (No se duplican usuarios: son los mismos ana/luis, ahora con hash, más admin.)
     * RECUERDA: al añadir password_hash a la entidad, borra data/*.mv.db y deja que resiembre.
     */
    private void sembrarUsuarios() {
        userRepository.save(new User(null, "ana", "ana@taskflow.dev", Role.ADMIN));
        userRepository.save(new User(null, "luis", "luis@taskflow.dev", Role.USER));
    }

    /** 3 proyectos (ids 1..3). El proyecto 1 pertenece a ana; el 3 se queda SIN tareas. */
    private void sembrarProyectos() {
        projectRepository.save(new Project(null, "Plataforma TaskFlow",
                "El backend REST del capstone", 1L, LocalDate.now().minusDays(30)));    // -> id 1, owner ana
        projectRepository.save(new Project(null, "App Móvil",
                "Cliente móvil que consume la API", 2L, LocalDate.now().minusDays(20))); // -> id 2, owner luis
        projectRepository.save(new Project(null, "Migración Legacy",
                "Aún sin tareas cargadas", 1L, LocalDate.now().minusDays(5)));           // -> id 3, sin tareas
    }

    /**
     * 9 tareas por el CONSTRUCTOR de rehidratación (id = null -> la BD lo asigna). El orden de
     * inserción fija los ids 1..9. Reparto: proyecto 1 = tareas 1..5; proyecto 2 = tareas 6..9;
     * proyecto 3 = 0. Fechas RELATIVAS a hoy. La tarea 7 es la única VENCIDA; la 6 es la única sin
     * responsable elegible para el 422; DONE = tareas 2 y 8.
     */
    private void sembrarTareas() throws TaskValidationException {
        // --- Proyecto 1: Plataforma TaskFlow (tareas 1..5) ---
        taskRepository.save(new Task(null, "Diseñar esquema de BD", "Tablas projects/tasks/users",
                TaskStatus.TODO, Priority.HIGH, 1L, 1L, LocalDate.now().plusDays(5)));           // id 1
        taskRepository.save(new Task(null, "Configurar Spring Boot", "Initializr + starter-web",
                TaskStatus.DONE, Priority.MED, 1L, 1L, LocalDate.now().minusDays(2)));           // id 2 (DONE)
        taskRepository.save(new Task(null, "Endpoint de login", "POST /auth/login con JWT",
                TaskStatus.IN_PROGRESS, Priority.HIGH, 1L, 2L, LocalDate.now().plusDays(3)));    // id 3
        taskRepository.save(new Task(null, "Escribir tests MockMvc", "Capa web con @SpringBootTest",
                TaskStatus.TODO, Priority.MED, 1L, null, LocalDate.now().plusDays(7)));          // id 4 (SIN assignee)
        taskRepository.save(new Task(null, "Optimizar consultas de la API", "Índices y paginación",
                TaskStatus.TODO, Priority.LOW, 1L, 1L, null));                                   // id 5

        // --- Proyecto 2: App Móvil (tareas 6..9) ---
        taskRepository.save(new Task(null, "Publicar en la tienda", "Alta en App Store / Play",
                TaskStatus.TODO, Priority.HIGH, 2L, null, LocalDate.now().plusDays(10)));        // id 6 (SIN assignee -> 422)
        taskRepository.save(new Task(null, "Corregir bug de fechas", "Zona horaria en el cliente",
                TaskStatus.IN_PROGRESS, Priority.LOW, 2L, 2L, LocalDate.now().minusDays(1)));    // id 7 (VENCIDA)
        taskRepository.save(new Task(null, "Integrar pasarela de pago", "Checkout con Stripe",
                TaskStatus.DONE, Priority.HIGH, 2L, 1L, LocalDate.now().minusDays(5)));          // id 8 (DONE)
        taskRepository.save(new Task(null, "Documentar la API con Swagger", "OpenAPI 3",
                TaskStatus.IN_PROGRESS, Priority.MED, 2L, 2L, null));                            // id 9
    }
}
