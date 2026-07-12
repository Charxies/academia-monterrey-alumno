# Checklist de lectura crítica de tests — canónica del curso

> **Imprímela / pínéala en el canal.** Es LA checklist que citan la rúbrica del integrador y el DoD.
> Se aplica a **cada** test generado ANTES de aceptarlo en tu suite. Cuatro preguntas; si una falla,
> el test es decorativo (pinta verde y protege nada) → se poda o se repara.

---

## Las 4 preguntas

### ① ¿Puede fallar?
¿Qué cambio en `src/main` lo pondría **rojo**? Nómbralo concreto (una mutación, una regla borrada).
**Si no encuentras ninguno, el test es decorativo.** Un test que no puede fallar no protege nada:
está ejecutando código sin verificarlo (justo lo que infla JaCoCo sin proteger).

### ② ¿Qué bug real cazaría?
Dilo en **una frase**: *"si alguien cambia `< 3` por `<= 3`, este test lo caza"*. Si no sabes nombrar
el bug que defiende, el test no defiende nada — aunque tenga asserts.

### ③ ¿El nombre dice el escenario y el cuerpo lo cumple?
`crear_tituloEnLimiteInferior_ok` con `"Comprar leche"` (13 chars) en el cuerpo **reprueba aquí**: el
nombre promete el borde 3, el cuerpo prueba un happy path cualquiera. El nombre es un contrato con
quien lea la suite dentro de seis meses.

### ④ ¿Los datos son DEL escenario o de relleno?
`"a".repeat(121)` es **del escenario** (prueba el límite exacto 121). `"tarea test"` / `"foo"` es
**relleno**: no ejercita ninguna frontera. Los datos con intención son la diferencia entre probar la
regla y pasar por encima de ella.

---

## Cómo se usa (30 segundos por test)

1. Lee el test **completo** (si no cabe en una pantalla, el paso fue demasiado grande).
2. Pregunta ① primero: intenta **imaginar la mutación** que lo pondría rojo. ¿Existe? Sigue. ¿No? Poda.
3. Si pasó ①, confirma ②–④.
4. **Veredicto:** `legítimo` (pasa las 4) · `decorativo` (falla ≥1 — cita cuál) · `reparable` (buena
   idea, datos/nombre a corregir).
5. Todo lo que podes o corriges → **al diario de decisiones** con la razón de 1 línea.

> **El puente con la mutación (T4):** la pregunta ① es una hipótesis; la **mutación manual es el
> veredicto**. Si crees que un test "sí puede fallar", demuéstralo: aplica la mutación que imaginaste
> y míralo rojo. Un test que no puedas anclar a una mutación concreta todavía no está auditado.

## Lo que la checklist NO dice (para no caer en paranoia)

- **Usar mocks no es pecado.** Un test con Mockito bien usado —que asierta un efecto observable real
  (una excepción, un `verify(..., never())`, un estado)— es **legítimo**. El objetivo es criterio, no
  borrar todos los mocks.
- **Un solo assert bien elegido basta.** No se trata de acumular asserts, sino de que **al menos uno**
  falle ante el bug que el test dice proteger.
- **El happy path legítimo existe.** Probar el camino feliz con datos **del** escenario (título de 3
  chars exactos) es válido; lo decorativo es disfrazarlo de borde con datos de relleno.
