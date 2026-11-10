package com.autotech.model;

import java.time.LocalDateTime;

public class ActividadTrabajo {
    private int idActividad;
    private int idOrden;
    private int idMecanico;
    private String descripcionActividad;
    private double horasTrabajadas;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;
    private boolean completada;
    private String nombreMecanico;

    public ActividadTrabajo(int idActividad, int idOrden, int idMecanico, String descripcionActividad, double horasTrabajadas, boolean completada) {
        this.idActividad = idActividad;
        this.idOrden = idOrden;
        this.idMecanico = idMecanico;
        this.descripcionActividad = descripcionActividad;
        this.horasTrabajadas = horasTrabajadas;
        this.fechaInicio = LocalDateTime.now();
        this.completada = completada;
    }

    public int getIdActividad() { return idActividad; }
    public void setIdActividad(int idActividad) { this.idActividad = idActividad; }

    public int getIdOrden() { return idOrden; }
    public void setIdOrden(int idOrden) { this.idOrden = idOrden; }

    public int getIdMecanico() { return idMecanico; }
    public void setIdMecanico(int idMecanico) { this.idMecanico = idMecanico; }

    public String getDescripcionActividad() { return descripcionActividad; }
    public void setDescripcionActividad(String descripcionActividad) { this.descripcionActividad = descripcionActividad; }

    public double getHorasTrabajadas() { return horasTrabajadas; }
    public void setHorasTrabajadas(double horasTrabajadas) { this.horasTrabajadas = horasTrabajadas; }

    public LocalDateTime getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(LocalDateTime fechaInicio) { this.fechaInicio = fechaInicio; }

    public LocalDateTime getFechaFin() { return fechaFin; }
    public void setFechaFin(LocalDateTime fechaFin) { this.fechaFin = fechaFin; }

    public boolean isCompletada() { return completada; }
    public void setCompletada(boolean completada) { this.completada = completada; }

    public String getNombreMecanico() { return nombreMecanico; }
    public void setNombreMecanico(String nombreMecanico) { this.nombreMecanico = nombreMecanico; }
}
