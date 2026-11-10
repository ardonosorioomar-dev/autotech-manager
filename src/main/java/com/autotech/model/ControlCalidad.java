package com.autotech.model;

import java.time.LocalDateTime;

public class ControlCalidad {
    private int idControl;
    private int idOrden;
    private int idMecanicoInspector;
    private LocalDateTime fechaInspeccion;
    private boolean pruebaRutaRealizada;
    private boolean inspeccionFugasAprobada;
    private boolean escaneoComputarizadoLimpio;
    private boolean torquesVerificados;
    private boolean aprobadoParaEntrega;
    private String observacionesFinales;

    public ControlCalidad(int idControl, int idOrden, int idMecanicoInspector, boolean pruebaRutaRealizada, boolean inspeccionFugasAprobada, boolean escaneoComputarizadoLimpio, boolean torquesVerificados, boolean aprobadoParaEntrega, String observacionesFinales) {
        this.idControl = idControl;
        this.idOrden = idOrden;
        this.idMecanicoInspector = idMecanicoInspector;
        this.fechaInspeccion = LocalDateTime.now();
        this.pruebaRutaRealizada = pruebaRutaRealizada;
        this.inspeccionFugasAprobada = inspeccionFugasAprobada;
        this.escaneoComputarizadoLimpio = escaneoComputarizadoLimpio;
        this.torquesVerificados = torquesVerificados;
        this.aprobadoParaEntrega = aprobadoParaEntrega;
        this.observacionesFinales = observacionesFinales;
    }

    public boolean todasLasPruebasAprobadas() {
        return pruebaRutaRealizada && inspeccionFugasAprobada && escaneoComputarizadoLimpio && torquesVerificados;
    }

    public int getIdControl() { return idControl; }
    public void setIdControl(int idControl) { this.idControl = idControl; }

    public int getIdOrden() { return idOrden; }
    public void setIdOrden(int idOrden) { this.idOrden = idOrden; }

    public int getIdMecanicoInspector() { return idMecanicoInspector; }
    public void setIdMecanicoInspector(int idMecanicoInspector) { this.idMecanicoInspector = idMecanicoInspector; }

    public LocalDateTime getFechaInspeccion() { return fechaInspeccion; }
    public void setFechaInspeccion(LocalDateTime fechaInspeccion) { this.fechaInspeccion = fechaInspeccion; }

    public boolean isPruebaRutaRealizada() { return pruebaRutaRealizada; }
    public void setPruebaRutaRealizada(boolean pruebaRutaRealizada) { this.pruebaRutaRealizada = pruebaRutaRealizada; }

    public boolean isInspeccionFugasAprobada() { return inspeccionFugasAprobada; }
    public void setInspeccionFugasAprobada(boolean inspeccionFugasAprobada) { this.inspeccionFugasAprobada = inspeccionFugasAprobada; }

    public boolean isEscaneoComputarizadoLimpio() { return escaneoComputarizadoLimpio; }
    public void setEscaneoComputarizadoLimpio(boolean escaneoComputarizadoLimpio) { this.escaneoComputarizadoLimpio = escaneoComputarizadoLimpio; }

    public boolean isTorquesVerificados() { return torquesVerificados; }
    public void setTorquesVerificados(boolean torquesVerificados) { this.torquesVerificados = torquesVerificados; }

    public boolean isAprobadoParaEntrega() { return aprobadoParaEntrega; }
    public void setAprobadoParaEntrega(boolean aprobadoParaEntrega) { this.aprobadoParaEntrega = aprobadoParaEntrega; }

    public String getObservacionesFinales() { return observacionesFinales; }
    public void setObservacionesFinales(String observacionesFinales) { this.observacionesFinales = observacionesFinales; }
}
