# TaskFlow API (Semana 3, Día 1) · STARTER

Punto de partida de S3D1. Es la API de **fin de S2D5 (v2.0)** — segura con JWT — más los **esqueletos
de hoy**. `src/main` **no se toca** en todo el día: los tests de hoy protegen código que YA existe. El
día entero vive en `src/test` y el `pom`.

## Estado del starter (checklist de arranque)

```bash
mvn -q compile        # COMPILA (src/main intacto de S2D5)
mvn test              # VERDE desde el minuto cero  <-  ESTA es tu LÍNEA BASE: cronométrala
mvn verify            # pasa, pero AÚN SIN GATE: el bloque JaCoCo del pom viene COMENTADO (lo activas en MP-10)
```

- Los tests **heredados de S2D5** están en sus paquetes viejos (`controller/`, `security/`, `repository/`,
  `service/`) y corren en **verde** — son la línea base que hoy se mide y se reorganiza.
- Los **esqueletos nuevos** (`unit/`, `integration/`) salen "verdes vacíos": cuerpos en `TODO`. Si alguien
  lo nota, es justo el fantasma del **test verde mentiroso** de S1D5 (un verde que no verifica nada).
- El **bloque JaCoCo** está comentado en `pom.xml` con TODOs numerados (MP-10). Activarlo es el patrón
  springdoc/postgresql de S2D3: descomentar y operar.
- Script `src/test/resources/sql/escenario-tareas.sql` **provisto** (lo usa el `@Sql` de MP-8).

## Mapa de los TODO del día

| Bloque | Archivo | Qué hacer |
|---|---|---|
| MP-1/2/3/5 | `unit/TaskServiceTest` | Mockito puro: `when/verify/captor`, `@Nested` (esqueleto con nombres dados) |
| MP-4 | `unit/TaskValidationTest` | 4 tests de S1D5 → 1 `@ParameterizedTest` de 6 casos (fronteras 3–120) |
| MP-6 | `controller/TaskControllerTest` | migrar `@SpringBootTest` → `@WebMvcTest` (medir antes/después) |
| MP-7 | `controller/ProjectControllerTest` | mismo tratamiento de slice |
| MP-8 | `repository/TaskRepositoryTest` | `flush()+clear()` (falso verde) + `@Sql` + `@EnumSource` |
| MP-9 | `integration/FlujoCompletoE2ETest` | el viaje E2E completo (register→login→…→DONE) |
| MP-10 | `pom.xml` | descomentar JaCoCo, operar el gate (y verlo fallar una vez) |
| Integrador | (toda la suite) | reorganizar a `unit/`/`slice/`/`integration/`; borrar el parche `service/TaskServiceTest` |

## Comandos del día

```bash
mvn test                                   # rápido (NO valida el gate)
mvn verify                                 # el GATE (jacoco:check) — la entrega se valida con ESTO
mvn test -Dtest=TaskControllerTest         # una clase sola (el ciclo de dev que corres 40×/día)
# reporte de cobertura (tras activar JaCoCo y correr verify): target/site/jacoco/index.html
```

## PLANTILLA — Tabla de tiempos (llénala durante el día; el integrador la exige poblada)

> Los números varían por máquina; la FORMA no. Mide DOS cosas: la suite completa y **una clase sola**.

| Métrica | Antes (S2D5, `@SpringBootTest`) | Después (S3D1, slice) |
|---|---|---|
| `mvn test` — suite completa (s) | _______ | _______ |
| # arranques de contexto Spring **completos** | _______ | _______ |
| `mvn test -Dtest=TaskControllerTest` (s) | _______ | _______ |
| Qué levanta esa clase sola | contexto COMPLETO | _______ |

## PLANTILLA — Inventario de la pirámide (llénala durante el día)

| Nivel | Paquete | # tests | Qué protege |
|---|---|---:|---|
| unit | `com.taskflow.unit` | _____ | lógica en aislamiento (ms, sin Spring) |
| slice | `com.taskflow.slice` | _____ | contratos de capa (`@WebMvcTest`, `@DataJpaTest`) |
| integration | `com.taskflow.integration` | _____ | el cableado entero (el lento, una sola vez) |
| | **Total** | _____ | |

## PLANTILLA — Cobertura (tras MP-10)

- Cobertura global de líneas: **____%** (gate en **70%**).
- El gate FALLÓ una vez con umbral 0.99: mensaje leído → `lines covered ratio is 0.__, but expected minimum is 0.99`.

## Entregable del día

Commit + push: **`test: quality gate jacoco 70 + slices`**. La suite reorganizada en pirámide, ≥3
`@ParameterizedTest`, slices migrados y medidos, E2E verde, y **`mvn verify` VERDE con el gate en 70%**.
