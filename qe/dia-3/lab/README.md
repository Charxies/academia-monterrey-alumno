# taskflow-qa — Día 3 QE (lab / starter)

Interacción con elementos + **esperas (waits)**. Ayer los ENCONTRASTE; hoy los USAS. Y en la
tarde llega **el tema que decide si tu suite sirve o es una ruleta**: las esperas explícitas.

Este `lab/` es el MISMO proyecto `taskflow-qa` de D1–D2 con los archivos nuevos de hoy en
esqueleto (TODOs numerados). Si ya tienes tu `taskflow-qa-<usuario>` de ayer, copia SOLO lo
nuevo; si llegas sin él (o atrasado de D2), este lab ya trae todo: pom, `utils/Paginas`, las
prácticas de D2 ya resueltas y los espejos HTML.

> **Hoy NO se agrega ni una dependencia.** `Select`, `WebDriverWait`, `FluentWait` y
> `ExpectedConditions` viven DENTRO de `selenium-java` (fijado en D1). Selenium Manager
> resuelve el chromedriver solo.

## Qué copiar a tu proyecto

Archivos NUEVOS de D3 (lo de D1–D2 ya lo tienes):

```
src/test/java/com/taskflow/qa/
  utils/Paginas.java                 # EXTENDIDA: formulario(), dropdowns(), alertas(), frames(),
                                     # framesAnidados(), ventanas(), cargaDinamica(modo),
                                     # controlesDinamicos(), tienda() y toast() — reemplaza la de D2
  interaccion/MP01FormulariosTest.java ... MP06VentanasTest.java
  esperas/MP07AntiPatronSleepTest.java (COMPLETA y @Disabled) ... MP09SauceDemoWaitsTest.java
  e2e/SauceDemoE2ETest.java  e2e/TheInternetExtrasTest.java   # tu entregable
src/test/resources/practice-pages/
  formulario.html toast.html dropdowns.html checkboxes.html alertas.html
  frames.html frames-anidados.html ventanas.html nueva-pestana.html
  carga-dinamica.html controles-dinamicos.html  y  tienda/ (6 páginas)
```

## Cómo correr

```bash
mvn -q test-compile                 # solo compilar (sin abrir navegador)
mvn test -Dpaginas.local=true       # TODO verde y offline (recomendado en clase)
mvn test                            # externo: the-internet + saucedemo.com
mvn test -Dtest=SauceDemoE2ETest    # solo el integrador de la tienda
```

Requisitos: **Java 21** + **Google Chrome** actualizado. Las páginas de hoy tienen **retrasos
reales de 0.5–3 s** (spinners, render diferido): sin latencia, las esperas no tendrían nada que
esperar. El flag `-Dpaginas.local=true` alterna externo↔local para TODAS las páginas de una:
un solo switch, no editas ocho archivos a media clase.

## Tabla de mapeo sitio externo → fallback local

| Práctica | Sitio externo | Página local |
|---|---|---|
| MP-1 texto/login | the-internet `/login` + `/inputs` | `login.html`, `formulario.html` |
| MP-2 click interceptado | *(no hay equivalente confiable)* | `toast.html` — **primaria, no fallback** |
| MP-3 dropdowns | `/dropdown` (solo el nativo) | `dropdowns.html` (nativo + custom) |
| MP-3 checkboxes/radios | `/checkboxes` | `checkboxes.html` (+ radios, solo local) |
| MP-4 alerts | `/javascript_alerts` | `alertas.html` |
| MP-5 frames | `/iframe` y `/nested_frames` | `frames.html` + `frames-anidados.html` |
| MP-6 ventanas | `/windows` | `ventanas.html` + `nueva-pestana.html` |
| MP-7/MP-8 esperas | `/dynamic_loading` (1 y 2) y `/dynamic_controls` | `carga-dinamica.html?modo=oculto\|inexistente`, `controles-dinamicos.html` |
| MP-9 + Integrador | saucedemo.com | `tienda/` (6 páginas) |

## Los MP de hoy

| Archivo | Qué practicas | Página |
|---|---|---|
| `interaccion/MP01FormulariosTest` | `sendKeys`/`clear`, bug de no limpiar, `submit()` vs click | login/formulario |
| `interaccion/MP02ClickInterceptadoTest` | **Error 1**: `ElementClickInterceptedException` por toast + cierre con `invisibilityOf` | toast |
| `interaccion/MP03SelectsCheckboxesTest` | `Select` nativo, dropdown custom (2 pasos), click condicional idempotente | dropdowns/checkboxes |
| `interaccion/MP04AlertsTest` | alert/confirm/prompt + `UnhandledAlertException` provocada | alertas |
| `interaccion/MP05FramesTest` | **Error 2**: `NoSuchElement` sin switch + iframe + anidados | frames |
| `interaccion/MP06VentanasTest` | window handles, switch, close, volver | ventanas |
| `esperas/MP07AntiPatronSleepTest` | **PROVISTA COMPLETA y `@Disabled`**: sleep vs explicit con cronómetro | carga-dinamica |
| `esperas/MP08EsperasDinamicasTest` | presence vs visibility, `invisibilityOf` del spinner, **Error 3**: `StaleElement` | carga/controles/toast |
| `esperas/MP09SauceDemoWaitsTest` | flujo corto + `performance_glitch_user` (mismo test, solo tarda) | tienda |

## Tabla canónica de ExpectedConditions (imprímela)

| Quiero… | Condición | Trampa que evita |
|---|---|---|
| Leer texto/atributos de algo que aparece | `visibilityOfElementLocated` | `presence` pasa con el elemento aún oculto |
| Clickear algo | `elementToBeClickable` | visible ≠ habilitado |
| Que un spinner/toast/overlay se VAYA | `invisibilityOfElementLocated` | clickear "a través" de un overlay |
| Verificar un mensaje/estado textual | `textToBePresentInElement` | leer el nodo antes de que JS ponga el texto |
| Solo que exista en el DOM (raro) | `presenceOfElementLocated` | usarla como default es el error nº 1 |

> En el atasco, pregúntate: **"¿qué voy a HACER con el elemento?"** y la tabla responde.

## Tabla de excepciones de interacción del día

| Excepción | Qué significa | Cómo se cura |
|---|---|---|
| `ElementClickInterceptedException` | otro elemento (overlay/toast/banner) recibiría el click | esperar `invisibilityOf` del interceptor |
| `ElementNotInteractableException` | existe pero no visible/habilitado | esperar `visibility`/`elementToBeClickable` |
| `UnhandledAlertException` | quedó un alert nativo abierto sin manejar | `accept()`/`dismiss()` en el mismo test |
| `NoSuchElementException` (mentiroso) | el elemento vive en otro frame | `switchTo().frame(...)` (y `defaultContent()` al salir) |
| `StaleElementReferenceException` | cacheaste un `WebElement` que el re-render mató | re-localizar; nunca cachear entre mutaciones |

## Integrador — tu entregable (`e2e/`)

`SauceDemoE2ETest` (4 tests) + `TheInternetExtrasTest` (2 tests), **todo con explicit waits**:

1. `loginInvalidoMuestraError` — `locked_out_user` + password errónea; mensaje EXACTO en `h3[data-test="error"]`.
2. `loginValidoEntraAlInventario` — `standard_user`; título "Products" + 6 `inventory_item`.
3. `agregarDosProductosYVerificarCarrito` — badge == "2" y ambos nombres (streams).
4. `checkoutHastaConfirmacion` — **total CALCULADO** (suma de precios == "Item total", NO hardcodeado) → "Thank you for your order!".
5. `manejaAlertNativo` — confirm nativo: `getText`, `accept`, assert de `#result`.
6. `escribeEnIframe` — switch al frame, escribir, `defaultContent()` y assert del documento externo.

## DoD del día

- (a) `mvn test` corre y los **6 tests del integrador** quedan verdes (saucedemo o `tienda/` local).
- (b) `grep -rn "Thread.sleep" src/test/java | grep -v MP07AntiPatronSleep` → **vacío**; MP07 está `@Disabled`.
- (c) `grep -rn "implicitlyWait" src/test/java` → **vacío** (implicit wait PROHIBIDO en el framework).
- (d) commiteado y pusheado a `taskflow-qa-<usuario>`: `feat(qa): e2e saucedemo con explicit waits + alerts e iframes`.
- (e) sabes explicar, sobre un test que el instructor elija, **por qué cada ExpectedCondition** (visibility vs clickable vs presence).

## Stretch (rápidos)

1. `FluentWait` con `pollingEvery(Duration.ofMillis(250)).ignoring(NoSuchElementException.class)` para el spinner del checkout.
2. Corre TODO el flujo con `performance_glitch_user` ajustando SOLO la constante `TIMEOUT` — y siente lo feo de tocarla en 2 clases (más gancho para el POM de D4).
3. `ChromeOptions` con `--headless=new` en UN test (anticipo de lo que `DriverFactory` formaliza en D4 y el CI usa en D5).

## Tarea previa OBLIGATORIA para D4 (el warm-up de D4 la VERIFICA)

1. Copia `recursos/taskflow-ui/static/` a `src/main/resources/static/` de tu `taskflow-api`.
2. Copia `recursos/taskflow-ui/application-h2.yml` a `src/main/resources/` (+ H2 scope runtime si falta).
3. Arranca: `mvn spring-boot:run -Dspring-boot.run.profiles=h2` (reinicia si ya corría).
4. Ejecuta la semilla (`seed/seed.http` o `seed.sh`).
5. Smoke manual: login `demo`/`Demo123!` en `http://localhost:8080/index.html` y verifica los 2 proyectos.

> Quien se atore lo reporta al canal **esta noche**, no mañana a las 9.
