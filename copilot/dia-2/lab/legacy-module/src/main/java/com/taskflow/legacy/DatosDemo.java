package com.taskflow.legacy;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Fabrica de datos de demostracion para el reporte semanal.
 *
 * <p>Semana objetivo: lunes 2026-03-02 a domingo 2026-03-08.
 * Los ultimos dos registros caen FUERA de esa semana (ruido) y no deberian
 * aparecer en el reporte.
 */
public final class DatosDemo {

    private DatosDemo() {
    }

    public static List<RegistroTarea> registros() {
        List<RegistroTarea> lista = new ArrayList<>();

        // --- taskflow-api ---
        lista.add(new RegistroTarea(101, "taskflow-api", "ana",   1, 120, 110, LocalDate.of(2026, 3, 2)));
        lista.add(new RegistroTarea(102, "taskflow-api", "ana",   2,  60,  75, LocalDate.of(2026, 3, 2)));
        lista.add(new RegistroTarea(103, "taskflow-api", "bruno", 1,  90,  90, LocalDate.of(2026, 3, 3)));
        lista.add(new RegistroTarea(104, "taskflow-api", "bruno", 3,  30,  25, LocalDate.of(2026, 3, 3)));
        lista.add(new RegistroTarea(105, "taskflow-api", "ana",   2,  45,  60, LocalDate.of(2026, 3, 4)));

        // --- taskflow-qa ---
        lista.add(new RegistroTarea(106, "taskflow-qa", "caro",  1, 150, 140, LocalDate.of(2026, 3, 2)));
        lista.add(new RegistroTarea(107, "taskflow-qa", "caro",  2,  80,  95, LocalDate.of(2026, 3, 4)));
        lista.add(new RegistroTarea(108, "taskflow-qa", "diego", 3,  40,  50, LocalDate.of(2026, 3, 5)));
        lista.add(new RegistroTarea(109, "taskflow-qa", "caro",  1, 100, 100, LocalDate.of(2026, 3, 6)));
        lista.add(new RegistroTarea(110, "taskflow-qa", "diego", 2,  70,  60, LocalDate.of(2026, 3, 6)));

        // --- infra ---
        lista.add(new RegistroTarea(111, "infra", "bruno", 1, 200, 240, LocalDate.of(2026, 3, 3)));
        lista.add(new RegistroTarea(112, "infra", "diego", 2,  50,  55, LocalDate.of(2026, 3, 5)));
        lista.add(new RegistroTarea(113, "infra", "ana",   3,  25,  30, LocalDate.of(2026, 3, 7)));
        lista.add(new RegistroTarea(114, "infra", "bruno", 1, 180, 170, LocalDate.of(2026, 3, 7)));

        // Unico registro terminado el DOMINGO 2026-03-08 (ultimo dia de la semana).
        lista.add(new RegistroTarea(115, "taskflow-api", "ana", 1, 90, 85, LocalDate.of(2026, 3, 8)));

        // --- ruido: fuera de la semana objetivo ---
        lista.add(new RegistroTarea(201, "taskflow-api", "ana",  1, 100, 100, LocalDate.of(2026, 2, 27)));
        lista.add(new RegistroTarea(202, "taskflow-qa",  "caro", 2,  60,  60, LocalDate.of(2026, 3, 9)));

        return lista;
    }
}
