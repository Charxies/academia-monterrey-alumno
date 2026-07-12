package com.taskflow.service;

import com.taskflow.model.Project;
import com.taskflow.model.Task;
import com.taskflow.repository.InMemoryProjectRepository;
import com.taskflow.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * ProjectService — la capa de negocio del lado Project. ESQUELETO del integrador de HOY.
 *
 * Inyección por constructor (regla del curso): pide DOS colaboradores —
 *   - InMemoryProjectRepository: para listar/buscar proyectos (clase concreta; no tiene interfaz, YAGNI).
 *   - TaskRepository (la INTERFAZ): para las tareas de un proyecto (filtra findAll() por projectId).
 *
 * Contrato de responsabilidades (T6): este service NO sabe de HTTP (cero ResponseEntity aquí); solo
 * reglas y orquestación. El ProjectController traduce sus Optional/listas a códigos HTTP.
 */
@Service
public class ProjectService {

    private final InMemoryProjectRepository projectRepository;
    private final TaskRepository taskRepository;

    public ProjectService(InMemoryProjectRepository projectRepository, TaskRepository taskRepository) {
        this.projectRepository = projectRepository;
        this.taskRepository = taskRepository;
    }

    /**
     * TODO (integrador): devuelve todos los proyectos.
     * Pista: return projectRepository.findAll();
     */
    public List<Project> listar() {
        return List.of();   // TODO (integrador): reemplazar
    }

    /**
     * TODO (integrador): busca un proyecto por id (Optional sube tal cual, como en TaskService.buscarPorId).
     * El controller usará el Optional para decidir 404 (no existe) vs 200. Pista: return projectRepository.findById(id);
     */
    public Optional<Project> buscarPorId(Long id) {
        return Optional.empty();   // TODO (integrador): reemplazar
    }

    /**
     * TODO (integrador): las tareas de un proyecto = filtra TaskRepository.findAll() por projectId (stream de S1D4).
     * OJO: "no hay proyecto" (lo decide buscarPorId en el controller -> 404) es DISTINTO de "proyecto sin
     * tareas" (este método devuelve lista vacía -> 200 con []). Pista:
     *   return taskRepository.findAll().stream().filter(t -> projectId.equals(t.getProjectId())).toList();
     */
    public List<Task> tareasDe(Long projectId) {
        return List.of();   // TODO (integrador): reemplazar
    }
}
