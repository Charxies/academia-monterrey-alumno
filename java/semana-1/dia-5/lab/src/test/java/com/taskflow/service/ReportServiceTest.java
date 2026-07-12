package com.taskflow.service;

import org.junit.jupiter.api.Test;

/**
 * ReportServiceTest — tests de los REPORTES (ESQUELETO, MP-7). Paga la promesa de D4: los reportes
 * RETORNAN datos, POR ESO se testean sin consola.
 *
 * Arma un repo (InMemoryTaskRepository) con tareas conocidas, pásalo a new ReportService(repo) y
 * compara el dato devuelto. Cuerpos VACÍOS = verde mentiroso (MP-6). Llénalos.
 */
class ReportServiceTest {

    @Test
    void tareasPorEstado_agrupaCorrecto() {
        // TODO MP-7: repo con 2 DONE + 1 IN_PROGRESS + 1 TODO; tareasPorEstado().get(DONE).size() == 2, etc.
    }

    @Test
    void porcentajeCompletadas_sinTareas_daCero() {
        // TODO MP-7: repo VACÍO -> assertEquals(0.0, reportes.porcentajeCompletadas()). Sin dividir entre cero (NaN).
    }
}
