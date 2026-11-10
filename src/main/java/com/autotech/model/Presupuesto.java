package com.autotech.model;

import java.time.LocalDateTime;

public class Presupuesto {
    private int idPresupuesto;
    private int idOrden;
    private String numeroPresupuesto;
    private LocalDateTime fechaEmision;
    private double subtotalServicios;
    private double subtotalRepuestos;
    private double porcentajeDescuento;
    private double montoDescuento;
    private double porcentajeISV;
    private double montoISV;
    private double totalGeneral;
    private String estadoPresupuesto;
    private String observaciones;

    public Presupuesto(int idPresupuesto, int idOrden, String numeroPresupuesto, double subtotalServicios, double subtotalRepuestos, double porcentajeDescuento, double porcentajeISV, String estadoPresupuesto, String observaciones) {
        this.idPresupuesto = idPresupuesto;
        this.idOrden = idOrden;
        this.numeroPresupuesto = numeroPresupuesto;
        this.fechaEmision = LocalDateTime.now();
        this.subtotalServicios = subtotalServicios;
        this.subtotalRepuestos = subtotalRepuestos;
        this.porcentajeDescuento = porcentajeDescuento;
        this.porcentajeISV = porcentajeISV;
        this.estadoPresupuesto = estadoPresupuesto;
        this.observaciones = observaciones;
        calcular();
    }

    public void calcular() {
        double subtotal = subtotalServicios + subtotalRepuestos;
        this.montoDescuento = subtotal * (porcentajeDescuento / 100.0);
        double gravable = Math.max(0, subtotal - montoDescuento);
        this.montoISV = gravable * (porcentajeISV / 100.0);
        this.totalGeneral = gravable + montoISV;
    }

    public int getIdPresupuesto() { return idPresupuesto; }
    public void setIdPresupuesto(int idPresupuesto) { this.idPresupuesto = idPresupuesto; }

    public int getIdOrden() { return idOrden; }
    public void setIdOrden(int idOrden) { this.idOrden = idOrden; }

    public String getNumeroPresupuesto() { return numeroPresupuesto; }
    public void setNumeroPresupuesto(String numeroPresupuesto) { this.numeroPresupuesto = numeroPresupuesto; }

    public LocalDateTime getFechaEmision() { return fechaEmision; }
    public void setFechaEmision(LocalDateTime fechaEmision) { this.fechaEmision = fechaEmision; }

    public double getSubtotalServicios() { return subtotalServicios; }
    public void setSubtotalServicios(double subtotalServicios) { this.subtotalServicios = subtotalServicios; calcular(); }

    public double getSubtotalRepuestos() { return subtotalRepuestos; }
    public void setSubtotalRepuestos(double subtotalRepuestos) { this.subtotalRepuestos = subtotalRepuestos; calcular(); }

    public double getPorcentajeDescuento() { return porcentajeDescuento; }
    public void setPorcentajeDescuento(double porcentajeDescuento) { this.porcentajeDescuento = porcentajeDescuento; calcular(); }

    public double getMontoDescuento() { return montoDescuento; }

    public double getPorcentajeISV() { return porcentajeISV; }
    public void setPorcentajeISV(double porcentajeISV) { this.porcentajeISV = porcentajeISV; calcular(); }

    public double getMontoISV() { return montoISV; }

    public double getTotalGeneral() { return totalGeneral; }

    public String getEstadoPresupuesto() { return estadoPresupuesto; }
    public void setEstadoPresupuesto(String estadoPresupuesto) { this.estadoPresupuesto = estadoPresupuesto; }

    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }

    @Override
    public String toString() {
        return numeroPresupuesto + " - Total: L " + String.format("%.2f", totalGeneral) + " (" + estadoPresupuesto + ")";
    }
}
