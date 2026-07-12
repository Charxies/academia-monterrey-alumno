package com.taskflow.practicas;

/**
 * MP-4 — Demo de RACE CONDITION: por qué el estado compartido mutable es peligroso (ESQUELETO).
 *
 * SIEMBRA de concepto (awareness), NO dominio. Hoy solo vemos QUÉ es un hilo y POR QUÉ compartir
 * estado mutable a la ligera rompe. synchronized/AtomicInteger/locks: más adelante (S2).
 *
 * Qué construir:
 *   1. Un contador COMPARTIDO mutable: como una lambda no puede mutar un int local, mételo en un
 *      campo de una clasecita (ej. static class Contador { int total; void incrementar(){ total++; } }).
 *   2. Un Runnable (lambda) que haga 100_000 veces contador.incrementar().
 *   3. DOS Thread con ese Runnable; start() en ambos (NO run()); luego join() en ambos.
 *   4. Imprime el total (esperado 200000) y REPITE 3 veces: verás resultados DISTINTOS.
 *
 * start() vs run(): start() crea un hilo real (corre en paralelo -> hay carrera); run() lo ejecuta
 * en el hilo actual, en secuencia (sin carrera, siempre 200000 -> el clásico error).
 * La lección ES el no determinismo, no un número concreto.
 *
 * Correr: mvn -q compile exec:java "-Dexec.mainClass=com.taskflow.practicas.MP04RaceCondition"
 */
public class MP04RaceCondition {

    public static void main(String[] args) throws InterruptedException {
        // TODO 1: contador compartido mutable (en un campo, no un int local)
        // TODO 2: Runnable que sume 100_000
        // TODO 3: 2 Thread -> start() + join()
        // TODO 4: imprimir el total y repetir 3 veces
        System.out.println("MP-4: pendiente. 2 hilos + contador compartido + join().");
    }
}
