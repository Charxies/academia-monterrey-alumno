package com.taskflow.practicas;

/**
 * MP-7 — Clasificador de prioridad con if / else if / else (15 min)
 *
 * Práctica:
 *   Dado diasRestantes (int hardcodeado), imprime la prioridad de la tarea:
 *     < 2   -> "HIGH"
 *     < 7   -> "MED"
 *     resto -> "LOW"
 *   Y una versión con operador ternario para el caso simple (2 salidas).
 *
 * Correr: mvn -q compile exec:java "-Dexec.mainClass=com.taskflow.practicas.MP07IfPrioridad"
 */
public class MP07IfPrioridad {

    public static void main(String[] args) {
        // TODO 1: declara int diasRestantes = 5;
        //   (al terminar, cámbialo a 1 y a 30 y vuelve a correr: espera HIGH, MED, LOW)

        // TODO 2: con if / else if / else guarda la prioridad en un String:
        //   String prioridad;
        //   if (diasRestantes < 2) { ... } else if (diasRestantes < 7) { ... } else { ... }
        //   OJO con el ORDEN de las condiciones: si preguntas primero < 7,
        //   una tarea con 1 día caería en MED y nunca llegaría a HIGH.

        // TODO 3: imprime "Días restantes: X -> prioridad Y".

        // TODO 4: versión ternario para un caso de DOS salidas:
        //   String urgencia = diasRestantes < 2 ? "URGENTE" : "puede esperar";
        //   El ternario brilla con 2 salidas; con 3+ ramas, if/else if se lee mejor.

        System.out.println("MP-7: listo cuando 1 -> HIGH, 5 -> MED y 30 -> LOW.");
    }
}
