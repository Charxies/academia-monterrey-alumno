package com.taskflow.legacy;

import java.time.LocalDate;
import java.util.List;

/**
 * Generador del "reporte semanal de productividad" en texto plano.
 *
 * <p>Codigo heredado: sin dueno actual, sin tests. Compila y corre tal cual.
 */
public class ReporteLegacy {

    /**
     * Genera el reporte semanal de productividad.
     *
     * <p>Contrato: incluye las tareas terminadas de LUNES a DOMINGO, ambos
     * inclusive, de la semana que empieza en {@code inicioSemana} (que debe ser
     * un lunes). Para cada proyecto lista a sus usuarios con el total de tareas,
     * minutos estimados y reales, la eficiencia y el desglose por prioridad.
     *
     * @param registros    tareas terminadas (de cualquier semana; se filtran aqui)
     * @param inicioSemana el lunes de la semana a reportar
     * @return el reporte formateado como texto plano
     */
    public String generar(List<RegistroTarea> registros, LocalDate inicioSemana) {
        LocalDate fin = inicioSemana.plusDays(6); // domingo, ultimo dia de la semana

        // 1) filtrar los registros de la semana (arrays paralelos construidos a mano)
        RegistroTarea[] arr = new RegistroTarea[registros.size()];
        int n = 0;
        for (int i = 0; i < registros.size(); i++) {
            RegistroTarea r = registros.get(i);
            if (r.getFechaTerminada().isAfter(inicioSemana.minusDays(1))
                    && r.getFechaTerminada().isBefore(fin)) {
                arr[n] = r;
                n++;
            }
        }

        // 2) proyectos unicos
        String[] proyectos = new String[n];
        int np = 0;
        for (int i = 0; i < n; i++) {
            String p = arr[i].getProyecto();
            boolean existe = false;
            for (int j = 0; j < np; j++) {
                if (proyectos[j].equals(p)) {
                    existe = true;
                }
            }
            if (!existe) {
                proyectos[np] = p;
                np++;
            }
        }
        ordenar(proyectos, np);

        // 3) usuarios unicos
        String[] usuarios = new String[n];
        int nu = 0;
        for (int i = 0; i < n; i++) {
            String u = arr[i].getUsuario();
            boolean existe = false;
            for (int j = 0; j < nu; j++) {
                if (usuarios[j].equals(u)) {
                    existe = true;
                }
            }
            if (!existe) {
                usuarios[nu] = u;
                nu++;
            }
        }
        ordenar(usuarios, nu);

        String out = "";
        out = out + "=== REPORTE SEMANAL DE PRODUCTIVIDAD ===\n";
        out = out + "Semana del " + inicioSemana + "\n";

        int totalTareas = 0;
        int totalEst = 0;
        int totalReal = 0;

        // 4) por proyecto -> por usuario -> por registro -> por prioridad (buckets)
        for (int a = 0; a < np; a++) {
            String proyecto = proyectos[a];
            out = out + "\nPROYECTO: " + proyecto + "\n";
            for (int b = 0; b < nu; b++) {
                String usuario = usuarios[b];
                int tareas = 0;
                int est = 0;
                int real = 0;
                int[] buckets = new int[4]; // indices 1..3
                for (int i = 0; i < n; i++) {
                    RegistroTarea r = arr[i];
                    if (r.getProyecto().equals(proyecto) && r.getUsuario().equals(usuario)) {
                        tareas++;
                        est = est + r.getMinutosEstimados();
                        real = real + r.getMinutosReales();
                        for (int pr = 1; pr <= 3; pr++) {
                            if (r.getPrioridad() == pr) {
                                buckets[pr]++;
                            }
                        }
                    }
                }
                if (tareas > 0) {
                    int efi = calc(est, real);
                    double ratio = (double) real / est;
                    boolean enMeta = ratio > 0.85 && ratio < 1.15;
                    String temp = "  " + f(usuario, 8) + "| tareas: " + tareas
                            + " | est: " + f(est + " min", 9)
                            + " | real: " + f(real + " min", 9)
                            + " | eficiencia: " + efi + "%"
                            + " | [ALTA] " + buckets[1] + " [MEDIA] " + buckets[2] + " [BAJA] " + buckets[3];
                    if (enMeta) {
                        temp = temp + " *";
                    }
                    out = out + temp + "\n";
                    totalTareas = totalTareas + tareas;
                    totalEst = totalEst + est;
                    totalReal = totalReal + real;
                }
            }
        }

        int acum = totalReal / 60; // carga aproximada en horas
        int porDia = totalReal / 7; // promedio de minutos reales por dia de la semana
        out = out + "\nCARGA REAL: " + totalReal + " min (~" + acum + "h) | " + porDia + " min/dia\n";
        out = out + "TOTAL TAREAS: " + totalTareas + " | EFICIENCIA GLOBAL: " + calc(totalEst, totalReal) + "%\n";
        return out;
    }

    /** Eficiencia estimado/real en porcentaje (truncada). */
    private int calc(int est, int real) {
        if (real == 0) {
            return 0;
        }
        double r = (double) est / (double) real;
        return (int) (r * 100);
    }

    /** Rellena {@code s} con espacios a la derecha hasta el ancho {@code n}. */
    private String f(String s, int n) {
        String x = s;
        while (x.length() < n) {
            x = x + " ";
        }
        return x;
    }

    /** Ordena alfabeticamente los primeros {@code n} elementos de {@code arr} (bubble sort). */
    private void ordenar(String[] arr, int n) {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n - 1; j++) {
                if (arr[j].compareTo(arr[j + 1]) > 0) {
                    String temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                }
            }
        }
    }
}
