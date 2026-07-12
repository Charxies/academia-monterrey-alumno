package com.taskflow.model;

import java.time.LocalDate;

/**
 * Project — un proyecto dueño de tareas. Es una CLASE (no un record) porque en el
 * dominio es una entidad con identidad propia; hoy es inmutable pero crecerá.
 *
 * Desviación deliberada vs CAPSTONE-SPEC: usamos 'User owner' (objeto) donde el
 * capstone dice 'ownerId'. Se aplana con JPA en S2D4 (el repositorio/BD guarda ids).
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
