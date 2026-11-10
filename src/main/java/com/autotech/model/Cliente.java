package com.autotech.model;

import java.util.ArrayList;
import java.util.List;

public class Cliente {
    private int idCliente;
    private String identidad;
    private String nombreCompleto;
    private String telefono;
    private String correo;
    private String direccion;
    private boolean activo;
    private List<Vehiculo> vehiculos;

    public Cliente(int idCliente, String identidad, String nombreCompleto, String telefono, String correo, String direccion, boolean activo) {
        this.idCliente = idCliente;
        this.identidad = identidad;
        this.nombreCompleto = nombreCompleto;
        this.telefono = telefono;
        this.correo = correo;
        this.direccion = direccion;
        this.activo = activo;
        this.vehiculos = new ArrayList<>();
    }

    public int getIdCliente() { return idCliente; }
    public void setIdCliente(int idCliente) { this.idCliente = idCliente; }

    public String getIdentidad() { return identidad; }
    public void setIdentidad(String identidad) { this.identidad = identidad; }

    public String getNombreCompleto() { return nombreCompleto; }
    public void setNombreCompleto(String nombreCompleto) { this.nombreCompleto = nombreCompleto; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }

    public List<Vehiculo> getVehiculos() { return vehiculos; }
    public void setVehiculos(List<Vehiculo> vehiculos) { this.vehiculos = vehiculos; }

    public void agregarVehiculo(Vehiculo vehiculo) {
        this.vehiculos.add(vehiculo);
    }

    @Override
    public String toString() {
        return nombreCompleto + " (" + identidad + ")";
    }
}
