package com.taskflow.repository;

import com.taskflow.model.Project;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * InMemoryProjectRepository — el lado Project de la persistencia en memoria. Viene de D2 (findAll/
 * findById); HOY (integrador paso 2) implementa la interfaz ProjectRepository extraída y gana el
 * CRUD que faltaba: save con secuencia-upsert y deleteById. Mismo patrón que InMemoryTaskRepository.
 *
 * Matiz vs Task: Project tiene 'id' primitivo (long) y es INMUTABLE (sin setId). Por eso save no
 * puede "ponerle" el id a un Project existente; cuando llega uno nuevo (id == 0, convención de "aún
 * sin id", como el null de Task) RECONSTRUYE un Project con el id asignado. En D4 esto lo hace JPA.
 */
// ================================================================================================
// TODO (S2D4, MP-6) — ESTA CLASE SE ELIMINA HOY (igual que InMemoryTaskRepository): cuando
// ProjectRepository extienda JpaRepository, sobra. Spring Data la implementa por debajo.
// ================================================================================================
@Repository
public class InMemoryProjectRepository implements ProjectRepository {

    private final Map<Long, Project> store = new HashMap<>();

    // Secuencia autoincremental encapsulada (como en InMemoryTaskRepository).
    private long secuencia = 0;

    /**
     * Guarda o actualiza (upsert):
     *   - id == 0 (proyecto nuevo, POST /projects) -> asigna el siguiente id y, como Project es
     *     inmutable, RECONSTRUYE el proyecto con ese id.
     *   - id != 0 (semilla del DataSeeder, o PUT con id existente) -> reemplaza y AVANZA la secuencia
     *     al máximo id visto (así cargar ids fijos no choca con los autogenerados).
     */
    @Override
    public Project save(Project project) {
        if (project.getId() == 0L) {
            long nuevoId = ++secuencia;
            Project conId = new Project(nuevoId, project.getName(), project.getDescription(),
                    project.getOwner(), project.getCreatedAt());
            store.put(nuevoId, conId);
            return conId;
        }
        secuencia = Math.max(secuencia, project.getId());
        store.put(project.getId(), project);
        return project;
    }

    /** Busca por id. Optional.empty() si no existe (nunca null): el service decidirá 404 vs 200. */
    @Override
    public Optional<Project> findById(Long id) {
        return Optional.ofNullable(store.get(id));
    }

    /** Todos los proyectos, como COPIA DEFENSIVA (nadie borra tocando esta lista). */
    @Override
    public List<Project> findAll() {
        return new ArrayList<>(store.values());
    }

    /** Borra por id (void). Si no había nada con ese id, no hace nada. La cascada la orquesta el service. */
    @Override
    public void deleteById(Long id) {
        store.remove(id);
    }
}
