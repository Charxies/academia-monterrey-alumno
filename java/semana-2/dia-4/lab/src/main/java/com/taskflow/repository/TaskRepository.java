package com.taskflow.repository;

import com.taskflow.model.Task;

import java.util.List;
import java.util.Optional;

/**
 * TaskRepository — CONTRATO del repositorio de tareas. Copiado de S1D5 (donde se extrajo con
 * Refactor -> Extract Interface) al proyecto Spring, con UNA cirugía de firma (paso 2 del integrador).
 *
 * En S2D1 este es el tipo que Spring INYECTA por constructor: TaskService/ReportService dependen de
 * ESTA interfaz, no de una implementación concreta (el "as bajo la manga" del jueves). Hoy detrás
 * hay un InMemoryTaskRepository anotado @Repository; en S2D4 la implementación la escribe Spring Data
 * (extends JpaRepository) SIN tocar el servicio — porque estos nombres (save/findById/findAll/
 * deleteById) son exactamente los que Spring Data espera.
 *
 * CIRUGÍA DE HOY (paso 2): deleteById devuelve void (en S1 devolvía boolean "¿existía?", que servía
 * al menú del CLI). El servicio REST hará findById(...).orElseThrow(...) ANTES de borrar (D3), así
 * que el boolean sobra — y void es justo la firma de Spring Data JPA.
 *
 * ================================================================================================
 * TODO (S2D4, MP-6) — EL MOMENTO DEL DÍA. Convertir este contrato en un repositorio Spring Data:
 *   1) 'public interface TaskRepository extends JpaRepository<Task, Long>' e importar
 *      org.springframework.data.jpa.repository.JpaRepository.
 *   2) BORRAR las 4 firmas de abajo: save/findById/findAll/deleteById VIENEN GRATIS de JpaRepository
 *      (+ count/existsById). Ya no las implementa nadie: un proxy de runtime lo hace.
 *   3) Como la interfaz sobrevive, InMemoryTaskRepository deja de compilar -> se ELIMINA (git lo
 *      recuerda). TaskService NO se toca (la promesa del día: git diff vacío).
 *   4) AÑADIR las derived queries del día (el NOMBRE del método ES la query):
 *        List<Task> findByStatus(TaskStatus status);
 *        List<Task> findByProjectId(Long projectId);
 *        List<Task> findByAssigneeIdAndStatus(Long assigneeId, TaskStatus status);
 *        List<Task> findByTitleContainingIgnoreCase(String fragment);
 *        List<Task> findByStatusNot(TaskStatus status);
 *        long countByStatus(TaskStatus status);
 * ================================================================================================
 */
public interface TaskRepository {

    /** Guarda o actualiza (upsert). Sin id -> asigna el siguiente; con id -> reemplaza y avanza la secuencia. */
    Task save(Task task);

    /** Busca por id. Optional.empty() si no existe (nunca null). */
    Optional<Task> findById(Long id);

    /** Todas las tareas (copia defensiva). */
    List<Task> findAll();

    /** Borra por id. void (cirugía del paso 2: la firma que Spring Data JPA espera en D4). */
    void deleteById(Long id);
}
