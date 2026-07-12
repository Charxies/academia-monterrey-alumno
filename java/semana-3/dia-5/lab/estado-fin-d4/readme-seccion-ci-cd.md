<!-- readme-seccion-ci-cd.md — la sección que se PEGA en el README.md del repo taskflow-api-<usuario>
     (integrador, fase 3). Sustituye <usuario> por tu owner de GitHub en MINÚSCULAS. -->

## Cómo se construye y despliega este proyecto

[![CI/CD](https://github.com/<usuario>/taskflow-api-<usuario>/actions/workflows/ci-cd.yml/badge.svg)](https://github.com/<usuario>/taskflow-api-<usuario>/actions/workflows/ci-cd.yml)

Cada `push`/`pull_request` a `main` dispara el pipeline **`.github/workflows/ci-cd.yml`** (GitHub
Actions). Tres jobs encadenados con `needs` y acotados con `if`:

```
                       ┌─────────────────────────────────────────────────────────────┐
 push / PR a main ───▶ │ test  (mvn -B verify: suite S3D1 + gate JaCoCo LINE ≥ 70%)   │
                       └───────────────┬─────────────────────────────────────────────┘
                                       │ needs: test   (solo si el push es a main; en PR PARA aquí)
                       ┌───────────────▼─────────────────────────────────────────────┐
                       │ build-and-push  (docker build ─▶ GHCR: tags :<sha> + :latest)│
                       └───────────────┬─────────────────────────────────────────────┘
                                       │ needs: build-and-push   (if: vars.DEPLOY_TARGET == 'ec2')
                       ┌───────────────▼─────────────────────────────────────────────┐
                       │ deploy  (ssh a la EC2 ─▶ compose pull api + up -d)           │
                       └─────────────────────────────────────────────────────────────┘
```

### Qué corre en PR vs en main

| Evento | `test` | `build-and-push` | `deploy` |
|---|---|---|---|
| **Pull request** a `main` | ✅ corre (es el required check) | ⛔ no (solo se prueba, no se publica) | ⛔ no |
| **Push** a `main` (tras merge) | ✅ | ✅ imagen nueva a GHCR | ✅ Plan A / `skipped` Plan B |

Un PR **rojo NO tiene botón de merge**: `main` tiene **branch protection** con `test` como *required
status check*. El gate `jacoco:check` (cobertura de líneas ≥ 70 %) es automático — código sin prueba
tumba el PR ANTES de mergear.

### Dónde vive la imagen

`ghcr.io/<usuario>/taskflow-api` — package **público**. Tags: `:latest` (conveniencia) y `:<sha>` del
commit (inmutable y trazable: ESE commit produjo ESA imagen). Traer la última:

```bash
docker pull ghcr.io/<usuario>/taskflow-api:latest
```

### Secrets y vars REQUERIDOS (nombres, JAMÁS valores)

Repo → Settings → Secrets and variables → Actions:

| Nombre | Tipo | Para qué |
|---|---|---|
| `EC2_SSH_KEY` | **Secret** | llave `.pem` de acceso a la EC2 (credencial) |
| `EC2_HOST` | **Var** | IP/host público de la EC2 |
| `EC2_USER` | **Var** | usuario SSH (`ec2-user`) |
| `DEPLOY_TARGET` | **Var** | `ec2` enciende el job `deploy`; sin setear → `skipped` (Plan B) |
| `GITHUB_TOKEN` | *(automático)* | login a GHCR; GitHub lo emite por run, nadie lo crea |

`JWT_SECRET` y el password de la BD **NO** están aquí: los usa la APP y viven en el `.env` de la EC2
(runtime, S3D2/S3D3) — GitHub nunca los ve.

### Deploy manual (Plan B / runbook de emergencia)

Si el `deploy` automático no aplica (sin cuenta AWS) o falla, se despliega a mano con los MISMOS
comandos del job (ver `docs/deploy-local.md`):

```bash
docker compose -f docker-compose.prod.yml pull api
docker compose -f docker-compose.prod.yml up -d
curl http://localhost:8080/info      # {"app":"taskflow-api","version":"3.0.0-rc1"}
```

> Antes de hoy, desplegar era `docker save | gzip` → `scp` (~200 MB) → `docker load` + `docker run`
> (~15 comandos). Hoy **desplegar = `git push`**.
