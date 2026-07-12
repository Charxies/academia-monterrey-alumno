package com.taskflow.model;

import org.junit.jupiter.api.Test;

/**
 * TaskValidationTest — tests de las VALIDACIONES de Task (ESQUELETO, MP-5/MP-6).
 *
 * OJO (MP-6, "el test verde mentiroso"): estos cuerpos están VACÍOS, así que 'mvn test' los da
 * VERDES aunque no prueben NADA. Un test debe poder FALLAR — míralo fallar al menos una vez.
 * Tu trabajo: llenar cada cuerpo con su assertThrows / assertEquals de verdad.
 *
 * Naming del curso: metodo_escenario_resultado. Usa la excepción ESPECÍFICA
 * (TaskValidationException.class), nunca un Exception genérico que taparía un bug.
 * assertThrows / assertEquals se importan de org.junit.jupiter.api.Assertions.
 */
class TaskValidationTest {

    @Test
    void crearTask_tituloNull_lanzaExcepcion() {
        // TODO MP-5: assertThrows(TaskValidationException.class,
        //   () -> Task.crear(null, "desc", Priority.MED, null, 1L, 7L));
    }

    @Test
    void crearTask_tituloEnBlanco_lanzaExcepcion() {
        // TODO MP-5: título "   " -> lanza.
    }

    @Test
    void crearTask_tituloDe2Chars_lanzaExcepcion() {
        // TODO MP-5: "ab" (2 chars < mínimo 3) -> lanza. Frontera inferior.
    }

    @Test
    void crearTask_tituloDe3Chars_noLanza() {
        // TODO MP-5: "abc" (== mínimo 3) -> NO lanza; assertEquals("abc", t.getTitle()).
    }

    @Test
    void crearTask_dueDateEnPasado_lanzaExcepcion() {
        // TODO MP-5: LocalDate.now().minusDays(1) en Task.crear -> lanza (regla temporal).
    }

    @Test
    void rehidratar_dueDateEnPasado_noLanza() {
        // TODO MP-5: MISMA fecha pasada por el CONSTRUCTOR new Task(...) -> NO lanza (dato legal,
        //   tarea vencida). Este par documenta la decisión creación-vs-rehidratación de D2 MP-8.
    }

    @Test
    void setStatusDone_sinAssignee_lanzaExcepcion() {
        // TODO MP-5: tarea con assigneeId null; setStatus(TaskStatus.DONE) -> lanza.
    }
}
