package com.taskflow.service;

import com.taskflow.dto.TaskRequest;
import com.taskflow.exception.TaskNotFoundException;
import com.taskflow.exception.TaskStateException;
import com.taskflow.exception.TaskValidationException;
import com.taskflow.model.Priority;
import com.taskflow.model.Task;
import com.taskflow.model.TaskStatus;
import com.taskflow.repository.TaskRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * TaskServiceTest — PARCHE de S2D4. En S2D1 este test instanciaba a mano
 * {@code new TaskService(new InMemoryTaskRepository())}: un unit puro, sin Spring. Hoy el swap de MP-6
 * ELIMINÓ InMemoryTaskRepository, así que perdió su fake y dejó de compilar.
 *
 * El parche: {@code @SpringBootTest + @ActiveProfiles("test")} inyectando el TaskService REAL contra
 * la H2 de test. Es un test "de servicio" que arranca TODO Spring — el inflamiento que S3D1 corrige
 * reconstruyéndolo como unit puro con Mockito (mockeando TaskRepository, sin BD).
 * // parche de S2D4: el unit test perdió su fake InMemory — S3D1 lo redime.
 *
 * TODO MP-1: HOY es S3D1 y este parche SE REDIME. El unit puro ya vive en unit/TaskServiceTest (esqueleto
 *   Mockito). Cuando ese esté completo, BORRA esta clase entera (integrador, paso 1: "borrar el parche
 *   service/TaskServiceTest si sobrevive"). Objetivo: cero @SpringBootTest fuera de integration/.
 *
 * @Transactional: rollback por test, así lo que se siembra aquí no contamina; y el DataSeeder ya
 * dejó 9 tareas en la BD, por eso los asserts miran ESTA tarea (por id), no el tamaño total.
 */
@SpringBootTest
@ActiveProfiles("test")
@Transactional
class TaskServiceTest {

    private static final Long PROYECTO = 1L;   // proyecto sembrado (existe la FK)

    @Autowired
    private TaskService service;

    @Autowired
    private TaskRepository repository;

    @Test
    void crear_datosValidos_asignaIdYApareceEnListar() throws TaskValidationException {
        Task creada = service.crear(
                new TaskRequest("Tarea válida", "desc", Priority.MED, null, LocalDate.now().plusDays(2)),
                PROYECTO);

        assertNotNull(creada.getId());                               // la BD le asignó id (IDENTITY)
        assertTrue(service.buscarPorId(creada.getId()).isPresent()); // y se puede releer
    }

    @Test
    void crear_tituloDe2Chars_lanzaTaskValidationException() {
        // La regla título 3-120 vive en Task (la aplica la factory Task.crear vía el mapper).
        assertThrows(TaskValidationException.class,
                () -> service.crear(new TaskRequest("ab", "desc", Priority.LOW, null, null), PROYECTO));
    }

    @Test
    void crear_dueDatePasado_lanzaTaskValidationException() {
        // La regla "dueDate no en el pasado AL CREAR" vive SOLO en la factory Task.crear (Factory).
        assertThrows(TaskValidationException.class,
                () -> service.crear(
                        new TaskRequest("Con fecha pasada", "desc", Priority.HIGH, null, LocalDate.now().minusDays(1)),
                        PROYECTO));
    }

    @Test
    void completar_conAssignee_quedaDone() throws TaskValidationException {
        // Sembramos con el CONSTRUCTOR de rehidratación y assigneeId puesto: SÍ puede pasar a DONE.
        Task conAssignee = repository.save(
                new Task(null, "Con responsable", "desc", TaskStatus.TODO, Priority.MED, PROYECTO, 1L, null));

        Task completada = service.completar(conAssignee.getId());

        assertEquals(TaskStatus.DONE, completada.getStatus());
    }

    @Test
    void completar_sinAssignee_lanzaTaskStateException() throws TaskValidationException {
        // Sin responsable, setStatus(DONE) lanza la checked; cambiarStatus la TRADUCE a la unchecked
        // TaskStateException (que el advice mapea a 422). Ajuste de MP-9 (D3).
        Task sinAssignee = repository.save(
                new Task(null, "Sin responsable", "desc", TaskStatus.TODO, Priority.MED, PROYECTO, null, null));

        assertThrows(TaskStateException.class,
                () -> service.completar(sinAssignee.getId()));
    }

    @Test
    void completar_idInexistente_lanzaTaskNotFoundException() {
        // findById(...).orElseThrow(...) convierte el "no existe" en un fallo explícito (unchecked).
        assertThrows(TaskNotFoundException.class,
                () -> service.completar(999_999L));
    }

    /**
     * STRETCH — la estrategia POR_URGENCIA desde el service: una tarea VENCIDA sembrada directo al
     * repo (constructor de rehidratación, fecha muy pasada, HIGH) queda de PRIMERA en listar(), por
     * encima incluso de la vencida de la semilla.
     */
    @Test
    void listar_conVencidaSembradaDirecto_vaPrimero() throws TaskValidationException {
        service.crear(new TaskRequest("Urgente futura", "desc", Priority.HIGH, null, LocalDate.now().plusDays(5)),
                PROYECTO);
        repository.save(new Task(null, "Vencida", "desc", TaskStatus.IN_PROGRESS, Priority.HIGH, PROYECTO, 1L,
                LocalDate.now().minusDays(30)));

        List<Task> orden = service.listar();

        assertEquals("Vencida", orden.get(0).getTitle());
    }
}
