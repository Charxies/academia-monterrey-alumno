package com.taskflow.practicas;

/**
 * MP-7 — Orden natural con Comparable&lt;T&gt;.
 *
 * Qué construir:
 *   1. En model/Task.java (su TODO MP-7): haz que la clase implemente Comparable<Task> y añade
 *        @Override public int compareTo(Task otra) { return this.priority.compareTo(otra.priority); }
 *      Como Priority se declaró LOW, MED, HIGH, su compareTo ordena LOW -> MED -> HIGH.
 *   2. Aquí abajo, en main: crea una List<Task> con prioridades mezcladas y ordénala con
 *      tareas.sort(null) (null = orden natural). Observa que sale de LOW a HIGH. Un esqueleto:
 *        List<Task> tareas = new ArrayList<>();
 *        tareas.add(new Task(1L, "Desplegar API", "", TaskStatus.TODO, Priority.HIGH, 1L, 1L, null));
 *        tareas.add(new Task(2L, "Escribir tests", "", TaskStatus.TODO, Priority.LOW,  1L, 1L, null));
 *        tareas.add(new Task(3L, "Configurar repo", "", TaskStatus.TODO, Priority.MED, 1L, 1L, null));
 *        tareas.sort(null);   // requiere el TODO 1 hecho (si no, ClassCastException en runtime)
 *        for (Task t : tareas) System.out.println("  " + t.getPriority() + " - " + t.getTitle());
 *      (necesitarás los imports de Task/Priority/TaskStatus, java.util.ArrayList/List, y
 *       declarar 'throws TaskValidationException' en main.)
 *   3. Pregunta guía: ¿y si el lunes las piden por fecha y el martes por título? Una clase tiene
 *      UN SOLO orden natural -> eso motiva el Comparator de MP-8.
 *
 * Correr: mvn -q compile exec:java "-Dexec.mainClass=com.taskflow.practicas.MP07OrdenNatural"
 */
public class MP07OrdenNatural {

    public static void main(String[] args) {
        // TODO 1: en model/Task.java, implementa Comparable<Task> + compareTo por prioridad.
        // TODO 2: aquí, crea una List<Task> con prioridades mezcladas, ordénala con sort(null) e imprime.
        System.out.println("MP-7: pendiente. Implementa Comparable en Task y ordena con sort(null).");
    }
}
