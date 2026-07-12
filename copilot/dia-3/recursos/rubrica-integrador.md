# Rúbrica — Integrador S5D3: Cobertura auditada de 2 módulos

> Se evalúa el **PR abierto** desde `test/cobertura-s5d3` de tu `taskflow-api` (no mergeado — mañana lo
> revisa Copilot Code Review). **La conoces ANTES de empezar el integrador.** El instructor revisa la
> **descripción del PR, el diff y el diario**, y hace un **spot-check oral**. Total: **100 pts**.
> Aprobado ≥ 70. La regla (d) del programa —explicar tu código— pesa explícito en las dos últimas filas
> y hoy aplica a **tests que TÚ no tecleaste**.

| # | Criterio (DoD) | Qué se busca | Pts |
|---|---|---|---:|
| **a** | **PR abierto + pipeline verde** | Rama `test/cobertura-s5d3` pusheada; PR **abierto** (no mergeado) contra `main`; pipeline de S3D4 **verde** (build+test en `pull_request`, **sin deploy**). Título exacto `test: cobertura auditada de <m1> y <m2> + docs` | 10 |
| **b** | **Descripción del PR completa** | Según `plantilla-pr-cobertura.md`: **meta declarada** (+15 o 80%) por módulo, tabla **JaCoCo antes/después**, **matriz mutación×test** (3 mutaciones/módulo, cada una cazada por ≥1 test, **cada test nuevo cazando ≥1**), lista de **podados con razón** de 1 línea | 25 |
| **c** | **`git diff src/main` limpio** | **Cero mutaciones commiteadas.** El diff del PR **solo toca tests y docs**. Se verifica con `git diff main -- src/main` | 10 |
| **d** | **Spot-check de lectura crítica** | El instructor elige **2 tests nuevos al azar**; los defiendes con la checklist (①–④). Si uno resulta decorativo: se **poda y se repone** — el PR no se acepta con teatro | 15 |
| **e** | **Docs auditadas** | Javadoc de los públicos de **ambos módulos** sin `@param`/`@throws` fantasma (`mvn javadoc:javadoc` **sin warnings nuevos**); sección de testing del README con **comandos verificados por ejecución** | 15 |
| **f** | **Diario de decisiones** | Sección "Día 3" con **≥1 entrada por bloque** (AM-1, AM-2, PM-1, integrador): prompt, qué generó, qué aceptaste/podaste **y por qué**, mutaciones y resultado. Incluye **≥1 poda** y **≥1 caso descartado con razón** | 10 |
| **g** | **Explicación oral (regla (d))** | En 1–2 min, **sin leer**: eliges un test que **no tecleaste** y respondes *"¿qué bug real caza y qué mutación lo demuestra?"* | 15 |

## Penalizaciones (restan sobre el total)

- **−15 · Mutación commiteada.** Cualquier cambio en `src/main` en el diff del PR (un `<=` colado, un
  `throw` borrado). Aunque la suite quede verde: rompiste el ritual y el DoD (c). Revert y re-entrega.
- **−10 · Teatro en el spot-check.** Un test elegido al azar resulta decorativo y no lo habías detectado
  (falla la ① y tú lo diste por bueno). Se poda, se repone, y baja la nota.
- **−10 · Portería movida.** La meta se "declaró" **después** de ver los números (no antes de generar).
  El compromiso público existe para no ajustar la meta a lo que salió.
- **−10 · Comando del README que no corre.** El README afirma un comando (`docker compose up`, un
  `mvn ...`) que al pegarlo en la terminal **falla o no existe**. La doc miente.
- **−5 · Diario relleno al final.** Entradas genéricas, sin prompts reales ni razones — se nota en el
  oral. El diario se llena EN EL MOMENTO; es insumo del reporte de D5.

## Señales de excelencia (desempate hacia arriba)

- **La matriz no tiene filas vacías tramposas:** para un test que las 3 mutaciones estándar no atacan,
  eligió una **4ª mutación** que sí lo caza (o justificó la regla distinta que cubre) en vez de dejar la
  fila en blanco.
- **La poda está argumentada fino:** no "borré 3 tests", sino *"`crear_ok_2` era redundante con
  `crear_tituloLongitud3` — mismo escenario, dato de relleno"*.
- **Stretch hecho:** `pitest-maven` en un módulo y **comparación honesta** con las 3 mutaciones manuales
  (cuántos mutantes sobreviven a la suite "auditada" — humildad instantánea); o el test **RestAssured**
  en `taskflow-qa` que aserta los códigos recién documentados del `PATCH /tasks/{id}/status`.
- **La cobertura BAJÓ en algún punto y el alumno lo defiende** como buena señal (número inflado
  era mentira decorativa), no como fracaso.

## Cómo lee esto el instructor en ~4 minutos

1. **Abre el PR** → ¿abierto, pipeline verde, título exacto? (a)
2. **Lee la descripción** → ¿meta declarada, tabla JaCoCo antes/después, matriz mutación×test con las
   dos condiciones (columnas y filas con ✓), podados con razón? (b)
3. `git diff main -- src/main` → **vacío**. Si toca producción, penalización −15. (c)
4. `mvn javadoc:javadoc` → sin warnings nuevos; pega 1 comando del README y lo corre. (e)
5. Abre el **diario** → ¿una entrada por bloque, con prompts y razones reales? (f)
6. **Spot-check oral (d + g):** elige 2 tests al azar. *"Defiéndelos con la checklist. De uno: ¿qué
   mutación lo caza?"* **Aquí se cae el copy-paste sin comprensión** — que es justo el anti-objetivo del
   día.
