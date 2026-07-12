package com.taskflow.qa.api.dto;

/**
 * ProjectRequest — cuerpo de POST /projects y PUT /projects/{id} (contrato S2D5).
 * El cliente solo elige name y description; id, ownerId y createdAt los pone la API.
 * name < 3 chars -> la API responde 400 (Bean Validation): base del test de error 400.
 */
public record ProjectRequest(String name, String description) {
}
