package com.taskflow;

import java.util.Scanner;

/**
 * Integrador D1 — TaskFlow CLI v0 (75 min)
 *
 * Menú interactivo de consola que muestra tareas hardcodeadas.
 *
 * Requisitos (checklist en el README):
 *   1) Ver tareas  -> tabla alineada con printf (%-30s %-15s %-10s),
 *      encabezado con text block, etiqueta de estado con switch expression.
 *   2) Ver resumen -> "TODO: n | IN_PROGRESS: n | DONE: n"
 *   3) Salir       -> despedida.
 *   - Arrays PARALELOS con mínimo 5 tareas del dominio TaskFlow.
 *   - Constantes final String para los 3 estados (nada de literales regados).
 *   - Opción inválida: mensaje claro, NO crashea, vuelve al menú.
 *
 * Entregable: commit "feat: taskflow cli v0 - menu y listado de tareas" + push.
 *
 * Este esqueleto COMPILA Y CORRE tal cual: ve llenando los TODO en orden.
 */
public class Main {

    // TODO 1: completa las constantes de estado. Ya te damos la primera.
    //         Convención: static final + UPPER_SNAKE_CASE.
    //         Úsalas en TODO el archivo: nada de escribir "DONE" suelto por ahí.
    static final String ESTADO_TODO = "TODO";
    static final String ESTADO_IN_PROGRESS = "IN_PROGRESS";
    static final String ESTADO_DONE = "DONE";

    public static void main(String[] args) {
        // TODO 2: completa los arrays PARALELOS hasta tener MÍNIMO 5 tareas.
        //         "Paralelos" = el índice i de los TRES arrays describe la MISMA tarea:
        //         titulos[0] tiene el estado estados[0] y la prioridad prioridades[0].
        //         Usa tareas del dominio TaskFlow (crear repo, endpoints, tests, deploy...)
        //         y tus constantes de estado del TODO 1. Prioridades: "LOW", "MED", "HIGH".
        //         (Sí, mantener 3 arrays sincronizados duele. Mañana lo curamos con POO.)
        String[] titulos = {
                "Crear repositorio en GitHub",
                "Configurar proyecto Maven",
                "Clonar repositorio a local",
                "Checar Endpoints",
                "Deployear el proyecto"
                // ...agrega al menos 3 más...
        };
        String[] estados = {
                ESTADO_DONE,
                ESTADO_DONE,
                ESTADO_DONE,
                ESTADO_IN_PROGRESS,
                ESTADO_TODO
                // ...uno por cada título, en el mismo orden...
        };
        String[] prioridades = {
                "HIGH",
                "MED",
                "MED",
                "LOW",
                "HIGH"
                // ...una por cada título, en el mismo orden...
        };

        Scanner scanner = new Scanner(System.in);

        // Leemos la opción con nextLine() y NO con nextInt(), por dos razones:
        //   1. nextInt() deja un salto de línea pendiente en el buffer (el bug de MP-10).
        //   2. Si el usuario escribe "abc", nextInt() truena con InputMismatchException;
        //      leyendo String, "abc" simplemente cae en el default del switch. Sin crash.
        String opcion;
        do {
            System.out.println("""

                    === TaskFlow CLI v0 ===
                    1) Ver tareas
                    2) Ver resumen
                    3) Salir
                    """);
            System.out.print("Elige una opción: ");
            opcion = scanner.nextLine().trim();

            switch (opcion) {
                case "1" -> {
                    // TODO 3: imprime la tabla de tareas.
                    //   3a. Encabezado con un TEXT BLOCK cuyas columnas midan 30, 15 y 10
                    //       (para que cuadre con el printf de las filas). Ejemplo de forma:
                    //       TÍTULO <espacios hasta 30> ESTADO <espacios hasta 15> PRIORIDAD
                    //       ------------------------------ --------------- ----------
                    //   3b. Recorre los arrays con un for clásico (necesitas el índice i).
                    //   3c. Convierte estados[i] a etiqueta legible con un SWITCH EXPRESSION:
                    //       ESTADO_TODO -> "Por hacer", ESTADO_IN_PROGRESS -> "En progreso",
                    //       ESTADO_DONE -> "Terminada", default -> estados[i] tal cual.
                    //       Pista: String etiqueta = switch (estados[i]) { ... };
                    //   3d. Imprime cada fila con printf: "%-30s %-15s %-10s%n"
                    //       (%-30s = String alineado a la izquierda en 30 espacios).
                    System.out.print(""" 
                        ----------------------------------------------------------
                        ||         TITULO          |      ESTADO    |    PRIO   ||
                        """);
                    for (int i = 0; i < titulos.length; i++){
                        String etiqueta = switch (estados [i]) {
                            case ESTADO_TODO -> "Por hacer";
                            case ESTADO_DONE-> "Hecho";
                            case ESTADO_IN_PROGRESS -> "En progreso";
                            default -> estados[i];
                        };
                        System.out.printf("||%-28s-%-16s-%-8s||%n", etiqueta,estados[i],prioridades[i]);
                    }
                    System.out.print("----------------------------------------------------------");
                }
                case "2" -> {
                    // TODO 4: cuenta cuántas tareas hay de cada estado.
                    //   Pista: tres contadores int + un for-each sobre estados
                    //   comparando con .equals(...) — ¡nunca con == !
                    //   Imprime EXACTAMENTE con este formato:
                    //   TODO: n | IN_PROGRESS: n | DONE: n
                    System.out.println("(TODO 4: aquí va el resumen por estado)");
                }
                case "3" -> {
                    // TODO 5: mensaje de despedida (la condición del while ya corta el loop).
                    System.out.println("(TODO 5: despedida)");
                }
                default -> {
                    // TODO 6: mensaje claro de opción inválida. No hace falta más:
                    //         el do-while vuelve a mostrar el menú solito.
                    System.out.println("(TODO 6: mensaje de opción inválida)");
                }
            }
        } while (!opcion.equals("3"));

        scanner.close();

        // ===================== STRETCH (solo si ya terminaste TODO lo demás) ==============
        // TODO 7 (STRETCH): opción 4 "Buscar por título".
        //   - Agrega la línea "4) Buscar por título" al menú y un case "4" al switch.
        //   - Pide un texto con scanner.nextLine() y muestra las tareas cuyo título
        //     lo contenga SIN distinguir mayúsculas.
        //     Pista: titulos[i].toLowerCase().contains(busqueda.toLowerCase())
        //   - Si nada coincide, dilo con un mensaje (no dejes la pantalla muda).
        //
        // TODO 8 (STRETCH): resalta en ROJO las filas con prioridad HIGH en la tabla.
        //   Pista ANSI: declara dos constantes String — rojo = "\u001B[31m" y
        //   reset = "\u001B[0m" (\u001B es la secuencia unicode del carácter
        //   ESC, el 27, que inicia los códigos de color de la terminal).
        //   Haz System.out.print(ROJO) ANTES del printf de la fila y
        //   System.out.print(RESET) DESPUÉS. Imprime el color FUERA del printf
        //   para no descuadrar la alineación de las columnas.
        // ==================================================================================
    }
}
