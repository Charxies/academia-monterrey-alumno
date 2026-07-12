package com.taskflow.practicas;

/**
 * MP-4 — Enums.
 *
 * El problema del "String mágico": status acepta "DONEE" y nadie avisa. El enum es la
 * solución: conjunto cerrado que el COMPILADOR vigila.
 *
 * Paso 1 (en model/): completa TaskStatus y Priority con su campo 'etiqueta' (ver los TODO
 * dentro de esos archivos). Paso 2: refactoriza Task para que status/priority sean
 * TaskStatus/Priority (no String); estaVencida() pasa a '... && status != TaskStatus.DONE'
 * ('==' / '!=' entre enums es SEGURO — contraste con el trap de equals de los String de D1).
 *
 * Aquí (el main):
 *   1. Recorre TaskStatus.values() y Priority.values() imprimiendo cada uno con su etiqueta.
 *   2. Comprueba que 'TaskStatus.DONE == TaskStatus.valueOf("DONE")' es true.
 *   3. Provoca (y SOLO LEE por ahora) el error: TaskStatus.valueOf("done") en minúsculas
 *      lanza IllegalArgumentException. En PM-1 aprenderás a capturarla.
 *
 * Correr: mvn -q compile exec:java "-Dexec.mainClass=com.taskflow.practicas.MP04Enums"
 */
public class MP04Enums {

    public static void main(String[] args) {
        // TODO 1: for (TaskStatus s : TaskStatus.values()) System.out.println(s + " -> " + s.getEtiqueta());
        //         (ídem con Priority)
        // TODO 2: System.out.println(TaskStatus.DONE == TaskStatus.valueOf("DONE"));
        // TODO 3: TaskStatus.valueOf("done");  // observa el IllegalArgumentException en consola

        System.out.println("MP-4: pendiente. Completa los enums con etiqueta y migra Task.");
    }
}
