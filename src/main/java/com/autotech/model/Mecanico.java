package com.autotech.model;

public class Mecanico {
    private int idMecanico;
    private String identidad;
    private String nombreCompleto;
    private String especialidad;
    private String telefono;
    private boolean activo;
    private boolean disponible;

    public Mecanico(int idMecanico, String identidad, String nombreCompleto, String especialidad, String telefono, boolean activo, boolean disponible) {
        this.idMecanico = idMecanico;
        this.identidad = identidad;
        this.nombreCompleto = nombreCompleto;
        this.especialidad = especialidad;
        this.telefono = telefono;
        this.activo = activo;
        this.disponible = disponible;
    }

    public int getIdMecanico() { return idMecanico; }
    public void setIdMecanico(int idMecanico) { this.idMecanico = idMecanico; }

    public String getIdentidad() { return identidad; }
    public void setIdentidad(String identidad) { this.identidad = identidad; }

    public String getNombreCompleto() { return nombreCompleto; }
    public void setNombreCompleto(String nombreCompleto) { this.nombreCompleto = nombreCompleto; }

    public String getEspecialidad() { return especialidad; }
    public void setEspecialidad(String especialidad) { this.especialidad = especialidad; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }

    public boolean isDisponible() { return disponible; }
    public void setDisponible(boolean disponible) { this.disponible = disponible; }

    @Override
    public String toString() {
        return nombreCompleto + " [" + especialidad + "]" + (disponible ? " (Disponible)" : " (Ocupado)");
    }
}
