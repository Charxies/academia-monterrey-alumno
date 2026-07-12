package com.taskflow;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * TaskflowApiApplicationTests — el test de humo generado por Initializr (se conserva).
 *
 * @SpringBootTest levanta el CONTEXTO completo; contextLoads() no tiene asserts porque su prueba ES
 * que el contexto arranque sin excepciones: si un bean no se puede cablear (falta un @Repository,
 * una clase fuera del package escaneado...), este test se pone ROJO y el error real está AL FONDO
 * del stack trace. Es el "¿el cableado arranca?" de la capa de Spring.
 *
 * Contraste con TaskServiceTest: ese NO levanta Spring (construye TaskService con 'new') — por eso
 * es rápido y no se entera de problemas de cableado. Cada uno prueba una cosa distinta.
 */
@SpringBootTest
class TaskflowApiApplicationTests {

    @Test
    void contextLoads() {
    }
}
