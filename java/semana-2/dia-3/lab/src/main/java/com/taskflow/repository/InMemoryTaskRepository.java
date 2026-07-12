package com.taskflow.repository;

import com.taskflow.model.Task;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * InMemoryTaskRepository — la persistencia EN MEMORIA copiada de S1 (D3/D4/D5).
 *
 * Novedad de S2D1: lleva @Repository — así el contenedor de Spring lo registra como BEAN y lo
 * inyecta donde se pida un TaskRepository (TaskService, ReportService). El estereotipo va en la
 * CLASE concreta, NUNCA en la interfaz (una interfaz no se instancia): ponerlo en TaskRepository
 * produciría "required a bean of type 'TaskRepository' that could not be found".
 *
 * Semánticamente @Repository marca la capa de acceso a datos (y en D4, con JPA, además traducirá
 * las excepciones de la BD). El cuerpo es el MISMO de S1: upsert con secuencia, findById con Optional.
 *
 * Cirugía del paso 2: deleteById ahora es void (antes devolvía boolean para el menú del CLI).
 */
@Repository
public class InMemoryTaskRepository implements TaskRepository {

    // El Map da búsqueda por id en O(1): adiós al 'for' lineal.
    private final Map<Long, Task> store = new HashMap<>();

    // Secuencia autoincremental encapsulada: nadie de fuera la ve ni la toca.
    private long secuencia = 0;

    /**
     * Guarda o actualiza una tarea (upsert):
     *   - Si viene sin id (id == null) -> le asigna el siguiente id y la inserta.
     *   - Si viene con id             -> reemplaza la que hubiera, y AVANZA la secuencia al
     *                                     máximo id visto (así cargar datos con ids no choca).
     */
    @Override
    public Task save(Task task) {
        if (task.getId() == null) {
            task.setId(nextId());
        } else {
            secuencia = Math.max(secuencia, task.getId());
        }
        store.put(task.getId(), task);
        return task;
    }

    /**
     * findById devuelve Optional&lt;Task&gt; (cirugía de S1D4): si store.get(id) es null (no existe)
     * queda un Optional.empty(); si hay tarea, un Optional con ella. El caller la consume con
     * orElseThrow / map / ifPresentOrElse. Regla dura del curso: prohibido .get().
     */
    @Override
    public Optional<Task> findById(Long id) {
        return Optional.ofNullable(store.get(id));
    }

    /**
     * Devuelve TODAS las tareas como una COPIA DEFENSIVA. 'store.values()' es una vista VIVA
     * del Map; la copia evita que quien la reciba borre tareas sin pasar por deleteById.
     */
    @Override
    public List<Task> findAll() {
        return new ArrayList<>(store.values());
    }

    /** Borra por id (void, cirugía del paso 2). Si no había nada con ese id, no hace nada. */
    @Override
    public void deleteById(Long id) {
        store.remove(id);
    }

    /** Id autoincremental encapsulado: pre-incremento, así el primer id es 1. */
    private long nextId() {
        return ++secuencia;
    }
}
