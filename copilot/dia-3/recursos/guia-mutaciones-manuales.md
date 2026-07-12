# Guía de mutación manual — catálogo M1/M2/M3, ritual git y matriz

> **Mutation testing a mano.** La idea (T4): rompes `src/main` a propósito; si la suite sigue verde, la
> suite **no protege esa línea** — un hecho medible, no una opinión. Esta guía trae el **catálogo
> estándar de 3 mutaciones** (canónico de aquí a D5), el **ritual git obligatorio** para no dejar
> mutaciones coladas, y la **plantilla de la matriz mutación×test** del integrador.
>
> Los ejemplos están sobre reglas de negocio que **tú escribiste** en `Task`/`TaskService` (S1D2–S2D3).
> Ajusta líneas/nombres a TU código real: los números de línea son de referencia.

---

## El catálogo estándar (3 mutaciones)

Elige las 3 **sobre las líneas que tus tests nuevos ejercitan** — mutar una línea que nadie prueba no
enseña nada. Cada una apunta a una regla concreta del dominio.

### M1 — Invertir una condición

Toma una condición de negocio y **niégala**. La regla "no se puede pasar a `DONE` sin assignee" vive en
`Task.setStatus` (el dominio lanza la CHECKED `TaskValidationException`; `TaskService.cambiarStatus` la
captura y la TRADUCE a `TaskStateException` → 422 en la frontera):

```java
// ANTES (Task.setStatus)
if (status == TaskStatus.DONE && assigneeId == null) {
    throw new TaskValidationException("No se puede marcar como DONE una tarea sin responsable");
}

// MUTACIÓN M1: == null  ->  != null
if (status == TaskStatus.DONE && assigneeId != null) {
```

**Qué debería cazarlo:** el test de service `cambiarStatus_aDoneSinAssignee_lanzaTaskStateException`
(una tarea con `assigneeId == null` que se pasa a `DONE` y espera la excepción), o su equivalente de
dominio `setStatus_doneSinAssignee_lanzaTaskValidationException`. Con la mutación, la condición se
vuelve falsa cuando NO hay assignee → **no lanza** → el test que esperaba la excepción se pone **rojo**.

### M2 — Mover un límite ±1

Desplaza una frontera un paso. La validación de título 3–120 vive en `Task.crear`:

```java
// ANTES (Task.crear)
if (title == null || title.length() < 3 || title.length() > 120) {
    throw new TaskValidationException("El título debe tener entre 3 y 120 caracteres");
}

// MUTACIÓN M2: < 3  ->  <= 3
if (title == null || title.length() <= 3 || title.length() > 120) {
```

**Qué debería cazarlo:** **solo** un test con un título de **exactamente 3 caracteres** que espera
éxito (`crear_tituloLongitud3_creaConEstadoTodo` / `crear_tituloLongitud3_esValido`). Con la mutación,
un título de 3 chars ahora **lanza** → el test que esperaba éxito se pone rojo. Si tu suite solo prueba
títulos de 5, 10 o 50 chars, **M2 sobrevive** y descubres que nunca probaste el borde. (Por eso el
prompt canónico de MP-1 pide el 3 explícito.)

### M3 — Neutralizar una llamada o quitar un `throw`

Borra un efecto. La regla "`dueDate` no puede estar en el pasado al crear" vive en `Task.crear`:

```java
// ANTES (Task.crear)
if (dueDate != null && dueDate.isBefore(LocalDate.now())) {
    throw new TaskValidationException("La fecha de vencimiento no puede estar en el pasado");
}

// MUTACIÓN M3: neutralizar el throw (comentarlo / dejar el if vacío)
if (dueDate != null && dueDate.isBefore(LocalDate.now())) {
    /* throw neutralizado (M3) */;
}
```

**Qué debería cazarlo:** el test `crear_dueDateEnElPasado_lanzaTaskValidationException` (una `dueDate` de
ayer que espera la excepción). Sin el `throw`, `crear` devuelve la tarea sin quejarse → el test se pone
rojo.

> **Regla de oro de la elección:** cada mutación debe apuntar a una **regla de negocio real** (un
> límite, una condición, un `throw`), no a línea de plomería (getters, logs). Mutar plomería genera
> "supervivientes" que no significan nada.

---

## El ritual git obligatorio (memorízalo)

El clásico atasco del día es la **mutación sin revertir**: media hora después la suite está roja
"misteriosamente", o peor, la mutación viaja al commit. El ritual lo evita:

```bash
# 1. Muta SOLO en el working tree — NO commitees.
#    (edita la línea a mano en src/main)

# 2. Corre la suite → espera ROJO.
mvn test                       # debe fallar el/los test(s) que cubren esa línea

# 3. Revierte la mutación.
git checkout -- src/main/java/com/taskflow/domain/Task.java
#    (o, si mutaste varios: git stash  →  luego  git stash pop para recuperar SI hubiera otros cambios)

# 4. Corre la suite → VERDE de nuevo.
mvn test

# 5. ANTES de cualquier commit de tests: confirma que producción está intacta.
git diff src/main              # DEBE salir vacío — cero mutaciones coladas (lo verifica el DoD (c))
git status
```

**Si una mutación NO pone nada en rojo** (sobrevive): el test que "cubría" esa línea era decorativo.
Escribe o pide el test que SÍ la caza, **míralo rojo con la mutación puesta, revierte, míralo verde.**
Así conviertes *"creo que mi suite sirve"* en un hecho.

> **Awareness (2 min):** **PIT / pitest** automatiza esto — corre decenas de mutantes por ti y te da un
> "mutation score". Es el stretch del integrador (`pitest-maven`). Hoy lo hacemos a mano para que
> entiendas **qué mide** antes de dejar que una herramienta lo mida. No es lo mismo un 90% de
> coverage que un 90% de mutación cazada.

---

## Plantilla de la matriz mutación×test (integrador)

Por cada módulo del integrador, una matriz. Marca `✓` si el test (fila) se pone **rojo** cuando aplicas
la mutación (columna). El contrato del DoD (b):

- **Cada mutación cazada por ≥1 test** (columna con al menos un `✓`).
- **Cada test nuevo caza ≥1 mutación** (fila con al menos un `✓`). Una fila sin `✓` es **sospechosa**:
  o la mutación estuvo mal elegida (re-elígela apuntando a la regla que ese test cubre), o el test es
  decorativo/redundante y **se poda**.

```
Módulo: <p. ej. domain / Task.crear+setStatus>

| Test nuevo                                         | M1 (cond.) | M2 (límite ±1) | M3 (throw) |
|----------------------------------------------------|:----------:|:--------------:|:----------:|
| crear_tituloLongitud3_creaConEstadoTodo            |            |       ✓        |            |
| crear_tituloLongitud2_lanzaTaskValidationException |            |                |            |  ← ¿fila vacía? sospechosa
| crear_dueDateEnElPasado_lanzaValidationException   |            |                |     ✓      |
| cambiarStatus_aDoneSinAssignee_lanzaStateException |     ✓      |                |            |
| ---                                                |            |                |            |
| ¿Cazada? (≥1 ✓ por columna)                        |    SÍ      |      SÍ        |    SÍ      |
```

> **Nota sobre filas vacías:** un test correcto puede no cazar **estas 3** mutaciones concretas y aun
> así ser legítimo (p. ej. `titulo=2` prueba el límite inferior inválido, que estas 3 no tocan). En ese
> caso **elige una 4ª mutación que sí lo ataque** (`<= 121` en el límite superior, o mover el `< 3` a
> `< 2`) y demuéstralo, o justifica en el diario por qué el test cubre una regla distinta. La matriz es
> una herramienta de sospecha, no una guillotina automática.

## Errores comunes al mutar (del canal de atascos)

- **Mutar y commitear.** El DoD (c) lo caza: `git diff src/main` debe estar vacío. Si ya pusheaste una
  mutación: `git revert` inmediato y cuéntalo en el canal — se aprende en público.
- **Mutar plomería.** Si la mutación no toca una regla, el "superviviente" no significa nada.
- **"Mi cobertura bajó al podar":** dirección correcta. El número baja, la protección sube; el número
  inflado era mentira de tests decorativos. La meta se mide con la suite **auditada**.
