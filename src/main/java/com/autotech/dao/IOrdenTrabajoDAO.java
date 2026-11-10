package com.autotech.dao;

import com.autotech.model.OrdenTrabajo;
import java.util.List;

public interface IOrdenTrabajoDAO {
    List<OrdenTrabajo> listarTodas();
    List<OrdenTrabajo> listarPorVehiculo(int idVehiculo);
    OrdenTrabajo buscarPorId(int id);
    OrdenTrabajo buscarPorNumero(String numeroOrden);
    boolean insertar(OrdenTrabajo o);
    boolean actualizarEstado(int idOrden, String nuevoEstado);
    boolean actualizarTotales(int idOrden, double servicios, double repuestos, double descuento, double isv, double total, double saldo);
}
