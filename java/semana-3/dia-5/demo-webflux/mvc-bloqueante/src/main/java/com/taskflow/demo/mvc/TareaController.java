package com.taskflow.demo.mvc;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * TareaController (bloqueante) — GET /tareas con latencia SIMULADA de 2 s.
 *
 * El Thread.sleep(2000) representa lo que en taskflow-api seria la espera a Postgres: el thread de
 * Tomcat NO trabaja, ESPERA — y mientras espera no puede atender a nadie mas. Con el pool capado a 10
 * (application.yml), 50 requests concurrentes se atienden en tandas de 10: la ultima termina ~10 s.
 * Ese es el numero que los alumnos PREDICEN antes de correr carga.sh.
 *
 * Codigo IMPERATIVO, se lee de arriba a abajo. Contrastar con el gemelo reactivo (Flux).
 */
@RestController
public class TareaController {

    /** Dominio de juguete que hace eco del capstone (Task: id, title, status). */
    public record Tarea(Long id, String title, String status) {}

    private static final List<Tarea> TAREAS = List.of(
            new Tarea(1L, "Planificar el sprint", "TODO"),
            new Tarea(2L, "Disenar la API de comentarios", "IN_PROGRESS"),
            new Tarea(3L, "Configurar el pipeline CI", "DONE")
    );

    @GetMapping("/tareas")
    public List<Tarea> getTareas() throws InterruptedException {
        Thread.sleep(2000);   // BLOQUEA el thread http-nio-* durante 2 s (simula la consulta a la BD)
        return TAREAS;
    }
}
