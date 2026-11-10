package com.autotech.model;

public class ServicioAvanzado extends Servicio {
    public ServicioAvanzado(int idServicio, String codigo, String nombre, String descripcion, double costoEstimadoManoObra, double tiempoEstimadoHoras, boolean activo) {
        super(idServicio, codigo, nombre, descripcion, 4, "Reparaciones avanzadas", costoEstimadoManoObra, tiempoEstimadoHoras, activo);
    }

    @Override
    public double calcularCostoTotal(double factorUrgencia, double costoRepuestosEstimados) {
        double inspeccionPrecision = 500.00;
        return (getCostoEstimadoManoObra() * factorUrgencia) + costoRepuestosEstimados + inspeccionPrecision;
    }

    @Override
    public String getDescripcionTecnica() {
        return "NIVEL 4 - REPARACIONES AVANZADAS: " + getDescripcion() + " (Requiere metrología especializada y escaneo de parámetros)";
    }
}
