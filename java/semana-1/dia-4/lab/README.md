# TaskFlow CLI v3 — Lab del Día 4 (starter)

Proyecto starter del **Día 4 de Java** (Lambdas y Streams). Trae el modelo y el repositorio
YA RESUELTOS de ayer (D3), un archivo por mini-práctica (MP-1 a MP-9), la clase de reportes a
completar (`ReportService`) y tu **integrador** (`Main.java`).

Todo el proyecto **compila tal cual está**: los huecos son comentarios `TODO` con pistas
—tu trabajo es llenarlos—. Nada aquí está roto; solo incompleto.

> Sigues sobre TU MISMO proyecto TaskFlow (mismo GAV `com.taskflow:taskflow`). El modelo, la
> excepción de validación y la semilla vienen resueltos para que nadie arrastre bugs de días
> anteriores al tema de hoy: streams y Optional.

## Requisitos previos

```bash
java -version    # debe decir 21
```

## Cómo abrir y correr

1. IntelliJ IDEA → **Open** → selecciona **esta carpeta `lab/`** (la que tiene `pom.xml`).
2. Deja que importe el proyecto Maven. Si marca SDK: **File → Project Structure → SDK → 21**.

Cada mini-práctica tiene su propio `main`. Desde terminal:

```bash
# El integrador (Main):
mvn -q compile exec:java

# Una práctica específica:
mvn -q compile exec:java "-Dexec.mainClass=com.taskflow.practicas.MP04FilterCollect"
```

> Las comillas alrededor de `-Dexec.mainClass=...` son obligatorias en PowerShell y no
> estorban en macOS/Linux — úsalas siempre.

## Mapa de archivos

### Provisto y resuelto (NO lo tocas — base del día)

| Archivo | Qué es |
|---|---|
| `model/Task.java`, `TaskStatus.java`, `Priority.java` | Modelo canónico S1 (idéntico a D3) |
| `exception/TaskValidationException.java` | Excepción de validación (checked) de D2 |
| `exception/TaskNotFoundException.java` | **NUEVA hoy** (unchecked, con el id): la usa `orElseThrow` |
| `util/SeedData.java` | 9 tareas semilla (≥2 por estado, vencidas, 1 sin fecha, sin asignar, "API" repetida) |

### Repositorio (`com.taskflow.repository`)

| Archivo | Estado | Qué haces |
|---|---|---|
| `InMemoryTaskRepository.java` | CRUD de D3 resuelto | **La cirugía:** cambiar `findById` a `Optional<Task>` (MP-8 / integrador) |

### Mini-prácticas (`com.taskflow.practicas`)

| # | Archivo | Tema |
|---|---|---|
| MP-1 | `MP01ComparatorLambda.java` | Comparator anónimo → lambda → `comparing` (las 3 versiones) |
| MP-2 | `MP02InterfacesFuncionales.java` | `Predicate` / `Function` / `Consumer` / `Supplier` + composición |
| MP-3 | `MP03MethodReferences.java` | lambda → method reference, con criterio |
| MP-4 | `MP04FilterCollect.java` | `filter` + `collect(toList)` |
| MP-5 | `MP05MapSorted.java` | `filter → sorted → map → collect` + el anti-patrón comentado |
| MP-6 | `MP06CountAnyMatch.java` | `count` / `anyMatch` / `noneMatch` |
| MP-7 | `MP07GroupingBy.java` | `groupingBy` → `List` vs `counting()` → `Long` |
| MP-8 | `MP08OptionalRepository.java` | cirugía `findById` → `Optional` en una copia local |
| MP-9 | `MP09FindFirst.java` | `findFirst()` → `Optional<Task>` |

### Servicio e integrador

| Archivo | Estado | Qué haces |
|---|---|---|
| `service/ReportService.java` | firmas + TODOs | Los 5 reportes con Streams (**sin `for`/`while`**) |
| `Main.java` | CRUD de v2 resuelto | Submenú **Reportes** + **Buscar por id** + cirugía de `Optional` |

## El integrador: TaskFlow CLI v3

Evoluciona tu CLI v2. Dos frentes:

- [ ] **Reportes en `ReportService`** (retornan datos; el menú imprime). Sin `for`/`while`:
  - [ ] Tareas por estado — `groupingBy(Task::getStatus)`.
  - [ ] Pendientes por fecha — `filter` + `sorted(... nullsLast ...)` (hay 1 tarea sin fecha).
  - [ ] Buscar por título — `filter` + `contains` case-insensitive (vacío → mensaje, no crashea).
  - [ ] % completadas — `count()` de `DONE` × `100.0` / total (ojo la división entera de D1).
  - [ ] Tareas por asignado — `filter(assigneeId != null)` + `groupingBy`; las demás → `Sin asignar: n`.
- [ ] **Cirugía de `Optional`:** `findById` → `Optional<Task>`; **Buscar por id** con `ifPresentOrElse`;
  **Completar por id** con `orElseThrow(() -> new TaskNotFoundException(id))`. Cero `null`, cero `.get()`.

**Entregable:**

```bash
git add .
git commit -m "feat: taskflow cli v3 - reportes con streams y Optional en repositorio"
git push
```

## Definition of Done del día

(a) compila y corre; (b) los 5 reportes producen salida correcta y su lógica NO contiene
`for`/`while` (busca `for (` / `while (` en `ReportService` = 0; el `do-while` del menú sí se
permite); (c) `findById` devuelve `Optional<Task>`, no hay ningún `Optional.get()` en el
proyecto, y un id inexistente muestra mensaje claro sin stack trace; (d) commiteado y pusheado;
(e) explicas tu código en 1–2 min (pregunta de control: "¿qué tipo devuelve tu `groupingBy` y por qué?").

## Stretch goals (solo si terminaste)

1. **Tareas por prioridad** — `groupingBy(Task::getPriority)`.
2. **Top 3 urgentes** — `sorted` (prioridad desc + fecha) + `limit(3)`.
3. **Exportar títulos a CSV** — `map(Task::getTitle).collect(Collectors.joining(", "))`.
4. **Vencidas vs no vencidas** — `partitioningBy(Task::estaVencida)`.

> Mañana (D5): estos reportes ya retornan datos puros sin tocar la consola — los blindamos con
> JUnit (vas a testear ESTOS métodos) y le agregamos persistencia a archivo. Cierre de la semana.
