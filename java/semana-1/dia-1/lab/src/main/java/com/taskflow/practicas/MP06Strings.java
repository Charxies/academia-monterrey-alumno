package com.taskflow.practicas;

/**
 * MP-6 — Métodos de String y el clásico equals vs == (15 min)
 *
 * Práctica:
 *   1. Ejercita length, toUpperCase, contains, substring e isBlank
 *      sobre un título de tarea.
 *   2. Reproduce el trap clásico: comparar Strings con == puede dar false
 *      aunque el contenido sea igual. La comparación correcta es .equals().
 *
 * Correr: mvn -q compile exec:java "-Dexec.mainClass=com.taskflow.practicas.MP06Strings"
 */
public class MP06Strings {

    public static void main(String[] args) {
        // TODO 1: declara String titulo = "Implementar login con JWT";
        String titulo = "Implementar login con JWT";
        // TODO 2: imprime, uno por línea:
        //   - titulo.length()            ¿cuántos caracteres tiene?
        //   - titulo.toUpperCase()       (ojo: NO modifica titulo, devuelve uno nuevo)
        //   - titulo.contains("JWT")     ¿contiene "JWT"? ¿y "jwt"? pruébalo
        //   - titulo.substring(0, 11)    los primeros 11 caracteres
        //   - "   ".isBlank()            ¿un String de puros espacios está "en blanco"?
        System.out.printf(
                " Titulo lenght : %d \n Titulo Upper: %s \n Titulo Lower: %s \n Titulo JWT?: %b \n Titulo sub: %s\n \"  \" blank?: %b\n",
                titulo.length(), titulo.toUpperCase(), titulo.toLowerCase(), titulo.contains("JWT"), titulo.substring(0,11), ("  ").isBlank()
        );
        // TODO 3: el trap de == — reprodúcelo EXACTAMENTE así:
           String estado1 = "DONE";
           String sufijo = "NE";               // OJO: sin final, es clave para el ejemplo
           String estado2 = "DO" + sufijo;     // se construye en tiempo de ejecución
        //   Imprime (estado1 == estado2) y estado1.equals(estado2).
        System.out.printf("est1 == est2: %b \n est1.equals(est2): %b\n",(estado1 == estado2), estado1.equals(estado2));
        //   ¿Por qué == da false si "se ven" iguales?
        //   Porque == compara REFERENCIAS (¿es el mismo objeto?) y equals compara CONTENIDO.

        // TODO 4: escribe en un comentario tu regla personal de una línea para
        //   nunca volver a caer en esto. (Sugerencia: "Strings SIEMPRE con equals".)

        System.out.println("MP-6: listo cuando tu == dé false y tu equals dé true.");
    }
}
