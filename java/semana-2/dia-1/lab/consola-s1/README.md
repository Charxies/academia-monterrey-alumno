# TaskFlow CLI v1.0 — Solución de referencia (Día 5, Integrador de Semana 1)

Espejo completo y compilable del `lab/` con **todo resuelto**: persistencia a `data/tasks.csv`
con `java.nio`, la demo de race condition, y la suite de tests JUnit 5 en **verde (≥8 tests reales)**.

Es el **cierre de la Semana 1**: TaskFlow deja de ser un CLI en memoria y se vuelve la primera
versión COMPLETA — funcional + persistente + probada. Por eso el commit del día la bautiza `v1.0`
(v0→v3 fueron incrementos diarios; el salto de nombre es narrativa deliberada, no errata).

> **Regla del curso:** esta solución NO se comparte con el grupo hasta cerrar el lab.

## Cómo correr

```bash
# El integrador resuelto (TaskFlow CLI v1.0). Córrelo desde ESTA carpeta (la de data/):
mvn -q compile exec:java

# Una práctica resuelta:
mvn -q compile exec:java "-Dexec.mainClass=com.taskflow.practicas.MP01LecturaArchivo"
mvn -q compile exec:java "-Dexec.mainClass=com.taskflow.practicas.MP03EscrituraArchivo"
mvn -q compile exec:java "-Dexec.mainClass=com.taskflow.practicas.MP04RaceCondition"

# La suite de tests (quality gate del día):
mvn test
```

> Las rutas del CSV son **relativas a la raíz del proyecto** (`data/tasks.csv`). Corre siempre
> desde la carpeta que contiene `data/` (o ajusta el Working directory en la Run Configuration).

## Lo que cambió respecto a v3 (D4)

- **`persistence/CsvTaskParser.java` (nueva):** `parse(String) -> Task` y `format(Task) -> String`
  sobre el formato canónico de 8 columnas. `parse` rehidrata (constructor, **NO** `Task.crear`):
  una tarea vencida es dato legal al releer. `split(",", -1)` conserva los vacíos del final. Línea
  con nº de campos ≠ 8 (o dato corrupto) → `IllegalArgumentException` clara con la línea culpable.
- **`persistence/FileTaskRepository.java` (nueva):** implementa `TaskRepository`, **compone**
  `InMemoryTaskRepository` (CRUD) + `CsvTaskParser`. `load()` carga el CSV al arrancar (o arranca
  vacío sin crashear); `save()` (snapshot, try-with-resources) escribe todo al salir. La
  `IOException` se maneja **aquí**: el menú nunca muere por un archivo.
- **`repository/TaskRepository.java` (nueva interfaz):** extraída de `InMemoryTaskRepository`
  (paso 1 del integrador). `ReportService` y `Main` ahora dependen del contrato, no de la clase.
  En S2D1 esta interfaz es la que Spring inyecta.
- **`Main.java`:** carga al arrancar (`repo.load()`), guarda al salir (`repo.save()`), y el resto
  del menú v3 intacto (reportes con streams + Optional).
- **Suite de tests JUnit 5:** validaciones de `Task`, repositorio, `ReportService` y `CsvTaskParser`.

## Persistencia: cargar / guardar

| Momento | Qué pasa |
|---|---|
| **Al arrancar** | Si `data/tasks.csv` existe → carga cada tarea con `save` (id explícito → la secuencia avanza al máx id). Si NO existe → arranca vacío y avisa "sin datos previos". |
| **Al salir (opción 7)** | Escribe el snapshot completo al CSV (encabezado + 1 línea por tarea) con try-with-resources; confirma "N tareas guardadas". |
| **Ante `IOException`** | Se atrapa en `FileTaskRepository` con mensaje claro; el menú sigue vivo. |

**Prueba de fuego (DoD):** agregar tarea → Salir → volver a abrir → la tarea sigue ahí.

## La suite de tests (quality gate)

`mvn test` corre **≥8 tests reales** (ninguno vacío, ningún assert de mentira):

| Clase | Qué cubre | Nº |
|---|---|---|
| `model/TaskValidationTest` | Validaciones de `Task` (D2): título 2 vs 3 chars, sin proyecto, `dueDate` pasada en `crear` vs rehidratación, `DONE` sin assignee | 8 |
| `repository/InMemoryTaskRepositoryTest` | `save` incremental, id explícito que avanza la secuencia, `findById` presente/vacío, `deleteById` | 6 |
| `service/ReportServiceTest` | `tareasPorEstado` agrupa, `% completadas` sin tareas = 0 (no NaN) y 2/4 = 50% | 3 |
| `persistence/CsvTaskParserTest` | línea válida (8 campos), opcionales vacíos → null, round-trip, **línea corrupta → excepción clara** (rojo-verde) | 6 |
| `persistence/FileTaskRepositoryTest` **(STRETCH)** | round-trip a archivo con `@TempDir`; archivo inexistente arranca vacío | 2 |

Reglas del día que encarnan estos tests: naming `metodo_escenario_resultado`; **AAA**
(arrange-act-assert); `@BeforeEach` da un repo NUEVO por test (independencia); excepción
**específica** (`assertThrows(TaskValidationException.class, ...)`), nunca `Exception` genérico;
`assertEquals(esperado, real)` en ese orden.

## Salida esperada con la semilla (`data/tasks.csv`, 6 tareas)

- **Tareas por estado:** DONE 2 · IN_PROGRESS 2 · TODO 2.
- **% completadas:** `33.3%` (2 de 6).
- **Buscar "api":** 2 coincidencias (`Implementar API de tareas`, `Documentar API REST`).
- **Vencidas:** 1 (`Implementar API de tareas`, `IN_PROGRESS` con fecha `2026-07-05`, ya pasada).
- **Sin asignar:** 1 (`Escribir tests del dominio`).

> La tarea 3 se escribió con `dueDate` en el pasado A PROPÓSITO: carga sin error porque `parse`
> rehidrata (no pasa por `Task.crear`), y así `estaVencida()` da `true`.

## Threading (awareness, no dominio)

`MP04RaceCondition` demuestra por qué el estado compartido mutable es peligroso: 2 hilos suman
100 000 cada uno sobre el mismo contador y el total casi nunca es 200 000; 3 corridas → resultados
distintos (no determinismo). `synchronized`/`AtomicInteger` se estudian en S2 — hoy la lección es
"no compartas estado mutable a la ligera".

## Estructura de paquetes

```
com.taskflow
├── Main                       # CLI v1.0: menú + carga/guardado
├── model/                     # Task, TaskStatus, Priority, Project, User, Role, Describible
├── exception/                 # TaskValidationException (checked), TaskNotFoundException (unchecked)
├── repository/                # TaskRepository (interfaz) + InMemoryTaskRepository
├── service/                   # ReportService (reportes con streams, retorna datos)
├── persistence/               # CsvTaskParser + FileTaskRepository (java.nio)
└── practicas/                 # MP01LecturaArchivo, MP03EscrituraArchivo, MP04RaceCondition
```

## Huecos de HOY (S2D1, mañana de patrones)

Esta consola es la de S1 **completa y en verde** (≥8 tests reales), con dos huecos de MP-3 para
ponerle nombre al patrón Strategy. `mvn test` pasa TAL CUAL: los 2 tests nuevos están vacíos (verde
mentiroso — recuérdalo). Tu trabajo de MP-3:

- **`service/TaskOrders.java` (esqueleto):** implementa las 3 estrategias `Comparator<Task>`
  (`POR_FECHA`, `POR_TITULO`, `POR_URGENCIA`) reemplazando los placeholders `(a, b) -> 0`.
- **`service/ReportService.java`:** completa la sobrecarga `pendientes(Comparator<Task> orden)` y
  haz que `pendientesPorFecha()` delegue en ella con `POR_FECHA` (mvn test debe seguir verde).
- **`service/ReportServiceTest.java`:** escribe el cuerpo de `pendientes_conOrdenUrgencia_vencidasPrimero`
  y `pendientes_conOrdenTitulo_alfabetico`.

> MP-4 (Singleton) NO se toca aquí: es un ejercicio en vivo sobre `InMemoryTaskRepository` que se
> **revierte con `git restore .`**. Es el puente narrativo al contenedor de Spring (`../taskflow-api`).
