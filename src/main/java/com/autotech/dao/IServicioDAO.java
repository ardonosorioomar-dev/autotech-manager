package com.autotech.dao;

import com.autotech.model.Servicio;
import java.util.List;

public interface IServicioDAO {
    List<Servicio> listarTodos();
    List<Servicio> listarPorNivel(int nivel);
    Servicio buscarPorId(int id);
    Servicio buscarPorCodigo(String codigo);
    boolean insertar(Servicio s);
}
