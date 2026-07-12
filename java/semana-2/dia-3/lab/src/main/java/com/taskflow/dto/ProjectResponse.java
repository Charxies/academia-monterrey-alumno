package com.taskflow.dto;

import java.time.LocalDate;

/**
 * ProjectResponse — ESQUELETO (integrador, paso 1). Contrato de salida de un proyecto.
 *
 * 'ownerId' (Long) es la forma canónica del capstone; la entidad Project todavía modela 'User owner'
 * como OBJETO (se aplana a Long en D4). El ProjectMapper hará el puente: ownerId = owner.id().
 *
 * Los componentes ya están declarados: tu trabajo es mapear la entidad a este record en ProjectMapper.
 */
public record ProjectResponse(
        Long id,
        String name,
        String description,
        Long ownerId,
        LocalDate createdAt) {
}
