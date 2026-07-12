package com.taskflow.repository;

import com.taskflow.model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * InMemoryTaskRepository — la capa de persistencia del CLI, en memoria.
 *
 * VIENE RESUELTO de ayer (D3): guarda tareas en un Map&lt;Long, Task&gt; con ids
 * autoincrementales encapsulados. El menú NUNCA toca el Map directo. Es la misma clase que
 * en S2D1 recibe @Repository y Spring inyecta (nombres save/findById/findAll/deleteById).
 *
 * LO ÚNICO PENDIENTE HOY: la CIRUGÍA de findById. Ahora devuelve Task/null (contrato de D3);
 * el trabajo de hoy (MP-8 + integrador) es cambiarlo a Optional&lt;Task&gt;. Ver el TODO abajo.
 */
public class InMemoryTaskRepository {

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
     * TODO (MP-8 / integrador) — CIRUGÍA DE OPTIONAL:
     *   Cambia la FIRMA de este método a:
     *       public Optional<Task> findById(Long id)
     *   y el cuerpo a:
     *       return Optional.ofNullable(store.get(id));
     *   (necesitarás  import java.util.Optional;)
     *
     *   En cuanto cambies la firma, el compilador marcará en ROJO a todos los que llaman a
     *   findById (Main.completarPorId, Main.buscarPorId): ESO ES LO BUENO. Un bug que hoy
     *   explota en runtime (NullPointerException) se convierte en un error de compilación.
     *   Actualiza cada caller con orElseThrow / map / ifPresentOrElse (nunca con .get()).
     *
     * Por ahora devuelve Task/null (contrato de D3) para que el starter compile tal cual.
     */
    public Task findById(Long id) {
        return store.get(id);
    }

    /**
     * Devuelve TODAS las tareas como una COPIA DEFENSIVA. 'store.values()' es una vista VIVA
     * del Map; la copia evita que quien la reciba borre tareas sin pasar por deleteById.
     */
    public List<Task> findAll() {
        return new ArrayList<>(store.values());
    }

    /** Borra por id. Devuelve true si existía y se borró; false si no había nada con ese id. */
    public boolean deleteById(Long id) {
        return store.remove(id) != null;
    }

    /** Id autoincremental encapsulado: pre-incremento, así el primer id es 1. */
    private long nextId() {
        return ++secuencia;
    }
}
