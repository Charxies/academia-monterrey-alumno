package com.taskflow.practicas;

/**
 * MP-9 — Los streams también devuelven Optional: findFirst().
 *
 * Qué construir (usa List<Task> tareas = SeedData.tareas();):
 *   1. Primera tarea cuyo título contiene un texto (case-insensitive) -> Optional<Task>:
 *        tareas.stream()
 *              .filter(t -> t.getTitle().toLowerCase().contains(q))
 *              .findFirst();
 *   2. Encadena .map(Task::getDueDate) sobre el Optional y ciérralo con .orElse(...).
 *   3. Prueba también con un texto que NO exista: el Optional viene vacío y orElse da el default
 *      (no crashea). Conecta: el Optional de findFirst es el MISMO contrato que el de findById.
 *
 * Correr: mvn -q compile exec:java "-Dexec.mainClass=com.taskflow.practicas.MP09FindFirst"
 */
public class MP09FindFirst {

    public static void main(String[] args) {
        // TODO 1: findFirst() con filter por título -> Optional<Task>.
        // TODO 2: encadena map(Task::getDueDate) + orElse.
        // TODO 3: prueba una búsqueda sin resultados (Optional vacío + orElse).
        System.out.println("MP-9: pendiente. findFirst() -> Optional<Task>.");
    }
}
