# taskflow-qa — Día 4 QE (lab / starter)

Este es tu `taskflow-qa` **al inicio de D4**: la base de D1–D3 (intacta) + los **esqueletos**
del framework POM de hoy. Compila tal cual (`mvn -q test-compile`), pero los métodos nuevos
lanzan `UnsupportedOperationException("TODO MP-x")` y los tests nuevos están en `fail("TODO ...")`
hasta que los completes.

> Si vienes al corriente, copia los archivos nuevos de hoy a TU repo. Si te atrasaste, adopta
> este `lab/` completo. En ambos casos es el MISMO proyecto `taskflow-qa` de siempre.

## Requisitos previos (ambiente TaskFlow) — el warm-up VERIFICA esto

1. Copia `recursos/taskflow-ui/static/` a `src/main/resources/static/` de tu `taskflow-api`.
2. Copia `recursos/taskflow-ui/application-h2.yml` a `src/main/resources/` (+ dependencia H2
   scope runtime si falta).
3. Arranca la API con perfil `h2` y siembra (`seed/seed.http` o `seed.sh`).
4. Smoke manual: `http://localhost:8080/index.html` → login `demo` / `Demo123!` → 2 proyectos,
   spinner ~1.5 s (nadie con `tf.delayMs=0`).

## Qué vas a completar hoy

| Archivo | Módulo | TODO |
|---|---|---|
| `pages/BasePage.java` | MP-1 | **Se entrega COMPLETA** como referencia. Único TODO: MP-7 saca el timeout a `Config`. |
| `pages/LoginPage.java` | MP-2 | constructor con landmark, `open`, `loginAs`, `loginExpectingError`, `errorMessage` |
| `pages/ProjectsPage.java` | MP-4 | `navUsername`, `createProject`, `openProject`, `projectNames`, `logout` |
| `pages/ProjectDetailPage.java` | MP-5 | lectura de tabla, `changeStatus` (Select), `sortBy` (dropdown custom), `filterByStatus`, borrado con confirmación, `waitNewRow` |
| `pages/TaskModal.java` | MP-6 | setters fluent, `save`, `saveExpectingError`, `cancel` |
| `utils/Config.java` | MP-7 | cargar `config.properties` + override por System property |
| `utils/DriverFactory.java` | MP-7 | chrome/firefox, headless con `--window-size`, SIN espera implícita |
| `utils/ScreenshotOnFailure.java` | MP-9 | capturar en `handleTestExecutionException` y adjuntar a Allure |
| `tests/BaseTest.java` | MP-7 | wire de `DriverFactory` en `@BeforeEach` |
| `tests/LoginTest.java` | MP-3/MP-8 | `loginOk` + `loginInvalido` parametrizado (`@CsvSource` ya puesto) |
| `tests/ProjectTest.java` | Integrador | crear proyecto con nombre único |
| `tests/TaskTest.java` | Integrador | crear/estado/orden/filtro/borrado + volver y logout |

Regla del día: **el `data-testid` se copia de la tabla del contrato (en `alumno.md`), no se
adivina.** Los `data-testid` ya están puestos en los page objects como `private static final By`.

## Cómo correr

```bash
mvn -q test-compile                       # debe dar BUILD SUCCESS con los TODO
mvn test -Dpaginas.local=true             # cuando completes: suite verde (D1-D3 + UI de hoy)
mvn test -Dpaginas.local=true -Dheadless=true    # como corre el CI mañana (D5)
mvn allure:serve                          # el reporte (descarga el CLI la 1ª vez, hazlo con red)
```

Requisitos: **Java 21** + **Google Chrome** + la **API TaskFlow arriba** (perfil H2 + semilla).
`MP07AntiPatronSleepTest` sigue `@Disabled` (única excepción del grep del DoD, heredada de D3).

## DoD (para darte por listo)

- `mvn test -Dpaginas.local=true` verde: login OK, login KO parametrizado (3), crear proyecto,
  crear tarea, cambiar estado (badge + toast), ordenar con dropdown custom, filtrar (incluido el
  `empty-state`), borrar con confirmación, toast verificado hasta desaparecer, volver + logout.
- Estructura `pages/` + `tests/` + `utils/` + `config.properties`; locators `private static final By`.
- `grep -rn "Thread.sleep\|implicitlyWait" src/ | grep -v MP07AntiPatronSleep` = **0**.
- `mvn allure:serve` muestra `@Step` legibles y el screenshot al fallo (fallo provocado y corregido).
