package com.taskflow.model;

/**
 * Describible — contrato PURO: quien lo implemente sabe describirse en una línea.
 * COMPLETO (3 líneas). En MP-6 lo implementan Task (clase) y User (record).
 */
public interface Describible {
    String descripcionCorta();
}
