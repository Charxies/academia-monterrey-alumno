# TaskFlow API · Semana 3, Día 4 · CI/CD con GitHub Actions — **LAB (starter)**

> **Hoy el lab NO es un proyecto Java.** Son los archivos de INFRAESTRUCTURA que copias a tu repo
> `taskflow-api-<usuario>` para que la app —que ya está lista desde D2— se **pruebe y despliegue
> SOLA**. Casi no se escribe Java (solo el mini-endpoint `/info` del PR de prueba, en el integrador).
>
> Frase ancla del día: **al final de hoy, desplegar = `git push`.**

## Requisito de arranque: tu repo al día (fin de D3)

Necesitas tu `taskflow-api-<usuario>` **público** con lo de S1–S3D3:
- `mvn -B verify` **verde local** (suite refactorizada a slices + **gate JaCoCo LINE ≥ 70 %**, S3D1).
- `Dockerfile` multi-stage + `docker-compose.yml` + perfil `docker`/`application-docker.yml` + 12-factor (S3D2).
- Docs de D3 (`infra/`, `plan-b/`).  **NO existe `.github/workflows/` todavía — nace HOY.**

**¿Tu repo viene atrasado o roto?** Usa el snapshot de rescate: la carpeta **`estado-fin-d3/`** es el
`taskflow-api` COMPLETO tal como debía quedar al cierre de D3 (compila y pasa `mvn -B verify` desde el
minuto cero, SIN workflows). Cópialo como base y sigues el día sin quedarte fuera:

```bash
cd estado-fin-d3
mvn -q compile      # compila
mvn -B verify       # suite + gate JaCoCo verde (LINE ≥ 70%)
```

## Qué archivo se copia a dónde (a la RAÍZ de tu repo)

| Archivo del lab | Va a | Para qué |
|---|---|---|
| `workflows/ci-cd.yml` | **`.github/workflows/ci-cd.yml`** | El pipeline. **Ruta EXACTA** (ni `workflow/` sin s). Esqueleto con **TODO-1..TODO-9**. |
| `docker-compose.prod.yml` | `docker-compose.prod.yml` (raíz) | El compose de prod (imagen de GHCR en vez de `build: .`). Esqueleto con TODO. |
| `checklists/secrets-y-vars.md` | (guía; la config se hace en GitHub, no en archivo) | Clasificar y cargar Secrets vs Vars (MP-6). |
| `checklists/costos-aws.md` | (guía) | Costos del día + limpieza en el wrap-up (regla de oro de D3). |
| `docs/deploy-local.md` | `docs/deploy-local.md` | Plan B "tú eres el runner" + runbook de emergencia. |

> `.github/workflows/ci-cd.yml` es una carpeta que **nace hoy** en tu repo. Créala:
> `mkdir -p .github/workflows` y copia ahí `workflows/ci-cd.yml`.

## El día en una línea por bloque

1. **AM-1 (CI)** — `ci-cd.yml` con el job `test` (`mvn -B verify`): el robot corre tu suite + gate en cada push/PR. Badge + artifact JaCoCo. Leer un run rojo (ERR-1) y "verde local, rojo en Actions" (ERR-2).
2. **AM-2 (gate + registro)** — branch protection en `main` (un PR rojo NO se mergea, ERR-3); job `build-and-push` publica la imagen en **GHCR** con tags `:<sha>` + `:latest`. Secrets vs vars (ERR-4).
3. **PM-1 (CD + awareness)** — job `deploy` por SSH a la EC2 (`compose pull + up -d`); mapa 1:1 con **CodePipeline/CodeDeploy** (~30 min, no se construye); demo **DynamoDB** (partition key, query vs scan).
4. **Integrador** — pipeline verde de punta a punta + el PR real `feat/endpoint-info` (`GET /info`) mergeado con checks verdes + README con la sección "Cómo se construye y despliega".

## Plan A / Plan B (el día es impartible COMPLETO sin AWS)

- **Toda la mañana (CI + gate + GHCR) es 100 % GitHub**: no toca AWS. Idéntica en ambos planes.
- **Deploy (PM):** Plan A = job `deploy` vivo contra tu EC2 (re-aprovisionada en el warm-up; **TERMINADA
  en el wrap-up**, regla de oro de D3). Plan B = job `skipped` (no seteas `DEPLOY_TARGET`) + ritual
  "tú eres el runner" (`docs/deploy-local.md`) contra tu imagen pública de GHCR.
- **⚠ Costos:** la EC2 free-tier **cuenta horas** activa → se termina al cierre; la tabla DynamoDB de
  la demo se **borra en vivo**. **Actions y GHCR son $0** en repos públicos. Ver `checklists/costos-aws.md`.

## DoD del día (resumen)

- [ ] `.github/workflows/ci-cd.yml` en `main` con los 3 jobs; último run de `main` **verde** de punta a punta (deploy verde en Plan A / `skipped` con `docs/deploy-local.md` ejecutado en Plan B).
- [ ] **Badge** en el README; **branch protection** en `main` con `test` como required check.
- [ ] Evidencia de hoy: **≥1 PR bloqueado por el gate y rescatado** (MP-4) + el PR `feat/endpoint-info` mergeado con checks verdes.
- [ ] Imagen `ghcr.io/<usuario>/taskflow-api` **PÚBLICA** con `:latest` + `:<sha>`, y `docker pull` de tu propia imagen funcionando.
- [ ] Reporte **JaCoCo** descargable como artifact del último run.
- [ ] Deploy verificable: `curl .../info` → `3.0.0-rc1` (EC2 en Plan A / `localhost` en Plan B).
- [ ] README con la sección "Cómo se construye y despliega" y **ningún secreto** en el yml (auditoría cruzada hecha).
- [ ] Commit `feat: pipeline ci/cd con github actions`. En Plan A: **EC2 TERMINADA** al cierre.
