<!-- ============================================================================
     PLANTILLA del guion de demo (MP-6). Rellena cada <TODO> con TUS piezas y tu
     reparto. 10 min de demo + 2 de preguntas.
     Reglas: reparto 50/50 · CAMBIO obligatorio de presentador en el punto 5 ·
     la demo se ensaya CON los datos del guion (siembra con datos-demo, ERR-3).
     Modelo lleno de referencia: ../../solucion/GUION-DEMO-ejemplo.md
     ============================================================================ -->

# Guion de demo — TaskFlow v3.0

- **Pareja:** A = ______________  ·  B = ______________
- **Entorno de esta demo:** [ ] AWS vivo   [ ] compose local
- **Datos:** sembrados con `datos-demo.postman_collection.json` y **verificados con un GET**.
- **Reparto:** A presenta 1–4, B presenta 5–6. **Cambio en el punto 5** (~min 5.5).

## Los 6 puntos

| # | Min | Quién | Qué se dice / se muestra | Pantalla lista |
|---|---|---|---|---|
| 1 | 1.0 | **A** | El problema + qué es TaskFlow (elevator pitch del README). TODO: _______ | README |
| 2 | 2.0 | **A** | Arquitectura con el diagrama del README (capas + Docker + pipeline). TODO: _______ | README (diagrama) |
| 3 | 2.0 | **A** | **Código destacado** — UNA pieza con orgullo, explicada NO leída. TODO elige: regla del owner `@PreAuthorize` / filtro JWT / `ReportService` híbrido / Dockerfile multi-stage → _______ | IDE en _______ |
| 4 | 1.5 | **A → CAMBIO** | Pipeline: el run **verde** del commit del tag `v3.0` + la imagen en GHCR. TODO: _______ | Actions + GHCR |
| 5 | 2.5 | **B** | **API viva**: login → crear tarea → `PATCH /tasks/{id}/status` a `DONE` → verla en `GET /projects/{id}/tasks`, sobre los datos del guion. (stretch: reportes). TODO: _______ | Postman/terminal + Swagger |
| 6 | 1.0 | **B** | Qué sigue: limitaciones conocidas (de las notas de release) + features candidatas (comentarios/etiquetas/notificaciones → S5). TODO: _______ | Notas del Release |

## Datos del guion (TODO confirmar tras sembrar)

- [ ] Usuarios de la historia registrados / login OK.
- [ ] Proyecto **"Sprint demo"** creado; su id = `{{sprintProjectId}}` = ______.
- [ ] 5 tareas variadas (TODO/IN_PROGRESS/DONE); al menos una **con assignee** para el PATCH a DONE.

## Respaldo

- [ ] Demo grabada (≤10 min, pantalla+voz) enlazada en el Release. **Nunca el .mp4 en git.**
- [ ] Si algo muere >60 s en vivo → cambiar a la grabación con calma y seguir el guion.

## Preguntas probables (prepárense LOS DOS — te preguntan lo que NO presentaste)

- ¿Dónde vive la regla del owner y por qué no en un matcher de URL?
- ¿Por qué multi-stage en el Dockerfile?
- ¿Qué pasa si la cobertura baja de 70 %?
- 401 vs 403.
- ¿Por qué su API no usa WebFlux?
