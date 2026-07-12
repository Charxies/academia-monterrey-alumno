package com.taskflow;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/**
 * TaskflowApiApplicationTests — el test de humo del cableo. Hoy también verifica que el contexto JPA
 * arranca: entidades mapeadas, EntityManagerFactory construido, los tres JpaRepository como proxies.
 *
 * @ActiveProfiles("test"): usa la H2 EN MEMORIA del perfil test — NO escribe en data/taskflow.mv.db
 * (regla de S1D5: los tests no tocan los datos reales).
 */
@SpringBootTest
@ActiveProfiles("test")
class TaskflowApiApplicationTests {

    @Test
    void contextLoads() {
    }
}
