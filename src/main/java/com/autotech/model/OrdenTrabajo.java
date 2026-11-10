package com.autotech.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class OrdenTrabajo {
    private int idOrden;
    private String numeroOrden;
    private int idCliente;
    private int idVehiculo;
    private LocalDateTime fechaIngreso;
    private LocalDateTime fechaCompromiso;
    private LocalDateTime fechaFinalizacion;
    private int kilometrajeIngreso;
    private String sintomasCliente;
    private String diagnosticoPreliminar;
    private String estado;
    private double subtotalServicios;
    private double subtotalRepuestos;
    private double descuento;
    private double impuestoISV;
    private double totalOrden;
    private double saldoPendiente;

    private String nombreCliente;
    private String descripcionVehiculo;
    private String placaVehiculo;
    private String nombreMecanicoLider;
    private int idMecanicoLider;

    private List<Mecanico> mecanicosAsignados = new ArrayList<>();
    private List<Servicio> servicios = new ArrayList<>();
    private List<Repuesto> repuestosConsumidos = new ArrayList<>();

    public OrdenTrabajo(int idOrden, String numeroOrden, int idCliente, int idVehiculo, LocalDateTime fechaIngreso, int kilometrajeIngreso, String sintomasCliente, String diagnosticoPreliminar, String estado) {
        this.idOrden = idOrden;
        this.numeroOrden = numeroOrden;
        this.idCliente = idCliente;
        this.idVehiculo = idVehiculo;
        this.fechaIngreso = fechaIngreso;
        this.kilometrajeIngreso = kilometrajeIngreso;
        this.sintomasCliente = sintomasCliente;
        this.diagnosticoPreliminar = diagnosticoPreliminar;
        this.estado = estado;
    }

    public void recalcularTotales(double porcentajeISV) {
        double subtotal = subtotalServicios + subtotalRepuestos;
        double baseImponible = Math.max(0, subtotal - descuento);
        this.impuestoISV = baseImponible * (porcentajeISV / 100.0);
        this.totalOrden = baseImponible + impuestoISV;
        this.saldoPendiente = totalOrden;
    }

    public int getIdOrden() { return idOrden; }
    public void setIdOrden(int idOrden) { this.idOrden = idOrden; }

    public String getNumeroOrden() { return numeroOrden; }
    public void setNumeroOrden(String numeroOrden) { this.numeroOrden = numeroOrden; }

    public int getIdCliente() { return idCliente; }
    public void setIdCliente(int idCliente) { this.idCliente = idCliente; }

    public int getIdVehiculo() { return idVehiculo; }
    public void setIdVehiculo(int idVehiculo) { this.idVehiculo = idVehiculo; }

    public LocalDateTime getFechaIngreso() { return fechaIngreso; }
    public void setFechaIngreso(LocalDateTime fechaIngreso) { this.fechaIngreso = fechaIngreso; }

    public LocalDateTime getFechaCompromiso() { return fechaCompromiso; }
    public void setFechaCompromiso(LocalDateTime fechaCompromiso) { this.fechaCompromiso = fechaCompromiso; }

    public LocalDateTime getFechaFinalizacion() { return fechaFinalizacion; }
    public void setFechaFinalizacion(LocalDateTime fechaFinalizacion) { this.fechaFinalizacion = fechaFinalizacion; }

    public int getKilometrajeIngreso() { return kilometrajeIngreso; }
    public void setKilometrajeIngreso(int kilometrajeIngreso) { this.kilometrajeIngreso = kilometrajeIngreso; }

    public String getSintomasCliente() { return sintomasCliente; }
    public void setSintomasCliente(String sintomasCliente) { this.sintomasCliente = sintomasCliente; }

    public String getDiagnosticoPreliminar() { return diagnosticoPreliminar; }
    public void setDiagnosticoPreliminar(String diagnosticoPreliminar) { this.diagnosticoPreliminar = diagnosticoPreliminar; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public double getSubtotalServicios() { return subtotalServicios; }
    public void setSubtotalServicios(double subtotalServicios) { this.subtotalServicios = subtotalServicios; }

    public double getSubtotalRepuestos() { return subtotalRepuestos; }
    public void setSubtotalRepuestos(double subtotalRepuestos) { this.subtotalRepuestos = subtotalRepuestos; }

    public double getDescuento() { return descuento; }
    public void setDescuento(double descuento) { this.descuento = descuento; }

    public double getImpuestoISV() { return impuestoISV; }
    public void setImpuestoISV(double impuestoISV) { this.impuestoISV = impuestoISV; }

    public double getTotalOrden() { return totalOrden; }
    public void setTotalOrden(double totalOrden) { this.totalOrden = totalOrden; }

    public double getSaldoPendiente() { return saldoPendiente; }
    public void setSaldoPendiente(double saldoPendiente) { this.saldoPendiente = saldoPendiente; }

    public String getNombreCliente() { return nombreCliente; }
    public void setNombreCliente(String nombreCliente) { this.nombreCliente = nombreCliente; }

    public String getDescripcionVehiculo() { return descripcionVehiculo; }
    public void setDescripcionVehiculo(String descripcionVehiculo) { this.descripcionVehiculo = descripcionVehiculo; }

    public String getPlacaVehiculo() { return placaVehiculo; }
    public void setPlacaVehiculo(String placaVehiculo) { this.placaVehiculo = placaVehiculo; }

    public String getNombreMecanicoLider() { return nombreMecanicoLider; }
    public void setNombreMecanicoLider(String nombreMecanicoLider) { this.nombreMecanicoLider = nombreMecanicoLider; }

    public int getIdMecanicoLider() { return idMecanicoLider; }
    public void setIdMecanicoLider(int idMecanicoLider) { this.idMecanicoLider = idMecanicoLider; }

    public List<Mecanico> getMecanicosAsignados() { return mecanicosAsignados; }
    public void setMecanicosAsignados(List<Mecanico> mecanicosAsignados) { this.mecanicosAsignados = mecanicosAsignados; }

    public List<Servicio> getServicios() { return servicios; }
    public void setServicios(List<Servicio> servicios) { this.servicios = servicios; }

    public List<Repuesto> getRepuestosConsumidos() { return repuestosConsumidos; }
    public void setRepuestosConsumidos(List<Repuesto> repuestosConsumidos) { this.repuestosConsumidos = repuestosConsumidos; }

    @Override
    public String toString() {
        return numeroOrden + " - " + estado + " (" + (placaVehiculo != null ? placaVehiculo : "Vehiculo") + ")";
    }
}
