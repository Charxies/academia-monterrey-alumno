package com.taskflow.service;

import com.taskflow.exception.TaskNotFoundException;
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
 * TaskServiceTest — testing de la capa de HOY, SIN mocks y SIN arrancar Spring.
 *
 * @BeforeEach construye TaskService con 'new' sobre un InMemoryTaskRepository REAL, instancia NUEVA
 * por test (independencia de S1D5): esto es posible JUSTO PORQUE la inyección es por constructor
 * (contraste directo con el Singleton de MP-4, donde 'new' estaba prohibido y los tests se
 * interferían). Un repo en memoria rápido y determinista NO se mockea; los mocks llegan cuando haya
 * capas que aislar (HTTP con MockMvc en S2D2, Mockito a fondo en S3D1).
 *
 * Naming: metodo_escenario_resultado (S1D5). AAA: arrange-act-assert.
 */
class TaskServiceTest {

    private InMemoryTaskRepository repository;
    private TaskService service;

    @BeforeEach
    void setUp() {
        repository = new InMemoryTaskRepository();   // repo REAL, limpio por test
        service = new TaskService(repository);       // 'new' posible = argumento #1 de la DI por constructor
    }

    @Test
    void crear_datosValidos_asignaIdYApareceEnListar() throws TaskValidationException {
        Task creada = service.crear("Tarea válida", "desc", Priority.MED, LocalDate.now().plusDays(2));

        assertNotNull(creada.getId());               // el repo le asignó id
        assertEquals(1, service.listar().size());    // y aparece al listar
    }

    @Test
    void crear_tituloDe2Chars_lanzaTaskValidationException() {
        // La regla título 3-120 vive en Task (la aplica la factory Task.crear): el service no re-valida.
        assertThrows(TaskValidationException.class,
                () -> service.crear("ab", "desc", Priority.LOW, null));
    }

    @Test
    void crear_dueDatePasado_lanzaTaskValidationException() {
        // La regla "dueDate no en el pasado AL CREAR" vive SOLO en la factory Task.crear (Factory).
        assertThrows(TaskValidationException.class,
                () -> service.crear("Con fecha pasada", "desc", Priority.HIGH, LocalDate.now().minusDays(1)));
    }

    @Test
    void completar_conAssignee_quedaDone() throws TaskValidationException {
        // Sembramos con el CONSTRUCTOR de rehidratación y assigneeId puesto (creación vs rehidratación):
        // una tarea con responsable SÍ puede pasar a DONE.
        Task conAssignee = repository.save(
                new Task(null, "Con responsable", "desc", TaskStatus.TODO, Priority.MED, 1L, 7L, null));

        Task completada = service.completar(conAssignee.getId());

        assertEquals(TaskStatus.DONE, completada.getStatus());
    }

    @Test
    void completar_sinAssignee_lanzaTaskValidationException() throws TaskValidationException {
        // Sin responsable, setStatus(DONE) aplica la regla del capstone y lanza la checked.
        Task sinAssignee = repository.save(
                new Task(null, "Sin responsable", "desc", TaskStatus.TODO, Priority.MED, 1L, null, null));

        assertThrows(TaskValidationException.class,
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
        service.crear("Urgente futura", "desc", Priority.HIGH, LocalDate.now().plusDays(5));
        repository.save(new Task(null, "Vencida", "desc", TaskStatus.IN_PROGRESS, Priority.LOW, 1L, 9L,
                LocalDate.now().minusDays(3)));

        List<Task> orden = service.listar();

        assertEquals("Vencida", orden.get(0).getTitle());
    }
}
