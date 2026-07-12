package com.taskflow.exception;

/**
 * TaskValidationException — excepción CHECKED (extiende Exception) para reglas de
 * negocio violadas por input del usuario: algo esperable y recuperable en el CLI.
 * Al ser checked, el compilador OBLIGA a quien crea/valida una Task a decidir qué hacer
 * (el menú: avisar y reintentar) en vez de dejar que el programa reviente.
 *
 * Ya viene COMPLETA. Tu trabajo (MP-8) NO es tocar este archivo, sino LANZARLA desde
 * Task cuando una regla no se cumple:  throw new TaskValidationException("mensaje útil");
 */
public class TaskValidationException extends Exception {

    public TaskValidationException(String mensaje) {
        super(mensaje);
    }
}
