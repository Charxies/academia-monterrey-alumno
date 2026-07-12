package com.taskflow.mapper;

import com.taskflow.dto.ProjectResponse;
import com.taskflow.model.Project;

/**
 * ProjectMapper — puente DTO <-> dominio del lado Project. Estático, a mano, sin MapStruct (igual que
 * TaskMapper).
 *
 * El único mapeo con "truco" es aResponse: aplana el objeto 'User owner' de la entidad al 'Long
 * ownerId' del contrato (owner.id()). En D4, cuando la entidad guarde ownerId directo, este mapper
 * se simplifica. La CONSTRUCCIÓN de la entidad (owner semilla + createdAt) vive en ProjectService,
 * porque necesita datos que el request no trae.
 */
public final class ProjectMapper {

    private ProjectMapper() {
        // no instanciable
    }

    /** Entidad -> DTO de salida. Deriva ownerId del objeto owner (owner.id()). */
    public static ProjectResponse aResponse(Project p) {
        Long ownerId = p.getOwner() == null ? null : p.getOwner().id();
        return new ProjectResponse(p.getId(), p.getName(), p.getDescription(), ownerId, p.getCreatedAt());
    }
}
