package com.autotech.model;

public class ServicioBasico extends Servicio {
    public ServicioBasico(int idServicio, String codigo, String nombre, String descripcion, double costoEstimadoManoObra, double tiempoEstimadoHoras, boolean activo) {
        super(idServicio, codigo, nombre, descripcion, 1, "Mantenimiento básico", costoEstimadoManoObra, tiempoEstimadoHoras, activo);
    }

    @Override
    public double calcularCostoTotal(double factorUrgencia, double costoRepuestosEstimados) {
        return (getCostoEstimadoManoObra() * factorUrgencia) + costoRepuestosEstimados;
    }

    @Override
    public String getDescripcionTecnica() {
        return "NIVEL 1 - MANTENIMIENTO BÁSICO: " + getDescripcion() + " (Tiempo est.: " + getTiempoEstimadoHoras() + " hrs)";
    }
}
