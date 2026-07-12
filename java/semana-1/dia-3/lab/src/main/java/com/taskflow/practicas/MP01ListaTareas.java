package com.taskflow.practicas;

import com.taskflow.exception.TaskValidationException;

/**
 * MP-1 — List&lt;Task&gt;: el reemplazo del array de tamaño fijo.
 *
 * Qué construir (main declara 'throws TaskValidationException' para poder crear Tasks):
 *   1. List&lt;Task&gt; tareas = new ArrayList&lt;&gt;();  // programa contra la INTERFAZ List
 *      Agrega 5 tareas con new Task(1L, "título >=3 chars", "", TaskStatus..., Priority..., 1L, assigneeId, null).
 *   2. Prueba size(), get(0), for-each, contains(...) e indexOf(...).
 *   3. Trampa remove(int) vs remove(Object) en List&lt;Integer&gt;:
 *        List<Integer> n = new ArrayList<>(List.of(10, 20, 30, 40));
 *        n.remove(2);                    // borra el ÍNDICE 2 (quita el 30)
 *        n.remove(Integer.valueOf(40));  // borra el VALOR 40
 *
 * Correr: mvn -q compile exec:java "-Dexec.mainClass=com.taskflow.practicas.MP01ListaTareas"
 */
public class MP01ListaTareas {

    public static void main(String[] args) throws TaskValidationException {
        // TODO 1: crea la List<Task> y agrega 5 tareas (ojo: título 3-120 chars).
        // TODO 2: size(), get(0), for-each, contains(...), indexOf(...).
        // TODO 3: la trampa remove(int) vs remove(Object) con una List<Integer>.
        System.out.println("MP-1: pendiente. Migra el array a List<Task>.");
    }
}
