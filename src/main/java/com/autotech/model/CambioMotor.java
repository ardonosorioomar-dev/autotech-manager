package com.autotech.model;

import java.time.LocalDateTime;

public class CambioMotor {
    private int idCambioMotor;
    private int idOrden;
    private int idVehiculo;
    private int idMotorNuevo;
    private String serieMotorAnterior;
    private String serieMotorNuevo;
    private String causaCambio;
    private String destinoMotorRetirado;
    private String resultadosPruebasCompresion;
    private int garantiaMeses;
    private int garantiaKilometros;
    private String certificadoGarantia;
    private LocalDateTime fechaInstalacion;
    private boolean aprobadoControlCalidad;

    public CambioMotor(int idCambioMotor, int idOrden, int idVehiculo, int idMotorNuevo, String serieMotorAnterior, String serieMotorNuevo, String causaCambio, String destinoMotorRetirado, String resultadosPruebasCompresion, int garantiaMeses, int garantiaKilometros, String certificadoGarantia, boolean aprobadoControlCalidad) {
        this.idCambioMotor = idCambioMotor;
        this.idOrden = idOrden;
        this.idVehiculo = idVehiculo;
        this.idMotorNuevo = idMotorNuevo;
        this.serieMotorAnterior = serieMotorAnterior;
        this.serieMotorNuevo = serieMotorNuevo;
        this.causaCambio = causaCambio;
        this.destinoMotorRetirado = destinoMotorRetirado;
        this.resultadosPruebasCompresion = resultadosPruebasCompresion;
        this.garantiaMeses = garantiaMeses;
        this.garantiaKilometros = garantiaKilometros;
        this.certificadoGarantia = certificadoGarantia;
        this.fechaInstalacion = LocalDateTime.now();
        this.aprobadoControlCalidad = aprobadoControlCalidad;
    }

    public int getIdCambioMotor() { return idCambioMotor; }
    public void setIdCambioMotor(int idCambioMotor) { this.idCambioMotor = idCambioMotor; }

    public int getIdOrden() { return idOrden; }
    public void setIdOrden(int idOrden) { this.idOrden = idOrden; }

    public int getIdVehiculo() { return idVehiculo; }
    public void setIdVehiculo(int idVehiculo) { this.idVehiculo = idVehiculo; }

    public int getIdMotorNuevo() { return idMotorNuevo; }
    public void setIdMotorNuevo(int idMotorNuevo) { this.idMotorNuevo = idMotorNuevo; }

    public String getSerieMotorAnterior() { return serieMotorAnterior; }
    public void setSerieMotorAnterior(String serieMotorAnterior) { this.serieMotorAnterior = serieMotorAnterior; }

    public String getSerieMotorNuevo() { return serieMotorNuevo; }
    public void setSerieMotorNuevo(String serieMotorNuevo) { this.serieMotorNuevo = serieMotorNuevo; }

    public String getCausaCambio() { return causaCambio; }
    public void setCausaCambio(String causaCambio) { this.causaCambio = causaCambio; }

    public String getDestinoMotorRetirado() { return destinoMotorRetirado; }
    public void setDestinoMotorRetirado(String destinoMotorRetirado) { this.destinoMotorRetirado = destinoMotorRetirado; }

    public String getResultadosPruebasCompresion() { return resultadosPruebasCompresion; }
    public void setResultadosPruebasCompresion(String resultadosPruebasCompresion) { this.resultadosPruebasCompresion = resultadosPruebasCompresion; }

    public int getGarantiaMeses() { return garantiaMeses; }
    public void setGarantiaMeses(int garantiaMeses) { this.garantiaMeses = garantiaMeses; }

    public int getGarantiaKilometros() { return garantiaKilometros; }
    public void setGarantiaKilometros(int garantiaKilometros) { this.garantiaKilometros = garantiaKilometros; }

    public String getCertificadoGarantia() { return certificadoGarantia; }
    public void setCertificadoGarantia(String certificadoGarantia) { this.certificadoGarantia = certificadoGarantia; }

    public LocalDateTime getFechaInstalacion() { return fechaInstalacion; }
    public void setFechaInstalacion(LocalDateTime fechaInstalacion) { this.fechaInstalacion = fechaInstalacion; }

    public boolean isAprobadoControlCalidad() { return aprobadoControlCalidad; }
    public void setAprobadoControlCalidad(boolean aprobadoControlCalidad) { this.aprobadoControlCalidad = aprobadoControlCalidad; }

    @Override
    public String toString() {
        return "Sustitución [" + certificadoGarantia + "] Anterior: " + serieMotorAnterior + " -> Nuevo: " + serieMotorNuevo;
    }
}
