package com.taskflow.demo.flux;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * FluxReactivoApplication — el gemelo REACTIVO de la demo AM-1 (Demo D2).
 *
 * Spring WebFlux sobre Netty: un puñado de threads del EVENT LOOP que nunca se bloquean. El GET /tareas
 * "espera" 2 s con Mono.delay (NO Thread.sleep): mientras ese temporizador corre, el thread atiende
 * otros eventos. Misma carga de 50 -> todas terminan ~2 s (no ~10 s como el gemelo bloqueante).
 *
 * Puerto 8092 (no choca con 8080 de los alumnos ni con 8091 del gemelo MVC).
 */
@SpringBootApplication
public class FluxReactivoApplication {
    public static void main(String[] args) {
        SpringApplication.run(FluxReactivoApplication.class, args);
    }
}
