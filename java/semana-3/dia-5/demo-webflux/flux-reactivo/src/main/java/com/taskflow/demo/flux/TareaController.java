package com.taskflow.demo.flux;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;

/**
 * TareaController (reactivo) — el MISMO GET /tareas del gemelo bloqueante, pero devolviendo un TIPO
 * REACTIVO. Leer juntos la DIFERENCIA de estilo: aqui se ENCADENAN operadores (delayElement, fromIterable,
 * map) en vez de codigo imperativo linea a linea.
 *
 * La clave (Demo D2): la "espera" de 2 s es Mono.delay / delayElement, NO Thread.sleep. El thread del
 * event loop NO se bloquea durante esos 2 s: atiende otras requests. Por eso 50 en paralelo terminan
 * ~2 s con un puñado de threads, mientras el gemelo bloqueante (pool 10) tardaba ~10 s.
 */
@RestController
public class TareaController {

    /** Mismo dominio de juguete que el gemelo MVC (eco del capstone). */
    public record Tarea(Long id, String title, String status) {}

    private static final List<Tarea> TAREAS = List.of(
            new Tarea(1L, "Planificar el sprint", "TODO"),
            new Tarea(2L, "Disenar la API de comentarios", "IN_PROGRESS"),
            new Tarea(3L, "Configurar el pipeline CI", "DONE")
    );

    /**
     * GET /tareas — Flux<Tarea> "que llegara en 2 s". delayElement retrasa la EMISION sin bloquear el
     * thread. Mismo shape de respuesta que el MVC (un JSON array), distinto motor.
     */
    @GetMapping("/tareas")
    public Flux<Tarea> getTareas() {
        return Mono.delay(Duration.ofSeconds(2))     // "promesa" de que en 2 s hay datos (no bloquea)
                .thenMany(Flux.fromIterable(TAREAS)); // ...y entonces emite las tareas
    }

    /**
     * GET /tareas/stream — BONUS de streaming (Demo D2). Flux.interval emite una tarea por segundo como
     * Server-Sent Events (text/event-stream): datos que LLEGAN al cliente, algo que el MVC clasico no
     * hace natural. Abrir en el navegador y ver las lineas aparecer una a una.
     */
    @GetMapping(value = "/tareas/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Tarea> streamTareas() {
        return Flux.interval(Duration.ofSeconds(1))                    // un "tick" por segundo
                .map(i -> TAREAS.get((int) (i % TAREAS.size())));      // ...que emite una tarea (en ciclo)
    }
}
