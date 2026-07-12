package com.taskflow.repository;

import com.taskflow.exception.TaskValidationException;
import com.taskflow.model.Priority;
import com.taskflow.model.Task;
import com.taskflow.model.TaskStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * InMemoryTaskRepositoryTest — tests del repositorio de D3/D4 (SOLUCIÓN, MP-7).
 *
 * Anatomía AAA (arrange-act-assert) y un comportamiento por test. INDEPENDENCIA: @BeforeEach crea
 * un repo NUEVO por test, así ninguno depende del estado que dejó otro (eco de la lección de MP-4:
 * el estado compartido muerde).
 */
class InMemoryTaskRepositoryTest {

    private InMemoryTaskRepository repo;

    @BeforeEach
    void nuevoRepo() {
        repo = new InMemoryTaskRepository();   // repo limpio por cada test
    }

    /** Helper: una tarea válida SIN id (para que la asigne el repo). */
    private Task tareaSinId(String titulo) throws TaskValidationException {
        return new Task(null, titulo, "desc", TaskStatus.TODO, Priority.MED, 1L, 3L, null);
    }

    @Test
    void save_sinId_asignaIdIncrementalYCreceFindAll() throws TaskValidationException {
        Task a = repo.save(tareaSinId("Primera"));
        Task b = repo.save(tareaSinId("Segunda"));

        assertEquals(1L, a.getId());          // primer id = 1
        assertEquals(2L, b.getId());          // segundo id = 2 (incremental)
        assertEquals(2, repo.findAll().size()); // findAll creció a 2
    }

    @Test
    void save_conIdExplicito_avanzaLaSecuencia() throws TaskValidationException {
        // Guardar con id explícito 6 (como al cargar el CSV) debe avanzar la secuencia:
        Task conId = new Task(6L, "Cargada del CSV", "desc", TaskStatus.TODO, Priority.LOW, 1L, 3L, null);
        repo.save(conId);

        // El siguiente save SIN id debe dar 7 (no 1): así los ids nuevos no chocan con los cargados.
        Task siguiente = repo.save(tareaSinId("Nueva tras carga"));
        assertEquals(7L, siguiente.getId());
    }

    @Test
    void findById_existente_devuelveOptionalConTitulo() throws TaskValidationException {
        Task guardada = repo.save(tareaSinId("Buscable"));

        // El Optional está presente y trae la tarea correcta.
        // (orElseThrow, no .get(): respetamos la regla dura del curso incluso en los tests.)
        assertTrue(repo.findById(guardada.getId()).isPresent());
        assertEquals("Buscable", repo.findById(guardada.getId()).orElseThrow().getTitle());
    }

    @Test
    void findById_inexistente_devuelveOptionalVacio() {
        // Sin tareas guardadas, cualquier id da Optional.empty() — nunca null (contrato de D4).
        assertTrue(repo.findById(999L).isEmpty());
    }

    @Test
    void deleteById_existente_eliminaYDevuelveTrue() throws TaskValidationException {
        Task guardada = repo.save(tareaSinId("A borrar"));

        assertTrue(repo.deleteById(guardada.getId()));   // existía -> true
        assertTrue(repo.findById(guardada.getId()).isEmpty()); // ya no está
        assertEquals(0, repo.findAll().size());
    }

    @Test
    void deleteById_inexistente_devuelveFalse() {
        assertFalse(repo.deleteById(123L));   // no había nada con ese id -> false
    }
}
