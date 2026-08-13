package com.taskflow.practicas;

/**
 * MP-4 — Variables y tipos (20 min)
 *
 * Práctica:
 *   1. Declara una variable de cada tipo primitivo visto (int, long, double,
 *      boolean, char) y un String, con nombres en camelCase.
 *   2. Imprime un resumen de TRES formas: concatenación (+),
 *      printf / String.format, y un text block.
 *   3. Provoca A PROPÓSITO dos errores de compilación y LEE el mensaje del
 *      compilador. Saber leer errores es una habilidad clave de junior.
 *
 * Correr: mvn -q compile exec:java "-Dexec.mainClass=com.taskflow.practicas.MP04Variables"
 */
public class MP04Variables {

    public static void main(String[] args) {
        // TODO 1: declara e inicializa estas variables sobre una tarea de TaskFlow:
        //   int     diasRestantes          (p. ej. 5)
        //   long    totalTareasHistoricas  (usa el sufijo L: 1_500_000L)
        //   double  horasEstimadas         (p. ej. 7.5)
        //   boolean completada             (p. ej. false)
        //   char    inicialPrioridad       (p. ej. 'H' — char usa comillas SIMPLES)
        //   String  titulo                 (p. ej. "Configurar proyecto Maven")
        int diasRestantes = 4;
        long totalTareasHistoricas = 1_500_000L;
        double horasEstimadas = 5.5;
        boolean completado = false;
        char inicialPrioridad = 'X';
        String titulo = "Configurar proyecto Maven";
        // TODO 2: imprime título y días restantes con CONCATENACIÓN (+).
        //   Pista: System.out.println("Tarea: " + titulo + " | días: " + diasRestantes);

        // TODO 3: imprime horas estimadas y completada con printf.
        //   Códigos: %s String, %d entero, %.1f double con 1 decimal, %b boolean,
        //   %c char, %n salto de línea portable.
        //   Pista: System.out.printf("Horas: %.1f | completada: %b%n", ...);

        // TODO 4: arma un resumen multilínea con un TEXT BLOCK ("""...""")
        //   y rellénalo con .formatted(titulo, diasRestantes, horasEstimadas).

        // TODO 5: declara una variable con var (el compilador infiere el tipo).
        //   Pista: var proyecto = "TaskFlow";   // infiere String
        //   Regla del curso: usa var solo cuando el tipo sea OBVIO al leer la línea.

        // TODO 6: declara una constante con final y UPPER_SNAKE_CASE, e imprímela.
        //   Pista: final int MAX_TAREAS_POR_DIA = 10;

        // ================== ERRORES A PROPÓSITO (uno a la vez) ==================
        // TODO 7: descomenta la siguiente línea, compila y LEE el error completo:
        //   "incompatible types: String cannot be converted to int".
        //   Cuando entiendas qué te está diciendo, vuelve a comentarla.
        // int numeroRoto = "42";

        // TODO 8: descomenta las DOS líneas siguientes, compila y lee el error:
        //   "variable sinValor might not have been initialized".
        //   Java NO deja usar variables locales sin inicializar. Coméntalas de nuevo.
        // int sinValor;
        // System.out.println(sinValor);
        // ========================================================================

        System.out.println("MP-4: listo cuando imprimas tu resumen de 3 formas distintas.");
    }
}
