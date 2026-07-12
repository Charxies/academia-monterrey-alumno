package com.taskflow.qa.tests;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.fail;

/**
 * TaskTest (integrador, fase 1) — el framework POM prueba TaskFlow de punta a punta.
 *
 * Cada test crea SUS propios datos (un proyecto fresco en @BeforeEach) y LEE los ids del DOM
 * (nunca los hardcodea): H2 es volátil y reproducible por corrida. Cero esperas fijas de tiempo (sin sleep a mano).
 *
 * Sugerencia: un @BeforeEach que hace login + createProject + openProject y deja un
 * ProjectDetailPage listo; luego un helper crearTarea(titulo, prioridad) que devuelve el id
 * (waitNewRow). Sobre eso, cubre: crear tarea, cambiar estado (badge + toast), ordenar con el
 * dropdown custom, filtrar (incluido el empty-state con filtro que deja 0), borrar con
 * confirmación (toast hasta su desaparición) y el cierre: «← Proyectos» + logout.
 */
class TaskTest extends BaseTest {

    @Test
    @DisplayName("crear tarea → nueva fila aparece sin recargar y el contador sube")
    void crearTareaAgregaFila() {
        // TODO integrador: crear tarea vía TaskModal → waitNewRow → assert cellText + taskCounter
        fail("TODO integrador: implementar crearTareaAgregaFila");
    }

    @Test
    @DisplayName("cambiar estado TODO→IN_PROGRESS: badge actualizado + toast de éxito")
    void cambiarEstadoActualizaBadgeYToast() {
        // TODO integrador: changeStatus(id, "IN_PROGRESS") → waitToastShown "Estado actualizado."
        //                  → statusBadge(id) == "in-progress" → waitToastGone
        fail("TODO integrador: implementar cambiarEstadoActualizaBadgeYToast");
    }

    @Test
    @DisplayName("ordenar por prioridad con el dropdown custom: la HIGH queda primero")
    void ordenarPorPrioridadDropdownCustom() {
        // TODO integrador: crear 2 tareas (LOW y HIGH) → sortBy("priority") → primer título == la HIGH
        fail("TODO integrador: implementar ordenarPorPrioridadDropdownCustom");
    }

    @Test
    @DisplayName("filtrar por estado: IN_PROGRESS muestra la fila; DONE deja 0 → empty-state")
    void filtrarPorEstado() {
        // TODO integrador: filterByStatus("IN_PROGRESS") muestra la fila; filterByStatus("DONE")
        //                  → emptyStateVisible() (E10 completo: orden Y filtro)
        fail("TODO integrador: implementar filtrarPorEstado");
    }

    @Test
    @DisplayName("borrar con confirmación propia: la fila desaparece + toast hasta su desaparición")
    void borrarConConfirmacion() {
        // TODO integrador: deleteTask(id) → confirmText contiene el título → confirmDelete()
        //                  → waitTaskRowGone(id) → toast "Tarea eliminada." → waitToastGone (E8)
        fail("TODO integrador: implementar borrarConConfirmacion");
    }

    @Test
    @DisplayName("cierre del flujo: volver con «← Proyectos» y hacer logout")
    void volverYLogout() {
        // TODO integrador: backToProjects() → logout() → assert de vuelta en el login
        fail("TODO integrador: implementar volverYLogout");
    }
}
