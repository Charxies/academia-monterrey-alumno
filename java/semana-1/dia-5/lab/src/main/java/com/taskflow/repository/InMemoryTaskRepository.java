package com.taskflow.repository;

import com.taskflow.model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * InMemoryTaskRepository — la persistencia del CLI EN MEMORIA (heredada de D3/D4).
 *
 * Cambio de hoy (S1D5, paso 1 del integrador): ahora IMPLEMENTA {@link TaskRepository}. El
 * cuerpo es el MISMO de ayer (upsert con secuencia, findById con Optional); lo único nuevo es
 * el "implements" + los @Override. Así comparte contrato con FileTaskRepository.
 *
 * Recordatorio de la cirugía de D4: findById devuelve Optional&lt;Task&gt; (el tipo dice "puede
 * no haber resultado") y save con id explícito AVANZA la secuencia al máximo id visto — es
 * justo lo que necesita cargar el CSV en el integrador: los ids nuevos no chocan con los cargados.
 */
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
     * findById devuelve Optional&lt;Task&gt; (cirugía de D4).
     *
     * Optional.ofNullable envuelve el resultado del Map: si store.get(id) es null (no existe),
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

    /** Borra por id. Devuelve true si existía y se borró; false si no había nada con ese id. */
    @Override
    public boolean deleteById(Long id) {
        return store.remove(id) != null;
    }

    /** Id autoincremental encapsulado: pre-incremento, así el primer id es 1. */
    private long nextId() {
        return ++secuencia;
    }
}
