# Ejercicio AM-2 — `/explain` sobre código ajeno: mapear el módulo legacy

> **Bloque:** AM-2 (90 min). **Sobre:** el `legacy-module` ya copiado a tu `taskflow-api` (rama
> `legacy-refactor`). **Producto:** una **tabla de validación** con cada afirmación relevante del
> chat clasificada, y **al menos una imprecisión cazada** con su número de línea.
>
> **Tesis del bloque:** el chat también alucina sobre TU código. Una explicación *verificable* afirma
> cosas contrastables con líneas concretas; una *plausible* suena bien y ya. Distinguirlas es el
> trabajo, no un extra.

## MP-4 — Recepción y línea base A OJO (15 min)

1. Copia el paquete `com.taskflow.legacy` a tu api en la rama `legacy-refactor`, compila, corre
   `MainLegacy` (ver [`legacy-module/README.md`](legacy-module/README.md)) y commitea:
   `chore: modulo legacy recibido tal cual`.
2. **5 min de lectura a ojo, SIN Copilot.** Con el toggle apagado (higiene de D1), lee `generar(...)`
   y escribe en el diario **2–3 frases de hipótesis** ("creo que hace X"). No las borres después:
   son tu línea base para medir qué te aportó `/explain` de verdad.

> Por qué a ojo primero: si arrancas con el chat, ya no sabes si entendiste tú o si te lo contaron.
> La hipótesis propia es lo que después vas a **confirmar o refutar con líneas**.

## MP-5 — Mapa por capas con `/explain` (25 min)

Pide `/explain` en **tres niveles**, del grueso al fino. Dale el contexto correcto en cada uno
(⚠ VERIFICAR-PREVIO: cómo se selecciona/adjunta contexto en tu versión del plugin):

1. **Clase completa** → propósito general. Abre `ReporteLegacy.java`, pide una explicación de la
   clase. Esperado: acierta el "qué" (genera un reporte semanal por proyecto/usuario).
2. **El método `generar(...)`** → estructura de los loops. Selecciónalo entero y pide que explique su
   flujo. Aquí empieza el terreno resbaloso: los 4 niveles de anidamiento, el filtro, los buckets.
3. **Bloques puntuales por selección** → selecciona y pregunta uno por uno:
   - el **filtro de fechas** (las dos condiciones del `if`),
   - `calc(int est, int real)`,
   - el bucle de **prioridades** que arma `[ALTA]/[MEDIA]/[BAJA]`,
   - `ordenar(String[] arr, int n)`.

**Mientras lees cada respuesta, llena la tabla de validación.** La regla dura:
**nada entra como "Verificada" sin número de línea.**

### Tabla de validación (cópiala a tu diario o a un scratch)

| # | Afirmación del chat (≤1 línea) | Sobre (método/bloque) | Veredicto | Evidencia |
|---|---|---|---|---|
| 1 | | | Verificada / No verificable / **Falsa** | línea N (o "no ubicable") |
| 2 | | | | |
| 3 | | | | |
| 4 | | | | |
| 5 | | | | |
| 6 | | | | |

- **Verificada (línea N):** fuiste al código, la línea N dice exactamente eso.
- **No verificable:** la afirmación es tan vaga que no hay línea que la confirme o niegue
  ("hace un procesamiento de los datos"). No es falsa; es humo. Cuenta como señal de mal prompt.
- **Falsa:** el código en la línea N dice **otra cosa**. Estas son oro — anótalas con precisión.

> Mínimo del bloque: **6 afirmaciones clasificadas**, y de ellas **al menos 1 Falsa o No verificable**
> con su línea. Con ~150 líneas de código ofuscado, las hay: si no encontraste ninguna, tu prompt
> pidió generalidades. Baja al detalle fino.

## MP-6 — Validación adversarial: cazar la imprecisión (25 min)

Ahora no leas para entender: **lee para refutar.** Empuja al chat a lo fino y contrasta contra el
código. Zonas donde el módulo suele hacer patinar a `/explain` (no las cites al chat: caza tú):

- **El umbral del asterisco.** El chat suele describir la banda `0.85–1.15` como *inclusiva*
  ("entre 0.85 y 1.15") cuando el código usa `>` y `<` (**exclusiva**). Un borde exacto de `0.85`
  **no** llevaría asterisco. → Ve a la línea del `if` del ratio y decide el veredicto.
- **`ordenar`.** Si el chat dice que ordena "por eficiencia" o "por cantidad de tareas", está mal:
  `ordenar` es un **bubble sort alfabético** de nombres. → Línea del `compareTo`.
- **El filtro de fechas.** Si el chat lo parafrasea como "toma de lunes a domingo" sin notar la
  condición exacta del segundo `isBefore`/`isAfter`, esa paráfrasis es **más generosa que el código**.
  Anótalo como afirmación a verificar — **no lo resuelvas aún**.
- **`calc`.** Fácil confundir estimado/real con real/estimado, o pasar por alto que **trunca**
  (división y cast a `int`). ¿La explicación menciona el truncamiento?

**Si tu par nota que el total dice `14` y al contar los registros salen más:** anótalo en el diario
como **"comportamiento sospechoso"** y **sigue**. NO lo confirmes ni lo niegues aún: el conteo raro se
caza formalmente en el integrador (es refactor ≠ fix — hoy no arreglamos nada todavía).

**Puesta en común (buffer):** cada par comparte **una** imprecisión con su línea. La más fina gana la
ronda del wrap-up.

## Criterios de "listo" (AM-2)

- [ ] Módulo recibido y commiteado en la rama `legacy-refactor`; `MainLegacy` corre.
- [ ] Hipótesis a-ojo escrita en el diario **antes** de usar `/explain`.
- [ ] Tabla de validación con **≥6 afirmaciones**, cada "Verificada" con **número de línea**.
- [ ] **≥1 imprecisión** (Falsa o No verificable) cazada, con la línea que lo prueba.
- [ ] Si apareció el conteo raro (14): registrado como "sospechoso", **no arreglado**.

> Esta tabla no muere aquí: se **anexa** a tu `EXPLICACION.md` del integrador como evidencia de que
> buscaste. "El chat lo dijo perfecto" sin esta tabla no es entrega válida.
