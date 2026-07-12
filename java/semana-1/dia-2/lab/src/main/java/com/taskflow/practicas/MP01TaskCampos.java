package com.taskflow.practicas;

/**
 * MP-1 — Clases, constructores, this, encapsulación (arranque).
 *
 * Idea ancla: "los datos que viajan juntos deben vivir juntos — eso es una clase".
 * Los 3 arrays paralelos de ayer se vuelven UNA clase Task.
 *
 * Trabajas en DOS archivos: aquí (el 'main' que prueba) y en model/Task.java (la clase).
 *
 * Práctica:
 *   1. En model/Task.java (MP-1): declara 4 CAMPOS (title, description, status, priority)
 *      —de momento String y PÚBLICOS a propósito— y un constructor de 4 args.
 *      OJO con el sombreado: dentro del constructor escribe 'this.title = title;'.
 *      (Error intencional a provocar y leer: 'title = title;' -> campos null silenciosos.)
 *   2. Aquí: crea las 5 tareas del CLI v0 como objetos Task e imprime campo por campo.
 *   3. El "ajá": para agregar un campo tocas UNA clase, no un cuarto array.
 *
 * Correr: mvn -q compile exec:java "-Dexec.mainClass=com.taskflow.practicas.MP01TaskCampos"
 */
public class MP01TaskCampos {

    public static void main(String[] args) {
        // TODO 1: cuando Task tenga sus campos y constructor, crea 5 tareas, p. ej.:
        //   Task t1 = new Task("Crear repositorio", "Repo + CI", "DONE", "HIGH");
        //   ... (5 en total, con estados/prioridades variados)
        // TODO 2: imprime cada tarea campo por campo, p. ej.:
        //   System.out.println(t1.title + " | " + t1.status + " | " + t1.priority);
        // TODO 3 (error intencional): en el constructor de Task escribe 'title = title;'
        //   (sin this), corre, observa que title queda null, LEE el warning de IntelliJ
        //   ("variable assigned to itself") y arréglalo con 'this.title = title;'.

        System.out.println("MP-1: pendiente. Crea la clase Task y sus 5 objetos.");
    }
}
