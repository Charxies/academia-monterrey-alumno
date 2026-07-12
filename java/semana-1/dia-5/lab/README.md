# TaskFlow CLI v1.0 — Lab del Día 5 (starter · Integrador de Semana 1)

Proyecto starter del **Día 5 de Java** (Archivos con `java.nio` + intro a Testing con JUnit 5).
Cierra la Semana 1: hoy TaskFlow deja de perder los datos al cerrar y estrena su primera suite de tests.

Trae **YA RESUELTO** el CLI v3 de ayer (menú + reportes con streams + Optional), el modelo, el
repositorio en memoria, la interfaz `TaskRepository` ya extraída, la semilla `data/tasks.csv`, y
**esqueletos con TODO** para lo de hoy: `CsvTaskParser`, `FileTaskRepository`, las 3 prácticas y
las 4 clases de test.

> Todo el proyecto **compila tal cual** y **`mvn test` corre** desde el minuto cero: los huecos son
> comentarios `TODO`. Nada está roto; solo incompleto. (Los tests-TODO salen "verdes" con el cuerpo
> vacío — eso es a propósito: es el "test verde mentiroso" de MP-6.)

## Requisitos previos

```bash
java -version    # debe decir 21
```

## Cómo abrir y correr

1. IntelliJ IDEA → **Open** → selecciona **esta carpeta `lab/`** (la que tiene `pom.xml`).
2. Deja que importe Maven. Si marca SDK: **File → Project Structure → SDK → 21**.
3. **Corre siempre desde la raíz del proyecto** (la carpeta con `data/`), o la ruta relativa
   `data/tasks.csv` "no se encontrará" (punto de dolor #1 del día — ver MP-1).

```bash
# El integrador (Main):
mvn -q compile exec:java

# Una práctica específica:
mvn -q compile exec:java "-Dexec.mainClass=com.taskflow.practicas.MP01LecturaArchivo"

# La suite de tests:
mvn test
```

> Las comillas alrededor de `-Dexec.mainClass=...` son obligatorias en PowerShell y no estorban
> en macOS/Linux — úsalas siempre.

## Mapa de archivos

### Provisto y resuelto (NO lo tocas — base del día)

| Archivo | Qué es |
|---|---|
| `model/Task.java`, `TaskStatus.java`, `Priority.java` | Modelo canónico S1 (validaciones intactas) |
| `model/Project.java`, `User.java`, `Role.java`, `Describible.java` | Dominio de D2 (semilla hardcodeada; no persisten hoy) |
| `exception/TaskValidationException.java`, `TaskNotFoundException.java` | Excepciones de S1 |
| `repository/TaskRepository.java` | **Interfaz NUEVA** (ya extraída del repo en memoria — paso 1) |
| `repository/InMemoryTaskRepository.java` | CRUD de D3/D4 (ahora `implements TaskRepository`) |
| `service/ReportService.java` | Reportes con streams (constructor recibe la interfaz) |
| `data/tasks.csv` | Semilla: 6 tareas (estados/prioridades variados, 1 sin fecha, 1 vencida) |

### Tu trabajo de hoy (esqueletos con TODO)

| Archivo | Qué haces |
|---|---|
| `persistence/CsvTaskParser.java` | `parse(String)->Task` y `format(Task)->String` (MP-2/MP-3) |
| `persistence/FileTaskRepository.java` | `implements TaskRepository`; `load()`/`save()` (Integrador) |
| `Main.java` | Cablear **cargar al arrancar** (TODO 1) y **guardar al salir** (TODO 2) |
| `practicas/MP01LecturaArchivo.java` | `Files.readAllLines` + `toAbsolutePath()` |
| `practicas/MP03EscrituraArchivo.java` | escribir CSV + round-trip (usa `CsvTaskParser`) |
| `practicas/MP04RaceCondition.java` | 2 hilos + contador compartido + `join()` (threading awareness) |
| `src/test/.../TaskValidationTest.java` | validaciones de `Task` (MP-5/MP-6) |
| `src/test/.../InMemoryTaskRepositoryTest.java` | repositorio (MP-7) |
| `src/test/.../ReportServiceTest.java` | reportes (MP-7 — paga la promesa de D4) |
| `src/test/.../CsvTaskParserTest.java` | parser, con el caso "línea corrupta" (MP-8, rojo-verde) |

## Formato CSV canónico del día (8 columnas)

```
id,title,description,status,priority,projectId,assigneeId,dueDate
1,Configurar repositorio,Setup inicial en GitHub,DONE,HIGH,1,1,2026-06-20
```

- Campos opcionales vacíos = cadena vacía → `null` al parsear (`assigneeId`, `dueDate`).
- Fechas en ISO (`yyyy-MM-dd`). `parse` **rehidrata** (constructor, no `Task.crear`): una tarea
  vencida es dato legal al releer.
- Limitación documentada: `description` **no** puede llevar comas (una línea con nº de campos ≠ 8
  → excepción clara del parser; hay test para eso en MP-8). Por esto existen OpenCSV/Jackson.

## El integrador: TaskFlow CLI v1.0 FINAL

Sobre el CLI v3, cierra la primera versión COMPLETA del capstone (funcional + persistente + probada).

- [ ] **Paso 1 — interfaz `TaskRepository`:** ya viene extraída en el starter (si traes TU repo,
  hazlo con IntelliJ: Refactor → Extract Interface, con `save`/`findById`/`findAll`/`deleteById`).
- [ ] **Persistencia — `FileTaskRepository`** (implements `TaskRepository`, compone
  `InMemoryTaskRepository` + `CsvTaskParser`):
  - [ ] `load()`: si `data/tasks.csv` existe → cargar con `save` (id explícito avanza la secuencia);
    si no → arrancar vacío **sin crashear** y avisar "sin datos previos".
  - [ ] `save()` (snapshot): al Salir, escribir todo al CSV con try-with-resources; confirmar "N guardadas".
  - [ ] La `IOException` se maneja **en** `FileTaskRepository`; el menú nunca muere por un archivo.
- [ ] **Cablear en `Main`:** TODO 1 (cargar al arrancar) y TODO 2 (guardar al salir).
- [ ] **Suite verde — mínimo 8 tests reales** (ninguno vacío): ≥2 validaciones, ≥2 repositorio,
  ≥2 `ReportService`, ≥2 parser CSV. `mvn test` en verde es requisito de entrega.
- [ ] **README** corto del proyecto en la raíz de tu repo.

**Prueba de fuego (así se verifica el DoD):** crear una tarea → Salir → volver a abrir → sigue ahí.

**Entregable:**

```bash
git add .
git commit -m "feat: taskflow cli v1.0 - persistencia y tests"
git push
```

## Definition of Done del día

(a) Prueba de fuego: crear tarea → salir → reabrir → persiste (y sin `tasks.csv` previo arranca
vacío sin crashear); (b) `mvn test` verde con **≥8 tests reales** (ningún test vacío ni assert de
mentira); (c) README corto en la raíz del repo; (d) commiteado y pusheado como
`feat: taskflow cli v1.0 - persistencia y tests`; (e) explicas el flujo cargar/guardar y UN test
tuyo en 1–2 min.

## Stretch goals (solo si terminaste)

1. **Guardar tras CADA operación mutadora** (no solo al salir).
2. **`FileTaskRepositoryTest` de round-trip a archivo con `@TempDir`** (no tocar el `data/tasks.csv`
   real — punto de dolor #10).
3. **Escapado simple de comas** en `description` (reemplazo por `;`, documentado).

> **Preview S2 (lunes):** este mismo dominio se vuelve API REST con Spring Boot. El repositorio ya
> lo tienen (por eso extrajimos `TaskRepository`): solo cambia quién lo llama — hoy un menú de
> Scanner, el lunes un controller HTTP.
