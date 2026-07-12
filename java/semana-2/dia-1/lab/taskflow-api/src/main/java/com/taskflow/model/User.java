package com.taskflow.model;

/**
 * User — datos de un usuario. Es un RECORD: clase de datos inmutable en una línea.
 * Qué REGALA el record: constructor canónico, accessors sin 'get' (u.username()),
 * equals()/hashCode() por valor, y toString() legible. Sin setters: "cambiar" un
 * record = construir otro.
 *
 * AJUSTE AL COPIAR DE S1 (paso 1 del integrador S2D1): se quitó "implements Describible".
 * Describible era el contrato del CLI de S1 (polimorfismo por interfaz para el menú) y NO viaja
 * a la API — sin ese ajuste el proyecto nuevo no compilaría (la interfaz no se copió).
 *
 * Desviación deliberada vs CAPSTONE-SPEC: se omite 'passwordHash' (llega en S2D5, seguridad).
 */
public record User(long id, String username, String email, Role role) {
}
