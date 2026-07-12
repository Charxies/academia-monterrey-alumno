<!-- ============================================================================
     PLANTILLA de notas de release (MP-5). Rellena cada <TODO>; estas notas se
     pegan en el Release de GitHub sobre el tag v3.0.
     La sección "Limitaciones conocidas" NO es opcional: declarar límites es señal
     senior y alimenta el punto 6 de la demo.
     Modelo lleno de referencia: ../../solucion/RELEASE-NOTES-v3.0-ejemplo.md
     ============================================================================ -->

# TaskFlow API v3.0 — Notas de release

## Qué es TaskFlow v3.0
<!-- TODO en ~5 líneas: la misma API de v2.0, ahora testeada / dockerizada / desplegada / con CI/CD.
     Cierra la serie v1.0 (consola) → v2.0 (API segura) → v3.0. -->
TODO

## Highlights por semana
- **S1 — consola (repo aparte):** TODO
- **S2 — API REST segura:** TODO
- **S3 — calidad, contenedores y entrega:** TODO

## Cómo probarlo
- **Compose local:** `cp .env.example .env` → `docker compose up -d --build` → `http://localhost:8080`.
- **Swagger:** `http://localhost:8080/swagger-ui.html`.
- **Imagen:** `docker pull ghcr.io/<usuario>/taskflow-api:latest`.
- **Sanity:** `curl http://localhost:8080/info` → `{"app":"taskflow-api","version":"3.0.0"}`.

## Limitaciones conocidas
<!-- TODO honestas: sin refresh tokens, sin paginación, un solo ambiente, sin migraciones, cobertura al 70% (piso). -->
- TODO
- TODO
- TODO

## Qué sigue (features candidatas — S5)
<!-- TODO comentarios en tareas / etiquetas / notificaciones. -->

## Evidencia y respaldo
- **Demo grabada de respaldo:** TODO enlace (Drive/Loom o asset del Release). Nunca el .mp4 en git.
- **Evidencia de despliegue (si hubo AWS):** TODO URL viva + request con respuesta, capturada ANTES de la limpieza.
- **Pipeline:** run verde sobre el commit del tag v3.0.

## Changelog
Historia completa v0 → v3.0 en `CHANGELOG.md`.

---

### Flujo de publicación (integrador, fase 1)
```bash
git status                 # working tree LIMPIO
mvn verify                 # verde local, gate incluido
git push origin main
git tag v3.0               # sobre el commit congelado
git push origin v3.0       # los tags NO viajan con 'git push' a secas
# GitHub → Releases → Draft new release → tag v3.0 → pegar estas notas
```
