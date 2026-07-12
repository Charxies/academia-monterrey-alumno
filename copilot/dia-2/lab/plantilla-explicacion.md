# Plantilla — `EXPLICACION.md` del módulo legacy

> **Cópiala como `EXPLICACION.md` junto al paquete `com.taskflow.legacy`** en tu rama
> `legacy-refactor`. Es el entregable que materializa la regla (d) del programa —*explica tu código*—
> aplicada a código ajeno que hiciste tuyo.
>
> **La entrega no es "lo que dijo el chat".** Es lo que dijo el chat **corregido a mano por ti**, con
> la evidencia de que verificaste. Mínimo **1 corrección real** marcada. "El chat lo dijo perfecto"
> solo se acepta con la tabla de validación anexa como prueba de que buscaste.

---

## Cómo se llena (3 pasos)

1. **Pega la salida de `/explain`** de la versión **ya refactorizada** en la sección "Explicación
   generada". Tal cual salió, sin maquillar.
2. **Corrige a mano.** Cada afirmación imprecisa o falsa: ~~táchala~~ y escribe al lado tu corrección
   **+ la línea del código que la prueba** (`ReporteLegacy.java:NN`). Usa el formato de abajo.
3. **Anexa la tabla de validación** de AM-2 (la de `/explain` por capas). Es tu evidencia de búsqueda.

**Convención de marcado:**

- `~~texto del chat~~` → **corrección**. Evidencia: `Archivo.java:NN`.

Ejemplo (ilustrativo — bórralo y pon los tuyos):

> El método marca con asterisco a los usuarios cuyo ratio real/estimado está ~~entre 0.85 y 1.15
> inclusive~~ → **estrictamente entre 0.85 y 1.15 (banda exclusiva)**; un valor de exactamente 0.85 no
> lleva asterisco. Evidencia: `ReporteLegacy.java:NN` usa `> 0.85 && < 1.15`.

---

## `EXPLICACION.md` — estructura a entregar

```markdown
# ReporteLegacy — explicación

## Qué hace (en 3 frases)
<tu resumen honesto, escrito por ti — no el del chat>

## Explicación generada con /explain (versión refactorizada)
<pega aquí la salida cruda del chat>

## Correcciones a la explicación del chat
1. ~~<afirmación del chat>~~ → <corrección>. Evidencia: `ReporteLegacy.java:NN`.
2. ...
<mínimo 1; si de verdad no encontraste ninguna, escríbelo y la tabla anexa debe probar que buscaste>

## El bug que encontramos, y por qué se arregló aparte
- Contrato (javadoc): <qué promete>
- Comportamiento real fijado por la caracterización: <qué hacía>
- El hueco: <en una línea>
- Por qué el fix fue en commit separado del refactor: <refactor ≠ fix, en tus palabras>

## Decisiones de refactor que valió la pena registrar
- <p. ej. por qué RegistroTarea se quedó POJO / se volvió record>
- <qué sugerencia de Copilot rechazaste y por qué>

## Anexo — tabla de validación de /explain (AM-2)
| # | Afirmación del chat | Sobre | Veredicto | Evidencia (línea) |
|---|---|---|---|---|
| 1 | | | Verificada / No verificable / Falsa | |
```

---

## Criterio de "listo"

- [ ] La salida cruda de `/explain` está pegada (no parafraseada).
- [ ] **≥1 corrección** marcada con `~~tachado~~` → corrección + `Archivo.java:NN`.
- [ ] La sección del bug explica **por qué** el fix fue aparte (refactor ≠ fix).
- [ ] La tabla de validación de AM-2 está anexa.
- [ ] Puedes explicar todo esto **en 1–2 min sin leer el archivo** (te lo van a pedir).
