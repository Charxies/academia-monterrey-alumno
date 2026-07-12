package com.taskflow.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

// TODO MP-9: imports que necesitarás al escribir el viaje (hoy comentados para que el esqueleto compile):
// import com.jayway.jsonpath.JsonPath;
// import org.springframework.http.MediaType;
// import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * FlujoCompletoE2ETest — ESQUELETO del test de integración de VERDAD (S3D1, MP-9). @SpringBootTest levanta
 * TODO el contexto; UN test-viaje automatiza la demo de pareja del viernes PARA SIEMPRE. Seguridad,
 * advice, JPA y dominio probados JUNTOS una vez — esto es lo que el slice ya no necesita repetir.
 *
 * Regla (que QE S4 elevará a ley de CI): el test CREA SUS PROPIOS DATOS (register de un usuario nuevo),
 * NO depende de la semilla. Corre sobre H2 en memoria (perfil test); @Transactional -> rollback.
 *
 * Hoy el cuerpo está en TODO: el esqueleto arranca el contexto y pasa "verde vacío" (¿lo notas? es el
 * fantasma del test mentiroso de S1D5 — un verde que no verifica nada). MP-9 lo llena.
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class FlujoCompletoE2ETest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void viajeCompleto_registerLoginCrearCompletarYVerificar() throws Exception {
        // TODO MP-9 — el viaje completo, paso a paso (cada paso alimenta al siguiente):
        //   1) POST /auth/register {username, email, password} -> 201; extraer $.id (será el assignee).
        //   2) POST /auth/login    {username, password}        -> 200; extraer $.token (JsonPath.read).
        //   3) POST /projects  (header "Authorization: Bearer <token>") -> 201; extraer $.id del proyecto.
        //   4) POST /projects/{projectId}/tasks  (assigneeId = el id del paso 1, dueDate futura) -> 201;
        //      extraer $.id de la tarea; asertar $.status == "TODO".
        //   5) PATCH /tasks/{taskId}/status  {"status":"DONE"} -> 200; asertar $.status == "DONE".
        //   6) GET /projects/{projectId}/tasks -> 200; asertar que la tarea aparece en DONE.
    }
}
