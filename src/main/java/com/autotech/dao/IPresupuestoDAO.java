package com.autotech.dao;

import com.autotech.model.Presupuesto;

public interface IPresupuestoDAO {
    Presupuesto buscarPorOrden(int idOrden);
    boolean guardar(Presupuesto p);
    boolean actualizarEstado(int idPresupuesto, String estado);
}
