package com.taskflow.model;

/**
 * Describible — contrato PURO: quien lo implemente sabe describirse en una línea.
 *
 * Lo implementan tanto Task (una clase) como User (un record): eso permite tratarlos
 * de forma uniforme —polimorfismo POR INTERFAZ, sin herencia de por medio—.
 */
public interface Describible {
    String descripcionCorta();
}
