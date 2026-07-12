package com.taskflow.unit;

import com.taskflow.exception.TaskValidationException;
import com.taskflow.model.Priority;
import com.taskflow.model.Task;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * TaskValidationTest — la regla de longitud de título 3-120 del capstone, que vive en Task.crear.
 *
 * PUNTO DE PARTIDA: los CUATRO tests de S1D5 (el primer viernes), copiados VERBATIM y VERDES. Casi
 * idénticos entre sí: ese "casi idéntico" es el olor que MP-4 corrige con @ParameterizedTest.
 *
 * TODO MP-4: colapsar los 4 de abajo en UN @ParameterizedTest de 6 casos con las fronteras EXACTAS
 *   (0,2,3,60,120,121). Ver el esqueleto comentado al final. Regla anti-#3: construir el título con
 *   "a".repeat(longitud) (NO hardcodeado) y ver fallar un caso imposible una vez antes de confiar.
 */
class TaskValidationTest {

    @Test
    void titulo_null_lanzaTaskValidationException() {
        assertThrows(TaskValidationException.class,
                () -> Task.crear(null, "desc", Priority.MED, null, 1L, null));
    }

    @Test
    void titulo_blank_lanzaTaskValidationException() {
        assertThrows(TaskValidationException.class,
                () -> Task.crear("   ", "desc", Priority.MED, null, 1L, null));
    }

    @Test
    void titulo_dosCaracteres_lanzaTaskValidationException() {
        assertThrows(TaskValidationException.class,
                () -> Task.crear("ab", "desc", Priority.MED, null, 1L, null));
    }

    @Test
    void titulo_tresCaracteres_noLanza() {
        assertDoesNotThrow(() -> Task.crear("abc", "desc", Priority.MED, null, 1L, null));
    }

    // ==================== TODO MP-4: el refactor a @ParameterizedTest ====================
    //
    // (1) Añade los imports:
    //     import org.junit.jupiter.params.ParameterizedTest;
    //     import org.junit.jupiter.params.provider.CsvSource;
    //
    // (2) Reemplaza los 4 tests de arriba (menos el de null, que queda como test simple aparte:
    //     un null en un CSV es más ruido que valor) por:
    //
    //     @ParameterizedTest(name = "título de longitud {0} → válido: {1}")
    //     @CsvSource({ "0,false", "2,false", "3,true", "60,true", "120,true", "121,false" })
    //     void titulo_segunLongitud_respetaFrontera3a120(int longitud, boolean esValido) {
    //         String titulo = "a".repeat(longitud);          // <- USA el parámetro (anti-#3)
    //         if (esValido) {
    //             assertDoesNotThrow(() -> Task.crear(titulo, "desc", Priority.MED, null, 1L, null));
    //         } else {
    //             assertThrows(TaskValidationException.class,
    //                     () -> Task.crear(titulo, "desc", Priority.MED, null, 1L, null));
    //         }
    //     }
    //
    // (3) Demo corta de @MethodSource: un Stream<Arguments> de casos inválidos COMPUESTOS
    //     (título fuera de rango o projectId nulo).
    //
    // (4) Anti-#3: cambia UN caso a un valor imposible (p.ej. "3,false") y velo FALLAR una vez;
    //     luego regrésalo. Un parametrizado que nunca se vio fallar no prueba nada.
}
