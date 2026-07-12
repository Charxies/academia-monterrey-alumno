package com.taskflow.legacy;

import java.time.LocalDate;

/**
 * Registro de una tarea terminada, tal como lo consume el reporte semanal.
 *
 * <p>POJO con getters y setters. (Nota: en el resto de TaskFlow los objetos de
 * transporte son {@code record}; este modulo es mas viejo y quedo asi.)
 */
public class RegistroTarea {

    private long idTarea;
    private String proyecto;
    private String usuario;
    private int prioridad; // 1 = alta, 2 = media, 3 = baja
    private int minutosEstimados;
    private int minutosReales;
    private LocalDate fechaTerminada;

    public RegistroTarea() {
    }

    public RegistroTarea(long idTarea, String proyecto, String usuario, int prioridad,
                         int minutosEstimados, int minutosReales, LocalDate fechaTerminada) {
        this.idTarea = idTarea;
        this.proyecto = proyecto;
        this.usuario = usuario;
        this.prioridad = prioridad;
        this.minutosEstimados = minutosEstimados;
        this.minutosReales = minutosReales;
        this.fechaTerminada = fechaTerminada;
    }

    public long getIdTarea() {
        return idTarea;
    }

    public void setIdTarea(long idTarea) {
        this.idTarea = idTarea;
    }

    public String getProyecto() {
        return proyecto;
    }

    public void setProyecto(String proyecto) {
        this.proyecto = proyecto;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public int getPrioridad() {
        return prioridad;
    }

    public void setPrioridad(int prioridad) {
        this.prioridad = prioridad;
    }

    public int getMinutosEstimados() {
        return minutosEstimados;
    }

    public void setMinutosEstimados(int minutosEstimados) {
        this.minutosEstimados = minutosEstimados;
    }

    public int getMinutosReales() {
        return minutosReales;
    }

    public void setMinutosReales(int minutosReales) {
        this.minutosReales = minutosReales;
    }

    public LocalDate getFechaTerminada() {
        return fechaTerminada;
    }

    public void setFechaTerminada(LocalDate fechaTerminada) {
        this.fechaTerminada = fechaTerminada;
    }
}
