package com.taskflow.practicas;

/**
 * MP-3 — Escribir List&lt;Task&gt; a un CSV y hacer round-trip (ESQUELETO). Usa CsvTaskParser.
 *
 * Qué construir:
 *   1. Carga la semilla (data/tasks.csv) a List<Task> con streams (skip(1) + map(parser::parse)).
 *   2. Escribe a un archivo TEMPORAL (Files.createTempFile, NO el data/ real): encabezado +
 *      una línea parser.format(t) por tarea, con try-with-resources sobre Files.newBufferedWriter
 *      (garantiza close()+flush; sin él el archivo saldría vacío -> error intencional #2).
 *   3. Round-trip: relee el archivo escrito, compara el tamaño y una tarea CAMPO POR CAMPO.
 *
 * OJO: nunca escribas al data/tasks.csv real -> corromperías la semilla del integrador (dolor #10).
 *
 * Correr: mvn -q compile exec:java "-Dexec.mainClass=com.taskflow.practicas.MP03EscrituraArchivo"
 */
public class MP03EscrituraArchivo {

    public static void main(String[] args) {
        // TODO 1: cargar data/tasks.csv a List<Task> (streams + parser.parse)
        // TODO 2: escribir a un archivo temporal (try-with-resources + parser.format)
        // TODO 3: releer y comparar tamaño + una tarea campo por campo
        System.out.println("MP-3: pendiente. Escribir CSV + round-trip con CsvTaskParser.");
    }
}
