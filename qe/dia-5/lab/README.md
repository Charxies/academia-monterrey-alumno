# taskflow-qa — Día 5 QE · Framework HÍBRIDO (API + UI) en CI (lab / starter)

![qa](https://github.com/TU-USUARIO/taskflow-qa-TU-USUARIO/actions/workflows/qa.yml/badge.svg)

> TODO: sustituye `TU-USUARIO` por tu usuario de GitHub (aquí y en `.github/workflows/qa.yml`).

Este es el MISMO proyecto `taskflow-qa` que nació en D1 y creció en D2–D4. HOY (D5) le sumas la capa
**API** (RestAssured) a la capa UI (POM de D4) — un solo framework, **híbrido** — y lo llevas a **CI**.
El starter compila con **TODOs**: tú los completas (MP-1..MP-9 + integrador).

## Requisitos previos

- **Java 21** y **Google Chrome** (Selenium Manager resuelve el chromedriver solo).
- Tu **`taskflow-api`** con perfil `h2` y la UI estática instalados (desde el wrap-up de D3) y
  **pusheados** al repo `taskflow-api-<usuario>` (el CI hace checkout de lo pusheado — MP-1).

## Cómo arrancar

```bash
# Arranca tu API (SUT) en otra terminal, en el repo de la API:
mvn spring-boot:run -Dspring-boot.run.profiles=h2      # localhost:8080

# Compilar el framework:
mvn -q test-compile

# Correr solo la capa CI de hoy (una vez etiquetes @Tag("ci") en api/ + SmokeUiTest):
mvn test -Dgroups=ci                  # local, headed
mvn test -Dgroups=ci -Dheadless=true  # como corre el pipeline
```

> **Nota del lab de rescate:** si arrancas este starter desde cero (solo las clases de HOY, sin las
> de D1–D4), `mvn test -Dgroups=ci` equivale a `mvn test` a secas — no hay más que correr.

## Dos modos de corrida del repo acumulado (D1–D5)

```bash
# LOCAL COMPLETO — toda la suite D1–D5. Prerrequisito: API h2 arriba + semilla (seed.sh/seed.http).
mvn test -Dpaginas.local=true

# CAPA CI — solo lo etiquetado @Tag("ci") (api/ + SmokeUiTest). No necesita la semilla (cada test
# crea sus datos). Es lo que corre el pipeline.
mvn test -Dgroups=ci -Dheadless=true
```

## Qué construyes hoy (guía rápida de los huecos)

| Archivo | Qué te toca |
|---|---|
| `api/ApiSpecs.java` | `base()` y `conToken(token)` → `RequestSpecification` (baseUri de `Config`) — MP-7 |
| `api/AuthClient.java` | `registrar()`, `login()`, `cuentaNueva()`/`tokenNuevo()` con usuario único — MP-4 |
| `api/dto/*` | records ya resueltos (RegisterRequest, LoginRequest, ProjectRequest, TaskRequest) |
| `api/tests/AuthApiTest` | register 201, login OK 200+token, login mala 401 — MP-2/4/6 |
| `api/tests/ProjectsApiTest` | CRUD encadenado + arreglar el test-**trampa** (ERR-1) — MP-5/8 |
| `api/tests/TasksApiTest` | crear/listar/patch/put/borrar tarea — MP-5 |
| `api/tests/ErroresApiTest` | 400, 401, 403 (dos actores), 404 — MP-6 |
| `tests/SmokeUiTest` | sembrar usuario por API (`@BeforeAll`) + login por UI — integrador |
| `.github/workflows/qa.yml` | pon tu `API_REPO`, revisa el nombre del artifact — MP-8 |
| `@Tag("ci")` | etiqueta las clases de `api/` y `SmokeUiTest` — MP-8 |

## Tabla de cobertura por capa (COMPLÉTALA — integrador req. 4)

| Capa | Clase | Qué valida | Cuándo DEBE fallar (rojo legítimo) |
|---|---|---|---|
| API · auth | `api/tests/AuthApiTest` | _TODO_ | _TODO_ |
| API · projects | `api/tests/ProjectsApiTest` | _TODO_ | _TODO_ |
| API · tasks | `api/tests/TasksApiTest` | _TODO_ | _TODO_ |
| API · errores | `api/tests/ErroresApiTest` | _TODO_ | _TODO_ |
| UI · smoke | `tests/SmokeUiTest` | _TODO_ | _TODO_ |

## DoD del día

- Pipeline **verde** en Actions (arranca la API h2 + `mvn test -Dgroups=ci -Dheadless=true`).
- Suite API **≥10 tests**: auth + CRUD (incluido `PUT /projects/{id}`) + los 4 códigos de error, sin
  depender de estado previo.
- Allure descargable como artifact del último run.
- Este README con comandos + tabla de cobertura llena + badge.
- Commiteado y pusheado (QA y, si destapaste bugs, la API).
