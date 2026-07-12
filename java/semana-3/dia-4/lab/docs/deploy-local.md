# Deploy manual "tú eres el runner" (Plan B / runbook de emergencia) · **A LLENAR**

> Plan B: sin cuentas AWS, el job `deploy` queda `skipped` (gris) y **TÚ** ejecutas a mano, uno por uno,
> los MISMOS comandos que corre el script del job — contra TU imagen pública de GHCR. El círculo
> **registry → pull → run** se cierra igual; cambia QUIÉN aprieta el último botón.
>
> Sirve además de **runbook de emergencia** en Plan A: si el pipeline no puede desplegar, esto es lo
> que corres a mano. Requisito: tu imagen ya está en GHCR y es **PÚBLICA** (dolor 7).

## 0) Prerrequisitos

- [ ] Docker Desktop corriendo.
- [ ] `docker-compose.prod.yml` en la raíz del repo (con tu `image: ghcr.io/<usuario>/taskflow-api:latest`).
- [ ] `.env` local con `POSTGRES_USER` / `POSTGRES_PASSWORD` / `POSTGRES_DB` / `JWT_SECRET` (`cp .env.example .env`).

## 1) Los comandos del deploy (rellena los huecos)

```bash
# Estás en la raíz de tu repo taskflow-api-<usuario>
cd _______________________________

# (público) baja la imagen que el pipeline publicó — el prod NO construye, CONSUME
docker compose -f _______________________ pull api

# levanta api + db en segundo plano
docker compose -f _______________________ up -d

# ¿está viva? (mira el estado 'healthy' de db y que api arrancó)
docker compose -f docker-compose.prod.yml ps
```

> Si `pull` da `unauthorized`: tu package de GHCR sigue **privado** → GitHub → tu perfil → Packages →
> `taskflow-api` → Package settings → Change visibility → **Public** (una sola vez).

## 2) Verificación (el mismo curl del smoke test)

```bash
# el endpoint /info es PÚBLICO (permitAll): NO necesita token
curl http://localhost:8080/______

# esperado:  {"app":"taskflow-api","version":"__________"}
```

- [ ] `/info` responde 200 con la versión correcta.

## 3) Limpieza

```bash
docker compose -f docker-compose.prod.yml down        # baja los contenedores (conserva el volumen)
# docker compose -f docker-compose.prod.yml down -v    # SOLO si quieres borrar también los datos
```

> Apunta en la pizarra, junto al villano del warm-up: **pasos del deploy con el pipeline = `git push`.**
