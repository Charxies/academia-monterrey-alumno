package com.taskflow.service;

import com.taskflow.dto.TaskRequest;
import com.taskflow.exception.TaskNotFoundException;
import com.taskflow.exception.TaskStateException;
import com.taskflow.exception.TaskValidationException;
import com.taskflow.model.Priority;
import com.taskflow.model.Task;
import com.taskflow.model.TaskStatus;
import com.taskflow.repository.InMemoryTaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * TaskServiceTest — la capa de negocio, SIN mocks y SIN arrancar Spring (repo en memoria real,
 * instancia nueva por test). Heredado de D1/D2; HOY se ajusta a dos cambios de S2D3:
 *   - crear(...) recibe ahora un TaskRequest y el projectId del path (murió el 1L demo): los tests
 *     construyen el DTO y pasan el proyecto 1.
 *   - completar(...) delega en cambiarStatus, que TRADUCE la regla de estado a TaskStateException
 *     (unchecked) -> completar_sinAssignee ahora espera ESA excepción (ajuste de MP-9).
 *
 * Naming: metodo_escenario_resultado (S1D5). AAA.
 */
class TaskServiceTest {

    private static final Long PROYECTO = 1L;

    private InMemoryTaskRepository repository;
    private TaskService service;

    @BeforeEach
    void setUp() {
        repository = new InMemoryTaskRepository();   // repo REAL, limpio por test
        service = new TaskService(repository);       // 'new' posible = la DI por constructor en acción
    }

    @Test
    void crear_datosValidos_asignaIdYApareceEnListar() throws TaskValidationException {
        Task creada = service.crear(
                new TaskRequest("Tarea válida", "desc", Priority.MED, null, LocalDate.now().plusDays(2)),
                PROYECTO);

        assertNotNull(creada.getId());               // el repo le asignó id
        assertEquals(1, service.listar().size());    // y aparece al listar
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
        // Sembramos con el CONSTRUCTOR de rehidratación y assigneeId puesto: una tarea con responsable
        // SÍ puede pasar a DONE.
        Task conAssignee = repository.save(
                new Task(null, "Con responsable", "desc", TaskStatus.TODO, Priority.MED, 1L, 7L, null));

        Task completada = service.completar(conAssignee.getId());

        assertEquals(TaskStatus.DONE, completada.getStatus());
    }

    @Test
    void completar_sinAssignee_lanzaTaskStateException() throws TaskValidationException {
        // Sin responsable, setStatus(DONE) lanza la checked; cambiarStatus la TRADUCE a la unchecked
        // TaskStateException (que el advice mapea a 422). Ajuste de MP-9.
        Task sinAssignee = repository.save(
                new Task(null, "Sin responsable", "desc", TaskStatus.TODO, Priority.MED, 1L, null, null));

        assertThrows(TaskStateException.class,
                () -> service.completar(sinAssignee.getId()));
    }

    @Test
    void completar_idInexistente_lanzaTaskNotFoundException() {
        // findById(...).orElseThrow(...) convierte el "no existe" en un fallo explícito (unchecked).
        assertThrows(TaskNotFoundException.class,
                () -> service.completar(999L));
    }

    // ==================== STRETCH ====================

    /**
     * STRETCH — verifica la estrategia POR_URGENCIA desde el service: una tarea VENCIDA sembrada
     * directo al repo (constructor de rehidratación, fecha pasada) queda de PRIMERA en listar(),
     * por encima de una HIGH no vencida.
     */
    @Test
    void listar_conVencidaSembradaDirecto_vaPrimero() throws TaskValidationException {
        service.crear(new TaskRequest("Urgente futura", "desc", Priority.HIGH, null, LocalDate.now().plusDays(5)),
                PROYECTO);
        repository.save(new Task(null, "Vencida", "desc", TaskStatus.IN_PROGRESS, Priority.LOW, 1L, 9L,
                LocalDate.now().minusDays(3)));

        List<Task> orden = service.listar();

        assertEquals("Vencida", orden.get(0).getTitle());
    }
}
