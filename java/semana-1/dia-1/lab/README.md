# TaskFlow CLI v0 — Lab del Día 1 (starter)

Este es el proyecto starter del **Día 1 de Java**. Contiene un archivo por cada
mini-práctica (MP-4 a MP-10) y el esqueleto del **integrador** (`Main.java`).
Todo el proyecto **compila tal cual está**: los huecos son comentarios `TODO`
numerados con pistas — tu trabajo es llenarlos.

> MP-1 a MP-3 (verificar JDK, Hello World, Git/GitHub) no tienen archivo aquí:
> son de setup y están documentadas paso a paso en `alumno.md`.

## Requisitos previos

```bash
java -version    # debe decir 21 (ej. "openjdk 21.x")
mvn -version     # opcional: solo si vas a correr desde terminal con Maven
```

Si `java -version` no dice 21, avisa en el canal de atascos ANTES de continuar.

## Cómo abrir el proyecto

1. IntelliJ IDEA → **Open** → selecciona **esta carpeta `lab/`** (la que contiene `pom.xml`).
2. Espera a que IntelliJ importe el proyecto Maven (barra de progreso abajo a la derecha).
3. Si IntelliJ marca errores de SDK: **File → Project Structure → SDK → 21**.

## Cómo correr cada archivo

**Desde IntelliJ (recomendado):** clic derecho sobre el archivo → `Run 'MP04Variables.main()'`.

**Desde terminal con Maven** (funciona igual en Windows, macOS y Linux):

```bash
# El integrador (Main):
mvn -q compile exec:java

# Una práctica específica:
mvn -q compile exec:java "-Dexec.mainClass=com.taskflow.practicas.MP04Variables"
```

> Las comillas alrededor de `-Dexec.mainClass=...` son obligatorias en PowerShell
> y no estorban en macOS/Linux — úsalas siempre y olvídate del problema.

**Sin Maven** (solo `javac`/`java`):

```bash
# macOS / Linux
javac -d target/classes $(find src -name "*.java")
java -cp target/classes com.taskflow.practicas.MP04Variables
java -cp target/classes com.taskflow.Main
```

```powershell
# Windows (PowerShell)
javac -d target/classes (Get-ChildItem -Recurse src -Filter *.java).FullName
java -cp target/classes com.taskflow.practicas.MP04Variables
java -cp target/classes com.taskflow.Main
```

> Usamos `target/classes` como salida (la misma carpeta que usa Maven): ya está
> cubierta por el `.gitignore` del día, así que los `.class` nunca acaban en tu repo.

## Orden de trabajo del día

| # | Archivo | Tema |
|---|---|---|
| MP-4 | `practicas/MP04Variables.java` | Tipos, variables, `var`, `final`, printf, text block, leer errores del compilador |
| MP-5 | `practicas/MP05Operadores.java` | Operadores, la trampa de la división entera, casting, `%` |
| MP-6 | `practicas/MP06Strings.java` | Métodos de `String` y el clásico `equals` vs `==` |
| MP-7 | `practicas/MP07IfPrioridad.java` | `if / else if / else`, operador ternario |
| MP-8 | `practicas/MP08SwitchEstados.java` | `switch` expression con `->` |
| MP-9 | `practicas/MP09LoopsArrays.java` | Loops, arrays, for-each, for con índice |
| MP-10 | `practicas/MP10Scanner.java` | `Scanner`, el bug del `\n` pendiente, menú con do-while |
| Integrador | `Main.java` | **TaskFlow CLI v0** — amarra todo lo anterior |

## Convenciones del starter

- Los `TODO` están **numerados**: hazlos en orden, cada uno trae una pista.
- Los **"errores a propósito"** (MP-4) están comentados: descoméntalos SOLO para
  compilar, **leer el mensaje del compilador** y entenderlo; luego vuélvelos a comentar.
- Cada archivo corre por separado (todos tienen su propio `main`).

## El integrador: TaskFlow CLI v0

Menú de consola (`Scanner` + `do-while` + `switch`) con tareas hardcodeadas:

- [ ] **1) Ver tareas** — tabla alineada con `printf` (`%-30s %-15s %-10s`),
      encabezado con text block, estado como etiqueta legible vía **switch expression**.
- [ ] **2) Ver resumen** — `TODO: n | IN_PROGRESS: n | DONE: n`.
- [ ] **3) Salir** — mensaje de despedida.
- [ ] **Arrays paralelos** con mínimo **5 tareas** del dominio TaskFlow.
- [ ] Constantes `final String` para los 3 estados (nada de literales regados).
- [ ] Opción inválida → mensaje claro, **NO crashea**, vuelve al menú.

**Entregable:** commit y push a tu repo `taskflow-<usuario>`:

```bash
git add .
git commit -m "feat: taskflow cli v0 - menu y listado de tareas"
git push
```

## Definition of Done del día

(a) compila y corre; (b) el menú repite hasta salir y la opción inválida no crashea;
(c) está commiteado y pusheado; (d) puedes explicar tu código en 1–2 minutos.

## Stretch goals (solo si terminaste todo)

1. Opción **4) Buscar por título**: `contains` sin distinguir mayúsculas/minúsculas.
2. Resaltar las tareas **HIGH** en rojo con códigos ANSI (`\u001B[31m` ... `\u001B[0m`).

> ¿Se siente feo mantener 3 arrays sincronizados a mano? Perfecto: **mañana ese
> dolor se convierte en la clase `Task`** — eso es POO.
