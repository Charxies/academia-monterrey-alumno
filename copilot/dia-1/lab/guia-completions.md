# Guía de completions — el contexto es el prompt (PM-1)

> **Sandbox sobre tu working tree real.** Todo lo de esta guía se teclea sobre tu `taskflow-api` de
> verdad (services, mappers reales) y **NADA se commitea**. Al cerrar el bloque:
> ```bash
> git restore .
> ```
> Lo único que hoy viaja al repo es la kata + el diario (guía aparte), en rama `s5d1-kata`.
>
> **⚠ VERIFICAR-PREVIO:** los atajos de abajo asumen keymap default de IntelliJ. Varían por SO y por
> versión del plugin — confírmalos en tu máquina antes de depender de ellos.

## El concepto central de la semana

El plugin arma un prompt con **lo que te rodea**: el archivo actual alrededor del cursor, las
pestañas/archivos abiertos, el lenguaje, la ruta del archivo. Los detalles exactos cambian por
versión; el concepto no:

> **El contexto es el prompt. Tu código ES el prompt.**

Consecuencia operativa inmediata, que aplicas hoy:
- **Abre los archivos relevantes ANTES de escribir** (la entidad `Task` y el `TaskResponse` cuando
  vas a tocar el mapper).
- **Nombra bien.** Tu código de 4 semanas con buenos nombres y records **ya es buen prompt**; el
  código cochino recibe sugerencias cochinas. Copilot **amplifica** la calidad que encuentra, en
  ambas direcciones.
- **Declara intención antes de pedir cuerpo.**

---

## MP-6 — Mecánica (sobre un service real)

Abre `TaskService` (o `ReportService`). Practica el manejo, no el resultado:

| Acción | Atajo (⚠ default IntelliJ; varía por SO/versión) |
|---|---|
| Aceptar la sugerencia **completa** | `Tab` |
| Aceptar **por palabra** | `Ctrl + →` (mac: `⌥ →` / `Cmd →` según versión ⚠) |
| **Ciclar** alternativas | `⌥ ]` / `⌥ [` (o el panel de sugerencias ⚠) |
| **Rechazar** | `Esc`, o simplemente **seguir tecleando** |

**Cuándo NI mirarlas:** nombre trivial, boilerplate que ya tienes en los dedos. **Si leer la
sugerencia tarda más que escribirla, escríbela.**

**Micro-ejercicio (guiado):** haz **5 aceptaciones deliberadas** (2 completas, 2 por palabra, 1
ciclada) y **3 rechazos conscientes**. El objetivo es que el manejo sea muscular antes de que
importe el contenido.

> **El Tab traicionero (dolor #5):** la costumbre de `Tab`-para-indentar acepta una sugerencia
> entera sin leerla. Si te pasa: `Ctrl+Z` inmediato **y una fila en el diario** ("aceptada sin leer"
> — honestidad ante todo). Considera aceptar-por-palabra como tu default personal.

---

## MP-7 — Las 3 técnicas, cada una sobre `taskflow-api`

### (a) Comentario-intención — el comentario dirige el cuerpo

Antes de un método nuevo en `ReportService`, teclea EXACTO este comentario y espera el ghost text:

```java
// normaliza el término de búsqueda: trim, minúsculas y sin acentos (Normalizer NFD)
public String normalizarBusqueda(String termino) {
    // <-- deja que Copilot proponga el cuerpo aquí
}
```

Lee lo que propone. ¿Usa `Normalizer.Form.NFD`? ¿O te sugiere `StringUtils` de una librería que no
está en tu `pom.xml`? Si es lo segundo: **material de rechazo** (no agregues dependencias por una
sugerencia).

### (b) Firma-primero — el contrato dirige la implementación

Escribe la **firma completa** y deja que proponga el cuerpo. Tú ya sabes LEER un stream sobre tu
repo (S1D4):

```java
public List<Task> tareasVencidas(Long projectId, LocalDate hoy) {
    // <-- cuerpo propuesto: un filter sobre el repo. ¿Reusa Task::estaVencida? ¿O reimplementa la
    //     condición dueDate.isBefore(hoy) && status != DONE a mano? Léelo y decide.
}
```

Fíjate si **reutiliza `Task::estaVencida()`** (el método que ya vive en tu modelo) o si reescribe la
regla a mano. Reutilizar el comportamiento del modelo es mejor diseño — decisión para el diario.

### (c) Patrón-en-archivo — el código vecino dirige el estilo

Con `TaskMapper` abierto (su `aResponse(Task)` a la vista), empieza a teclear la firma paralela y
observa cómo la calca:

```java
// ya existe, a la vista:  public static TaskResponse aResponse(Task task) { ... }

// empiezas a teclear esto y Copilot completa el cuerpo espejo:
public static ProjectResponse aResponse(Project project) {
    // <-- lo propone calcado del patrón de arriba
}
```

> (La spec llama a esta técnica con el ejemplo genérico `toDto(Task)`→`toDto(Project)`; en TU código
> el método real se llama `aResponse` — mismo patrón, mismo aprendizaje.)

### Cierre de MP-7 — el experimento A/B (la evidencia, vivida)

Toma el MISMO método de (b) y escríbelo **dos veces**, en pantalla dividida:

```java
// VERSIÓN A — nombres buenos
public List<Task> tareasVencidas(Long projectId, LocalDate hoy) { ... }

// VERSIÓN B — nombres basura
public List<Task> m1(Long x, LocalDate y) { ... }     // o incluso List<Task> a
```

Compara las sugerencias. La versión con buenos nombres recibe un cuerpo dirigido; la de basura, una
sugerencia genérica o equivocada. **Eso es T6 en vivo:** el código bueno es buen prompt.

---

## MP-8 — Medir señal/ruido: "aceptar solo lo que leíste"

Implementa un método pequeño — p. ej. `contarPorPrioridad()` en `ReportService` (devuelve
`Map<Priority, Long>`) — llevando la cuenta en esta tabla. **Es, literalmente, la primera página de
tu diario de decisiones.**

| # | Sugerencia (≤1 línea) | Vista | Aceptada íntegra | Aceptada editada | Rechazada | Razón (≤1 línea) | Cómo verifiqué |
|---|---|:---:|:---:|:---:|:---:|---|---|
| 1 | `groupingBy(Task::getPriority, counting())` | ✅ | ✅ |  |  | es exactamente el patrón de S1D4 | lectura + compilador |
| 2 | `import org.apache.commons...` | ✅ |  |  | ✅ | no está en el pom | lectura |
| 3 | ... |  |  |  |  |  |  |

Al terminar, en la puesta en común honesta (sin regaño): **¿qué % aceptaste? ¿aceptaste algo que no
leíste?** Esa métrica es el germen del **reporte de aceleración** de D5.

---

## Al cerrar PM-1 — la higiene, otra vez

```bash
git status      # confirma que solo tocaste el working tree, sin commits
git restore .   # descarta TODO lo del sandbox — nada de esto viaja al repo
git status      # árbol limpio de nuevo
```

Nada de esta guía se entrega. Lo que se entrega es la **kata** (siguiente guía).
