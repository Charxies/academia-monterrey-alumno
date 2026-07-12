package com.taskflow.practicas;

/**
 * MP-4 — Demo de RACE CONDITION: por qué el estado compartido mutable es peligroso (SOLUCIÓN).
 *
 * SIEMBRA de concepto (awareness), NO dominio: hoy solo vemos QUÉ es un hilo y POR QUÉ compartir
 * estado mutable a la ligera rompe. synchronized / AtomicInteger / locks se estudian cuando Spring
 * atienda N peticiones a la vez (S2). Aquí la lección es una sola: NO compartas estado mutable sin cuidado.
 *
 * Qué hace: dos hilos suman 100 000 cada uno sobre el MISMO contador (total = 200 000 esperado).
 * Como 'total++' NO es atómico (leer, sumar, escribir = 3 pasos), los hilos se pisan y el
 * resultado casi siempre es MENOR a 200 000. Corremos 3 veces: los resultados VARÍAN -> la lección
 * es el NO DETERMINISMO, no un número concreto.
 *
 * start() vs run(): usamos start() (crea un hilo REAL y ejecuta run() en él). Si llamáramos run()
 * directo, correría en el hilo actual, en secuencia -> sin carrera y siempre 200 000 (el clásico).
 *
 * Correr: mvn -q compile exec:java "-Dexec.mainClass=com.taskflow.practicas.MP04RaceCondition"
 */
public class MP04RaceCondition {

    private static final int VUELTAS = 100_000;

    /**
     * Estado compartido MUTABLE: el objeto que los dos hilos tocan a la vez. Una lambda no puede
     * mutar un 'int' local (debe ser final/effectively final), así que el contador vive en un campo.
     */
    static class Contador {
        int total = 0;

        void incrementar() {
            total++;   // NO atómico: leer + sumar + escribir. Aquí es donde los hilos se pisan.
        }
    }

    public static void main(String[] args) throws InterruptedException {
        System.out.println("Esperado siempre: " + (2 * VUELTAS) + " (2 hilos x " + VUELTAS + ")\n");

        for (int intento = 1; intento <= 3; intento++) {
            Contador contador = new Contador();

            // Runnable con lambda (conecta con D4): la tarea que hará cada hilo.
            Runnable tarea = () -> {
                for (int i = 0; i < VUELTAS; i++) {
                    contador.incrementar();
                }
            };

            Thread h1 = new Thread(tarea);
            Thread h2 = new Thread(tarea);
            h1.start();   // start(), NO run(): esto SÍ crea hilos reales que corren en paralelo
            h2.start();
            h1.join();    // esperar a que ambos terminen antes de leer el resultado
            h2.join();

            int obtenido = contador.total;
            String veredicto = (obtenido == 2 * VUELTAS) ? "(coincidió por suerte)" : "(¡se perdieron sumas!)";
            System.out.println("Intento " + intento + ": total = " + obtenido + " " + veredicto);
        }

        System.out.println("\nLección: distintos resultados = no determinismo. NO compartas estado "
                + "mutable a la ligera. (synchronized/AtomicInteger: más adelante.)");
    }
}
