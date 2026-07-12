package com.taskflow.practicas;

/**
 * MP-7 — Excepciones: capturar lo MÁS ESPECÍFICO que puedas manejar.
 *
 * Provoca y CAPTURA específicamente 3 excepciones distintas, cada una en su try/catch:
 *   1. NullPointerException      -> lee una celda null de un 'Task[] t = new Task[3];' (t[0].getTitle()).
 *   2. ArrayIndexOutOfBoundsException -> accede a un índice fuera de rango (t[9]).
 *   3. NumberFormatException     -> Integer.parseInt("dos").
 * Agrega un bloque con 'finally' que imprima "esto sale SIEMPRE" (pruébalo con y sin excepción).
 *
 * Error intencional (para LEER, va comentado porque NO compila):
 *   (a) 'catch (Exception e)' ANTES del catch específico -> el compilador marca el segundo
 *       como unreachable.
 *   (b) la variante peor: SOLO catch genérico con cuerpo VACÍO -> el bug desaparece sin
 *       rastro. Regla del día: nunca catch vacío (mínimo imprime el mensaje).
 *
 * Correr: mvn -q compile exec:java "-Dexec.mainClass=com.taskflow.practicas.MP07Excepciones"
 */
public class MP07Excepciones {

    public static void main(String[] args) {
        // TODO 1: Task[] t = new Task[3]; try { t[0].getTitle(); } catch (NullPointerException e) { ... }
        // TODO 2: try { System.out.println(t[9]); } catch (ArrayIndexOutOfBoundsException e) { ... }
        // TODO 3: try { Integer.parseInt("dos"); } catch (NumberFormatException e) { ... }
        // TODO 4: un try/finally donde el finally imprima "esto sale SIEMPRE".
        // TODO 5 (leer, no dejar): escribe (y comenta) un catch (Exception e) ANTES del específico
        //         y observa el error de compilación "unreachable"/"already been caught".

        System.out.println("MP-7: pendiente. Provoca y captura NPE, AIOOBE y NumberFormatException.");
    }
}
