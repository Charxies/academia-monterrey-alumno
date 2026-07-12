# TaskFlow API · Semana 3, Día 3 · AWS · **Solución de referencia**

> Esta es la solución de referencia (no se comparte hasta cerrar el lab). El **código Java es
> idéntico al de D2** — hoy no se escribe Java: `git diff src/main/java` del día es **vacío**. Lo que
> cambia es que aparecen los artefactos de infraestructura (`infra/`, `plan-b/`) y aquí van **llenos
> con los ids de ejemplo de la corrida del instructor** (`topologia-aws.md`, `evidencia-deploy.md`),
> con la limpieza ya marcada y el `plan-b/docker-compose.topologia.yml` **resuelto**.

> ⚠️ **Regla de oro del día: TODO lo que se crea hoy SE DESTRUYE hoy** (y el **budget de $5 se crea
> ANTES que el primer recurso**). Apagándolo todo, el día cuesta **< $1 USD** (free tier ~$0). El
> budget NO se borra: queda de guardián el resto de la semana.

## Qué contiene esta solución

| Archivo | Estado en la solución |
|---|---|
| `infra/comandos-ec2.md` | Cheatsheet canónico (idéntico al del lab; **se usa tal cual**). |
| `infra/topologia-aws.md` | **Completado** con ids de ejemplo de la corrida del instructor. |
| `infra/evidencia-deploy.md` | **Llenado** como referencia de formato de la evidencia. |
| `infra/checklist-limpieza-aws.md` | **Marcado** como referencia del cierre nominal. |
| `infra/checklist-previo-semana.md` | Checklist del coordinador (idéntico al del lab). |
| `plan-b/docker-compose.topologia.yml` | **Resuelto**: dos redes `edge`/`data`, `db` solo en `data` y sin `ports`. |
| `plan-b/quiz-decisiones.md` | Con **respuestas modelo** comentadas. |
| `src/`, `pom.xml`, `Dockerfile`, `docker-compose.yml`, `postman/` | La app de D2, **sin cambios**. |

## El flujo del día en 10 líneas

1. **MP-1** Budget de $5 con alerta (root, una vez) → **nada existe todavía**.
2. **MP-2** Asegurar la cuenta: MFA al root, usuario IAM `dev-<nombre>`, entrar como IAM.
3. **MP-3** Lanzar EC2 `taskflow-ec2` (Amazon Linux 2023, t3.micro), SG `taskflow-ec2-sg`: SSH **My IP**, 8080 público.
4. **MP-4/5** `ssh` a la EC2, instalar Docker, relogin por el grupo.
5. **MP-6** `docker save | gzip` → `scp` → `docker load`: la imagen viaja, **mismo IMAGE ID**.
6. **MP-7** `docker run -p 8080:8080` (perfil default = H2) → Swagger PÚBLICO.
7. **MP-8** Lanzar RDS Postgres 16 `taskflow-db` (tarda ~10 min; primero), SG `taskflow-rds-sg`.
8. **MP-9** Bucket S3 privado + ZIP del reporte JaCoCo de D1 → **presigned URL**.
9. **T6** Topología (VPC conceptual) → `infra/topologia-aws.md`.
10. **MP-10** `docker run` perfil `docker` + `DB_HOST=<endpoint-rds>` + `JWT_SECRET` → SG→SG → API contra RDS.

## Verificación del material (cómo se valida esta solución)

Hoy no hay `mvn` que "resuelva" (la app no cambió), pero la solución se valida así:

```bash
# 1) La app de D2 sigue compilando y el gate de cobertura sigue verde (LINE >= 70%)
mvn -q compile
mvn verify              # jacoco:check en la fase verify; reporte en target/site/jacoco/index.html

# 2) El compose de Plan B parsea y demuestra el aislamiento de redes
cd plan-b
docker compose -f docker-compose.topologia.yml config >/dev/null && echo "compose OK"
docker compose -f docker-compose.topologia.yml up -d
psql -h localhost -p 5432 -U taskflow taskflow   # DEBE FALLAR: 'db' no publica puertos (la "muralla")
docker compose -f docker-compose.topologia.yml exec api sh -c 'true'  # 'api' sí alcanza 'db' por red 'data'
docker compose -f docker-compose.topologia.yml down -v
```

- El aislamiento clave: **`db` está solo en la red `data` y sin `ports:`** → inalcanzable desde el
  host (= RDS "Public access: NO"); **`api` está en `edge` + `data`** → da la cara en `edge` (8080) y
  alcanza la BD en `data` (= la EC2 que cruza). La membresía de red ≈ Security Group.
