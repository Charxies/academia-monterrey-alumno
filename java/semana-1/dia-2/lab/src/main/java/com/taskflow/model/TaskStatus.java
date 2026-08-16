package com.taskflow.model;

/**
 * TaskStatus — los 3 estados posibles de una tarea. Conjunto CERRADO que vigila el
 * compilador (adiós a los String mágicos de D1: nadie mete un "DONEE").
 */
public enum TaskStatus {
    TODO,
    IN_PROGRESS,
    DONE;

    // TODO MP-4: agrega un campo 'etiqueta' con su constructor y getEtiqueta(). Objetivo:
    //   TODO("Por hacer"), IN_PROGRESS("En curso"), DONE("Hecha");
    private final String etiqueta;

      TaskStatus(String etiqueta) {
          this.etiqueta = etiqueta;
      }
    public String getEtiqueta() {
          return etiqueta;
      }
    // (Recuerda: la lista de constantes va PRIMERO y termina en ';' antes de los campos.)
}
