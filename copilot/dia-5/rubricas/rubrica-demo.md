# Rúbrica — Demo final (5 min por pareja)

> Evalúa la **demo en vivo** del integrador, no el repo (eso lo cubre `rubrica-capstone.md`). 5 min por
> pareja, **timer visible**. Ambos miembros hablan; el instructor puede pedir a **cualquiera** que
> explique **cualquier** parte (regla d). Total: **50 pts**.
>
> **El componente que pondera más alto es "qué rechazamos y por qué".** Rechazar con criterio es LA
> habilidad de la semana; una demo que solo muestra lo que funcionó, sin un solo rechazo defendido,
> tiene techo bajo aunque la feature sea impecable.

| # | Componente | Qué se busca | Pts |
|---|---|---|---:|
| 1 | **Feature funcionando en vivo** | Un **request real** contra su API (curl/Postman/Swagger) con un **dato visible** en la respuesta — no un screenshot, la cosa corriendo | 12 |
| 2 | **Pipeline verde + QA verde** | El PR mergeado con el **job de tests verde** en pantalla **y** el test de `taskflow-qa` de la feature en verde (local vale). La red de S3–S4 haciendo su trabajo | 10 |
| 3 | **Reporte de aceleración — tiempos** | Tiempos por fase (del diario), **qué generó Copilot vs qué escribieron a mano**. Honesto, con números de SU semana, no titulares | 8 |
| 4 | **Qué rechazamos y por qué** ⭐ | Al menos un rechazo **defendido con criterio**: la sugerencia, por qué la rechazaron, cómo lo verificaron (test, lectura, Central). Aquí se gana la demo | 12 |
| 5 | **Explicación cruzada (regla d)** | El instructor pregunta a **quien no lo tecleó**; responde sin leer, incluida una parte generada por la IA. Rotación real evidente | 8 |

## Anti-señales (bajan la nota, aunque la feature "se vea bien")

- **Demo de solo éxitos:** cero rechazos, cero fricción reportada. Irreal → o no auditaron, o no lo
  cuentan. El día premia la honestidad de "aquí estorbó".
- **"Copilot lo hizo todo":** no saben decir qué escribieron a mano vs qué generaron. Se cae en el §5.
- **Números redondos sospechosos** ("ahorramos 80%"): el reporte pide fases y minutos, no eslóganes.
- **Un solo miembro habla:** el otro asiente. La rotación era obligatoria; se nota en el oral.

## Señales de excelencia (desempate hacia arriba)

- El rechazo estrella es de **seguridad o dependencia** (SQL concatenado que rechazaron, dependencia de
  correo que no pegaron porque su alcance no la necesitaba) — el hilo de AM-1 aterrizado en su capstone.
- Reportan honestamente una fase donde **auditar tardó más que teclear** y explican por qué aun así valió.
- Muestran el **sabotaje** en 20 segundos: rompen la línea clave, el test se pone rojo, en vivo.

## Logística de la demo (para el instructor)

- **Orden:** por sorteo al inicio; la pareja con API caída en el checklist pre-demo pasa al **final de
  la cola** mientras reinicia (plan B: test de `taskflow-qa` verde + request grabado en Postman).
- **Timer:** 5 min por pareja hasta 8 parejas; si la cohorte es mayor, **4 min** — decisión ANTES de
  empezar, anunciada. Corte en seco al timer: la disciplina de 5 min es parte de la evaluación.
- **Nadie demuestra sin `checklist-pre-demo.md` completo** (compose arriba, seed, smoke request,
  pipeline verde, reporte abierto).
