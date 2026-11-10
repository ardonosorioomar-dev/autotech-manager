package com.autotech.controller;

import com.autotech.util.ConexionBD;
import com.autotech.util.Esquema;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Base64;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/** Login y registro reales contra la tabla dbo.Usuarios. Contrasenas con PBKDF2 + salt. */
@WebServlet("/api/auth/*")
public class AuthServlet extends HttpServlet {

    private static final Gson GSON = new Gson();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String ruta = req.getPathInfo() == null ? "" : req.getPathInfo();
        JsonObject body;
        try {
            body = JsonParser.parseReader(req.getReader()).getAsJsonObject();
        } catch (Exception e) {
            responder(resp, 400, "Solicitud invalida.", null);
            return;
        }
        try {
            if (ruta.equals("/register")) {
                registrar(body, resp);
            } else if (ruta.equals("/login")) {
                iniciar(body, req, resp);
            } else {
                responder(resp, 404, "Ruta no encontrada.", null);
            }
        } catch (SQLException e) {
            responder(resp, 500, "Error de base de datos: " + e.getMessage(), null);
        } catch (Exception e) {
            responder(resp, 500, "Error: " + e.getMessage(), null);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession s = req.getSession(false);
        if ("/logout".equals(req.getPathInfo())) {
            if (s != null) { s.invalidate(); }
            responder(resp, 200, null, "");
            return;
        }
        String nombre = s == null ? null : (String) s.getAttribute("usuario");
        responder(resp, nombre == null ? 401 : 200, nombre == null ? "Sin sesion" : null, nombre);
    }

    private void registrar(JsonObject b, HttpServletResponse resp) throws Exception {
        String nombre = texto(b, "nombre");
        String correo = texto(b, "correo").toLowerCase();
        String pass = texto(b, "password");
        if (nombre.isEmpty() || correo.isEmpty() || !correo.contains("@")) {
            responder(resp, 400, "Escribe tu nombre y un correo valido.", null);
            return;
        }
        if (pass.length() < 6) {
            responder(resp, 400, "La contrasena debe tener al menos 6 caracteres.", null);
            return;
        }
        byte[] salt = new byte[16];
        new SecureRandom().nextBytes(salt);
        try (Connection cn = ConexionBD.obtener()) {
            Esquema.asegurar(cn);
            try (PreparedStatement ps = cn.prepareStatement("SELECT 1 FROM dbo.Usuarios WHERE Correo = ?")) {
                ps.setString(1, correo);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        responder(resp, 409, "Ese correo ya tiene una cuenta.", null);
                        return;
                    }
                }
            }
            try (PreparedStatement ps = cn.prepareStatement(
                    "INSERT INTO dbo.Usuarios (Nombre, Correo, PasswordHash, Salt) VALUES (?, ?, ?, ?)")) {
                ps.setString(1, nombre);
                ps.setString(2, correo);
                ps.setString(3, hash(pass, salt));
                ps.setString(4, Base64.getEncoder().encodeToString(salt));
                ps.executeUpdate();
            }
        }
        responder(resp, 200, null, nombre);
    }

    private void iniciar(JsonObject b, HttpServletRequest req, HttpServletResponse resp) throws Exception {
        String correo = texto(b, "correo").toLowerCase();
        String pass = texto(b, "password");
        try (Connection cn = ConexionBD.obtener();
             PreparedStatement ps = prep(cn,
                 "SELECT Nombre, PasswordHash, Salt FROM dbo.Usuarios WHERE Correo = ? AND Activo = 1")) {
            ps.setString(1, correo);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    byte[] salt = Base64.getDecoder().decode(rs.getString("Salt"));
                    byte[] esperado = rs.getString("PasswordHash").getBytes("UTF-8");
                    byte[] dado = hash(pass, salt).getBytes("UTF-8");
                    if (MessageDigest.isEqual(esperado, dado)) {
                        req.getSession(true).setAttribute("usuario", rs.getString("Nombre"));
                        responder(resp, 200, null, rs.getString("Nombre"));
                        return;
                    }
                }
            }
        }
        responder(resp, 401, "Correo o contrasena incorrectos.", null);
    }

    private static PreparedStatement prep(Connection cn, String sql) throws SQLException {
        Esquema.asegurar(cn);
        return cn.prepareStatement(sql);
    }

    private static String hash(String pass, byte[] salt) throws Exception {
        SecretKeyFactory f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        byte[] h = f.generateSecret(new PBEKeySpec(pass.toCharArray(), salt, 65536, 256)).getEncoded();
        return Base64.getEncoder().encodeToString(h);
    }

    private static String texto(JsonObject o, String k) {
        return o.has(k) && !o.get(k).isJsonNull() ? o.get(k).getAsString().trim() : "";
    }

    private static void responder(HttpServletResponse resp, int codigo, String error, String nombre) throws IOException {
        resp.setStatus(codigo);
        resp.setContentType("application/json; charset=UTF-8");
        JsonObject o = new JsonObject();
        o.addProperty("ok", error == null);
        if (error != null) { o.addProperty("error", error); }
        if (nombre != null) { o.addProperty("nombre", nombre); }
        resp.getWriter().write(GSON.toJson(o));
    }
}
