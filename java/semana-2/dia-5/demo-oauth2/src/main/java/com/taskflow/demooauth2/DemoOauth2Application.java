package com.taskflow.demooauth2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Punto de entrada del demo de awareness OAuth2/OIDC (SOLO instructor).
 *
 * Arranca en :8081 (ver application.yml) para no chocar con el taskflow-api (:8080). El mensaje del
 * demo: HOY en el capstone USTEDES emitieron el token (JwtService propio + tabla users); aquí, con
 * OAuth2, se DELEGA la identidad a un proveedor externo (Google). OIDC = capa de identidad sobre OAuth2.
 */
@SpringBootApplication
public class DemoOauth2Application {

    public static void main(String[] args) {
        SpringApplication.run(DemoOauth2Application.class, args);
    }
}
