# Guía de setup — Copilot en 4 frentes (AM-2)

> Objetivo del bloque: **4/4 verde por alumno**. Cada check tiene un criterio *verificable* — algo
> que debes **VER**, no un "creo que sí". Si algo queda rojo, va al canal de atascos y al buffer.
>
> **⚠ VERIFICAR-PREVIO:** Copilot cambia UI, nombres de plugin, atajos y límites cada mes. Cada
> punto marcado con ⚠ pudo moverse desde que se escribió esto: fíate del criterio (*qué debes ver*),
> no de la ruta exacta de menú. El instructor re-validó las rutas la semana previa.

## Antes de nada: ¿qué cuenta estás usando?

El dolor #1 y #7 de este bloque son la **cuenta equivocada**. Muchos tienen dos: la personal y la
del curso/cliente. El asiento de Copilot vive en UNA de ellas. Ten claro **cuál** antes de empezar,
y úsala en los 4 frentes.

---

## Check 1 — Licencia activa

**Qué hacer:**
1. Entra a **`github.com/settings/copilot`** (⚠ ruta) con la cuenta del curso.
2. Busca tu plan: **Copilot Pro** o **Copilot Business** (el asiento del cliente).
3. Si es vía organización y ves una **invitación pendiente**: acéptala. Si el cliente usa **SSO**,
   autorízalo (botón "Authorize" / "Configure SSO").

**✅ Verde cuando VES:** la página dice que tienes Copilot activo (Pro/Business), sin banner de
"no access" ni invitación sin aceptar.

**Fallback documentado — Copilot Free.** Si NO hay asiento:
- Copilot Free existe y **alcanza para practicar HOY, no para la semana**. Límites mensuales
  aproximados: **~2,000 completions / ~50 mensajes de chat** (⚠ cifras — verifícalas en la página
  de tu plan; GitHub las ajusta).
- **Si la cohorte cae en Free:** se trabaja **en parejas con un asiento por pareja**, rotando driver,
  y se priorizan los ejercicios marcados. Racionar es criterio, no castigo. **Dilo el lunes**, no lo
  descubras a media semana con el cupo agotado (dolor #8).

**Troubleshooting — "no access to Copilot" con asiento (dolor #1):**
- Invitación de la org sin aceptar → acéptala.
- SSO sin autorizar → autorízalo y **re-loguéate** el plugin (logout/login).
- Sigue fallando → escala al contacto del cliente con la lista de asientos. Mientras tanto: Free o
  pairing.

---

## Check 2 — IntelliJ (el entorno PRINCIPAL del día)

IntelliJ es donde trabajas hoy (continuidad con 4 semanas). VS Code se instala pero se usa en D4.

**Qué hacer:**
1. **Settings → Plugins → Marketplace** → busca **"GitHub Copilot"** (⚠ nombre exacto y versión
   mínima de IDE) → Install → reinicia el IDE.
2. Login: el plugin muestra un **device-code**; ábrelo en el navegador, pégalo, autoriza.
3. Abre **TU `taskflow-api`**. Mira el **icono de estado de Copilot** en la status bar (abajo).
4. **Smoke test (solo VER, la mecánica es de PM-1):** en un service, teclea un comentario de
   intención y espera el **ghost text** (texto gris de sugerencia). Por ejemplo, en `TaskService`:
   ```java
   // devuelve las tareas del proyecto ordenadas por dueDate ascendente
   ```
   Debe aparecer una sugerencia gris debajo. **No la aceptes** — solo confirma que aparece.

**✅ Verde cuando VES:** el icono de status bar en estado OK **y** al menos un ghost text gris sobre
tu `taskflow-api`.

**Verifica la cuenta activa (dolor #7):** en el panel/status del plugin, confirma que la cuenta
logueada es **la que tiene el asiento**. Si no: logout → login con la correcta.

**Troubleshooting — el ghost text NO aparece pese a login OK (dolor #3):** diagnostica en orden:
1. **Proxy/cert corporativo** → configura el HTTP proxy del IDE + el certificado (los dominios de
   Copilot los pediste a IT la semana previa ⚠).
2. **Completions deshabilitadas** globalmente o para el lenguaje, en los settings del plugin (⚠ ruta).
3. **Icono en error** → abre el panel de estado/logs del plugin y **lee el mensaje** (la misma
   habilidad de S1D1: leer el error, no adivinar).
4. **Plugin vs IDE incompatibles (dolor #2):** el plugin vigente exige una versión mínima de IntelliJ
   (⚠). Si el IDE está viejo: actualízalo. En máquina administrada con update bloqueado: instala la
   versión de plugin **compatible** desde el Marketplace y anota la limitación.

---

## Check 3 — VS Code (el SEGUNDO entorno)

Por qué dos entornos, dicho honesto: las capacidades más nuevas (**Copilot Edits**, **agent mode**)
maduran primero en VS Code — se usan en **D4**. Hoy solo queda **instalado y autenticado**; el
trabajo del día sigue en IntelliJ.

**Qué hacer:**
1. Instala **VS Code** (si no lo tienes).
2. Extensiones: **GitHub Copilot** y **GitHub Copilot Chat** (⚠ nombres/empaquetado — puede venir
   como una sola extensión con chat incluido).
3. Login con la cuenta del asiento.
4. Abre el **MISMO `taskflow-api`**, smoke test de ghost text igual que en IntelliJ.

**✅ Verde cuando VES:** ghost text gris sobre tu api también en VS Code, con la cuenta correcta.

---

## Check 4 — Terminal (CLI)

Agnóstico al IDE. Se usa a fondo en D4; hoy solo queda autenticado.

**Qué hacer:**
1. `gh auth status` → ¿estás autenticado y con qué cuenta?
2. Si no tienes `gh` o no estás logueado: `gh auth login` (3 min; elige GitHub.com → HTTPS → login
   por navegador).
3. Instala Copilot para terminal (⚠ **VERIFICAR-PREVIO**: la forma vigente puede ser la extensión
   `gh copilot` **o** un CLI standalone — el empaquetado ha cambiado; el instructor fijó el comando
   exacto en el checklist previo, dolor #9).
4. **Smoke test agnóstico** — pídele que explique un comando que ya conoces, p. ej. el `docker
   compose` de tu S3D2:
   ```bash
   gh copilot explain "docker compose up -d --build"    # ⚠ sintaxis según empaquetado vigente
   ```

**✅ Verde cuando VES:** `gh auth status` confirma la cuenta correcta **y** el comando de explicación
devuelve una explicación coherente.

---

## Check 5 (de higiene) — El toggle

No es un 5º frente; es una habilidad que usas **esta misma tarde** en la kata.

**Qué hacer:** aprende a **desactivar Copilot temporalmente** (icono de status bar / atajo ⚠) y
**por lenguaje** (p. ej. Markdown si estorba).

**Regla explícita:** apagarlo **no es hacer trampa, es criterio**. En la fase de "tests a mano" de la
kata, Copilot va **APAGADO** a propósito: el contrato lo escribes tú. Y si en cualquier momento las
sugerencias estorban en código que quieres escribir solo — apágalo, no pelees con ellas tecla a tecla
(dolor #4).

---

## Checklist final del bloque (parte del DoD)

- [ ] **Check 1** — licencia Pro/Business activa (o Free documentado y funcionando en pareja)
- [ ] **Check 2** — IntelliJ: plugin OK, ghost text visible sobre `taskflow-api`, cuenta correcta
- [ ] **Check 3** — VS Code: extensiones instaladas, autenticado, ghost text visible
- [ ] **Check 4** — terminal: `gh auth status` OK, `explain` responde
- [ ] **Check 5** — sé apagar/encender Copilot (lo usaré en la kata)

Quien tenga un rojo pasa al buffer con el instructor.
