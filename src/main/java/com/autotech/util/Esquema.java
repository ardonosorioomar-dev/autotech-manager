package com.autotech.util;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/** Crea (una sola vez) las tablas que usa la aplicacion web si todavia no existen. */
public final class Esquema {

    private static volatile boolean listo = false;

    private Esquema() { }

    public static void asegurar(Connection cn) throws SQLException {
        if (listo) { return; }
        synchronized (Esquema.class) {
            if (listo) { return; }
            try (Statement st = cn.createStatement()) {
                st.execute("IF OBJECT_ID(N'dbo.Usuarios', N'U') IS NULL CREATE TABLE dbo.Usuarios ("
                    + "IdUsuario INT IDENTITY(1,1) NOT NULL PRIMARY KEY, Nombre VARCHAR(120) NOT NULL, "
                    + "Correo VARCHAR(100) NOT NULL UNIQUE, PasswordHash VARCHAR(200) NOT NULL, "
                    + "Salt VARCHAR(64) NOT NULL, Activo BIT NOT NULL DEFAULT 1, "
                    + "FechaRegistro DATETIME NOT NULL DEFAULT GETDATE())");
                st.execute("IF OBJECT_ID(N'dbo.Documentos', N'U') IS NULL CREATE TABLE dbo.Documentos ("
                    + "Coleccion VARCHAR(30) NOT NULL, Id INT NOT NULL, Contenido NVARCHAR(MAX) NOT NULL, "
                    + "Actualizado DATETIME NOT NULL DEFAULT GETDATE(), CONSTRAINT PK_Documentos PRIMARY KEY (Coleccion, Id))");
            }
            listo = true;
        }
    }
}
