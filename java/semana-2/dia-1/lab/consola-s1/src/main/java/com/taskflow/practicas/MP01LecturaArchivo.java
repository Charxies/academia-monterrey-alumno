package com.taskflow.practicas;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * MP-1 — Leer data/tasks.csv con java.nio y diagnosticar rutas (SOLUCIÓN).
 *
 * Conceptos: Path.of, Files.exists, Files.readAllLines, y sobre todo ruta RELATIVA vs ABSOLUTA.
 *
 * Error intencional #1 del día: correr esto desde IntelliJ funciona (su working directory es la
 * raíz del proyecto, donde vive data/); correr el mismo .class parado en OTRA carpeta desde la
 * terminal -> el archivo "no existe", porque una ruta relativa cuelga del working directory de
 * QUIEN ejecuta, no de dónde está el .class. El diagnóstico es imprimir toAbsolutePath() y el
 * working directory. Regla del curso: rutas relativas a la raíz del proyecto y se corre desde ahí.
 *
 * Correr: mvn -q compile exec:java "-Dexec.mainClass=com.taskflow.practicas.MP01LecturaArchivo"
 */
public class MP01LecturaArchivo {

    public static void main(String[] args) {
        Path ruta = Path.of("data", "tasks.csv");

        // Diagnóstico: de dónde cuelga la ruta relativa.
        System.out.println("Working directory : " + Path.of("").toAbsolutePath());
        System.out.println("Ruta relativa     : " + ruta);
        System.out.println("Ruta absoluta     : " + ruta.toAbsolutePath());
        System.out.println("¿Existe?          : " + Files.exists(ruta));

        if (!Files.exists(ruta)) {
            // Esto es lo que verías con NoSuchFileException si intentaras leer: mejor avisarlo claro.
            System.out.println("No encuentro " + ruta.toAbsolutePath()
                    + ". Corre el programa desde la raíz del proyecto (la carpeta que contiene data/).");
            return;
        }

        // IOException es CHECKED: el compilador obliga a manejarla. La atrapamos aquí con try/catch.
        try {
            List<String> lineas = Files.readAllLines(ruta);
            System.out.println("\nLeídas " + lineas.size() + " líneas:");
            lineas.forEach(System.out::println);   // method reference: imprime cada línea
        } catch (IOException e) {
            System.out.println("Error de E/S al leer " + ruta + ": " + e.getMessage());
        }
    }
}
