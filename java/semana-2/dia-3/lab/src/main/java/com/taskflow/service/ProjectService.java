package com.taskflow.service;

import com.taskflow.model.Project;
import com.taskflow.model.Task;
import com.taskflow.repository.InMemoryProjectRepository;
import com.taskflow.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * ProjectService — la capa de negocio del lado Project (integrador S2D2).
 *
 * Inyección por constructor (regla del curso): pide DOS colaboradores —
 *   - InMemoryProjectRepository: para listar/buscar proyectos (clase concreta; sin interfaz, YAGNI).
 *   - TaskRepository (la INTERFAZ): para las tareas de un proyecto (filtra findAll() por projectId).
 *
 * Contrato de responsabilidades (T6): este service NO sabe de HTTP (cero ResponseEntity aquí); solo
 * reglas y orquestación. El ProjectController traduce sus Optional/listas a códigos HTTP.
 *
 * TODO (integrador, pasos 2-3): crece a CRUD completo —
 *   - Paso 2: EXTRAE la interfaz ProjectRepository (Refactor -> Extract Interface, el gesto de S1D5)
 *       con save/findById/findAll/deleteById, haz que InMemoryProjectRepository la implemente, y
 *       cambia el tipo del campo/constructor de aquí a la INTERFAZ ProjectRepository.
 *   - Paso 3: crear(ProjectRequest) -> new Project(0L, name, description, OWNER_SEMILLA, LocalDate.now())
 *       + save (owner semilla id 1L; "D5: saldrá del JWT"). reemplazar(Long id, ProjectRequest) ->
 *       404 si no existe; conserva id, owner y createdAt. eliminar(Long id) -> 404 si no existe;
 *       CASCADA MANUAL: borra sus tareas (tareasDe + taskRepository.deleteById) y luego el proyecto.
 */
@Service
public class ProjectService {

    private final InMemoryProjectRepository projectRepository;
    private final TaskRepository taskRepository;

    public ProjectService(InMemoryProjectRepository projectRepository, TaskRepository taskRepository) {
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
     * OJO a la distinción del enunciado: "no existe el proyecto" lo decide buscarPorId en el
     * controller (-> 404); ESTE método, si el proyecto existe pero no tiene tareas, devuelve una
     * lista vacía (-> 200 con []). No son lo mismo.
     */
    public List<Task> tareasDe(Long projectId) {
        return taskRepository.findAll().stream()
                .filter(t -> projectId.equals(t.getProjectId()))
                .toList();
    }
}
