package com.taskflow.practicas;

/**
 * MP-1 — Leer data/tasks.csv con java.nio y diagnosticar rutas (ESQUELETO).
 *
 * Qué construir:
 *   1. Path ruta = Path.of("data", "tasks.csv");
 *   2. Imprime el working directory (Path.of("").toAbsolutePath()), la ruta relativa, y
 *      ruta.toAbsolutePath()  <- ESTE es el diagnóstico del "no encuentra el archivo".
 *   3. Si Files.exists(ruta): léelo con Files.readAllLines(ruta) e imprime línea por línea.
 *      La IOException es CHECKED: manéjala con try/catch (o 'throws' en main).
 *
 * Error intencional #1 del día: corre desde IntelliJ (funciona) y luego el mismo .class parado en
 * OTRA carpeta desde terminal -> "no existe", porque una ruta relativa cuelga del working directory
 * de QUIEN ejecuta. Regla del curso: rutas relativas a la raíz del proyecto y se corre desde ahí.
 *
 * Correr: mvn -q compile exec:java "-Dexec.mainClass=com.taskflow.practicas.MP01LecturaArchivo"
 */
public class MP01LecturaArchivo {

    public static void main(String[] args) {
        // TODO 1: Path.of("data", "tasks.csv")
        // TODO 2: imprime working directory + ruta relativa + toAbsolutePath()
        // TODO 3: si existe, Files.readAllLines y forEach(System.out::println) (maneja IOException)
        System.out.println("MP-1: pendiente. Files.readAllLines + toAbsolutePath.");
    }
}
