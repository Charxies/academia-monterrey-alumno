package com.taskflow.practicas;

/**
 * MP-5 — Operadores y la trampa de la división entera (20 min)
 *
 * Práctica:
 *   1. Promedio de minutos estimados de 3 tareas — con la trampa de int/int incluida.
 *   2. Conversión double -> int con cast explícito (¿redondea o trunca?).
 *   3. Par o impar con el operador módulo (%).
 *
 * Correr: mvn -q compile exec:java "-Dexec.mainClass=com.taskflow.practicas.MP05Operadores"
 */
public class MP05Operadores {

    public static void main(String[] args) {
        // TODO 1: declara los minutos estimados de 3 tareas:
        //   int min1 = 90, min2 = 85, min3 = 78;
        int min1 = 90, min2 = 85, min3  = 78;
        // TODO 2: calcula el promedio "ingenuo" en un int: (min1 + min2 + min3) / 3
        //   e imprímelo. La suma es 253, así que esperarías 84.33... ¿qué imprime?
        //   int/int DESCARTA los decimales en silencio. No avisa. No redondea.
        int prom = (min1 + min2 + min3)/3;
        System.out.print(prom);//regresara un int sin decimales
        // TODO 3: comprueba la trampa mínima imprimiendo estas dos divisiones:
           System.out.println(5 / 2);     // ¿2 o 2.5?
           System.out.println(5 / 2.0);   // basta UN double para que la división sea double

        // TODO 4: calcula el promedio CORRECTO en un double, de las dos formas:
        //   a) dividiendo entre 3.0
        //   b) casteando: (double) (min1 + min2 + min3) / 3
        //   Imprímelo con printf y 2 decimales: %.2f
        double promDouble = (3.7 + 4.2 + 1.1 )/3;
        double promDoubleCast = (double) (min1+min2+min3)/3;
        System.out.printf("prom double 1 = %.2f || prom double cast = %.2f \n", promDouble, promDoubleCast);
        // TODO 5: convierte un double a int con cast explícito:
        //   double horasReales = 6.9;
        //   int horasEnteras = (int) horasReales;
        //   Imprime ambos. ¿El cast redondeó a 7 o truncó a 6?

        // TODO 6: usa % (módulo) para saber si un número de tarea es par o impar:
        //   int numeroTarea = 7;
        //   Pista: numeroTarea % 2 == 0 es un boolean — guárdalo e imprímelo.

        System.out.println("MP-5: listo cuando expliques por qué 253/3 no da 84.33 en Java.");
    }
}
