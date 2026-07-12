package com.taskflow.repository;

import com.taskflow.model.Task;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * KATA MP-2 — snippet 1 de 3.
 *
 * Generado por Copilot Chat ante el prompt: "un query rápido para buscar tareas por título".
 * No lo copies a tu repo sin auditarlo: audita, clasifica el riesgo y, si aplica, corrígelo.
 *
 * Nota: no compila fuera de tu proyecto (usa tipos de taskflow-api). Se LEE, no se ejecuta;
 * el inyectable jamás se corre contra una BD real en clase.
 */
@Repository
public class TaskSearchRepository {

    @PersistenceContext
    private EntityManager em;

    /**
     * Busca tareas cuyo título contenga el texto dado.
     */
    @SuppressWarnings("unchecked")
    public List<Task> buscarPorTitulo(String titulo) {
        String sql = "SELECT * FROM tasks WHERE title LIKE '%" + titulo + "%'";
        return em.createNativeQuery(sql, Task.class).getResultList();
    }
}
