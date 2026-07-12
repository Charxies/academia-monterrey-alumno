package com.taskflow.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

/**
 * TaskRepositoryTest — ESQUELETO del integrador (paso 3). @DataJpaTest arranca un SLICE de JPA: H2 EN
 * MEMORIA con esquema fresco por suite (auto-reemplaza el datasource) + TestEntityManager para
 * preparar datos. Distinción en voz alta: estos tests corren contra H2 en MEMORIA; el runtime usa el
 * ARCHIVO — un verde aquí no absuelve una config divergente (dolor #5).
 *
 * OJO — TEST MENTIROSO (eco S1D5 MP-6): los cuerpos están VACÍOS y por eso salen "verdes" sin probar
 * NADA. Un test que no asserta miente. Rellena los 6 cuerpos con AAA real (usa el TestEntityManager
 * para persistir; la FK obliga a persistir el Project ANTES que sus tareas).
 *
 * TODO (integrador) — para que estos tests prueben algo:
 *   1) Descomenta el @Autowired de TaskRepository (existe cuando el repo extienda JpaRepository, MP-6).
 *   2) Rellena cada cuerpo. Pistas por método en el nombre. Criterio: >=6 tests reales VERDES.
 */
@DataJpaTest
class TaskRepositoryTest {

    @Autowired
    private TestEntityManager em;

    // TODO (MP-6 hecho): descomentar — el bean existe cuando TaskRepository extienda JpaRepository.
    // @Autowired
    // private TaskRepository taskRepository;

    @Test
    void save_asignaId_yNoEsLaSecuenciaDeInMemory() {
        // TODO: persistir un Project, guardar una Task con id=null por el repo, y afirmar que la BD
        // le asignó un id (no una secuencia manual: el Math.max de InMemory murió).
    }

    @Test
    void findById_inexistente_optionalVacio() {
        // TODO: taskRepository.findById(<id que no existe>) -> Optional vacío.
    }

    @Test
    void findByStatus_filtraCorrecto() {
        // TODO: persistir tareas de varios estados; findByStatus(TODO) devuelve solo las TODO.
    }

    @Test
    void findByProjectId_soloLasDelProyecto() {
        // TODO: persistir el Project PRIMERO (la FK enseña orden), luego tareas repartidas entre dos
        // proyectos; findByProjectId(uno) devuelve solo las de ese proyecto.
    }

    @Test
    void findByAssigneeIdAndStatus_combinaCriterios() {
        // TODO: tareas de distintos asignados/estados; findByAssigneeIdAndStatus combina ambos con And.
    }

    @Test
    void countByStatus_yTituloContaining() {
        // TODO: countByStatus cuenta en la BD; findByTitleContainingIgnoreCase("api") matchea "API" y "api".
    }
}
