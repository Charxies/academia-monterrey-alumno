package com.taskflow.practicas;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * MP-6 — Error intencional #2: el HashSet "roto" por equals SIN hashCode.
 *
 * La entidad TareaEntidad (abajo) YA tiene equals por id, pero le FALTA hashCode. Corre esta
 * clase tal cual y observa el bug:
 *   - equals(t1, t1bis) da true (mismo id)...
 *   - ...pero set.contains(t1bis) da FALSE y set.add(t1bis) ACEPTA el duplicado (size 2).
 * Por qué: HashSet usa PRIMERO hashCode (para la cubeta) y LUEGO equals. Sin hashCode, dos
 * objetos "iguales" caen en cubetas distintas -> el Set no los reconoce.
 *
 *   TODO MP-6: añade el método hashCode() a TareaEntidad (return Objects.hash(id)) y vuelve a
 *              correr: ahora contains da true y el duplicado se rechaza (size 1). Aplica lo
 *              MISMO al Task real en model/Task.java (su propio TODO MP-6).
 *
 * Correr: mvn -q compile exec:java "-Dexec.mainClass=com.taskflow.practicas.MP06HashSetRoto"
 */
public class MP06HashSetRoto {

    public static void main(String[] args) {
        Set<TareaEntidad> set = new HashSet<>();

        TareaEntidad t1 = new TareaEntidad(7L, "Implementar login");
        set.add(t1);

        // Otra instancia con el MISMO id: para el dominio, es "la misma tarea".
        TareaEntidad t1bis = new TareaEntidad(7L, "Implementar login (recargada)");

        System.out.println("equals por id: " + t1.equals(t1bis));         // true
        System.out.println("contains(t1bis): " + set.contains(t1bis));    // ¿true o false? (sin hashCode: false)
        System.out.println("add(t1bis) [duplicado]: " + set.add(t1bis));  // ¿se cuela el duplicado?
        System.out.println("size del set: " + set.size());                // ¿1 o 2?
    }

    /** Entidad mínima con identidad por id. Tiene equals... pero le FALTA hashCode (ese es el TODO). */
    static class TareaEntidad {
        private final Long id;
        private final String title;

        TareaEntidad(Long id, String title) {
            this.id = id;
            this.title = title;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            TareaEntidad otra = (TareaEntidad) o;
            return Objects.equals(id, otra.id);
        }

        // TODO MP-6: añade aquí el hashCode por id -> @Override public int hashCode() { return Objects.hash(id); }

        @Override
        public String toString() {
            return "TareaEntidad{id=" + id + ", title='" + title + "'}";
        }
    }
}
