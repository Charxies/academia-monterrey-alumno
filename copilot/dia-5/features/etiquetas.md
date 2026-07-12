# Feature del capstone — B) Etiquetas (tags)

> **Intermedia del menú — relación N–M.** Spec CERRADA: el alcance de abajo es el único. Los
> **recortes** se copian tal cual a tu `docs/feature-spec.md` en la Fase 1. Cualquier divergencia se
> resuelve a favor del apéndice de la spec del día.
>
> **Por qué es la intermedia:** la novedad es la **relación N–M** (`task_tags`) y la **semántica de
> reemplazo del `PUT`** — dos decisiones de diseño que el chat suele resolver de más de una forma, así
> que tú las fijas en la espec. Cero dependencias nuevas; extiende el endpoint de listado de tareas que
> ya tienes de S2.

---

## 1. Qué y por qué

Clasificar tareas con etiquetas reutilizables y poder filtrar las tareas de un proyecto por una
etiqueta. Valor: cortar el ruido de un proyecto grande ("muéstrame solo lo de `backend`").

## 2. Modelo — `Tag` (+ relación N–M con `Task`)

| Campo | Tipo | Nota |
|---|---|---|
| `id` | `Long` | generado por la BD |
| `name` | `String` | 2–30 chars, **único case-insensitive**, **normalizado a lowercase** al guardar |

- **Relación N–M con `Task`** vía tabla intermedia `task_tags` (`task_id`, `tag_id`). En JPA:
  `@ManyToMany` desde `Task` (o desde `Tag`, decide el dueño de la relación en la espec).

> **Decisión de modelado (la que Copilot inventaría mal):** la unicidad es **case-insensitive**. Guarda
> siempre `name.toLowerCase().trim()` y define un índice único sobre esa columna. Si el chat te propone
> `@Column(unique = true)` sin normalizar, `Backend` y `backend` entrarían como dos tags — audita eso.

## 3. Endpoints

| Método y ruta | Auth | Request | Respuestas |
|---|---|---|---|
| `POST /tags` | JWT | `{"name": "backend"}` | **201** con `TagResponse`; **409** si el nombre ya existe (case-insensitive); **400** si `name` inválido (fuera de 2–30) |
| `GET /tags` | JWT | — | **200** lista de tags |
| `PUT /tasks/{id}/tags` | JWT | `{"tagIds": [1, 2]}` | **200** (o 204) — **reemplaza el set completo**; **400** si algún id no existe o si son **> 5**; **404** si la task no existe |
| `GET /projects/{id}/tasks?tag={name}` | JWT | — | **200** tareas del proyecto que tienen esa etiqueta (extiende el endpoint existente de S2) |

**DTOs (records):**

- `TagRequest(String name)` — `@NotBlank`, `@Size(min = 2, max = 30)`.
- `TagResponse(Long id, String name)`.
- `TaskTagsRequest(List<Long> tagIds)` — validación de tamaño en el service (≤ 5), no solo anotación.

## 4. Reglas de negocio (y de dónde salen)

- **`name` único case-insensitive** → antes de crear, `existsByNameIgnoreCase(...)`; si existe → **409**.
- **`name` normalizado a lowercase** al persistir (regla de negocio en el service/entidad, no en el
  controller).
- **Máximo 5 tags por task** — validación en el service; si el `PUT` trae 6 ids → **400**.
- **Semántica PUT = reemplazo total** — el set enviado **sustituye** el actual; NO es un delta
  (no agrega ni quita incremental). Set vacío `{"tagIds": []}` → la task queda sin tags.
- **Todos los `tagIds` deben existir** — si uno no existe → **400** y no se aplica nada (atómico).
- **La task debe existir** → si no, `TaskNotFoundException` → **404** vía `GlobalExceptionHandler`.

## 5. Recortes predefinidos — FUERA DE ALCANCE (cópialos a tu espec, no los quites)

- ❌ Sin colores ni metadatos de la etiqueta.
- ❌ Sin renombrar (`PUT /tags/{id}`) ni borrar tags (`DELETE /tags/{id}`).
- ❌ Sin autocompletar / búsqueda de tags por prefijo.
- ❌ Filtro por **una sola** etiqueta (sin combinaciones AND/OR de varias).

> **Timer de recorte (min 30 de PM-1):** si a esa hora no tienes `POST /tags` + `PUT /tasks/{id}/tags`
> respondiendo, el recorte impuesto es: **dejar fuera el filtro** `?tag={name}` (F5/F6) y cerrar la
> feature con crear-tag + asignar-tags + sus tests. El roundtrip de QA se ajusta a lo que quede vivo.

## 6. Criterios de aceptación (dado / cuando / entonces — cada uno es un test)

- **Dado** ninguna tag `backend`, **cuando** `POST /tags {"name":"Backend"}`, **entonces** 201 y el
  `name` guardado es `backend` (lowercase).
- **Dado** que existe `backend`, **cuando** `POST /tags {"name":"BACKEND"}`, **entonces** **409**.
- **Dado** una task y 2 tags existentes, **cuando** `PUT /tasks/{id}/tags {"tagIds":[1,2]}`,
  **entonces** 200 y la task queda con exactamente esas 2 tags.
- **Dado** una task con 2 tags, **cuando** `PUT` con `{"tagIds":[3]}`, **entonces** la task queda con
  **solo** la tag 3 (reemplazo, no suma).
- **Dado** 6 tagIds, **cuando** `PUT`, **entonces** **400** y el set de la task no cambia.
- **Dado** tareas etiquetadas, **cuando** `GET /projects/{id}/tasks?tag=backend`, **entonces** 200 con
  solo las tareas que tienen `backend`.

## 7. Test canónico en `taskflow-qa` (RestAssured, ≥1 verde en local)

**Roundtrip:** crear tag → asignarla a una task → filtrar las tareas del proyecto por esa tag → la
task aparece.

```
POST /tags {"name":"backend"}                 -> 201, capturar tagId
PUT  /tasks/{taskId}/tags {"tagIds":[tagId]}  -> 200
GET  /projects/{projectId}/tasks?tag=backend  -> 200
then la respuesta contiene la task {taskId}
```

## 8. Presupuesto de tiempo (para que quepa en el bloque)

| Fase | Qué | Min |
|---|---|---:|
| F1 | Espec (copia §2–§6) — clava la semántica PUT y el normalizado | 20 |
| F2 | Plan con chat + auditar (¿propone delta o reemplazo? corrígelo) | 20 |
| F3 | Rama, `Tag` + `task_tags`, esqueleto, commit | 10 + 30 |
| F4 | Tests (borde de 5 tags, reemplazo, 409 case-insensitive) + sabotaje | 20 |
| F5 | 1 test API RestAssured | 15 |
| F6 | OpenAPI + README | 10 |
| F7 | PR + doble review + merge | 15 |

Total ≈ **140 min netos** — cabe en AM-2 + PM-1.

## 9. Reuso obligatorio (no recodifiques lo que ya tienes)

- `GlobalExceptionHandler` de S2D3 → 400/404/409 (mapea una `TagConflictException` propia a 409 en el
  advice, no con `try/catch` en el controller).
- El endpoint `GET /projects/{id}/tasks` de S2 **se extiende** con el query param `tag` — no crees un
  controller nuevo.
- Repos con **queries derivadas** de Spring Data (`existsByNameIgnoreCase`, `findByNameIgnoreCase`),
  **nunca** SQL nativo concatenado con el `name` del usuario (la kata de MP-2 es justo ese anti-patrón).
