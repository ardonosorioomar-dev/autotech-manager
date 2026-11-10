package com.autotech.model;

public class Vehiculo {
    private int idVehiculo;
    private int idCliente;
    private String placa;
    private String vin;
    private String marca;
    private String modelo;
    private int anio;
    private String color;
    private int kilometraje;
    private String tipoCombustible;
    private boolean activo;
    private String nombrePropietario;

    public Vehiculo(int idVehiculo, int idCliente, String placa, String vin, String marca, String modelo, int anio, String color, int kilometraje, String tipoCombustible, boolean activo) {
        this.idVehiculo = idVehiculo;
        this.idCliente = idCliente;
        this.placa = placa;
        this.vin = vin;
        this.marca = marca;
        this.modelo = modelo;
        this.anio = anio;
        this.color = color;
        this.kilometraje = kilometraje;
        this.tipoCombustible = tipoCombustible;
        this.activo = activo;
    }

    public int getIdVehiculo() { return idVehiculo; }
    public void setIdVehiculo(int idVehiculo) { this.idVehiculo = idVehiculo; }

    public int getIdCliente() { return idCliente; }
    public void setIdCliente(int idCliente) { this.idCliente = idCliente; }

    public String getPlaca() { return placa; }
    public void setPlaca(String placa) { this.placa = placa; }

    public String getVin() { return vin; }
    public void setVin(String vin) { this.vin = vin; }

    public String getMarca() { return marca; }
    public void setMarca(String marca) { this.marca = marca; }

    public String getModelo() { return modelo; }
    public void setModelo(String modelo) { this.modelo = modelo; }

    public int getAnio() { return anio; }
    public void setAnio(int anio) { this.anio = anio; }

    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }

    public int getKilometraje() { return kilometraje; }
    public void setKilometraje(int kilometraje) { this.kilometraje = kilometraje; }

    public String getTipoCombustible() { return tipoCombustible; }
    public void setTipoCombustible(String tipoCombustible) { this.tipoCombustible = tipoCombustible; }

    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }

    public String getNombrePropietario() { return nombrePropietario; }
    public void setNombrePropietario(String nombrePropietario) { this.nombrePropietario = nombrePropietario; }

    public String getDescripcionCompleta() {
        return marca + " " + modelo + " (" + anio + ") - " + color + " [" + placa + "]";
    }

    @Override
    public String toString() {
        return placa + " - " + marca + " " + modelo + " (" + anio + ")";
    }
}
