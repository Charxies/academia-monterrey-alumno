# Lab — Copilot S5D4: CLI, Edits/agent mode y code review

> **Semana 5 (GitHub Copilot).** Como todos los días de la semana, el "lab" **no es un starter de
> código**: es **TU `taskflow-api` real** (Spring + JWT + JPA + Docker + CI, con `Task.estaVencida()`
> y `dueDate` en el modelo, JaCoCo desde S3D1) y **TU `taskflow-qa`** (Selenium + RestAssured) — más
> estas guías. El único material provisto hoy es de **lectura** (`comandos-heredados.sh`) y de
> **plantilla** (espec, diario, rúbrica). Nada que copiar-pegar como código de producción.

## El rol cambia hoy: de teclear a especificar y revisar

D4 es el pico del espectro de autonomía de la semana:

```
D1 completions  →  D2 chat/custom instructions  →  D3 tests/docs  →  D4 agentes y review  →  D5 todo junto
   (completar          (editar un archivo)          (generar bajo        (editar N archivos,     (feature grande)
    una línea)                                        tu mirada)           planear-y-ejecutar,
                                                                           revisar como par)
```

La herramienta hace más; **tú decides más**. El costo de revisar crece con la autonomía y el criterio
NO se delega. La regla del programa ("explica tu código en 1–2 min") hoy aplica a **código que no
tecleaste**.

## Mapa de guías → bloque del día

| Bloque | Guía | Qué haces |
|---|---|---|
| **AM-1** — CLI | [`comandos-heredados.sh`](comandos-heredados.sh) + [`guia-cli.md`](guia-cli.md) | `gh copilot explain` sobre comandos ajenos (find/awk); `gh copilot suggest` para construir comandos de TU flujo (git/docker/mvn), leídos ANTES de ejecutar |
| **AM-2** — Edits/agent mode | [`guia-agente.md`](guia-agente.md) | Sesión multi-archivo en VS Code (`?priority=` en `GET /projects/{id}/tasks`): revisar plan → diffs archivo por archivo → aceptar/rechazar por partes; scope ANTES que contenido |
| **PM-1 + Integrador** — review y feature | [`guia-review.md`](guia-review.md) · [`plantilla-espec.md`](plantilla-espec.md) · [`rubrica-integrador.md`](rubrica-integrador.md) | Clasificar el review de Copilot (correcto/ruido/incorrecto); escribir la espec de `GET /tasks/overdue`; el ciclo espec→agente→doble review→merge |
| **Stretch / plan B** | [`features-equivalentes.md`](features-equivalentes.md) | Segunda feature del mismo tamaño con el MISMO proceso |
| **Todo el día** | [`anexo-diario-d4.md`](anexo-diario-d4.md) | Extiende tu `docs/copilot/diario-s5.md` con los campos nuevos del día |

## Antes de pedirle nada a ningún agente (baseline verde — pedido desde el wrap-up de D3)

```bash
cd taskflow-api
git checkout main && git pull        # main YA con la cobertura de D3 (se mergea en MP-7)
docker compose up -d                  # Postgres arriba
mvn test                              # DEBE ir verde: es la red de todo el día
```

**Sin baseline verde no hay agente.** Si `mvn test` sale rojo o el compose no levanta, se arregla
PRIMERO y **a mano** (Postgres caído, seed ausente, puerto ocupado). Usar el agente para "arreglar"
fallas de entorno que él no entiende multiplica el desorden.

Verifica además que `.github/copilot-instructions.md` (de D2: español en comentarios, records para
DTOs, inyección por constructor, forma canónica de `Task`) esté en la rama de trabajo — los agentes
lo aprovechan.

## Ramas del día (cómo se organiza el trabajo)

| Rama | De dónde nace | Para qué |
|---|---|---|
| `test/cobertura-s5d3` | (ya existe, de D3) | Recibe Copilot Code Review; se clasifica y se **mergea con pipeline verde** en MP-7 |
| `lab/edits-warmup` | `main` | Mecánica de Edits (AM-2, MP-4). Desechable: se puede tirar después |
| `feature/overdue` | `main` **ya con la cobertura de D3 mergeada** | La feature del día. La espec se commitea **antes** del primer prompt |

Orden que importa: **primero se mergea el PR de D3 (MP-7), luego se ramifica `feature/overdue` de ese
main** (`git checkout main && git pull`). Los timestamps de los commits son la evidencia de que la
espec vino antes que el código.

## Parejas cruzadas (driver / navigator)

Hoy se trabaja en pares y **cada quien tiene su propio PR en su propio repo**. En el integrador,
cada alumno **revisa el PR de su pareja** (peer review humana) mientras Copilot revisa en paralelo.
El navigator no es pasajero: **revisa los diffs** — es rol real, no castigo, y con Copilot Free
(cuota compartida) es además cómo se reparte el asiento.

## La regla que rige la semana (y hoy más que nunca)

> Nada generado entra al repo sin (a) **leerlo** (scope del diff ANTES que su contenido), (b) **correr
> la suite**, (c) **poder explicarlo** en 1–2 min. Un comentario de Copilot es la opinión de **un
> revisor más**: se responde con **evidencia**, no con obediencia.

## Higiene de hoy

- Lo del **AM-1 (CLI)** y el **warm-up de Edits (`lab/edits-warmup`)** son mecánica: no ensucian tu
  feature. Al terminar la mecánica: `git restore .` o tira la rama.
- Lo que **sí viaja a `main`**: el PR de D3 (MP-7) y el PR `feature/overdue` (integrador), ambos con
  **pipeline verde**.
- ⚠ Si un merge dispara el CD de S3 y el **job de deploy** falla por infraestructura (RDS apagada,
  credenciales AWS expiradas) y **no por tu código**: el gate del DoD es el **job de tests**. Lee tu
  propio `.github/workflows/*.yml`, documenta la causa y desactiva/skipea ese job — no bloquees el
  merge por infraestructura muerta. (Aplica igual mañana en el capstone.)
