package com.autotech.dao;

import com.autotech.model.Mecanico;
import java.util.List;

public interface IMecanicoDAO {
    List<Mecanico> listarTodos();
    List<Mecanico> listarActivos();
    Mecanico buscarPorId(int id);
    boolean insertar(Mecanico m);
    boolean actualizarDisponibilidad(int idMecanico, boolean disponible);
}
