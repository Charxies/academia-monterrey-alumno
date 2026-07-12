package com.taskflow.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * InfoController — el cambio canónico del PR de prueba de S3D4 (integrador, fase 2).
 *
 * Expone GET /info con {"app":"taskflow-api","version":"3.0.0-rc1"}: un endpoint PÚBLICO (sin token)
 * que el smoke test del deploy consulta para confirmar que la versión recién publicada está viva
 * ("curl http://<EC2>:8080/info"). Es minúsculo a propósito: el foco del día es el PIPELINE, no el Java.
 *
 * La versión "3.0.0-rc1" (release candidate) es la que D5 sube a "3.0.0" al taggear v3.0 — y el
 * pipeline de HOY es el que valida ese release. Se deja como constante para que el bump de D5 sea de
 * una sola línea (y para que el test sea determinista, sin depender de config externa).
 *
 * OJO seguridad: sin la línea permitAll("/info") en SecurityConfig, este endpoint hereda
 * anyRequest().authenticated() y responde 401 sin token — el curl del deploy fallaría. El test de
 * integración (200 SIN token) atrapa justo ese olvido.
 */
@RestController
@Tag(name = "Info", description = "Metadatos públicos del servicio (nombre y versión). Sin token.")
public class InfoController {

    private static final String APP = "taskflow-api";
    private static final String VERSION = "3.0.0-rc1";   // D5 lo sube a "3.0.0" al taggear v3.0

    @Operation(summary = "Nombre y versión del servicio",
            description = "Público (sin token): lo consume el smoke test del deploy tras cada release.")
    @GetMapping("/info")
    public Map<String, String> info() {
        return Map.of("app", APP, "version", VERSION);
    }
}
