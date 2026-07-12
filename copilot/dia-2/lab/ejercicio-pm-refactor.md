# Ejercicio PM + Integrador — Custom instructions, caracterizar → refactorizar → fix aparte

> **Bloques:** PM-1 (90 min) + Integrador (75 min). **Sobre:** tu `taskflow-api` (rama
> `legacy-refactor`). **Corazón del día:** la secuencia disciplinada **caracterizar → refactorizar
> incremental → arreglar aparte**. Copilot acelera cada paso; la disciplina (leer diffs, tests
> verdes en cada paso, commits pequeños) es humana y **no es negociable**.
>
> Se califica con [`rubrica-integrador.md`](rubrica-integrador.md). **Léela antes de empezar.**

---

## Parte A — PM-1

### §1 · Custom instructions de TaskFlow (MP-7, 20 min)

**Qué son:** convenciones del repo que Copilot inyecta a cada petición del chat. Viven en
`.github/copilot-instructions.md`, en la **raíz** del repo (⚠ VERIFICAR-PREVIO: ubicación/nombre
vigentes y si el plugin de IntelliJ ya las honra en tu versión; si aún no, se demuestra el efecto en
VS Code y se documenta la brecha). Reglas **cortas y verificables**, no ensayos.

**Escríbelas para `taskflow-api`** (las reales del proyecto — las que llevas 4 semanas usando):

- Comentarios y javadoc en **español**; identificadores en **inglés**.
- DTOs como **`record`**.
- **Inyección por constructor** (nunca field injection).
- Forma canónica de `Task`: `status` ∈ `TODO/IN_PROGRESS/DONE`, `priority` ∈ `LOW/MED/HIGH`
  (CAPSTONE-SPEC).
- Validación con `@Valid` en los DTOs de request.
- Errores vía **excepciones de dominio + `@RestControllerAdvice`** (tu `GlobalExceptionHandler` de
  S2D3).
- Tests **JUnit 5** (`org.junit.jupiter`) con naming **`metodo_escenario_resultado`** (tu convención
  desde S1D5 — nómbrala, no digas "la que el repo usa").

**Experimento antes/después (obligatorio):** la MISMA petición —*"genera el DTO de request para crear
un comentario en una tarea"* (guiño a la feature de D5)— en un chat **SIN** instructions y otro
**CON**. Compara lado a lado y anota **3 diferencias** en el diario (¿usó `record`? ¿español en los
comentarios? ¿`@Valid`/validaciones?). Para forzar la recarga: abre un chat **nuevo** (los en curso
pueden no recargarlas) y confirma el indicador de referencia en la respuesta (⚠ VERIFICAR-PREVIO).

Commit de las instructions. La versión de referencia del instructor
([`../solucion/copilot-instructions-referencia.md`](../solucion/copilot-instructions-referencia.md))
se libera al final para comparar — no antes.

### §2 · Refactor con red: tests de caracterización (T5 + MP-8, 25 min)

**Regla de oro:** los tests de caracterización (golden master *light*) **fijan el comportamiento
ACTUAL** —incluido lo feo e incluido cualquier bug—. No opinan sobre lo correcto: **fotografían lo que
hay**. Son la red que te dirá "lo rompiste" en cuanto un refactor cambie una coma del output.

**`/tests` para caracterizar `ReporteLegacy`** (⚠ VERIFICAR-PREVIO disponibilidad de `/tests`): con la
clase y `DatosDemo` como contexto, pide tests del output de `generar(...)` sobre los datos demo —
total, la línea de un usuario concreto, el encabezado, un bucket.

**El punto de dolor está aquí:** `/tests` a veces aserta lo que **cree** que el código hace, no lo que
hace. Es muy probable que genere `TOTAL TAREAS: 15` —porque "leyó" el contrato del javadoc
("inclusive")—. **Córrelo.** Donde falle, **ajusta la expectativa al comportamiento REAL**: en
caracterización, **el código manda**, no el javadoc ni tu intuición. Verás que el total real es otro;
fija ESE. Anota la discrepancia en el diario como sospecha (no la arregles).

```bash
mvn test    # la suite de caracterización, VERDE, fijando el statu quo (bug incluido)
git add . && git commit -m "test: caracterizacion de ReporteLegacy (golden master)"
```

> Mínimo de la suite: **total, 2 usuarios, encabezado, un bucket**. Si `/tests` quedó corto, se
> completa en el integrador §1.

### §3 · Refactor incremental, tests verdes en CADA paso (MP-9, 20 min)

Con **inline chat** (pide un cambio → **lee el diff completo** → aceptar/rechazar → `mvn test`). Un
paso = un commit. Empieza estos y sigue en el integrador:

1. Extraer el **filtro de fechas** a un método con **nombre honesto** → verde → commit.
2. Renombrar `a/b/temp/acum` a nombres reales → verde → commit.
3. Pasar **un** `for` a stream (los buckets o la suma) → verde → commit.
4. `/doc` para javadoc en español (las custom instructions recién escritas **deben notarse**).

> **El tamaño del paso ES la red de seguridad.** Si le pides al panel *"refactoriza toda la clase a
> streams y métodos pequeños"* de un golpe, lo típico es que compile a medias o la suite explote →
> `git checkout .` y de vuelta al camino incremental. Copilot **tienta** a dar el paso gigante;
> resístelo. Si el diff no cabe en una pantalla, el paso era demasiado grande: pártelo.

---

## Parte B — Integrador (75 min, en pares · driver/navigator rotan a la mitad)

Sobre la rama `legacy-refactor`. El instructor revisa tu **`git log`**, no solo el estado final: un
commit que mezcle dos intenciones cuenta **en contra**.

### 1 · Caracterización cerrada (≈10 min)

Suite golden master **verde y commiteada**. Si §2 quedó corto, complétala aquí: mínimo **total, 2
usuarios, encabezado, un bucket**. Esta suite es lo que protege todo lo que sigue.

### 2 · Refactor completo en commits pequeños (≈25 min)

Termina lo de §3. Meta del estado final de `generar(...)`:

- métodos de **≤15 líneas** con **nombres honestos**,
- **constantes** en lugar de números mágicos (`3`, `100`, `60`, `0.85`, `1.15`, `6`, `7`),
- **streams** donde aporten (mínimo: dedupe/agrupación y sumas),
- `ordenar` reemplazado por `Arrays.sort`/`sorted()`,
- `RegistroTarea` **puede quedar POJO** (tocarlo es **opcional**: discute costo/beneficio en el diario
  — cambiarlo a `record` toca todos los getters/setters y no aporta a la legibilidad del reporte).

**Mínimo 4 commits de refactor, cada uno con la suite VERDE.** Recuerda: esto es **refactor**, la
suite NO debe cambiar de expectativas — si un cambio tuyo la pone roja, o rompiste algo, o estás
haciendo un fix disfrazado de refactor. En ambos casos: para y sepáralo.

### 3 · El bug — cazado, testeado, arreglado APARTE (≈15 min)

Ahora sí. Parte del **contrato del javadoc** ("de lunes a domingo, inclusive") **contra** el
comportamiento que fijaste en la caracterización (el total real). Ese hueco es el bug.

**El orden importa (refactor ≠ fix):**

1. Escribe **primero** el test que **demuestra** el bug: una tarea terminada el **domingo**
   (`2026-03-08`) debe aparecer en el reporte. Córrelo: nace **ROJO**.
2. Arregla el filtro: la condición del límite superior debe **incluir** el domingo (pista de
   contrato: "inclusive"; hoy el código lo excluye).
3. **Actualiza deliberadamente el golden master** al nuevo total, **en el mismo commit del fix**.

```bash
mvn test    # verde: la caracterización actualizada + el test del bug
git add . && git commit -m "fix: incluir el ultimo dia de la semana en el reporte"
```

**Discusión obligatoria (par + diario):** ¿por qué NO lo arreglamos durante el refactor, aunque ya lo
sospechábamos desde AM-2? Porque **refactor ≠ fix**: si mezclas ambos, no puedes distinguir "lo rompí"
de "lo arreglé". Los tests de caracterización protegieron el refactor **precisamente porque fijaban el
bug** — si el refactor lo hubiera cambiado sin querer, la suite se habría puesto roja y te habrías
enterado.

### 4 · Explicación escrita — `EXPLICACION.md` (≈20 min)

Junto al módulo, `EXPLICACION.md` con el **formato de [`plantilla-explicacion.md`](plantilla-explicacion.md)**:

- Genera la explicación con `/explain` sobre la versión **YA refactorizada** (más limpia = mejor
  explicación de base).
- **Corrígela a mano:** marca lo que el chat dijo mal con ~~tachado~~ → tu corrección + **la línea que
  lo prueba**. **Mínimo 1 corrección real.**
- **Anexa** la tabla de validación de AM-2 como evidencia de búsqueda.

> "El chat lo dijo perfecto" **no** es entrega válida sin evidencia de haber buscado (la tabla anexa).
> Sobre una versión ya refactorizada seguirá habiendo algo que matizar —un método que "asume" hace X y
> hace X'—; encuéntralo.

### 5 · Diario al día (≈5 min)

Sección **Día 2** de `docs/copilot/diario-s5.md`: **mínimo 5 entradas** de hoy (qué pediste, qué dio
Copilot, qué aceptaste/rechazaste/corregiste y por qué, cómo lo verificaste). Es el insumo del reporte
de D5 — se llena **en el momento**, no de memoria al cierre.

### Entregable

Push de la rama `legacy-refactor` (o PR a tu main, a criterio del instructor). El **historial** debe
contar la historia:

```
chore: modulo legacy recibido tal cual
test: caracterizacion de ReporteLegacy (golden master)
refactor: … (≥4 commits incrementales, cada uno con la suite verde)
fix: incluir el ultimo dia de la semana en el reporte   ← con su test, APARTE
```

---

## Stretch (solo si terminaste y el DoD está verde)

- **`/tests` de contrato del método extraído del filtro de fechas** (ya no caracterización: ahora sí
  de lo *correcto* — domingo inclusive, lunes anterior fuera, lunes siguiente fuera).
- Pídele al chat una **crítica de su propio refactor** (*"¿qué mejorarías?"*) y evalúa cuáles
  sugerencias valen y cuáles son ruido. Anota el veredicto en el diario.

## Criterios de "listo" (DoD del día)

- [ ] Rama `legacy-refactor` compila y **toda la suite verde** (caracterización actualizada + test del
      bug).
- [ ] Historial: recepción → caracterización → **≥4 commits de refactor** → **fix en commit separado**
      con su test. Ningún commit mezcla dos intenciones.
- [ ] `EXPLICACION.md` con **≥1 corrección marcada** sobre la salida de `/explain` + tabla de
      validación anexa.
- [ ] Commiteado y pusheado.
- [ ] Explicas el módulo y **una imprecisión que corregiste** en 1–2 min, **sin leer el archivo**.
- [ ] Diario con **≥5 entradas** del día.
