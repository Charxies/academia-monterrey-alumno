package com.taskflow.practicas;

/**
 * MP-8 — La validación vive DENTRO de Task.
 *
 * En model/Task.java reparte la validación CON INTENCIÓN:
 *   - Invariantes -> en el CONSTRUCTOR (declara 'throws TaskValidationException'):
 *       title obligatorio, ni null ni blank, entre 3 y 120 chars; y project != null.
 *   - Regla temporal -> en la FACTORY estática 'crear(...)': dueDate no en el pasado
 *       (dueDate != null && dueDate.isBefore(LocalDate.now())); null se permite (sin fecha).
 *       crear() valida y DELEGA en el constructor (id nace null, status nace TODO).
 *   ¿Por qué así? El capstone dice "al crear": releer una tarea vieja NO es crearla, por eso
 *   el constructor rehidrata sin la regla de fecha (una tarea vencida debe ser construible).
 *
 * Aquí (el main), cada caso en su try/catch:
 *   1. Task.crear con título "ab" (corto)          -> captura e imprime el mensaje.
 *   2. Task.crear con "x".repeat(121) (largo)       -> ídem.
 *   3. Task.crear con dueDate de ayer               -> ídem (la regla "al crear").
 *   4. El CONSTRUCTOR directo con fecha de ayer SÍ construye (rehidratación) y estaVencida() da true.
 *   5. Un caso VÁLIDO pasa sin excepción.
 * Punto clave: es IMPOSIBLE que quien usa Task cree una inválida.
 *
 * Correr: mvn -q compile exec:java "-Dexec.mainClass=com.taskflow.practicas.MP08Validacion"
 */
public class MP08Validacion {

    public static void main(String[] args) {
        // TODO 1-3: try { Task.crear(...caso inválido...); } catch (TaskValidationException e) {
        //               System.out.println(e.getMessage()); }
        // TODO 4: try { Task vieja = new Task(...dueDate de ayer...); System.out.println(vieja.estaVencida()); }
        //         catch (TaskValidationException e) { ... }
        // TODO 5: un Task.crear(...) VÁLIDO que pase sin excepción.

        System.out.println("MP-8: pendiente. Mete la validación en el constructor y en crear().");
    }
}
