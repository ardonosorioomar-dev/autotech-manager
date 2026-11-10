package com.autotech.dao;

import com.autotech.model.Motor;
import java.util.List;

public interface IMotorDAO {
    List<Motor> listarDisponibles();
    List<Motor> listarTodos();
    Motor buscarPorId(int id);
    Motor buscarPorSerie(String serie);
    boolean cambiarEstado(int idMotor, String nuevoEstado, String ubicacion);
}
