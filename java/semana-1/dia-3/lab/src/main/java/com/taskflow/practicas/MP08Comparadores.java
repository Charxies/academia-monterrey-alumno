package com.taskflow.practicas;

import com.taskflow.exception.TaskValidationException;

/**
 * MP-8 — Comparator: órdenes externos e intercambiables.
 *
 * Qué construir (sobre una List&lt;Task&gt; con prioridades mezcladas y alguna dueDate null):
 *   1. MUNDO ANTIGUO — clase anónima por dueDate (cópiala tal cual; D4 la refactoriza a lambda):
 *        Comparator<Task> porFecha = new Comparator<Task>() {
 *            @Override public int compare(Task a, Task b) { return a.getDueDate().compareTo(b.getDueDate()); }
 *        };
 *      (usa fechas NO nulas para esta parte). Ordena una copia y observa.
 *   2. RECETA MODERNA — el orden del integrador (prioridad HIGH->LOW, luego dueDate asc nulls last):
 *        Comparator.comparing(Task::getPriority, Comparator.reverseOrder())
 *            .thenComparing(Task::getDueDate, Comparator.nullsLast(Comparator.naturalOrder()))
 *      El reverse va POR CRITERIO (no reversed() al final); nullsLast evita el NPE con fechas null.
 *   3. Variante: ordena por título (Comparator.comparing(Task::getTitle)).
 *   4. Ordena SIEMPRE una copia (new ArrayList<>(...)) para no mutar la fuente.
 *
 * Correr: mvn -q compile exec:java "-Dexec.mainClass=com.taskflow.practicas.MP08Comparadores"
 */
public class MP08Comparadores {

    public static void main(String[] args) throws TaskValidationException {
        // TODO 1: clase anónima Comparator<Task> por dueDate (fechas no nulas); ordena una copia.
        // TODO 2: comparing(Task::getPriority, reverseOrder()).thenComparing(getDueDate, nullsLast(naturalOrder())).
        // TODO 3: variante por título.
        // TODO 4: ordena copias, no la fuente (verifica que la original no cambió).
        System.out.println("MP-8: pendiente. Clase anónima + comparing/thenComparing.");
    }
}
