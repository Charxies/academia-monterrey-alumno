package com.taskflow.practicas;

import com.taskflow.exception.TaskValidationException;

/**
 * MP-5 — Set: colección SIN duplicados.
 *
 * Qué construir:
 *   1. Set&lt;String&gt; etiquetas = new HashSet&lt;&gt;(); add("urgente") dos veces:
 *      el primero devuelve true, el segundo false (el Set no admite duplicados).
 *   2. Dada una List&lt;Task&gt; con assigneeId repetidos, recolecta los únicos en un Set&lt;Long&gt;
 *      (ignora los null). Verás que el mismo id no se repite.
 *   3. contains(...) es O(1): pregúntale al Set si un id está.
 *
 * Correr: mvn -q compile exec:java "-Dexec.mainClass=com.taskflow.practicas.MP05EtiquetasUnicas"
 */
public class MP05EtiquetasUnicas {

    public static void main(String[] args) throws TaskValidationException {
        // TODO 1: Set<String> con add("urgente") x2 -> observa el boolean (true, luego false).
        // TODO 2: recolecta assigneeId únicos de una List<Task> con repetidos en un Set<Long>.
        // TODO 3: contains(...) O(1).
        System.out.println("MP-5: pendiente. Set sin duplicados.");
    }
}
