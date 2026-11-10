package com.autotech.dao;

import com.autotech.model.Pago;
import java.util.List;

public interface IPagoDAO {
    List<Pago> listarPorOrden(int idOrden);
    boolean registrarPago(Pago pago);
    double calcularTotalPagado(int idOrden);
}
