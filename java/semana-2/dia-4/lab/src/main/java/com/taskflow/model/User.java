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
 *
 * ============================================================================================
 * TODO (S2D4, MP-5) — DE RECORD A CLASE @Entity. Un record NO puede ser entidad (campos finales, sin
 * constructor no-arg). Regla del curso: entidades = clases, DTOs = records. Convertir:
 *   - 'public class User' con campos privados (Long id, String username, String email, Role role),
 *     getters, constructor no-arg 'protected' y constructor completo.
 *   - @Entity + @Table(name = "users")  <-- 'users' OBLIGATORIO: 'USER' es palabra RESERVADA en H2 y
 *     Postgres (create table user FALLA en el DDL). @Id + @GeneratedValue(IDENTITY) en 'id';
 *     @Enumerated(EnumType.STRING) en 'role'; @Column(unique = true) en 'username'.
 *   - OJO: los accessors cambian de u.username() (record) a u.getUsername() (clase). passwordHash
 *     NO entra hoy (llega en D5 con PasswordEncoder).
 * ============================================================================================
 */
public record User(long id, String username, String email, Role role) {
}
