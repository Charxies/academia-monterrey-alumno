# Cheatsheet canónico — EC2, imagen y RDS (S3D3)

> Este archivo se **usa tal cual**: copia y pega, sustituyendo solo los valores entre `<…>`
> (tu IP pública de la EC2, el endpoint del RDS, tu password). No hay TODOs que rellenar aquí —
> los únicos "huecos" son datos de tu corrida (IPs, endpoints) que solo existen en tiempo real.
>
> Convención: `<IP>` = IP **pública** de tu EC2 (cambia con stop/start, dolor #3) ·
> `<endpoint-rds>` = el endpoint del RDS (`taskflow-db.xxxx.us-east-1.rds.amazonaws.com`) ·
> Región del día: **`us-east-1`** (no te muevas de ahí).

---

## 0) Región y verificación previa (una vez)

- Selector de región arriba a la derecha en **`us-east-1` (N. Virginia)** en TODA pantalla de AWS.
  Si "tus recursos desaparecieron", casi siempre es que el selector saltó de región (dolor #10).

## 1) Permisos de la llave privada (`.pem`)

La llave `taskflow-key.pem` es TU credencial de SSH: se guarda **FUERA del repo** (nunca se
commitea, es un secreto — como el `.env` de D2). SSH se niega a usarla si es legible por otros.

### macOS / Linux
```bash
chmod 400 taskflow-key.pem        # solo TÚ puedes leerla; sin esto: WARNING UNPROTECTED PRIVATE KEY
```

### Windows (PowerShell) — equivalente de chmod 400
```powershell
icacls taskflow-key.pem /inheritance:r                          # corta permisos heredados
icacls taskflow-key.pem /grant:r "$($env:USERNAME):(R)"        # solo tu usuario, solo lectura
# Alternativa sin SSH local: "EC2 Instance Connect" (consola web). OJO: como el SG lo cerramos a
# "My IP", el Connect del navegador NO entra: sale del rango del servicio EC2 Instance Connect
# (us-east-1: 18.206.107.24/29), NO de tu IP. Para usarlo, añade una regla inbound SSH 22 con
# Source=18.206.107.24/29 (el CIDR del servicio, NO 0.0.0.0/0) o usa un Instance Connect Endpoint.
```

## 2) Conectarse por SSH

```bash
ssh -i taskflow-key.pem ec2-user@<IP>      # usuario canónico de Amazon Linux 2023 = ec2-user
```
Dentro de la instancia, para comprobar dónde estás:
```bash
uname -a          # kernel/arquitectura de la máquina rentada
curl ifconfig.me  # su IP pública, vista desde internet (debe coincidir con <IP>)
```
> "Ayer entraba y hoy timeout" = tu IP de casa cambió y la regla **My IP** del SG quedó vieja
> (dolor #7): edita la regla SSH del `taskflow-ec2-sg` con tu IP nueva. NO abras 0.0.0.0/0.

## 3) Instalar Docker en la EC2

```bash
sudo dnf install -y docker                 # Amazon Linux 2023 usa dnf
sudo systemctl enable --now docker         # arranca el daemon y lo deja habilitado al boot
sudo usermod -aG docker ec2-user           # tu usuario entra al grupo 'docker' (sin sudo en cada comando)
exit                                        # SAL y vuelve a entrar: la sesión vieja no tiene el grupo (dolor #8)
# ...vuelve a conectar con el ssh de arriba...
docker run hello-world                     # verifica: si imprime "Hello from Docker!", listo
```
> Si `docker` da `permission denied ... docker.sock` justo tras el `usermod`: no volviste a
> entrar. Sal y reconecta (o `newgrp docker` en la misma sesión).

## 4) La imagen viaja: `save` → `scp` → `load`

**En tu LAPTOP** (donde está `taskflow-api:local`, construida en D2 — no se reconstruye):
```bash
docker images | grep taskflow-api                          # confirma que existe :local y anota su IMAGE ID
docker save taskflow-api:local | gzip > taskflow-api.tar.gz  # empaca la imagen en un archivo (~200 MB)
scp -i taskflow-key.pem taskflow-api.tar.gz ec2-user@<IP>:~  # cópiala a la EC2 (tarda 3-8 min, es normal)
```
**En la EC2** (ya con Docker):
```bash
docker load < taskflow-api.tar.gz          # descomprime y registra la imagen
docker images                              # el IMAGE ID debe ser EL MISMO que en tu laptop: es el mismo artefacto, bit a bit
```
> Anti-patrón: **NO compiles en la EC2**. t3.micro = 1 GB de RAM; Maven muere por OOM (dolor #9).
> La imagen viaja hecha. Mañana el registry (GHCR/ECR) sustituye este `scp` artesanal.

## 5) Primer arranque PÚBLICO (perfil default = H2 dentro del contenedor)

```bash
docker run -d --name taskflow -p 8080:8080 taskflow-api:local
docker logs -f taskflow                    # espera "Tomcat started on port 8080" (Ctrl-C sale del log, no apaga)
```
Abre en TU navegador: **`http://<IP>:8080/swagger-ui/index.html`** — tu API está en internet.
Login de la semilla de S2D5: `ana` / `ana123` → token → un GET.
> ¿No carga? mini-diagnóstico: ¿la regla **8080** existe en `taskflow-ec2-sg`? ¿`docker ps` muestra
> el contenedor vivo? ¿estás usando la IP **pública** actual? (esta H2 es de juguete y muere con el
> contenedor; en PM la sustituye RDS).

## 6) `docker run` CANÓNICO contra RDS (perfil `docker` + env vars) — MP-10

La MISMA imagen, ahora apuntada a Postgres administrado. **No se toca código ni se reconstruye:**
solo cambian las variables. Primero borra el contenedor de H2 si sigue vivo:
```bash
docker rm -f taskflow
```
```bash
docker run -d --name taskflow -p 8080:8080 \
  -e SPRING_PROFILES_ACTIVE=docker \
  -e DB_HOST=<endpoint-rds> \
  -e DB_NAME=taskflow \
  -e DB_USER=taskflow \
  -e DB_PASSWORD='<tu-password-del-rds>' \
  -e JWT_SECRET='<64-chars-NUEVOS-no-los-de-dev>' \
  taskflow-api:local
docker logs -f taskflow
```
- `DB_NAME` trae default en el yml (`taskflow`); se pone explícito para que el comando sea legible y
  auto-documentado. `DB_PORT` no se pasa: el default `5432` del yml ya sirve para RDS. Lo mínimo que
  cambia frente al compose es `DB_HOST`.
- Al primer arranque contra RDS: Hibernate crea las tablas (`ddl-auto: update`) y el `DataSeeder`
  siembra `admin`/`ana`/`luis` solo (login de demo funciona sin pasos extra).
- **El `JWT_SECRET` explícito** en un servidor real paga la promesa de S2D5: el secret de prod vive
  en el ambiente, no en el código ni en un `.env` que viaje al servidor.

> **La moraleja del día:** de `compose` a RDS **sin tocar una línea de código ni reconstruir la
> imagen** — solo cambiaron las variables. Eso compró el 12-factor de ayer.

## 7) Diagnóstico de red (cuando la API no alcanza al RDS)

| Síntoma (en el log de la API / HikariCP) | Qué dice la red | Causa típica de hoy |
|---|---|---|
| `Connection ... timed out` | el paquete se fue y NADIE contestó | falta la regla en el SG del RDS, IP/endpoint equivocado, red sin ruta |
| `Connection refused` | el host contestó "aquí no escucha nadie" | puerto equivocado, la BD apagada |
| `UnknownHostException` | el nombre no resuelve | endpoint mal copiado (typo) |
| `401` / `403` (HTTP) | la RED está bien; es la APP | eso ya lo dominan desde S2D5 |

Prueba la conectividad TCP al RDS **desde la EC2** (sin levantar la app). `nc` NO viene en Amazon
Linux 2023; instálalo primero (o usa la sonda de bash puro de abajo, que no instala nada):
```bash
sudo dnf install -y nmap-ncat          # provee 'nc' en Amazon Linux 2023 (si no, "command not found")
nc -zv <endpoint-rds> 5432
# "succeeded" = el SG del RDS te deja entrar; "timed out" = falta la regla SG->SG (arréglalo abajo)

# Sin instalar nada (bash trae /dev/tcp): "conectado" o cuelga = timeout
timeout 5 bash -c "</dev/tcp/<endpoint-rds>/5432" && echo conectado || echo "sin ruta (timeout/refused)"
```
**El arreglo COMO SE DEBE** (no con la IP privada de la EC2 — dolor #12): en `taskflow-rds-sg`,
regla inbound **PostgreSQL (5432)** con **Source = `taskflow-ec2-sg`** (el SG, no una IP). Así
sobrevive aunque la EC2 cambie de IP o mañana haya 3 EC2.

## 8) Si el contenedor se reinicia solo (OOMKilled en t3.micro)

1 GB de RAM es poco; si la JVM pide de más, el kernel la mata. Limita el heap:
```bash
docker rm -f taskflow
docker run -d --name taskflow -p 8080:8080 \
  -e JAVA_TOOL_OPTIONS=-Xmx512m \
  -e SPRING_PROFILES_ACTIVE=docker -e DB_HOST=<endpoint-rds> \
  -e DB_NAME=taskflow -e DB_USER=taskflow -e DB_PASSWORD='<tu-password>' \
  -e JWT_SECRET='<64-chars>' \
  taskflow-api:local
```

## 9) `psql` desde la EC2 (stretch / diagnóstico) — el círculo SQL en la nube

```bash
sudo dnf install -y postgresql15          # cliente psql (Amazon Linux 2023)
psql -h <endpoint-rds> -U taskflow -d taskflow
```
Dentro de `psql`:
```sql
\dt                         -- lista las tablas que Hibernate creó
SELECT title, status FROM tasks;   -- las tareas que sembró el DataSeeder / creaste por Swagger
\q
```
> Si el RDS quedó `Available` pero da `FATAL: database "taskflow" does not exist`: el wizard NO
> creó la BD porque "Initial database name" quedó vacío (dolor #11). Arreglo sin recrear:
> `psql -h <endpoint-rds> -U taskflow -d postgres` y luego `CREATE DATABASE taskflow;`.

## 10) Stretch: `deploy.sh` en la EC2 ("mañana esto lo ejecuta un robot")

```bash
# ~/deploy.sh — re-despliega la imagen ya cargada en la EC2
#!/usr/bin/env bash
set -euo pipefail
docker stop taskflow 2>/dev/null || true
docker rm   taskflow 2>/dev/null || true
docker load < ~/taskflow-api.tar.gz
docker run -d --name taskflow -p 8080:8080 \
  --restart unless-stopped \
  -e SPRING_PROFILES_ACTIVE=docker \
  -e DB_HOST="$DB_HOST" -e DB_NAME=taskflow -e DB_USER=taskflow \
  -e DB_PASSWORD="$DB_PASSWORD" -e JWT_SECRET="$JWT_SECRET" \
  taskflow-api:local
```
```bash
chmod +x ~/deploy.sh
DB_HOST=<endpoint-rds> DB_PASSWORD='<pw>' JWT_SECRET='<64-chars>' ~/deploy.sh
```
- `--restart unless-stopped` + `sudo reboot`: la API revive sola tras el reinicio. **Ojo:** `reboot`
  CONSERVA la IP pública; `stop`/`start` NO (dolor #3) — no pares la instancia a media clase.
