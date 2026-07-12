# Lab — Copilot S5D1: Fundamentos, setup, completions y la kata

> **Semana 5 (GitHub Copilot).** Esta semana el "lab" **no es un starter de código**. Es **TU
> `taskflow-api` real** (Spring + JWT + JPA + Docker + CI, con `Task.estaVencida()` y `dueDate` en
> el modelo, JaCoCo desde S3D1) y **TU `taskflow-qa`** (Selenium + RestAssured) — más estas guías.
> Copilot se aprende sobre código de verdad, no de juguete.

## El modelo de labs de la semana (léelo una vez)

| Día | Qué código NUEVO se provee |
|---|---|
| **D1 (hoy)** | Nada de proyecto. Solo la **kata `ProjectSlugGenerator`** (la escribes tú) + estas guías |
| D2 | El **único código ajeno de la semana**: un módulo *legacy* para destriparlo con el chat |
| D3 | Nada nuevo: generas tests y docs sobre TU api |
| D4 | Refs de comandos (runbook del CLI) como insumo de *lectura* |
| D5 | *Specs* de features (texto) + *snippets* de auditoría (demo) |

El resto del tiempo trabajas sobre lo que ya construiste en 4 semanas. Tu suite de tests de S1–S4
es la **red de seguridad**: si Copilot alucina, algo se pone rojo. Ese es el trato.

## Antes de empezar (warm-up, 2 min — hazlo ya)

```bash
cd taskflow-api        # tu repo real
git status             # árbol limpio: hoy vas a teclear sobre él y luego 'git restore .'
mvn test               # DEBE ir verde: es la red de toda la semana
```

Si `mvn test` sale rojo, avísalo en el canal de atascos **ahora**, no el miércoles.

## Mapa de guías → bloque del día

| Bloque | Guía | Qué haces |
|---|---|---|
| **AM-2** — Setup (4 checks) | [`guia-setup.md`](guia-setup.md) | Licencia, IntelliJ, VS Code, CLI verificados uno por uno + fallback Free |
| **PM-1** — Completions | [`guia-completions.md`](guia-completions.md) | Mecánica (aceptar/ciclar/rechazar/toggle), las 3 técnicas de contexto, el A/B, y la tabla señal/ruido |
| **Integrador** — Kata | [`kata-slug-generator.md`](kata-slug-generator.md) | `ProjectSlugGenerator`: tests a mano → impl asistida → diario, en rama `s5d1-kata` |
| **Todo el día** | [`plantilla-diario-copilot.md`](plantilla-diario-copilot.md) | La copias a `docs/copilot/diario-s5.md` de tu api; la llenas EN EL MOMENTO |

## La regla que rige la semana

**El contexto es el prompt. Tu código ES el prompt.** Y su corolario operativo:

> Nada generado entra al repo sin (a) **leerlo**, (b) **correr los tests**, (c) **poder explicarlo**
> en 1–2 min. Aceptar sin leer no pasa la rúbrica — el spot-check oral lo destapa.

## Higiene de hoy (no la olvides)

- **PM-1 se teclea sobre tu working tree real y NO se commitea.** Al cerrar el bloque: `git restore .`
- **Lo único que viaja al repo hoy:** la kata (2 commits) + el diario, en la rama **`s5d1-kata`**.
  Nunca a `main`: tu pipeline de S3D4 (`.github/workflows/ci-cd.yml`) dispara `build-and-push`/deploy
  en push a `main`. Ábrelo y confírmalo tú mismo (los filtros `branches: [main]`) — tu
  infraestructura, tu responsabilidad.
