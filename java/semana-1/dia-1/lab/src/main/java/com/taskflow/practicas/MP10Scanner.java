package com.taskflow.practicas;

import java.util.Scanner;

/**
 * MP-10 — Scanner: leer entrada, el bug del \n pendiente, y menú con do-while (15 min)
 *
 * Práctica:
 *   1. Lee la edad con nextInt() y luego el nombre con nextLine().
 *   2. Observa EL BUG: el nextLine() no espera a que escribas — se traga el
 *      Enter (\n) que nextInt() dejó pendiente en el buffer.
 *   3. Arréglalo consumiendo el \n con un nextLine() extra.
 *   4. Menú que se repite con do-while hasta elegir "Salir".
 *
 * Correr: mvn -q compile exec:java "-Dexec.mainClass=com.taskflow.practicas.MP10Scanner"
 */
public class MP10Scanner {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // TODO 1: pide la edad e imprímela:
        System.out.print("Tu edad: ");
        int edad = scanner.nextInt();
        scanner.nextLine();
        // TODO 2: AHORA pide el nombre con nextLine() y corre el programa:
           System.out.print("Tu nombre: ");
           String nombre = scanner.nextLine();
           System.out.println("Nombre leído: [" + nombre + "]");
        //   ¿Te dejó escribir? NO: imprime [] vacío. Ese es EL BUG del \n pendiente:
        //   nextInt() lee el número pero DEJA el Enter en el buffer, y tu nextLine()
        //   lee ese Enter en vez de esperar tu texto.

        // TODO 3: ARREGLA el bug: agrega un scanner.nextLine() extra inmediatamente
        //   después del nextInt() (su único trabajo es tragarse el \n pendiente)
        //   y vuelve a pedir el nombre. Regla de oro del día:
        //   "después de nextInt(), un nextLine() de cortesía".

        // TODO 4: menú que repite hasta salir, con do-while:
        //   1) Saludar   2) Mostrar edad   3) Salir   (otra cosa -> "Opción inválida")
        //   Estructura sugerida:
        //   int opcion;
        //   do {
        //       // imprimir menú
        //       // opcion = scanner.nextInt();  y  scanner.nextLine();  <- la regla de oro
        //       // if / else if para cada opción
        //   } while (opcion != 3);
        int opcion = 0;
        do{
            System.out.print("1) saludar \t2) Mostrar Edad\t3) Salir\n");
            opcion = scanner.nextInt();
            scanner.nextLine();
            if ( opcion == 1){ System.out.print("Hola!\n");}
            else if( opcion == 2) { System.out.printf("Edad: %d\n",edad );}
            else if( opcion == 3){System.out.print("Adios!");}
            else { System.out.print("opcion invalida!\n");
            }
            scanner.nextLine();
            System.out.println("\033[4A\033[0J");
            System.out.flush();
        }while(opcion != 3);

        // TODO 5 (para pensar): ¿qué pasa si el usuario escribe "abc" cuando pides
        //   un int? Pruébalo. En el integrador evitaremos ese crash leyendo la
        //   opción como String con nextLine() y comparando con equals.

        System.out.println("MP-10: completa los TODO (este archivo compila tal cual).");
        scanner.close();
    }
}
