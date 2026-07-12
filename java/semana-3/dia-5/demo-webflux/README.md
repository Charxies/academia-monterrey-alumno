# demo-webflux · SOLO instructor · Spring WebFlux awareness (S3D5, AM-1)

> El gemelo **MVC bloqueante** vs **WebFlux reactivo**, con carga, para ver EN NÚMEROS lo que la charla
> explica. Los alumnos **leen, predicen y preguntan**; no construyen nada (right-sizing del día). Dos
> proyectos Maven independientes + un script de carga.

## Puertos (no chocan con el 8080 de las apis de los alumnos)

| Proyecto | Framework | Puerto | Endpoint |
|---|---|---|---|
| `mvc-bloqueante/` | Spring MVC (Tomcat), pool capado a **10** | **8091** | `GET /tareas` (2 s bloqueantes) |
| `flux-reactivo/` | Spring WebFlux (Netty) | **8092** | `GET /tareas` (2 s con `Mono.delay`), `GET /tareas/stream` (SSE) |

## Preparación (una vez)

```bash
cd mvc-bloqueante && /opt/homebrew/bin/mvn -q compile && cd ..
cd flux-reactivo  && /opt/homebrew/bin/mvn -q compile && cd ..
chmod +x carga.sh
```

Arrancar cada uno en su terminal:

```bash
# terminal 1
cd mvc-bloqueante && mvn spring-boot:run
# terminal 2
cd flux-reactivo  && mvn spring-boot:run
```

---

## Demo D1 — "Verlo en números" (MVC bloqueante, 8091) · ~15 min

1. Un request solo: `curl -s -o /dev/null -w "%{time_total}s\n" http://localhost:8091/tareas` → ~2 s.
2. **Pregunta a la clase:** *si lanzo 50 requests a la vez y el pool tiene 10 threads, ¿cuánto tarda la última?*
   (Respuesta esperada: **~10 s** — tandas de 10, cada tanda 2 s.)
3. Correr y comparar:
   ```bash
   ./carga.sh http://localhost:8091/tareas
   ```
4. Mirar los threads `http-nio-*` ocupados: cada uno DUERME 2 s. **El thread no trabaja: ESPERA.** Ese
   es el desperdicio que motiva todo lo demás.

## Demo D2 — "El gemelo reactivo, lado a lado" (Flux, 8092) · ~20 min

1. Leer juntos `flux-reactivo/.../TareaController.java`: el `GET /tareas` devuelve `Flux<Tarea>`
   encadenando `Mono.delay(...).thenMany(Flux.fromIterable(...))` — estilo funcional, contra el
   imperativo del MVC que "se lee de arriba a abajo".
2. Misma carga de 50:
   ```bash
   ./carga.sh http://localhost:8092/tareas
   ```
   **Todas terminan ~2 s** con un puñado de threads del event loop (no ~10 s).
3. **Bonus streaming:** abrir en el navegador `http://localhost:8092/tareas/stream` — una tarea por
   segundo (SSE): datos que **llegan**, lo que MVC no hace natural. Preguntas de comprensión, no de sintaxis.

## Demo D3 — "El contexto 2026: virtual threads" (Loom, Java 21) · ~10 min

1. En `mvc-bloqueante/src/main/resources/application.yml`, **descomentar**:
   ```yaml
   spring:
     threads:
       virtual:
         enabled: true
   ```
2. Reiniciar `mvc-bloqueante` y repetir la MISMA carga:
   ```bash
   ./carga.sh http://localhost:8091/tareas
   ```
   **Todas ~2 s** — **con el código imperativo INTACTO** (no se tocó una línea del controller).
3. **Qué significa:** threads baratos del JVM (ya no ~1 thread del SO por request); el caso "muchos
   requests que solo esperan I/O" se resuelve **sin cambiar de paradigma**.
   **Qué NO resuelve:** backpressure, streaming, composición de flujos — eso sigue siendo territorio
   reactivo (el gemelo Flux).
4. **Volver a comentar el flag** (revertir): el día de la demo no se estrenan configs (dolor 11).

## Números esperados (resumen para la pizarra)

| Escenario | 50 requests concurrentes | Cómo |
|---|---|---|
| MVC bloqueante (pool 10) | **~10 s** | tandas de 10 × 2 s |
| MVC + virtual threads | **~2 s** | 1 línea de config, código imperativo intacto |
| Flux reactivo | **~2 s** | event loop, un puñado de threads |

## Troubleshooting

- **`carga.sh` reporta ~0 s o números raros:** en macOS `date +%s` es por segundos (resolución
  gruesa); el orden de magnitud (2 vs 10) es lo que importa, no el decimal. Si tienes `hey` instalado
  (`brew install hey`), el script lo usa solo y da percentiles.
- **Puerto ocupado:** `lsof -i :8091` / `lsof -i :8092` y mata el zombi. Un solo proceso por puerto.
- **El flag de virtual threads "no hace nada":** confirma la indentación YAML (2 espacios, sin tabs) y
  que reiniciaste la app (los cambios de `application.yml` no son en caliente).
- **El stream no se ve en el navegador:** algunos navegadores buferean; probar
  `curl -N http://localhost:8092/tareas/stream` (la `-N` desactiva el buffer y verás las líneas llegar).

> **Cierre (posicionamiento para entrevistas):** "Conozco el modelo y sus trade-offs: WebFlux usa un
> event loop y tipos `Mono`/`Flux` para no bloquear threads en I/O; brilla en gateways, streaming y
> altísima concurrencia. Mi proyecto es un CRUD con JPA, ahí reactivo no pagaba su complejidad — y con
> virtual threads de Java 21 el caso 'muchos requests esperando I/O' se resuelve sin cambiar el modelo.
> Lo he visto correr en comparativas de carga; no lo he llevado a producción."
