package com.taskflow.model;

import java.time.LocalDate;

/**
 * Project — un proyecto dueño de tareas. Es una CLASE (no un record) porque en el
 * dominio es una entidad con identidad propia; hoy es inmutable pero crecerá.
 *
 * Desviación deliberada vs CAPSTONE-SPEC: usamos 'User owner' (objeto) donde el
 * capstone dice 'ownerId'. Se aplana con JPA en S2D4 (el repositorio/BD guarda ids).
 *
 * ============================================================================================
 * TODO (S2D4, MP-4) — CONVERTIR EN @Entity y APLANAR el owner (pagar la promesa de S1D2):
 *   - @Entity + @Table(name = "projects"); @Id + @GeneratedValue(strategy = IDENTITY) sobre 'id';
 *     constructor 'protected Project() {}' para JPA; @Column(nullable = false) en 'name'.
 *   - APLANAR: 'User owner' -> 'Long ownerId' (forma canónica: id, name, description, ownerId,
 *     createdAt). El id 'long' primitivo pasa a 'Long' (nace null; lo asigna la BD).
 *   - Ajustar en consecuencia: ProjectMapper.aResponse (usa p.getOwnerId(), ya no p.getOwner().id()),
 *     ProjectService.crear/reemplazar (manejan un Long ownerId, no un objeto User) y el DataSeeder.
 * ============================================================================================
 */
public class Project {

    private final long id;
    private final String name;
    private final String description;
    private final User owner;
    private final LocalDate createdAt;

    public Project(long id, String name, String description, User owner, LocalDate createdAt) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.owner = owner;
        this.createdAt = createdAt;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public User getOwner() {
        return owner;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    @Override
    public String toString() {
        // STRETCH: el toString incluye al owner (no solo su id) para leerlo de un vistazo.
        return "Project{id=" + id + ", name='" + name + '\''
                + ", owner=" + (owner == null ? "sin owner" : owner.username())
                + ", createdAt=" + createdAt + '}';
    }
}
