package com.autotech.dao;

import com.autotech.model.Cliente;
import java.util.List;

public interface IClienteDAO {
    List<Cliente> listarTodos();
    Cliente buscarPorId(int id);
    Cliente buscarPorIdentidad(String identidad);
    boolean insertar(Cliente c);
    boolean actualizar(Cliente c);
    boolean cambiarEstadoActivo(int idCliente, boolean activo);
}
