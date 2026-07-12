package com.taskflow.legacy;

import java.time.LocalDate;

/** Punto de entrada: imprime el reporte semanal con los datos de demostracion. */
public class MainLegacy {

    public static void main(String[] args) {
        ReporteLegacy reporte = new ReporteLegacy();
        System.out.println(reporte.generar(DatosDemo.registros(), LocalDate.of(2026, 3, 2)));
    }
}
