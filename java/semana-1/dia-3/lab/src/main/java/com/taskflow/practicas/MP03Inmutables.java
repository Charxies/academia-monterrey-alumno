package com.taskflow.practicas;

/**
 * MP-3 — Inmutabilidad básica con List.of(...).
 *
 * Qué construir:
 *   1. List&lt;String&gt; fija = List.of("backend", "urgente", "cli");
 *   2. Intenta fija.add("x") -> UnsupportedOperationException. LEE el error (envuélvelo en
 *      try/catch o déjalo comentado). ¿Cuándo conviene una lista inmutable? (constantes,
 *      retornos que nadie debe mutar).
 *   3. List.of tampoco acepta null: List.of("a", null) -> NullPointerException.
 *   4. ¿Necesitas mutar? Copia mutable: new ArrayList&lt;&gt;(List.of("backend", "urgente")), y ahí sí add(...).
 *
 * Correr: mvn -q compile exec:java "-Dexec.mainClass=com.taskflow.practicas.MP03Inmutables"
 */
public class MP03Inmutables {

    public static void main(String[] args) {
        // TODO 1: crea la lista inmutable con List.of(...).
        // TODO 2: intenta add(...) y observa UnsupportedOperationException.
        // TODO 3: List.of(..., null) y observa NullPointerException.
        // TODO 4: copia mutable con new ArrayList<>(List.of(...)) y add(...).
        System.out.println("MP-3: pendiente. List.of inmutable + copia mutable.");
    }
}
