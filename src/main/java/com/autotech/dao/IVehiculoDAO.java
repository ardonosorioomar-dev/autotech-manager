package com.autotech.dao;

import com.autotech.model.Vehiculo;
import java.util.List;

public interface IVehiculoDAO {
    List<Vehiculo> listarTodos();
    List<Vehiculo> listarPorCliente(int idCliente);
    Vehiculo buscarPorId(int id);
    Vehiculo buscarPorPlaca(String placa);
    boolean insertar(Vehiculo v);
    boolean actualizar(Vehiculo v);
}
