package com.taskflow.dto;

/**
 * ProjectRequest — ESQUELETO (integrador, paso 1). Contrato de entrada para crear (POST) y reemplazar
 * (PUT) un proyecto. El cliente solo elige nombre y descripción; 'id', 'ownerId' y 'createdAt' los
 * pone el service.
 *
 * TODO (integrador, paso 1): valida 'name' con @NotBlank + @Size(min = 3, max = 80) (mensajes en
 * español). Es una regla NUEVA de la API (no viene de S1, donde Project no validaba el nombre).
 */
public record ProjectRequest(
        String name,
        String description) {
}
