package com.taskflow.persistence;

import com.taskflow.model.Task;

/**
 * CsvTaskParser — traduce entre una línea de texto CSV y una {@link Task} (ESQUELETO).
 *
 * Es la clase REAL del integrador (FileTaskRepository la usa para leer/escribir data/tasks.csv).
 * Formato canónico del día (8 columnas, en este orden):
 *
 *     id,title,description,status,priority,projectId,assigneeId,dueDate
 *
 * El esqueleto COMPILA (parse devuelve null, format devuelve ""). Tu trabajo: llenar los TODO.
 * Estas firmas son las que testeará CsvTaskParserTest (MP-8).
 */
public class CsvTaskParser {

    /** Encabezado canónico: la primera línea del archivo. Se salta al cargar (skip(1)). */
    public static final String ENCABEZADO =
            "id,title,description,status,priority,projectId,assigneeId,dueDate";

    /** El formato tiene exactamente 8 columnas. */
    private static final int NUM_COLUMNAS = 8;

    /**
     * TODO MP-2 — parse: una línea CSV -> Task.
     *
     * Pasos:
     *   1. String[] campos = linea.split(",", -1);   // el -1 CONSERVA los vacíos del final
     *      (sin -1, "a,b,," daría length 2 y el dueDate vacío del final desaparecería).
     *   2. Si campos.length != NUM_COLUMNAS -> lanza IllegalArgumentException con la línea
     *      (esto lo pide el test de "línea corrupta" de MP-8; hoy sin esto truena feo).
     *   3. Reconstruye por el CONSTRUCTOR DE REHIDRATACIÓN (new Task(id, ...)), NUNCA Task.crear:
     *      un dueDate en el pasado es dato LEGAL al releer (tarea vencida). campos vacíos:
     *      assigneeId/dueDate vacíos -> null (usa campo.isBlank()). Fechas: LocalDate.parse.
     *   4. El constructor declara TaskValidationException (checked): atrápala y re-lánzala como
     *      IllegalArgumentException con la línea, junto con NumberFormatException / DateTimeParseException.
     */
    public Task parse(String linea) {
        // TODO MP-2
        return null;
    }

    /**
     * TODO MP-3 — format: Task -> una línea CSV con las 8 columnas en orden.
     *
     * Pista: String.join(",", ...) con cada campo. null (description/assigneeId/dueDate) -> "".
     * Enums a su name() crudo (TODO/IN_PROGRESS/DONE, LOW/MED/HIGH), NO la etiqueta legible.
     * Debe ser INVERSO de parse: parse(format(task)) reconstruye la misma tarea (round-trip de MP-8).
     */
    public String format(Task t) {
        // TODO MP-3
        return "";
    }
}
