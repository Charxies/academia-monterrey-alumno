# Plantilla de descripción del PR — cobertura auditada (S5D3)

> Copia **todo lo que está debajo de la línea** en la descripción de tu PR `test/cobertura-s5d3` y
> rellénalo. Los `<placeholders>` se sustituyen; los comentarios `<!-- ... -->` se borran. La meta se
> declara **ANTES de generar** (compromiso público). El PR queda **ABIERTO** — mañana lo revisa Copilot
> Code Review.

---

## Cobertura auditada de `<modulo1>` y `<modulo2>` + docs

### Meta declarada (antes de generar)
<!-- +15 puntos de líneas O 80% del módulo, lo que sea alcanzable. Se declara ANTES de ver resultados. -->

| Módulo | Métrica base | Meta declarada |
|---|---|---|
| `<modulo1>` | `<líneas base %>` | `<+15 pts → __%  /  o llegar a 80%>` |
| `<modulo2>` | `<líneas base %>` | `<+15 pts → __%  /  o llegar a 80%>` |

### JaCoCo antes / después
<!-- Números EXACTOS de target/site/jacoco/index.html. Líneas y branches por módulo. -->

| Módulo | Líneas antes | Líneas después | Branches antes | Branches después | Meta alcanzada |
|---|---|---|---|---|:---:|
| `<modulo1>` | `__%` | `__%` | `__%` | `__%` | ✅ / ❌ |
| `<modulo2>` | `__%` | `__%` | `__%` | `__%` | ✅ / ❌ |

### Matriz mutación×test
<!-- Una tabla por módulo. ✓ = el test (fila) se pone ROJO con la mutación (columna).
     Contrato: cada COLUMNA con ≥1 ✓ (mutación cazada) y cada FILA con ≥1 ✓ (test que caza algo). -->

**`<modulo1>`** — mutaciones aplicadas:
- **M1** (invertir condición): `<archivo:línea — antes → después>`
- **M2** (mover límite ±1): `<archivo:línea — antes → después>`
- **M3** (neutralizar throw/llamada): `<archivo:línea — antes → después>`

| Test nuevo | M1 | M2 | M3 |
|---|:--:|:--:|:--:|
| `<test_1>` |  |  |  |
| `<test_2>` |  |  |  |
| `<test_3>` |  |  |  |
| **¿Cazada? (≥1 ✓)** | SÍ/NO | SÍ/NO | SÍ/NO |

**`<modulo2>`** — mutaciones aplicadas:
- **M1**: `<...>`  · **M2**: `<...>`  · **M3**: `<...>`

| Test nuevo | M1 | M2 | M3 |
|---|:--:|:--:|:--:|
| `<test_1>` |  |  |  |
| `<test_2>` |  |  |  |
| **¿Cazada? (≥1 ✓)** | SÍ/NO | SÍ/NO | SÍ/NO |

### Tests podados (con razón de 1 línea)
<!-- Podar ES parte del trabajo, no un fracaso. Cada línea: test → razón (checklist ①-④ o redundancia). -->

- `<test_podado_1>` — `<razón: p.ej. "decorativo, falla ① — la mutación M3 lo dejaba verde">`
- `<test_podado_2>` — `<razón: p.ej. "redundante con crear_tituloLongitud3 — mismo escenario, dato de relleno">`
- `<... o "ninguno" si no podaste>`

### Casos propuestos por Copilot y descartados
<!-- Del "¿qué casos borde faltan?" — los que NO aplican al dominio, con la razón contrastada al código. -->

- `<caso alucinado>` — `<no existe esa regla en el dominio; contrastado contra Archivo.java:NN>` `<(ticket: sí/no)>`

### Verificación de docs
- [ ] `mvn javadoc:javadoc` **sin warnings nuevos** (doclint) sobre los públicos de ambos módulos.
- [ ] Comandos de la sección de testing del README **ejecutados** (no solo escritos): `<lista>`.

### Checklist de entrega (DoD)
- [ ] `git diff main -- src/main` **vacío** (cero mutaciones commiteadas — el diff solo toca tests/docs).
- [ ] Pipeline S3D4 **verde** (build+test en `pull_request`, sin deploy).
- [ ] Diario "Día 3" al día (≥1 entrada por bloque).
- [ ] **PR ABIERTO** — no lo mergees (mañana lo revisa Copilot Code Review).
