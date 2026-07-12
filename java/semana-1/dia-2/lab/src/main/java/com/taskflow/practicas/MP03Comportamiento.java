package com.taskflow.practicas;

/**
 * MP-3 — El comportamiento vive CON los datos.
 *
 * En model/Task.java: agrega el campo 'LocalDate dueDate' (intro mínima de java.time:
 * LocalDate.of(a,m,d), LocalDate.now(), fecha.isBefore(otra) — 3 métodos y ya) y el método:
 *   boolean estaVencida() {
 *       return dueDate != null && dueDate.isBefore(LocalDate.now()) && !status.equals("DONE");
 *   }
 * (El equals("DONE") feo aquí es SEMILLA: en MP-4 el enum lo vuelve 'status != DONE'.)
 *
 * Aquí (el main): crea una tarea con fecha de AYER (LocalDate.now().minusDays(1)) y otra
 * con fecha futura; comprueba que la primera reporta estaVencida() == true y la otra false.
 *
 * Correr: mvn -q compile exec:java "-Dexec.mainClass=com.taskflow.practicas.MP03Comportamiento"
 */
public class MP03Comportamiento {

    public static void main(String[] args) {
        // TODO 1: crea una Task con dueDate = LocalDate.now().minusDays(1) -> vencida.
        // TODO 2: crea otra con dueDate = LocalDate.now().plusDays(3)  -> no vencida.
        // TODO 3: imprime t.estaVencida() de cada una y verifica true/false.

        System.out.println("MP-3: pendiente. Agrega dueDate y estaVencida() a Task.");
    }
}
