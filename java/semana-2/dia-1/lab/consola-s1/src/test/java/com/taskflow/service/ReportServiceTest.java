package com.taskflow.service;

import com.taskflow.exception.TaskValidationException;
import com.taskflow.model.Priority;
import com.taskflow.model.Task;
import com.taskflow.model.TaskStatus;
import com.taskflow.repository.InMemoryTaskRepository;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * ReportServiceTest — tests de los REPORTES de D4 (STARTER: 3 tests de S1 resueltos + 2 de MP-3 con cuerpo TODO).
 *
 * Se paga la promesa de ayer: los reportes RETORNAN datos (Map/double), no imprimen. POR ESO se
 * testean sin consola — armamos un repo con tareas conocidas, llamamos al método y comparamos el
 * dato devuelto. Como ReportService depende de la INTERFAZ TaskRepository, le pasamos el repo en
 * memoria directamente.
 */
class ReportServiceTest {

    /** Arma un repo con tareas de estados conocidos: 2 DONE, 1 IN_PROGRESS, 1 TODO. */
    private InMemoryTaskRepository repoConTareas() throws TaskValidationException {
        InMemoryTaskRepository repo = new InMemoryTaskRepository();
        repo.save(new Task(null, "Hecha 1", "d", TaskStatus.DONE, Priority.HIGH, 1L, 1L, null));
        repo.save(new Task(null, "Hecha 2", "d", TaskStatus.DONE, Priority.MED, 1L, 1L, null));
        repo.save(new Task(null, "En curso", "d", TaskStatus.IN_PROGRESS, Priority.LOW, 1L, 2L, null));
        repo.save(new Task(null, "Pendiente", "d", TaskStatus.TODO, Priority.MED, 1L, null, null));
        return repo;
    }

    @Test
    void tareasPorEstado_agrupaCorrecto() throws TaskValidationException {
        ReportService reportes = new ReportService(repoConTareas());

        Map<TaskStatus, List<Task>> porEstado = reportes.tareasPorEstado();

        assertEquals(2, porEstado.get(TaskStatus.DONE).size());
        assertEquals(1, porEstado.get(TaskStatus.IN_PROGRESS).size());
        assertEquals(1, porEstado.get(TaskStatus.TODO).size());
    }

    @Test
    void porcentajeCompletadas_sinTareas_daCero() {
        // Repo VACÍO: el porcentaje debe ser 0.0, no NaN (evitamos el 0/0). Sin dividir entre cero.
        ReportService reportes = new ReportService(new InMemoryTaskRepository());
        assertEquals(0.0, reportes.porcentajeCompletadas());
    }

    @Test
    void porcentajeCompletadas_dosDeCuatro_daCincuenta() throws TaskValidationException {
        // 2 DONE de 4 = 50.0%. Ojo con la división entera de D1: el *100.0 lo arregla.
        ReportService reportes = new ReportService(repoConTareas());
        assertEquals(50.0, reportes.porcentajeCompletadas());
    }

    // ==================== MP-3: la misma pendientes(...), distinta estrategia ====================
    // OJO: hoy estos 2 tests están VACÍOS -> pasan en verde sin probar NADA (el "test mentiroso"
    // de S1D5 MP-6). Escribe el cuerpo cuando implementes TaskOrders y pendientes(Comparator).

    @Test
    void pendientes_conOrdenUrgencia_vencidasPrimero() {
        // TODO MP-3: sembrar una tarea VENCIDA (constructor de rehidratación, fecha pasada) y una
        //   HIGH no vencida; reportes.pendientes(TaskOrders.POR_URGENCIA); assertEquals que la
        //   VENCIDA quede en la posición 0.
    }

    @Test
    void pendientes_conOrdenTitulo_alfabetico() {
        // TODO MP-3: sembrar tareas con títulos desordenados (p.ej. Zeta, Alfa, Mike);
        //   reportes.pendientes(TaskOrders.POR_TITULO); assertEquals el orden alfabético esperado.
    }
}
