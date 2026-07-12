package com.taskflow.practicas;

import java.util.Scanner;

/**
 * MP-9 — Lectura robusta de consola. Estas DOS utilidades se COPIAN tal cual al integrador.
 *
 * Escribe dos métodos static que reintentan hasta obtener un valor válido (sin crashear):
 *
 *   static int leerEntero(Scanner sc, String prompt) {
 *       // loop: imprime prompt, lee sc.nextLine(), intenta Integer.parseInt(entrada.trim());
 *       // si lanza NumberFormatException -> avisa y repite.
 *   }
 *   static LocalDate leerFecha(Scanner sc, String prompt) {
 *       // loop: imprime prompt (con el formato yyyy-MM-dd VISIBLE), lee la línea;
 *       // si está vacía -> return null (sin fecha); intenta LocalDate.parse(entrada);
 *       // si lanza DateTimeParseException -> avisa y repite.
 *   }
 *
 * Nota de continuidad: nextLine()+parse mata de raíz el bug del '\n' de D1 -> de aquí en
 * adelante es el estándar del curso; adiós nextInt().
 *
 * En el main: usa un Scanner(System.in) y prueba leerEntero y leerFecha con entradas malas y buenas.
 *
 * Correr: mvn -q compile exec:java "-Dexec.mainClass=com.taskflow.practicas.MP09LecturaRobusta"
 */
public class MP09LecturaRobusta {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        // TODO 1: implementa los métodos leerEntero y leerFecha (abajo).
        // TODO 2: int edad = leerEntero(sc, "Tu edad: ");   (prueba escribiendo "abc" y luego un número)
        // TODO 3: LocalDate f = leerFecha(sc, "Fecha (yyyy-MM-dd, vacío = sin fecha): ");
        // TODO 4: imprime lo leído.
        System.out.println("MP-9: pendiente. Escribe leerEntero y leerFecha (se copian al integrador).");
        sc.close();
    }

    // TODO MP-9: static int leerEntero(Scanner sc, String prompt) { ... }
    // TODO MP-9: static LocalDate leerFecha(Scanner sc, String prompt) { ... }
    //   (necesitarás: import java.time.LocalDate; import java.time.format.DateTimeParseException;)
}
