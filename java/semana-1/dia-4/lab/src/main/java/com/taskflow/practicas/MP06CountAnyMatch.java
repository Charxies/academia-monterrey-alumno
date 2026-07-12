package com.taskflow.practicas;

/**
 * MP-6 — Terminales de escaneo: count, anyMatch, noneMatch.
 *
 * Qué construir (usa List<Task> tareas = SeedData.tareas();):
 *   1. count() de DONE: filter(t -> t.getStatus() == TaskStatus.DONE).count().
 *   2. anyMatch de tarea vencida: tareas.stream().anyMatch(Task::estaVencida) -> alerta si true.
 *   3. noneMatch de título en blanco: tareas.stream().noneMatch(t -> t.getTitle().isBlank())
 *      (valida la regla de negocio: título obligatorio).
 *
 * Awareness: count es un reduce especializado; el reduce general existe, hoy no se practica.
 *
 * Correr: mvn -q compile exec:java "-Dexec.mainClass=com.taskflow.practicas.MP06CountAnyMatch"
 */
public class MP06CountAnyMatch {

    public static void main(String[] args) {
        // TODO 1: count() de DONE.
        // TODO 2: anyMatch(Task::estaVencida) -> alerta.
        // TODO 3: noneMatch de título en blanco.
        System.out.println("MP-6: pendiente. count / anyMatch / noneMatch.");
    }
}
