package com.taskflow.demooauth2;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * SecurityConfig del demo — el CONTRASTE con el taskflow-api del día:
 *   - taskflow-api: nosotros autenticamos (tabla users + BCrypt) y firmamos el JWT (JwtService).
 *   - aquí: NO hay tabla de usuarios ni JwtService; oauth2Login() delega TODO el "¿quién eres?" a
 *     Google (authorization server). Nuestra app es solo el 'client' que recibe el resultado.
 *
 * Con una sola 'registration' (google), la página /login autogenerada muestra el enlace
 * "Login with Google"; al pulsarlo se dispara el flujo: redirect a Google -> pantalla de
 * consentimiento -> callback a /login/oauth2/code/google -> principal con los claims de Google.
 */
@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        // Todo exige estar logueado: al entrar sin sesión, Spring manda a /login
                        // (la página con el botón de Google).
                        .anyRequest().authenticated())
                // El flujo OAuth2/OIDC completo con la configuración por defecto.
                .oauth2Login(Customizer.withDefaults());
        return http.build();
    }
}
