# Feature del capstone — C) Notificaciones por email

> **La más arquitectónica del menú — para parejas fuertes.** Spec CERRADA: el alcance de abajo es el
> único. Los **recortes** se copian tal cual a tu `docs/feature-spec.md` en la Fase 1. Cualquier
> divergencia se resuelve a favor del apéndice de la spec del día.
>
> **El recorte ES el diseño.** No hay proveedor real, no hay JavaMail contra un servidor externo. El
> alcance mínimo NO requiere **ninguna** dependencia nueva — se resuelve con una interfaz + una
> implementación basada en log. Esta es la feature donde Copilot **va a intentar** meterte una
> dependencia de correo alucinada (`spring-boot-starter-email`, ver MP-2 y `demos/fallback-alucinacion.md`):
> el ejercicio es **rechazarla** porque tu alcance no la necesita.

---

## 1. Qué y por qué

Notificar por "email" a las personas correctas cuando pasan dos cosas en una tarea, de forma
**verificable por API** (sin buzón real). Valor: demostrar un flujo de efectos secundarios
desacoplado, robusto ante fallos y auditable — la pieza más de "diseño" del capstone.

## 2. Diseño (interfaz + fake log-based)

```
interface EmailSender {
    void send(String recipientEmail, String subject, String body);
}
```

**Implementación única — `LogEmailSender implements EmailSender`:** un fake SMTP basado en log que
(1) escribe la notificación al log de la app **y** (2) la **persiste** en la tabla `notification_log`.

### Modelo — `NotificationLog`

| Campo | Tipo | Nota |
|---|---|---|
| `id` | `Long` | generado por la BD |
| `recipientEmail` | `String` | a quién iba dirigida |
| `subject` | `String` | asunto |
| `body` | `String` | cuerpo |
| `createdAt` | `Instant` | asignado por el servidor |

> **NO proveedor real. NO JavaMail. NO `spring-boot-starter-mail`.** El log + la tabla SON el "buzón".
> Cualquier dependencia de correo que sugiera el chat se **rechaza** en esta feature: el alcance mínimo
> se cierra con `slf4j` (que ya tienes) + una entidad JPA (que ya sabes hacer).

## 3. Disparadores (exactamente 2)

1. **Al asignar/reasignar una tarea** (cambia `assigneeId`) → notificación al **nuevo assignee**:
   asunto/cuerpo "Se te asignó la tarea *{title}*".
2. **Al pasar una tarea a `DONE`** → notificación al **`owner` del proyecto**: "La tarea *{title}* se
   completó".

Estos disparos se enganchan en el service de tareas de S2 (`TaskService.cambiarStatus` /
`actualizar`), donde ya vive la lógica de cambio de estado y de assignee — **no** en el controller.

## 4. Endpoint de verificación

| Método y ruta | Auth | Respuestas |
|---|---|---|
| `GET /notifications` | **solo `ADMIN`** | **200** lista de `notification_log`, **más reciente primero**; **403** si no eres ADMIN |

Este endpoint es lo que hace la feature **verificable por API** (y por `taskflow-qa`) sin buzón real.

**DTO:** `NotificationResponse(Long id, String recipientEmail, String subject, String body, Instant createdAt)`.

## 5. Reglas de negocio (y de dónde salen)

- **No auto-notificar:** si el actor (quien hace el cambio) **es** el destinatario, no se emite
  notificación. Ej.: si te autoasignas una tarea, no te llega correo a ti mismo.
- **El "envío" NUNCA rompe la operación principal:** el guardado/actualización de la tarea es la
  transacción; el "envío" es un efecto secundario. Si `LogEmailSender` fallara, **la tarea se guarda
  igual** y el error se loguea. (Con el fake log-based esto es casi imposible que falle, pero el diseño
  debe tolerarlo — es el punto arquitectónico de la feature: envolver el efecto para que no propague.)
- **`GET /notifications` es solo ADMIN** → autorización con el rol de S2; si no, **403**.

## 6. Recortes predefinidos — FUERA DE ALCANCE (cópialos a tu espec, no los quites)

- ❌ Fake SMTP **log-based**, no proveedor real (SendGrid/SES/SMTP externo) — **sin dependencias nuevas**.
- ❌ Sin plantillas HTML (el cuerpo es texto plano).
- ❌ Sin async / colas / `@Async` / reintentos.
- ❌ Sin preferencias de usuario (todos "reciben" siempre que aplique el disparador).

> **Timer de recorte (min 30 de PM-1):** si a esa hora no tienes **un** disparador emitiendo y el
> `GET /notifications` listando, el recorte impuesto es: **quedarte con el disparador de asignación**
> (el más simple) y dejar el de `DONE` como stretch. El DoD se cumple con 1 disparador + el endpoint.
>
> **Vigilancia especial:** si en F3 aparece en tu `pom.xml` una dependencia de correo, es la alucinación
> (E2) en su hábitat natural. Verifícala en `search.maven.org` **antes** de pegarla — y aquí ni siquiera
> hace falta verificar: **tu alcance mínimo no lleva dependencias nuevas**, así que se rechaza de entrada.

## 7. Criterios de aceptación (dado / cuando / entonces — cada uno es un test)

- **Dado** una tarea con `assigneeId = A`, **cuando** se reasigna a `B` (B ≠ actor), **entonces** se
  crea un `NotificationLog` con `recipientEmail` = email de B y asunto de asignación.
- **Dado** que el actor se autoasigna la tarea, **cuando** cambia el assignee a sí mismo, **entonces**
  **no** se crea notificación (regla de no auto-notificar).
- **Dado** una tarea `IN_PROGRESS` con assignee, **cuando** pasa a `DONE`, **entonces** se crea un
  `NotificationLog` dirigido al **owner del proyecto**.
- **Dado** que `EmailSender.send` lanza excepción (mock que falla), **cuando** se guarda la tarea,
  **entonces** la tarea se persiste igual (la operación principal no se rompe).
- **Dado** un usuario no-ADMIN, **cuando** `GET /notifications`, **entonces** **403**.

## 8. Test canónico en `taskflow-qa` (RestAssured, ≥1 verde en local)

**Roundtrip:** asignar una tarea vía API → `GET /notifications` (como admin) contiene el registro con
el destinatario correcto.

```
PUT/PATCH la tarea para cambiar assigneeId a B   -> 200
GET /notifications  (auth admin)                 -> 200
then la lista contiene un item con recipientEmail == email(B) y subject de asignación
```

## 9. Presupuesto de tiempo (para que quepa en el bloque)

| Fase | Qué | Min |
|---|---|---:|
| F1 | Espec (copia §2–§7) — clava los 2 disparadores y "no rompe la operación" | 20 |
| F2 | Plan con chat + auditar (¿mete dependencia de correo? recházala) | 20 |
| F3 | Rama, `EmailSender`+`LogEmailSender`+`NotificationLog`, enganche en `TaskService`, commit | 10 + 30 |
| F4 | Tests (disparo, no-auto-notificar, el "no rompe" con mock que falla) + sabotaje | 20 |
| F5 | 1 test API RestAssured | 15 |
| F6 | OpenAPI de `GET /notifications` + README | 10 |
| F7 | PR + doble review + merge | 15 |

Total ≈ **140 min netos** — cabe en AM-2 + PM-1. Es la más ajustada: por eso es para parejas fuertes.

## 10. Reuso obligatorio (no recodifiques lo que ya tienes)

- `slf4j` (ya está en el classpath vía Spring Boot) para el log — **ninguna** dependencia nueva.
- El rol `ADMIN` de S2 para autorizar `GET /notifications`.
- El punto de enganche vive en `TaskService` (S2), donde ya está la lógica de cambio de estado/assignee.
- Inyección por constructor de `EmailSender` en `TaskService`: en tests inyectas un **mock** que
  falla, para probar la regla "no rompe la operación principal" (checklist D3 + sabotaje).
