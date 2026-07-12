package com.taskflow.practicas;

/**
 * MP-2 — Encapsulación.
 *
 * En model/Task.java: haz TODOS los campos private; genera getters para todos; setters
 * SOLO para status y priority (title/description se fijan al crear). Escribe un par de
 * getters A MANO y luego usa IntelliJ Generate (Alt+Insert / Cmd+N). Override de toString().
 *
 * Aquí (el main):
 *   1. Crea una Task y léela SOLO con getters.
 *   2. Comprueba que el acceso directo a un campo (t.title) YA NO COMPILA -> descomenta
 *      la línea del TODO 2, lee el error "title has private access in ...Task", vuélvela a comentar.
 *   3. Muta lo mutable: t.setStatus(...), t.setPriority(...). Imprime con toString().
 *
 * Correr: mvn -q compile exec:java "-Dexec.mainClass=com.taskflow.practicas.MP02Encapsulacion"
 */
public class MP02Encapsulacion {

    public static void main(String[] args) {
        // TODO 1: crea una Task y muestra t.getTitle(), t.getStatus(), etc.
        // TODO 2 (no compila a propósito): descomenta y lee el error de acceso private:
        //   System.out.println(t.title);
        // TODO 3: t.setPriority(...); t.setStatus(...); System.out.println(t);

        System.out.println("MP-2: pendiente. Encapsula Task y habla con ella solo por getters.");
    }
}
