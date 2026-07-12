package com.taskflow.repository;

import com.taskflow.model.Project;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * InMemoryProjectRepository — el lado Project de la persistencia en memoria. PROVISTO HOY (S2D2).
 *
 * Por qué te lo damos LISTO: construir repositorios en memoria ya fue el tema de S1D3; el foco de
 * hoy es la capa WEB (controllers, códigos HTTP, MockMvc), no volver a escribir un Map con ids.
 *
 * Detalles de diseño:
 *   - Lleva @Repository: el contenedor lo registra como bean y lo inyecta donde se pida un
 *     InMemoryProjectRepository (lo usa ProjectService por constructor).
 *   - NO tiene interfaz (a diferencia de TaskRepository): YAGNI. En S2D4, cuando llegue Spring Data
 *     JPA, el repositorio se resuelve de otra forma; extraer una interfaz hoy sería ceremonia inútil.
 *   - Mismo patrón que InMemoryTaskRepository: Map<Long, Project> para búsqueda O(1) por id, y una
 *     copia defensiva en findAll(). Aquí el id lo trae ya puesto el Project (lo pone el DataSeeder),
 *     así que save() solo hace put por el id del propio proyecto.
 */
@Repository
public class InMemoryProjectRepository {

    private final Map<Long, Project> store = new HashMap<>();

    /** Guarda (o reemplaza) un proyecto usando su propio id como clave. Lo usa el DataSeeder. */
    public Project save(Project project) {
        store.put(project.getId(), project);
        return project;
    }

    /** Busca por id. Optional.empty() si no existe (nunca null): el controller decidirá 404 vs 200. */
    public Optional<Project> findById(Long id) {
        return Optional.ofNullable(store.get(id));
    }

    /** Todos los proyectos, como COPIA DEFENSIVA (nadie borra tocando esta lista). */
    public List<Project> findAll() {
        return new ArrayList<>(store.values());
    }
}
