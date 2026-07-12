package com.taskflow.controller;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * AuthControllerTest — la capa que se construye HOY (MP-6): register y login. @SpringBootTest +
 * MockMvc, perfil test (H2 en memoria). @Transactional -> rollback por test.
 *
 * Los tests están @Disabled hasta que implementes register/login (MP-5) y el token real (MP-7): quita
 * el @Disabled a medida que cada MP quede listo. Puedes autoinyectar UserRepository para verificar el
 * hash en la BD (una vez que User tenga getPasswordHash).
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Disabled("TODO MP-6: implementar tras MP-5")
    @Test
    void register_usuarioNuevo_devuelve201HasheaYNoDevuelvePassword() throws Exception {
        // TODO MP-6: POST /auth/register con un usuario nuevo (p.ej. "carla"):
        //   - status 201
        //   - jsonPath("$.username").value("carla")
        //   - jsonPath("$.passwordHash").doesNotExist()   // la respuesta NO trae el hash
        //   - en la BD: userRepository.findByUsername("carla").getPasswordHash() empieza con "$2"
    }

    @Disabled("TODO MP-6: implementar tras MP-5")
    @Test
    void register_usernameDuplicado_devuelve409() throws Exception {
        // TODO MP-6: POST /auth/register con "ana" (ya sembrado) -> 409.
    }

    @Disabled("TODO MP-6: implementar tras MP-5")
    @Test
    void login_credencialesValidas_devuelve200ConToken() throws Exception {
        // TODO MP-6: POST /auth/login ana/ana123 -> 200. Tras MP-7 endurece: el token no vacío con 2 puntos.
    }

    @Disabled("TODO MP-6: implementar tras MP-5")
    @Test
    void login_passwordMalo_devuelve401() throws Exception {
        // TODO MP-6: POST /auth/login ana/password-incorrecto -> 401.
    }
}
