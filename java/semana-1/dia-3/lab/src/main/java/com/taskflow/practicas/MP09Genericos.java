package com.taskflow.practicas;

import com.taskflow.exception.TaskValidationException;

/**
 * MP-9 — Generics: por qué NO usar colecciones CRUDAS.
 *
 * Qué construir:
 *   1. Error intencional #3 — la List CRUDA:
 *        List cruda = new ArrayList();   // sin <Task>: el compilador solo AVISA (raw type)
 *        cruda.add(task); cruda.add("sorpresa");
 *        for (Object o : cruda) { Task t = (Task) o; ... }  // en "sorpresa" -> ClassCastException (runtime)
 *      Provoca la ClassCastException y LEE el mensaje (envuélvela en try/catch para seguir).
 *   2. La misma jugada con List&lt;Task&gt;: intenta add("sorpresa") -> NO compila (el compilador te salva).
 *      Déjala comentada (si no, no compila).
 *   3. Escribe la clase genérica Caja&lt;T&gt; (archivo Caja.java) con guardar(T) y sacar():T,
 *      y úsala con Caja&lt;Task&gt; y Caja&lt;String&gt;.
 *
 * Correr: mvn -q compile exec:java "-Dexec.mainClass=com.taskflow.practicas.MP09Genericos"
 */
public class MP09Genericos {

    public static void main(String[] args) throws TaskValidationException {
        // TODO 1: List cruda -> add(task) y add("sorpresa") -> ClassCastException al castear en el for.
        // TODO 2: List<Task> -> intenta add("sorpresa") (comentado: no compila; ese es el punto).
        // TODO 3: completa Caja<T> (Caja.java) y úsala con Caja<Task> y Caja<String>.
        System.out.println("MP-9: pendiente. Raw type -> ClassCastException; Caja<T>.");
    }
}
