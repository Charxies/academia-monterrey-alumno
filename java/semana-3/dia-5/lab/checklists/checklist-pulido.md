# Checklist de pulido — AM-2 (documento RECTOR del bloque)

> El repo se va a **enseñar en entrevistas**: hoy se pule como producto. Marca cada casilla **con un
> comando o una prueba**, no a ojo. Regla de oro del día: desde ahora solo docs, limpieza y fixes de
> lo que este pulido destape — **nada de features**.

## 1. README que pasa la PRUEBA DEL CLON (MP-1, ERR-1)

La prueba: clona TU repo en un directorio **limpio** y corre los comandos del README **tal cual**
(copy-paste, sin memoria muscular). Si un comando no corre → es un bug del README.

```bash
cd /tmp && rm -rf prueba-clon && git clone https://github.com/<usuario>/taskflow-api-<usuario>.git prueba-clon
cd prueba-clon
# ...ahora ejecuta CADA comando del README, en orden, sin cambiar nada
```

- [ ] **Qué es** en 3 líneas (sin jerga): qué resuelve, para quién.
- [ ] **Arquitectura con diagrama** (Mermaid o ASCII): cliente → API (controller/service/repo) → Postgres, + el pipeline al lado.
- [ ] **Cómo correr — camino A (dev local, H2):** los comandos corren en el clon limpio y la app levanta en `:8080`.
- [ ] **Cómo correr — camino B (`docker compose up`):** `cp .env.example .env` documentado; el compose levanta API+Postgres.
- [ ] **Cómo correr — camino C (nube):** URL viva o referencia a las notas de release (no un comando que ya no aplica).
- [ ] **Cómo probar:** `mvn verify` (con el gate) y la colección Postman, ambos mencionados.
- [ ] **Badges:** workflow de D4 (verde) y — al final del día — el tag de release.
- [ ] Ningún comando muerto: el `mvn spring-boot:run -Dspring-boot.run.profiles=postgres` de la era pre-compose **ya no está** (ese es ERR-1).

## 2. OpenAPI completa (MP-2)

Recorre `/swagger-ui.html` **endpoint por endpoint**. Criterio de listo: un dev externo entiende la API
solo con Swagger (y es una pantalla de la demo — dolor 5).

- [ ] Todo endpoint con `@Operation(summary, description)` — típicamente faltan los `/auth/*` de S2D5.
- [ ] Códigos de respuesta documentados donde D3 los dejó (400/401/403/404/409/422).
- [ ] Cada `@RestController` con su `@Tag(name, description)`.
- [ ] Ningún endpoint "abandonado" (sin summary) visible en la pantalla.

## 3. Barrida de deuda + quality gate (MP-3)

Con comandos, no a ojo:

```bash
grep -rn "TODO\|FIXME" src/                 # cero, o justificado con comentario-issue
grep -rn "System.out.println" src/main      # cero: lo legítimo va por logger
# Optimize Imports del IDE (imports muertos fuera)
/opt/homebrew/bin/mvn -q clean verify        # SIN warnings evitables y con el gate JaCoCo en VERDE
```

- [ ] `grep TODO/FIXME` → cero (o cada uno con su justificación).
- [ ] `grep System.out.println` en `src/main` → cero.
- [ ] Optimize Imports pasado (sin imports muertos).
- [ ] `mvn clean verify` → **BUILD SUCCESS**, gate **LINE ≥ 70 %** en verde. Si borrar código muerto movió la cobertura, se ajusta **AQUÍ**, no a las 4 pm.
- [ ] Commits de limpieza pequeños y descriptivos (`chore: ...`) — también son historia que el CHANGELOG contará.

## 4. CHANGELOG.md rastreable (MP-4, ERR-2)

Regla: **cada línea es rastreable a commits reales**. Curar (agrupar, traducir a valor) sí; inventar no.

```bash
git log --oneline --reverse       # la historia cruda: de aquí sale cada entrada
```

- [ ] `CHANGELOG.md` en la raíz, formato Keep a Changelog simplificado.
- [ ] **[3.0]** hoy (suite+gate, Docker/compose, deploy, CI/CD GHCR, pulido).
- [ ] **[2.0]** (API REST completa: CRUD+DTOs+validación+Swagger, JPA/H2, JWT+roles — el commit `feat: taskflow api v2.0 - seguridad jwt` está en tu log).
- [ ] **[0.1]** (nace `taskflow-api` con Initializr).
- [ ] Sección **Prehistoria** que **enlaza** al repo consola `taskflow-<usuario>` (v0 → v1.0) — se referencia, no se fabrica.
- [ ] Cero features inventadas ("multi-tenant", "cache distribuido", "99.9% uptime") — eso es ERR-2.

## 5. Notas de release v3.0 (borrador) (MP-5)

- [ ] Qué es TaskFlow v3.0 en 5 líneas.
- [ ] Highlights por semana (S1/S2/S3).
- [ ] Sección **"Limitaciones conocidas"** honesta (sin refresh tokens, sin paginación, un solo ambiente…).
- [ ] Flujo de release claro para la fase 1: `git tag v3.0` + `git push origin v3.0` (los tags NO viajan con `git push` a secas — dolor 3) + Release de GitHub sobre el tag.
- [ ] **HOY NADIE taggea todavía**: el tag se corta tras el freeze (dolor 2).

## Cierre del bloque

- [ ] La sección [3.0] del CHANGELOG y las notas de release cuentan **la MISMA historia** (dolor 8).
- [ ] Todo lo anterior commiteado; working tree camino a limpio para el freeze del integrador.
