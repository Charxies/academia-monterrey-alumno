package com.taskflow.demooauth2;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Muestra el principal que Google nos devolvió tras el callback. El punto del demo: el 'quién eres'
 * NO lo resolvimos nosotros — llega firmado por Google dentro del id_token (OIDC). Aquí solo LEEMOS
 * sus claims (sub, name, email). Ninguna línea de criptografía ni de BD de usuarios de nuestro lado.
 */
@RestController
public class HomeController {

    @GetMapping("/")
    @ResponseBody
    public Map<String, Object> home(@AuthenticationPrincipal OAuth2User principal) {
        // Estos claims vienen de Google (authorization server), no de nuestra tabla users.
        return Map.of(
                "mensaje", "Autenticado vía Google (OAuth2/OIDC). La identidad la emitió Google, no nosotros.",
                "sub", String.valueOf(principal.getAttribute("sub")),
                "nombre", String.valueOf(principal.getAttribute("name")),
                "email", String.valueOf(principal.getAttribute("email"))
        );
    }
}
