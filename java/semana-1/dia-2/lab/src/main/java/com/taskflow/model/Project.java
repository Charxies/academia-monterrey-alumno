package com.taskflow.model;

 import java.time.LocalDate;   // lo necesitarás para createdAt

/**
 * Project — un proyecto dueño de tareas. Es una CLASE (no un record). Se completa en el
 * INTEGRADOR. Compila vacío por ahora.
 *
 * Desviación deliberada vs CAPSTONE-SPEC: usamos 'User owner' (objeto) donde el capstone
 * dice 'ownerId'. Se aplana con JPA en S2D4.
 *
 * TODO Integrador:
 *   - Campos: long id, String name, String description, User owner, LocalDate createdAt.
 *   - Constructor que los reciba todos + getters.
 *   - toString() (STRETCH: que incluya al owner, no solo su id).
 */
public class Project {
    private long id;
    private String name;
    private String description;
    private User owner;
    private LocalDate createdAt;

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

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }
// TODO Integrador: declara los campos, el constructor, los getters y toString().
@Override
public String toString() {
    return "Project{id=" + id
            + ", name='" + name + "'"
            + ", owner=" + owner.descripcionCorta()
            + ", createdAt=" + createdAt + "}";
}

}
