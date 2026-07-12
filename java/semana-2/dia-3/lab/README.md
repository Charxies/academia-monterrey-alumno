# taskflow-api — Semana 2, Día 3 · STARTER

Este es el **punto de partida** del día: el proyecto en el estado de **ayer (S2D2)** — la API de
solo lectura que ya arranca y siembra datos — **más los esqueletos y TODOs de hoy**. Compila y
`mvn test` corre en **verde desde el minuto cero** (los tests-TODO tienen cuerpo vacío: "pasan"
hasta que los llenes; eco del test verde mentiroso de S1D5).

Hoy abres la **puerta de escritura** (POST/PUT/PATCH/DELETE) y, con ella, las cuatro piezas que una
puerta abierta necesita: **DTOs**, **validación** (`@Valid` + Bean Validation), **errores
centralizados** (`@RestControllerAdvice`) y **documentación viva** (Swagger).

## Cómo arrancar

```bash
mvn spring-boot:run     # IntelliJ: ▶ sobre TaskflowApiApplication. "Tomcat started on port 8080".
mvn compile             # debe dar BUILD SUCCESS desde el inicio
mvn test                # verde desde el inicio (los tests de hoy son TODOs vacíos)
```

## Qué ya viene hecho (de D2, no lo reconstruyas)

- Dominio en `model/` + excepciones de S1 (`TaskValidationException`, `TaskNotFoundException`).
- `repository/`: `TaskRepository` (interfaz) + `InMemoryTaskRepository`; `InMemoryProjectRepository`
  (concreto, aún **sin** interfaz).
- `service/`: `TaskService` (crear/completar/listar/porEstado/buscarPorId), `ProjectService`
  (listar/buscarPorId/tareasDe), `TaskOrders`, `ReportService`.
- `controller/`: `TaskController` y `ProjectController` con los **GET** (todavía exponen la ENTIDAD:
  eso se cobra hoy). El endpoint `/reports` de D2 **no está** (una API con DTOs no expone la entidad).
- `config/DataSeeder`: 3 proyectos (uno sin tareas) + 9 tareas, con ≥1 tarea CON y ≥1 SIN assignee.

## Qué tienes que construir hoy (sigue los TODOs numerados en el código)

| Dónde | Qué |
|---|---|
| `pom.xml` | `spring-boot-starter-validation` ya activa; **descomenta** springdoc (2.8.9) en MP-8. |
| `dto/` | Anota `TaskRequest` / `ProjectRequest` / `TaskStatusUpdateRequest` (Bean Validation). |
| `mapper/` | Implementa `TaskMapper` (aEntidadNueva / aReemplazo / aResponse) y `ProjectMapper` (aResponse). |
| `advice/GlobalExceptionHandler` | Los handlers: 404, 400 (dominio), 400 por campo, 400 JSON malformado, 422. |
| `exception/` | Completa `TaskStateException` (422) y `ProjectNotFoundException` (404). |
| `controller/` | Refactoriza los GET a DTOs y añade POST/PUT/PATCH/DELETE (201+`Location`, 204...). |
| `service/` | `TaskService`: crear con `projectId`, reemplazar, eliminar, cambiarStatus (completar delega). `ProjectService`: extrae `ProjectRepository`, crear/reemplazar/eliminar con cascada. |
| `repository/` | Extrae la interfaz `ProjectRepository`; `save` con secuencia-upsert + `deleteById`. |
| `src/test/` | Migra los tests a `@SpringBootTest` + `@AutoConfigureMockMvc` e implementa los TODOs. |
| `postman/` | Añade la carpeta **"Errores"** (415/400/404/422). |

## Errores intencionales del día (los provoca el instructor; tú los vives y los arreglas)

1. POST con la entidad `Task` como `@RequestBody` → Jackson revienta → motiva los DTOs.
2. POST sin `Content-Type: application/json` → **415**.
3. PUT con cuerpo incompleto → reemplaza TODO por diseño → motiva PATCH.
4. `@Valid` olvidado → la validación no corre (silencio) → checklist "todo `@RequestBody` lleva `@Valid`".
5. El advice "tragón" (`Exception.class`) convierte 400/404 de Spring en 500 → maneja lo que conoces.

## Definición de "listo" (DoD)

Compila; los endpoints del capstone (menos `/auth/*`) responden con los códigos correctos; POST
inválido → 400 con `errors[]`, id inexistente → 404, `DONE` sin assignee → **422**, JSON malformado
→ 400; `mvn test` verde (incluidos los tests de escritura que implementaste); `/swagger-ui.html`
operativo; colección Postman con la carpeta "Errores"; commiteado y pusheado.
