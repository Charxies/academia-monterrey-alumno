# TaskFlow CLI v1 — Lab del Día 2 (starter)

Proyecto starter del **Día 2 de Java** (POO + Excepciones). Contiene los modelos del
dominio (esqueletos con TODOs), un archivo por mini-práctica (MP-1 a MP-9) y tu
**integrador** (`Main.java`, que hoy trae tu CLI v0 de ayer lista para refactorizar).

Todo el proyecto **compila tal cual está**: los huecos son comentarios `TODO` con pistas
—tu trabajo es llenarlos—. Nada aquí está roto; solo incompleto.

> Sigues sobre TU MISMO proyecto TaskFlow del Día 1 (mismo GAV `com.taskflow:taskflow`).
> Si trabajas en tu repo, copia estos archivos nuevos (paquetes `model/`, `exception/`,
> `practicas/`) y sustituye tu `Main.java` por el refactor cuando lo tengas.

## Requisitos previos

```bash
java -version    # debe decir 21
```

## Cómo abrir y correr

1. IntelliJ IDEA → **Open** → selecciona **esta carpeta `lab/`** (la que tiene `pom.xml`).
2. Deja que importe el proyecto Maven. Si marca SDK: **File → Project Structure → SDK → 21**.

Cada archivo tiene su propio `main`. Desde terminal:

```bash
# El integrador (Main):
mvn -q compile exec:java

# Una práctica específica:
mvn -q compile exec:java "-Dexec.mainClass=com.taskflow.practicas.MP01TaskCampos"
```

> Las comillas alrededor de `-Dexec.mainClass=...` son obligatorias en PowerShell y no
> estorban en macOS/Linux — úsalas siempre.

## Mapa de archivos

### Modelos (`com.taskflow.model`) — los construyes a lo largo del día

| Archivo | Estado inicial | Qué haces |
|---|---|---|
| `Task.java` | esqueleto vacío | El grueso del día: MP-1..MP-4 + MP-8 + integrador |
| `TaskStatus.java` | enum con 3 constantes | MP-4: agregar campo `etiqueta` |
| `Priority.java` | enum con 3 constantes | MP-4: agregar campo `etiqueta` |
| `Role.java` | **completo** | nada |
| `User.java` | record (header) | MP-6: `implements Describible` |
| `Project.java` | esqueleto vacío | Integrador: campos, constructor, getters, toString |
| `Describible.java` | **completa** (interface) | nada |

### Excepción (`com.taskflow.exception`)

| Archivo | Estado | Qué haces |
|---|---|---|
| `TaskValidationException.java` | **completa** | NO la tocas; en MP-8 la LANZAS desde `Task` |

### Mini-prácticas (`com.taskflow.practicas`)

| # | Archivo | Tema |
|---|---|---|
| MP-1 | `MP01TaskCampos.java` | Clase, campos, constructor, `this` |
| MP-2 | `MP02Encapsulacion.java` | `private` + getters/setters + `toString` |
| MP-3 | `MP03Comportamiento.java` | `dueDate` + `estaVencida()` |
| MP-4 | `MP04Enums.java` | `TaskStatus`/`Priority` con etiqueta; `==` entre enums |
| MP-5 | `MP05Polimorfismo.java` | abstract + herencia + dispatch dinámico |
| MP-6 | `MP06RecordUser.java` | `record User` + interface `Describible` |
| MP-7 | `MP07Excepciones.java` | try/catch/finally; NPE, AIOOBE, NumberFormat |
| MP-8 | `MP08Validacion.java` | validación en `Task` (constructor + `crear`) |
| MP-9 | `MP09LecturaRobusta.java` | `leerEntero` / `leerFecha` (se copian al integrador) |
| Integrador | `Main.java` | **TaskFlow CLI v1** |

## El integrador: TaskFlow CLI v1

Refactor de tu CLI v0 con la POO del día:

- [ ] **Modelos:** `Task` (validación + factory `crear`), `record User` + `Role`, clase `Project`.
- [ ] **Cero arrays paralelos:** `Task[] tareas = new Task[20]` + `int numTareas`.
- [ ] **Cero literales String** de estado/prioridad: enums en todo el flujo.
- [ ] **Ver tareas** — tabla `printf` por getters; etiquetas de enum; `*` en vencidas; encabezado con proyecto + owner.
- [ ] **Ver resumen** — conteo con `switch` sobre `TaskStatus`.
- [ ] **Agregar tarea** — `Task.crear(...)` en try/catch; prioridad con `valueOf` + repregunta; `dueDate` con `leerFecha`.
- [ ] **Completar tarea** — `setStatus(DONE)` con la regla "no DONE sin assignee".
- [ ] Toda opción numérica del menú por `leerEntero`: entrada no numérica NO crashea.
- [ ] Validación SOLO dentro de `Task`; `Main` solo orquesta.

**Entregable:**

```bash
git add .
git commit -m "feat: taskflow cli v1 - modelo POO con enums y validacion"
git push
```

## Definition of Done del día

(a) compila y corre; (b) datos inválidos al agregar/completar muestran mensaje claro y
NO crashean, y vuelven al menú; (c) no queda ni un array paralelo ni un literal String de
estado/prioridad; (d) commiteado y pusheado; (e) explicas por qué la validación vive dentro
de `Task` (constructor/factory/`setStatus`) y por qué `crear` valida la fecha pero el
constructor no (creación de negocio vs rehidratación).

## Stretch goals (solo si terminaste)

1. Búsqueda por título adaptada a objetos (case-insensitive).
2. `toString()` de `Project` que incluya al owner.
3. Opción "Asignar tarea": elige una tarea y asígnale el `User` demo (habilita completar una
   que nació sin assignee).

> El `Task[20]` que se llena y el `for` a mano para buscar duelen a propósito: mañana (D3)
> llegan `List`, `Set`, `Map` y generics — el repositorio en memoria con CRUD de verdad.
