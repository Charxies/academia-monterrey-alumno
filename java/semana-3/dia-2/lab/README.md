# TaskFlow API (Semana 3, Día 2) · Starter (estado fin de S3D1)

> Este es el punto de partida de **S3D2 (Docker)**: la API de S3D1 (suite en pirámide + quality gate
> JaCoCo 70%) tal cual. Hoy NO se escribe Java: se añaden los archivos de infraestructura (`Dockerfile`,
> `docker-compose.yml`, perfil `docker`, `.env`) y esta sección **"Correr con Docker"** se completa en el
> integrador. Los esqueletos con `TODO` te guían MP por MP; la versión final vive en `solucion/`.

## Correr con Docker (TODO — integrador paso 4)

Al terminar el día, documenta aquí el ciclo del stack (referencia: `solucion/README.md`):

- **Levantar:** `docker compose up -d --build`  (construye la imagen y levanta `api` + `db`).
- **Variables:** `cp .env.example .env` y rellena `POSTGRES_USER/PASSWORD/DB` + `JWT_SECRET` (12-factor).
- **Logs:** `docker compose logs -f api`.
- **Tirar:** `docker compose down`  ·  **`down -v` BORRA el volumen `pgdata` y TODOS los datos** (a conciencia).
- **Perfiles:** `dev` = H2 local sin Docker (default) · `docker` = stack completo con Postgres.

---

**Testing en profundidad + quality gate.** La API de S2D5 ya funcionaba y estaba segura; hoy la hacemos
**CONFIABLE**: la suite se reorganiza en una **pirámide** (`unit/` · `slice/` · `integration/`), los
`@SpringBootTest` lentos del CRUD regresan a **slices** (`@WebMvcTest`) y se instala **JaCoCo** con un
**quality gate que muerde**: `mvn verify` FALLA bajo 70% de cobertura de líneas.

> Tesis del día: *cada capa se prueba DONDE es barata (unit en ms, slice en s, integración —el lento—
> una sola vez); y "coverage alto" no garantiza nada: mide EJECUCIÓN, no VERIFICACIÓN.*

`src/main` **no se tocó hoy**: los tests de hoy protegen código que ya existía. El día entero vive en
`src/test` y el `pom`. Commit del día: `test: quality gate jacoco 70 + slices`.

## Cómo correr

```bash
mvn test        # RÁPIDO: corre la suite (unit + slice + integration). NO valida el gate.
mvn verify      # EL GATE: corre la suite + jacoco:check (LINE ≥ 70%). Es lo que el CI de S3D4 correrá.
mvn test -Dtest=com.taskflow.slice.TaskControllerTest   # una clase sola (el ciclo de dev, 40×/día)
```

Reporte de cobertura tras `mvn verify`: **`target/site/jacoco/index.html`** (columnas *Missed
Instructions* / *Missed Branches*; drill-down hasta la línea roja/amarilla).

> **La entrega se valida con `mvn verify`, no con `mvn test`** (regla del curso desde hoy). El gate
> `jacoco:check` vive en la fase `verify`: `mvn test` NUNCA lo ejecuta (error intencional #4). Un
> `mvn test` verde no dice nada del umbral.

## Calidad

### (a) Tabla de tiempos — antes (S2D5) vs después (S3D1)

> **De referencia (máquina del autor); los tuyos VARIARÁN, la FORMA no.** Spring **cachea contextos**:
> misma configuración = mismo contexto reutilizado; cada combinación distinta de anotaciones /
> `@MockitoBean` / perfiles = contexto NUEVO. Por eso se miden DOS cosas: la suite completa y —la
> métrica del ciclo real— **una clase sola**.

| Métrica | Antes · S2D5 (todo `@SpringBootTest`) | Después · S3D1 (unit + slice + integración) |
|---|---|---|
| `mvn test` — suite completa | ~7.2 s · **41** tests | ~8.0 s · **66** tests |
| Qué contexto levanta un test de **CRUD** (TaskController) | **COMPLETO** (JPA+seguridad+seeder+web) | **slice web ligero** (solo la capa web) |
| Contextos que arranca la suite | 2 completos (MockMvc + servicio) | 2 completos (`integration/`) **+** 3 ligeros (2 `@WebMvcTest`, 1 `@DataJpaTest`) |
| `mvn test -Dtest=TaskControllerTest` (clase sola) | ~4.4 s · contexto **COMPLETO** | ~3.7 s · **slice ligero** |

Lectura honesta: la suite total NO baja —hay **25 tests MÁS** (unit + slice + E2E) y Spring **cachea**
los contextos completos de todos modos—. Lo que cambia es la NATURALEZA de lo que paga cada test: antes
**cada** test de CRUD levantaba un contexto COMPLETO; ahora el contrato web se prueba en un **slice
ligero** (sin JPA/seguridad/seeder) y el contexto completo se reserva para `integration/`, donde el flujo
entero se prueba **una sola vez**. La métrica que importa es la **clase sola** (el ciclo de dev, 40×/día):
baja ~0.7 s en wall-clock, y su contexto pasó de pesado a ligero. El token DESAPARECIÓ de los tests de
slice: ya no hay filtro que lo exija (`addFilters = false`).

### (b) Inventario de la pirámide

| Nivel | Paquete | # tests | Velocidad | Qué protege |
|---|---|---:|---|---|
| **unit** | `com.taskflow.unit` | **26** | ms (sin Spring) | lógica de dominio/servicio en aislamiento (Mockito) |
| **slice** | `com.taskflow.slice` | **26** | s (contexto ligero) | contratos de capa: web (`@WebMvcTest`) y JPA (`@DataJpaTest`) |
| **integration** | `com.taskflow.integration` | **14** | el lento (contexto completo) | el cableado entero: seguridad + advice + JPA + dominio, JUNTOS |
| | **Total** | **66** | | |

Detalle por clase:

| Clase | Nivel | # | Notas |
|---|---|---:|---|
| `unit/TaskServiceTest` | unit | 7 | Mockito puro (`@Mock`/`@InjectMocks`), `@Nested`, `verify(never())`, `ArgumentCaptor`, `thenThrow` |
| `unit/TaskValidationTest` | unit | 10 | `@ParameterizedTest` `@CsvSource` (6 fronteras 3–120) + null simple + `@MethodSource` |
| `unit/ReportServiceTest` | unit | 9 | Mockito paga la cobertura de `ReportService` (la trampa `long/long`) |
| `slice/TaskControllerTest` | slice | 10 | `@WebMvcTest` + `@MockitoBean` + `addFilters=false`; `@ParameterizedTest` `@ValueSource` (títulos→400) |
| `slice/ProjectControllerTest` | slice | 5 | ídem; `.principal(...)` para el `Authentication` del POST |
| `slice/TaskRepositoryTest` | slice | 11 | `@DataJpaTest`, `flush()+clear()`, `@Sql`, `@ParameterizedTest` `@EnumSource(TaskStatus)` |
| `integration/AuthControllerTest` | integration | 4 | register/login sobre contexto completo |
| `integration/SecurityRulesTest` | integration | 8 | token real (`tokenDe`); `@ParameterizedTest` `@ValueSource` (rutas→401) |
| `integration/FlujoCompletoE2ETest` | integration | 1 | register→login→proyecto→tarea→`DONE`→verificar, con usuario propio |
| `integration/TaskflowApiApplicationTests` | integration | 1 | `contextLoads` |

> La forma sana es una pirámide (muchos abajo, pocos arriba). La de hoy es más un **rectángulo**
> (26/26/14) — y está BIEN: lo que importa es que **cada regla nueva busque el nivel MÁS barato que la
> atrape**. QE S4 retoma la pirámide desde la automatización; esto es la base.

### (c) El gate

- **`jacoco:check` activo en `LINE ≥ 0.70`** (versión fijada **0.8.12**, regla `BUNDLE`/`LINE` amarrada a
  `verify`; se excluye solo `TaskflowApiApplication.class` — arranque sin lógica, decisión PÚBLICA en el pom).
- **`mvn verify` → BUILD SUCCESS**; cobertura global de referencia: **~86% de líneas** (400/463) → *All
  coverage checks have been met.*
- **El gate MUERDE** (se vio fallar una vez, regla de S1D5 aplicada al build): subir el umbral a `0.99` →
  `mvn verify` ROJO con `Rule violated for bundle taskflow-api: lines covered ratio is 0.86, but expected
  minimum is 0.99` → regresar a `0.70`.

## Reorganización de la suite (integrador, paso 1)

Los paquetes de test `controller/`, `security/`, `repository/`, `service/` de S2D5 **ya no existen**. La
excepción CONSCIENTE a la regla "espejo de `src/main`": el paquete de test ahora declara el **NIVEL de la
pirámide**, no la clase que prueba.

- El parche heredado `service/TaskServiceTest` (`@SpringBootTest` que arrancaba TODO Spring para probar
  un servicio) **se borró**: renace en `unit/TaskServiceTest` como Mockito puro (corre en **ms**).
- `TaskControllerTest`/`ProjectControllerTest` pasaron de `@SpringBootTest` a **`@WebMvcTest`** con
  `@MockitoBean` del service, `@AutoConfigureMockMvc(addFilters=false)` y `@MockitoBean JwtAuthenticationFilter`
  (el slice escanea los `Filter` `@Component`; mockearlo evita arrastrar `JwtService`/`JpaUserDetailsService`).
- **Verificación mecánica**: `grep -rl "@SpringBootTest" src/test` devuelve **solo** clases en
  `integration/` — cero `@SpringBootTest` fuera de ese paquete.

## `@Mock` vs `@MockitoBean` (la confusión #1 del día)

| | `@Mock` + `@InjectMocks` | `@MockitoBean` |
|---|---|---|
| Dónde | tests SIN Spring (`unit/`) | tests CON contexto (`slice/`, `integration/`) |
| Requiere | `@ExtendWith(MockitoExtension.class)` | una anotación `@…Test` de Spring |
| Qué hace | instancia y cablea mocks a mano | **sustituye un bean** del contexto |
| Síntoma si te equivocas | "mi mock es null" (falta la extensión) | NPE en el colaborador (no hay contexto) |

Regla mecánica: *¿la clase tiene alguna anotación `@…Test` de Spring? → `@MockitoBean`; ¿no? → `@Mock`.*

## Por qué cobertura alta NO garantiza nada (S1D5 hoy tiene número)

Comenta los `assert` de un test verde → `mvn verify` → **cobertura IDÉNTICA**: *coverage mide ejecución,
no verificación*. El mutation testing (PIT) sí caza asserts vacíos (awareness, no se instala hoy). Ésta
es exactamente la trampa que Copilot (S5) explotará: tests generados que mockean todo, "pasan" y no
prueban nada — decirlo hoy vacuna para la semana 5.

## Preview S3D2

La API está probada y con gate… pero vive SOLO en tu laptop, sobre una BD de juguete (H2). **Mañana:**
Docker en serio, la API contenerizada, **Postgres de verdad** con compose y perfiles, y una demo de
**Testcontainers** que cierra el círculo de hoy (el mismo `@DataJpaTest` de repositorio, contra un
Postgres real efímero). **Tarea para mañana:** `java/semana-3/dia-2/lab/checklist-docker.md`.
