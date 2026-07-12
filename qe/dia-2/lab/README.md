# taskflow-qa — Día 2 QE (lab / starter)

Localización de elementos: **encontrar cualquier elemento y elegir el locator que seguirá
funcionando el mes que entra**. Hoy se ENCUENTRA, no se interactúa (eso es D3).

Este `lab/` es el MISMO proyecto `taskflow-qa` de D1 con los archivos nuevos de hoy en
esqueleto (TODOs). Si ya tienes tu `taskflow-qa-<usuario>` de ayer, copia SOLO lo nuevo;
si llegas sin él, este lab ya trae todo (pom, `utils/Paginas`, los espejos HTML).

## Qué copiar a tu proyecto

Archivos NUEVOS de D2 (los demás ya los tienes de D1):

```
src/test/java/com/taskflow/qa/
  utils/Paginas.java                 # EXTENDIDA: checkboxes(), tabla(), dinamicos(),
                                     # perfilV1(), perfilV2() y local(...) — reemplaza la de D1
  practicas/MP02LoginTest.java ... MP09AbsolutoVsRelativoTest.java
  integrador/SuiteLocalizacionTest.java   # tu entregable (15 objetivos)
src/test/resources/practice-pages/
  checkboxes.html  tabla.html  dinamicos.html  perfil-v1.html  perfil-v2.html
```

## Cómo correr

```bash
mvn -q test-compile                 # solo compilar (sin abrir navegador)
mvn test -Dpaginas.local=true       # TODO verde y offline (recomendado en clase)
mvn test                            # MPs contra the-internet; el integrador SIEMPRE es local
mvn test -Dtest=SuiteLocalizacionTest   # solo el integrador
```

Requisitos: **Java 21** + **Google Chrome** actualizado. Selenium Manager resuelve el
chromedriver solo (sin descargas manuales). El integrador corre SIEMPRE contra las páginas
locales (usa `Paginas.local(...)`), así que no depende de la red ni del flag.

> **Regla de oro del día:** ningún locator entra a Java sin pasar por `$$('css')` / `$x('xpath')`
> en la Console de DevTools y devolver **exactamente 1** (o el conteo que esperas).

## Los MP de hoy

| Archivo | Qué practicas | Página |
|---|---|---|
| `MP02LoginTest` | `By.id`, `By.name`, CSS de atributo; `findElements` vacío + `assertThrows` | login.html |
| `MP03LocatorAmbiguoTest` | **Error 1**: locator ambiguo, `findElement` toma el primero; detectar con `size()` | login.html |
| `MP04CssBasicosTest` | CSS: atributo, combinación `tag.class[attr]`, `#id`, `[data-testid]` | checkboxes.html + login.html |
| `MP05CssTablaTest` | CSS de relación: `>`, `:nth-child`, `:last-child`, conteos tras mutar el DOM | tabla.html + dinamicos.html |
| `MP06ClaseFrameworkTest` | **Error 3**: clase de framework `css-a1b2c3` que se regenera vs `data-testid` | perfil-v1/v2 |
| `MP07XPathTextoTest` | XPath: `text()`, `contains()`, `normalize-space()` | tabla.html + dinamicos.html |
| `MP08XPathEjesTest` | XPath ejes: `following-sibling`, `ancestor::tr` (navegar una fila por su dato) | tabla.html |
| `MP09AbsolutoVsRelativoTest` | **Error 2**: XPath absoluto roto en v2, relativo sobrevive (`@Disabled` = evidencia) | perfil-v1/v2 |

## Integrador — Suite de localización (tu entregable)

`integrador/SuiteLocalizacionTest.java`: 15 objetivos, cada uno un `@Test` que localiza y
**valida algo** del elemento. Corre SIEMPRE contra las páginas locales.

### Checklist de 15 objetivos

| # | Página | Objetivo | Estrategia esperada | Validación mínima |
|---|---|---|---|---|
| 1 | login.html | Campo username (tiene id) | `By.id` | `isDisplayed()` |
| 2 | login.html | Campo password (por name) | `By.name` | `getAttribute("type")` = `password` |
| 3 | login.html | Botón submit (sin id) | CSS `button[type='submit']` | texto del botón |
| 4 | login.html | Mensaje flash (id + clases de estado) | `By.id` | clase contiene `error`/`success` |
| 5 | checkboxes.html | AMBOS checkboxes | CSS + `findElements` | `size()==2` |
| 6 | checkboxes.html | Solo el segundo checkbox | CSS `:nth-of-type(2)` | `isSelected()==true` |
| 7 | tabla.html | Header "Email" | CSS `th:nth-child(n)` | `getText()` |
| 8 | tabla.html | Celda con email exacto | XPath `//td[text()='...']` | `getText()` = email |
| 9 | tabla.html | El "Due" de la fila cuyo email es X | XPath ejes (`following-sibling`) | monto esperado |
| 10 | tabla.html | Link "delete" de la fila del apellido Y | XPath `ancestor::tr` + descendiente | `href` no vacío |
| 11 | tabla.html | Última fila de la tabla | CSS `tbody tr:last-child` | apellido en su 1ª celda |
| 12 | dinamicos.html | Botón "Add Element" (sin id) | XPath por texto o CSS de atributo | habilitado |
| 13 | dinamicos.html | Botones "Delete" tras 3 clicks en Add | `findElements` CSS | `size()==3`; y `==0` antes |
| 14 | dinamicos.html | Elemento marcado con `data-testid` | CSS `[data-testid='...']` | texto/estado |
| 15 | perfil-v1.html | Link de contacto por su texto visible | `By.linkText` | `href` esperado |

### Mapa de páginas (the-internet ↔ local)

| Ejercicio | the-internet | Local |
|---|---|---|
| Login | `/login` | `login.html` |
| Checkboxes | `/checkboxes` | `checkboxes.html` |
| Tablas | `/tables` (table1) | `tabla.html` |
| Dinámicos | `/add_remove_elements/` | `dinamicos.html` |
| Perfil v1/v2 | *(no existe)* | `perfil-v1.html` / `perfil-v2.html` (solo local) |

## Tabla de locators del entregable (LLENA una fila por objetivo)

La columna **"Por qué esta y no otra" es la evaluada**: *el razonamiento vale tanto como el
código*. Aceptable: "tiene id estable puesto por el dev". Inaceptable: "fue el que funcionó".

| # | Elemento | Locator | Estrategia | Por qué esta y no otra |
|---|---|---|---|---|
| 1 |  |  |  |  |
| 2 |  |  |  |  |
| 3 |  |  |  |  |
| 4 |  |  |  |  |
| 5 |  |  |  |  |
| 6 |  |  |  |  |
| 7 |  |  |  |  |
| 8 |  |  |  |  |
| 9 |  |  |  |  |
| 10 |  |  |  |  |
| 11 |  |  |  |  |
| 12 |  |  |  |  |
| 13 |  |  |  |  |
| 14 |  |  |  |  |
| 15 |  |  |  |  |

## Árbol de decisión de estrategia (úsalo en cada objetivo)

1. ¿Tiene `id` estable? → `By.id`
2. ¿Tiene `name`? → `By.name`
3. ¿Tiene `data-testid` u otro atributo estable? → CSS de atributo `[data-testid='...']`
4. ¿Estructura simple (jerarquía/posición)? → CSS de relación (`>`, `:nth-child`, `:last-child`)
5. ¿Solo lo distingue el TEXTO o hay que navegar EJES? → XPath relativo (`text()`, `following-sibling`, `ancestor::`)
6. XPath absoluto (`/html/body/div[2]/...`): **NUNCA**.

> "Copy selector / Copy XPath" de DevTools es punto de partida para orientarte, **jamás commit**.

## DoD del día

- (a) `mvn test` verde con los 15 objetivos (contra páginas locales).
- (b) cada test valida ALGO del elemento (assert significativo, no solo presencia).
- (c) README con la tabla de 15 filas locator + estrategia + justificación completa.
- (d) cero XPath absolutos activos y cero `Thread.sleep` (el absoluto de MP-9 solo `@Disabled`).
- (e) commiteado y pusheado: `feat: suite de localizacion d2 - 15 locators documentados`.
- (f) sabes explicar en 1–2 min por qué elegiste CSS o XPath en 2 objetivos que el instructor señale.

## Stretch (rápidos)

1. Método estático `assertLocatorUnico(WebDriver, By)` con `findElements().size()==1` — semilla del framework de D4.
2. Reescribe los objetivos 8–10 en CSS **o** justifica en el README por qué es imposible (pista: CSS no tiene selector de texto).
3. `record Objetivo(String nombre, String pagina, By locator)` + UN test que recorre los objetivos de match único (streams + `assertAll`) verificando unicidad de todos de golpe.
