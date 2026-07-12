# practice-pages — espejos MAESTROS de los sitios de práctica (QE S4, D1–D3)

**Esta carpeta es la fuente ÚNICA de verdad.** Los labs de cada día **copian** desde aquí
las páginas que usan a `src/test/resources/practice-pages/` de su proyecto
(`taskflow-qa-<usuario>`), para que el repo del alumno sea autosuficiente y el fallback
viaje con él (patrón fijado por la spec de D1 y repetido por D2 y D3). **Nunca edites la
copia de un lab: edita el maestro aquí y re-copia.** Cualquier cambio en una página obliga
a revisar los checklists de las specs (los datos de las páginas y los asserts de los
ejercicios están acoplados a propósito).

Sirven como fallback/espejo de `the-internet.herokuapp.com` y `saucedemo.com` cuando el
sitio externo está caído o la red corporativa lo bloquea. Se acceden vía `file://` con el
helper único de la semana `utils/Paginas` (flag `-Dpaginas.local=true`), o servidas con
`python3 -m http.server` para inspección manual.

## Reglas duras (de las specs de D1–D3)

- **HTML + CSS + JS vanilla, TODO inline. Cero orígenes externos** (sin CDN, fetch,
  módulos, fuentes ni imágenes remotas): deben funcionar vía `file://` y sin red.
- **Mismos ids / clases / atributos / textos** que el sitio externo que espejan: el mismo
  test corre contra ambos modos **sin tocar un locator**.
- Las páginas de D1–D2 son **síncronas a propósito** (los locators se aprenden sin el
  ruido de la sincronización). Las de D3 tienen **retrasos reales de 0.5–3 s** vía
  `setTimeout` (spinners, render diferido) para que los explicit waits tengan algo que
  esperar; `alertas.html` usa `alert()/confirm()/prompt()` **nativos** a propósito.
- **Un solo dueño por archivo** (la spec del día dueño define su contrato; las otras lo
  referencian sin re-producirlo). Cada página lleva en su cabecera un comentario con su
  dueño y el contrato exacto que los tests asertan.

## Inventario y dueños

| Archivo | Dueño | Espejo de | Lo usan |
|---|---|---|---|
| `index.html` | **D1** | the-internet home | D1 MP-6/MP-7, Integrador D1 tests 1–2 |
| `login.html` | **D1** | `/login` | D1 Int. test 3 · D2 MP-2/3/4 e Int. obj 1–4 · D3 MP-1 |
| `checkboxes.html` | **D2** | `/checkboxes` (+ radios, solo local) | D2 MP-4 e Int. obj 5–6 · D3 MP-3 |
| `tabla.html` | **D2** | `/tables` (table1) | D2 MP-5/7/8 e Int. obj 7–11 |
| `dinamicos.html` | **D2** | `/add_remove_elements/` | D2 MP-5/7 e Int. obj 12–14 |
| `perfil-v1.html` / `perfil-v2.html` | **D2** | *(solo local)* | D2 MP-6/MP-9 e Int. obj 15 |
| `formulario.html` | **D3** | `/inputs` (ampliado: campo precargado) | D3 MP-1 |
| `toast.html` | **D3** | *(solo local — página PRIMARIA de MP-2)* | D3 MP-2 y cierre en MP-8 |
| `dropdowns.html` | **D3** | `/dropdown` (+ dropdown custom, solo local) | D3 MP-3 |
| `alertas.html` | **D3** | `/javascript_alerts` | D3 MP-4 e Int. test 5 |
| `frames.html` | **D3** | `/iframe` | D3 MP-5 e Int. test 6 |
| `frames-anidados.html` | **D3** | `/nested_frames` | D3 MP-5 |
| `ventanas.html` + `nueva-pestana.html` | **D3** | `/windows` y `/windows/new` | D3 MP-6 |
| `carga-dinamica.html` (`?modo=oculto\|inexistente`) | **D3** | `/dynamic_loading` ej. 1 y 2 | D3 MP-7/MP-8 |
| `controles-dinamicos.html` | **D3** | `/dynamic_controls` | D3 MP-8 |
| `tienda/` (6 páginas) | **D3** | saucedemo.com | D3 MP-9 e Integrador tests 1–4 |

## Qué copia cada lab

- **D1** → `index.html`, `login.html`
- **D2** → `login.html`, `checkboxes.html`, `tabla.html`, `dinamicos.html`,
  `perfil-v1.html`, `perfil-v2.html`
- **D3** → `login.html`, `formulario.html`, `toast.html`, `dropdowns.html`,
  `checkboxes.html`, `alertas.html`, `frames.html`, `frames-anidados.html`,
  `ventanas.html`, `nueva-pestana.html`, `carga-dinamica.html`,
  `controles-dinamicos.html` y `tienda/` completa

## Notas de implementación

- `tienda/`: credenciales de saucedemo (`standard_user`, `locked_out_user`,
  `performance_glitch_user` + `secret_sauce`), mensajes de error EXACTOS, carrito en
  `sessionStorage`, impuesto 8% redondeado al centavo; `performance_glitch_user`
  retrasa cada página 4 s (los demás 0.8 s) con spinner — el mismo test solo tarda más.
- `frames.html` y `frames-anidados.html` usan `srcdoc` inline (sin archivos de frame
  separados ni red).
- `tabla.html`: la celda `Frank` trae espacios alrededor **a propósito** (demo
  `normalize-space()` de D2 MP-7).
- `perfil-v1` → `perfil-v2`: clases `css-*` regeneradas (botón `css-a1b2c3` → `css-x9y8z7`)
  y un `div` contenedor extra; los `data-testid` sobreviven. Es la demo medible de
  D2 MP-6 y MP-9.
- `carga-dinamica.html` acepta query string también en `file://`
  (`file:///...carga-dinamica.html?modo=inexistente`).
