package com.taskflow.runner;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * SeedRunner — la "UI" de hoy: sembrará 5 tareas al arrancar y las listará en consola (ESQUELETO).
 *
 * @Component + implements CommandLineRunner: Spring detecta el bean y ejecuta run(...) UNA vez al
 * terminar de levantar el contexto. HOY el starter arranca con run(...) VACÍO: el contexto sube,
 * este runner corre y no hace nada, y el proceso termina (sin spring-boot-starter-web no hay servidor
 * que quede vivo — es el comportamiento correcto del día).
 *
 * TODO integrador paso 4:
 *   1) Recibe TaskService por CONSTRUCTOR (nadie hace 'new' — lo inyecta el contenedor):
 *        private final TaskService taskService;
 *        public SeedRunner(TaskService taskService) { this.taskService = taskService; }
 *      Al hacerlo (con TaskService ya @Service), verás el checkpoint-error #3a si InMemoryTaskRepository
 *      aún no tiene @Repository. Léelo completo y anótalo.
 *   2) En run(...): siembra 5 tareas con taskService.crear(...) (títulos del dominio, prioridades
 *      variadas, dueDate futura o null -- hoy NO hay vencidas), manejando la checked
 *      TaskValidationException en un try/catch ÚNICO.
 *   3) Lista con taskService.listar() en una tabla printf (formato de S1D1).
 */
@Component
public class SeedRunner implements CommandLineRunner {

    @Override
    public void run(String... args) {
        // TODO paso 4: sembrar 5 tareas con taskService.crear(...) y listarlas con taskService.listar()
    }
}
