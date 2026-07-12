# Plantilla de especificación de feature

> **Cópiala a `specs/<feature>.md` de TU `taskflow-api` y llénala ANTES del primer prompt al agente.**
> La espec obliga a decidir lo que el agente **no puede adivinar** (¿tareas de quién? ¿qué orden?
> ¿`dueDate == hoy` cuenta como vencida?). Los `<placeholders>` se sustituyen; los comentarios
> `<!-- ... -->` se borran.
>
> **Regla del día:** el commit de la espec va **antes** que el primer prompt. Los timestamps son la
> evidencia (DoD del integrador). Una espec vaga produce un diff vago que no sabrás revisar.

---

## Feature: `<MÉTODO> <ruta>`  <!-- p.ej. GET /tasks/overdue -->

### 1. Qué y por qué (1–2 líneas)
<!-- El valor de negocio, no la implementación. "Listar las tareas vencidas para que el usuario las
     atienda primero." -->

### 2. Contrato HTTP

| Aspecto | Decisión |
|---|---|
| Método y ruta | `<GET /tasks/overdue>` |
| Auth | `<JWT requerida / público>` |
| Query params | `<ninguno / ?days=N ...>` |
| Cuerpo de request | `<ninguno / DTO>` |
| Respuesta 200 | `<DTO canónico — p.ej. List<TaskResponse>>` |
| Respuesta lista vacía | `<200 con []  — NO 404>` |
| Otros códigos | `<400/401/404 según aplique>` |

### 3. Reglas de negocio y de dónde salen
<!-- Nombra la regla Y su fuente (S1/S2, la clase, el método). Si reusas lógica existente, DILO:
     el agente debe REUSAR, no recodificar. -->

- `<regla 1 — p.ej. "una tarea está vencida si dueDate quedó en el pasado y status != DONE">` —
  fuente: `<Task.estaVencida() de S1 — se REUTILIZA, no se reescribe el criterio>`
- `<regla 2 — orden de salida>` — fuente: `<...>`

### 4. Decisiones de borde (las que el agente inventaría mal)
<!-- Aquí es donde se ganan o pierden los bugs que ni Copilot ni tu pareja cachan. Sé explícito. -->

- **`<borde 1>`** → `<decisión>`  <!-- p.ej. "dueDate == hoy → NO vencida (isBefore es estricto, consistente con estaVencida())" -->
- **`<borde 2>`** → `<decisión>`  <!-- p.ej. "dueDate == null → NO vencida" -->
- **`<borde 3>`** → `<decisión>`  <!-- p.ej. "tarea DONE aunque su fecha pasó → NO aparece" -->
- **Zona horaria / reloj:** `<qué reloj — LocalDate.now() del servidor; implicación conocida>`

### 5. Criterios de aceptación (dado / cuando / entonces)
<!-- Cada uno es un test. Escríbelos como el contrato del endpoint. -->

- **Dado** `<estado inicial>` **cuando** `<acción>` **entonces** `<resultado observable>`.
  <!-- Dado tareas con dueDate pasado y status TODO/IN_PROGRESS, cuando GET /tasks/overdue, entonces
       200 con esas tareas ordenadas por dueDate ascendente. -->
- **Dado** una tarea con `dueDate == hoy` **cuando** `<GET /tasks/overdue>` **entonces** `<no aparece>`.
- **Dado** una tarea vencida pero **DONE** **cuando** `<...>` **entonces** `<no aparece>`.
- **Dado** ninguna tarea vencida **cuando** `<...>` **entonces** `200 []`.
- **Dado** sin token JWT **cuando** `<...>` **entonces** `401`.

### 6. Qué NO toca esta feature (restricciones para el agente)
<!-- Lo que protege tu suite y tu arquitectura. El agente lo lee y no se sale del carril. -->

- No modifica tests existentes (los nuevos son solo del endpoint nuevo).
- No toca `<SecurityConfig / application.yml / otros controllers>`.
- Respeta `.github/copilot-instructions.md` (español en comentarios, records para DTOs, inyección
  por constructor, forma canónica de `Task`).

### 7. Tests nuevos previstos
<!-- Slice/service que verifican los criterios de §5. Nombre estilo metodo_escenario_resultado. -->

- `<vencidas_dueDatePasadoNoDone_lasDevuelveOrdenadas>`
- `<vencidas_dueDateHoy_noAparece>`
- `<overdue_sinResultados_200ListaVacia>`

---

> **Peer-check (2 min):** tu pareja lee la espec y señala **UNA ambigüedad**. La ambigüedad que no
> cierres aquí es el bug que se les irá a los dos en el review. Cierra, commitea la espec, **luego**
> el primer prompt.
