package com.taskflow.mapper;

/**
 * ProjectMapper — ESQUELETO (integrador, paso 1). Puente DTO <-> dominio del lado Project. Estático,
 * a mano, sin MapStruct (igual que TaskMapper).
 *
 * TODO (integrador, paso 1): implementa aResponse(Project p) — aplana el objeto 'User owner' de la
 * entidad al 'Long ownerId' del contrato:
 *     Long ownerId = p.getOwner() == null ? null : p.getOwner().id();
 *     return new ProjectResponse(p.getId(), p.getName(), p.getDescription(), ownerId, p.getCreatedAt());
 *
 * La CONSTRUCCIÓN de la entidad (owner semilla + createdAt) vive en ProjectService, porque necesita
 * datos que el request no trae.
 */
public final class ProjectMapper {

    private ProjectMapper() {
        // no instanciable
    }
}
