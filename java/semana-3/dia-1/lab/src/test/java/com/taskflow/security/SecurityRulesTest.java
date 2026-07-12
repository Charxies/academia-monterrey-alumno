package com.taskflow.security;

import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * SecurityRulesTest — los tests de la CAPA de hoy (seguridad), el corazón del Integrador S2. Patrón
 * CANÓNICO: TOKEN REAL generado en el setup (login real vía MockMvc contra los usuarios semilla) con
 * el helper tokenDe(username, password). NO se usa @WithMockUser porque brincaría el filtro JWT, que
 * es justo el artefacto que se construyó hoy.
 *
 * Cierra la tabla 401 vs 403 con evidencia:
 *   - sin token           -> 401 (no sé quién eres)
 *   - con token válido     -> 200 (te reconozco)
 *   - luis (no owner)      -> 403 (sé quién eres y NO puedes)   <- ¿por qué 403 y no 401? porque la API SÍ sabe quién es luis
 *   - ana (owner) / admin  -> 204
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class SecurityRulesTest {

    @Autowired
    private MockMvc mockMvc;

    /** Helper PROVISTO: login real contra un usuario semilla; devuelve su JWT. */
    private String tokenDe(String username, String password) throws Exception {
        String body = """
                { "username": "%s", "password": "%s" }
                """.formatted(username, password);
        String json = mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        return JsonPath.read(json, "$.token");
    }

    @Test
    void getProjects_sinToken_devuelve401() throws Exception {
        mockMvc.perform(get("/projects"))
                .andExpect(status().isUnauthorized());
    }

    // TODO integrador (paso 2, refactor a): convertir el test de arriba en un @ParameterizedTest que
    //   cubra VARIAS rutas protegidas sin token -> 401. Añade los imports
    //     import org.junit.jupiter.params.ParameterizedTest;
    //     import org.junit.jupiter.params.provider.ValueSource;
    //   y reemplaza por:
    //     @ParameterizedTest(name = "GET {0} sin token → 401")
    //     @ValueSource(strings = { "/projects", "/tasks", "/projects/1/tasks" })
    //     void rutaProtegida_sinToken_devuelve401(String ruta) throws Exception {
    //         mockMvc.perform(get(ruta)).andExpect(status().isUnauthorized());
    //     }
    //   (Anti-#3: velo fallar una vez metiendo una ruta PÚBLICA como "/auth/login" y quítala.)

    @Test
    void getProjects_conTokenDeAna_devuelve200() throws Exception {
        String token = tokenDe("ana", "ana123");
        mockMvc.perform(get("/projects").header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }

    @Test
    void deleteProject_comoLuisNoOwner_devuelve403() throws Exception {
        // luis (USER) no es owner del proyecto 1 (es de ana) ni ADMIN -> 403 (NO 401: la API sabe quién es).
        String token = tokenDe("luis", "luis123");
        mockMvc.perform(delete("/projects/1").header("Authorization", "Bearer " + token))
                .andExpect(status().isForbidden());
    }

    @Test
    void deleteProject_comoAnaOwner_devuelve204() throws Exception {
        // ana ES la owner del proyecto 1 -> la rama @projectSecurity.esOwner del @PreAuthorize pasa.
        String token = tokenDe("ana", "ana123");
        mockMvc.perform(delete("/projects/1").header("Authorization", "Bearer " + token))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteProject_comoAdmin_devuelve204() throws Exception {
        // admin (ADMIN) borra cualquier proyecto -> la rama hasRole('ADMIN') pasa.
        String token = tokenDe("admin", "admin123");
        mockMvc.perform(delete("/projects/1").header("Authorization", "Bearer " + token))
                .andExpect(status().isNoContent());
    }

    /**
     * STRETCH — token MANIPULADO: se altera un carácter de la firma. El parser lanza DENTRO del filtro
     * JWT; el try/catch del filtro responde 401 (no 500). Prueba de que el Error intencional 3 quedó bien.
     */
    @Test
    void getProjects_conTokenManipulado_devuelve401() throws Exception {
        String token = tokenDe("ana", "ana123");
        // Alteramos el PRIMER carácter de la firma (parts[2]): cambia los primeros bits reales de la
        // firma -> ya no cuadra con el payload -> el parser lanza -> el filtro responde 401.
        String[] parts = token.split("\\.");
        char[] firma = parts[2].toCharArray();
        firma[0] = (firma[0] == 'A') ? 'B' : 'A';
        String manipulado = parts[0] + "." + parts[1] + "." + new String(firma);
        mockMvc.perform(get("/projects").header("Authorization", "Bearer " + manipulado))
                .andExpect(status().isUnauthorized());
    }
}
