# Menú de features tamaño-`overdue` — stretch e insumo del plan B

> Tres features del **mismo tamaño** que `GET /tasks/overdue`, para el **stretch** del integrador
> (parejas rápidas: una segunda feature con el MISMO proceso end-to-end — espec → agente → doble
> review → merge) o como **alternativa** si tu repo divergió y `overdue` no te calza.
>
> **La repetición autónoma es donde se fija el ciclo.** El proceso es idéntico; cambia el dominio.
> Cada una: escribe la espec ([`plantilla-espec.md`](plantilla-espec.md)) **antes** del primer prompt,
> reusa lo que ya existe, no toques tests ajenos, doble review, merge con suite verde.

## Regla común a las tres

- **Reusa, no recodifiques.** Cada feature apoya en piezas canónicas que ya tienes (repositorio con
  derived queries, `TaskResponse`, `TaskMapper`, `TaskOrders`). Si el criterio ya vive en el dominio
  (como `estaVencida()`), reúsalo.
- **Auth JWT** heredada de `anyRequest().authenticated()` — no reconfigures seguridad.
- **Sin resultados → `200 []`** (o el summary con ceros), nunca 404.
- Nombra en la espec las **decisiones de borde**: son el hueco que se le va a Copilot y a tu pareja.

---

## Opción A — `GET /tasks/unassigned`

Tareas **sin responsable** (`assigneeId == null`), típicamente para repartir trabajo.

| Aspecto | Decisión sugerida |
|---|---|
| Filtro | `assigneeId == null` |
| Reuso | derived query nueva `findByAssigneeIdIsNull()` (patrón de las que ya tienes), o `findAll().stream().filter(t -> t.getAssigneeId() == null)` |
| Orden | por prioridad (`TaskOrders.POR_URGENCIA`) o por título (`POR_TITULO`) — **decláralo** |
| Bordes a fijar | ¿incluye tareas DONE sin assignee? (dato histórico posible) → decide sí/no en la espec |
| Respuesta | `List<TaskResponse>`; vacío → `200 []` |
| Tests | `unassigned_conYSinAssignee_devuelveSoloSinAssignee`, `unassigned_todasAsignadas_200ListaVacia` |

---

## Opción B — `GET /projects/{id}/summary` (conteo por estado)

Resumen de un proyecto: cuántas tareas hay en cada estado.

| Aspecto | Decisión sugerida |
|---|---|
| Salida | un **record nuevo** `ProjectSummaryResponse(Long projectId, long todo, long inProgress, long done, long total)` |
| Reuso | `ProjectService.tareasDe(id)` + conteo por estado; o `countByStatus` combinado con el proyecto |
| Proyecto inexistente | `404` (`ProjectNotFoundException`) — igual que el resto de `/projects/{id}/...` |
| Bordes a fijar | proyecto **sin tareas** → summary con todos en `0` y `total = 0` (no 404) |
| Convención | record para el DTO (custom instructions de D2) |
| Tests | `summary_proyectoConTareas_cuentaPorEstado`, `summary_proyectoSinTareas_ceros`, `summary_proyectoInexistente_404` |

> Ojo de review: si sumas los tres conteos y no dan `total`, o si un estado nuevo del enum no se
> contempla, es bug — buen caso para la clasificación correcto/ruido/incorrecto.

---

## Opción C — `GET /tasks/due-soon?days=N`

Tareas que **vencen pronto**: `dueDate` entre hoy y hoy+N, y aún no DONE. El complemento "hacia
adelante" de `overdue`.

| Aspecto | Decisión sugerida |
|---|---|
| Param | `?days=N` (default sensato, p.ej. 7; **decláralo**) |
| Filtro | `dueDate != null && !dueDate.isBefore(hoy) && !dueDate.isAfter(hoy.plusDays(N)) && status != DONE` |
| Bordes a fijar | ¿`dueDate == hoy` cuenta como "due soon"? (probablemente sí — contrasta con la decisión opuesta de `overdue`). ¿`N` negativo o `0`? → `400` o lista vacía, **decláralo** |
| Reuso | `TaskOrders.POR_FECHA` para ordenar por fecha ascendente |
| Validación | `@RequestParam` con validación de `N` (rango) |
| Tests | `dueSoon_dentroDeLaVentana_lasDevuelve`, `dueSoon_hoyEsBorde_incluye`, `dueSoon_daysCero_<decisión>`, `dueSoon_vencidaYaPasada_noAparece` |

> Contraste pedagógico jugoso: `overdue` decide `dueDate == hoy` → **NO**; `due-soon` probablemente
> decide `dueDate == hoy` → **SÍ**. Que dos features tomen decisiones opuestas en el mismo borde es
> exactamente por qué la decisión va **escrita en la espec**, no dejada al agente.
