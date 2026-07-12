package com.taskflow;

import java.util.Scanner;

/**
 * Integrador D2 — TaskFlow CLI v1 (75 min)
 *
 * ESTE ARCHIVO ES TU v0 DE AYER (la solución de referencia del Día 1), lista y
 * corriendo. Tu trabajo hoy es REFACTORIZARLA a v1 usando la POO del día. Si tu
 * propia v0 quedó rota, parte de esta.
 *
 * ----------------------------------------------------------------------------------
 * TODOs del refactor a v1 (hazlos en orden; apóyate en tus MP-1..MP-9 y en alumno.md):
 *
 *   TODO 1  Modelos: usa Task (con validación y factory crear), record User + enum Role,
 *           y la clase Project. Al arrancar, crea un User demo y un Project demo; TODA
 *           tarea pertenece a ese proyecto.
 *   TODO 2  Adiós arrays paralelos: reemplaza titulos/estados/prioridades por
 *           'Task[] tareas = new Task[20];' + 'int numTareas'. Precarga 5 tareas vía
 *           CONSTRUCTOR directo (rehidratación), ya con enums; incluye >=1 con dueDate
 *           de ayer (LocalDate.now().minusDays(1)) para ver el '*' de vencida, y
 *           >=1 con assignee y >=1 sin.
 *   TODO 3  Ver tareas: tabla printf alimentada por getters; estado y prioridad imprimen
 *           la etiqueta del enum; marca '*' las vencidas con estaVencida(); el encabezado
 *           muestra el proyecto y su owner (descripcionCorta()).
 *   TODO 4  Ver resumen: conteo por estado con SWITCH sobre TaskStatus (ya no Strings).
 *   TODO 5  NUEVA "Agregar tarea": pide título, descripción, prioridad (texto ->
 *           Priority.valueOf(entrada.trim().toUpperCase()); si IllegalArgumentException,
 *           muestra opciones y repregunta) y dueDate con leerFecha (vacío = sin fecha).
 *           Task.crear(...) en try/catch de TaskValidationException: si truena, mensaje
 *           claro y de vuelta al menú SIN crashear. Arreglo lleno -> "límite de 20".
 *   TODO 6  NUEVA "Completar tarea": lee el número con leerEntero y valida el rango;
 *           setStatus(TaskStatus.DONE) aplica la regla "no DONE sin assignee" ->
 *           el menú cachea la TaskValidationException y avisa sin crashear.
 *   TODO 7  Toda opción numérica del menú se lee con leerEntero (MP-9): copia leerEntero
 *           y leerFecha de tu MP09LecturaRobusta a este archivo.
 *   TODO 8  Requisitos: CERO arrays paralelos; CERO literales String de estado/prioridad;
 *           validación SOLO dentro de Task (nada de if de reglas en el menú); Main orquesta.
 *   TODO 9  STRETCH (opcional): buscar por título adaptada a objetos; toString de Project
 *           con owner; opción "Asignar tarea" (elige tarea -> asígnale el User demo).
 *
 * Entregable: commit "feat: taskflow cli v1 - modelo POO con enums y validacion" + push.
 * ----------------------------------------------------------------------------------
 *
 * Abajo queda tu v0 intacta (arrays paralelos): compila y corre. Ve migrándola.
 */
public class Main {

    // Constantes de estado de la v0. En v1 MUEREN: los reemplazan los enums TaskStatus/Priority.
    static final String ESTADO_TODO = "TODO";
    static final String ESTADO_IN_PROGRESS = "IN_PROGRESS";
    static final String ESTADO_DONE = "DONE";

    // ===== STRETCH v0: códigos ANSI para colorear la terminal =====
    static final String ANSI_ROJO = "\u001B[31m";
    static final String ANSI_RESET = "\u001B[0m";

    public static void main(String[] args) {
        // v0: arrays PARALELOS (el índice i de los TRES describe la MISMA tarea).
        // TODO 2: esto se convierte en Task[] + numTareas.
        String[] titulos = {
                "Crear repositorio en GitHub",
                "Configurar proyecto Maven",
                "Diseñar modelo de tareas",
                "Implementar menú de consola",
                "Escribir tests del dominio",
                "Desplegar API en AWS"
        };
        String[] estados = {
                ESTADO_DONE,
                ESTADO_DONE,
                ESTADO_IN_PROGRESS,
                ESTADO_IN_PROGRESS,
                ESTADO_TODO,
                ESTADO_TODO
        };
        String[] prioridades = {
                "HIGH",
                "MED",
                "HIGH",
                "HIGH",
                "MED",
                "LOW"
        };

        Scanner scanner = new Scanner(System.in);

        String opcion;
        do {
            System.out.println("""

                    === TaskFlow CLI v0 ===
                    1) Ver tareas
                    2) Ver resumen
                    3) Salir
                    4) Buscar por título (STRETCH)
                    """);
            System.out.print("Elige una opción: ");
            opcion = scanner.nextLine().trim();

            switch (opcion) {
                case "1" -> {
                    System.out.println("""
                            TÍTULO                         ESTADO          PRIORIDAD
                            ------------------------------ --------------- ----------""");

                    for (int i = 0; i < titulos.length; i++) {
                        String etiquetaEstado = switch (estados[i]) {
                            case ESTADO_TODO -> "Por hacer";
                            case ESTADO_IN_PROGRESS -> "En progreso";
                            case ESTADO_DONE -> "Terminada";
                            default -> estados[i];
                        };

                        boolean esHigh = prioridades[i].equals("HIGH");
                        if (esHigh) {
                            System.out.print(ANSI_ROJO);
                        }
                        System.out.printf("%-30s %-15s %-10s%n",
                                titulos[i], etiquetaEstado, prioridades[i]);
                        if (esHigh) {
                            System.out.print(ANSI_RESET);
                        }
                    }
                }
                case "2" -> {
                    int cuentaTodo = 0;
                    int cuentaInProgress = 0;
                    int cuentaDone = 0;
                    for (String estado : estados) {
                        if (estado.equals(ESTADO_TODO)) {
                            cuentaTodo++;
                        } else if (estado.equals(ESTADO_IN_PROGRESS)) {
                            cuentaInProgress++;
                        } else if (estado.equals(ESTADO_DONE)) {
                            cuentaDone++;
                        }
                    }
                    System.out.println(ESTADO_TODO + ": " + cuentaTodo
                            + " | " + ESTADO_IN_PROGRESS + ": " + cuentaInProgress
                            + " | " + ESTADO_DONE + ": " + cuentaDone);
                }
                case "3" -> System.out.println(
                        "¡Hasta mañana! No olvides el commit y push de tu entregable.");
                case "4" -> {
                    System.out.print("Texto a buscar: ");
                    String busqueda = scanner.nextLine().toLowerCase();
                    int encontradas = 0;
                    for (int i = 0; i < titulos.length; i++) {
                        if (titulos[i].toLowerCase().contains(busqueda)) {
                            System.out.printf("%-30s %-15s %-10s%n",
                                    titulos[i], estados[i], prioridades[i]);
                            encontradas++;
                        }
                    }
                    if (encontradas == 0) {
                        System.out.println("Ninguna tarea contiene: [" + busqueda + "]");
                    }
                }
                default -> System.out.println(
                        "Opción inválida: [" + opcion + "]. Escribe 1, 2, 3 o 4.");
            }
        } while (!opcion.equals("3"));

        scanner.close();
    }
}
