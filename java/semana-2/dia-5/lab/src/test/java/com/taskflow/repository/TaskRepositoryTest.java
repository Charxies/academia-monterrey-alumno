package com.taskflow.repository;

import com.taskflow.exception.TaskValidationException;
import com.taskflow.model.Priority;
import com.taskflow.model.Project;
import com.taskflow.model.Task;
import com.taskflow.model.TaskStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * TaskRepositoryTest — los tests de la CAPA que se construyó hoy (@DataJpaTest, decisión MASTER-PLAN).
 *
 * @DataJpaTest arranca un SLICE de JPA (no toda la app): H2 EN MEMORIA con esquema FRESCO por suite
 * (auto-reemplaza el datasource), Hibernate, y TestEntityManager para preparar datos. NO carga el
 * DataSeeder ni los @Service — la BD empieza VACÍA y cada test siembra lo suyo. Es @Transactional: cada
 * test hace rollback, así quedan aislados.
 *
 * Distinción en voz alta (dolor #5): estos tests corren contra H2 en MEMORIA; el runtime usa el
 * ARCHIVO. Un verde aquí NO absuelve una config divergente — por eso producción usa validate + migraciones.
 *
 * La FK task.project_id enseña ORDEN: hay que persistir el Project ANTES que sus tareas.
 */
@DataJpaTest
class TaskRepositoryTest {

    @Autowired
    private TestEntityManager em;

    @Autowired
    private TaskRepository taskRepository;

    // ---- helpers ----

    /** Persiste un proyecto y devuelve su id (asignado por la BD). La FK obliga a hacerlo primero. */
    private Long nuevoProyecto(String name) {
        Project p = em.persistFlushFind(new Project(null, name, "desc", 1L, LocalDate.now().minusDays(1)));
        return p.getId();
    }

    /** Construye y persiste una tarea de rehidratación (id=null -> lo pone la BD). */
    private Task persistirTarea(String title, TaskStatus status, Priority priority,
                                Long projectId, Long assigneeId, LocalDate due) throws TaskValidationException {
        return em.persistFlushFind(
                new Task(null, title, "desc", status, priority, projectId, assigneeId, due));
    }

    // ==================== Tests ====================

    @Test
    void save_asignaId_yNoEsLaSecuenciaDeInMemory() throws TaskValidationException {
        Long pid = nuevoProyecto("Proyecto A");

        // save() a través del repositorio JPA (no del TestEntityManager): la BD asigna el id.
        Task guardada = taskRepository.save(
                new Task(null, "Primera tarea", "desc", TaskStatus.TODO, Priority.MED, pid, 1L, null));

        // El id lo puso la BD (IDENTITY), no una secuencia manual (el Math.max de InMemory murió).
        assertThat(guardada.getId()).isNotNull();
    }

    @Test
    void findById_inexistente_optionalVacio() {
        assertThat(taskRepository.findById(9_999L)).isEmpty();
    }

    @Test
    void findByStatus_filtraCorrecto() throws TaskValidationException {
        Long pid = nuevoProyecto("Proyecto B");
        persistirTarea("TODO uno", TaskStatus.TODO, Priority.LOW, pid, 1L, null);
        persistirTarea("TODO dos", TaskStatus.TODO, Priority.MED, pid, 1L, null);
        persistirTarea("Hecha", TaskStatus.DONE, Priority.HIGH, pid, 1L, null);

        List<Task> todo = taskRepository.findByStatus(TaskStatus.TODO);

        assertThat(todo).hasSize(2);
        assertThat(todo).allMatch(t -> t.getStatus() == TaskStatus.TODO);
    }

    @Test
    void findByProjectId_soloLasDelProyecto() throws TaskValidationException {
        // Dos proyectos (persistidos PRIMERO — la FK obliga el orden); tareas repartidas.
        Long pidA = nuevoProyecto("Proyecto con dos");
        Long pidB = nuevoProyecto("Proyecto con una");
        persistirTarea("A-1", TaskStatus.TODO, Priority.LOW, pidA, 1L, null);
        persistirTarea("A-2", TaskStatus.IN_PROGRESS, Priority.MED, pidA, 1L, null);
        persistirTarea("B-1", TaskStatus.TODO, Priority.HIGH, pidB, 1L, null);

        List<Task> deA = taskRepository.findByProjectId(pidA);

        assertThat(deA).hasSize(2);
        assertThat(deA).allMatch(t -> t.getProjectId().equals(pidA));
    }

    @Test
    void findByAssigneeIdAndStatus_combinaCriterios() throws TaskValidationException {
        Long pid = nuevoProyecto("Proyecto C");
        persistirTarea("De 5, TODO", TaskStatus.TODO, Priority.MED, pid, 5L, null);      // match
        persistirTarea("De 5, DONE", TaskStatus.DONE, Priority.MED, pid, 5L, null);      // status distinto
        persistirTarea("De 6, TODO", TaskStatus.TODO, Priority.MED, pid, 6L, null);      // asignado distinto

        List<Task> resultado = taskRepository.findByAssigneeIdAndStatus(5L, TaskStatus.TODO);

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getTitle()).isEqualTo("De 5, TODO");
    }

    @Test
    void countByStatus_yTituloContaining() throws TaskValidationException {
        Long pid = nuevoProyecto("Proyecto D");
        persistirTarea("Documentar la API", TaskStatus.TODO, Priority.HIGH, pid, 1L, null);   // "API"
        persistirTarea("api gateway", TaskStatus.TODO, Priority.LOW, pid, 1L, null);          // "api"
        persistirTarea("Sin coincidencia", TaskStatus.DONE, Priority.MED, pid, 1L, null);

        // countByStatus: cuenta en la BD (no trae filas para contar).
        assertThat(taskRepository.countByStatus(TaskStatus.TODO)).isEqualTo(2);

        // IgnoreCase: "api" matchea tanto "API" como "api" (2 de las 3).
        assertThat(taskRepository.findByTitleContainingIgnoreCase("api")).hasSize(2);
    }
}
