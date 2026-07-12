# Lab — Copilot S5D2: Chat, inline chat, custom instructions y el módulo legacy

> **Semana 5 (GitHub Copilot).** El "lab" sigue siendo **TU `taskflow-api` real** (Spring + JWT +
> JPA + Docker + CI, con `Task.estaVencida()` y `dueDate` en el modelo, JaCoCo desde S3D1) — más
> estas guías. La **única excepción de la semana** es el [`legacy-module/`](legacy-module/): el único
> código ajeno que vas a tocar. Está aquí a propósito: nadie siente el problema de "código de otro"
> refactorizando lo que escribió la semana pasada.

## El modelo de labs de la semana (recordatorio)

| Día | Qué código NUEVO se provee |
|---|---|
| D1 | Nada de proyecto. La kata `ProjectSlugGenerator` (la escribiste tú) + guías |
| **D2 (hoy)** | El **único código ajeno de la semana**: `legacy-module/` para destriparlo con el chat |
| D3 | Nada nuevo: generas tests y docs sobre TU api |
| D4 | Refs de comandos (runbook del CLI) como insumo de *lectura* |
| D5 | *Specs* de features (texto) + *snippets* de auditoría (demo) |

## Antes de empezar (warm-up, 2 min — hazlo ya)

```bash
cd taskflow-api        # tu repo real
git status             # árbol limpio
mvn test               # DEBE ir verde: es la red de todo el día
```

El refactor de hoy se apoya en tu suite verde. Si `mvn test` sale rojo, al canal de atascos **ahora**.

## Mapa de guías → bloque del día

| Bloque | Guía | Qué haces |
|---|---|---|
| **AM-1** — Panel vs inline + slash commands | (teoría en `alumno.md`; se practica sobre tu api) | `/explain` sobre tu código frío, inline chat = diff, `/fix` propone / los tests disponen |
| **AM-2** — `/explain` sobre código ajeno | [`ejercicio-am-explain.md`](ejercicio-am-explain.md) | Mapa del `legacy-module` con `/explain` por capas + **tabla de validación** (cada afirmación con línea) |
| **PM-1** — Custom instructions + refactor con red | [`ejercicio-pm-refactor.md`](ejercicio-pm-refactor.md) (§1–2) + [`copilot-instructions-referencia`](../solucion/copilot-instructions-referencia.md) *(se libera al final)* | Escribir `.github/copilot-instructions.md`, caracterizar con `/tests`, refactor incremental |
| **Integrador** — Legacy dominado | [`ejercicio-pm-refactor.md`](ejercicio-pm-refactor.md) (§3–7) | Refactor en commits pequeños → bug cazado/testeado/arreglado aparte → `EXPLICACION.md` |
| **Entrega escrita** | [`plantilla-explicacion.md`](plantilla-explicacion.md) | Formato de `EXPLICACION.md`: salida de `/explain` **corregida a mano** |
| **Calificación** | [`rubrica-integrador.md`](rubrica-integrador.md) | Con qué se mide el entregable (léela ANTES de empezar) |
| **Todo el día** | El **diario de decisiones** que ya vive en tu api | `docs/copilot/diario-s5.md`, sección **Día 2**. Se llena EN EL MOMENTO |

> **El diario NO se redefine hoy.** Es el mismo `docs/copilot/diario-s5.md` que copiaste en D1
> desde `copilot/dia-1/lab/plantilla-diario-copilot.md`. Hoy solo **continúas** en la sección "Día 2"
> (mínimo **5 entradas** del día: qué pediste, qué dio Copilot, qué aceptaste/rechazaste/corregiste y
> por qué). Es el insumo directo del reporte de aceleración de D5.

## La regla que rige el día

**El chat también alucina sobre TU código.** Validar no es opcional, es el trabajo. Su corolario:

> Ninguna afirmación de `/explain` entra como "verificada" a tu tabla **sin número de línea**. Ningún
> `/fix` se commitea sin **leer el diff** y **correr los tests**. `refactor ≠ fix`: cambiar la forma
> preservando el comportamiento va en unos commits; cambiar el comportamiento a propósito va en OTRO,
> con su propio test.

## Las etiquetas ⚠ VERIFICAR-PREVIO

Copilot cambia su UI/atajos/menús cada mes. Todo lo sensible a versión —nombre del panel de chat,
atajo del inline chat, mecanismo de referencias de archivos, ubicación exacta de
`copilot-instructions.md`, disponibilidad de `/tests` según el plugin— va marcado
**⚠ VERIFICAR-PREVIO** en `alumno.md` y en las guías. El instructor lo re-validó la semana previa. Si
la pantalla no coincide con lo escrito: **el concepto manda, busca el equivalente**.

## Higiene de hoy

- **AM-1 (MP-1..MP-3) se teclea sobre tu working tree real y NO se commitea.** Al cerrar: `git restore .`
- **El entregable viaja en la rama `legacy-refactor`** de tu `taskflow-api`, nunca a `main` (tu
  pipeline de S3D4 puede disparar deploy desde `main` — lee tu propio workflow si dudas).
