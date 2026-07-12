# Rúbrica — Integrador S5D4: `GET /tasks/overdue` vía agente + doble review

> Se evalúa el PR `feat: GET /tasks/overdue via agente + doble review` sobre TU `taskflow-api`, más el
> diario del día. **El instructor lee el `git log` y los timestamps, no solo el estado final:** la
> espec **antes** del primer prompt es evidencia, no promesa. La usan el instructor **y tu pareja**.
> Total: **100 pts**. Aprobado ≥ 70. La regla (d) —explicar el código que NO tecleaste— pesa explícito.

| Criterio | Qué se busca | Pts |
|---|---|---:|
| **1. Espec previa commiteada** | `specs/overdue.md` con contrato HTTP, reglas con su fuente (reusa `estaVencida()`), **decisiones de borde explícitas** (`dueDate == hoy` → no vencida; null → no; DONE → no aparece), criterios dado/cuando/entonces. Commit **antes** del primer prompt (timestamp lo prueba) | 20 |
| **2. Endpoint correcto y coherente** | `GET /tasks/overdue` devuelve tareas con `dueDate` pasada y `status != DONE`, ordenadas por `dueDate` ascendente; `200 []` sin resultados; auth JWT. **Reutiliza** `estaVencida()` (no recodifica el criterio) | 15 |
| **3. Suite completa verde** | Tests de S1–S4 **+ los nuevos del endpoint** en verde. Los nuevos cubren los bordes de la espec (hoy, null, DONE, vacío) | 15 |
| **4. Scope de los diffs (revisión del agente)** | El diario registra qué del **plan/diffs rechazó** y por qué (scope ANTES que contenido). Si hubo E1 (archivo fuera de alcance), fue rechazado y anotado. El diff del PR toca **solo** lo que la feature justifica (≤6 archivos) | 15 |
| **5. Tests existentes intactos (o justificado)** | Ningún test previo modificado sin **justificación escrita en el PR**. Si el agente intentó "arreglar" un test para que pase (E2): fue **rechazado + revert + follow-up** al código, registrado | 10 |
| **6. Doble review clasificado** | Review de Copilot **+** peer review humana; cada comentario clasificado correcto/ruido/incorrecto **con evidencia**; los válidos atendidos antes del merge. **Tabla comparativa** de hallazgos en el diario | 10 |
| **7. PR de D3 cerrado (MP-7)** | `test/cobertura-s5d3` con review de Copilot, **≥2 comentarios clasificados** en el diario, **mergeado con pipeline verde** antes del integrador | 5 |
| **8. Merge con pipeline verde** | `feature/overdue` mergeado a `main`; job de **tests** verde (si el deploy falla por infraestructura, documentado y skipeado leyendo el yml, no bloquea) | 5 |
| **9. Explicación oral (regla (d))** | En 1–2 min, **sin leer**: qué hace el endpoint + **la parte que no tecleó** (por qué reusa `estaVencida()`, por qué `dueDate == hoy` no cuenta) + una decisión que le rechazó al agente | 5 |

## Penalizaciones (restan sobre el total)

- **−20 · Espec después del código.** Si el commit de la espec es posterior al primer commit de
  implementación (o no existe): el proceso del día no se respetó, aunque el endpoint funcione.
- **−15 · Test existente modificado sin justificación.** El clásico E2 aceptado en silencio: el agente
  cambió una aserción para que pase y se mergeó. Los tests son el contrato, no un obstáculo.
- **−10 · Diff gigante aceptado en bloque.** Plan con más archivos de los que la feature justifica,
  aceptado sin re-especificar más chico ni rechazar nada.
- **−10 · Comentario de review "obedecido" sin evidencia.** Aplicaste un cambio confidently-wrong
  (o rechazaste uno correcto) sin leer el código / correr el test que lo decide.
- **−5 · Diff pegado sin leer.** Se cae en el oral: si no puedes explicar una línea del endpoint
  "tuyo", no era tuyo.

## Señales de excelencia (desempate hacia arriba)

- La **tabla comparativa** tiene una fila **"se nos fue a ambos"** identificada como hueco de la
  **espec** (zona horaria de `LocalDate.now()`, `dueDate == hoy`), no del código — y se anotó como
  follow-up honesto.
- El alumno defiende una decisión de **borde** con el test que la prueba corriendo en verde.
- Rechazó un comentario de Copilot **incorrecto** refutándolo en el PR con evidencia (no solo lo ignoró).
- Stretch hecho: una **segunda feature** de [`features-equivalentes.md`](features-equivalentes.md) con
  el mismo proceso end-to-end.

## Cómo lee esto el instructor / la pareja en ~4 minutos

1. `git log --oneline feature/overdue` → ¿la espec commiteó **antes** del primer prompt? (criterio 1, penaliz. −20).
2. `git diff main...feature/overdue -- src/test` → ¿algún test **existente** tocado? ¿justificado? (criterio 5).
3. Lista de archivos del PR → ¿el scope corresponde a la feature? (criterio 4).
4. `mvn test` → verde, con los tests de borde nuevos (criterios 2, 3).
5. Diario → tabla comparativa + rechazos del agente + clasificación de ambos PRs (criterios 4, 6, 7).
6. Pregunta oral 1–2 min (criterio 9). Aquí se cae el copy-paste sin comprensión.
