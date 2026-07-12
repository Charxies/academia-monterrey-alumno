package com.taskflow.repository;

import com.taskflow.model.Project;

import java.util.List;
import java.util.Optional;

/**
 * ProjectRepository — CONTRATO del repositorio de proyectos. EXTRAÍDO HOY (S2D3, integrador paso 2)
 * con Refactor -> Extract Interface: el MISMO gesto que en S1D5 con TaskRepository.
 *
 * Por qué ahora sí (y en D2 era YAGNI): hoy el lado Project crece a CRUD completo (save con
 * secuencia, deleteById), y el ProjectService debe hablar con la INTERFAZ, no con la implementación
 * concreta — así en D4 InMemoryProjectRepository se cambia por Spring Data JPA sin tocar el service.
 * Estos 4 nombres (save/findById/findAll/deleteById) son los que Spring Data espera.
 */
// ================================================================================================
// TODO (S2D4, MP-6) — Mismo swap que TaskRepository: 'extends JpaRepository<Project, Long>', borrar
// las 4 firmas de abajo, y ELIMINAR InMemoryProjectRepository. Aquí no se necesita ninguna derived
// query hoy (tareasDe vive en TaskRepository.findByProjectId). Recuerda: Project.id pasa a Long (MP-4).
// ================================================================================================
public interface ProjectRepository {

    /** Guarda o actualiza (upsert). id == 0 -> asigna el siguiente; con id -> reemplaza. */
    Project save(Project project);

    /** Busca por id. Optional.empty() si no existe (nunca null). */
    Optional<Project> findById(Long id);

    /** Todos los proyectos (copia defensiva). */
    List<Project> findAll();

    /** Borra por id. void (la firma que Spring Data JPA espera en D4). */
    void deleteById(Long id);
}
