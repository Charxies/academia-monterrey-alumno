# Demo D1 — El secret arrastrado al contexto (guion paso a paso)

> **La demo más citable de la semana.** El objetivo: que VEAN cómo el chat arrastra el archivo abierto
> a su contexto, y que un secret en ese archivo viaja con la pregunta. Ni pánico ni sermón: se muestra
> el mecanismo y se saca la regla de equipo.
>
> **Regla de seguridad de la propia demo (punto de dolor #10):** el token es OBVIAMENTE falso (sufijo
> `FAKE`), vive **fuera del repo** (en un scratch), y se **borra en vivo** como parte de la demo. Antes
> de compartir pantalla, revisa TU `application.yml` real (dogfooding de MP-1): que no haya un secret de
> verdad a la vista.

---

## 0. Preparación (antes de compartir pantalla)

- [ ] Crea un archivo **fuera del repo** (p. ej. `~/scratch/application-demo.yml`) — que NO esté en
      ningún working tree con git, para que sea imposible commitearlo por accidente.
- [ ] Ten a mano el token falso (abajo). Comprueba que tu editor tenga el chat de Copilot listo.
- [ ] Cierra pestañas con secrets reales. Silencia notificaciones.
- [ ] ⚠ **VERIFICAR-PREVIO:** cómo se ve HOY el **panel de contexto/referencias del chat** (qué archivos
      adjunta automáticamente, el chip del "archivo actual", cómo se adjunta uno a mano). El nombre y la
      ubicación cambian por versión — re-valida la semana previa y ajusta el guion a lo que exista.

## 1. El token falso (cópialo TAL CUAL — es inofensivo)

```yaml
# ~/scratch/application-demo.yml   (FUERA del repo — archivo de demo, se borra al final)
spring:
  datasource:
    url: jdbc:postgresql://db.internal:5432/taskflow
    username: taskflow_app
    password: S3cr3t-DoNotShip
aws:
  access-key-id: AKIAIOSFODNN7EXAMPLEFAKE
  secret-access-key: wJalrXUtnFEMI-K7MDENG-bPxRfiCYEXAMPLEKEYFAKE
jwt:
  secret: hs512-demo-signing-key-0000000000000000000000-FAKE
```

> Di en voz alta al pegarlo: *"Este token termina en `FAKE`, no sirve para nada, y este archivo vive
> fuera de cualquier repo. Lo recalco porque el mensaje del día es literal: **un secret real, una vez
> visto en pantalla o pusheado, se ROTA, no se borra** — el historial de Git no perdona, y una captura
> de pantalla tampoco."*

## 2. La demo (2 minutos, en cámara)

1. **Abre** `application-demo.yml` en el editor. Déjalo como el archivo activo.
2. **Abre el chat de Copilot** y haz una pregunta cualquiera, NO relacionada con secrets:
   > *"¿Cómo configuro el pool de conexiones de HikariCP en una app Spring Boot?"*
3. **Señala el panel de contexto/referencias** (⚠ VERIFICAR-PREVIO): el chat incluye el **archivo
   actual** entre sus referencias. Lo que hiciste fue preguntar por Hikari; lo que MANDASTE fue tu
   archivo con credenciales.
   > Guion: *"Yo no pegué el password en el prompt. No hizo falta: el chat toma el archivo abierto como
   > contexto. El secret salió de mi máquina con una pregunta que ni hablaba de secrets."*
4. **Zona de riesgo nombrada:** estos son exactamente los archivos que ELLOS tienen en `taskflow-api`:
   `.env`, `application.yml`, `application-*.yml` (dev/prod), cualquier `*.properties` con credenciales.
5. **Adjuntar a mano** (si la versión lo permite, ⚠ VERIFICAR-PREVIO): muestra que adjuntar un archivo
   al chat es aún más explícito — y que adjuntar el yml equivocado es un clic.

## 3. Content exclusions (solo si el plan las trae — Business) — ⚠ VERIFICAR-PREVIO

Si el asiento es **Business/Enterprise** y hay **content exclusions** configuradas:
- Muestra **qué excluyen** (rutas/globs de archivos que Copilot no debe usar como contexto ni sugerir).
- Y **qué NO garantizan:** son a nivel de organización/repo, dependen de configuración correcta, y no
  sustituyen el hábito de no abrir el archivo. Enséñalas como **red adicional, no como permiso** para
  relajar la regla.
- Si el plan es **Pro/personal** (sin exclusions): dilo explícito — "esta red no existe en su plan; el
  hábito ES la red".

## 4. Limpieza en vivo (parte de la demo, no un after)

```bash
rm ~/scratch/application-demo.yml        # borra el archivo de demo EN CÁMARA
```
> *"Y lo borro ahora, con ustedes viendo. Si esto hubiera sido un secret real, borrarlo no bastaría:
> ya salió a un servicio externo con mi pregunta. La única acción correcta sería **rotarlo**."*

## 5. El entregable que sale de aquí (MP-1) — regla de equipo en 3 líneas

Cada pareja escribe, sobre SU `taskflow-api`:
1. **Qué archivos jamás se abren/adjuntan con el chat activo** (su `.env`, `application*.yml` con
   credenciales…).
2. **Qué va a variables de entorno / secrets del CI** (ya lo hacen desde S3 — nómbrenlo).
3. **Qué se excluye** si hay content exclusions (o "N/A, plan Pro" si no aplica).

## 6. Errores a evitar (checklist del presentador)

- [ ] El token termina en `FAKE` y el archivo está fuera del repo. (No uses un secret real "por realismo".)
- [ ] Revisaste tu `application.yml` real antes de compartir pantalla.
- [ ] No pusheaste nada del scratch. `git status` en tu repo de demo sigue limpio.
- [ ] Cerraste con la frase: **"un secret pusheado se rota, no se borra"**.
