# Guía de respaldo — Instalar JDK 21 (Temurin) en 10 minutos

> Para el alumno que llega al Día 1 **sin JDK 21** (o con una versión vieja). El cliente se comprometió a preinstalarlo, pero MP-1 verifica, no asume. Sigue SOLO la sección de tu sistema operativo y luego la verificación final.
>
> Usamos **Eclipse Temurin 21 (LTS)** de [adoptium.net](https://adoptium.net) — es OpenJDK, gratuito y el build más usado en la industria.

## Windows

1. Entra a <https://adoptium.net/temurin/releases/?version=21&os=windows> y descarga el instalador **`.msi`** para **x64** (JDK, no JRE).
2. Ejecuta el `.msi`. En la pantalla de componentes, activa estas dos opciones (vienen desactivadas):
   - **Add to PATH** ✅
   - **Set JAVA_HOME variable** ✅
3. Termina el instalador con los demás valores por defecto.
4. **Cierra TODAS las terminales abiertas y abre una nueva** (PowerShell) — las terminales viejas no ven el PATH nuevo. Este paso se salta el 90% de la gente; no seas del 90%.

## macOS

Opción A — instalador gráfico:

1. Entra a <https://adoptium.net/temurin/releases/?version=21&os=mac> y descarga el **`.pkg`** para tu chip: **aarch64** si tu Mac es Apple Silicon (M1/M2/M3/M4), **x64** si es Intel (menú  → Acerca de este Mac te lo dice).
2. Ejecuta el `.pkg` con todos los valores por defecto. Listo: macOS lo registra solo.

Opción B — si ya usas Homebrew (más rápido):

```bash
brew install --cask temurin@21
```

## Linux (Ubuntu/Debian)

```bash
sudo apt update && sudo apt install -y temurin-21-jdk 2>/dev/null || {
  # Si el paquete no está en tus repos, agrega el repositorio de Adoptium:
  sudo apt install -y wget apt-transport-https gpg
  wget -qO - https://packages.adoptium.net/artifactory/api/gpg/key/public | sudo gpg --dearmor -o /etc/apt/keyrings/adoptium.gpg
  echo "deb [signed-by=/etc/apt/keyrings/adoptium.gpg] https://packages.adoptium.net/artifactory/deb $(. /etc/os-release && echo $VERSION_CODENAME) main" | sudo tee /etc/apt/sources.list.d/adoptium.list
  sudo apt update && sudo apt install -y temurin-21-jdk
}
```

En Fedora/RHEL: `sudo dnf install temurin-21-jdk` (con el repo de Adoptium equivalente).

## Verificación (los 3 sistemas)

En una terminal **NUEVA**:

```bash
java -version
```

Debe decir `openjdk version "21...`. Si sigue diciendo 8/11/17 o `command not found`, tienes varios JDK conviviendo o el PATH no se actualizó → es el **punto de dolor #1** de `instructor.md` (§9.1): ahí está la solución paso a paso por SO (`JAVA_HOME`, PATH, y cómo apuntar IntelliJ al 21).

## Que IntelliJ lo tome

1. `File → Project Structure → Project → SDK` → elige **21**.
2. Si no aparece en la lista: `Add SDK → Detect` (IntelliJ suele encontrarlo solo) o `Add SDK → JDK…` apuntando a la carpeta instalada.
3. En el mismo diálogo, `Language level: 21`.

> Atajo de emergencia: si nada de lo anterior funciona y la sesión ya avanza, IntelliJ puede **descargarte el JDK él mismo**: `Add SDK → Download JDK… → 21 → Eclipse Temurin`. Con eso sigues el día desde el IDE y la terminal se arregla en un descanso.
