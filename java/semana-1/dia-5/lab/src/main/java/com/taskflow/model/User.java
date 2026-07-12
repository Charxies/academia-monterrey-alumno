package com.taskflow.model;

/**
 * User — datos de un usuario. Es un RECORD: clase de datos inmutable en una línea.
 * Qué REGALA el record: constructor canónico, accessors sin 'get' (u.username()),
 * equals()/hashCode() por valor, y toString() legible. Sin setters: "cambiar" un
 * record = construir otro.
 *
 * Desviación deliberada vs CAPSTONE-SPEC: se omite 'passwordHash' (llega en S2D5,
 * seguridad). Un record TAMBIÉN puede implementar interfaces: aquí, Describible.
 */
public record User(long id, String username, String email, Role role) implements Describible {

    @Override
    public String descripcionCorta() {
        return username + " <" + email + "> · " + role;
    }
}
