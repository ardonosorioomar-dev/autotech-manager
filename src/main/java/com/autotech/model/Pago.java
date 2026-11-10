package com.autotech.model;

import java.time.LocalDateTime;

public class Pago {
    private int idPago;
    private int idOrden;
    private String numeroRecibo;
    private double monto;
    private String metodoPago;
    private String referenciaTransaccion;
    private LocalDateTime fechaPago;
    private String notas;

    public Pago(int idPago, int idOrden, String numeroRecibo, double monto, String metodoPago, String referenciaTransaccion, LocalDateTime fechaPago, String notas) {
        this.idPago = idPago;
        this.idOrden = idOrden;
        this.numeroRecibo = numeroRecibo;
        this.monto = monto;
        this.metodoPago = metodoPago;
        this.referenciaTransaccion = referenciaTransaccion;
        this.fechaPago = fechaPago != null ? fechaPago : LocalDateTime.now();
        this.notas = notas;
    }

    public int getIdPago() { return idPago; }
    public void setIdPago(int idPago) { this.idPago = idPago; }

    public int getIdOrden() { return idOrden; }
    public void setIdOrden(int idOrden) { this.idOrden = idOrden; }

    public String getNumeroRecibo() { return numeroRecibo; }
    public void setNumeroRecibo(String numeroRecibo) { this.numeroRecibo = numeroRecibo; }

    public double getMonto() { return monto; }
    public void setMonto(double monto) { this.monto = monto; }

    public String getMetodoPago() { return metodoPago; }
    public void setMetodoPago(String metodoPago) { this.metodoPago = metodoPago; }

    public String getReferenciaTransaccion() { return referenciaTransaccion; }
    public void setReferenciaTransaccion(String referenciaTransaccion) { this.referenciaTransaccion = referenciaTransaccion; }

    public LocalDateTime getFechaPago() { return fechaPago; }
    public void setFechaPago(LocalDateTime fechaPago) { this.fechaPago = fechaPago; }

    public String getNotas() { return notas; }
    public void setNotas(String notas) { this.notas = notas; }

    @Override
    public String toString() {
        return numeroRecibo + " - L " + String.format("%.2f", monto) + " (" + metodoPago + ")";
    }
}
