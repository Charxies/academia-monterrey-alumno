package com.taskflow.practicas;

/**
 * MP-9 — Loops y arrays: contar e imprimir numerado (15 min)
 *
 * Práctica, sobre el array de 8 estados que ya te damos:
 *   1. Cuenta cuántos son "DONE" con un for-each + if.
 *   2. Imprime todos numerados (1. TODO / 2. DONE / ...) con un for con índice.
 *
 * Correr: mvn -q compile exec:java "-Dexec.mainClass=com.taskflow.practicas.MP09LoopsArrays"
 */
public class MP09LoopsArrays {

    public static void main(String[] args) {
        // El array ya viene dado: 8 estados de tareas (no lo modifiques).
        String[] estados = {
                "TODO", "DONE", "IN_PROGRESS", "DONE",
                "TODO", "DONE", "IN_PROGRESS", "TODO"
        };

        // TODO 1: cuenta cuántos elementos son "DONE" con un FOR-EACH:
           int cuentaDone = 0;
           for (String estado : estados) {
               if (estado.equals("DONE")) cuentaDone += 1;
           }
        //   Recuerda: la comparación es con .equals("DONE"), nunca con ==.

        // TODO 2: imprime "Tareas DONE: X de Y" usando tu contador y estados.length.
        //   (Con este array la respuesta correcta es 3 de 8.)
        System.out.printf("Tareas DONE: %d de %d\n", cuentaDone,estados.length);
        // TODO 3: imprime la lista NUMERADA con un for clásico (necesitas el índice):
        //   for (int i = 0; i < estados.length; i++) { ... }
        //   Formato de cada línea:  "1. TODO"  (ojo: i empieza en 0, la lista en 1).

        // TODO 4 (para pensar): ¿por qué el TODO 1 usó for-each y el TODO 3 un for
        //   clásico? Escribe tu respuesta en un comentario de una línea.
        //   (Pista: ¿cuál de las dos tareas necesita saber la POSICIÓN?)

        System.out.println("MP-9: listo cuando cuente 3 DONE e imprima los 8 numerados.");
    }
}
