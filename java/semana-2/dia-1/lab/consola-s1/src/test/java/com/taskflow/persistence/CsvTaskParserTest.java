package com.taskflow.persistence;

import com.taskflow.exception.TaskValidationException;
import com.taskflow.model.Priority;
import com.taskflow.model.Task;
import com.taskflow.model.TaskStatus;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * CsvTaskParserTest — tests del parser CSV (SOLUCIÓN, MP-8).
 *
 * Incluye el ciclo ROJO->VERDE del día: el test de "línea corrupta" exige una excepción CLARA
 * (IllegalArgumentException con la línea en el mensaje). Antes de blindar el parser, una línea de
 * 7 campos reventaba con ArrayIndexOutOfBounds/DateTimeParseException; el test obliga a arreglarlo.
 */
class CsvTaskParserTest {

    private final CsvTaskParser parser = new CsvTaskParser();

    @Test
    void parse_lineaValida_devuelveTaskConLos8Campos() {
        String linea = "3,Implementar API,Endpoints CRUD,IN_PROGRESS,HIGH,1,7,2026-07-20";

        Task t = parser.parse(linea);

        assertEquals(3L, t.getId());
        assertEquals("Implementar API", t.getTitle());
        assertEquals("Endpoints CRUD", t.getDescription());
        assertEquals(TaskStatus.IN_PROGRESS, t.getStatus());
        assertEquals(Priority.HIGH, t.getPriority());
        assertEquals(1L, t.getProjectId());
        assertEquals(7L, t.getAssigneeId());
        assertEquals(LocalDate.of(2026, 7, 20), t.getDueDate());
    }

    @Test
    void parse_camposOpcionalesVacios_danNull() {
        // assigneeId y dueDate vacíos al final -> null. split(",", -1) NO se come los vacíos del final.
        String linea = "5,Escribir tests,,TODO,LOW,1,,";

        Task t = parser.parse(linea);

        assertEquals("", t.getDescription());   // description vacía = cadena vacía
        assertNull(t.getAssigneeId());          // assignee vacío = null
        assertNull(t.getDueDate());             // fecha vacía = null (sin fecha)
    }

    @Test
    void roundTrip_parseFormat_conservaLosCampos() throws TaskValidationException {
        // format(task) -> parse(...) debe reconstruir una tarea equivalente campo por campo.
        Task original = new Task(9L, "Preparar demo", "Guion y datos", TaskStatus.TODO,
                Priority.MED, 1L, 2L, LocalDate.of(2026, 7, 7));

        Task reconstruida = parser.parse(parser.format(original));

        assertEquals(original.getId(), reconstruida.getId());
        assertEquals(original.getTitle(), reconstruida.getTitle());
        assertEquals(original.getDescription(), reconstruida.getDescription());
        assertEquals(original.getStatus(), reconstruida.getStatus());
        assertEquals(original.getPriority(), reconstruida.getPriority());
        assertEquals(original.getProjectId(), reconstruida.getProjectId());
        assertEquals(original.getAssigneeId(), reconstruida.getAssigneeId());
        assertEquals(original.getDueDate(), reconstruida.getDueDate());
    }

    @Test
    void parse_lineaConMenosCampos_lanzaIllegalArgumentConLaLinea() {
        // ROJO->VERDE: 7 campos (falta el dueDate) -> excepción clara que NOMBRA la línea culpable.
        String corrupta = "3,Implementar API,Endpoints CRUD,IN_PROGRESS,HIGH,1,7";

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> parser.parse(corrupta));
        assertTrue(ex.getMessage().contains(corrupta),
                "El mensaje debe incluir la línea corrupta para poder diagnosticarla");
    }

    @Test
    void parse_statusInventado_lanzaIllegalArgument() {
        // "FINISHED" no es un TaskStatus válido -> valueOf lanza IllegalArgumentException; el parser
        // la re-empaqueta con la línea. Usamos la excepción ESPECÍFICA, no un Exception genérico.
        String corrupta = "3,Título,Desc,FINISHED,HIGH,1,7,2026-07-20";

        assertThrows(IllegalArgumentException.class, () -> parser.parse(corrupta));
    }

    @Test
    void parse_fechaMalformada_lanzaIllegalArgument() {
        // "20-07-2026" no es ISO (yyyy-MM-dd) -> DateTimeParseException, re-empaquetada como IAE.
        String corrupta = "3,Título,Desc,TODO,HIGH,1,7,20-07-2026";

        assertThrows(IllegalArgumentException.class, () -> parser.parse(corrupta));
    }
}
