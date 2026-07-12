package com.taskflow.practicas;

/**
 * MP-6 — record + interface (polimorfismo SIN herencia).
 *
 * Paso 1 (en model/): el enum Role ya está; haz que el record User implemente Describible
 * (ver el TODO en User.java) y que la clase Task también implemente Describible con
 * descripcionCorta().
 *
 * Aquí (el main):
 *   1. Crea un User y comprueba los "regalos" del record: toString gratis, accessor SIN get
 *      (u.username(), no getUsername()), y que NO tiene setters (para "cambiar", construyes otro).
 *   2. Polimorfismo por interfaz: arma 'Describible[] cosas = { unaTask, unUser };' y recórrelo
 *      imprimiendo d.descripcionCorta() — una clase y un record bajo el MISMO contrato.
 *
 * Correr: mvn -q compile exec:java "-Dexec.mainClass=com.taskflow.practicas.MP06RecordUser"
 */
public class MP06RecordUser {

    public static void main(String[] args) {
        // TODO 1: User ada = new User(1L, "ada", "ada@taskflow.dev", Role.ADMIN);
        //         System.out.println(ada);            // toString gratis
        //         System.out.println(ada.username()); // accessor sin 'get'
        // TODO 2: Describible[] cosas = { unaTask, ada };
        //         for (Describible d : cosas) System.out.println(d.descripcionCorta());

        System.out.println("MP-6: pendiente. Implementa Describible en User y Task.");
    }
}
