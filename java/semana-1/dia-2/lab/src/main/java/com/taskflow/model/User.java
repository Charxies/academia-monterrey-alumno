package com.taskflow.model;

/**
 * User — datos de un usuario. Es un RECORD: inmutable, con accessors sin 'get'
 * (u.username(), no u.getUsername()), y equals/hashCode/toString gratis. Sin setters.
 *
 * Desviación deliberada vs CAPSTONE-SPEC: se omite 'passwordHash' (llega en S2D5).
 *
 * TODO MP-6: haz que User implemente Describible y agrega descripcionCorta():
 *   public record User(...) implements Describible {
 *       @Override public String descripcionCorta() { return username + " <" + email + "> · " + role; }
 *   }
 */
public record User(long id, String username, String email, Role role) {
}
