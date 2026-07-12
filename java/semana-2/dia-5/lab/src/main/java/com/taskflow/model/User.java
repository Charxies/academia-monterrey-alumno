package com.taskflow.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * User — usuario del sistema. Entidad JPA desde D4 (de record a clase). @Table(name = "users") es
 * OBLIGATORIO: 'USER' es palabra RESERVADA en H2 y Postgres (viene de D4).
 *
 * HOY (S2D5, MP-3) gana el campo que faltaba desde S1D2: passwordHash. No fue olvido, fue diseño —
 * un password no se modela hasta que se puede HASHEAR. El campo se llama passwordHash (columna
 * password_hash) y NO 'password' porque el nombre DOCUMENTA que ahí jamás vive un password en claro,
 * solo su hash BCrypt (prefijo $2a$/$2b$).
 */
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String username;

    // TODO MP-3: añade el campo passwordHash (el HASH BCrypt, NUNCA el password en claro):
    //   @com.fasterxml.jackson.annotation.JsonIgnore   // cinturón extra: nunca al JSON (punto de dolor 8)
    //   @Column(name = "password_hash")
    //   private String passwordHash;
    // y su getter getPasswordHash(). AJUSTA el constructor para recibir passwordHash (ver TODO abajo).

    private String email;

    @Enumerated(EnumType.STRING)
    private Role role;

    /** Constructor no-arg protegido: EXCLUSIVO de JPA. */
    protected User() {
        // solo para JPA
    }

    // TODO MP-3: añade passwordHash al constructor:
    //   public User(Long id, String username, String passwordHash, String email, Role role) { ... }
    // (el DataSeeder y AuthService lo usarán con el hash del encoder).
    public User(Long id, String username, String email, Role role) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public Role getRole() {
        return role;
    }

    @Override
    public String toString() {
        // toString NUNCA imprime el hash (buena higiene: nada de credenciales en logs).
        return "User{id=" + id + ", username='" + username + '\'' + ", role=" + role + '}';
    }
}
