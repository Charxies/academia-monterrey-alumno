package com.taskflow.service;

import com.taskflow.dto.ProjectRequest;
import com.taskflow.exception.ProjectNotFoundException;
import com.taskflow.model.Project;
import com.taskflow.model.Role;
import com.taskflow.model.Task;
import com.taskflow.model.User;
import com.taskflow.repository.ProjectRepository;
import com.taskflow.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * ProjectService — la capa de negocio del lado Project. HOY (integrador) crece a CRUD completo y pasa
 * a depender de la INTERFAZ ProjectRepository (extraída en el paso 2), no de la clase concreta.
 *
 * Dos colaboradores por constructor:
 *   - ProjectRepository (interfaz): CRUD de proyectos.
 *   - TaskRepository (interfaz): para tareasDe(...) y la CASCADA manual al borrar ("no Task sin
 *     Project"). El filtrado por proyecto vive AQUÍ (tareasDe, de D2) y NO se duplica en TaskService.
 *
 * Contrato de responsabilidades (T6): cero HTTP aquí. El "no existe" se traduce con orElseThrow ->
 * ProjectNotFoundException, y el advice le pone el 404.
 *
 * ============================================================================================
 * TODO (S2D4) — este servicio SÍ se toca hoy (la promesa "no tocar el servicio" protege a TaskService,
 * no a ProjectService):
 *   MP-4: al aplanar Project (User owner -> Long ownerId), 'crear' y 'reemplazar' manejan un Long
 *         ownerId, no un objeto User. 'crear' construye con id=null (la BD asigna), no con 0L.
 *         Sustituir la constante OWNER_SEMILLA (User) por un OWNER_SEMILLA_ID (Long = 1L).
 *   MP-6: 'tareasDe' cambia el stream-filter por taskRepository.findByProjectId(projectId) — la nota
 *         de D3 ("en D4 esto se vuelve un query method") se paga AQUÍ.
 * ============================================================================================
 */
@Service
public class ProjectService {

    // Owner semilla (usuario id 1L). D5: saldrá del JWT del request en lugar de fijarse a mano.
    private static final User OWNER_SEMILLA = new User(1L, "ana", "ana@taskflow.dev", Role.ADMIN);

    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;

    public ProjectService(ProjectRepository projectRepository, TaskRepository taskRepository) {
        this.projectRepository = projectRepository;
        this.taskRepository = taskRepository;
    }

    /** Todos los proyectos. */
    public List<Project> listar() {
        return projectRepository.findAll();
    }

    /** Un proyecto por id (el Optional sube tal cual: el controller decide 404 vs 200). */
    public Optional<Project> buscarPorId(Long id) {
        return projectRepository.findById(id);
    }

    /**
     * Las tareas de un proyecto: filtra TaskRepository.findAll() por projectId (stream de S1D4).
     * "no existe el proyecto" (404) lo decide el controller con buscarPorId; ESTE método, si el
     * proyecto existe pero no tiene tareas, devuelve [] (-> 200 con []). No son lo mismo.
     */
    public List<Task> tareasDe(Long projectId) {
        return taskRepository.findAll().stream()
                .filter(t -> projectId.equals(t.getProjectId()))
                .toList();
    }

    /**
     * Crea un proyecto (POST): el request trae name y description; el service pone el owner (semilla)
     * y createdAt (hoy). id == 0 -> el repositorio le asigna el siguiente en save.
     */
    public Project crear(ProjectRequest request) {
        Project nuevo = new Project(0L, request.name(), request.description(),
                OWNER_SEMILLA, LocalDate.now());   // D5: el owner saldrá del JWT
        return projectRepository.save(nuevo);
    }

    /**
     * Reemplazo COMPLETO (PUT): existe -> reconstruye con el MISMO id, conservando owner y createdAt
     * (el cliente no los toca); name y description vienen del request. No existe -> 404.
     */
    public Project reemplazar(Long id, ProjectRequest request) {
        Project actual = projectRepository.findById(id)
                .orElseThrow(() -> new ProjectNotFoundException(id));
        Project reemplazo = new Project(actual.getId(), request.name(), request.description(),
                actual.getOwner(), actual.getCreatedAt());
        return projectRepository.save(reemplazo);
    }

    /**
     * Elimina un proyecto (DELETE) con CASCADA MANUAL: borra primero sus tareas (regla "no Task sin
     * Project") y luego el proyecto. No existe -> 404. En D4, JPA formaliza esto con cascade/
     * orphanRemoval; la regla owner/ADMIN llega en D5 (hoy queda abierto, documentado en el @Operation).
     */
    public void eliminar(Long id) {
        projectRepository.findById(id)
                .orElseThrow(() -> new ProjectNotFoundException(id));
        tareasDe(id).forEach(t -> taskRepository.deleteById(t.getId()));   // cascada manual
        projectRepository.deleteById(id);
    }
}
