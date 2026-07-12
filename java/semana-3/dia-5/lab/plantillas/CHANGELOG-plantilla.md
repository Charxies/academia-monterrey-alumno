<!-- ============================================================================
     PLANTILLA de CHANGELOG (MP-4). Copia a la RAÍZ de tu repo como CHANGELOG.md y
     rellena cada <TODO> desde TU historia real.

     REGLA (ERR-2): cada línea debe ser rastreable a un commit real.
        git log --oneline --reverse    <- de aquí sale cada entrada
     Curar (agrupar commits, traducir a valor para el lector) SÍ. Inventar NO
     ("multi-tenant", "cache", "99.9% uptime" si no existen = mentira).
     Modelo lleno de referencia: ../../solucion/CHANGELOG-ejemplo.md
     ============================================================================ -->

# Changelog

Formato [Keep a Changelog](https://keepachangelog.com/es-ES/1.1.0/) (simplificado);
versionado [SemVer](https://semver.org/lang/es/). Cada línea rastreable a `git log`.

## [3.0.0] - TODO-fecha

<!-- La misma API, ahora testeada, dockerizada, desplegada y con CI/CD. Entrega final. -->

### Added
- TODO: suite en slices + gate JaCoCo LINE ≥70 %. _(commit `test: ...`)_
- TODO: Dockerfile multi-stage + compose (Postgres) + perfil `docker` 12-factor. _(commit `feat: ...`)_
- TODO: CI/CD GitHub Actions (test → build-and-push GHCR → deploy). _(commit `ci: ...`)_
- TODO: endpoint `GET /info`. _(commit `feat: ...`)_

### Changed
- TODO: `/info` de `3.0.0-rc1` a `3.0.0` (freeze). _(commit `chore: release ...`)_
- TODO: README que pasa la prueba del clon; OpenAPI completa. _(commit `docs: ...`)_

### Removed
- TODO: barrida de deuda (TODOs, sysouts, imports muertos). _(commit `chore: ...`)_

## [2.0.0] - TODO-fecha

### Added
- TODO: CRUD Project/Task con DTOs + validación. _(commit `feat: ...`)_
- TODO: Swagger + GlobalExceptionHandler. _(commit `feat: ...`)_
- TODO: persistencia JPA (H2 archivo). _(commit `feat: ...`)_
- TODO: JWT + roles + regla de owner. _(commit `feat: taskflow api v2.0 - seguridad jwt`)_

## [0.1.0] - TODO-fecha
- TODO: nace `taskflow-api` desde Initializr. _(commit `chore: init ...`)_

---

## Prehistoria — la consola (repo aparte)

<!-- No vive en ESTE repo: se REFERENCIA, no se fabrica. -->
- **v1.0** — consola persistida y testeada (S1D5).
- **v0** — primeros modelos de consola (S1D1–S1D4).

Repo consola: `https://github.com/<usuario>/taskflow-<usuario>`.

<!-- TODO enlaces de comparación por tag -->
[3.0.0]: https://github.com/<usuario>/taskflow-api-<usuario>/releases/tag/v3.0
[2.0.0]: https://github.com/<usuario>/taskflow-api-<usuario>/releases/tag/v2.0
