package com.autotech.controller;

import com.autotech.util.ConexionBD;
import com.autotech.util.Esquema;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Guarda en Azure SQL las colecciones de la aplicacion (mecanicos, servicios, repuestos, motores,
 * ordenes, presupuestos, pagos y cambiosMotor) en la tabla dbo.Documentos.
 * GET  /api/docs/{coleccion} -> lista (204 si aun no se ha guardado nunca)
 * POST /api/docs/{coleccion} -> guarda la lista completa y devuelve la lista con los id asignados
 */
@WebServlet("/api/docs/*")
public class DocsServlet extends HttpServlet {

    private static final Gson GSON = new Gson();
    private static final Set<String> PERMITIDAS = Set.of("mecanicos", "servicios", "repuestos", "motores",
        "ordenes", "presupuestos", "pagos", "cambiosMotor");

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String col = coleccion(req);
        if (col == null) { error(resp, 404, "Coleccion no valida."); return; }
        try (Connection cn = ConexionBD.obtener()) {
            Esquema.asegurar(cn);
            JsonArray lista = leer(cn, col);
            if (lista == null) { resp.setStatus(204); return; }
            enviar(resp, lista);
        } catch (Exception e) {
            error(resp, 500, "Error de base de datos: " + e.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String col = coleccion(req);
        if (col == null) { error(resp, 404, "Coleccion no valida."); return; }
        try (Connection cn = ConexionBD.obtener()) {
            Esquema.asegurar(cn);
            JsonArray datos = JsonParser.parseReader(req.getReader()).getAsJsonArray();
            cn.setAutoCommit(false);
            try {
                guardar(cn, col, datos);
                cn.commit();
            } catch (SQLException ex) {
                cn.rollback();
                throw ex;
            }
            cn.setAutoCommit(true);
            JsonArray lista = leer(cn, col);
            enviar(resp, lista == null ? new JsonArray() : lista);
        } catch (Exception e) {
            error(resp, 400, "No se pudo guardar: " + e.getMessage());
        }
    }

    private static String coleccion(HttpServletRequest req) {
        String p = req.getPathInfo();
        if (p == null || p.length() < 2) { return null; }
        String c = p.substring(1);
        return PERMITIDAS.contains(c) ? c : null;
    }

    private static JsonArray leer(Connection cn, String col) throws SQLException {
        JsonArray lista = new JsonArray();
        boolean hay = false;
        try (PreparedStatement ps = cn.prepareStatement(
                "SELECT Contenido FROM dbo.Documentos WHERE Coleccion = ? ORDER BY Id")) {
            ps.setString(1, col);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    hay = true;
                    lista.add(JsonParser.parseString(rs.getString(1)));
                }
            }
        }
        return hay ? lista : null;
    }

    private static void guardar(Connection cn, String col, JsonArray datos) throws SQLException {
        int max = 0;
        // El bloqueo serializa a quienes guardan la misma coleccion al mismo tiempo
        try (PreparedStatement ps = cn.prepareStatement(
                "SELECT ISNULL(MAX(Id), 0) FROM dbo.Documentos WITH (UPDLOCK, HOLDLOCK) WHERE Coleccion = ?")) {
            ps.setString(1, col);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) { max = rs.getInt(1); }
            }
        }
        List<Integer> ids = new ArrayList<>();
        for (JsonElement e : datos) {
            JsonObject o = e.getAsJsonObject();
            long id = o.has("id") && !o.get("id").isJsonNull() ? o.get("id").getAsLong() : 0;
            if (id <= 0) { id = ++max; }
            int idFinal = (int) id;
            o.addProperty("id", idFinal);
            ids.add(idFinal);
            String json = GSON.toJson(o);
            int filas;
            try (PreparedStatement ps = cn.prepareStatement(
                    "UPDATE dbo.Documentos SET Contenido = ?, Actualizado = GETDATE() WHERE Coleccion = ? AND Id = ?")) {
                ps.setString(1, json);
                ps.setString(2, col);
                ps.setInt(3, idFinal);
                filas = ps.executeUpdate();
            }
            if (filas == 0) {
                try (PreparedStatement ps = cn.prepareStatement(
                        "INSERT INTO dbo.Documentos (Coleccion, Id, Contenido) VALUES (?, ?, ?)")) {
                    ps.setString(1, col);
                    ps.setInt(2, idFinal);
                    ps.setString(3, json);
                    ps.executeUpdate();
                }
            }
        }
        // Lo que ya no viene en la lista se elimina (la lista enviada es la version completa)
        StringBuilder sql = new StringBuilder("DELETE FROM dbo.Documentos WHERE Coleccion = ?");
        if (!ids.isEmpty()) {
            sql.append(" AND Id NOT IN (");
            for (int i = 0; i < ids.size(); i++) { sql.append(i == 0 ? "?" : ",?"); }
            sql.append(")");
        }
        try (PreparedStatement ps = cn.prepareStatement(sql.toString())) {
            ps.setString(1, col);
            for (int i = 0; i < ids.size(); i++) { ps.setInt(i + 2, ids.get(i)); }
            ps.executeUpdate();
        }
    }

    private static void enviar(HttpServletResponse resp, JsonArray datos) throws IOException {
        resp.setStatus(200);
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
