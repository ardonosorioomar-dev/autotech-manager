package com.autotech.model;

public abstract class Servicio {
    private int idServicio;
    private String codigo;
    private String nombre;
    private String descripcion;
    private int nivelComplejidad;
    private String categoria;
    private double costoEstimadoManoObra;
    private double tiempoEstimadoHoras;
    private boolean activo;

    public Servicio(int idServicio, String codigo, String nombre, String descripcion, int nivelComplejidad, String categoria, double costoEstimadoManoObra, double tiempoEstimadoHoras, boolean activo) {
        this.idServicio = idServicio;
        this.codigo = codigo;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.nivelComplejidad = nivelComplejidad;
        this.categoria = categoria;
        this.costoEstimadoManoObra = costoEstimadoManoObra;
        this.tiempoEstimadoHoras = tiempoEstimadoHoras;
        this.activo = activo;
    }

    public abstract double calcularCostoTotal(double factorUrgencia, double costoRepuestosEstimados);

    public abstract String getDescripcionTecnica();

    public int getIdServicio() { return idServicio; }
    public void setIdServicio(int idServicio) { this.idServicio = idServicio; }

    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public int getNivelComplejidad() { return nivelComplejidad; }
    public void setNivelComplejidad(int nivelComplejidad) { this.nivelComplejidad = nivelComplejidad; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public double getCostoEstimadoManoObra() { return costoEstimadoManoObra; }
    public void setCostoEstimadoManoObra(double costoEstimadoManoObra) { this.costoEstimadoManoObra = costoEstimadoManoObra; }

    public double getTiempoEstimadoHoras() { return tiempoEstimadoHoras; }
    public void setTiempoEstimadoHoras(double tiempoEstimadoHoras) { this.tiempoEstimadoHoras = tiempoEstimadoHoras; }

    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }

    @Override
    public String toString() {
        return "[" + codigo + "] " + nombre + " (Nivel " + nivelComplejidad + " - L " + String.format("%.2f", costoEstimadoManoObra) + ")";
    }
}
