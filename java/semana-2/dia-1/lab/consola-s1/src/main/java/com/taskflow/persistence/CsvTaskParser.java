package com.taskflow.persistence;

import com.taskflow.exception.TaskValidationException;
import com.taskflow.model.Priority;
import com.taskflow.model.Task;
import com.taskflow.model.TaskStatus;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

/**
 * CsvTaskParser — traduce entre una línea de texto CSV y una {@link Task} (SOLUCIÓN).
 *
 * Es la clase REAL del integrador (no un ejercicio desechable): FileTaskRepository la usa para
 * leer y escribir data/tasks.csv. Formato canónico del día (8 columnas, en este orden):
 *
 *     id,title,description,status,priority,projectId,assigneeId,dueDate
 *
 * Reglas del formato:
 *   - Campos opcionales vacíos = cadena vacía en el CSV -> null en la Task (assigneeId, dueDate).
 *   - Fechas en ISO (yyyy-MM-dd), LocalDate.parse directo.
 *   - Limitación DOCUMENTADA: description NO puede contener comas (partiríamos mal la línea).
 *     Una línea con un número de campos != 8 es una línea corrupta -> IllegalArgumentException
 *     clara (con la línea culpable). Por casos así existen OpenCSV / Jackson en la vida real.
 */
public class CsvTaskParser {

    /** Encabezado canónico: la primera línea del archivo. Se salta al cargar (skip(1)). */
    public static final String ENCABEZADO =
            "id,title,description,status,priority,projectId,assigneeId,dueDate";

    /** El formato tiene exactamente 8 columnas. */
    private static final int NUM_COLUMNAS = 8;

    /**
     * parse: una línea CSV -> Task.
     *
     * CLAVE (decisión canónica de D2, MP-8): reconstruye vía el CONSTRUCTOR DE REHIDRATACIÓN
     * (new Task(id, ...)), NUNCA vía Task.crear. Un dueDate en el pasado es dato LEGAL al releer
     * (una tarea vencida), y la regla "dueDate no en el pasado" solo vive en la factory crear(...)
     * que usa el menú al agregar. Rehidratar NO revalida la fecha.
     *
     * split(",", -1): el -1 conserva los campos vacíos DEL FINAL (sin él, "a,b,," daría length 2
     * y el dueDate vacío del final desaparecería -> punto de dolor #4 del día).
     *
     * Ante cualquier corrupción (columnas != 8, id/projectId no numérico, status/priority
     * inexistente, fecha malformada, invariante de Task violada) lanza IllegalArgumentException
     * con un mensaje que INCLUYE la línea, para que el error diga QUÉ archivo/renglón falló.
     */
    public Task parse(String linea) {
        String[] campos = linea.split(",", -1);
        if (campos.length != NUM_COLUMNAS) {
            throw new IllegalArgumentException(
                    "Línea CSV inválida: esperaba " + NUM_COLUMNAS + " columnas y recibí "
                            + campos.length + " -> \"" + linea + "\"");
        }
        try {
            Long id = Long.parseLong(campos[0].trim());
            String title = campos[1];
            String description = campos[2];
            TaskStatus status = TaskStatus.valueOf(campos[3].trim());
            Priority priority = Priority.valueOf(campos[4].trim());
            Long projectId = Long.parseLong(campos[5].trim());
            Long assigneeId = campos[6].isBlank() ? null : Long.parseLong(campos[6].trim());
            LocalDate dueDate = campos[7].isBlank() ? null : LocalDate.parse(campos[7].trim());
            // REHIDRATACIÓN: dato existente, sin la regla temporal de crear(...).
            return new Task(id, title, description, status, priority, projectId, assigneeId, dueDate);
        } catch (IllegalArgumentException | DateTimeParseException | TaskValidationException e) {
            // NumberFormatException y el IAE de valueOf(...) caen aquí (IllegalArgumentException);
            // DateTimeParseException (fecha) y la checked TaskValidationException, también.
            throw new IllegalArgumentException(
                    "No se pudo parsear la línea CSV -> \"" + linea + "\": " + e.getMessage(), e);
        }
    }

    /**
     * format: Task -> una línea CSV con las 8 columnas en orden.
     *
     * Campos null (description, assigneeId, dueDate) se escriben como cadena vacía (round-trip
     * simétrico con parse). Enums a su name() crudo (TODO/IN_PROGRESS/DONE, LOW/MED/HIGH), NO su
     * etiqueta legible: el CSV guarda el valor canónico, no el texto de la interfaz.
     */
    public String format(Task t) {
        return String.join(",",
                String.valueOf(t.getId()),
                t.getTitle(),
                t.getDescription() == null ? "" : t.getDescription(),
                t.getStatus().name(),
                t.getPriority().name(),
                String.valueOf(t.getProjectId()),
                t.getAssigneeId() == null ? "" : String.valueOf(t.getAssigneeId()),
                t.getDueDate() == null ? "" : t.getDueDate().toString());
    }
}
