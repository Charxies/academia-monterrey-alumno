# Guía Día 0 — Preparación de ambiente

> Completa esto **ANTES** del primer día. La academia es virtual y de ritmo alto: cada minuto instalando en vivo es un minuto que no programas. Cada sección trae el comando de **verificación** y qué salida esperar. Windows / macOS / Linux donde difieran.

Al final debes poder responder "sí" a todo el **checklist final**.

---

## 1. JDK 21 (obligatorio, semana 1)

Instala **Eclipse Temurin (Adoptium) JDK 21** — no un JRE, el **JDK**.
- **Windows / macOS / Linux:** descarga de https://adoptium.net (Temurin 21, LTS).
- **macOS (alternativa):** `brew install --cask temurin@21`
- **Linux (alternativa):** el paquete `temurin-21-jdk` del repo de Adoptium.

**Verificar:**
```bash
java -version      # debe decir "21.x"
javac -version     # debe decir "javac 21.x"
```
> Si tienes varios JDK y sale otra versión: define `JAVA_HOME` apuntando al 21 (Windows: Variables de entorno; mac/Linux: en tu `~/.zshrc`/`~/.bashrc`). En IntelliJ además: *Project Structure → SDK → 21*.

## 2. IntelliJ IDEA (obligatorio, semana 1)

**Community Edition** es suficiente. https://www.jetbrains.com/idea/download
Al primer arranque, en *Project Structure → SDK*, selecciona el JDK 21. Instala el plugin de GitHub Copilot **solo cuando llegue la semana 5** (o desde ya si ya tienes licencia).

## 3. Maven (obligatorio, semana 1)

- **macOS:** `brew install maven`
- **Windows:** descarga de https://maven.apache.org, descomprime y agrega `bin/` al `PATH` — o deja que IntelliJ use su Maven embebido.
- **Linux:** `sudo apt install maven` (o el equivalente).

**Verificar:**
```bash
mvn -version       # "Apache Maven 3.9.x" y "Java version: 21"
```
> La **primera** compilación descarga muchas dependencias ("medio internet"). Es normal y ocurre una sola vez.

## 4. Git + cuenta de GitHub (obligatorio, semana 1)

Instala Git (https://git-scm.com) y crea una cuenta en https://github.com si no tienes.

**Verificar y configurar:**
```bash
git --version
git config --global user.name  "Tu Nombre"
git config --global user.email "tu@correo.com"
```

**Autenticación (crítico):** GitHub **ya no acepta tu contraseña** al hacer `push`. Usa un **Personal Access Token (PAT)**:
1. GitHub → *Settings → Developer settings → Personal access tokens → Tokens (classic) → Generate new token*.
2. Scope: **`repo`**. Cópialo (no lo vuelves a ver).
3. La primera vez que hagas `push`, cuando pida "password", pega el **PAT**.
   - **Windows:** Git Credential Manager lo guarda solo.
   - **macOS:** el Keychain lo guarda solo.

## 5. Docker Desktop (semana 3 — instalar en día 0)

https://www.docker.com/products/docker-desktop
- **Windows:** requiere **WSL2** activado (el instalador te guía; puede pedir reiniciar). Hazlo con tiempo, no la víspera.
- **macOS:** elige la imagen para tu chip (Apple Silicon / Intel).

**Verificar (con Docker Desktop abierto):**
```bash
docker --version
docker run hello-world      # descarga y corre una imagen mínima -> mensaje de éxito
```
> **Tarea previa a la semana 3** (te la recuerdan): tener `docker run hello-world` en verde y pre-descargar las imágenes base para no esperar en clase:
> ```bash
> docker pull postgres:16
> docker pull maven:3.9-eclipse-temurin-21
> docker pull eclipse-temurin:21-jre
> ```

## 6. Google Chrome (semana 4 — QE)

Instala **Google Chrome** (https://www.google.com/chrome). Selenium 4 (con **Selenium Manager**) descarga solo el `chromedriver` correcto — **no** instales chromedriver a mano.

**Verificar:** que Chrome abra y sepas su versión (menú → Ayuda → Información de Google Chrome).

## 7. Cuenta AWS (semana 3 — opcional, con Plan B)

Solo si el cliente provee/pide cuentas AWS. **Créala con días de anticipación** (la verificación de tarjeta/identidad puede tardar). El **primer ejercicio** de ese día es crear una **alarma de facturación** de $5 — juniors + cuenta propia = facturas sorpresa; se previene desde el minuto uno, y **todo recurso se apaga/termina al cierre del día**.
> Si no hay cuentas AWS, la semana 3 es **impartible completa** con su Plan B (topología simulada en Docker compose local).

## 8. Licencia de GitHub Copilot (semana 5 — opcional, con fallback)

Idealmente un asiento **Copilot Pro/Business** provisto por el cliente, activado en tu cuenta de GitHub. Si no, existe **Copilot Free** (pocas completions/chats al mes: alcanza para practicar racionando, no para la semana entera).
- Plugin en **IntelliJ** (principal) y extensión en **VS Code** (se usa en D4 para agent mode).
- `gh` CLI autenticado (`gh auth login`) para la parte de Copilot en la terminal.

---

## Checklist final (todo debe ser "sí")

- [ ] `java -version` y `javac -version` dicen **21**
- [ ] IntelliJ instalado, con el JDK 21 seleccionado
- [ ] `mvn -version` dice 3.9.x sobre Java 21
- [ ] `git --version` OK, `user.name`/`user.email` configurados
- [ ] Cuenta de GitHub + **PAT** con scope `repo` generado y guardado
- [ ] Docker Desktop instalado y `docker run hello-world` en verde (semana 3)
- [ ] Google Chrome instalado (semana 4)
- [ ] *(si aplica)* Cuenta AWS creada y verificada (semana 3)
- [ ] *(si aplica)* Licencia de GitHub Copilot activada (semana 5)

> **Problemas comunes:** JDK equivocado tomado por IntelliJ → *Project Structure → SDK*. `push` rechazado → usa el PAT, no la contraseña. Docker no arranca en Windows → falta WSL2. Ninguno de estos debe resolverse en vivo el día 1: por eso este día 0 existe.
