package com.taskflow.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;

/**
 * SecurityConfig — la configuración de seguridad del día. HOY llega como ESQUELETO: los beans de
 * apoyo (PasswordEncoder, AuthenticationManager, entry point 401 JSON) vienen PROVISTOS; TÚ escribes
 * la SecurityFilterChain (MP-2 en la mañana, MP-8 en la tarde).
 *
 * MIENTRAS no definas una SecurityFilterChain, Spring Security aplica su chain POR DEFECTO: bloquea
 * TODO (401 universal). Ese es el lockdown que vives al primer arranque (MP-1). Al añadir el @Bean de
 * abajo, tu chain sustituye a la de Boot.
 *
 * @EnableMethodSecurity habilita @PreAuthorize (la regla de owner del DELETE de proyectos, MP-9).
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    // El filtro JWT ya es un @Component; para MP-8 inyéctalo aquí y regístralo con addFilterBefore.
    // (Descomenta cuando escribas la SecurityFilterChain.)
    // private final JwtAuthenticationFilter jwtAuthenticationFilter;
    // public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
    //     this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    // }

    /*
     * TODO MP-2 (mañana) — escribe el @Bean SecurityFilterChain con lambda DSL:
     *   @Bean
     *   public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
     *       http
     *         .csrf(csrf -> csrf.disable())   // Error intencional 1: sin esto, POST válido -> 403 (CSRF)
     *         .authorizeHttpRequests(auth -> auth
     *             .requestMatchers("/auth/**").permitAll()
     *             .requestMatchers("/swagger-ui/**", "/swagger-ui.html", "/v3/api-docs/**").permitAll()
     *             .requestMatchers("/h2-console/**").permitAll()
     *             .anyRequest().authenticated())   // ¡SIEMPRE al final! (punto de dolor 1)
     *         .httpBasic(Customizer.withDefaults());   // TEMPORAL: esta tarde lo sustituye el filtro JWT
     *       return http.build();
     *   }
     *
     * TODO MP-8 (tarde) — evoluciona el chain a JWT stateless:
     *   - .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
     *   - .exceptionHandling(eh -> eh
                        .authenticationEntryPoint(restAuthenticationEntryPoint())
                        .accessDeniedHandler(restAccessDeniedHandler()))  // 401 JSON
     *   - .headers(h -> h.frameOptions(f -> f.sameOrigin()))   // para la consola H2 (usa frames)
     *   - .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
     *   - ELIMINA httpBasic (fue andamiaje de la mañana)
     */

    /** PROVISTO — BCrypt: salt automático + factor de costo. El mismo password da hashes DISTINTOS -> matches(). */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * PROVISTO — AuthenticationManager (se lee, no se memoriza). Spring lo arma con los beans presentes
     * (tu JpaUserDetailsService + PasswordEncoder) en un DaoAuthenticationProvider. Lo usa
     * AuthService.login para verificar credenciales.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    /**
     * PROVISTO — entry point 401 JSON: sin él, el anónimo recibe 403 y la tabla canónica 401 vs 403 se
     * rompe. Lo conectas en el exceptionHandling del chain (MP-8).
     */
    @Bean
    public AuthenticationEntryPoint restAuthenticationEntryPoint() {
        return (request, response, authException) -> {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.getWriter().write("{\"status\":401,\"message\":\"No autenticado: falta un token válido.\"}");
        };
    }

    /**
     * Access denied handler PROVISTO: 403 JSON cuando un usuario AUTENTICADO no tiene permiso
     * (la regla @PreAuthorize del owner al borrar un proyecto ajeno). Distinto del 401.
     */
    @Bean
    public AccessDeniedHandler restAccessDeniedHandler() {
        return (request, response, ex) -> {
            response.setStatus(HttpStatus.FORBIDDEN.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.getWriter().write("{\"status\":403,\"message\":\"Sin permiso para esta operación.\"}");
        };
    }
}
