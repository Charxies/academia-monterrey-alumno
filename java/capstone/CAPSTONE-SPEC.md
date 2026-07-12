# Capstone — "TaskFlow" API

Proyecto único que crece las 5 semanas. Cada día lo avanza un poco. Sirve de hilo conductor y de portafolio para el alumno.

## Dominio

Gestor de tareas por proyecto (como un Trello mínimo, sin frontend).

### Entidades

| Entidad | Campos | Relaciones |
|---|---|---|
| `User` | id, username, passwordHash, email, role (`USER`/`ADMIN`) | 1–N con Task (asignado) |
| `Project` | id, name, description, ownerId, createdAt | 1–N con Task |
| `Task` | id, title, description, status (`TODO`/`IN_PROGRESS`/`DONE`), priority (`LOW`/`MED`/`HIGH`), projectId, assigneeId, dueDate | N–1 con Project y User |

### Reglas de negocio (dan pie a excepciones/validación)

- Un `Task` no puede existir sin `Project`.
- `title` obligatorio, 3–120 chars.
- No se puede pasar a `DONE` una tarea sin `assignee`.
- Solo `ADMIN` o el `owner` del proyecto puede borrar el proyecto.
- `dueDate` no puede ser en el pasado al crear.

## Evolución por semana

| Semana | Estado del capstone |
|---|---|
| S1 | App de **consola** en Java puro: modelos, repositorio en memoria + persistencia a archivo, reportes con streams, tests JUnit |
| S2 | **API REST Spring Boot**: CRUD de Project/Task, DTOs+validación, Swagger, persistencia JPA (S2D4 usa **H2 archivo**; el swap a Postgres vía Docker se formaliza en S3D2), login JWT + roles |
| S3 | Testeada (coverage ≥70%), **dockerizada** (compose con Postgres), desplegada en **AWS/RDS** con **CI/CD** |
| S4 (QE) | Suite de automatización: **RestAssured** contra sus endpoints + **Selenium/POM** contra la UI provista |
| S5 (Copilot) | Feature nueva end-to-end con **GitHub Copilot** (ej: comentarios en tareas, etiquetas, o notificaciones) + reporte de aceleración |

## Endpoints (meta al final de S2)

```
POST   /auth/register
POST   /auth/login            -> JWT
GET    /projects              (auth)
POST   /projects
GET    /projects/{id}
PUT    /projects/{id}
DELETE /projects/{id}         (owner/ADMIN)
GET    /projects/{id}/tasks
POST   /projects/{id}/tasks
PUT    /tasks/{id}
PATCH  /tasks/{id}/status
DELETE /tasks/{id}
```

## Recursos provistos (en `recursos/`)

- `taskflow-ui/` — mini-frontend estático (HTML+JS) para automatizar en semana QE. **No se enseña a construirlo**; se entrega listo.
- Colección Postman + datos semilla.
- Repo plantilla con `.gitignore`, `pom.xml` base y CI.

> Feature nueva de S5 (Copilot) se elige entre: **comentarios en tareas**, **etiquetas/tags**, o **notificaciones por email** — la que el instructor prefiera para demostrar el flujo completo asistido por IA.

## Apéndice — Modelo canónico de `Task` en S1 (consola)

> Fija la forma ÚNICA de `Task` en la cadena S1D2→S1D5. Las specs diarias la referencian; cualquier divergencia entre días se resuelve a favor de este apéndice.

Desde S1D3 (tras el **puente objeto→id** del warm-up: `Project project` → `Long projectId`, `User assignee` → `Long assigneeId` — *"el repositorio guarda ids, como hará la BD en S2"*) y hasta el final de S1, `Task` tiene exactamente estos 8 campos — son las 8 columnas del CSV de S1D5:

| Campo | Tipo | Nota |
|---|---|---|
| `id` | `Long` | nace en S1D2 siempre `null`; lo asigna el repositorio (S1D3) |
| `title` | `String` | obligatorio, 3–120 chars (valida el constructor) |
| `description` | `String` | opcional |
| `status` | `TaskStatus` | `TODO` / `IN_PROGRESS` / `DONE` |
| `priority` | `Priority` | declarado en orden `LOW, MED, HIGH` (el sort de S1D3 depende de él) |
| `projectId` | `Long` | en S1D2 es objeto `Project project`; S1D3 lo aplana |
| `assigneeId` | `Long` | ídem con `User assignee`; opcional (regla: no `DONE` sin assignee) |
| `dueDate` | `LocalDate` | opcional (`null` = sin fecha) |

Decisiones canónicas asociadas (valen para toda S1):
- Excepción de validación: **`com.taskflow.exception.TaskValidationException`** (checked) — único nombre y paquete en S1.
- Excepción de búsqueda fallida: **`com.taskflow.exception.TaskNotFoundException`** (unchecked, constructor con el `id`) — nace en S1D4 con la cirugía de `Optional`/`orElseThrow`, forma parte del estado final de S1 (S1D5) y viaja a S2 tal cual (el advice de S2D3 la mapea a 404).
- **Firma canónica de la factory** (fijada desde S1D3, la usan D1/D3 de S2 tal cual): `Task.crear(String title, String description, Priority priority, LocalDate dueDate, Long projectId, Long assigneeId)` — `id` nace `null`, `status` nace `TODO`; valida título 3–120, `projectId` obligatorio ("no Task sin Project") y `dueDate` no en el pasado; `assigneeId` es opcional (`null` = sin asignar). Callers canónicos: el menú de la consola S1 pasa `projectId = 1L` y `assigneeId = null`; `TaskService.crear` de S2D1 ídem; `TaskMapper.aEntidadNueva` de S2D3 pasa el `projectId` del path y el `assigneeId` del `TaskRequest`.
- **Creación vs rehidratación:** la regla "dueDate no puede ser en el pasado **al crear**" vive SOLO en la factory estática `Task.crear(...)` (la usa el menú al agregar); el constructor rehidrata datos existentes sin esa regla (precargas de S1D2/D3, `SeedData` de S1D4, `CsvTaskParser.parse` de S1D5) — así una tarea vencida es construible y `estaVencida()` tiene sentido.
- Repositorio S1: clase concreta `InMemoryTaskRepository` (S1D3; su `save` con id explícito hace upsert Y avanza la secuencia al máximo id visto), gana `Optional<Task>` en `findById` en S1D4; la interfaz `TaskRepository` se extrae hasta S1D5 (integrador, paso 1).
