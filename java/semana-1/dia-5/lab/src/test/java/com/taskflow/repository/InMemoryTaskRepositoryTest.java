package com.taskflow.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * InMemoryTaskRepositoryTest — tests del repositorio (ESQUELETO, MP-7).
 *
 * AAA (arrange-act-assert), un comportamiento por test. INDEPENDENCIA: @BeforeEach crea un repo
 * NUEVO por test (nada de estado compartido entre tests — eco de MP-4).
 *
 * Cuerpos VACÍOS = verde mentiroso (MP-6). Llénalos.
 */
class InMemoryTaskRepositoryTest {

    private InMemoryTaskRepository repo;

    @BeforeEach
    void nuevoRepo() {
        repo = new InMemoryTaskRepository();   // repo limpio por cada test
    }

    @Test
    void save_sinId_asignaIdIncrementalYCreceFindAll() {
        // TODO MP-7: guarda 2 tareas sin id -> ids 1 y 2; findAll().size() == 2.
    }

    @Test
    void save_conIdExplicito_avanzaLaSecuencia() {
        // TODO MP-7: guarda una con id 6; el siguiente save SIN id debe dar 7 (no 1).
        //   (Es justo lo que necesita cargar el CSV en el integrador.)
    }

    @Test
    void findById_existente_devuelveOptionalConTitulo() {
        // TODO MP-7: guarda una; findById(id).isPresent() y el título correcto (orElseThrow, NO get()).
    }

    @Test
    void findById_inexistente_devuelveOptionalVacio() {
        // TODO MP-7: assertTrue(repo.findById(999L).isEmpty());
    }

    @Test
    void deleteById_existente_eliminaYDevuelveTrue() {
        // TODO MP-7: deleteById de una existente -> true; luego findById vacío.
    }
}
