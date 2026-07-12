package com.taskflow.practicas;

/**
 * MP-5 — Pipeline completo: filter -> sorted -> map -> collect.
 *
 * Qué construir (usa List<Task> tareas = SeedData.tareas();):
 *   1. Títulos de las tareas PENDIENTES ordenadas por dueDate, como List<String>:
 *        tareas.stream()
 *              .filter(t -> t.getStatus() != TaskStatus.DONE)
 *              .sorted(Comparator.comparing(Task::getDueDate,
 *                      Comparator.nullsLast(Comparator.naturalOrder())))
 *              .map(Task::getTitle)
 *              .collect(Collectors.toList());
 *      (map cambia el TIPO del pipeline: Stream<Task> -> Stream<String>.)
 *   2. ANTI-PATRÓN (error intencional #3) — DÉJALO COMENTADO y explica por qué NO va:
 *        - int contador = 0; ... forEach(t -> contador++);  // NO compila: effectively final.
 *        - listaExterna.add(t) dentro de map(...);           // compila pero es efecto colateral.
 *      Regla: el stream PRODUCE el resultado con una terminal (collect/count), no lo empuja afuera.
 *
 * Correr: mvn -q compile exec:java "-Dexec.mainClass=com.taskflow.practicas.MP05MapSorted"
 */
public class MP05MapSorted {

    public static void main(String[] args) {
        // TODO 1: pipeline filter -> sorted(nullsLast) -> map(Task::getTitle) -> collect.
        // TODO 2: deja comentado el anti-patrón (contador++ / add externo) con su explicación.
        System.out.println("MP-5: pendiente. Pipeline filter -> sorted -> map -> collect.");
    }
}
