# TaskFlow API · Semana 3, Día 3 · AWS (EC2 · S3 · VPC conceptual · RDS)

> ⚠️ **ANTES DE TOCAR NADA EN AWS: crea un presupuesto de $5 con alerta por email (MP-1).**
> **Regla de oro del día: TODO lo que se crea hoy SE DESTRUYE hoy.** Antes de cerrar sesión
> corres `infra/checklist-limpieza-aws.md` completo — EC2 *terminated*, RDS *deleted*, bucket
> borrado. El **budget NO se borra**: queda de guardián el resto de la semana. Apagándolo todo,
> el día cuesta **< $1 USD** (con free tier ~$0); el presupuesto de $5 es tu red de seguridad.

Hoy **NO se escribe ni una línea de Java**. El "código" del día son comandos, dos checklists y
(en Plan B) un `docker-compose`. La app es TU `taskflow-api` tal como quedó al cierre de **D2**
(imagen `taskflow-api:local`, perfil `docker`, `application-docker.yml`, `docker-compose.yml`):
`src/main/java` queda **idéntico**. Hoy esa MISMA imagen sale de tu laptop y aterriza en un
servidor real en internet, y luego se conecta a una base de datos administrada (RDS) **cambiando
solo variables de entorno** — la paga del 12-factor de ayer.

## El flujo del día en 10 líneas

1. **MP-1** Budget de $5 con alerta (root, una vez) → **nada existe todavía**.
2. **MP-2** Asegurar la cuenta: MFA al root, usuario IAM `dev-<nombre>`, entrar como IAM.
3. **MP-3** Lanzar EC2 `taskflow-ec2` (Amazon Linux 2023, t3.micro), SG `taskflow-ec2-sg`: SSH **My IP**, 8080 público.
4. **MP-4/5** `ssh` a la EC2, instalar Docker (`dnf install docker`), relogin por el grupo.
5. **MP-6** `docker save | gzip` → `scp` → `docker load`: la imagen viaja, **mismo IMAGE ID**.
6. **MP-7** `docker run -p 8080:8080` (perfil default = H2) → Swagger PÚBLICO desde tu navegador.
7. **MP-8** Lanzar RDS Postgres 16 `taskflow-db` (tarda ~10 min; se lanza primero), SG `taskflow-rds-sg`.
8. **MP-9** Bucket S3 privado + subir el ZIP del reporte JaCoCo de D1 → compartir con **presigned URL**.
9. **T6** Dibujar la topología (VPC conceptual) en `infra/topologia-aws.md`.
10. **MP-10** `docker run` con `SPRING_PROFILES_ACTIVE=docker` + `DB_HOST=<endpoint-rds>` + `JWT_SECRET` → SG→SG → la API vive contra RDS.

Cierre: **Integrador** (evidencia al repo + prueba social cruzada) → **limpieza guiada** → wrap-up.

## Qué archivo se copia a dónde

Los archivos de `infra/` y `plan-b/` de este `lab/` se **copian a la raíz de tu repo
`taskflow-api-<usuario>`** (crea la carpeta `infra/`). Son el estado que **D4 hereda**.

| Archivo del lab | Va a | Para qué |
|---|---|---|
| `infra/comandos-ec2.md` | `infra/comandos-ec2.md` | Cheatsheet canónico del día (ssh/scp/save/load/docker run/psql). **Se usa tal cual.** |
| `infra/topologia-aws.md` | `infra/topologia-aws.md` | Plantilla del diagrama VPC — la **completas con TUS ids reales** (sg-…, endpoint RDS). |
| `infra/evidencia-deploy.md` | `infra/evidencia-deploy.md` | Plantilla de evidencia (URL, región, screenshots) — la llenas en el integrador. |
| `infra/checklist-limpieza-aws.md` | `infra/checklist-limpieza-aws.md` | Checklist de destrucción nominal — lo marcas al cerrar. |
| `infra/checklist-previo-semana.md` | (lo usa el **coordinador**, no el alumno) | Cuentas creadas ≥3 días antes. |
| `plan-b/docker-compose.topologia.yml` | `plan-b/…` | Solo si corres **Plan B** (sin cuenta AWS). |
| `plan-b/quiz-decisiones.md` | `plan-b/…` | Quiz de decisiones de diseño (Plan B o cierre). |

> La app en sí (Dockerfile, docker-compose.yml, src/, pom.xml, postman/) es tu entrega de D2 **sin
> cambios**. La colección Postman ya usa la variable `{{baseUrl}}` (`http://localhost:8080`): para el
> smoke test contra la EC2 solo cambias su valor a `http://<IP>:8080` — nada que reprogramar.

## Cómo activar Plan B (sin cuenta AWS)

Si no tienes cuenta AWS operativa (o el instructor corre el día en Plan B), NO te quedas fuera:
sigues la topología **localmente** con Docker, que ya dominas desde D2.

```bash
cd plan-b
docker compose -f docker-compose.topologia.yml up -d      # api + db en 2 redes (edge/data)
docker compose -f docker-compose.topologia.yml ps
psql -h localhost -p 5432 -U taskflow taskflow             # FALLA: db no publica puertos = "muralla" OK
docker compose -f docker-compose.topologia.yml down -v     # limpieza (¡borra el volumen!)
```

- Tu entregable de Plan B es el **diseño AWS** en `infra/topologia-aws.md` (los SGs, subnets y porqués)
  + `plan-b/quiz-decisiones.md` respondido. Ver la sección PLAN B del `alumno.md`.
- El `docker-compose.topologia.yml` del `lab/` es un **esqueleto con TODOs**: lo completas para
  lograr el aislamiento (dos redes, `db` solo en `data` y sin `ports`). La versión resuelta está en
  `solucion/plan-b/`.

## Definición de "listo" (DoD del día)

**Plan A:** budget activo creado ANTES de todo · evidencia commiteada (`infra/evidencia-deploy.md` con
URL pública + screenshots de Swagger, Postman verde y budget; `infra/topologia-aws.md` con tus ids) ·
la API corrió en EC2 contra RDS por perfil `docker` + env vars y los datos **sobreviven a
`docker rm -f` + re-run** (misma imagen, sin rebuild) · prueba social (consumo cruzado) ·
`infra/checklist-limpieza-aws.md` completado + confirmación nominal en el wrap-up · commits
`feat: taskflow desplegada en aws - ec2 + rds (evidencia)` y `chore: limpieza aws d3 completa`.

**Plan B:** compose de topología arriba con redes separadas y `db` inalcanzable desde el host (síntomas
`pause`→timeout y puerto→refused provocados) · API accesible desde otro dispositivo de tu red local
(screenshot) · `topologia-aws.md` + `quiz-decisiones.md` respondidos y revisados en pareja · push de la
evidencia local.
