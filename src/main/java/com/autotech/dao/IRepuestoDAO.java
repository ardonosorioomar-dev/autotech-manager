package com.autotech.dao;

import com.autotech.model.Repuesto;
import java.util.List;

public interface IRepuestoDAO {
    List<Repuesto> listarTodos();
    List<Repuesto> listarBajoMinimo();
    Repuesto buscarPorId(int id);
    boolean descontarStock(int idRepuesto, int cantidad);
    boolean ingresarStock(int idRepuesto, int cantidad);
    boolean insertar(Repuesto r);
}
