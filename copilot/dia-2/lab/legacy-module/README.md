# Módulo legacy — Reporte semanal de productividad

> Código **heredado**: lo escribió alguien que ya no está en el equipo. Compila, corre y —según
> quien lo escribió— "funciona". No tiene tests, no tiene dueño, y nadie recuerda del todo qué hace.
> Tu trabajo esta semana es el encargo más común de la vida real: **volverlo mantenible sin romperlo.**

Es un mini-proyecto **Maven standalone** (Java 21, JUnit 5 en scope test, **sin Spring**, sin
dependencias de `taskflow-api`). Genera el "reporte semanal de productividad" en texto plano a partir
de una lista de tareas terminadas.

## Qué hace

`ReporteLegacy.generar(List<RegistroTarea> registros, LocalDate inicioSemana)` recibe registros de
tareas terminadas y el **lunes** de una semana, y devuelve un `String` con:

- un encabezado con la fecha de la semana,
- por **proyecto** → por **usuario**: total de tareas, minutos estimados/reales, `eficiencia: Z%`,
  el desglose por prioridad `[ALTA] n [MEDIA] n [BAJA] n`, y un asterisco `*` junto al usuario cuando
  su ritmo estuvo "en meta",
- una línea final `TOTAL TAREAS: n | EFICIENCIA GLOBAL: m%`.

### Las clases

| Clase | Qué es |
|---|---|
| `ReporteLegacy` | El motor del reporte. Un método `generar(...)` largo, con loops anidados, arrays paralelos a mano, variables `a`/`b`/`temp`/`acum`, números mágicos y cero streams. Helpers `calc`, `f`, `ordenar` |
| `RegistroTarea` | POJO clásico con getters/setters: `idTarea`, `proyecto`, `usuario`, `prioridad` (1..3), `minutosEstimados`, `minutosReales`, `fechaTerminada` |
| `DatosDemo` | Fábrica estática de registros hardcodeados para probar el reporte |
| `MainLegacy` | Imprime `generar(DatosDemo.registros(), ...)` — el módulo corre tal cual |

## Cómo correrlo — dos vías

### Vía A — standalone (para explorarlo hoy en AM-2)

Desde esta carpeta (`legacy-module/`):

```bash
mvn -q compile exec:java          # imprime el reporte con los datos demo
mvn -q test                        # (una vez que le agregues tus tests de caracterización)
```

O sin Maven, para verlo correr al desnudo:

```bash
javac -d target/classes $(find src/main/java -name '*.java')
java -cp target/classes com.taskflow.legacy.MainLegacy
```

### Vía B — copiado a tu `taskflow-api` (el entregable del día)

El entregable vive en tu repo real. En tu `taskflow-api`, en una **rama nueva**:

```bash
cd taskflow-api
git checkout -b legacy-refactor
mkdir -p src/main/java/com/taskflow/legacy
cp -r <ruta-al-lab>/legacy-module/src/main/java/com/taskflow/legacy/* \
      src/main/java/com/taskflow/legacy/
mvn -q compile
git add src/main/java/com/taskflow/legacy
git commit -m "chore: modulo legacy recibido tal cual"
```

Copiar solo el **paquete** `com.taskflow.legacy` (no el `pom.xml` del módulo) hace que el contraste
sea literal —"el resto del proyecto usa streams y este módulo no"—, que tus tests corran con el
`surefire` que ya tienes, y que el entregable quede en tu portafolio real. La guía del día
([`../ejercicio-pm-refactor.md`](../ejercicio-pm-refactor.md)) parte de aquí.

## Una nota honesta

Este módulo está **feo a propósito**: los nombres crípticos (`a`, `b`, `temp`, `calc`, `f`), los
números mágicos y la ausencia de streams son el artefacto. No los "arregles" antes de tiempo. El día
tiene un orden — **caracterizar → refactorizar → arreglar** — y ese orden es el punto. Lee primero, a
ojo y sin Copilot; luego usa el chat para acelerar; y valida SIEMPRE contra el código, no contra lo
que suena plausible.
