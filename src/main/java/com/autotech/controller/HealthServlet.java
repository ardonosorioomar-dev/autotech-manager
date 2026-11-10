package com.autotech.controller;

import com.autotech.util.ConexionBD;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

/** Prueba de conexion: abrir http://localhost:8080/AutoTechWeb/health */
@WebServlet("/health")
public class HealthServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/plain; charset=UTF-8");
        PrintWriter out = resp.getWriter();
        try (Connection cn = ConexionBD.obtener();
             Statement st = cn.createStatement();
             ResultSet rs = st.executeQuery(
                 "SELECT (SELECT COUNT(*) FROM dbo.Servicios) AS Servicios, " +
                 "(SELECT COUNT(*) FROM dbo.Repuestos) AS Repuestos, " +
                 "(SELECT COUNT(*) FROM dbo.Mecanicos) AS Mecanicos")) {
            rs.next();
            out.println("Conexion OK a SQL Server");
            out.println("Servicios: " + rs.getInt("Servicios"));
            out.println("Repuestos: " + rs.getInt("Repuestos"));
            out.println("Mecanicos: " + rs.getInt("Mecanicos"));
        } catch (Exception e) {
            resp.setStatus(500);
            out.println("ERROR de conexion: " + e.getMessage());
        }
    }
}
