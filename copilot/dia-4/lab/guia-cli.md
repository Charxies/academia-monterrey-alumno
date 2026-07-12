# Guía — Copilot en la terminal (`gh copilot explain` / `suggest`) · AM-1

> La terminal es otra superficie de Copilot. Dos verbos hoy, y **ninguno ejecuta nada solo**:
> `explain` (descifrar comandos ajenos) y `suggest` (construir comandos de TU flujo). La CLI
> **agéntica** —que sí lee el repo y EJECUTA— es otra herramienta (T2); ⚠ VERIFICAR-PREVIO si tu
> asiento la incluye y con qué comando se invoca.
>
> **Regla de oro del bloque:** ningún comando sugerido se ejecuta sin **leerlo y entenderlo**. Los
> `rm` / `prune` / `reset --hard` no perdonan.

## 0. Checklist de arranque (warm-up)

```bash
gh auth status                                  # debe decir "Logged in"
gh extension install github/gh-copilot          # ⚠ VERIFICAR-PREVIO: nombre/estado de la extensión
gh copilot --help                               # confirma que 'explain' y 'suggest' aparecen
```

- ¿`gh` sin auth? → `gh auth login` (elige HTTPS, navegador).
- ⚠ VERIFICAR-PREVIO: si el asiento del cliente incluye además una **CLI agéntica standalone**, su
  instalación y su comando son DISTINTOS — el instructor te dice cuál aplica.

---

## MP-1 — `gh copilot explain`: descifrar el runbook heredado (15 min)

Insumo: [`comandos-heredados.sh`](comandos-heredados.sh) (léelo como "runbook de ops", **no lo corras**).

Para cada comando, pega el comando dentro de las comillas:

```bash
gh copilot explain "find /var/log/taskflow -name '*.log*' -mtime +14 -size +1M -exec gzip {} +"
gh copilot explain "awk '\$9 ~ /^5/ { count[\$7]++ } END { for (r in count) print count[r], r }' access.log | sort -rn | head"
```

**El ejercicio no es leer la explicación, es AUDITARLA.** Por cada comando contrasta con `man`:

| Pregunta de auditoría | find del [1] | awk del [2] |
|---|---|---|
| ¿Explicó el **porqué** o solo parafraseó flags? | ¿dijo por qué `-exec ... +` invoca gzip **una vez con muchos archivos** en vez de una por archivo (`\;`)? | ¿dijo por qué `^5` captura **toda** la familia 5xx y no solo `500`? |
| ¿Qué te tuvo que aclarar `man`/`--help`? | `-mtime +14` = **más de** 14 días (no "14 días") | el `END {}` corre **una vez al final**, tras acumular |
| ¿Hay algo que **omitió** y es peligroso? | que `gzip` **reemplaza** el original por `.gz` | que el `sort -rn` va fuera del awk (orden del pipe) |

**Entregable de MP-1 (al diario):** por cada comando, 1 cosa que `explain` te aclaró y 1 que tuviste
que verificar aparte. `explain` es un buen primer lector, **no** la última palabra.

---

## MP-2 — `gh copilot suggest`: construir comandos de TU flujo (20 min)

`suggest` te propone un comando a partir de una descripción en lenguaje natural. Elige el tipo con
`-t` (`git` / `shell` / `gh`) si tu versión lo pide ⚠ VERIFICAR-PREVIO. **Lee la sugerencia, NO la
ejecutes a ciegas**: varias de estas borran de verdad.

Construye **4–5** de estos sobre tu `taskflow-api`. Formula el prompt tú; la columna "respuesta
esperada (compara, no copies)" es tu **clave de auto-verificación**, no algo para pegar.

### git

| Pide con `suggest` | Respuesta esperada (compara, no copies) | Trampa a cazar |
|---|---|---|
| "commits que tocaron `TaskController.java` **incluyendo renombres**" | `git log --follow -- src/main/java/com/taskflow/controller/TaskController.java` | Sin `--follow` **pierde** la historia previa al renombre — el detalle es todo el punto |
| "deshaz el último commit **sin perder cambios**" | `git reset --soft HEAD~1` (deja los cambios en staging) | Si te sugiere `--hard`: **rechaza y re-pregunta** — `--hard` borra tus cambios |

### docker

| Pide con `suggest` | Respuesta esperada (compara, no copies) | Trampa a cazar |
|---|---|---|
| "sigue los logs **solo del servicio postgres** del compose" | `docker compose logs -f postgres` (o el nombre real de tu servicio de BD) | Que use el nombre del **contenedor** en vez del **servicio** del compose |
| "borra imágenes **dangling** y contenedores **parados**" | `docker image prune` + `docker container prune` (o `docker system prune`) | **Lee antes de Enter**: `prune` borra de verdad; `system prune -a --volumes` puede llevarse tu volumen de Postgres con datos |

### mvn

| Pide con `suggest` | Respuesta esperada (compara, no copies) | Trampa a cazar |
|---|---|---|
| "corre **solo** `TaskServiceTest`" | `mvn test -Dtest=TaskServiceTest` | Un método suelto: `-Dtest=TaskServiceTest#crear_tituloEnLimite_ok` |
| "empaqueta **sin correr tests**" | `mvn package -DskipTests` | **Cuándo NO hacerlo:** nunca para el artefacto que despliegas sin que CI haya corrido la suite. `-DskipTests` **compila** los tests; `-Dmaven.test.skip=true` ni los compila — saber cuál pediste importa |

**Entregable de MP-2 (al diario):** ejecuta 4–5 sugerencias **tras leerlas** y anota **1 sugerencia
incorrecta o subóptima** que recibiste (el `--hard` de más, el `prune` peligroso, el `-Dtest` con
sintaxis vieja) y cómo la corregiste. Recibir una sugerencia mala y cacharla vale para la rúbrica
tanto como recibir una buena.

---

## Cierre — el puente hacia AM-2

`explain` y `suggest` te dan comandos que **tú** ejecutas. La CLI agéntica y el agent mode de VS Code
(AM-2) suben un escalón: leen el repo y **ejecutan/editan** por su cuenta. Mismo criterio, más en
juego: mañana revisas diffs, no una línea de shell.

## Tabla de comandos del día (CLI)

| Comando | Qué hace |
|---|---|
| `gh copilot explain "<cmd>"` | Explica en lenguaje natural un comando ajeno. NO lo ejecuta |
| `gh copilot suggest -t git "<descripción>"` | Propone un comando (git/shell/gh) desde texto. Lo LEES antes de correr ⚠ flags según versión |
| `gh extension install github/gh-copilot` | Instala la extensión CLI de Copilot ⚠ VERIFICAR-PREVIO |
| `gh auth status` / `gh auth login` | Verifica / inicia sesión de `gh` |
