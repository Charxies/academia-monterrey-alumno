# Kata de auditoría (MP-2, AM-1) — 3 snippets, 15 min por pareja

> Estos tres archivos **simulan** salidas que Copilot te daría con total confianza. Ninguno vive en un
> proyecto Maven: se **leen y auditan**, no se ejecutan (el inyectable jamás se corre contra una BD).
> El objetivo del día en una frase: **leer / probar / explicar lo generado antes de aceptarlo.**

## Los 3 snippets

| Archivo | Qué "resuelve" |
|---|---|
| `TaskSearchRepository.java` | Buscar tareas por título (query) |
| `pom-fragment.xml` | Envío de correo (dependencia para el `pom.xml`) |
| `CommentService.java` | Crear/listar comentarios de una tarea (servicio) |

## Qué hacer (por pareja, driver/navigator)

1. **Clasifica cada snippet** en una de tres cajas y anota **por qué** (1 línea con evidencia):
   - 🟥 **vulnerable** — introduce un riesgo de seguridad explotable.
   - 🟥 **no verificable / fantasma** — depende de algo que quizá no existe.
   - 🟩 **correcto** — se puede aceptar tal cual (no lo "arregles" de más).

2. **En el vulnerable:** nombra el ataque, explica en 1–2 frases cómo se explota, y **corrígelo**
   (escribe la versión segura). Pista de S2D4: JPA, parámetros nombrados / queries derivadas.

3. **En el de la dependencia:** **verifica la coordenada completa** `groupId:artifactId:version` en
   [search.maven.org](https://search.maven.org) o [central.sonatype.com](https://central.sonatype.com).
   ¿Existe **ese** artefacto de **ese** groupId? Si no: ¿cuál es el real que resuelve el problema, y
   cuánta dependencia nueva necesita de verdad tu alcance mínimo? Nombra el ataque asociado a los
   nombres que los LLM alucinan.

4. **En el correcto:** confírmalo contra `.github/copilot-instructions.md`. Criterio de listo: **NO lo
   modificaste** (el snippet correcto no se "corrige" de más — cambiarlo por cambiar es un antipatrón).

## Criterio de listo (DoD de la kata)

- Identificaron los **3** (cuál es cuál) con una razón por cada uno.
- Corrigieron el **vulnerable** a una versión parametrizada/segura.
- Verificaron el **artefacto en Maven Central** por coordenada completa (no solo por nombre) y
  nombraron el ataque (*slopsquatting*).
- El snippet **correcto NO fue tocado**.
- Todo va al **diario de decisiones** (sección Día 5): qué clasificaron, qué corrigieron, qué verificaron.

> La respuesta la revisan en plenaria; el valor no es "adivinar", es **defender la clasificación con
> evidencia** (el código leído, el test que lo probaría, la búsqueda en Central).
