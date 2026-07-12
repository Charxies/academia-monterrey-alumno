package com.taskflow.qa.api.dto;

/**
 * TaskRequest — cuerpo de POST /projects/{projectId}/tasks y PUT /tasks/{id} (contrato S2D5).
 *
 * Decisiones del DTO de test (deliberadas, para no arrastrar dependencias):
 *   - priority y dueDate viajan como String. La API deserializa "HIGH" al enum Priority y
 *     "2026-12-31" a LocalDate. Usar String evita el módulo jackson-jsr310 (no está en el pom)
 *     y deja el JSON idéntico al que manda la UI.
 *   - dueDate y assigneeId son OPCIONALES (null): la regla "dueDate no en pasado" y "no DONE sin
 *     assignee" son del dominio de la API, no del cliente. title 3-120 lo valida la API (400).
 *
 * Campos EXACTOS del contrato: title, description, priority (LOW|MED|HIGH), assigneeId, dueDate.
 */
public record TaskRequest(
        String title,
        String description,
        String priority,
        Long assigneeId,
        String dueDate
) {
}
