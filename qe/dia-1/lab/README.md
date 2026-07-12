# taskflow-qa — Día 1 QE (lab / starter)

Proyecto **nuevo** de la semana de automatización. Es el framework que crece toda la
semana hasta ser híbrido **UI + API**. Hoy (D1) armamos el esqueleto + los primeros
**smoke tests** con Selenium sobre un sitio de práctica.

> Este `lab/` es la **red de seguridad**: compila tal cual, con los huecos marcados como
> `TODO` numerados y los `@Test` en rojo con `fail("TODO …")`. Tu trabajo (MP-5 a MP-7 y
> el integrador) es llenar esos huecos. En un curso de QE un starter "verde sin hacer
> nada" mentiría, por eso arranca en rojo.

## Requisitos

- **Java 21** (`java -version` debe decir 21).
- **Google Chrome** instalado y **actualizado** (`chrome://version` — anota la versión en
  el canal). NO necesitas descargar chromedriver: **Selenium Manager** (integrado en
  Selenium 4) lo resuelve solo la primera vez que corres un test.
- IntelliJ + Maven + Git (los mismos de las 3 semanas de Java).

## Cómo correr

```bash
# Todos los tests contra el sitio de práctica externo (headed: verás Chrome abrirse)
mvn test

# Fallback local (si the-internet está caído o la red lo bloquea):
# corre EXACTAMENTE los mismos tests contra los espejos file:// del repo
mvn test -Dpaginas.local=true

# Solo la suite smoke del integrador (usa @Tag("smoke"))
mvn test -Dgroups=smoke

# Solo compilar (sin abrir navegador)
mvn -q test-compile
```

> Prueba el fallback local **hoy**, no el día que Heroku se caiga. Nota: the-internet
> tiene *cold start* (~10–30 s el primer request tras dormir el dyno); "tarda" no es
> "está caído": reintenta antes de declarar emergencia.

## Estructura

```
src/test/java/com/taskflow/qa/
  pages/.gitkeep            # vacío A PROPÓSITO: se llena en D4 con los Page Objects
  utils/Paginas.java        # helper ÚNICO de la semana: home() / login().
                            #   -Dpaginas.local=true -> file:// de los espejos (por classpath)
  tests/
    practicas/
      MP06PrimerTest.java   # MP-6: primer test Selenium (anatomía driver->get->find->assert)
      MP07CicloDriverTest.java  # MP-7: ciclo de vida del driver + anti-patrones (@Disabled)
    PracticeSiteSmokeTest.java  # Integrador: 3 smoke tests (el entregable del día)
src/test/resources/practice-pages/
  index.html                # espejo local de la home de the-internet
  login.html                # espejo local de /login
```

Las páginas espejo de `src/test/resources/practice-pages/` son **copia** de la fuente
única `qe/recursos/practice-pages/`. Van dentro del repo para que el fallback viaje con
tu proyecto. No las edites aquí: se editan en el maestro y se re-copian.

## Reglas del framework (desde hoy)

- **Driver fresco por test**: `@BeforeEach new ChromeDriver()`, `@AfterEach driver.quit()`.
- `quit()` vive en `@AfterEach` para que corra **aunque el test falle**.
- **CERO `Thread.sleep`**: las páginas de hoy son estáticas; si "necesitas" un sleep,
  algo está mal.
- **Ninguna URL literal** en los tests: todas salen de `utils/Paginas`.
- Nada de `System.setProperty("webdriver.chrome.driver", …)` ni `WebDriverManager`: eso es
  historia/legacy. En este framework, Selenium Manager.

## Roadmap del proyecto (qué carpeta crece cada día)

| Día | Qué agrega |
|---|---|
| **D1** | Esqueleto + `utils/Paginas` + 3 smoke tests (este entregable) |
| D2 | Locators a fondo (CSS / XPath) sobre el zoo de the-internet |
| D3 | Interacciones + explicit waits |
| D4 | Page Object Model (llena `pages/`) + `DriverFactory`/`BaseTest` + Allure + UI de TaskFlow |
| D5 | API testing con RestAssured contra tu propia API + CI en GitHub Actions |

## Definition of Done (D1)

(a) `mvn test` corre los 3 tests del integrador **verdes** (headed) contra el sitio de
práctica; (b) los mismos 3 pasan con `-Dpaginas.local=true`; (c) al terminar la suite **no
queda ningún proceso** chrome/chromedriver vivo; (d) repo `taskflow-qa-<usuario>` en GitHub
con este README y push hecho; (e) explicas la anatomía `driver -> findElement -> assert` en
1–2 min.

Entregable:

```bash
git add .
git commit -m "feat: taskflow-qa arrancado - 3 smoke tests sobre sitio de practica"
git push
```

## Stretch (solo si terminaste)

1. Imprime la versión real de navegador/driver:
   `((HasCapabilities) driver).getCapabilities()`.
2. Cuarto test que verifica 3 links de la home iterando una `List<String>` de textos con
   `assertAll` (la versión `@ParameterizedTest` llega en D4).
