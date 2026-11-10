package com.autotech.model;

public class ServicioMayor extends Servicio {
    private boolean requiereCertificadoGarantia;
    private int garantiaMesesPorDefecto;

    public ServicioMayor(int idServicio, String codigo, String nombre, String descripcion, double costoEstimadoManoObra, double tiempoEstimadoHoras, boolean activo) {
        super(idServicio, codigo, nombre, descripcion, 5, "Reparaciones mayores", costoEstimadoManoObra, tiempoEstimadoHoras, activo);
        this.requiereCertificadoGarantia = true;
        this.garantiaMesesPorDefecto = 12;
    }

    @Override
    public double calcularCostoTotal(double factorUrgencia, double costoRepuestosEstimados) {
        double pruebasCompresionYEscaner = 1200.00;
        return (getCostoEstimadoManoObra() * factorUrgencia) + costoRepuestosEstimados + pruebasCompresionYEscaner;
    }

    @Override
    public String getDescripcionTecnica() {
        return "NIVEL 5 - REPARACIONES MAYORES (ALTA COMPLEJIDAD): " + getDescripcion() + 
               " | Incluye pruebas de compresión, hermanado electrónico de ECU y garantía certificada de " + garantiaMesesPorDefecto + " meses.";
    }

    public boolean isRequiereCertificadoGarantia() { return requiereCertificadoGarantia; }
    public int getGarantiaMesesPorDefecto() { return garantiaMesesPorDefecto; }
}
