package com.taskflow.practicas;

/**
 * MP-3 — De lambda a method reference, CON criterio.
 *
 * Regla: si la lambda solo llama UN método con los mismos argumentos -> method ref;
 * si hay lógica (comparar, concatenar) -> se queda como lambda.
 *
 * Qué construir:
 *   1. Convierte a method reference las que SÍ se puede (de MP-2):
 *        Consumer<Task> imprimir = System.out::println;   // instancia de un objeto
 *        Function<Task,String> titulo = Task::getTitle;    // instancia de tipo arbitrario
 *        Function<String,Integer> aEntero = Integer::parseInt; // estático
 *   2. Deja como LAMBDA las que NO se puede y explica por qué en un comentario:
 *        esUrgente (usa &&) y aResumen (concatena) -> no son "un método con los mismos argumentos".
 *   3. Regla de oro: acepta la sugerencia de IntelliJ solo si puedes decir a qué método apunta.
 *
 * Correr: mvn -q compile exec:java "-Dexec.mainClass=com.taskflow.practicas.MP03MethodReferences"
 */
public class MP03MethodReferences {

    public static void main(String[] args) {
        // TODO 1: las que SÍ -> method reference (System.out::println, Task::getTitle, Integer::parseInt).
        // TODO 2: las que NO (esUrgente, aResumen) -> lambda, con comentario del porqué.
        System.out.println("MP-3: pendiente. Lambda -> method reference con criterio.");
    }
}
