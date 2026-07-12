package com.taskflow.repository;

import com.taskflow.model.Task;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * InMemoryTaskRepository — la capa de persistencia del CLI, HOY en memoria.
 *
 * Guarda tareas en un Map&lt;Long, Task&gt; (clave = id) e ids autoincrementales encapsulados.
 * El menú NUNCA toca el Map: todo pasa por estos métodos. Esta clase es literalmente la que
 * en S2D1 recibe la anotación @Repository y Spring inyecta — por eso los nombres son los de
 * Spring Data (save/findById/findAll/deleteById): adoptarlos HOY es intencional.
 *
 * El esqueleto COMPILA (los cuerpos devuelven valores neutros). Tu trabajo: llenar los TODO.
 */
public class InMemoryTaskRepository {

    // El Map dará búsqueda por id en O(1): adiós al 'for' lineal del warm-up.
    private final Map<Long, Task> store = new HashMap<>();

    // Secuencia autoincremental encapsulada: nadie de fuera la ve ni la toca.
    private long secuencia = 0;

    /**
     * TODO 1 — save (upsert):
     *   - Si task.getId() == null  -> task.setId(nextId()) y guarda en el Map.
     *   - Si trae id               -> reemplaza (store.put) Y avanza la secuencia:
     *                                 secuencia = Math.max(secuencia, task.getId())
     *                                 (así cargar datos que ya traen ids — el CSV de D5 — no choca).
     *   - Devuelve la misma tarea (ya con id).
     */
    public Task save(Task task) {
        // TODO 1
        return task;
    }

    /**
     * TODO 2 — findById: devuelve store.get(id); null si no existe.
     *   (En D4 esto se volverá Optional&lt;Task&gt;.)
     */
    public Task findById(Long id) {
        // TODO 2
        return null;
    }

    /**
     * TODO 3 — findAll: COPIA DEFENSIVA -> new ArrayList<>(store.values()).
     *   Ojo: store.values() es una vista VIVA del Map; si la devuelves tal cual, quien la
     *   reciba podría borrar tareas del repositorio sin pasar por deleteById.
     */
    public List<Task> findAll() {
        // TODO 3
        return null;
    }

    /**
     * TODO 4 — deleteById: true si existía y se borró, false si no había nada con ese id.
     *   Pista: store.remove(id) devuelve el valor anterior (o null).
     */
    public boolean deleteById(Long id) {
        // TODO 4
        return false;
    }

    /** TODO 5 — nextId: id autoincremental encapsulado (pre-incremento: ++secuencia). */
    private long nextId() {
        // TODO 5
        return 0;
    }
}
