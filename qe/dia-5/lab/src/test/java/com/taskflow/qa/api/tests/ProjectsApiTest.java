package com.taskflow.qa.api.tests;

import com.taskflow.qa.api.ApiSpecs;
import com.taskflow.qa.api.AuthClient;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasItem;
import static org.junit.jupiter.api.Assertions.fail;

// TODO (MP-8): etiqueta esta clase con @Tag("ci").

/**
 * ProjectsApiTest — CRUD de proyectos ENCADENADO (MP-5): create -> extraer id -> read -> update(PUT)
 * -> delete -> 404. El patrón real de API testing: la salida de un request alimenta al siguiente.
 *
 * OJO: abajo hay un test-TRAMPA (ERR-1). Corre verde en tu máquina y ROJO en CI. Antes de "arreglarlo",
 * ENTIENDE por qué falla (lo diagnosticas leyendo el log del run en MP-8).
 */
class ProjectsApiTest {

    /** TODO (MP-4/MP-5): obtén un token FRESCO con AuthClient (idealmente en un @BeforeAll estático,
     *  para no generarlo una vez global y que expire a media suite: ERR-2). */

    /** TODO (MP-5): el encadenamiento completo:
     *   POST /projects (201, extrae el id con extract().jsonPath().getLong("id")) ->
     *   GET /projects/{id} (200, name) -> PUT /projects/{id} (200, name NUEVO) ->
     *   DELETE /projects/{id} (204) -> GET /projects/{id} (404). */
    @Test
    @DisplayName("CRUD proyecto encadenado: create -> read -> update(PUT) -> delete -> 404")
    void crudProyectoEncadenado() {
        fail("TODO MP-5: CRUD encadenado de proyecto");
    }

    /** TODO (MP-5): GET /projects -> 200; la lista contiene el proyecto que ESTE test creó
     *  (body("name", hasItem(...))). No asumas proyectos previos. */
    @Test
    @DisplayName("GET /projects: la lista contiene mi proyecto recién creado")
    void listaContieneMiProyecto() {
        fail("TODO MP-5: crear y luego encontrar mi propio proyecto en la lista");
    }

    /**
     * ERR-1 (test-trampa). ¿Por qué este test es una trampa?
     *
     * Da por hecho DATOS PREVIOS: el proyecto «Rediseño del sitio» de la semilla de la semana
     * (seed.sh) que vive en TU H2/Postgres local. En tu máquina pasa VERDE. En CI la API nace con
     * una H2 limpia (solo la semilla mínima del DataSeeder, que crea OTROS proyectos y NO a demo) ->
     * el proyecto no está -> ROJO. Es el clásico "verde local, rojo en CI por datos".
     *
     * En MP-8: NO lo borres a ciegas. Léelo, diagnostica el log del run, y RE-ESCRÍBELO para que
     * cree su propio proyecto y asserte sobre él (así funciona en cualquier ambiente). La solución
     * muestra la versión corregida.
     */
    @Test
    @DisplayName("(ERR-1 trampa) asume el proyecto semilla «Rediseño del sitio»")
    void trampaAsumeProyectoSemilla() {
        AuthClient auth = new AuthClient();
        String token = auth.tokenNuevo();   // el auth SÍ funciona (usuario propio); lo que falla son los DATOS
        given()
                .spec(ApiSpecs.conToken(token))
        .when()
                .get("/projects")
        .then()
                .log().ifValidationFails()
                .statusCode(200)
                .body("name", hasItem("Rediseño del sitio"));   // <- la suposición que truena en CI
    }
}
