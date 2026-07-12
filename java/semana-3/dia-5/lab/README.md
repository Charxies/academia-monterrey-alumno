# TaskFlow API · Semana 3, Día 5 · Entrega final v3.0 — **LAB (el kit del día)**

> **Hoy el lab no trae un starter de la API que construir.** Hoy no se estrenan features (regla de oro
> del día): se trabaja sobre TU `taskflow-api-<usuario>` puliéndolo como producto, ensayando la demo y
> publicando el **release v3.0**. Este lab te da el **kit**: las plantillas, checklists, ejemplos de
> error, la rúbrica y los datos de demo — más una **copia de rescate** del `taskflow-api` fin-D4
> (`estado-fin-d4/`, break-glass por si tu repo viene roto; ver abajo). Tu código Java del día
> prácticamente no cambia (solo un bump de una línea del `/info` en el freeze).
>
> Frase ancla del día: **si no está en el README no existe; si está y no corre, es un bug.**

## Requisito de arranque: tu repo al día (fin de D4)

Necesitas tu `taskflow-api-<usuario>` **público** con lo de S1–S3D4:
- `mvn verify` **verde local** (suite en slices + **gate JaCoCo LINE ≥ 70 %**, S3D1).
- `Dockerfile` + `docker-compose.yml` + perfil `docker`/`application-docker.yml` (S3D2).
- `.github/workflows/ci-cd.yml` con el pipeline de 3 jobs y la imagen en GHCR (S3D4).
- `GET /info` respondiendo `"version": "3.0.0-rc1"` (nace en el PR de D4).

**¿Tu repo viene atrasado o roto?** Usa `estado-fin-d4/` — es el `taskflow-api` COMPLETO tal como
debía quedar al cierre de D4 (compila y pasa `mvn verify` desde el minuto cero). Cópialo como base:

```bash
cd estado-fin-d4
/opt/homebrew/bin/mvn -q compile      # compila
/opt/homebrew/bin/mvn -B verify       # suite + gate JaCoCo verde (LINE ≥ 70%)
```

## Mapa del kit — qué pieza se usa en qué bloque

| Pieza del kit | Bloque | Para qué |
|---|---|---|
| `checklists/checklist-pulido.md` | **AM-2** (documento rector) | README (prueba del clon), OpenAPI, barrida de deuda, gate ≥70 %, CHANGELOG, notas de release. |
| `plantillas/README-plantilla.md` | MP-1 | Las secciones canónicas del README profesional (badges + diagrama Mermaid). |
| `ejemplos/README-v2-ejemplo.md` | MP-1 (**ERR-1**) | El README que **miente** (comando desactualizado) — el instructor lo corre en un clon limpio. |
| `plantillas/CHANGELOG-plantilla.md` | MP-4 | Keep a Changelog simplificado: `[3.0]`/`[2.0]`/`[0.1]`/Prehistoria. |
| `ejemplos/CHANGELOG-inflado.md` | MP-4 (**ERR-2**) | El changelog con features inventadas — la demo de "curar ≠ inventar". |
| `plantillas/RELEASE-NOTES-v3.0.md` | MP-5 | Qué es, highlights por semana, **limitaciones conocidas**. |
| `plantillas/GUION-DEMO.md` | MP-6 | Los 6 puntos con minutos y reparto A/B (cambio fijado en el punto 5). |
| `postman/datos-demo.postman_collection.json` | MP-6 (**ERR-3**) | Siembra el escenario: usuarios de la historia, proyecto "Sprint demo", 5 tareas variadas. |
| `checklists/checklist-pre-demo.md` | PM / integrador | Entorno arriba, datos sembrados **y verificados con un GET**, pestañas listas, grabación a la mano. |
| `rubricas/rubrica-demo.md` | PM-1 (MP-7) + integrador | Criterios y escala; la usan el feedback cruzado Y el instructor. |
| `checklists/checklist-limpieza-aws.md` | Integrador, **fase 3** | Evidencia → apagar EC2 → borrar RDS → buckets → Billing en cero. |

> **Los MODELOS llenos** (un README final, un CHANGELOG curado, un guion lleno, unas notas de release y
> una rúbrica con feedback) están en `../solucion/` — se comparten al cerrar cada bloque, no antes.

## El día en una línea por bloque

1. **AM-1 (WebFlux awareness)** — charla + 3 demos del instructor (MVC bloqueante vs Flux vs virtual
   threads). Tú lees, predices y preguntas; **no escribes código reactivo**. Sin mini-práctica.
2. **AM-2 (pulido)** — README que pasa la prueba del clon (ERR-1), OpenAPI completa, barrida de deuda +
   gate ≥70 %, CHANGELOG rastreable (ERR-2), borrador de notas de release. Documento rector: `checklist-pulido.md`.
3. **PM-1 (ensayo)** — llenar `GUION-DEMO.md` y sembrar `datos-demo` (ERR-3); ensayo cruzado con la
   rúbrica; segunda pasada + **grabar la demo de respaldo**.
4. **Integrador (release + demo final)** — freeze → bump `/info` a `3.0.0` → tag `v3.0` + Release con
   notas → **demo de 10 min + 2 de preguntas**. Si hubo AWS: evidencia primero, limpieza después.

## Plan A / Plan B (el día es impartible COMPLETO sin AWS)

- **Toda la demo corre igual contra `docker compose up` local** (Plan B). AWS solo añade la evidencia
  de despliegue al release.
- **D4 terminó su EC2 al cierre** → a D5 no se hereda infraestructura AWS viva. Quien quiera evidencia
  la **re-aprovisiona hoy** (runbook de D4) y la **termina en la fase 3** (`checklist-limpieza-aws.md`).
- **⚠ Costos:** una EC2/RDS olvidada cobra en semanas lo que costó el free tier entero. Regla: evidencia
  primero, apagado después, verificación final en Billing. Actions y GHCR son **$0** en repos públicos.

## DoD del día (resumen)

- [ ] **Release `v3.0`** publicado (tag pusheado con `git push origin v3.0`) con notas y **demo grabada enlazada**.
- [ ] `CHANGELOG.md` curado v0→v3.0 (cada línea rastreable; Prehistoria referenciada) en el repo.
- [ ] README pasa la **prueba del clon** en todos sus caminos; OpenAPI con summary/description; cero TODOs/sysouts.
- [ ] `mvn verify` verde con el **gate ≥70 %** y el pipeline verde sobre el commit del tag; `/info` → `"3.0.0"`.
- [ ] **Demo final** presentada (10+2) con reparto 50/50. Si hubo AWS: evidencia capturada y recursos **apagados**.
