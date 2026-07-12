# Deploy manual "tú eres el runner" (Plan B / runbook de emergencia) · **REFERENCIA (lleno)**

> Plan B: sin cuentas AWS, el job `deploy` queda `skipped` (gris) y **TÚ** ejecutas a mano, uno por uno,
> los MISMOS comandos que corre el script del job — contra TU imagen pública de GHCR. El círculo
> **registry → pull → run** se cierra igual; cambia QUIÉN aprieta el último botón.
>
> Sirve además de **runbook de emergencia** en Plan A. Requisito: tu imagen ya está en GHCR y es
> **PÚBLICA** (dolor 7). Ejemplo con el owner `anagarcia` — sustituye por el tuyo (minúsculas).

## 0) Prerrequisitos

- [x] Docker Desktop corriendo.
- [x] `docker-compose.prod.yml` en la raíz del repo con `image: ghcr.io/anagarcia/taskflow-api:latest`.
- [x] `.env` local con `POSTGRES_USER` / `POSTGRES_PASSWORD` / `POSTGRES_DB` / `JWT_SECRET` (`cp .env.example .env`).

## 1) Los comandos del deploy (idénticos al script del job `deploy`)

```bash
# Estás en la raíz de tu repo taskflow-api-anagarcia
cd ~/taskflow-api-anagarcia

# (público) baja la imagen que el pipeline publicó — el prod NO construye, CONSUME
docker compose -f docker-compose.prod.yml pull api

# levanta api + db en segundo plano
docker compose -f docker-compose.prod.yml up -d

# ¿está viva? (db 'healthy' y api arriba)
docker compose -f docker-compose.prod.yml ps
```

> `pull` da `unauthorized` → tu package sigue **privado**: GitHub → perfil → Packages → `taskflow-api`
> → Package settings → Change visibility → **Public** (una sola vez; los repos ya son públicos).

## 2) Verificación (el mismo curl del smoke test)

```bash
# /info es PÚBLICO (permitAll): NO necesita token
curl http://localhost:8080/info

# esperado:
# {"app":"taskflow-api","version":"3.0.0-rc1"}
```

- [x] `/info` responde 200 con `"version":"3.0.0-rc1"`.

## 3) Limpieza

```bash
docker compose -f docker-compose.prod.yml down        # baja los contenedores (conserva el volumen)
# docker compose -f docker-compose.prod.yml down -v    # SOLO si quieres borrar también los datos
```

> Pizarra, junto al villano del warm-up (deploy de D3 = N pasos / M minutos):
> **pasos del deploy con el pipeline = `git push`.**
