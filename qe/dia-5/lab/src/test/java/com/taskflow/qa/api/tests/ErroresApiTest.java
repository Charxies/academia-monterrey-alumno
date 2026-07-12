package com.taskflow.qa.api.tests;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.fail;

// TODO (MP-8): etiqueta esta clase con @Tag("ci").

/**
 * ErroresApiTest — probar errores ES probar el negocio (T4, MP-6). Un caso por código:
 *   400 -> title < 3 chars (Bean Validation en la frontera; ojo: si te da 500, encontraste un bug
 *          real en TU API -> arréglalo en el repo de la API)
 *   401 -> request SIN token (given().spec(ApiSpecs.base()), sin Authorization)
 *   403 -> regla del owner: usuario B borra el proyecto de A (DOS tokens de AuthClient)
 *   404 -> id inexistente (p.ej. GET /projects/999999)
 */
class ErroresApiTest {

    /** TODO (MP-6): POST /projects/{id}/tasks con title "ab" (2 chars) -> 400. (Crea antes un
     *  proyecto propio con un token de AuthClient.) */
    @Test
    @DisplayName("400: crear tarea con título de 2 chars")
    void tituloCortoDa400() {
        fail("TODO MP-6: title de 2 chars -> 400");
    }

    /** TODO (MP-2/MP-6): GET /projects SIN token -> 401. */
    @Test
    @DisplayName("401: GET /projects sin token")
    void sinTokenDa401() {
        fail("TODO MP-6: sin token -> 401");
    }

    /** TODO (MP-6): DOS actores. A crea un proyecto; B (otro token) intenta DELETE /projects/{id}
     *  de A -> 403 (la API sabe quién es B, por eso 403 y no 401). El test más interesante del día. */
    @Test
    @DisplayName("403: usuario B intenta borrar el proyecto de A (regla del owner)")
    void borrarProyectoAjenoDa403() {
        fail("TODO MP-6: DELETE de proyecto ajeno -> 403");
    }

    /** TODO (MP-6): GET /projects/999999 -> 404. */
    @Test
    @DisplayName("404: GET /projects/999999")
    void idInexistenteDa404() {
        fail("TODO MP-6: id inexistente -> 404");
    }
}
