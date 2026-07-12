package com.taskflow.repository;

import com.taskflow.model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * InMemoryTaskRepository — la persistencia EN MEMORIA copiada de S1 (D3/D4/D5).
 *
 * OJO (checkpoint-error #3a del integrador): esta clase se copia HOY SIN el estereotipo, A PROPÓSITO.
 * Cuando anotes TaskService con @Service y arranques, Spring dirá:
 *   "Parameter 0 of constructor in ...TaskService required a bean of type
 *    'com.taskflow.repository.TaskRepository' that could not be found"
 * Léelo COMPLETO: dice qué falta y quién lo pedía. La solución es anotar ESTA clase (no la interfaz)
 * con @Repository -> así el contenedor la registra como bean y la inyecta.
 *
 * TODO integrador paso 4: añade la anotación @Repository sobre la clase (y su import
 *   org.springframework.stereotype.Repository) cuando el arranque te lo exija.
 *
 * El cuerpo es el MISMO de S1; deleteById ya es void (cirugía del paso 2).
 */
public class InMemoryTaskRepository implements TaskRepository {

    // El Map da búsqueda por id en O(1): adiós al 'for' lineal.
    private final Map<Long, Task> store = new HashMap<>();

    // Secuencia autoincremental encapsulada: nadie de fuera la ve ni la toca.
    private long secuencia = 0;

    /**
     * Guarda o actualiza una tarea (upsert):
     *   - Si viene sin id (id == null) -> le asigna el siguiente id y la inserta.
     *   - Si viene con id             -> reemplaza la que hubiera, y AVANZA la secuencia al máximo
     *                                     id visto (así cargar datos con ids no choca).
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

    /** findById devuelve Optional (cirugía de S1D4): empty() si no existe, nunca null. */
    @Override
    public Optional<Task> findById(Long id) {
        return Optional.ofNullable(store.get(id));
    }

    /** Todas las tareas como COPIA DEFENSIVA (no exponemos la vista viva del Map). */
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
