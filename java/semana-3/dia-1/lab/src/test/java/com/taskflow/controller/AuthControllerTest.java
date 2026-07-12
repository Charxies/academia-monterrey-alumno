package com.taskflow.controller;

import com.taskflow.model.User;
import com.taskflow.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.matchesPattern;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * AuthControllerTest — la capa que se construyó HOY (MP-6): register y login. @SpringBootTest +
 * MockMvc, perfil test (H2 en memoria). @Transactional -> rollback por test (el register no ensucia
 * la BD de los otros tests). El endurecimiento del token (2 puntos) llegó en MP-7, cuando murió el stub.
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Test
    void register_usuarioNuevo_devuelve201HasheaYNoDevuelvePassword() throws Exception {
        String body = """
                {
                  "username": "carla",
                  "email": "carla@taskflow.dev",
                  "password": "carla123"
                }
                """;

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value("carla"))
                .andExpect(jsonPath("$.role").value("USER"))
                // La respuesta (UserResponse) NO contiene passwordHash: el campo no existe en el JSON.
                .andExpect(jsonPath("$.passwordHash").doesNotExist());

        // En la BD, el password quedó HASHEADO (BCrypt): prefijo $2a$/$2b$ y != al plano.
        User guardado = userRepository.findByUsername("carla").orElseThrow();
        assertThat(guardado.getPasswordHash()).startsWith("$2");
        assertThat(guardado.getPasswordHash()).isNotEqualTo("carla123");
    }

    @Test
    void register_usernameDuplicado_devuelve409() throws Exception {
        // 'ana' ya está sembrado.
        String body = """
                {
                  "username": "ana",
                  "email": "otra-ana@taskflow.dev",
                  "password": "otro-pass"
                }
                """;

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409));
    }

    @Test
    void login_credencialesValidas_devuelve200ConToken() throws Exception {
        String body = """
                { "username": "ana", "password": "ana123" }
                """;

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                // Token real: cadena no vacía con 2 puntos (header.payload.signature).
                .andExpect(jsonPath("$.token").value(matchesPattern("[^.]+\\.[^.]+\\.[^.]+")));
    }

    @Test
    void login_passwordMalo_devuelve401() throws Exception {
        String body = """
                { "username": "ana", "password": "password-incorrecto" }
                """;

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status").value(401));
    }
}
