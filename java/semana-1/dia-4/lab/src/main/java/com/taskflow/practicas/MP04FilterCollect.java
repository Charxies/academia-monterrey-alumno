package com.taskflow.practicas;

/**
 * MP-4 — filter + collect(Collectors.toList()) sobre una List<Task>.
 *
 * Qué construir (usa List<Task> tareas = SeedData.tareas();):
 *   1. Pendientes: filter(t -> t.getStatus() != TaskStatus.DONE).collect(Collectors.toList()).
 *   2. Prioridad HIGH: filter(t -> t.getPriority() == Priority.HIGH)...
 *   3. Las DOS condiciones: primero con dos filter encadenados; luego reutilizando el Predicate
 *      compuesto de MP-2 (filter(esUrgente)) -> mismo resultado.
 *   4. Imprime cada lista con forEach(imprimirTarea) (un Consumer<Task> reutilizado).
 *
 * Correr: mvn -q compile exec:java "-Dexec.mainClass=com.taskflow.practicas.MP04FilterCollect"
 */
public class MP04FilterCollect {

    public static void main(String[] args) {
        // TODO 1: pendientes con filter + collect.
        // TODO 2: prioridad HIGH.
        // TODO 3: las dos condiciones (dos filter, luego filter(esUrgente)).
        // TODO 4: imprime con forEach(imprimirTarea).
        System.out.println("MP-4: pendiente. filter + collect(toList).");
    }
}
