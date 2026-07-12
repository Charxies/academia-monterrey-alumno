# Rúbrica — Integrador S5D2: Legacy dominado

> Se evalúa la rama `legacy-refactor` de tu `taskflow-api`. **El instructor revisa el `git log`, no
> solo el estado final:** la historia de cómo llegaste ahí es parte de la nota. Total: **100 pts**.
> Aprobado ≥ 70. La regla (d) del programa —explicar tu código— pesa explícito en la última fila.

| Criterio | Qué se busca | Pts |
|---|---|---:|
| **1. Suite de caracterización** | Golden master verde y commiteada **antes** de refactorizar. Cubre mínimo: total, ≥2 usuarios, encabezado, un bucket. Fija el comportamiento REAL (no el del javadoc) | 15 |
| **2. Calidad del historial de commits** | Recepción → caracterización → **≥4 commits de refactor incremental** → fix aparte. Cada commit con la suite verde. **Intenciones separadas**: un commit = una idea | 20 |
| **3. Refactor efectivo** | `generar(...)` en métodos ≤15 líneas con nombres honestos; constantes en vez de números mágicos; streams donde aportan; `ordenar` → `Arrays.sort`/`sorted()`. El **output no cambió** (la caracterización lo prueba) | 20 |
| **4. Bug: cazado, testeado, arreglado aparte** | Test que demuestra el bug escrito **primero** (nació rojo: la tarea del domingo debe aparecer); fix del `isBefore`→inclusivo; golden master actualizado **en el mismo commit del fix**; mensaje `fix:` claro. **No** mezclado con el refactor | 20 |
| **5. `EXPLICACION.md`** | Salida cruda de `/explain` (versión refactorizada) + **≥1 corrección marcada** (~~tachado~~ → corrección + `Archivo.java:NN`) + tabla de validación de AM-2 anexa | 10 |
| **6. Diario de decisiones** | Sección Día 2 con **≥5 entradas** reales del día (incluye el antes/después de las custom instructions y ≥1 rechazo con porqué técnico) | 5 |
| **7. Explicación oral (regla (d))** | En 1–2 min, **sin leer el archivo**: qué hace el módulo + **una imprecisión que le corregiste al chat** con la línea que lo prueba | 10 |

## Penalizaciones (restan sobre el total)

- **−15 · Mega-commit.** La clase entera regenerada de un golpe (o un solo commit "refactor" gigante).
  Aunque quede verde: el tamaño del paso ES la red, y no lo respetaste.
- **−10 · Refactor y fix mezclados.** El fix del bug metido dentro de un commit de refactor. No se
  puede distinguir "lo rompí" de "lo arreglé".
- **−10 · Caracterización que aserta 15 desde el inicio.** Si tu golden master "correcto" (leído del
  javadoc) reemplazó al comportamiento real antes del fix, no caracterizaste: reescribiste. La
  caracterización fotografía lo que HAY, bug incluido.
- **−10 · Suite roja o que no compila** en el estado entregado.
- **−5 · Diff pegado sin leer.** Se detecta en el oral: si no puedes explicar una línea de "tu"
  refactor, no era tuyo.

## Señales de excelencia (desempate hacia arriba)

- El `EXPLICACION.md` corrige una imprecisión **fina** (el borde exclusivo del asterisco, el orden
  alfabético de `ordenar`, el truncamiento de `calc`) — no una obvia.
- La discusión del diario argumenta **por qué** `RegistroTarea` se quedó POJO (o por qué convertirlo a
  `record` no valía el costo) en lugar de tocarlo por reflejo.
- Stretch hecho: tests **de contrato** (no de caracterización) sobre el método del filtro de fechas.

## Cómo lee esto el instructor en 3 minutos

1. `git log --oneline` de `legacy-refactor` → ¿la historia se cuenta sola? (criterios 2 y 4).
2. `git show <commit del fix>` → ¿el test del bug y la actualización del golden master están **juntos**
   y **solos**? (criterio 4).
3. `mvn test` → verde (criterios 1 y 3).
4. Abre `EXPLICACION.md` → ¿hay ~~tachado~~ con línea? (criterio 5).
5. Pregunta oral de 1–2 min (criterio 7). Aquí se cae el copy-paste sin comprensión.
