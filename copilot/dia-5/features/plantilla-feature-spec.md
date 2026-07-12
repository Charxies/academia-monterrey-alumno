# Plantilla de especificación de feature — capstone (Fase 1)

> **Cópiala a `docs/feature-spec.md` de TU `taskflow-api` y llénala en la Fase 1, ANTES del primer
> prompt al chat/agente.** El commit de la espec va **antes** que el primer prompt de implementación:
> los timestamps del `git log` son la evidencia (regla de oro de la semana, DoD del capstone). Una
> espec vaga produce un diff vago que no sabrás revisar.
>
> **De dónde sale el contenido:** los §2–§6 se copian de la spec cerrada de TU feature
> (`features/comentarios.md` · `etiquetas.md` · `notificaciones.md`). La sección **"Fuera de alcance"**
> se copia **literal** de los recortes predefinidos — no es opcional recortarla.
>
> Sustituye los `<placeholders>`; borra los comentarios `<!-- ... -->`.

---

## Feature: `<A comentarios / B etiquetas / C notificaciones>`

**Pareja:** `<nombre 1>` · `<nombre 2>`  · **Rama:** `feature/<nombre>`

### 1. Qué y por qué (1–2 líneas)
<!-- El valor de negocio, no la implementación. -->

### 2. Modelo (entidad nueva)

| Campo | Tipo | Nota (obligatorio / generado por servidor / etc.) |
|---|---|---|
| `<id>` | `<Long>` | `<generado por la BD>` |
| `<...>` | `<...>` | `<...>` |

**Decisión de modelado que el chat inventaría mal** (cópiala de tu feature): `<...>`.

### 3. Contrato HTTP (endpoints)

| Método y ruta | Auth | Request | Respuestas (con códigos reales) |
|---|---|---|---|
| `<POST /...>` | `<JWT / ADMIN>` | `<DTO / —>` | `<201 ... · 400 ... · 404 ...>` |
| `<GET /...>` | `<JWT>` | `<—>` | `<200 ... · 200 [] si vacío>` |

**DTOs (records — convención D2):** `<XRequest(...)>` con `<@NotBlank/@Size...>`, `<XResponse(...)>`.

### 4. Reglas de negocio y de dónde salen
<!-- Nombra la regla Y su fuente. Si reúsas algo de S1–S4, DILO: el agente debe REUSAR, no recodificar. -->

- `<regla 1>` — fuente: `<GlobalExceptionHandler de S2D3 → 404 / auth JWT de S2 / ...>`
- `<regla 2>` — fuente: `<...>`

### 5. Decisiones de borde (las que el chat inventaría mal)
<!-- Aquí se ganan o pierden los bugs que ni Copilot ni tu pareja cachan. Sé explícito. -->

- **`<borde 1>`** → `<decisión>`
- **`<borde 2>`** → `<decisión>`
- **Autor / actor:** `<de dónde sale el usuario — SecurityContext, NO el body>`

### 6. Criterios de aceptación (dado / cuando / entonces)
<!-- Cada uno es un test. Cópialos de tu feature y ajústalos. -->

- **Dado** `<...>` **cuando** `<...>` **entonces** `<...>`.
- **Dado** `<caso inválido>` **cuando** `<...>` **entonces** `<40x, no se persiste>`.
- **Dado** `<caso vacío>` **cuando** `<GET>` **entonces** `<200 []>`.

### 7. Fuera de alcance (RECORTES PREDEFINIDOS — copiar literal, no negociar)
<!-- Esto protege que la feature quepa en el bloque. NO quites recortes para "hacer más". -->

- ❌ `<recorte 1 de tu feature>`
- ❌ `<recorte 2>`
- ❌ `<recorte 3>`
- **Recorte del timer (min 30 PM-1):** `<el que impone el instructor si no hay endpoint respondiendo>`.

### 8. Qué NO toca esta feature (carril del agente)

- No modifica tests existentes (los nuevos son solo de esta feature).
- No toca `<SecurityConfig / application.yml / otros controllers>` salvo el enganche que la feature
  justifica (`<p. ej. TaskService para el disparador de notificaciones>`).
- Respeta `.github/copilot-instructions.md` (español en comentarios, `record` para DTOs, inyección por
  constructor, forma canónica de `Task`, tests `metodo_escenario_resultado`).

### 9. Tests nuevos previstos (nombre `metodo_escenario_resultado`)

- `<crear_bodyVacio_lanzaValidationException / 400>`
- `<listar_ordenPorFechaDesc_masRecientePrimero>`
- `<...>`

### 10. Test canónico de `taskflow-qa` (el roundtrip de tu feature)

`<POST ... → GET ... → contiene ...>` (cópialo de la §7 de tu feature).

---

> **Peer-check (2 min, en el breakout):** tu pareja lee la espec y señala **UNA ambigüedad**. La que no
> cierres aquí es el bug que se les irá a los dos en el review. Cierra la ambigüedad, **commitea la
> espec** (`git commit -m "docs: espec de <feature>"`), **luego** el primer prompt.
>
> **El instructor valida el alcance pareja por pareja en F1.** Recortar aquí es barato; a las 4 pm es caro.
