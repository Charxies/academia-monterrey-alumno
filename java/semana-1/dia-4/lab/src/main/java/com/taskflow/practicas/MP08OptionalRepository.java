package com.taskflow.practicas;

import com.taskflow.model.Task;

import java.util.HashMap;
import java.util.Map;

/**
 * MP-8 — Cirugía de firma en una COPIA LOCAL del repositorio.
 *
 * Practica aquí el cambio  Task findById(Long)  ->  Optional<Task> findById(Long)  sobre el
 * mini-repo local de abajo (el repo REAL se opera en el integrador).
 *
 * Qué construir:
 *   1. Cambia MiniRepo.findById para que devuelva Optional<Task> con Optional.ofNullable(store.get(id))
 *      (importa java.util.Optional).
 *   2. Carga la semilla: SeedData.tareas().forEach(repo::save).
 *   3. Consume el Optional SIN .get():
 *        - orElseThrow(() -> new TaskNotFoundException(id))  (importa la excepción)
 *        - map(Task::getTitle).orElse("(no encontrada)")
 *        - ifPresent(t -> ...)
 *   Regla dura del curso: .get() PROHIBIDO.
 *
 * Correr: mvn -q compile exec:java "-Dexec.mainClass=com.taskflow.practicas.MP08OptionalRepository"
 */
public class MP08OptionalRepository {

    /** Copia LOCAL del repositorio para la cirugía (no es el InMemoryTaskRepository real). */
    static class MiniRepo {
        private final Map<Long, Task> store = new HashMap<>();

        void save(Task t) {
            store.put(t.getId(), t);
        }

        // TODO 1: cambia la firma a  Optional<Task> findById(Long id)
        //         y el cuerpo a  return Optional.ofNullable(store.get(id));
        Task findById(Long id) {
            return store.get(id);
        }
    }

    public static void main(String[] args) {
        // TODO 2: carga la semilla con SeedData.tareas().forEach(repo::save).
        // TODO 3: consume el Optional con orElseThrow / map+orElse / ifPresent (sin .get()).
        System.out.println("MP-8: pendiente. findById -> Optional<Task> y sus consumidores.");
    }
}
