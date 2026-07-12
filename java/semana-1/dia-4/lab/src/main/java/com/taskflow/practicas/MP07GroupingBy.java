package com.taskflow.practicas;

/**
 * MP-7 — Collectors.groupingBy.
 *
 * Qué construir (usa List<Task> tareas = SeedData.tareas();):
 *   1. groupingBy(Task::getStatus) -> Map<TaskStatus, List<Task>>; imprime cada grupo con
 *      encabezado (map.forEach((estado, lista) -> ...)).
 *   2. Versión CONTEO: groupingBy(Task::getStatus, Collectors.counting()) -> Map<TaskStatus, Long>
 *      (¡es EXACTAMENTE el "Ver resumen" que escribiste con loop en D1, ahora en una línea!).
 *   3. Extra: agrupa por Priority.
 *
 * PUNTO DE DOLOR 7 (tipo receptor): escribe PRIMERO el tipo de la variable — ¿quiero List<Task>
 * (groupingBy simple) o Long (counting())? — y deja que el compilador valide. Y ojo: map() del
 * Stream NO es lo mismo que Map<K,V> la colección, aunque groupingBy devuelva un Map.
 *
 * Correr: mvn -q compile exec:java "-Dexec.mainClass=com.taskflow.practicas.MP07GroupingBy"
 */
public class MP07GroupingBy {

    public static void main(String[] args) {
        // TODO 1: groupingBy(Task::getStatus) -> Map<TaskStatus, List<Task>>; imprime cada grupo.
        // TODO 2: groupingBy(..., counting()) -> Map<TaskStatus, Long>.
        // TODO 3: extra por Priority.
        System.out.println("MP-7: pendiente. groupingBy -> List vs counting -> Long.");
    }
}
