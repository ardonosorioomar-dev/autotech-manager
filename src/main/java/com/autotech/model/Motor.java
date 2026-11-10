package com.autotech.model;

public class Motor {
    private int idMotor;
    private Integer idRepuesto;
    private String numeroSerie;
    private String codigoMotor;
    private double cilindradaLitros;
    private String tipoCombustible;
    private String marcaCompatible;
    private String modeloCompatible;
    private int anioInicioCompatible;
    private int anioFinCompatible;
    private String estado;
    private String ubicacionTaller;
    private String observaciones;

    public Motor(int idMotor, Integer idRepuesto, String numeroSerie, String codigoMotor, double cilindradaLitros, String tipoCombustible, String marcaCompatible, String modeloCompatible, int anioInicioCompatible, int anioFinCompatible, String estado, String ubicacionTaller, String observaciones) {
        this.idMotor = idMotor;
        this.idRepuesto = idRepuesto;
        this.numeroSerie = numeroSerie;
        this.codigoMotor = codigoMotor;
        this.cilindradaLitros = cilindradaLitros;
        this.tipoCombustible = tipoCombustible;
        this.marcaCompatible = marcaCompatible;
        this.modeloCompatible = modeloCompatible;
        this.anioInicioCompatible = anioInicioCompatible;
        this.anioFinCompatible = anioFinCompatible;
        this.estado = estado;
        this.ubicacionTaller = ubicacionTaller;
        this.observaciones = observaciones;
    }

    public boolean esCompatibleCon(Vehiculo v) {
        if (v == null) return false;
        boolean marcaOk = this.marcaCompatible.equalsIgnoreCase(v.getMarca());
        boolean modeloOk = this.modeloCompatible.equalsIgnoreCase(v.getModelo());
        boolean anioOk = v.getAnio() >= this.anioInicioCompatible && v.getAnio() <= this.anioFinCompatible;
        return marcaOk && modeloOk && anioOk;
    }

    public int getIdMotor() { return idMotor; }
    public void setIdMotor(int idMotor) { this.idMotor = idMotor; }

    public Integer getIdRepuesto() { return idRepuesto; }
    public void setIdRepuesto(Integer idRepuesto) { this.idRepuesto = idRepuesto; }

    public String getNumeroSerie() { return numeroSerie; }
    public void setNumeroSerie(String numeroSerie) { this.numeroSerie = numeroSerie; }

    public String getCodigoMotor() { return codigoMotor; }
    public void setCodigoMotor(String codigoMotor) { this.codigoMotor = codigoMotor; }

    public double getCilindradaLitros() { return cilindradaLitros; }
    public void setCilindradaLitros(double cilindradaLitros) { this.cilindradaLitros = cilindradaLitros; }

    public String getTipoCombustible() { return tipoCombustible; }
    public void setTipoCombustible(String tipoCombustible) { this.tipoCombustible = tipoCombustible; }

    public String getMarcaCompatible() { return marcaCompatible; }
    public void setMarcaCompatible(String marcaCompatible) { this.marcaCompatible = marcaCompatible; }

    public String getModeloCompatible() { return modeloCompatible; }
    public void setModeloCompatible(String modeloCompatible) { this.modeloCompatible = modeloCompatible; }

    public int getAnioInicioCompatible() { return anioInicioCompatible; }
    public void setAnioInicioCompatible(int anioInicioCompatible) { this.anioInicioCompatible = anioInicioCompatible; }

    public int getAnioFinCompatible() { return anioFinCompatible; }
    public void setAnioFinCompatible(int anioFinCompatible) { this.anioFinCompatible = anioFinCompatible; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getUbicacionTaller() { return ubicacionTaller; }
    public void setUbicacionTaller(String ubicacionTaller) { this.ubicacionTaller = ubicacionTaller; }

    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }

    @Override
    public String toString() {
        return "Serie: " + numeroSerie + " (" + codigoMotor + " " + cilindradaLitros + "L " + marcaCompatible + " " + modeloCompatible + " [" + anioInicioCompatible + "-" + anioFinCompatible + "]) - " + estado;
    }
}
