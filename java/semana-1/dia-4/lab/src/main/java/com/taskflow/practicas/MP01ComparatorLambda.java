package com.taskflow.practicas;

/**
 * MP-1 — El puente de continuidad: refactor del Comparator anónimo de D3 → lambda.
 *
 * Qué construir (las 3 versiones del MISMO comparator por dueDate, lado a lado):
 *   1. Arma una List<Task> con 3-4 tareas de FECHAS NO NULAS (usa el constructor de rehidratación
 *      new Task(id, title, "", status, priority, 1L, 1L, LocalDate.of(...))). Declara
 *      main(...) throws TaskValidationException.
 *   2. v1 — clase anónima:  new Comparator<Task>() { public int compare(Task a, Task b) {
 *          return a.getDueDate().compareTo(b.getDueDate()); } }
 *   3. v2 — lambda:         (a, b) -> a.getDueDate().compareTo(b.getDueDate())
 *   4. v3 — comparing:      Comparator.comparing(Task::getDueDate)
 *   5. Ordena una COPIA con cada una (new ArrayList<>(fuente); copia.sort(cmp)) e imprime:
 *      las 3 dan el MISMO orden.
 *   6. Encadena: Comparator.comparing(Task::getDueDate).thenComparing(Task::getPriority)
 *      y prueba .reversed() (invierte TODA la cadena).
 *
 * Correr: mvn -q compile exec:java "-Dexec.mainClass=com.taskflow.practicas.MP01ComparatorLambda"
 */
public class MP01ComparatorLambda {

    public static void main(String[] args) {
        // TODO 1: List<Task> con fechas no nulas.
        // TODO 2-4: las 3 versiones del comparator por dueDate (anónima, lambda, comparing).
        // TODO 5: ordenar una copia con cada una e imprimir.
        // TODO 6: thenComparing(Task::getPriority) y reversed().
        System.out.println("MP-1: pendiente. Las 3 versiones del comparator por dueDate.");
    }
}
