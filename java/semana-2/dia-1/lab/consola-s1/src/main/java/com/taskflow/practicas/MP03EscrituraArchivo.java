package com.taskflow.practicas;

import com.taskflow.model.Task;
import com.taskflow.persistence.CsvTaskParser;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * MP-3 — Escribir List&lt;Task&gt; a un CSV y hacer round-trip (SOLUCIÓN).
 *
 * Flujo: lee la semilla (data/tasks.csv) -> la escribe a un archivo TEMPORAL con try-with-resources
 * -> la vuelve a leer -> compara tamaño y una tarea campo por campo. Si el round-trip conserva
 * todo, parse y format son inversos (lo que probará CsvTaskParserTest en MP-8).
 *
 * OJO: escribimos a un archivo TEMPORAL, nunca al data/tasks.csv real — no queremos corromper la
 * semilla del integrador (punto de dolor #10). try-with-resources garantiza el close()+flush: sin
 * él, el buffer podría quedar sin volcar y el archivo saldría vacío (error intencional #2 del día).
 *
 * Correr: mvn -q compile exec:java "-Dexec.mainClass=com.taskflow.practicas.MP03EscrituraArchivo"
 */
public class MP03EscrituraArchivo {

    public static void main(String[] args) throws IOException {
        CsvTaskParser parser = new CsvTaskParser();
        Path origen = Path.of("data", "tasks.csv");

        if (!Files.exists(origen)) {
            System.out.println("No encuentro " + origen.toAbsolutePath()
                    + ". Corre desde la raíz del proyecto (ver MP-1).");
            return;
        }

        // 1) Cargar la semilla a List<Task> con streams (los de D4): saltar encabezado + parsear.
        List<Task> tareas = Files.readAllLines(origen).stream()
                .skip(1)
                .filter(l -> !l.isBlank())
                .map(parser::parse)
                .toList();
        System.out.println("Cargadas " + tareas.size() + " tareas desde " + origen + ".");

        // 2) Escribir a un archivo TEMPORAL (no tocar el data/ real): encabezado + una línea por tarea.
        Path destino = Files.createTempFile("taskflow-roundtrip", ".csv");
        try (BufferedWriter writer = Files.newBufferedWriter(destino)) {
            writer.write(CsvTaskParser.ENCABEZADO);
            writer.newLine();
            for (Task t : tareas) {
                writer.write(parser.format(t));
                writer.newLine();
            }
        } // <- close()+flush aquí, siempre
        System.out.println("Snapshot escrito en: " + destino);

        // 3) Round-trip: releer el archivo escrito y comparar.
        List<Task> releidas = Files.readAllLines(destino).stream()
                .skip(1)
                .filter(l -> !l.isBlank())
                .map(parser::parse)
                .toList();

        System.out.println("\n--- Round-trip ---");
        System.out.println("Tamaño original vs releído: " + tareas.size() + " == " + releidas.size()
                + " -> " + (tareas.size() == releidas.size() ? "OK" : "DIFIERE"));

        // Comparar la primera tarea campo por campo (id, título, estado, prioridad, fecha).
        Task a = tareas.get(0);
        Task b = releidas.get(0);
        boolean iguales = a.getId().equals(b.getId())
                && a.getTitle().equals(b.getTitle())
                && a.getStatus() == b.getStatus()
                && a.getPriority() == b.getPriority()
                && java.util.Objects.equals(a.getDueDate(), b.getDueDate());
        System.out.println("Primera tarea idéntica campo por campo: " + (iguales ? "OK" : "DIFIERE"));
        System.out.println("   original: " + a);
        System.out.println("   releída : " + b);

        Files.deleteIfExists(destino);   // limpiamos el temporal
    }
}
