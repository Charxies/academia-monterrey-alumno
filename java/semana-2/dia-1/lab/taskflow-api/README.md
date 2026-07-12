# taskflow-api — starter (Semana 2, Día 1)

Red de seguridad para el integrador de S2D1. **El camino principal es GENERARLO tú** con Spring
Initializr (ver `alumno.md`, MP-6) y copiar el dominio de TU propia consola. Este starter existe
para quien se atoró con el wizard o la red.

Estado de este starter = **lo que deja MP-6** (proyecto Initializr) + el dominio de S1 ya copiado +
los esqueletos de hoy con TODOs. **Compila y arranca** (sin sembrar: los `run(...)` están vacíos).

```bash
mvn spring-boot:run   # arranca, corre el SeedRunner vacío y termina (correcto: hoy sin web)
mvn test              # corre; contextLoads verde + TaskServiceTest con cuerpos vacíos (verde mentiroso)
```

## Huecos a completar (integrador)

| Archivo | Qué hacer |
|---|---|
| `repository/InMemoryTaskRepository` | anotar `@Repository` (paso 4, tras el checkpoint-error #3a). Hoy viene SIN anotar A PROPÓSITO. |
| `service/TaskOrders` | implementar las 3 estrategias `Comparator<Task>` (MP-3): reemplazar los `(a,b)->0`. |
| `service/ReportService` | anotar `@Service` (paso 2) y completar la sobrecarga `pendientes(Comparator)`. |
| `service/TaskService` | anotar `@Service` (paso 3) y rellenar `crear` / `listar` / `completar`. |
| `runner/SeedRunner` | recibir `TaskService` por constructor, sembrar 5 tareas y listarlas (paso 4). |
| `src/test/.../TaskServiceTest` | escribir los 6 cuerpos de test (hoy vacíos = verde mentiroso). |

## Checkpoint-error esperado (paso 4)

Al anotar `TaskService`/`ReportService` con `@Service` y arrancar ANTES de anotar el repositorio:

```
Parameter 0 of constructor in com.taskflow.service.TaskService required a bean of type
'com.taskflow.repository.TaskRepository' that could not be found.

Action: Consider defining a bean of type 'com.taskflow.repository.TaskRepository' in your configuration.
```

Léelo COMPLETO: dice qué falta y quién lo pedía. Solución: `@Repository` en la CLASE
`InMemoryTaskRepository` (nunca en la interfaz).

## Reglas del starter

- Todo vive bajo `com.taskflow` (el `@ComponentScan` de `@SpringBootApplication` escanea ese package
  y subpackages). `Describible` NO está; `User` va sin su `implements`.
- `FileTaskRepository` / `CsvTaskParser` NO se copian (la persistencia de la API será JPA en D4).
