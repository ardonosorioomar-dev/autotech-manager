package com.autotech.controller;

import com.autotech.util.ConexionBD;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Guarda y lee clientes y vehiculos en SQL Server (consultas parametrizadas).
 * GET  /api/sync/clientes  -> lista
 * POST /api/sync/clientes  -> recibe la lista, inserta (id <= 0) o actualiza (id > 0) y devuelve la lista real
 */
@WebServlet("/api/sync/*")
public class SyncServlet extends HttpServlet {

    private static final Gson GSON = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String tabla = req.getPathInfo() == null ? "" : req.getPathInfo().substring(1);
        try (Connection cn = ConexionBD.obtener()) {
            enviar(resp, 200, listar(cn, tabla));
        } catch (IllegalArgumentException e) {
            error(resp, 404, "Coleccion no valida.");
        } catch (Exception e) {
            error(resp, 500, "Error de base de datos: " + e.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String tabla = req.getPathInfo() == null ? "" : req.getPathInfo().substring(1);
        try (Connection cn = ConexionBD.obtener()) {
            JsonArray datos = JsonParser.parseReader(req.getReader()).getAsJsonArray();
            cn.setAutoCommit(false);
            try {
                for (JsonElement e : datos) {
                    guardar(cn, tabla, e.getAsJsonObject());
                }
                cn.commit();
            } catch (SQLException ex) {
                cn.rollback();
                throw ex;
            }
            cn.setAutoCommit(true);
            enviar(resp, 200, listar(cn, tabla));
        } catch (IllegalArgumentException e) {
            error(resp, 404, "Coleccion no valida.");
        } catch (SQLException e) {
            String m = e.getMessage() == null ? "" : e.getMessage();
            if (m.contains("UNIQUE") || m.contains("duplicate")) {
                error(resp, 400, "Ya existe un registro con esa identidad, placa o VIN.");
            } else if (m.contains("CHECK")) {
                error(resp, 400, "Algun dato no cumple las reglas de la base de datos.");
            } else {
                error(resp, 400, "No se pudo guardar: " + m);
            }
        } catch (Exception e) {
            error(resp, 400, "Solicitud invalida: " + e.getMessage());
        }
    }

    // ---------- lectura ----------
    private JsonArray listar(Connection cn, String tabla) throws SQLException {
        JsonArray lista = new JsonArray();
        String sql;
        if (tabla.equals("clientes")) {
            sql = "SELECT IdCliente, Identidad, NombreCompleto, Telefono, Correo, Direccion, Activo "
                + "FROM dbo.Clientes ORDER BY IdCliente";
        } else if (tabla.equals("vehiculos")) {
            sql = "SELECT IdVehiculo, IdCliente, Placa, VIN, Marca, Modelo, Anio, Color, Kilometraje, "
                + "TipoCombustible, Activo FROM dbo.Vehiculos ORDER BY IdVehiculo";
        } else {
            throw new IllegalArgumentException(tabla);
        }
        try (PreparedStatement ps = cn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                JsonObject o = new JsonObject();
                if (tabla.equals("clientes")) {
                    o.addProperty("id", rs.getInt("IdCliente"));
                    o.addProperty("identidad", rs.getString("Identidad"));
                    o.addProperty("nombreCompleto", rs.getString("NombreCompleto"));
                    o.addProperty("telefono", rs.getString("Telefono"));
                    o.addProperty("correo", rs.getString("Correo"));
                    o.addProperty("direccion", rs.getString("Direccion"));
                    o.addProperty("activo", rs.getBoolean("Activo"));
                } else {
                    o.addProperty("id", rs.getInt("IdVehiculo"));
                    o.addProperty("idCliente", rs.getInt("IdCliente"));
                    o.addProperty("placa", rs.getString("Placa"));
                    o.addProperty("vin", rs.getString("VIN"));
                    o.addProperty("marca", rs.getString("Marca"));
                    o.addProperty("modelo", rs.getString("Modelo"));
                    o.addProperty("anio", rs.getInt("Anio"));
                    o.addProperty("color", rs.getString("Color"));
                    o.addProperty("kilometraje", rs.getInt("Kilometraje"));
                    o.addProperty("tipoCombustible", rs.getString("TipoCombustible"));
                    o.addProperty("activo", rs.getBoolean("Activo"));
                }
                lista.add(o);
            }
        }
        return lista;
    }

    // ---------- escritura ----------
    private void guardar(Connection cn, String tabla, JsonObject o) throws SQLException {
        int id = o.has("id") ? o.get("id").getAsLong() > 0 ? (int) o.get("id").getAsLong() : 0 : 0;
        if (tabla.equals("clientes")) {
            String sql = id > 0
                ? "UPDATE dbo.Clientes SET Identidad=?, NombreCompleto=?, Telefono=?, Correo=?, Direccion=?, Activo=? WHERE IdCliente=?"
                : "INSERT INTO dbo.Clientes (Identidad, NombreCompleto, Telefono, Correo, Direccion, Activo) VALUES (?,?,?,?,?,?)";
            try (PreparedStatement ps = cn.prepareStatement(sql)) {
                ps.setString(1, s(o, "identidad"));
                ps.setString(2, s(o, "nombreCompleto"));
                ps.setString(3, s(o, "telefono"));
                ps.setString(4, s(o, "correo"));
                ps.setString(5, s(o, "direccion"));
                ps.setBoolean(6, b(o, "activo"));
                if (id > 0) { ps.setInt(7, id); }
                ps.executeUpdate();
            }
        } else if (tabla.equals("vehiculos")) {
            String sql = id > 0
                ? "UPDATE dbo.Vehiculos SET IdCliente=?, Placa=?, VIN=?, Marca=?, Modelo=?, Anio=?, Color=?, Kilometraje=?, TipoCombustible=?, Activo=? WHERE IdVehiculo=?"
                : "INSERT INTO dbo.Vehiculos (IdCliente, Placa, VIN, Marca, Modelo, Anio, Color, Kilometraje, TipoCombustible, Activo) VALUES (?,?,?,?,?,?,?,?,?,?)";
            try (PreparedStatement ps = cn.prepareStatement(sql)) {
                ps.setInt(1, o.get("idCliente").getAsInt());
                ps.setString(2, s(o, "placa"));
                ps.setString(3, s(o, "vin"));
                ps.setString(4, s(o, "marca"));
                ps.setString(5, s(o, "modelo"));
                ps.setInt(6, o.get("anio").getAsInt());
                ps.setString(7, s(o, "color"));
                ps.setInt(8, o.get("kilometraje").getAsInt());
                ps.setString(9, s(o, "tipoCombustible"));
                ps.setBoolean(10, b(o, "activo"));
                if (id > 0) { ps.setInt(11, id); }
                ps.executeUpdate();
            }
        } else {
            throw new IllegalArgumentException(tabla);
        }
    }

    private static String s(JsonObject o, String k) {
        return o.has(k) && !o.get(k).isJsonNull() ? o.get(k).getAsString().trim() : null;
    }

    private static boolean b(JsonObject o, String k) {
        return !o.has(k) || o.get(k).isJsonNull() || o.get(k).getAsBoolean();
    }

    private static void enviar(HttpServletResponse resp, int codigo, JsonArray datos) throws IOException {
        resp.setStatus(codigo);
        resp.setContentType("application/json; charset=UTF-8");
        resp.getWriter().write(GSON.toJson(datos));
    }

    private static void error(HttpServletResponse resp, int codigo, String msg) throws IOException {
        resp.setStatus(codigo);
        resp.setContentType("application/json; charset=UTF-8");
        JsonObject o = new JsonObject();
        o.addProperty("error", msg);
        resp.getWriter().write(GSON.toJson(o));
    }
}
