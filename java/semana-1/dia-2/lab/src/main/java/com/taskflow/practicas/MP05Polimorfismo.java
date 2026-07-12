package com.taskflow.practicas;

/**
 * MP-5 — Herencia + polimorfismo (jerarquía DIDÁCTICA; NO entra al capstone hoy).
 *
 * Construye TODO en ESTE archivo, con clases auxiliares NO PÚBLICAS abajo (decisión
 * deliberada: no regar archivos por una demo):
 *
 *   abstract class Notificacion {
 *       protected final String destinatario;
 *       Notificacion(String destinatario) { this.destinatario = destinatario; }
 *       abstract void enviar(String mensaje);           // contrato: cada hija lo implementa
 *       String encabezado() { return "[Para " + destinatario + "]"; }  // concreto, compartido
 *   }
 *   class NotificacionEmail extends Notificacion { ... @Override void enviar(...) {...} }
 *   class NotificacionConsola extends Notificacion { ... @Override void enviar(...) {...} }
 *
 * En el main: arma 'Notificacion[] canales = { new NotificacionEmail(...), new NotificacionConsola(...) };'
 * y recórrelo llamando enviar(...) -> el DISPATCH DINÁMICO se ve (cada objeto imprime distinto).
 * Demuestra también que 'new Notificacion(...)' NO compila (clase abstracta) y usa @Override SIEMPRE.
 *
 * Correr: mvn -q compile exec:java "-Dexec.mainClass=com.taskflow.practicas.MP05Polimorfismo"
 */
public class MP05Polimorfismo {

    public static void main(String[] args) {
        // TODO 1: declara las clases abstract Notificacion + NotificacionEmail + NotificacionConsola
        //         (abajo, fuera de esta clase pública).
        // TODO 2: crea Notificacion[] canales = { ... } y recórrelo llamando n.enviar("...").
        // TODO 3: descomenta 'new Notificacion("x")' para leer el error "is abstract; cannot be instantiated".

        System.out.println("MP-5: pendiente. Crea la jerarquía Notificacion y ve el dispatch dinámico.");
    }
}

// TODO MP-5: aquí abajo van tus clases auxiliares (no públicas): Notificacion (abstract),
//            NotificacionEmail y NotificacionConsola. Recuerda @Override en cada enviar(...).
