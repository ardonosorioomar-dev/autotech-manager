package com.autotech.model;

public class ServicioIntermedio extends Servicio {
    public ServicioIntermedio(int idServicio, String codigo, String nombre, String descripcion, double costoEstimadoManoObra, double tiempoEstimadoHoras, boolean activo) {
        super(idServicio, codigo, nombre, descripcion, 3, "Reparaciones intermedias", costoEstimadoManoObra, tiempoEstimadoHoras, activo);
    }

    @Override
    public double calcularCostoTotal(double factorUrgencia, double costoRepuestosEstimados) {
        double pruebaDinamica = 250.00;
        return (getCostoEstimadoManoObra() * factorUrgencia) + costoRepuestosEstimados + pruebaDinamica;
    }

    @Override
    public String getDescripcionTecnica() {
        return "NIVEL 3 - REPARACIONES INTERMEDIAS: " + getDescripcion() + " (Incluye verificación de torque y pruebas de banco)";
    }
}
