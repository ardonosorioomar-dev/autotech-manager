-- Ejecutar en la base AutoTechDB (Azure y local). Crea la tabla de usuarios del login.
IF OBJECT_ID(N'dbo.Usuarios', N'U') IS NULL
CREATE TABLE dbo.Usuarios (
    IdUsuario     INT IDENTITY(1,1) NOT NULL PRIMARY KEY,
    Nombre        VARCHAR(120) NOT NULL,
    Correo        VARCHAR(100) NOT NULL UNIQUE,
    PasswordHash  VARCHAR(200) NOT NULL,
    Salt          VARCHAR(64)  NOT NULL,
    Activo        BIT NOT NULL DEFAULT 1,
    FechaRegistro DATETIME NOT NULL DEFAULT GETDATE()
);
GO
