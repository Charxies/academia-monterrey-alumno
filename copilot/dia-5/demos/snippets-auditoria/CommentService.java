package com.taskflow.service;

import com.taskflow.exception.TaskNotFoundException;
import com.taskflow.model.Comment;
import com.taskflow.repository.CommentRepository;
import com.taskflow.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

/**
 * KATA MP-2 — snippet 3 de 3.
 *
 * Servicio de comentarios de tareas. Este snippet es el DISTRACTOR: está CORRECTO.
 * Sigue las convenciones del repo (inyección por constructor, campos private final, excepción de
 * dominio para not-found, queries derivadas de Spring Data sin SQL nativo). No hay nada que "corregir":
 * el criterio de listo de la kata es que NO se toque de más.
 *
 * Nota: no compila fuera de tu proyecto (usa tipos de taskflow-api). Se LEE, no se ejecuta.
 */
@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final TaskRepository taskRepository;

    public CommentService(CommentRepository commentRepository, TaskRepository taskRepository) {
        this.commentRepository = commentRepository;
        this.taskRepository = taskRepository;
    }

    /** Crea un comentario para una tarea existente; el autor sale del usuario autenticado. */
    public Comment crear(Long taskId, Long authorId, String body) {
        if (!taskRepository.existsById(taskId)) {
            throw new TaskNotFoundException(taskId);
        }
        Comment comment = new Comment(null, taskId, authorId, body, Instant.now());
        return commentRepository.save(comment);
    }

    /** Lista los comentarios de una tarea, más recientes primero. */
    public List<Comment> listarPorTarea(Long taskId) {
        if (!taskRepository.existsById(taskId)) {
            throw new TaskNotFoundException(taskId);
        }
        return commentRepository.findByTaskIdOrderByCreatedAtDesc(taskId);
    }
}
