package com.taskflow.practicas;

/**
 * MP-8 — Switch expression: de estado técnico a etiqueta legible (15 min)
 *
 * Práctica:
 *   Convierte un estado de tarea ("TODO" / "IN_PROGRESS" / "DONE") a una
 *   etiqueta legible en español usando un SWITCH EXPRESSION con -> :
 *     "TODO"        -> "Por hacer"
 *     "IN_PROGRESS" -> "En progreso"
 *     "DONE"        -> "Terminada"
 *     default       -> algo claro para estados desconocidos
 *
 * Correr: mvn -q compile exec:java "-Dexec.mainClass=com.taskflow.practicas.MP08SwitchEstados"
 */
public class MP08SwitchEstados {

    public static void main(String[] args) {
        // TODO 1: declara String estado = "IN_PROGRESS";
        //   (después pruébalo con "TODO", "DONE" y un valor inventado como "CANCELLED")

        // TODO 2: escribe el switch EXPRESSION. Forma general:
        //   String etiqueta = switch (estado) {
        //       case "TODO" -> "Por hacer";
        //       ...
        //       default -> ...;
        //   };
        //   Nota el ; al final: es una EXPRESIÓN que devuelve un valor.
        //   Sin break, sin fall-through, y el compilador te obliga a cubrir todo.

        // TODO 3: imprime  estado + " -> " + etiqueta

        // TODO 4 (reto opcional): replica el bug del switch CLÁSICO: escribe un
        //   switch clásico (case X: ... break;) sobre un int y quítale UN break.
        //   Corre y observa cómo la ejecución "cae" al siguiente case (fall-through).
        //   Ese bug silencioso es la razón por la que preferimos -> .

        System.out.println("MP-8: listo cuando cada estado imprima su etiqueta en español.");
    }
}
