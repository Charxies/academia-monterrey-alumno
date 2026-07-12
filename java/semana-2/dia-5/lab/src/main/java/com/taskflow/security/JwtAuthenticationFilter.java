package com.taskflow.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * JwtAuthenticationFilter — la LÓGICA CENTRAL DEL DÍA (MP-8). Corre UNA vez por request
 * (OncePerRequestFilter) ANTES del UsernamePasswordAuthenticationFilter: si el request trae un
 * "Bearer <token>" válido, autentica al usuario en el SecurityContext para que el resto de la cadena
 * (autorización por URL y @PreAuthorize) lo vea.
 *
 * Contraste clave (Error intencional 3): este filtro corre ANTES del DispatcherServlet, así que el
 * @ControllerAdvice de D3 NO ve las excepciones que aquí se lancen. Por eso un token corrupto se
 * atrapa AQUÍ con try/catch y se responde 401 a mano — si no, el parser lanzaría y el usuario vería
 * un 500 en vez del 401 de la tabla canónica.
 *
 * Tienes inyectados jwtService y userDetailsService: úsalos.
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    public JwtAuthenticationFilter(JwtService jwtService, UserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {
        // TODO MP-8 (1): leer el header "Authorization". Si es null o NO empieza con "Bearer " ->
        //                chain.doFilter(request, response) y RETURN (request anónimo; sin este guard,
        //                el substring daría NPE -> 500 en los endpoints públicos).
        // TODO MP-8 (2): quitar el prefijo "Bearer " (substring(7)) para quedarte con el token.
        // TODO MP-8 (3): jwtService.extractUsername(token). Si hay username y el SecurityContext aún
        //                no tiene Authentication, cargar el UserDetails con userDetailsService.
        // TODO MP-8 (4): si jwtService.isTokenValid(token, userDetails), construir un
        //                UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities())
        //                y ponerlo en SecurityContextHolder.getContext().setAuthentication(...).
        // TODO Error intencional 3: envuelve los pasos 2-4 en try/catch (JwtException |
        //                UsernameNotFoundException). En el catch: limpia el contexto, responde 401 JSON
        //                (setStatus(401), setContentType JSON, write) y RETURN (no sigas la cadena).

        // Placeholder para que el proyecto COMPILE y arranque hoy (lockdown por defecto de Spring
        // Security): por ahora el filtro solo deja pasar. Al completar MP-8, elimina esta línea y
        // enruta la cadena según la lógica de arriba.
        chain.doFilter(request, response);
    }
}
