package com.taskflow.practicas;

import com.taskflow.exception.TaskValidationException;

/**
 * MP-2 — Error intencional #1: ConcurrentModificationException (CME).
 *
 * Qué construir:
 *   0. La forma ROTA (para verla explotar): recorre una List&lt;Task&gt; con for-each y dentro
 *      haz list.remove(t) de las DONE -> salta ConcurrentModificationException. LEE el stack
 *      trace completo. (Déjala COMENTADA cuando termines, o envuélvela en try/catch.)
 *   a. Arréglalo con Iterator explícito: it.next(); if (...) it.remove();
 *   b. Arréglalo con removeIf(t -> t.getStatus() == TaskStatus.DONE);  // la lambda es RECETA (teoría D4)
 *
 * Regla: NUNCA borres de una lista dentro de un for-each. Usa removeIf o Iterator.remove.
 *
 * Correr: mvn -q compile exec:java "-Dexec.mainClass=com.taskflow.practicas.MP02BorrarSeguro"
 */
public class MP02BorrarSeguro {

    public static void main(String[] args) throws TaskValidationException {
        // TODO 0: provoca la CME borrando con for-each + list.remove(t) y lee el stack trace.
        // TODO a: arréglalo con Iterator + it.remove().
        // TODO b: arréglalo con removeIf(...). Compara: ¿cuántas tareas quedan?
        System.out.println("MP-2: pendiente. Provoca la CME y arréglala 2 formas.");
    }
}
