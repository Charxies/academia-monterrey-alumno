# Anexo D4 del diario de decisiones — CLI, agentes y review

> **Este anexo EXTIENDE la plantilla base** [`copilot/dia-1/lab/plantilla-diario-copilot.md`](../../dia-1/lab/plantilla-diario-copilot.md)
> (regla de la semana: la plantilla se **referencia**, no se duplica). Sigue llenando la sección
> **"Día 4"** de tu `docs/copilot/diario-s5.md` con la tabla base de decisiones
> (aceptada/editada/rechazada · lectura/compilador/test), y **agrega debajo** los tres campos nuevos
> del día. Se llena **EN EL MOMENTO**, no de memoria al cierre — es insumo directo del reporte de
> aceleración de D5.

---

## Campo nuevo 1 — Plan/diffs del agente: qué acepté y qué RECHACÉ (AM-2 e integrador)

Por cada sesión de Edits/agent mode (o inline chat en plan B). El **rechazo con porqué técnico** es lo
que la rúbrica premia — incluidos los rechazos tipo **E1** (archivo fuera de alcance) y **E2** (el
agente "arregla" un test en vez del código).

| # | Feature / sesión | Qué pedí (espec en 1 línea) | Del PLAN acepté | Del PLAN/diff RECHACÉ | Por qué (técnico) | Verificado |
|---|---|---|---|---|---|---|
| 1 | `?priority=` (warm-up) |  |  |  |  | test |
| 2 | `overdue` |  |  |  |  | test |
| 3 |  |  |  |  |  |  |

> Mínimo del día: **≥1 rechazo** de scope/contenido con porqué. Si el agente jamás se salió del
> carril, di **cómo lo confirmaste** (revisaste la lista de archivos del plan y coincidía con la espec).

## Campo nuevo 2 — Clasificación de comentarios de review (D3 y overdue)

Cada comentario de Copilot (y de tu pareja) en tus DOS PRs del día. Clase = correcto / ruido /
incorrecto, **con la evidencia** que la decide.

**PR de D3 (`test/cobertura-s5d3`)** — mínimo del DoD: **≥2 comentarios clasificados**.

| # | Comentario (resumen) | Clase | Evidencia (test/línea/regla) | Qué hice |
|---|---|---|---|---|
| 1 |  | correcto/ruido/incorrecto |  | atendido / follow-up / refutado |
| 2 |  |  |  |  |

**PR de `feature/overdue`** — incluye al menos un *confidently-wrong* si te tocó (clase = incorrecto,
refutado con evidencia).

| # | Comentario (resumen) | Autor (Copilot/pareja) | Clase | Evidencia | Qué hice |
|---|---|---|---|---|---|
| 1 |  |  |  |  |  |
| 2 |  |  |  |  |  |

## Campo nuevo 3 — Tabla comparativa de hallazgos (integrador)

Consolida quién cachó qué en el PR de `feature/overdue`. Llénala **después** de que tu pareja hizo su
peer review (para que sean conjuntos independientes).

| Hallazgo | Copilot | Humano (pareja) | Clase | ¿Hueco de código o de ESPEC? | Atendido |
|---|:--:|:--:|---|---|:--:|
|  | ✓/✗ | ✓/✗ | correcto/ruido/incorrecto | código / espec |  |
|  |  |  |  |  |  |
|  |  |  |  |  |  |

> La fila más valiosa suele ser **"se les fue a ambos"** (Copilot ✗, humano ✗). Casi siempre es un
> hueco de la **espec** (¿`dueDate == hoy`? ¿zona horaria de `LocalDate.now()`?), no del código.
> Anótala — es la que se lee en el wrap-up.

---

## Resumen del día 4 (alimenta el reporte de aceleración de D5)

- **Dónde aceleró el agente:**
- **Dónde COSTÓ tiempo (revisar de más, re-especificar, follow-ups):**
- **Qué rechacé del plan/diffs (y por qué):**
- **Veredicto personal (3 líneas): ¿dónde ahorró tiempo el agente y dónde lo costó?**
  1.
  2.
  3.
