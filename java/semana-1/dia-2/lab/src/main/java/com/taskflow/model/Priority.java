package com.taskflow.model;

/**
 * Priority — prioridad de una tarea. El ORDEN importa (LOW, MED, HIGH): el sort del
 * Día 3 dependerá del orden natural del enum. No lo alteres.
 */
public enum Priority {
    LOW,
    MED,
    HIGH;

    // TODO MP-4: agrega un campo 'etiqueta' con su constructor y getEtiqueta(). Objetivo:
    //   LOW("Baja"), MED("Media"), HIGH("Alta");
    //   private final String etiqueta;
    //   Priority(String etiqueta) { this.etiqueta = etiqueta; }
    //   public String getEtiqueta() { return etiqueta; }
}
