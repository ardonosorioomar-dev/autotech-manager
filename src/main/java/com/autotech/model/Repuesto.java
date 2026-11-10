package com.autotech.model;

public class Repuesto {
    private int idRepuesto;
    private int idProveedor;
    private String codigo;
    private String nombre;
    private String categoria;
    private int stockActual;
    private int stockMinimo;
    private double costoUnitario;
    private double precioVenta;
    private boolean activo;
    private String nombreProveedor;

    public Repuesto(int idRepuesto, int idProveedor, String codigo, String nombre, String categoria, int stockActual, int stockMinimo, double costoUnitario, double precioVenta, boolean activo) {
        this.idRepuesto = idRepuesto;
        this.idProveedor = idProveedor;
        this.codigo = codigo;
        this.nombre = nombre;
        this.categoria = categoria;
        this.stockActual = stockActual;
        this.stockMinimo = stockMinimo;
        this.costoUnitario = costoUnitario;
        this.precioVenta = precioVenta;
        this.activo = activo;
    }

    public boolean esBajoMinimo() {
        return stockActual <= stockMinimo;
    }

    public int getIdRepuesto() { return idRepuesto; }
    public void setIdRepuesto(int idRepuesto) { this.idRepuesto = idRepuesto; }

    public int getIdProveedor() { return idProveedor; }
    public void setIdProveedor(int idProveedor) { this.idProveedor = idProveedor; }

    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public int getStockActual() { return stockActual; }
    public void setStockActual(int stockActual) { this.stockActual = stockActual; }

    public int getStockMinimo() { return stockMinimo; }
    public void setStockMinimo(int stockMinimo) { this.stockMinimo = stockMinimo; }

    public double getCostoUnitario() { return costoUnitario; }
    public void setCostoUnitario(double costoUnitario) { this.costoUnitario = costoUnitario; }

    public double getPrecioVenta() { return precioVenta; }
    public void setPrecioVenta(double precioVenta) { this.precioVenta = precioVenta; }

    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }

    public String getNombreProveedor() { return nombreProveedor; }
    public void setNombreProveedor(String nombreProveedor) { this.nombreProveedor = nombreProveedor; }

    @Override
    public String toString() {
        return nombre + " (Stock: " + stockActual + " | L " + String.format("%.2f", precioVenta) + ")";
    }
}
