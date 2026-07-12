package com.taskflow.qa.tests;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.fail;

/**
 * ProjectTest (integrador, fase 1) — crear un proyecto por UI.
 *
 * Login por UI en el @BeforeEach (independencia por clase). Nombre ÚNICO por timestamp
 * ("QA " + System.currentTimeMillis()): re-ejecutable sin resetear la BD (H2 acumula).
 */
class ProjectTest extends BaseTest {

    @Test
    @DisplayName("crear proyecto: la card aparece sin recargar + toast de éxito")
    void crearProyecto() {
        // TODO integrador:
        //   ProjectsPage projects = LoginPage.open(getDriver()).loginAs("demo", "Demo123!");
        //   String nombre = "QA " + System.currentTimeMillis();
        //   projects.createProject(nombre, "...");
        //   String toast = projects.waitToastShown();
        //   assertTrue(projects.projectNames().contains(nombre));
        //   assertEquals("Proyecto creado con éxito.", toast);
        fail("TODO integrador: implementar crearProyecto");
    }
}
