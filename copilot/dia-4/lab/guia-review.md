# Guía — Copilot Code Review y el marco correcto / ruido / incorrecto · PM-1 + Integrador

> Un comentario de Copilot es **la opinión de un revisor más**. Se responde con **evidencia**, no con
> obediencia. Esta guía se usa dos veces hoy: en **MP-7** (cerrar el PR de D3 `test/cobertura-s5d3`) y
> en el **integrador** (el PR de `feature/overdue`).

## 1. Cómo solicitar el review (⚠ VERIFICAR-PREVIO)

⚠ VERIFICAR-PREVIO — la forma exacta cambia con la versión/plan y con la config del repo u org:

- En el PR, busca la acción de **agregar a "Copilot" como reviewer** (lista de Reviewers), **o**
- el **setting del repositorio/organización** que activa Copilot code review (automático en cada PR),
  **o** el comando/acción equivalente que te indique el instructor la semana de la sesión.

El review llega como comentarios en el PR (en "Files changed" / "Conversation"), como los de un
humano. Los reviews de tus PRs de D3 los **solicitaste en el warm-up**: ya están llegando durante la
mañana.

### Si "Copilot" NO aparece como reviewer (dolor 9 — fallback vía Chat)

Se pierde la integración en el PR, **se conserva la clasificación de hallazgos**. Copia el diff del
PR y pégalo en Copilot Chat con este prompt de review estructurado:

```text
Actúa como revisor de código senior. Revisa este diff de un endpoint Spring Boot (Java 21, JPA,
JUnit 5). Reporta SOLO hallazgos accionables, cada uno como:
  - [severidad: alta/media/baja] archivo:línea — problema — por qué importa — arreglo sugerido
No inventes convenciones: si citas una, di de dónde sale. No comentes estilo si no rompe una regla
del proyecto. Distingue bug real de preferencia. Si algo está bien, no lo comentes.

<pega aquí el diff completo>
```

Aplica igual al PR de D3 (MP-7) y al del integrador. Lo que devuelva se clasifica con el mismo marco.

---

## 2. El marco de clasificación: correcto / ruido / incorrecto

Cada comentario cae en **una** de tres cajas. La caja se decide **con evidencia**, no con la
sensación de si suena convincente.

| Clase | Qué es | Qué haces | Evidencia que lo prueba |
|---|---|---|---|
| **Correcto** | Señala un problema real (bug, hueco, riesgo, convención rota de verdad) | Atiéndelo (follow-up del agente o a mano) **o** anótalo como follow-up en el PR si es trivial | El test que lo confirma, la regla del repo que cita, la línea que efectivamente falla |
| **Ruido** | No está mal, pero no aporta: estilístico, redundante, "podrías considerar…", preferencia sin regla detrás | Lo reconoces y lo dejas pasar (o lo resuelves si cuesta 10 s), sin culpa | Que NO existe regla del proyecto que lo respalde; que el test sigue verde con y sin el cambio |
| **Incorrecto** | Afirma algo **falso** sobre tu código. El peligroso: **confidently-wrong** — suena impecable, cita una convención plausible, y está mal | Lo **refutas con evidencia** en el PR (respondes al comentario) y NO lo aplicas | El código referido leído, o el test que lo contradice corriendo en verde |

> **La trampa (E3):** el comentario *confidently-wrong* es el más caro. Regla dura: **veredicto solo
> con evidencia** — leer el código referido y/o correr el test que lo contradice. "Suena bien" no es
> evidencia. Un ejemplo típico en `overdue`: *"esto debería usar `!isAfter` en vez de `isBefore` para
> incluir el borde"* — plausible, pero si tu decisión de espec es que **`dueDate == hoy` NO cuenta
> como vencida** (consistente con `estaVencida()`), el comentario es **incorrecto** y lo refutas con
> el test de borde que ya tienes verde.

---

## 3. Qué VE y qué NO ve Copilot review (calibra tus expectativas)

| Suele cachar | Suele NO ver |
|---|---|
| Null-safety, off-by-one, ramas sin cubrir, recursos sin cerrar | Que tu **espec** tenía un hueco (zona horaria de `LocalDate.now()`, `dueDate == hoy`) |
| Convenciones mecánicas, nombres, imports | Reglas de **tu** dominio que no están en el código (las conoces tú, no él) |
| Patrones de bug conocidos en el diff | El **contexto de negocio**: por qué elegiste ese orden, esa decisión de borde |

Corolario del integrador (spoiler que el instructor revela al final): **Copilot y el humano cachan
conjuntos DISTINTOS**, y lo que se le va a **ambos** suele ser un hueco de la **espec**, no del código.
Por eso la feature nace de una espec escrita **antes**.

---

## 4. MP-7 — cerrar el PR de D3 (la promesa de ayer, pagada)

Sobre TU `test/cobertura-s5d3` (que quedó abierto en D3 justo para esto):

1. Toma **2–3 comentarios** del review de Copilot. Clasifícalos correcto/ruido/incorrecto **al
   diario, con evidencia** (mínimo del DoD: ≥2 clasificados).
2. Atiende lo **válido trivial** (o anótalo como follow-up en el PR).
3. **Mergea con pipeline verde.** Desde este momento `main` incluye la cobertura auditada de D3 — y
   **`feature/overdue` nace de ese main** (`git checkout main && git pull`).

> ⚠ Si el merge dispara el CD de S3 y el **job de deploy** falla por infraestructura (no por tu
> código): el gate es el **job de tests**. Documenta y skipea el job de deploy leyendo tu propio yml;
> no bloquees el merge.

## 5. Integrador — doble review y tabla comparativa

1. Solicita el review de Copilot en el PR de `feature/overdue` (⚠ como en §1).
2. **Mientras llega**, tu pareja hace **peer review humana** del diff en "Files changed" y anota sus
   hallazgos en el diario **ANTES** de abrir la conversación del PR (para no contaminarse con los
   comentarios de Copilot).
3. Clasifica cada comentario de Copilot (correcto/ruido/incorrecto, con evidencia) y consolida la
   **tabla comparativa** (plantilla en [`anexo-diario-d4.md`](anexo-diario-d4.md)):

   | Hallazgo | Lo cachó Copilot | Lo cachó el humano | Clase | Atendido |
   |---|:--:|:--:|---|:--:|
   | … | ✓/✗ | ✓/✗ | correcto/ruido/incorrecto | ✓/✗ |

   La fila más valiosa suele ser la de **"se nos fue a ambos"** — casi siempre un hueco de la espec.
4. Atiende lo válido, deja la suite verde, re-request review si aplica, **merge a main**.
