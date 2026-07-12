# Kata — `ProjectSlugGenerator` (integrador, 75 min)

> Un util **nuevo y no trivial** para TU `taskflow-api` real: genera *slugs* de proyecto — URLs
> legibles para `Project` (dominio real, no juguete). El flujo es el hábito de toda la semana:
> **tests a mano → implementación asistida → diario**, con red de seguridad.

## Ubicación y restricción

- Paquete de referencia: **`com.taskflow.util`** — ajústalo al **paquete raíz real de TU api**.
- Tests en el espejo de `src/test/java`.
- **Restricción dura: solo JDK. Cero dependencias nuevas en el `pom.xml`.** El camino idiomático
  para quitar acentos es `java.text.Normalizer` en forma **NFD** + regex. Si Copilot te sugiere una
  librería que no está —el caso clásico es `StringUtils.stripAccents` de commons-lang3—: **existe y
  funciona, pero agregarla viola la restricción** → es **material de rechazo para el diario**, exista
  o no la librería. Esa es justamente una de las 2 decisiones de rechazo que la rúbrica te pide.

## La firma

```java
public static String slugify(String nombre, Set<String> existentes)
```

## Las 7 reglas (se aplican EN ORDEN)

1. **Minúsculas**; espacios y `_` → `-`.
2. **Sin acentos ni `ñ`** (`á→a`, `ñ→n`). El camino idiomático es `Normalizer` NFD + regex sobre las
   marcas combinantes — si Copilot lo propone, **leerlo y entenderlo es parte del punto** (el
   spot-check pregunta *por qué NFD y no NFC*).
3. **Eliminar** todo lo que no sea `[a-z0-9-]`.
4. **Colapsar** guiones múltiples; sin `-` inicial ni final.
5. **Máximo 40 chars SIN cortar palabra:** si el corte cae a media palabra, recorta hasta el último
   `-` anterior.
6. Si queda **vacío** → `"proyecto"`.
7. **Unicidad** contra `existentes`: sufijo `-2`, `-3`, … **El sufijo también respeta el máximo**
   (trampa de borde deliberada: si `base + "-2"` se pasa de 40, hay que recortar la raíz para hacerle
   sitio al sufijo).

> **Lo que suele salir mal a la primera** (y por qué los tests importan): la normalización (reglas
> 1–4) sale casi sola. El **truncado-sin-cortar-palabra** (regla 5) y el **sufijo-que-respeta-el-
> máximo** (regla 7) salen mal a la primera — los tests los cazan y se itera. Ese es el ejercicio.

## Los ≥8 casos mínimos (escríbelos TÚ, a mano)

Con **Copilot APAGADO** (toggle de MP-5): los tests son EL contrato y hoy lo escribes tú (disciplina
de S1D5). Mínimo estos 8 — puedes tener más:

| # | Caso | Entrada de ejemplo | Slug esperado |
|---|---|---|---|
| 1 | simple | `"Mi Proyecto"` | `mi-proyecto` |
| 2 | acentos + `ñ` | `"Diseño Español Ágil"` | `diseno-espanol-agil` |
| 3 | símbolos | `"API v2.0 (beta)!"` | `api-v20-beta` |
| 4 | colapso de guiones | `"  --Hola--Mundo--  "` | `hola-mundo` |
| 5 | truncado a media palabra | nombre cuyo slug pasa de 40 | recortado al último `-` ≤ 40 |
| 6 | vacío / solo símbolos | `"@#$ %^&*"` | `proyecto` |
| 7 | colisión simple | `"Mi Proyecto"`, existe `mi-proyecto` | `mi-proyecto-2` |
| 8 | colisión doble | existen `mi-proyecto` y `mi-proyecto-2` | `mi-proyecto-3` |

**Extra recomendado (la trampa de borde):** un slug base de ~40 chars que colisiona → el sufijo `-2`
obliga a recortar la raíz. Ese caso separa a quien leyó la regla 7 de quien la asumió.

JUnit 5 puro, sin Spring (es solo-JDK). Un test = un contrato de una regla.

## El flujo de 2 commits, en rama `s5d1-kata`

**Nunca en `main`:** tu pipeline de S3D4 dispara `build-and-push`/deploy en push a `main`. Trabaja y
pushea en rama.

```bash
git checkout -b s5d1-kata
```

**Fase 1 — tests a mano (Copilot APAGADO), ~20 min.** Escribe los ≥8 casos. Están **rojos** (aún no
hay implementación). El historial debe evidenciar el orden:

```bash
git add src/test/...
git commit -m "test: casos de slug de proyecto (rojos)"      # Commit 1
```

**Fase 2 — implementación asistida (Copilot ENCENDIDO), ~30 min.** Firma + comentario-intención con
las reglas → itera sugerencias (aceptar/editar/rechazar/ciclar) hasta que la suite pase **verde**.
Cada decisión relevante → **una fila del diario EN EL MOMENTO** (no de memoria al final).

```bash
mvn test                                                     # verde
git add src/main/... docs/copilot/diario-s5.md
git commit -m "feat: project slug generator asistido (verde)"  # Commit 2
git push -u origin s5d1-kata
```

## Rúbrica (DoD (d) reforzado)

Aceptar todo sin leer y que "pase verde" **NO** pasa la rúbrica — el spot-check oral lo destapa.

- [ ] **Historial con tests-primero:** 2 commits en orden (`test:` rojos → `feat:` verde).
- [ ] **Suite verde local:** los ≥8 tests pasan sobre la implementación asistida.
- [ ] **Diario ≥6 decisiones**, de ellas **≥2 rechazos** con porqué técnico + el **"resumen del
      día"** (dónde aceleró / dónde estorbó / qué rechacé).
- [ ] **Explicas la implementación en 1–2 min.**
- [ ] **Defensa oral de tus 2 rechazos** con argumento técnico.

**Spot-checks que puede hacer el instructor** (prepárate para responder):
- *"¿Por qué `Normalizer.Form.NFD` y no NFC?"* — NFD **descompone** (á → `a` + marca combinante) y
  entonces la regex `\p{M}` borra la marca y deja el ASCII; NFC **recompone** y no hay marca que
  borrar.
- *"Enséñame el test que cazó tu truncado."* — el de la regla 5.
- *"¿Qué pasa si el sufijo `-2` no cabe en 40?"* — la raíz se recorta para hacerle sitio (regla 7).

## Entregable

Rama **`s5d1-kata`** pusheada a tu `taskflow-api` con: los tests + la implementación + el diario
(`docs/copilot/diario-s5.md`), en los 2 commits en orden. Congela el entregable en el wrap-up.

---

## Stretch (solo si terminaste)

1. **Variante `TaskSearchNormalizer`** — mismo flujo (tests a mano → impl asistida). Normaliza un
   término de búsqueda de tareas para comparar sin ruido: `trim`, minúsculas, sin acentos, colapsa
   espacios internos. Reglas 1–2 de la kata, sin el resto:
   `public static String normalizar(String termino)` → `"  Configurar  CI/CD "` → `configurar ci/cd`
   (decide tú si `/` sobrevive: es una decisión de diseño **para el diario**). Reutiliza el
   `Normalizer` NFD que ya entendiste.
2. **Tu tasa de aceptación del día:** calcúlala con la tabla señal/ruido de MP-8.
3. **La kata con nombres basura:** repite pidiendo la implementación con la firma en nombres cochinos
   (`s(String a, Set<String> b)`) y compara qué propone (el A/B de MP-7, en grande).
