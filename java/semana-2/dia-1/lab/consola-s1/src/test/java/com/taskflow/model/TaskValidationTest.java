package com.taskflow.model;

import com.taskflow.exception.TaskValidationException;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * TaskValidationTest — tests de las VALIDACIONES de Task (D2), con JUnit 5 (SOLUCIÓN, MP-5/MP-6).
 *
 * Naming: metodo_escenario_resultado (ej. crearTask_tituloDe2Chars_lanzaExcepcion).
 * Estas pruebas viven del lado de src/test/java, espejo de src/main/java.
 *
 * Clave del día: separan CREACIÓN (Task.crear, con la regla de fecha) de REHIDRATACIÓN
 * (constructor, sin ella) — la decisión canónica de D2 MP-8. Y usan la excepción ESPECÍFICA
 * (TaskValidationException), nunca un Exception genérico que taparía un bug (error #3 de MP-6).
 */
class TaskValidationTest {

    // Datos válidos de apoyo para no repetir literales en cada test.
    private static final Long PROYECTO = 1L;
    private static final Long ASSIGNEE = 7L;

    @Test
    void crearTask_tituloNull_lanzaExcepcion() {
        assertThrows(TaskValidationException.class, () ->
                Task.crear(null, "desc", Priority.MED, null, PROYECTO, ASSIGNEE));
    }

    @Test
    void crearTask_tituloEnBlanco_lanzaExcepcion() {
        assertThrows(TaskValidationException.class, () ->
                Task.crear("   ", "desc", Priority.MED, null, PROYECTO, ASSIGNEE));
    }

    @Test
    void crearTask_tituloDe2Chars_lanzaExcepcion() {
        // Frontera inferior: 2 chars < mínimo (3) -> debe lanzar.
        assertThrows(TaskValidationException.class, () ->
                Task.crear("ab", "desc", Priority.MED, null, PROYECTO, ASSIGNEE));
    }

    @Test
    void crearTask_tituloDe3Chars_noLanza() throws TaskValidationException {
        // Frontera EXACTA: 3 chars == mínimo -> NO debe lanzar. El caso borde es donde viven los bugs.
        Task t = Task.crear("abc", "desc", Priority.MED, null, PROYECTO, ASSIGNEE);
        assertEquals("abc", t.getTitle());     // expected, actual: se lee "esperaba abc, obtuve ..."
    }

    @Test
    void crearTask_sinProyecto_lanzaExcepcion() {
        // Regla del capstone: no hay Task sin Project (projectId == null).
        assertThrows(TaskValidationException.class, () ->
                Task.crear("Título válido", "desc", Priority.MED, null, null, ASSIGNEE));
    }

    @Test
    void crearTask_dueDateEnPasado_lanzaExcepcion() {
        // La regla temporal vive SOLO en la factory crear(...).
        LocalDate ayer = LocalDate.now().minusDays(1);
        assertThrows(TaskValidationException.class, () ->
                Task.crear("Tarea nueva", "desc", Priority.HIGH, ayer, PROYECTO, ASSIGNEE));
    }

    @Test
    void rehidratar_dueDateEnPasado_noLanza() throws TaskValidationException {
        // MISMA fecha en el pasado, pero por el CONSTRUCTOR (rehidratación): es dato legal (tarea
        // vencida). Este par de tests documenta la decisión creación-vs-rehidratación de D2 MP-8.
        LocalDate ayer = LocalDate.now().minusDays(1);
        Task t = new Task(10L, "Tarea vencida", "desc", TaskStatus.IN_PROGRESS,
                Priority.HIGH, PROYECTO, ASSIGNEE, ayer);
        // Se construyó sin excepción y, además, estaVencida() tiene sentido:
        assertEquals(true, t.estaVencida());
    }

    @Test
    void setStatusDone_sinAssignee_lanzaExcepcion() throws TaskValidationException {
        // No se puede pasar a DONE una tarea sin responsable: la regla vive en setStatus.
        Task sinResponsable = new Task(11L, "Sin responsable", "desc", TaskStatus.TODO,
                Priority.LOW, PROYECTO, null, null);
        assertThrows(TaskValidationException.class, () -> sinResponsable.setStatus(TaskStatus.DONE));
    }
}
