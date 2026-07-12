package com.taskflow.qa.tests;

import com.taskflow.qa.pages.LoginPage;
import com.taskflow.qa.pages.ProjectsPage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * ProjectTest — crear un proyecto por UI.
 *
 * Login por UI en el @BeforeEach (independencia por clase; trade-off discutido: inyectar el
 * token en localStorage sería más rápido pero salta el flujo real — se retoma en D5).
 *
 * Nombre ÚNICO por timestamp: el test es re-ejecutable sin resetear la BD (H2 acumula).
 */
class ProjectTest extends BaseTest {

    private ProjectsPage projects;

    @BeforeEach
    void iniciarSesion() {
        projects = LoginPage.open(getDriver()).loginAs("demo", "Demo123!");
    }

    @Test
    @DisplayName("crear proyecto: la card aparece sin recargar + toast de éxito")
    void crearProyecto() {
        String nombre = "QA " + System.currentTimeMillis();

        projects.createProject(nombre, "Proyecto creado por la suite de QE");
        String toast = projects.waitToastShown();

        assertAll("proyecto creado",
                () -> assertTrue(projects.projectNames().contains(nombre),
                        "la card nueva debería aparecer sin recargar: " + projects.projectNames()),
                () -> assertEquals("Proyecto creado con éxito.", toast));
    }
}
