package com.taskflow.practicas;

import com.taskflow.exception.TaskValidationException;

/**
 * MP-4 — Map&lt;Long,Task&gt;: buscar por clave en O(1) (adiós al for lineal del warm-up).
 *
 * Qué construir:
 *   1. Map&lt;Long,Task&gt; store = new HashMap&lt;&gt;(); mete 5 tareas con claves 1L..5L (store.put(1L, ...)).
 *   2. store.get(3L) va directo a la tarea 3 (compáralo con el for de ayer).
 *   3. Recorre store.entrySet() imprimiendo "id -> título" (e.getKey(), e.getValue().getTitle()).
 *   4. Conteo por estado: Map&lt;TaskStatus,Integer&gt; con getOrDefault(estado, 0) + 1.
 *   5. Trampa del autoboxing: store.get(1) devuelve null (1 es Integer, no Long); store.get(1L) sí encuentra.
 *
 * Correr: mvn -q compile exec:java "-Dexec.mainClass=com.taskflow.practicas.MP04MapaRepositorio"
 */
public class MP04MapaRepositorio {

    public static void main(String[] args) throws TaskValidationException {
        // TODO 1: crea el Map<Long,Task> y mete 5 tareas con claves 1L..5L.
        // TODO 2: get(3L) directo.
        // TODO 3: recorre entrySet() imprimiendo "id -> título".
        // TODO 4: conteo por estado con Map<TaskStatus,Integer> + getOrDefault.
        // TODO 5: reproduce get(1) (null) vs get(1L) y explícalo en un comentario.
        System.out.println("MP-4: pendiente. Mini-repositorio con Map<Long,Task>.");
    }
}
