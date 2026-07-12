package com.taskflow.qa.api;

import com.taskflow.qa.utils.Config;
import io.restassured.specification.RequestSpecification;

/**
 * ApiSpecs — las specs REUTILIZABLES de RestAssured (T5, MP-7). DRY del framework híbrido: baseUri,
 * Content-Type y auth comunes se declaran UNA sola vez aquí, no en cada test.
 *
 * La base URL DEBE salir de {@link Config} (Config.baseUrl()) — la MISMA fuente que la capa UI de D4.
 * NO crees una clase de config nueva ni hardcodees "http://localhost:8080".
 *
 * PISTA: usa io.restassured.builder.RequestSpecBuilder + io.restassured.http.ContentType.JSON.
 * Recuerda contentType(JSON) en base() (si falta en un POST/PUT -> 415 o 400 "raro": dolor 11).
 */
public final class ApiSpecs {

    private ApiSpecs() {
        // Clase de utilería: no se instancia.
    }

    /** TODO (MP-7): spec base con setBaseUri(Config.baseUrl()) + setContentType(ContentType.JSON). */
    public static RequestSpecification base() {
        // TODO: construir y devolver la RequestSpecification base con RequestSpecBuilder.
        return null;
    }

    /** TODO (MP-7): base() + addHeader("Authorization", "Bearer " + token). */
    public static RequestSpecification conToken(String token) {
        // TODO: partir de base() (addRequestSpecification) y agregar el header Authorization.
        return null;
    }
}
