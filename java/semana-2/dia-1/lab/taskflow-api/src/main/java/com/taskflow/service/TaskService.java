package com.taskflow.service;

import com.taskflow.exception.TaskValidationException;
import com.taskflow.model.Priority;
import com.taskflow.model.Task;
import com.taskflow.repository.TaskRepository;

import java.time.LocalDate;
import java.util.List;

/**
 * TaskService — el corazón del integrador de S2D1: la clase NUEVA de hoy (ESQUELETO).
 *
 * TODO integrador paso 3: anota la clase con @Service (import org.springframework.stereotype.Service)
 *   para que sea un bean de la capa de negocio.
 *
 * La dependencia se declara en el CONSTRUCTOR (inyección por constructor, la ÚNICA del curso): campo
 * 'final', obligatoria y visible, y la clase se construye con 'new' en un test (TaskServiceTest, sin
 * Spring). Se depende de la INTERFAZ TaskRepository (as bajo la manga de D4). El constructor YA está
 * escrito: rellena los cuerpos de crear/listar/completar.
 *
 * Reglas de negocio: viven en Task (la factory Task.crear y setStatus). El service ORQUESTA, no re-valida.
 */
// TODO paso 3: @Service
public class TaskService {

    // El capstone exige "no hay Task sin Project"; en S2D1 el proyecto es solo un id demo (como en S1).
    private static final Long PROYECTO_DEMO = 1L;

    private final TaskRepository repository;

    public TaskService(TaskRepository repository) {
        this.repository = repository;
    }

    /**
     * TODO (1) crear: construye la tarea con la FACTORY de negocio
     *   Task.crear(title, description, priority, dueDate, PROYECTO_DEMO, null)  -- firma canónica del
     *   apéndice del capstone (status TODO, projectId demo, sin assignee) -- y guárdala con
     *   repository.save(...); devuelve la tarea con el id ya asignado.
     */
    public Task crear(String title, String description, Priority priority, LocalDate dueDate)
            throws TaskValidationException {
        // TODO (1): Task.crear(...) + repository.save(...)
        return null;
    }

    /**
     * TODO (2) listar: repository.findAll() ordenado con la estrategia TaskOrders.POR_URGENCIA
     *   (reuso literal de la Strategy de MP-3). Pista: .stream().sorted(TaskOrders.POR_URGENCIA).toList()
     */
    public List<Task> listar() {
        // TODO (2): findAll() ordenado por POR_URGENCIA
        return List.of();
    }

    /**
     * TODO (3) completar: repository.findById(id).orElseThrow(() -> new TaskNotFoundException(id))
     *   (el Optional de S1D4), luego task.setStatus(TaskStatus.DONE) (regla "no DONE sin assignee"),
     *   y cierra con repository.save(task) (redundante en memoria, correcto cuando el repo sea JPA en D4).
     */
    public Task completar(Long id) throws TaskValidationException {
        // TODO (3): findById(id).orElseThrow(...) + setStatus(DONE) + save(...)
        return null;
    }
}
