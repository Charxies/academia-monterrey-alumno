# Reporte de aceleración — Capstone S5

> **Entrégalo en `docs/reporte-aceleracion.md` de TU `taskflow-api`.** Se arma **desde tu diario de
> decisiones** (`docs/copilot/diario-s5.md`, llenado desde D1) — no de memoria al final. Es lo que
> presentas en la demo (§3) y lo que la `rubrica-demo.md` evalúa más alto en su fila "qué rechazamos".
>
> **Regla de honestidad:** un reporte que solo dice "aceleró todo" no es creíble ni útil. El valor está
> en dónde estorbó y qué rechazaste. La autopercepción sobreestima la ganancia (T3); tus **minutos por
> fase** son el dato, no la sensación.

**Feature:** `<comentarios / etiquetas / notificaciones>` · **Pareja:** `<n1>` · `<n2>` · **PR:** `<link>`

---

## 1. Tabla por fase (del diario)

| Fase | Inicio–Fin | Min | Qué **generó** Copilot | Qué escribimos **a mano** | Qué **rechazamos** y por qué |
|---|---|---:|---|---|---|
| F1 espec | `__:__`–`__:__` |  | (esta fase suele ser sin IA) |  |  |
| F2 plan | `__:__`–`__:__` |  |  |  |  |
| F3 implementación | `__:__`–`__:__` |  |  |  |  |
| F4 tests | `__:__`–`__:__` |  |  |  |  |
| F5 test QA | `__:__`–`__:__` |  |  |  |  |
| F6 docs | `__:__`–`__:__` |  |  |  |  |
| F7 PR/review | `__:__`–`__:__` |  |  |  |  |
| **Total** |  | **__** |  |  |  |

## 2. Dónde aceleró de verdad (con evidencia de TU semana)

<!-- Lo que TUS datos digan, no titulares. Típico: boilerplate (DTOs, mapper), tests repetitivos,
     explicar código ajeno. Cita la fila del diario que lo respalda. -->
-
-

## 3. Dónde estorbó (fricción real)

<!-- Sugerencias que hubo que deshacer, contexto que confundió, tiempo auditando > tiempo ahorrado.
     Al menos una fase donde auditar tardó más que teclear, si la hubo. -->
-
-

## 4. Qué rechazamos y por qué (lo que más pesa)

<!-- Mínimo 2 rechazos defendibles. Por cada uno: qué sugirió, por qué lo rechazamos, cómo lo
     verificamos (test / lectura / búsqueda en Maven Central). -->

1. **Sugerencia:** `<...>` — **Rechazo porque:** `<...>` — **Verificado con:** `<test/lectura/Central>`.
2. **Sugerencia:** `<...>` — **Rechazo porque:** `<...>` — **Verificado con:** `<...>`.

## 5. El sabotaje (prueba de que los tests protegen)

<!-- La línea de negocio que rompiste, el test que se puso rojo, que revertiste y volvió a verde. -->
- **Línea saboteada:** `<archivo:método — qué cambiaste>` -> **test rojo:** `<nombre>` -> revertido, verde.

## 6. Balance en una frase

<!-- ¿La herramienta te aceleró, y a costa de qué criterio? La conclusión operativa de T3: medir lo
     propio > citar estudios. -->

---

> **Del diario al reporte:** este documento no inventa nada — reordena las 5 secciones "resumen del día"
> de tu diario (D1–D5) en la vista que la demo necesita. Si el diario está honesto, esto se llena en 15 min.
