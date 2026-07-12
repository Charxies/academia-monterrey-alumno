package com.taskflow.qa.api.tests;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.fail;

// TODO (MP-8): etiqueta esta clase con @Tag("ci").

/**
 * TasksApiTest — CRUD de tareas encadenado bajo un proyecto propio (MP-5):
 *   POST /projects/{id}/tasks (201) -> GET /projects/{id}/tasks -> PATCH /tasks/{id}/status ->
 *   PUT /tasks/{id} -> DELETE /tasks/{id} (204).
 *
 * OJO con el PATCH: mueve el estado a IN_PROGRESS, NO a DONE. Pasar a DONE una tarea sin assignee es
 * una regla de negocio de la API (responde 422) — ese caso se audita aparte.
 */
class TasksApiTest {

    /** TODO (MP-5): token + un proyecto propio en @BeforeAll (guarda su id para las tareas). */

    /** TODO (MP-5): POST tarea (201, status TODO) y verifica que aparece en GET /projects/{id}/tasks. */
    @Test
    @DisplayName("crear tarea: 201 y aparece en la lista de tareas del proyecto")
    void crearTareaYListar() {
        fail("TODO MP-5: crear tarea y listarla");
    }

    /** TODO (MP-5): sobre una tarea -> PATCH status a IN_PROGRESS (200) -> PUT reemplazo (200,
     *  título nuevo) -> DELETE (204) -> GET /tasks/{id} (404). */
    @Test
    @DisplayName("patch estado, PUT y borrar una tarea")
    void patchPutYBorrarTarea() {
        fail("TODO MP-5: patch + put + delete de tarea");
    }
}
