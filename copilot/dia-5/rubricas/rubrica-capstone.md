# Rúbrica — Capstone final: feature nueva de TaskFlow end-to-end con Copilot

> Se evalúa el **PR mergeado** de la feature (comentarios / etiquetas / notificaciones) sobre TU
> `taskflow-api`, más el test API en `taskflow-qa`, el diario y la explicación oral. Sirve para el
> **peer review cruzado** (una pareja evalúa a otra con esta hoja) **y** para la evaluación del
> instructor. **Se lee el `git log` y los timestamps, no solo el estado final:** la espec **antes** del
> primer prompt es evidencia, no promesa.
>
> Total: **100 pts**. Aprobado ≥ 70. La regla (d) del programa —**cualquiera de la pareja explica
> cualquier parte**— pesa explícito y puede tumbar puntos de otros criterios si se cae en el oral.

| # | Criterio | Qué se busca | Pts |
|---|---|---|---:|
| 1 | **Espec previa commiteada** (F1) | `docs/feature-spec.md` con modelo, contrato HTTP (códigos reales), reglas con su fuente, **decisiones de borde** y **"fuera de alcance" = recortes predefinidos**. Commit **antes** del primer prompt (timestamp lo prueba) | 15 |
| 2 | **Plan auditado** (F2) | El plan del chat quedó en la descripción del PR **con las correcciones anotadas**: qué propuso vs qué corrigieron contra su arquitectura (archivo inexistente, ignoró el `GlobalExceptionHandler`, dependencia de más). En el diario | 10 |
| 3 | **Feature correcta y coherente** (F3) | Los endpoints hacen lo que dice la espec, con los **códigos de respuesta reales**; reglas de negocio respetadas (auth, autor del contexto, unicidad, "no rompe la operación", según feature). Reutiliza arquitectura (advice, DTOs record, inyección por constructor) | 20 |
| 4 | **Tests que sobreviven al sabotaje** (F4) | Unit + integración de la feature en verde **y** al **romper la línea clave del service el test FALLA** (demostrado, no prometido). Sin tests decorativos: sin el antipatrón "mockear el comportamiento que se dice probar". Checklist D3 aplicado | 20 |
| 5 | **Test API en `taskflow-qa`** (F5) | ≥1 test RestAssured del **roundtrip canónico** de la feature, **verde en local** contra la API. El framework de S4 auditando el código asistido por IA de S5 | 10 |
| 6 | **Docs auditadas** (F6) | OpenAPI muestra los endpoints nuevos con **los códigos que el controller devuelve de verdad** (no los que "deberían") + sección de la feature en el README de `taskflow-api` | 5 |
| 7 | **PR limpio + doble review + merge verde** (F7) | PR con descripción editada (plan + enlace a la espec); **Copilot code review** solicitado + **peer review** con esta rúbrica; lo válido atendido; **merge con el job de tests verde** | 10 |
| 8 | **Diario de decisiones** | Filas por fase con hora inicio/fin; **generado vs escrito a mano**; **qué rechazaron y por qué** (mínimo 2 rechazos defendibles). Alimenta el reporte de aceleración | 5 |
| 9 | **Regla (d) — explicación oral cruzada** | El instructor pide a **cualquiera** de la pareja explicar **cualquier** parte (incluida la que "no le tocó" y la generada). En 1–2 min, sin leer: qué hace, por qué esa decisión de borde, qué le rechazaron a Copilot | 5 |

## Penalizaciones (restan sobre el total)

- **−20 · Espec después del código.** Commit de la espec posterior al primer commit de implementación
  (o inexistente): el proceso de la semana no se respetó, aunque la feature funcione.
- **−15 · Alcance inflado.** Quitaron recortes para "hacer más" y NO cerraron el DoD (a)/(b). El alcance
  mínimo era el contrato; ampliarlo y no terminar cuesta más que terminar lo mínimo.
- **−15 · Test decorativo aceptado.** Un test verde que **no cae** al sabotear la línea que dice
  proteger (incluye el mock-del-propio-comportamiento). El verde falso es peor que la ausencia de test.
- **−10 · Dependencia/artefacto no verificado en el `pom.xml`.** Se pegó una dependencia sin verificar
  la coordenada en Central (o se metió una que el alcance mínimo no necesitaba — típico en notificaciones).
- **−10 · Vulnerabilidad generada y aceptada.** SQL concatenado (u otra) que llegó al diff sin auditar.
- **−10 · Diff pegado sin leer.** Se cae en el oral: si no pueden explicar una línea "suya", no era suya.

## Señales de excelencia (desempate hacia arriba)

- Un **rechazo a Copilot documentado con evidencia**: nombraron la sugerencia, por qué la rechazaron y
  cómo lo verificaron (la parte que la rúbrica de la demo pondera más alto).
- El **sabotaje** se muestra en vivo: rompen la línea, el test se pone rojo, revierten, verde de nuevo.
- La feature **no tocó** nada fuera de su carril (auth, otros controllers, config) salvo el enganche
  justificado; diff chico y legible.
- Ambos miembros explican con soltura la parte del otro (rotación real, no de adorno).

## Cómo lo lee el instructor / la pareja evaluadora en ~5 min

1. `git log --oneline` → ¿la espec commiteó **antes** del primer prompt? (criterio 1, penaliz. −20).
2. Archivos del PR → ¿scope corresponde a la feature? ¿algún test existente tocado sin justificar?
3. `mvn test` (o el pipeline del PR) → verde, con los tests nuevos. Pide **un sabotaje en vivo** de una
   línea de negocio → el test debe caer (criterio 4).
4. `taskflow-qa`: el test de la feature en verde local (criterio 5).
5. Swagger + README → códigos reales (criterio 6). Diario → rechazos + tiempos (criterios 2, 8).
6. **Pregunta oral cruzada** (criterio 9): aquí se separa usar la herramienta de que la herramienta los use.
