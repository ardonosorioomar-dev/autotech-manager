package com.autotech.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Conexion JDBC a SQL Server.
 * Local: usa los valores por defecto (abajo).
 * Nube (Render + Azure SQL): define las variables de entorno DB_URL, DB_USER y DB_PASS.
 */
public final class ConexionBD {

    // Local con autenticacion de SQL Server (usuario y clave). Cambia estos datos a los tuyos.
    private static final String URL_LOCAL =
        "jdbc:sqlserver://localhost:1433;databaseName=AutoTechDB;encrypt=true;trustServerCertificate=true";
    private static final String USER_LOCAL = "autotech";
    private static final String PASS_LOCAL = "Autotech2026";

    private ConexionBD() { }

    public static Connection obtener() throws SQLException {
        String url  = env("DB_URL",  URL_LOCAL);
        String user = env("DB_USER", USER_LOCAL);
        String pass = env("DB_PASS", PASS_LOCAL);
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("No se encontro el driver de SQL Server (mssql-jdbc).", e);
        }
        return DriverManager.getConnection(url, user, pass);
    }

    private static String env(String clave, String porDefecto) {
        String v = System.getenv(clave);
        return (v == null || v.isBlank()) ? porDefecto : v;
    }
}
