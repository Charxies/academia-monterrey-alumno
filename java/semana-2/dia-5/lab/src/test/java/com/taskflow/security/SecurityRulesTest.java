package com.taskflow.security;

import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * SecurityRulesTest — los tests de la CAPA de hoy (seguridad), el corazón del Integrador S2. Patrón
 * CANÓNICO: TOKEN REAL generado en el setup (login real vía MockMvc contra los usuarios semilla) con
 * el helper tokenDe(username, password), que ya viene PROVISTO abajo. NO uses @WithMockUser: brincaría
 * el filtro JWT, que es justo el artefacto que construyes hoy.
 *
 * Los tests están @Disabled hasta que el login funcione (MP-5/MP-7) y las reglas estén (MP-8/MP-9):
 * quita el @Disabled a medida que avances. Mínimo 4 (cierran la tabla 401 vs 403):
 *   - GET /projects sin token -> 401
 *   - GET /projects con token de ana -> 200
 *   - DELETE /projects/{id} como luis (no owner) -> 403
 *   - DELETE /projects/{id} como admin -> 204
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class SecurityRulesTest {

    @Autowired
    private MockMvc mockMvc;

    /** Helper PROVISTO: login real contra un usuario semilla; devuelve su JWT. Úsalo en cada test. */
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

    @Disabled("TODO Integrador: implementar tras MP-8")
    @Test
    void getProjects_sinToken_devuelve401() throws Exception {
        // TODO: GET /projects sin header Authorization -> 401.
    }

    @Disabled("TODO Integrador: implementar tras MP-8")
    @Test
    void getProjects_conTokenDeAna_devuelve200() throws Exception {
        // TODO: String token = tokenDe("ana", "ana123");
        //       GET /projects con header "Authorization: Bearer " + token -> 200.
    }

    @Disabled("TODO Integrador: implementar tras MP-9")
    @Test
    void deleteProject_comoLuisNoOwner_devuelve403() throws Exception {
        // TODO: token de luis; DELETE /projects/1 -> 403 (luis no es owner ni admin).
    }

    @Disabled("TODO Integrador: implementar tras MP-9")
    @Test
    void deleteProject_comoAdmin_devuelve204() throws Exception {
        // TODO: token de admin; DELETE /projects/1 -> 204.
    }
}
