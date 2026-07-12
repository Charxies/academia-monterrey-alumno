# Guía — Copilot Edits / agent mode en VS Code · AM-2

> Hoy en VS Code (⚠ VERIFICAR-PREVIO: nombres exactos de paneles/modos y cómo se cambia entre ellos;
> hoy VS Code porque estas capacidades están más maduras ahí — IntelliJ sigue siendo tu IDE diario).
> El objetivo NO es que el agente escriba código bonito: es que **tú** domines el ciclo
> **especificar → revisar plan → aceptar/rechazar por archivo → correr tu suite**.
>
> ⚠ VERIFICAR-PREVIO / plan B: si tu asiento o tu versión no traen Edits/agent mode, salta a la
> [sección "Plan B: inline chat"](#plan-b-sin-edits--agent-mode) — se pierde la orquestación
> multi-archivo, se conserva íntegro el ciclo.

## Antes de empezar

```bash
cd taskflow-api
git checkout main && git pull
git checkout -b lab/edits-warmup       # rama desechable: aquí solo se practica la MECÁNICA
docker compose up -d && mvn test        # baseline verde — sin esto no se le pide nada al agente
```

Verifica que Copilot detecta `.github/copilot-instructions.md` (⚠ el soporte por modo cambia con la
versión): un cambio generado que ignore las convenciones (español en comentarios, records para DTOs,
inyección por constructor) es **follow-up**, no aceptación resignada.

---

## Vocabulario mínimo (T4)

| Concepto | Qué es | Dónde mirar |
|---|---|---|
| **Working set / contexto** | Los archivos que el agente puede leer y editar | ⚠ el panel que lista los archivos "en juego" |
| **Plan** | Lo que el agente SE PROPONE hacer, antes de tocar código | Revísalo **primero**: ¿los archivos listados corresponden a lo pedido? |
| **Diff por archivo** | El cambio propuesto, archivo por archivo | Se acepta/descarta **por partes**, no todo o nada |
| **Edits vs agent mode** | Edits: editar N archivos que tú encuadras. Agent mode: además planea-y-ejecuta (puede correr comandos) | ⚠ VERIFICAR-PREVIO cuál tienes y cómo se cambia |

**Las dos reglas de revisión, en orden:**

1. **SCOPE primero.** ¿La **lista de archivos** del plan/diff corresponde a lo que pediste? Un
   archivo de más es una bandera roja **antes** de leer una sola línea.
2. **CONTENIDO después.** Solo cuando el scope cuadra, lees el diff de cada archivo.

---

## MP-4 — Mecánica en frío (15 min): dominar aceptar/descartar por archivo

Cambio deliberadamente trivial que toca **2 archivos**, solo para aprender el flujo (la rama se tira
después): **unificar el mensaje de una excepción y su aserción en el test.**

Ejemplo sobre tu repo (adapta a tus nombres reales):

> Prompt: *"En `Task.setStatus`, cuando se intenta pasar a DONE sin assignee, unifica el mensaje de
> la `TaskValidationException` a exactamente `"No se puede completar una tarea sin responsable"`, y
> actualiza la aserción del test que verifica ese mensaje. No toques nada más."*

Practica el ciclo hasta que sea reflejo:

- [ ] Leer el **plan**: ¿lista exactamente `Task.java` + el test? Si lista más, ya es señal.
- [ ] Abrir el diff de `Task.java` → **aceptar**.
- [ ] Abrir el diff del test → **aceptar**.
- [ ] `mvn test` → verde.
- [ ] Practica también **descartar** un archivo y volver a pedirlo, para tener el gesto en los dedos.

> Al terminar: `git checkout main && git branch -D lab/edits-warmup` (o déjala; da igual, era mecánica).

---

## MP-5 — Sesión multi-archivo real (25 min): `?priority=` en `GET /projects/{id}/tasks`

En pares (driver/navigator). El cambio toca **controller + service + (repo) + test** y es de verdad:
hoy `GET /projects/{id}/tasks` acepta `?status=` pero **no** `?priority=`.

### La especificación que le das al agente (contexto + restricciones + criterio de listo)

> Prompt: *"Agrega un filtro opcional `?priority=` a `GET /projects/{id}/tasks` en `ProjectController`
> (método `getTareasDeProyecto`), con el MISMO patrón que el `?status=` existente. El servicio filtra
> por prioridad dentro del proyecto (reusa el repositorio: una derived query estilo
> `findByProjectIdAndPriority`, como ya existe `findByAssigneeIdAndStatus`). Respeta las convenciones
> de `.github/copilot-instructions.md`. Agrega UN test de slice que verifique el filtro. **NO** toques
> otros controllers, ni `application.yml`, ni la seguridad. Criterio de listo: `mvn test` verde y el
> endpoint responde la lista filtrada."*

Nota por qué esta espec es buena: dice **qué archivos** (contexto), **qué NO tocar** (restricciones)
y **cuándo está listo** (criterio). La calidad del resultado es función de la especificación (T5).

### El ciclo de revisión (esto es lo que se evalúa, no el código)

1. **Plan → SCOPE.** ¿Los archivos son `ProjectController`, `ProjectService`, `TaskRepository` y un
   test? Si aparece `application.yml`, `SecurityConfig`, u **otro** controller → eso es **E1** (abajo).
2. **Diff archivo por archivo → CONTENIDO.** ¿El derived query se llama bien? ¿El filtro respeta el
   patrón de `?status=`? ¿El comentario está en español? ¿El DTO sigue siendo `TaskResponse`?
3. **Corre TU suite completa** (`mvn test`). La suite de S1–S4 es la red.
4. **Follow-ups** para lo que no cuadró (mejor un follow-up chico que aceptar y "arreglar a mano"
   sin registrar). Si el plan lista **más de lo razonable** (>6 archivos para esto), **no lo revises
   más duro: re-especifica más chico** ("solo el service primero", "sin tocar tests todavía").
5. **Diario:** qué del plan/diffs **rechazaste** y por qué.

### E1 — el agente toca un archivo FUERA DE ALCANCE

No es sembrable (la conducta del agente no es determinista): **puede** pasar y hay que saber tratarlo.

- **Síntoma:** el plan/diff incluye un archivo que la feature no justifica (reformatea
  `application.yml`, "mejora" otro controller, reordena imports en una clase ajena).
- **Tratamiento:** **rechaza ESE archivo**, acepta el resto. El scope se revisa **antes** que el
  contenido: un archivo de más es motivo de rechazo aunque su diff "se vea bien".
- Si en tu sesión no pasa: perfecto, se celebra. El instructor tiene el fallback enlatado
  (grabación/capturas de una sesión real donde tocó de más, con el diff del rechazo).

---

## T5 — Cómo pedir (resumen operativo)

- **Contexto:** qué archivos/clases están en juego (nómbralos).
- **Restricciones:** convenciones (cita `copilot-instructions.md`), y **qué NO tocar**.
- **Criterio de listo:** "suite verde", "el endpoint responde X", "sin tocar tests".
- **Diffs chicos y verificables > pedidos gigantes.** Cuándo un follow-up y cuándo **descartar todo y
  re-especificar** desde cero (cuando el plan ya nació torcido, re-especificar es más barato que
  parchar).

---

## Plan B: sin Edits / agent mode

Si tu asiento/versión no los trae (⚠ el instructor lo verifica la semana previa), haces la **misma
feature con inline chat, archivo por archivo**, en este orden: **controller → service → (repo) →
test**. Se pierde la orquestación multi-archivo; **se conserva el ciclo**: especificar antes, revisar
el diff de cada archivo, correr la suite, follow-up. La rúbrica no cambia.

Con **cuota agotada / rate limits** (norma con Copilot Free): turnen el asiento activo driver/navigator
—el navigator revisa diffs, que no gasta cuota— y prioricen la cuota para la feature del integrador,
no para la mecánica del warm-up.
