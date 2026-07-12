package com.taskflow.repository;

import com.taskflow.model.Task;

import java.util.List;
import java.util.Optional;

/**
 * TaskRepository — CONTRATO del repositorio de tareas. NUEVA de hoy (S1D5, paso 1 del integrador).
 *
 * Se EXTRAE de InMemoryTaskRepository con IntelliJ (Refactor -> Extract Interface): son sus 4
 * métodos públicos de CRUD, tal cual. A partir de aquí InMemoryTaskRepository (memoria) y
 * FileTaskRepository (memoria + CSV) comparten el MISMO contrato, así que quien los use
 * (Main, ReportService, los tests) depende de la INTERFAZ, no de una implementación concreta.
 *
 * Por qué importa mañana: en S2D1, Spring inyecta ESTE tipo. Hoy el que lo llama es un menú de
 * Scanner; el lunes lo llamará un controller HTTP — y no habrá que tocar el repositorio.
 *
 * Nota: métodos de persistencia como load()/save() (snapshot a disco) NO viven en este contrato;
 * son propios de FileTaskRepository (leer/escribir archivo no es responsabilidad de un repo en
 * memoria). El contrato es solo el CRUD que todos comparten.
 */
public interface TaskRepository {

    /** Guarda o actualiza (upsert). Sin id -> asigna el siguiente; con id -> reemplaza y avanza la secuencia. */
    Task save(Task task);

    /** Busca por id. Optional.empty() si no existe (nunca null). */
    Optional<Task> findById(Long id);

    /** Todas las tareas (copia defensiva). */
    List<Task> findAll();

    /** Borra por id. true si existía y se borró; false si no había nada con ese id. */
    boolean deleteById(Long id);
}
