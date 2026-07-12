# Checklist previo de Docker — TAREA para la noche antes de S3D2

> Esto se hace **la noche anterior**, no en frío en clase: mañana el día es 100% Docker y el ancho de
> banda del salón no aguanta 20 personas bajando imágenes a la vez. Al terminar deberías tener Docker
> Desktop en verde, `hello-world` corriendo y las 4 imágenes del día ya descargadas. Si algo falla,
> tráelo al **canal de atascos**: mañana el instructor de apoyo arranca resolviendo esto en paralelo.

## 0) Requisitos de recursos (revísalo primero)

- **RAM:** 8 GB mínimo; con 4 GB libres para Docker el día fluye. En mac/Windows, Docker corre los
  contenedores dentro de una VM Linux ligera — por eso pide RAM (y por eso las laptops modestas sufren en
  el build de Maven dentro del contenedor).
- **Disco:** ~5 GB libres (las 4 imágenes + capas de build).

## 1) Instalar Docker Desktop

### macOS
1. Descarga Docker Desktop del sitio oficial (elige **Apple Silicon** si tu Mac es M1/M2/M3/M4, o **Intel**
   si es Intel — si te equivocas, no abre).
2. Instala, ábrelo, acepta los permisos. Espera el **ícono de la ballena en verde** en la barra de menú.

### Windows (con WSL2 — el camino canónico)
1. **Habilitar virtualización en BIOS/UEFI** si está apagada (síntoma: Docker Desktop se queja al abrir).
   - Reinicia y entra a BIOS (F2/F10/Del/Esc según fabricante).
   - Busca **Intel VT-x / AMD-V** (a veces bajo "SVM Mode" en AMD, o "Intel Virtualization Technology") y
     ponla en **Enabled**. Guarda y reinicia.
2. **Instalar/actualizar WSL2** (PowerShell como administrador):
   ```powershell
   wsl --install
   wsl --update
   wsl --status      # debe decir "Versión predeterminada: 2"
   ```
   Si ya lo tenías: `wsl --update` y reinicia.
3. Instala **Docker Desktop for Windows**, deja marcada la opción **"Use WSL 2 based engine"**.
4. Ábrelo y espera el **ícono de la ballena en verde**.

### Linux
Docker Engine + el plugin `docker compose` v2 (no el `docker-compose` viejo con guion). Sigue la guía de
tu distro; verifica con `docker compose version` (v2.x).

## 2) Verificar que el daemon responde

```bash
docker --version          # p.ej. Docker version 27.x
docker compose version    # DEBE ser v2.x (plugin). Si solo tienes 'docker-compose' v1, actualiza Docker Desktop
docker run hello-world    # descarga una mini-imagen, la corre e imprime "Hello from Docker!"
```

Lee el output de `hello-world` completo: describe justo lo que pasó (el daemon no encontró la imagen local
→ la bajó del registry → creó el contenedor → lo corrió). Ese es el ciclo que mañana abrimos flag por flag.

## 3) Pre-pull de las 4 imágenes del día (lo importante del checklist)

```bash
docker pull postgres:16
docker pull eclipse-temurin:21
docker pull eclipse-temurin:21-jre
docker pull maven:3.9-eclipse-temurin-21
```

Verifica que estén las 4:

```bash
docker images
```

> Mañana, cuando corras un `pull` o un `build`, muchas capas dirán **"Already exists"** — eso es este
> pre-pull pagando. Sin él, la clase entera se queda esperando la red.

## 4) Higiene: cazar el `taskflow-db` viejo del stretch de S2D4

Si hiciste el stretch de Postgres de S2D4, es probable que un contenedor `taskflow-db` siga vivo ocupando
el puerto **5432** — y mañana el compose lo necesita (dolor #4). Revísalo y bórralo:

```bash
docker ps -a                    # ¿aparece 'taskflow-db'?
docker rm -f taskflow-db        # bórralo si existe (sus datos no importaban; eran de prueba)
```

## Troubleshooting rápido

| Síntoma | Causa | Arreglo |
|---|---|---|
| `Cannot connect to the Docker daemon` / `error during connect` | Docker Desktop no arrancó | Ábrelo y ESPERA el ícono verde (arranca lento en laptops modestas) |
| Docker Desktop se queja de virtualización (Windows) | VT-x/AMD-V apagada en BIOS | Habilítala (paso 1); revisa `wsl --status` |
| `docker compose` no existe, solo `docker-compose` | instalación vieja (plugin v1) | Actualiza Docker Desktop (el curso usa `docker compose` v2) |
| El build de Maven muere con exit code 137 | OOM: poca RAM para Docker | Settings → Resources → ≥4 GB y 2 CPUs; cierra IntelliJ durante el build |
| `Bind for 0.0.0.0:5432 failed: port is already allocated` | un Postgres/contenedor viejo ocupa el 5432 | `docker ps -a` + `docker rm -f taskflow-db` (paso 4) |
| Warning `does not match the specified platform` (Mac ARM) | imagen solo-amd64 | las 4 del día son multi-arch (sin drama); si aparece, corre lenta bajo emulación |

Con esto listo, mañana entras directo a lo interesante. Nos vemos en verde.
