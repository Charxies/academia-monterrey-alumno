# TaskFlow CLI v2 — Lab del Día 3 (starter)

Proyecto starter del **Día 3 de Java** (Colecciones y Generics). Trae el modelo del dominio
YA RESUELTO de ayer (con el puente objeto->id aplicado), un archivo por mini-práctica
(MP-1 a MP-9) + `Caja.java`, el repositorio a completar y tu **integrador** (`Main.java`).

Todo el proyecto **compila tal cual está**: los huecos son comentarios `TODO` con pistas
—tu trabajo es llenarlos—. Nada aquí está roto; solo incompleto.

> Sigues sobre TU MISMO proyecto TaskFlow (mismo GAV `com.taskflow:taskflow`). Si trabajas
> en tu repo, aplica primero el **puente objeto->id** del warm-up: en tu `Task`, cambia
> `Project project` por `Long projectId` y `User assignee` por `Long assigneeId` (y ajusta
> constructor, `Task.crear` y la regla de `setStatus`). El objeto `Project` y su encabezado
> salen del CLI: vuelven en D5 y como entidades JPA en S2.

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
mvn -q compile exec:java "-Dexec.mainClass=com.taskflow.practicas.MP01ListaTareas"
```

> Las comillas alrededor de `-Dexec.mainClass=...` son obligatorias en PowerShell y no
> estorban en macOS/Linux — úsalas siempre.

## Mapa de archivos

### Modelo (`com.taskflow.model`) — ya resuelto (base del día)

| Archivo | Estado | Qué haces |
|---|---|---|
| `Task.java` | **completo** (forma canónica: id, projectId, assigneeId) | Solo MP-6: añadir `equals`/`hashCode` por id |
| `TaskStatus.java` / `Priority.java` | **completos** | nada (`Priority` va en orden `LOW, MED, HIGH`: de él depende el sort) |

### Excepción (`com.taskflow.exception`)

| Archivo | Estado | Qué haces |
|---|---|---|
| `TaskValidationException.java` | **completa** | nada (se reutiliza tal cual de D2) |

### Repositorio (`com.taskflow.repository`)

| Archivo | Estado | Qué haces |
|---|---|---|
| `InMemoryTaskRepository.java` | firmas + TODOs | `save` (upsert + id auto), `findById`, `findAll` (copia defensiva), `deleteById`, `nextId` |

### Mini-prácticas (`com.taskflow.practicas`)

| # | Archivo | Tema |
|---|---|---|
| MP-1 | `MP01ListaTareas.java` | `List<Task>`; trampa `remove(int)` vs `remove(Object)` |
| MP-2 | `MP02BorrarSeguro.java` | `ConcurrentModificationException`; `removeIf` / `Iterator.remove` |
| MP-3 | `MP03Inmutables.java` | `List.of` inmutable; copia mutable |
| MP-4 | `MP04MapaRepositorio.java` | `Map<Long,Task>`; `getOrDefault`; trampa `get(1)` vs `get(1L)` |
| MP-5 | `MP05EtiquetasUnicas.java` | `Set` sin duplicados; `contains` O(1) |
| MP-6 | `MP06HashSetRoto.java` | `equals` sin `hashCode` = HashSet roto (añade `hashCode`) |
| MP-7 | `MP07OrdenNatural.java` | `Comparable` / `compareTo` |
| MP-8 | `MP08Comparadores.java` | clase anónima + `comparing`/`thenComparing`/`nullsLast` |
| MP-9 | `MP09Genericos.java` + `Caja.java` | raw type -> `ClassCastException`; clase genérica `<T>` |
| Integrador | `Main.java` | **TaskFlow CLI v2** (repositorio en memoria) |

## El integrador: TaskFlow CLI v2

Evoluciona tu CLI v1: los datos salen del `main` y viven en `InMemoryTaskRepository`.

- [ ] **Repositorio:** `save` (upsert + id auto), `findById`, `findAll` (copia defensiva), `deleteById`.
- [ ] **Agregar tarea** — `Task.crear(...)` en try/catch; prioridad `1=LOW 2=MED 3=HIGH`; `dueDate` con `leerFecha`; el id lo asigna `save` -> "Creada tarea #N".
- [ ] **Listar tareas** — `findAll()` ordenado (prioridad HIGH->LOW, luego `dueDate` asc nulls last); tabla `printf`; `*` en vencidas; lista vacía -> mensaje.
- [ ] **Completar por id** — `findById`; `null` -> "No existe"; `setStatus(DONE)` con la regla "no DONE sin assignee".
- [ ] **Eliminar por id** — boolean de `deleteById`; no crashea con id inexistente ni con entrada no numérica.
- [ ] **Resumen por estado** — `Map<TaskStatus,Integer>` con `getOrDefault` sobre `findAll()`.
- [ ] El menú **NUNCA toca el `Map`**: todo pasa por el repositorio.

**Entregable:**

```bash
git add .
git commit -m "feat: taskflow cli v2 - repositorio en memoria con Map<Long,Task>"
git push
```

## Definition of Done del día

(a) compila y corre; (b) alta/listado/completado/borrado/resumen funcionan SOLO a través de
`InMemoryTaskRepository` e ids autoincrementales asignados por `save`; (c) el listado sale
ordenado por prioridad HIGH->LOW (y fecha como desempate); (d) eliminar/completar un id
inexistente, completar sin assignee y las entradas inválidas NO crashean; (e) `Task` tiene
`equals`/`hashCode` por id (verificable con dos tareas del mismo id en un `HashSet`);
(f) commiteado y pusheado; (g) explicas tu código en 1-2 min (en particular: por qué `Map`
y no `List` para buscar por id).

## Stretch goals (solo si terminaste)

1. **Etiquetas por tarea** con `Set<String>` (rechaza duplicados avisando).
2. **Asignar por id** — fija el `assigneeId` (habilita completar una tarea que nació sin responsable).

> Guarda con cariño `InMemoryTaskRepository`: en S2D1 le ponemos `@Repository` encima y Spring
> lo inyecta. Acabas de escribir tu primera capa de persistencia.
