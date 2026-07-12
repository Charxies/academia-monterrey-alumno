# Feature del capstone — A) Comentarios en tareas

> **La más pautada del menú — el default si la pareja duda.** Esta es la spec CERRADA: el alcance de
> abajo es el alcance ÚNICO. Los **recortes** no son opcionales de quitar: son la definición de "fuera
> de alcance" que copias tal cual a tu `docs/feature-spec.md` en la Fase 1. Cualquier divergencia se
> resuelve a favor del apéndice de la spec del día.
>
> **Por qué cabe en el bloque:** cero relaciones nuevas complejas (N–1 con `Task`, como ya tienes con
> `Project`), cero dependencias nuevas, patrón CRUD que ya escribiste tres veces en S2. Todo lo que
> sigue reutiliza tu arquitectura de S1–S4: `@RestControllerAdvice`/`GlobalExceptionHandler`, auth JWT
> heredada, DTOs como `record`, validación Jakarta. **No inventes infraestructura: extiende la tuya.**

---

## 1. Qué y por qué

Permitir que un usuario autenticado deje comentarios en una tarea y los lea en orden cronológico
inverso. Valor: trazabilidad de la conversación alrededor de una tarea, sin salir de TaskFlow.

## 2. Modelo — `Comment`

| Campo | Tipo | Nota |
|---|---|---|
| `id` | `Long` | generado por la BD (`@GeneratedValue`), nace `null` |
| `taskId` | `Long` | N–1 con `Task`; la tarea DEBE existir |
| `authorId` | `Long` | **el usuario autenticado** — NO viene en el request; sale del `SecurityContext`/principal |
| `body` | `String` | 1–500 chars, obligatorio |
| `createdAt` | `Instant` | lo asigna el **servidor** al crear, nunca el cliente |

> **Decisión de modelado (la que Copilot inventaría mal):** guarda `taskId` como `Long` (coherente con
> el aplanado objeto→id de S1D3), no un `@ManyToOne Task task`. Si prefieres la asociación JPA, es tu
> decisión de arquitectura — pero decídela tú en la espec, no la aceptes porque el chat la escupió.

## 3. Endpoints

| Método y ruta | Auth | Request | Respuestas |
|---|---|---|---|
| `POST /tasks/{id}/comments` | JWT | `{"body": "..."}` | **201** con `CommentResponse`; **404** si la task no existe; **400** si `body` inválido (vacío o >500) |
| `GET /tasks/{id}/comments` | JWT | — | **200** lista ordenada por `createdAt` **desc**; **200 `[]`** si no hay; **404** si la task no existe |
| `DELETE /comments/{id}` | JWT | — | **204**; **403** si no eres el autor ni `ADMIN`; **404** si el comentario no existe |

**DTOs (records, convención D2):**

- `CommentRequest(String body)` — con `@NotBlank` y `@Size(min = 1, max = 500)`, validado con `@Valid`.
- `CommentResponse(Long id, Long taskId, Long authorId, String body, Instant createdAt)`.

## 4. Reglas de negocio (y de dónde salen)

- **`body` 1–500 chars, obligatorio** — validación Jakarta en el `record` de request (patrón de S2D3).
- **`authorId` = principal autenticado** — se toma del contexto de seguridad de S2 (JWT), NUNCA del
  body. Un cliente no puede comentar "en nombre de" otro.
- **No se comenta una tarea inexistente** — si `taskId` no existe → `TaskNotFoundException` → el
  `GlobalExceptionHandler` de S2D3 la mapea a **404**. Reutilizas la excepción, no creas una nueva.
- **Borrado restringido** — solo el `authorId` del comentario o un `ADMIN` puede borrar; si no, **403**.

## 5. Recortes predefinidos — FUERA DE ALCANCE (cópialos a tu espec, no los quites)

- ❌ Sin edición (`PUT /comments/{id}`).
- ❌ Sin paginación (la lista devuelve todo; en el alcance mínimo una tarea tiene pocos comentarios).
- ❌ Sin respuestas anidadas / hilos.
- ❌ Sin markdown, menciones (`@usuario`) ni notificaciones al comentar.

> **Timer de recorte (min 30 de PM-1):** si a esa hora no tienes al menos `POST` respondiendo 201 en
> local, el recorte que impone el instructor es: **quedarte solo con `POST` + `GET`** y dejar `DELETE`
> (con su 403) como stretch. El DoD (a)/(b) se cumple con `POST` + `GET` + sus tests.

## 6. Criterios de aceptación (dado / cuando / entonces — cada uno es un test)

- **Dado** una task existente y un usuario autenticado, **cuando** `POST /tasks/{id}/comments` con
  `body` válido, **entonces** 201 y el `CommentResponse` trae `authorId` = usuario autenticado y
  `createdAt` asignado por el servidor.
- **Dado** un `body` vacío o de 501 chars, **cuando** `POST`, **entonces** 400 (no se persiste nada).
- **Dado** un `taskId` inexistente, **cuando** `POST` o `GET`, **entonces** 404.
- **Dado** dos comentarios en tiempos distintos, **cuando** `GET`, **entonces** el más reciente va
  primero (orden `createdAt` desc).
- **Dado** un comentario de otro usuario y tú sin rol `ADMIN`, **cuando** `DELETE /comments/{id}`,
  **entonces** 403 y el comentario sigue existiendo.

## 7. Test canónico en `taskflow-qa` (RestAssured, ≥1 verde en local)

**Roundtrip:** `POST` un comentario → `GET` la lista de esa tarea → la respuesta **contiene** el
comentario con el `body` enviado y el `authorId` correcto.

```
given auth(JWT) body({"body":"revisar el borde de dueDate"})
when  POST /tasks/{id}/comments      -> 201, capturar id
when  GET  /tasks/{id}/comments      -> 200
then  la lista contiene un item con body == "revisar el borde de dueDate" y authorId == usuario
```

## 8. Presupuesto de tiempo (para que quepa en el bloque)

| Fase | Qué | Min |
|---|---|---:|
| F1 | Espec en `docs/feature-spec.md` (copia §2–§6) | 20 |
| F2 | Plan con chat + auditarlo contra tu arquitectura | 20 |
| F3 | Rama, entidad `Comment` + repo, esqueleto, commit | 10 + 30 |
| F4 | Tests unit/integración (con checklist D3 + sabotaje) | 20 |
| F5 | 1 test API RestAssured en `taskflow-qa` | 15 |
| F6 | OpenAPI de los endpoints + sección en README | 10 |
| F7 | PR + doble review + merge con pipeline verde | 15 |

Total ≈ **140 min netos** — cabe en AM-2 (F1–F3 arranque) + PM-1, con la comida de por medio.

## 9. Reuso obligatorio (no recodifiques lo que ya tienes)

- `GlobalExceptionHandler` (`@RestControllerAdvice`) de S2D3 → 404/400/403. **No** metas `try/catch`
  en el controller.
- Auth JWT de S2 → el principal autenticado; de ahí sale `authorId`.
- Patrón mapper estático (`CommentMapper.aResponse(...)`) como `TaskMapper` de S2D3.
- `CommentRepository extends JpaRepository<Comment, Long>` con **query derivada**
  `findByTaskIdOrderByCreatedAtDesc(Long taskId)` — **sin SQL nativo concatenado** (ver la kata de
  MP-2: el `LIKE '%"+x+"%'` es exactamente lo que NO se hace).
