package com.taskflow.qa.tests;

import com.taskflow.qa.pages.LoginPage;
import com.taskflow.qa.pages.ProjectDetailPage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * TaskTest — el integrador del día: el framework POM prueba TaskFlow de punta a punta.
 *
 * Cada test crea SUS propios datos (un proyecto fresco en @BeforeEach) y LEE los ids del DOM
 * (nunca los hardcodea): el ambiente H2 es volátil y reproducible por corrida.
 *
 * Toda carga post-fetch se espera con waits explícitos; CERO esperas fijas de tiempo (sin sleep a mano).
 */
class TaskTest extends BaseTest {

    private ProjectDetailPage detalle;

    @BeforeEach
    void prepararProyecto() {
        String nombre = "Tareas QA " + System.currentTimeMillis();
        detalle = LoginPage.open(getDriver())
                .loginAs("demo", "Demo123!")
                .createProject(nombre, "Proyecto de la suite de tareas")
                .openProject(nombre);
    }

    @Test
    @DisplayName("crear tarea → nueva fila aparece sin recargar y el contador sube")
    void crearTareaAgregaFila() {
        List<Integer> antes = detalle.taskRowIds();

        detalle = detalle.openNewTaskModal()
                .title("Escribir pruebas E2E")
                .description("Cobertura del flujo de tareas")
                .priority("MED")
                .save();

        int id = detalle.waitNewRow(antes);

        assertAll("la tarea creada aparece en la tabla",
                () -> assertEquals("Escribir pruebas E2E", detalle.cellText(id, 1)),
                () -> assertEquals("1 tareas", detalle.taskCounter()));
    }

    @Test
    @DisplayName("cambiar estado TODO→IN_PROGRESS: badge actualizado + toast de éxito")
    void cambiarEstadoActualizaBadgeYToast() {
        int id = crearTarea("Refactor del login", "HIGH");

        detalle.changeStatus(id, "IN_PROGRESS");
        String toast = detalle.waitToastShown();  // llega tras el delay; el re-render ya ocurrió

        assertAll("estado cambiado",
                () -> assertEquals("Estado actualizado.", toast),
                () -> assertEquals("in-progress", detalle.statusBadge(id)));
        detalle.waitToastGone();
    }

    @Test
    @DisplayName("ordenar por prioridad con el dropdown custom: la HIGH queda primero")
    void ordenarPorPrioridadDropdownCustom() {
        crearTarea("Alpha baja", "LOW");
        crearTarea("Beta alta", "HIGH");

        detalle.sortBy("priority");

        int primero = detalle.taskRowIds().get(0);
        assertEquals("Beta alta", detalle.cellText(primero, 1),
                "con orden por prioridad (HIGH→LOW) la tarea de prioridad alta va primero");
    }

    @Test
    @DisplayName("filtrar por estado: IN_PROGRESS muestra la fila; DONE deja 0 → empty-state")
    void filtrarPorEstado() {
        int id = crearTarea("Tarea filtrable", "MED");
        detalle.changeStatus(id, "IN_PROGRESS");
        detalle.waitToastShown();
        detalle.waitToastGone();

        // Filtro que SÍ deja filas
        detalle.filterByStatus("IN_PROGRESS");
        assertTrue(detalle.taskRowIds().contains(id),
                "el filtro IN_PROGRESS debería mostrar la tarea en curso");

        // Filtro que deja 0 filas → empty-state «Sin tareas» (E10 completo)
        detalle.filterByStatus("DONE");
        assertAll("filtro sin coincidencias",
                () -> assertTrue(detalle.emptyStateVisible(),
                        "un filtro que deja 0 tareas muestra el empty-state"),
                () -> assertTrue(detalle.taskRowIds().isEmpty(),
                        "no debería haber filas visibles con el filtro DONE"));
    }

    @Test
    @DisplayName("borrar con confirmación propia: la fila desaparece + toast hasta su desaparición")
    void borrarConConfirmacion() {
        int id = crearTarea("Tarea a borrar", "LOW");

        detalle.deleteTask(id);
        assertTrue(detalle.confirmText().contains("Tarea a borrar"),
                "el diálogo debería nombrar la tarea: " + detalle.confirmText());

        detalle.confirmDelete();

        assertTrue(detalle.waitTaskRowGone(id), "la fila debería desaparecer tras confirmar");
        assertEquals("Tarea eliminada.", detalle.waitToastShown());
        detalle.waitToastGone();  // E8: visibility → invisibility
    }

    @Test
    @DisplayName("cierre del flujo: volver con «← Proyectos» y hacer logout")
    void volverYLogout() {
        crearTarea("Cualquier tarea", "MED");

        boolean deVueltaEnLogin = detalle
                .backToProjects()      // link «← Proyectos» (hueco → By.linkText)
                .logout()              // btn-logout → de vuelta al login
                .getClass() == com.taskflow.qa.pages.LoginPage.class;

        assertTrue(deVueltaEnLogin, "logout debería dejarnos en la página de login");
        assertFalse(getDriver().getCurrentUrl().contains("projects.html"),
                "ya no deberíamos estar en la lista de proyectos");
    }

    // ------------------------------------------------------------------
    // Helper: crea una tarea y devuelve su id (leído del DOM). Reasigna 'detalle' al page
    // object que devuelve save().
    // ------------------------------------------------------------------
    private int crearTarea(String titulo, String prioridad) {
        List<Integer> antes = detalle.taskRowIds();
        detalle = detalle.openNewTaskModal()
                .title(titulo)
                .priority(prioridad)
                .save();
        int id = detalle.waitNewRow(antes);
        // Cerrar el toast de creación ANTES de la siguiente mutación: un toast vivo (3 s)
        // podría confundirse con el de la acción que sigue (E8: encadenar mutaciones limpia).
        detalle.waitToastGone();
        return id;
    }
}
