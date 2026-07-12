# TaskFlow API · **Snapshot de rescate — estado al cierre de D4** (punto de PARTIDA de S3D5)

> **Úsalo SOLO si tu repo viene atrasado o roto.** Esta carpeta ES el repo `taskflow-api-<usuario>`
> tal como debía quedar **al cierre de D4**: la app de S1–S3 + el pipeline `ci-cd.yml` + el mini-PR
> `/info` (versión **`3.0.0-rc1`**, todavía SIN el bump del freeze). Compila y pasa `mvn -B verify`
> (suite con slices + **gate JaCoCo LINE ≥ 70 %**) desde el minuto cero. Cópialo como base y sigues el
> día de pulido/demo sin quedarte fuera.
>
> **Hoy (S3D5) NO se escribe Java** (salvo el bump de una línea del `InfoController` en el freeze:
> `3.0.0-rc1` → `3.0.0`). Todo el trabajo del día es documentación, pulido y ensayo — el kit vive en la
> raíz de `lab/` (checklists, plantillas, ejemplos, rúbrica, datos-demo). `git diff src/main/java` del
> día es prácticamente vacío.
>
> ```bash
> cd estado-fin-d4
> mvn -q compile      # compila
> mvn -B verify       # suite + gate JaCoCo verde (LINE ≥ 70%)
> ```

---

_Referencia del contenido heredado de D4:_

> Esta carpeta ES el repo `taskflow-api-<usuario>` tal como queda **al cierre de D4**: la app de S1–S3
> (sin cambios de fondo) + el pipeline completo + el mini-PR `/info` YA integrado. Es el estado que el
> instructor mantiene en su repo de referencia `taskflow-api-referencia`, cuyo badge verde es la
> evidencia de que `ci-cd.yml` corre de punta a punta.
>
> **En D4 casi no se escribió Java:** respecto a D3, `src/main/java` solo ganó **`InfoController`** (el
> endpoint `GET /info` del PR de prueba) y **una línea** en `SecurityConfig` (`permitAll("/info")`).
> Todo lo demás nuevo es INFRAESTRUCTURA de entrega.

## Qué contiene (además de la app de S3D3)

| Artefacto | Estado |
|---|---|
| `.github/workflows/ci-cd.yml` | Pipeline en su ruta real (repo funcional). **Idéntico** a `ci-cd.yml` de la raíz. |
| `ci-cd.yml` (raíz) | El pipeline completo como **entregable de referencia** (3 jobs, `needs`/`if`/`permissions`/lowercase/tags, stretch marcado). |
| `docker-compose.prod.yml` | Compose de prod: `api` usa `image: ghcr.io/<usuario>/taskflow-api:latest` (NO `build: .`). |
| `checklists/secrets-y-vars.md` | Tabla canónica Secrets vs Vars, **llena**. |
| `docs/deploy-local.md` | Runbook Plan B "tú eres el runner", **lleno** (owner de ejemplo `anagarcia`). |
| `readme-seccion-ci-cd.md` | La sección "Cómo se construye y despliega este proyecto" (badge + diagrama + tabla). |
| `pr-info/` | El cambio canónico del PR aislado: `InfoController.java`, `InfoControllerTest.java`, `SecurityConfig.diff`. |
| `src/`, `pom.xml`, `Dockerfile`, `docker-compose.yml`, `infra/`, `plan-b/`, `postman/` | La app de S3D3 (el `pom.xml` trae el gate JaCoCo de D1). |

> `pr-info/` guarda el PR **aislado** (para enseñar el diff). Los MISMOS archivos ya están **integrados**
> en `src/` (`controller/InfoController.java`, `integration/InfoControllerTest.java`) y la línea del
> `.diff` ya está aplicada en `config/SecurityConfig.java` — por eso `mvn verify` corre el test nuevo.

## El pipeline en 3 jobs (`ci-cd.yml`)

1. **`test`** — en push a `main` Y en cada PR: `mvn -B verify` (toda la suite de S3D1 + el gate JaCoCo LINE ≥ 70 %); sube `target/site/jacoco/` como artifact con `if: always()`.
2. **`build-and-push`** — solo push a `main` (`needs: test`): login a GHCR con `GITHUB_TOKEN` (`packages: write`), `docker build` con `:<sha>` + `:latest`, `docker push --all-tags`. Owner a minúsculas.
3. **`deploy`** — `needs: build-and-push`, guardado tras `if: vars.DEPLOY_TARGET == 'ec2'`: `appleboy/ssh-action` → `git pull` + `compose -f docker-compose.prod.yml pull api` + `up -d`. En Plan B queda `skipped` y el pipeline sigue verde.

## Verificación de esta solución

```bash
# 1) La app compila y el gate sigue VERDE con el endpoint /info integrado (incluye InfoControllerTest)
mvn -q compile
mvn -B verify                 # jacoco:check en la fase verify; reporte en target/site/jacoco/index.html

# 2) Los YAML del día parsean sin error
python3 -c "import yaml; yaml.safe_load(open('ci-cd.yml'))"
python3 -c "import yaml; yaml.safe_load(open('docker-compose.prod.yml'))"
```

- El pipeline REAL (badge verde de punta a punta) se valida en el repo de referencia del instructor, con
  su EC2 en Plan A y con el guard apagado en Plan B — no en local (aquí no hay runner ni GHCR).

## Sustituir `<usuario>`

En `ci-cd.yml` **NO hay** `<usuario>` hardcodeado: el owner sale de `${GITHUB_REPOSITORY_OWNER,,}` en
tiempo de run. Sí hay que sustituirlo (por tu owner en minúsculas) en: `docker-compose.prod.yml`
(`image:`), `readme-seccion-ci-cd.md` (badge + `docker pull`) y el `.env`/vars del repo.
