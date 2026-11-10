package com.autotech.model;

public class ServicioPreventivo extends Servicio {
    public ServicioPreventivo(int idServicio, String codigo, String nombre, String descripcion, double costoEstimadoManoObra, double tiempoEstimadoHoras, boolean activo) {
        super(idServicio, codigo, nombre, descripcion, 2, "Mantenimiento preventivo", costoEstimadoManoObra, tiempoEstimadoHoras, activo);
    }

    @Override
    public double calcularCostoTotal(double factorUrgencia, double costoRepuestosEstimados) {
        double recargoInsumosEspeciales = 150.00;
        return (getCostoEstimadoManoObra() * factorUrgencia) + costoRepuestosEstimados + recargoInsumosEspeciales;
    }

    @Override
    public String getDescripcionTecnica() {
        return "NIVEL 2 - MANTENIMIENTO PREVENTIVO: " + getDescripcion() + " (Incluye calibración e insumos de limpieza ultrasónica)";
    }
}
