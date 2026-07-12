package com.taskflow.controller;

// TODO (MP-2): tras agregar spring-boot-starter-web al pom, descomenta estos imports.
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.PathVariable;
// import org.springframework.web.bind.annotation.RestController;

/**
 * PingController — andamiaje DESECHABLE de MP-3 para estrenar la capa web. Se BORRA en el integrador
 * (está anunciado: era solo para ver un @GetMapping funcionando y provocar el error intencional #2).
 *
 * Este archivo COMPILA hoy porque todo lo que depende de spring-web vive en comentarios; lo activas
 * después de MP-2 descomentando imports, la anotación @RestController y los métodos.
 */
// @RestController
public class PingController {

    // TODO (MP-3): GET /ping -> "pong"
    // @GetMapping("/ping")
    // public String ping() {
    //     return "pong";
    // }

    // TODO (MP-3): GET /ping/{nombre} -> "pong, <nombre>".
    // ERROR INTENCIONAL #2: prueba primero SIN @PathVariable -> responde "pong, null" sin error
    // (Spring lo trata como query param opcional). Regla: TODO parámetro de controller se anota
    // explícito y su nombre coincide con el template de la ruta.
    // @GetMapping("/ping/{nombre}")
    // public String pingNombre(@PathVariable("nombre") String nombre) {
    //     return "pong, " + nombre;
    // }

    // TODO (MP-3, cierre): GET /demo/task -> retorna un Task construido inline (constructor de
    // rehidratación) y deja que Jackson lo convierta a JSON "gratis". También se BORRA en el integrador.
}
