package com.taskflow.demo.mvc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * MvcBloqueanteApplication — el gemelo BLOQUEANTE de la demo AM-1 (Demo D1 / Demo D3).
 *
 * Spring MVC sobre Tomcat: cada request de GET /tareas toma un thread http-nio-* y se queda DORMIDO
 * 2 s esperando (simula la consulta a Postgres). El pool esta capado a 10 (application.yml) para ver
 * el fenomeno a escala humana: 50 requests -> tandas de 10 -> la ultima termina ~10 s.
 *
 * Puerto 8091 (no choca con el 8080 de las apis de los alumnos ni con el 8092 del gemelo reactivo).
 */
@SpringBootApplication
public class MvcBloqueanteApplication {
    public static void main(String[] args) {
        SpringApplication.run(MvcBloqueanteApplication.class, args);
    }
}
