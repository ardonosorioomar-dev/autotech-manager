-- ============================================================================
-- PROYECTO: AutoTech Manager - Sistema Integral de Gestión para Taller Mecánico
-- ASIGNATURA: Programación Avanzada II | Segundo Parcial
-- CATEDRÁTICO: Master Kevin Alexander Maradiaga
-- BASE DE DATOS: Microsoft SQL Server
-- DESCRIPCIÓN: Script DDL y DML completo con 16 tablas principales, tablas puente,
--              restricciones de integridad, índices, vistas, procedimientos 
--              almacenados transaccionales, triggers de auditoría y datos semilla.
-- ============================================================================


-- 2. ELIMINACIÓN PREVIA EN ORDEN DE DEPENDENCIAS (DROP IF EXISTS)
IF OBJECT_ID('dbo.trg_AuditoriaRepuestos', 'TR') IS NOT NULL DROP TRIGGER dbo.trg_AuditoriaRepuestos;
IF OBJECT_ID('dbo.trg_AuditoriaOrdenes', 'TR') IS NOT NULL DROP TRIGGER dbo.trg_AuditoriaOrdenes;
IF OBJECT_ID('dbo.sp_RegistrarOrdenTrabajo', 'P') IS NOT NULL DROP PROCEDURE dbo.sp_RegistrarOrdenTrabajo;
IF OBJECT_ID('dbo.sp_ProcesarSustitucionMotor', 'P') IS NOT NULL DROP PROCEDURE dbo.sp_ProcesarSustitucionMotor;
IF OBJECT_ID('dbo.sp_RegistrarPagoOrden', 'P') IS NOT NULL DROP PROCEDURE dbo.sp_RegistrarPagoOrden;
IF OBJECT_ID('dbo.vw_ResumenOrdenesTrabajo', 'V') IS NOT NULL DROP VIEW dbo.vw_ResumenOrdenesTrabajo;
IF OBJECT_ID('dbo.vw_InventarioStockCritico', 'V') IS NOT NULL DROP VIEW dbo.vw_InventarioStockCritico;
IF OBJECT_ID('dbo.vw_HistorialVehiculoCompleto', 'V') IS NOT NULL DROP VIEW dbo.vw_HistorialVehiculoCompleto;

IF OBJECT_ID('dbo.AuditoriaRepuestos', 'U') IS NOT NULL DROP TABLE dbo.AuditoriaRepuestos;
IF OBJECT_ID('dbo.AuditoriaOrdenes', 'U') IS NOT NULL DROP TABLE dbo.AuditoriaOrdenes;
IF OBJECT_ID('dbo.ControlesCalidad', 'U') IS NOT NULL DROP TABLE dbo.ControlesCalidad;
IF OBJECT_ID('dbo.ActividadesTrabajo', 'U') IS NOT NULL DROP TABLE dbo.ActividadesTrabajo;
IF OBJECT_ID('dbo.CambiosMotor', 'U') IS NOT NULL DROP TABLE dbo.CambiosMotor;
IF OBJECT_ID('dbo.Pagos', 'U') IS NOT NULL DROP TABLE dbo.Pagos;
IF OBJECT_ID('dbo.DetallePresupuestoRepuestos', 'U') IS NOT NULL DROP TABLE dbo.DetallePresupuestoRepuestos;
IF OBJECT_ID('dbo.DetallePresupuestoServicios', 'U') IS NOT NULL DROP TABLE dbo.DetallePresupuestoServicios;
IF OBJECT_ID('dbo.Presupuestos', 'U') IS NOT NULL DROP TABLE dbo.Presupuestos;
IF OBJECT_ID('dbo.MovimientosInventario', 'U') IS NOT NULL DROP TABLE dbo.MovimientosInventario;
IF OBJECT_ID('dbo.Motores', 'U') IS NOT NULL DROP TABLE dbo.Motores;
IF OBJECT_ID('dbo.Repuestos', 'U') IS NOT NULL DROP TABLE dbo.Repuestos;
IF OBJECT_ID('dbo.Proveedores', 'U') IS NOT NULL DROP TABLE dbo.Proveedores;
IF OBJECT_ID('dbo.Diagnosticos', 'U') IS NOT NULL DROP TABLE dbo.Diagnosticos;
IF OBJECT_ID('dbo.OrdenMecanicos', 'U') IS NOT NULL DROP TABLE dbo.OrdenMecanicos;
IF OBJECT_ID('dbo.DetalleOrden', 'U') IS NOT NULL DROP TABLE dbo.DetalleOrden;
IF OBJECT_ID('dbo.OrdenesTrabajo', 'U') IS NOT NULL DROP TABLE dbo.OrdenesTrabajo;
IF OBJECT_ID('dbo.Mecanicos', 'U') IS NOT NULL DROP TABLE dbo.Mecanicos;
IF OBJECT_ID('dbo.Servicios', 'U') IS NOT NULL DROP TABLE dbo.Servicios;
IF OBJECT_ID('dbo.Vehiculos', 'U') IS NOT NULL DROP TABLE dbo.Vehiculos;
IF OBJECT_ID('dbo.Clientes', 'U') IS NOT NULL DROP TABLE dbo.Clientes;
GO

-- ============================================================================
-- 3. TABLAS PRINCIPALES (16 REQUERIDAS + TABLAS PUENTE Y AUDITORÍA)
-- ============================================================================

-- TABLA 1: Clientes (Propietarios)
CREATE TABLE dbo.Clientes (
    IdCliente INT IDENTITY(1,1) PRIMARY KEY,
    Identidad VARCHAR(20) NOT NULL UNIQUE,
    NombreCompleto VARCHAR(120) NOT NULL,
    Telefono VARCHAR(25) NOT NULL,
    Correo VARCHAR(100) NOT NULL,
    Direccion VARCHAR(200) NULL,
    Activo BIT NOT NULL DEFAULT 1,
    FechaRegistro DATETIME NOT NULL DEFAULT GETDATE()
);
GO

-- TABLA 2: Vehiculos (Vehículos de clientes)
CREATE TABLE dbo.Vehiculos (
    IdVehiculo INT IDENTITY(1,1) PRIMARY KEY,
    IdCliente INT NOT NULL,
    Placa VARCHAR(15) NOT NULL UNIQUE,
    VIN VARCHAR(30) NOT NULL UNIQUE,
    Marca VARCHAR(50) NOT NULL,
    Modelo VARCHAR(50) NOT NULL,
    Anio INT NOT NULL CHECK (Anio BETWEEN 1970 AND YEAR(GETDATE()) + 1),
    Color VARCHAR(30) NOT NULL,
    Kilometraje INT NOT NULL CHECK (Kilometraje >= 0),
    TipoCombustible VARCHAR(20) NOT NULL CHECK (TipoCombustible IN ('Gasolina', 'Diesel', 'Hibrido', 'Electrico', 'Gas')),
    Activo BIT NOT NULL DEFAULT 1,
    FechaRegistro DATETIME NOT NULL DEFAULT GETDATE(),
    CONSTRAINT FK_Vehiculos_Clientes FOREIGN KEY (IdCliente) REFERENCES dbo.Clientes(IdCliente)
);
GO

-- TABLA 3: Mecanicos (Personal y especialidades)
CREATE TABLE dbo.Mecanicos (
    IdMecanico INT IDENTITY(1,1) PRIMARY KEY,
    Identidad VARCHAR(20) NOT NULL UNIQUE,
    NombreCompleto VARCHAR(120) NOT NULL,
    Especialidad VARCHAR(80) NOT NULL,
    Telefono VARCHAR(25) NOT NULL,
    Activo BIT NOT NULL DEFAULT 1,
    Disponible BIT NOT NULL DEFAULT 1,
    FechaContratacion DATE NOT NULL DEFAULT CAST(GETDATE() AS DATE)
);
GO

-- TABLA 4: Servicios (Catálogo clasificado por niveles 1 al 5)
CREATE TABLE dbo.Servicios (
    IdServicio INT IDENTITY(1,1) PRIMARY KEY,
    Codigo VARCHAR(20) NOT NULL UNIQUE,
    Nombre VARCHAR(100) NOT NULL,
    Descripcion VARCHAR(255) NOT NULL,
    NivelComplejidad INT NOT NULL CHECK (NivelComplejidad BETWEEN 1 AND 5),
    Categoria VARCHAR(50) NOT NULL,
    CostoEstimadoManoObra DECIMAL(12,2) NOT NULL CHECK (CostoEstimadoManoObra >= 0),
    TiempoEstimadoHoras DECIMAL(5,2) NOT NULL CHECK (TiempoEstimadoHoras > 0),
    Activo BIT NOT NULL DEFAULT 1
);
GO

-- TABLA 5: OrdenesTrabajo (Encabezado y flujo de estados)
CREATE TABLE dbo.OrdenesTrabajo (
    IdOrden INT IDENTITY(1,1) PRIMARY KEY,
    NumeroOrden VARCHAR(30) NOT NULL UNIQUE,
    IdCliente INT NOT NULL,
    IdVehiculo INT NOT NULL,
    FechaIngreso DATETIME NOT NULL DEFAULT GETDATE(),
    FechaCompromiso DATETIME NULL,
    FechaFinalizacion DATETIME NULL,
    KilometrajeIngreso INT NOT NULL CHECK (KilometrajeIngreso >= 0),
    SintomasCliente VARCHAR(500) NOT NULL,
    DiagnosticoPreliminar VARCHAR(500) NULL,
    Estado VARCHAR(35) NOT NULL DEFAULT 'Recibida' CHECK (
        Estado IN (
            'Recibida', 
            'En diagnóstico', 
            'Pendiente de aprobación', 
            'En reparación', 
            'En control de calidad', 
            'Finalizada y entregada', 
            'Rechazada', 
            'Cancelada'
        )
    ),
    SubtotalServicios DECIMAL(12,2) NOT NULL DEFAULT 0.00 CHECK (SubtotalServicios >= 0),
    SubtotalRepuestos DECIMAL(12,2) NOT NULL DEFAULT 0.00 CHECK (SubtotalRepuestos >= 0),
    Descuento DECIMAL(12,2) NOT NULL DEFAULT 0.00 CHECK (Descuento >= 0),
    ImpuestoISV DECIMAL(12,2) NOT NULL DEFAULT 0.00 CHECK (ImpuestoISV >= 0),
    TotalOrden DECIMAL(12,2) NOT NULL DEFAULT 0.00 CHECK (TotalOrden >= 0),
    SaldoPendiente DECIMAL(12,2) NOT NULL DEFAULT 0.00 CHECK (SaldoPendiente >= 0),
    CONSTRAINT FK_Ordenes_Clientes FOREIGN KEY (IdCliente) REFERENCES dbo.Clientes(IdCliente),
    CONSTRAINT FK_Ordenes_Vehiculos FOREIGN KEY (IdVehiculo) REFERENCES dbo.Vehiculos(IdVehiculo)
);
GO

-- TABLA PUENTE: OrdenMecanicos (Asignación múltiple con un responsable principal)
CREATE TABLE dbo.OrdenMecanicos (
    IdOrdenMecanico INT IDENTITY(1,1) PRIMARY KEY,
    IdOrden INT NOT NULL,
    IdMecanico INT NOT NULL,
    EsResponsablePrincipal BIT NOT NULL DEFAULT 0,
    FechaAsignacion DATETIME NOT NULL DEFAULT GETDATE(),
    CONSTRAINT FK_OM_Orden FOREIGN KEY (IdOrden) REFERENCES dbo.OrdenesTrabajo(IdOrden),
    CONSTRAINT FK_OM_Mecanico FOREIGN KEY (IdMecanico) REFERENCES dbo.Mecanicos(IdMecanico),
    CONSTRAINT UQ_Orden_Mecanico UNIQUE (IdOrden, IdMecanico)
);
GO

-- TABLA 6: DetalleOrden (Servicios por orden con congelamiento de precio histórico)
CREATE TABLE dbo.DetalleOrden (
    IdDetalleOrden INT IDENTITY(1,1) PRIMARY KEY,
    IdOrden INT NOT NULL,
    IdServicio INT NOT NULL,
    PrecioUnitarioCobrado DECIMAL(12,2) NOT NULL CHECK (PrecioUnitarioCobrado >= 0),
    HorasReales DECIMAL(5,2) NOT NULL DEFAULT 1.0 CHECK (HorasReales > 0),
    Subtotal DECIMAL(12,2) NOT NULL CHECK (Subtotal >= 0),
    Observaciones VARCHAR(255) NULL,
    CONSTRAINT FK_DO_Orden FOREIGN KEY (IdOrden) REFERENCES dbo.OrdenesTrabajo(IdOrden),
    CONSTRAINT FK_DO_Servicio FOREIGN KEY (IdServicio) REFERENCES dbo.Servicios(IdServicio)
);
GO

-- TABLA 7: Diagnosticos (Evaluaciones técnicas)
CREATE TABLE dbo.Diagnosticos (
    IdDiagnostico INT IDENTITY(1,1) PRIMARY KEY,
    IdOrden INT NOT NULL,
    IdMecanico INT NOT NULL,
    FechaDiagnostico DATETIME NOT NULL DEFAULT GETDATE(),
    HallazgosTecnicos VARCHAR(1000) NOT NULL,
    EvidenciasFotograficas VARCHAR(500) NULL,
    RequiereCambioMotor BIT NOT NULL DEFAULT 0,
    AprobadoParaPresupuesto BIT NOT NULL DEFAULT 1,
    CONSTRAINT FK_Diag_Orden FOREIGN KEY (IdOrden) REFERENCES dbo.OrdenesTrabajo(IdOrden),
    CONSTRAINT FK_Diag_Mecanico FOREIGN KEY (IdMecanico) REFERENCES dbo.Mecanicos(IdMecanico)
);
GO

-- TABLA 8: Proveedores (Suministradores)
CREATE TABLE dbo.Proveedores (
    IdProveedor INT IDENTITY(1,1) PRIMARY KEY,
    RTN VARCHAR(20) NOT NULL UNIQUE,
    NombreEmpresa VARCHAR(120) NOT NULL,
    Contacto VARCHAR(100) NOT NULL,
    Telefono VARCHAR(25) NOT NULL,
    Correo VARCHAR(100) NOT NULL,
    Direccion VARCHAR(200) NULL,
    Activo BIT NOT NULL DEFAULT 1
);
GO

-- TABLA 9: Repuestos (Inventario general con stock mínimo y costos)
CREATE TABLE dbo.Repuestos (
    IdRepuesto INT IDENTITY(1,1) PRIMARY KEY,
    IdProveedor INT NOT NULL,
    Codigo VARCHAR(30) NOT NULL UNIQUE,
    Nombre VARCHAR(120) NOT NULL,
    Categoria VARCHAR(50) NOT NULL,
    StockActual INT NOT NULL DEFAULT 0 CHECK (StockActual >= 0),
    StockMinimo INT NOT NULL DEFAULT 2 CHECK (StockMinimo >= 0),
    CostoUnitario DECIMAL(12,2) NOT NULL CHECK (CostoUnitario >= 0),
    PrecioVenta DECIMAL(12,2) NOT NULL CHECK (PrecioVenta >= 0),
    Activo BIT NOT NULL DEFAULT 1,
    CONSTRAINT FK_Repuestos_Proveedores FOREIGN KEY (IdProveedor) REFERENCES dbo.Proveedores(IdProveedor)
);
GO

-- TABLA 10: Motores (Trazabilidad estricta de motores para sustitución)
CREATE TABLE dbo.Motores (
    IdMotor INT IDENTITY(1,1) PRIMARY KEY,
    IdRepuesto INT NULL,
    NumeroSerie VARCHAR(60) NOT NULL UNIQUE,
    CodigoMotor VARCHAR(40) NOT NULL,
    CilindradaLitros DECIMAL(3,1) NOT NULL,
    TipoCombustible VARCHAR(20) NOT NULL,
    MarcaCompatible VARCHAR(50) NOT NULL,
    ModeloCompatible VARCHAR(50) NOT NULL,
    AnioInicioCompatible INT NOT NULL,
    AnioFinCompatible INT NOT NULL,
    Estado VARCHAR(30) NOT NULL DEFAULT 'Disponible' CHECK (
        Estado IN ('Disponible', 'Instalado', 'Dado de baja', 'En revisión')
    ),
    UbicacionTaller VARCHAR(50) NOT NULL DEFAULT 'Bodega Motores A-1',
    Observaciones VARCHAR(255) NULL,
    CONSTRAINT FK_Motores_Repuesto FOREIGN KEY (IdRepuesto) REFERENCES dbo.Repuestos(IdRepuesto)
);
GO

-- TABLA 11: MovimientosInventario (Entradas y salidas atómicas)
CREATE TABLE dbo.MovimientosInventario (
    IdMovimiento INT IDENTITY(1,1) PRIMARY KEY,
    IdRepuesto INT NOT NULL,
    IdOrden INT NULL,
    TipoMovimiento VARCHAR(20) NOT NULL CHECK (TipoMovimiento IN ('ENTRADA', 'SALIDA', 'AJUSTE', 'REVERSION')),
    Cantidad INT NOT NULL CHECK (Cantidad > 0),
    CostoMomento DECIMAL(12,2) NOT NULL CHECK (CostoMomento >= 0),
    PrecioMomento DECIMAL(12,2) NOT NULL CHECK (PrecioMomento >= 0),
    FechaMovimiento DATETIME NOT NULL DEFAULT GETDATE(),
    Motivo VARCHAR(255) NOT NULL,
    Usuario VARCHAR(50) NOT NULL DEFAULT 'Sistema',
    CONSTRAINT FK_Mov_Repuesto FOREIGN KEY (IdRepuesto) REFERENCES dbo.Repuestos(IdRepuesto),
    CONSTRAINT FK_Mov_Orden FOREIGN KEY (IdOrden) REFERENCES dbo.OrdenesTrabajo(IdOrden)
);
GO

-- TABLA 12: Presupuestos (Cotizaciones y aprobaciones)
CREATE TABLE dbo.Presupuestos (
    IdPresupuesto INT IDENTITY(1,1) PRIMARY KEY,
    IdOrden INT NOT NULL UNIQUE,
    NumeroPresupuesto VARCHAR(30) NOT NULL UNIQUE,
    FechaEmision DATETIME NOT NULL DEFAULT GETDATE(),
    SubtotalServicios DECIMAL(12,2) NOT NULL DEFAULT 0.00 CHECK (SubtotalServicios >= 0),
    SubtotalRepuestos DECIMAL(12,2) NOT NULL DEFAULT 0.00 CHECK (SubtotalRepuestos >= 0),
    PorcentajeDescuento DECIMAL(5,2) NOT NULL DEFAULT 0.00 CHECK (PorcentajeDescuento BETWEEN 0 AND 100),
    MontoDescuento DECIMAL(12,2) NOT NULL DEFAULT 0.00 CHECK (MontoDescuento >= 0),
    PorcentajeISV DECIMAL(5,2) NOT NULL DEFAULT 15.00 CHECK (PorcentajeISV >= 0),
    MontoISV DECIMAL(12,2) NOT NULL DEFAULT 0.00 CHECK (MontoISV >= 0),
    TotalGeneral DECIMAL(12,2) NOT NULL DEFAULT 0.00 CHECK (TotalGeneral >= 0),
    EstadoPresupuesto VARCHAR(20) NOT NULL DEFAULT 'Borrador' CHECK (
        EstadoPresupuesto IN ('Borrador', 'Presentado', 'Aprobado', 'Rechazado')
    ),
    FechaRespuestaCliente DATETIME NULL,
    Observaciones VARCHAR(500) NULL,
    CONSTRAINT FK_Presupuesto_Orden FOREIGN KEY (IdOrden) REFERENCES dbo.OrdenesTrabajo(IdOrden)
);
GO

-- TABLA PUENTE: DetallePresupuestoServicios
CREATE TABLE dbo.DetallePresupuestoServicios (
    IdDetalleServicio INT IDENTITY(1,1) PRIMARY KEY,
    IdPresupuesto INT NOT NULL,
    IdServicio INT NOT NULL,
    CostoManoObra DECIMAL(12,2) NOT NULL CHECK (CostoManoObra >= 0),
    HorasPresupuestadas DECIMAL(5,2) NOT NULL CHECK (HorasPresupuestadas > 0),
    Subtotal DECIMAL(12,2) NOT NULL CHECK (Subtotal >= 0),
    CONSTRAINT FK_DPS_Presupuesto FOREIGN KEY (IdPresupuesto) REFERENCES dbo.Presupuestos(IdPresupuesto),
    CONSTRAINT FK_DPS_Servicio FOREIGN KEY (IdServicio) REFERENCES dbo.Servicios(IdServicio)
);
GO

-- TABLA PUENTE: DetallePresupuestoRepuestos
CREATE TABLE dbo.DetallePresupuestoRepuestos (
    IdDetalleRepuesto INT IDENTITY(1,1) PRIMARY KEY,
    IdPresupuesto INT NOT NULL,
    IdRepuesto INT NOT NULL,
    Cantidad INT NOT NULL CHECK (Cantidad > 0),
    PrecioUnitario DECIMAL(12,2) NOT NULL CHECK (PrecioUnitario >= 0),
    Subtotal DECIMAL(12,2) NOT NULL CHECK (Subtotal >= 0),
    CONSTRAINT FK_DPR_Presupuesto FOREIGN KEY (IdPresupuesto) REFERENCES dbo.Presupuestos(IdPresupuesto),
    CONSTRAINT FK_DPR_Repuesto FOREIGN KEY (IdRepuesto) REFERENCES dbo.Repuestos(IdRepuesto)
);
GO

-- TABLA 13: CambiosMotor (Caso obligatorio: Registro completo de sustitución de motores)
CREATE TABLE dbo.CambiosMotor (
    IdCambioMotor INT IDENTITY(1,1) PRIMARY KEY,
    IdOrden INT NOT NULL UNIQUE,
    IdVehiculo INT NOT NULL,
    IdMotorNuevo INT NOT NULL,
    SerieMotorAnterior VARCHAR(60) NOT NULL,
    SerieMotorNuevo VARCHAR(60) NOT NULL,
    CausaCambio VARCHAR(300) NOT NULL,
    DestinoMotorRetirado VARCHAR(150) NOT NULL DEFAULT 'Almacén de Chatarra / Retorno al Cliente',
    ResultadosPruebasCompresion VARCHAR(300) NULL,
    GarantiaMeses INT NOT NULL DEFAULT 12 CHECK (GarantiaMeses >= 0),
    GarantiaKilometros INT NOT NULL DEFAULT 20000 CHECK (GarantiaKilometros >= 0),
    CertificadoGarantia VARCHAR(50) NOT NULL UNIQUE,
    FechaInstalacion DATETIME NOT NULL DEFAULT GETDATE(),
    AprobadoControlCalidad BIT NOT NULL DEFAULT 0,
    CONSTRAINT FK_CM_Orden FOREIGN KEY (IdOrden) REFERENCES dbo.OrdenesTrabajo(IdOrden),
    CONSTRAINT FK_CM_Vehiculo FOREIGN KEY (IdVehiculo) REFERENCES dbo.Vehiculos(IdVehiculo),
    CONSTRAINT FK_CM_MotorNuevo FOREIGN KEY (IdMotorNuevo) REFERENCES dbo.Motores(IdMotor)
);
GO

-- TABLA 14: ActividadesTrabajo (Actividades por mecánico)
CREATE TABLE dbo.ActividadesTrabajo (
    IdActividad INT IDENTITY(1,1) PRIMARY KEY,
    IdOrden INT NOT NULL,
    IdMecanico INT NOT NULL,
    DescripcionActividad VARCHAR(255) NOT NULL,
    HorasTrabajadas DECIMAL(5,2) NOT NULL CHECK (HorasTrabajadas > 0),
    FechaInicio DATETIME NOT NULL,
    FechaFin DATETIME NULL,
    Completada BIT NOT NULL DEFAULT 0,
    CONSTRAINT FK_Act_Orden FOREIGN KEY (IdOrden) REFERENCES dbo.OrdenesTrabajo(IdOrden),
    CONSTRAINT FK_Act_Mecanico FOREIGN KEY (IdMecanico) REFERENCES dbo.Mecanicos(IdMecanico)
);
GO

-- TABLA 15: ControlesCalidad (Verificaciones antes de finalizar orden)
CREATE TABLE dbo.ControlesCalidad (
    IdControl INT IDENTITY(1,1) PRIMARY KEY,
    IdOrden INT NOT NULL,
    IdMecanicoInspector INT NOT NULL,
    FechaInspeccion DATETIME NOT NULL DEFAULT GETDATE(),
    PruebaRutaRealizada BIT NOT NULL DEFAULT 0,
    InspeccionFugasAprobada BIT NOT NULL DEFAULT 0,
    EscaneoComputarizadoLimpio BIT NOT NULL DEFAULT 0,
    TorquesVerificados BIT NOT NULL DEFAULT 0,
    AprobadoParaEntrega BIT NOT NULL DEFAULT 0,
    ObservacionesFinales VARCHAR(500) NOT NULL,
    CONSTRAINT FK_CC_Orden FOREIGN KEY (IdOrden) REFERENCES dbo.OrdenesTrabajo(IdOrden),
    CONSTRAINT FK_CC_Inspector FOREIGN KEY (IdMecanicoInspector) REFERENCES dbo.Mecanicos(IdMecanico)
);
GO

-- TABLA 16: Pagos (Abonos y pagos parciales o totales con prevención de sobrepagos)
CREATE TABLE dbo.Pagos (
    IdPago INT IDENTITY(1,1) PRIMARY KEY,
    IdOrden INT NOT NULL,
    NumeroRecibo VARCHAR(30) NOT NULL UNIQUE,
    Monto DECIMAL(12,2) NOT NULL CHECK (Monto > 0),
    MetodoPago VARCHAR(30) NOT NULL CHECK (MetodoPago IN ('Efectivo', 'Tarjeta de Credito', 'Tarjeta de Debito', 'Transferencia', 'Cheque')),
    ReferenciaTransaccion VARCHAR(50) NULL,
    FechaPago DATETIME NOT NULL DEFAULT GETDATE(),
    Notas VARCHAR(200) NULL,
    CONSTRAINT FK_Pagos_Orden FOREIGN KEY (IdOrden) REFERENCES dbo.OrdenesTrabajo(IdOrden)
);
GO

-- TABLAS DE AUDITORÍA (Cumplimiento de Requerimientos de SQL Server)
CREATE TABLE dbo.AuditoriaRepuestos (
    IdAuditoria INT IDENTITY(1,1) PRIMARY KEY,
    IdRepuesto INT NOT NULL,
    Accion VARCHAR(10) NOT NULL,
    StockAnterior INT NULL,
    StockNuevo INT NULL,
    PrecioAnterior DECIMAL(12,2) NULL,
    PrecioNuevo DECIMAL(12,2) NULL,
    FechaModificacion DATETIME NOT NULL DEFAULT GETDATE(),
    Usuario VARCHAR(60) NOT NULL DEFAULT SUSER_SNAME()
);
GO

CREATE TABLE dbo.AuditoriaOrdenes (
    IdAuditoria INT IDENTITY(1,1) PRIMARY KEY,
    IdOrden INT NOT NULL,
    EstadoAnterior VARCHAR(35) NULL,
    EstadoNuevo VARCHAR(35) NULL,
    TotalAnterior DECIMAL(12,2) NULL,
    TotalNuevo DECIMAL(12,2) NULL,
    FechaModificacion DATETIME NOT NULL DEFAULT GETDATE(),
    Usuario VARCHAR(60) NOT NULL DEFAULT SUSER_SNAME()
);
GO

-- ============================================================================
-- 4. ÍNDICES DE RENDIMIENTO Y CLAVES FORÁNEAS
-- ============================================================================
CREATE INDEX IX_Vehiculos_IdCliente ON dbo.Vehiculos(IdCliente);
CREATE INDEX IX_Ordenes_Vehiculo ON dbo.OrdenesTrabajo(IdVehiculo);
CREATE INDEX IX_Ordenes_Estado ON dbo.OrdenesTrabajo(Estado);
CREATE INDEX IX_Repuestos_Stock ON dbo.Repuestos(StockActual, StockMinimo);
CREATE INDEX IX_Motores_Compatibilidad ON dbo.Motores(MarcaCompatible, ModeloCompatible, Estado);
GO

-- ============================================================================
-- 5. VISTAS ADMINISTRATIVAS (MÍNIMO 2 REQUERIDAS)
-- ============================================================================

-- VISTA 1: Resumen de Órdenes de Trabajo Activas
CREATE VIEW dbo.vw_ResumenOrdenesTrabajo AS
SELECT 
    o.IdOrden,
    o.NumeroOrden,
    o.FechaIngreso,
    o.Estado,
    c.NombreCompleto AS Cliente,
    c.Telefono AS TelefonoCliente,
    v.Placa,
    v.Marca + ' ' + v.Modelo + ' (' + CAST(v.Anio AS VARCHAR) + ')' AS Vehiculo,
    ISNULL(m.NombreCompleto, 'Sin Asignar') AS MecanicoLider,
    o.TotalOrden,
    o.SaldoPendiente,
    (SELECT COUNT(*) FROM dbo.ActividadesTrabajo a WHERE a.IdOrden = o.IdOrden AND a.Completada = 0) AS ActividadesPendientes,
    ISNULL((SELECT cc.AprobadoParaEntrega FROM dbo.ControlesCalidad cc WHERE cc.IdOrden = o.IdOrden), 0) AS ControlCalidadAprobado
FROM dbo.OrdenesTrabajo o
INNER JOIN dbo.Clientes c ON o.IdCliente = c.IdCliente
INNER JOIN dbo.Vehiculos v ON o.IdVehiculo = v.IdVehiculo
LEFT JOIN dbo.OrdenMecanicos om ON o.IdOrden = om.IdOrden AND om.EsResponsablePrincipal = 1
LEFT JOIN dbo.Mecanicos m ON om.IdMecanico = m.IdMecanico;
GO

-- VISTA 2: Inventario bajo mínimo con alerta de reposición
CREATE VIEW dbo.vw_InventarioStockCritico AS
SELECT 
    r.IdRepuesto,
    r.Codigo,
    r.Nombre AS Repuesto,
    r.Categoria,
    r.StockActual,
    r.StockMinimo,
    (r.StockMinimo - r.StockActual) AS UnidadesFaltantes,
    r.CostoUnitario,
    r.PrecioVenta,
    p.NombreEmpresa AS Proveedor,
    p.Telefono AS TelefonoProveedor
FROM dbo.Repuestos r
INNER JOIN dbo.Proveedores p ON r.IdProveedor = p.IdProveedor
WHERE r.StockActual <= r.StockMinimo AND r.Activo = 1;
GO

-- VISTA 3: Historial Integral por Vehículo (Fechas, Servicios, Mecánicos, Repuestos, Costos)
CREATE VIEW dbo.vw_HistorialVehiculoCompleto AS
SELECT 
    v.Placa,
    v.VIN,
    v.Marca + ' ' + v.Modelo + ' ' + CAST(v.Anio AS VARCHAR) AS Vehiculo,
    c.NombreCompleto AS Propietario,
    o.NumeroOrden,
    o.FechaIngreso,
    o.FechaFinalizacion,
    o.Estado AS EstadoOrden,
    s.Nombre AS ServicioRealizado,
    s.NivelComplejidad,
    d.Subtotal AS CostoServicio,
    m.NombreCompleto AS MecanicoResponsable,
    o.TotalOrden,
    cm.SerieMotorNuevo AS MotorInstaladoSustitucion
FROM dbo.Vehiculos v
INNER JOIN dbo.Clientes c ON v.IdCliente = c.IdCliente
INNER JOIN dbo.OrdenesTrabajo o ON v.IdVehiculo = o.IdVehiculo
LEFT JOIN dbo.DetalleOrden d ON o.IdOrden = d.IdOrden
LEFT JOIN dbo.Servicios s ON d.IdServicio = s.IdServicio
LEFT JOIN dbo.OrdenMecanicos om ON o.IdOrden = om.IdOrden AND om.EsResponsablePrincipal = 1
LEFT JOIN dbo.Mecanicos m ON om.IdMecanico = m.IdMecanico
LEFT JOIN dbo.CambiosMotor cm ON o.IdOrden = cm.IdOrden;
GO

-- ============================================================================
-- 6. TRIGGERS DE AUDITORÍA (MÍNIMO 1 REQUERIDO)
-- ============================================================================

-- TRIGGER 1: Auditoría de cambios en Stock y Precio de Repuestos
CREATE TRIGGER dbo.trg_AuditoriaRepuestos
ON dbo.Repuestos
AFTER UPDATE, DELETE
AS
BEGIN
    SET NOCOUNT ON;
    
    IF EXISTS (SELECT * FROM inserted) AND EXISTS (SELECT * FROM deleted)
    BEGIN
        -- UPDATE
        INSERT INTO dbo.AuditoriaRepuestos (IdRepuesto, Accion, StockAnterior, StockNuevo, PrecioAnterior, PrecioNuevo, FechaModificacion, Usuario)
        SELECT 
            i.IdRepuesto,
            'UPDATE',
            d.StockActual,
            i.StockActual,
            d.PrecioVenta,
            i.PrecioVenta,
            GETDATE(),
            SUSER_SNAME()
        FROM inserted i
        INNER JOIN deleted d ON i.IdRepuesto = d.IdRepuesto
        WHERE i.StockActual <> d.StockActual OR i.PrecioVenta <> d.PrecioVenta;
    END
    ELSE IF EXISTS (SELECT * FROM deleted)
    BEGIN
        -- DELETE
        INSERT INTO dbo.AuditoriaRepuestos (IdRepuesto, Accion, StockAnterior, StockNuevo, PrecioAnterior, PrecioNuevo, FechaModificacion, Usuario)
        SELECT 
            d.IdRepuesto,
            'DELETE',
            d.StockActual,
            NULL,
            d.PrecioVenta,
            NULL,
            GETDATE(),
            SUSER_SNAME()
        FROM deleted d;
    END
END;
GO

-- TRIGGER 2: Auditoría de cambios de Estado en Órdenes de Trabajo
CREATE TRIGGER dbo.trg_AuditoriaOrdenes
ON dbo.OrdenesTrabajo
AFTER UPDATE
AS
BEGIN
    SET NOCOUNT ON;
    
    INSERT INTO dbo.AuditoriaOrdenes (IdOrden, EstadoAnterior, EstadoNuevo, TotalAnterior, TotalNuevo, FechaModificacion, Usuario)
    SELECT 
        i.IdOrden,
        d.Estado,
        i.Estado,
        d.TotalOrden,
        i.TotalOrden,
        GETDATE(),
        SUSER_SNAME()
    FROM inserted i
    INNER JOIN deleted d ON i.IdOrden = d.IdOrden
    WHERE i.Estado <> d.Estado OR i.TotalOrden <> d.TotalOrden;
END;
GO

-- ============================================================================
-- 7. PROCEDIMIENTOS ALMACENADOS TRANSACCIONALES (MÍNIMO 3 REQUERIDOS)
-- ============================================================================

-- PROCEDIMIENTO 1: Apertura de Orden de Trabajo con Asignación de Mecánico Líder
CREATE PROCEDURE dbo.sp_RegistrarOrdenTrabajo
    @IdCliente INT,
    @IdVehiculo INT,
    @Kilometraje INT,
    @Sintomas VARCHAR(500),
    @DiagnosticoPreliminar VARCHAR(500),
    @IdMecanicoPrincipal INT,
    @NuevoIdOrden INT OUTPUT,
    @NuevoNumeroOrden VARCHAR(30) OUTPUT
AS
BEGIN
    SET NOCOUNT ON;
    BEGIN TRY
        BEGIN TRANSACTION;

        -- Validar que el cliente esté activo
        IF NOT EXISTS (SELECT 1 FROM dbo.Clientes WHERE IdCliente = @IdCliente AND Activo = 1)
        BEGIN
            RAISERROR('El cliente no existe o se encuentra inactivo.', 16, 1);
        END

        -- Validar que el vehículo pertenezca al cliente
        IF NOT EXISTS (SELECT 1 FROM dbo.Vehiculos WHERE IdVehiculo = @IdVehiculo AND IdCliente = @IdCliente)
        BEGIN
            RAISERROR('El vehículo seleccionado no corresponde al cliente registrado.', 16, 1);
        END

        -- Validar mecánico activo y disponible
        IF NOT EXISTS (SELECT 1 FROM dbo.Mecanicos WHERE IdMecanico = @IdMecanicoPrincipal AND Activo = 1)
        BEGIN
            RAISERROR('El mecánico líder seleccionado no está activo.', 16, 1);
        END

        -- Generar código secuencial de orden: ORD-YYYY-XXXXX
        DECLARE @AnioActual INT = YEAR(GETDATE());
        DECLARE @Secuencia INT;
        
        SELECT @Secuencia = ISNULL(MAX(IdOrden), 0) + 1 FROM dbo.OrdenesTrabajo;
        SET @NuevoNumeroOrden = 'ORD-' + CAST(@AnioActual AS VARCHAR) + '-' + RIGHT('00000' + CAST(@Secuencia AS VARCHAR), 5);

        -- Insertar Orden de Trabajo
        INSERT INTO dbo.OrdenesTrabajo (
            NumeroOrden, IdCliente, IdVehiculo, KilometrajeIngreso, SintomasCliente, DiagnosticoPreliminar, Estado
        ) VALUES (
            @NuevoNumeroOrden, @IdCliente, @IdVehiculo, @Kilometraje, @Sintomas, @DiagnosticoPreliminar, 'Recibida'
        );

        SET @NuevoIdOrden = SCOPE_IDENTITY();

        -- Asignar Mecánico Líder
        INSERT INTO dbo.OrdenMecanicos (IdOrden, IdMecanico, EsResponsablePrincipal)
        VALUES (@NuevoIdOrden, @IdMecanicoPrincipal, 1);

        -- Actualizar kilometraje del vehículo si es mayor
        UPDATE dbo.Vehiculos 
        SET Kilometraje = @Kilometraje 
        WHERE IdVehiculo = @IdVehiculo AND Kilometraje < @Kilometraje;

        COMMIT TRANSACTION;
    END TRY
    BEGIN CATCH
        IF @@TRANCOUNT > 0 ROLLBACK TRANSACTION;
        THROW;
    END CATCH
END;
GO

-- PROCEDIMIENTO 2: Sustitución Integral de Motor (Caso Especial Toyota Hilux)
CREATE PROCEDURE dbo.sp_ProcesarSustitucionMotor
    @IdOrden INT,
    @IdMotorNuevo INT,
    @SerieMotorAnterior VARCHAR(60),
    @CausaCambio VARCHAR(300),
    @DestinoRetirado VARCHAR(150),
    @ResultadosPruebas VARCHAR(300),
    @GarantiaMeses INT,
    @GarantiaKm INT,
    @CertificadoSalida VARCHAR(50) OUTPUT
AS
BEGIN
    SET NOCOUNT ON;
    BEGIN TRY
        BEGIN TRANSACTION;

        -- 1. Validar estado de la orden
        DECLARE @EstadoOrden VARCHAR(35);
        DECLARE @IdVehiculo INT;
        SELECT @EstadoOrden = Estado, @IdVehiculo = IdVehiculo FROM dbo.OrdenesTrabajo WHERE IdOrden = @IdOrden;

        IF @EstadoOrden IS NULL
        BEGIN
            RAISERROR('La orden de trabajo no existe.', 16, 1);
        END

        IF @EstadoOrden NOT IN ('En reparación', 'En diagnóstico', 'Pendiente de aprobación')
        BEGIN
            RAISERROR('La orden no se encuentra en una etapa válida para sustitución de motor.', 16, 1);
        END

        -- 2. Validar motor en inventario y disponible
        DECLARE @EstadoMotor VARCHAR(30);
        DECLARE @SerieMotorNuevo VARCHAR(60);
        DECLARE @MarcaComp VARCHAR(50);
        DECLARE @ModeloComp VARCHAR(50);
        DECLARE @AnioIni INT, @AnioFin INT;
        DECLARE @IdRepuestoMotor INT;
        DECLARE @CostoMotor DECIMAL(12,2), @PrecioMotor DECIMAL(12,2);

        SELECT 
            @EstadoMotor = m.Estado,
            @SerieMotorNuevo = m.NumeroSerie,
            @MarcaComp = m.MarcaCompatible,
            @ModeloComp = m.ModeloCompatible,
            @AnioIni = m.AnioInicioCompatible,
            @AnioFin = m.AnioFinCompatible,
            @IdRepuestoMotor = m.IdRepuesto,
            @CostoMotor = ISNULL(r.CostoUnitario, 0),
            @PrecioMotor = ISNULL(r.PrecioVenta, 0)
        FROM dbo.Motores m
        LEFT JOIN dbo.Repuestos r ON m.IdRepuesto = r.IdRepuesto
        WHERE m.IdMotor = @IdMotorNuevo;

        IF @EstadoMotor IS NULL
        BEGIN
            RAISERROR('El motor especificado no existe en el catálogo.', 16, 1);
        END

        IF @EstadoMotor <> 'Disponible'
        BEGIN
            RAISERROR('El motor seleccionado no está disponible (Estado actual: %s).', 16, 1, @EstadoMotor);
        END

        -- 3. Validar compatibilidad técnica con el vehículo
        DECLARE @MarcaVeh VARCHAR(50), @ModeloVeh VARCHAR(50), @AnioVeh INT;
        SELECT @MarcaVeh = Marca, @ModeloVeh = Modelo, @AnioVeh = Anio FROM dbo.Vehiculos WHERE IdVehiculo = @IdVehiculo;

        IF @MarcaComp <> @MarcaVeh OR @ModeloComp <> @ModeloVeh OR (@AnioVeh NOT BETWEEN @AnioIni AND @AnioFin)
        BEGIN
            DECLARE @MsgIncompatible VARCHAR(250) = 
                'Incompatibilidad técnica: El motor ' + @SerieMotorNuevo + ' está diseñado para ' + 
                @MarcaComp + ' ' + @ModeloComp + ' (' + CAST(@AnioIni AS VARCHAR) + '-' + CAST(@AnioFin AS VARCHAR) + 
                '), pero el vehículo es ' + @MarcaVeh + ' ' + @ModeloVeh + ' año ' + CAST(@AnioVeh AS VARCHAR) + '.';
            RAISERROR(@MsgIncompatible, 16, 1);
        END

        -- 4. Generar Certificado de Garantía único
        SET @CertificadoSalida = 'GAR-MOT-' + CAST(YEAR(GETDATE()) AS VARCHAR) + '-' + RIGHT(@SerieMotorNuevo, 6);

        -- 5. Registrar el Cambio de Motor
        INSERT INTO dbo.CambiosMotor (
            IdOrden, IdVehiculo, IdMotorNuevo, SerieMotorAnterior, SerieMotorNuevo, CausaCambio,
            DestinoMotorRetirado, ResultadosPruebasCompresion, GarantiaMeses, GarantiaKilometros, CertificadoGarantia
        ) VALUES (
            @IdOrden, @IdVehiculo, @IdMotorNuevo, @SerieMotorAnterior, @SerieMotorNuevo, @CausaCambio,
            @DestinoRetirado, @ResultadosPruebas, @GarantiaMeses, @GarantiaKm, @CertificadoSalida
        );

        -- 6. Actualizar estado del motor a "Instalado"
        UPDATE dbo.Motores 
        SET Estado = 'Instalado', UbicacionTaller = 'Instalado en Vehículo Placa ' + (SELECT Placa FROM dbo.Vehiculos WHERE IdVehiculo = @IdVehiculo)
        WHERE IdMotor = @IdMotorNuevo;

        -- 7. Si el motor tiene repuesto asociado, descontar de inventario y registrar movimiento atómico
        IF @IdRepuestoMotor IS NOT NULL
        BEGIN
            UPDATE dbo.Repuestos 
            SET StockActual = StockActual - 1 
            WHERE IdRepuesto = @IdRepuestoMotor AND StockActual >= 1;

            IF @@ROWCOUNT = 0
            BEGIN
                RAISERROR('No hay existencia disponible en el inventario de repuestos para el motor.', 16, 1);
            END

            INSERT INTO dbo.MovimientosInventario (
                IdRepuesto, IdOrden, TipoMovimiento, Cantidad, CostoMomento, PrecioMomento, Motivo
            ) VALUES (
                @IdRepuestoMotor, @IdOrden, 'SALIDA', 1, @CostoMotor, @PrecioMotor, 'Sustitución de Motor instalada en Orden ' + CAST(@IdOrden AS VARCHAR)
            );
        END

        -- 8. Avanzar orden a "En control de calidad"
        UPDATE dbo.OrdenesTrabajo 
        SET Estado = 'En control de calidad' 
        WHERE IdOrden = @IdOrden;

        COMMIT TRANSACTION;
    END TRY
    BEGIN CATCH
        IF @@TRANCOUNT > 0 ROLLBACK TRANSACTION;
        THROW;
    END CATCH
END;
GO

-- PROCEDIMIENTO 3: Registro de Pagos con Control Estricto de Sobrepagos
CREATE PROCEDURE dbo.sp_RegistrarPagoOrden
    @IdOrden INT,
    @Monto DECIMAL(12,2),
    @MetodoPago VARCHAR(30),
    @Referencia VARCHAR(50),
    @Notas VARCHAR(200),
    @NuevoRecibo VARCHAR(30) OUTPUT,
    @SaldoRestante DECIMAL(12,2) OUTPUT
AS
BEGIN
    SET NOCOUNT ON;
    BEGIN TRY
        BEGIN TRANSACTION;

        DECLARE @SaldoActual DECIMAL(12,2);
        DECLARE @TotalOrden DECIMAL(12,2);
        
        -- Bloqueo de fila para control de concurrencia
        SELECT @SaldoActual = SaldoPendiente, @TotalOrden = TotalOrden 
        FROM dbo.OrdenesTrabajo WITH (UPDLOCK, ROWLOCK)
        WHERE IdOrden = @IdOrden;

        IF @SaldoActual IS NULL
        BEGIN
            RAISERROR('La orden de trabajo especificada no existe.', 16, 1);
        END

        IF @Monto <= 0
        BEGIN
            RAISERROR('El monto del abono debe ser mayor a cero.', 16, 1);
        END

        -- Prevenir sobrepago
        IF @Monto > @SaldoActual
        BEGIN
            DECLARE @ErrorSobrepago VARCHAR(200) = 
                'Sobrepago denegado: El monto L ' + CAST(@Monto AS VARCHAR) + 
                ' excede el saldo pendiente actual de L ' + CAST(@SaldoActual AS VARCHAR) + '.';
            RAISERROR(@ErrorSobrepago, 16, 1);
        END

        -- Generar número de recibo: REC-YYYY-XXXXX
        DECLARE @SecuenciaPago INT;
        SELECT @SecuenciaPago = ISNULL(MAX(IdPago), 0) + 1 FROM dbo.Pagos;
        SET @NuevoRecibo = 'REC-' + CAST(YEAR(GETDATE()) AS VARCHAR) + '-' + RIGHT('00000' + CAST(@SecuenciaPago AS VARCHAR), 5);

        -- Registrar pago
        INSERT INTO dbo.Pagos (IdOrden, NumeroRecibo, Monto, MetodoPago, ReferenciaTransaccion, Notas)
        VALUES (@IdOrden, @NuevoRecibo, @Monto, @MetodoPago, @Referencia, @Notas);

        -- Actualizar saldo pendiente
        SET @SaldoRestante = @SaldoActual - @Monto;
        
        UPDATE dbo.OrdenesTrabajo 
        SET SaldoPendiente = @SaldoRestante 
        WHERE IdOrden = @IdOrden;

        COMMIT TRANSACTION;
    END TRY
    BEGIN CATCH
        IF @@TRANCOUNT > 0 ROLLBACK TRANSACTION;
        THROW;
    END CATCH
END;
GO

-- ============================================================================
-- 8. DATOS SEMILLA (DML) - CASOS DE PRUEBA Y CASO OBLIGATORIO TOYOTA HILUX
-- ============================================================================

-- A. Clientes iniciales
INSERT INTO dbo.Clientes (Identidad, NombreCompleto, Telefono, Correo, Direccion) VALUES 
('0801-1988-12450', 'Carlos Roberto Mendoza', '9876-5432', 'carlos.mendoza@gmail.com', 'Col. Kennedy, Tegucigalpa'),
('0801-1992-05421', 'María Elena Rodríguez', '9541-2365', 'maria.rodriguez@hotmail.com', 'Col. Las Hadas, Tegucigalpa'),
('0501-1985-09874', 'Transportes del Valle S. de R.L.', '2234-8899', 'gerencia@transportesvalle.hn', 'San Pedro Sula, Cortés');
GO

-- B. Vehículos (Caso: Carlos Mendoza tiene 2 vehículos con historiales separados, incluyendo el Toyota Hilux 2015 del examen)
INSERT INTO dbo.Vehiculos (IdCliente, Placa, VIN, Marca, Modelo, Anio, Color, Kilometraje, TipoCombustible) VALUES 
(1, 'HBC 4821', 'MR0HA32G9F0012345', 'Toyota', 'Hilux', 2015, 'Gris Metálico', 185420, 'Diesel'),
(1, 'HDA 1176', '2HGFA16596H501892', 'Honda', 'Civic', 2016, 'Negro', 94200, 'Gasolina'),
(2, 'HBR 7745', 'JT2BF22K930129841', 'Toyota', 'Corolla', 2012, 'Blanco', 145000, 'Gasolina');
GO

-- C. Personal Mecánico y Especialidades
INSERT INTO dbo.Mecanicos (Identidad, NombreCompleto, Especialidad, Telefono, Activo, Disponible) VALUES 
('0801-1980-00123', 'Kevin Alexander Maradiaga', 'Master Diagnóstico y Motores Diesel', '9988-7766', 1, 1),
('0801-1990-11223', 'Carlos Eduardo Ramos', 'Transmisiones y Tren Delantero', '9711-2233', 1, 1),
('0801-1995-44556', 'José Antonio Hernández', 'Electricidad Automotriz y Escaneo', '9655-4433', 1, 1),
('0801-1985-77889', 'Roberto Pineda (Inactivo)', 'Mantenimiento General', '9122-3344', 0, 0);
GO

-- D. Catálogo de Servicios (Niveles 1 al 5 según Tabla de Requerimientos)
INSERT INTO dbo.Servicios (Codigo, Nombre, Descripcion, NivelComplejidad, Categoria, CostoEstimadoManoObra, TiempoEstimadoHoras) VALUES 
('SRV-101', 'Cambio de Aceite y Filtro', 'Sustitución de lubricante de motor y filtro de aceite universal', 1, 'Mantenimiento básico', 450.00, 1.0),
('SRV-102', 'Cambio de Bujías e Inspección Eléctrica', 'Reemplazo de 4 bujías y revisión de cables de encendido', 1, 'Mantenimiento básico', 500.00, 1.5),
('SRV-201', 'Afinamiento e Inyectores', 'Limpieza profunda de inyectores por ultrasonido y calibración', 2, 'Mantenimiento preventivo', 1200.00, 3.0),
('SRV-202', 'Servicio Completo de Frenos', 'Rectificación de discos y sustitución de pastillas delanteras/traseras', 2, 'Mantenimiento preventivo', 950.00, 2.5),
('SRV-301', 'Cambio de Radiador y Purga de Enfriamiento', 'Sustitución de radiador fisurado y llenado de refrigerante 50/50', 3, 'Reparaciones intermedias', 1500.00, 4.0),
('SRV-302', 'Reemplazo de Alternador y Batería', 'Sustitución de alternador defectuoso y prueba de carga', 3, 'Reparaciones intermedias', 1100.00, 2.5),
('SRV-401', 'Reparación de Transmisión Automática', 'Desmontaje de caja, cambio de discos de embrague y sellos', 4, 'Reparaciones avanzadas', 6500.00, 14.0),
('SRV-402', 'Cepillado y Reconstrucción de Culata', 'Prueba hidrostática, cepillado de culata y empaque nuevo', 4, 'Reparaciones avanzadas', 5500.00, 12.0),
('SRV-501', 'Sustitución Completa de Motor', 'Desmontaje de motor averiado, acople de motor nuevo, purga, escaneo y pruebas de compresión', 5, 'Reparaciones mayores', 12500.00, 24.0);
GO

-- E. Proveedores
INSERT INTO dbo.Proveedores (RTN, NombreEmpresa, Contacto, Telefono, Correo, Direccion) VALUES 
('08019003214567', 'Repuestos y Partes Toyota Genuine', 'Ing. Mario Silva', '2239-1122', 'ventas@toyotaparts.hn', 'Blvd. Suyapa, Tegucigalpa'),
('08019012876543', 'Bosch Automotriz Honduras', 'Lic. Andrea Paz', '2221-4455', 'contacto@bosch.hn', 'Blvd. Morazán, Tegucigalpa'),
('05019008761234', 'Distribuidora de Motores del Atlántico', 'Sr. Héctor Ruiz', '2550-9988', 'h.ruiz@motoresatlantico.hn', 'San Pedro Sula');
GO

-- F. Inventario de Repuestos
INSERT INTO dbo.Repuestos (IdProveedor, Codigo, Nombre, Categoria, StockActual, StockMinimo, CostoUnitario, PrecioVenta) VALUES 
(1, 'REP-ACE-01', 'Aceite Sintético 10W-30 (Galón)', 'Aceites', 25, 6, 420.00, 640.00),
(1, 'REP-FLT-01', 'Filtro de Aceite Hilux/Corolla', 'Filtros', 18, 5, 110.00, 185.00),
(2, 'REP-BUJ-01', 'Bujía de Iridio Bosch', 'Bujías', 12, 8, 130.00, 215.00),
(2, 'REP-BAT-01', 'Batería 12V 650 CCA Bosch', 'Baterías', 4, 2, 1950.00, 2900.00),
(1, 'REP-FRN-01', 'Pastillas de Freno Delanteras Hilux', 'Frenos', 6, 3, 620.00, 980.00),
(2, 'REP-RAD-01', 'Radiador de Aluminio Hilux 2012-2016', 'Radiadores', 3, 1, 2400.00, 3600.00),
(3, 'REP-MOT-1KD', 'Motor Completo 3.0L D-4D 1KD-FTV', 'Motores', 2, 1, 48000.00, 68000.00),
(3, 'REP-MOT-2ZR', 'Motor 1.8L Dual VVT-i 2ZR-FE Corolla', 'Motores', 1, 1, 28000.00, 42000.00);
GO

-- G. Motores Físicos en Inventario (Incluye Motor Compatible para la Hilux y Motor Incompatible para probar validaciones)
INSERT INTO dbo.Motores (IdRepuesto, NumeroSerie, CodigoMotor, CilindradaLitros, TipoCombustible, MarcaCompatible, ModeloCompatible, AnioInicioCompatible, AnioFinCompatible, Estado, UbicacionTaller, Observaciones) VALUES 
(7, '1KD-883921-HN', '1KD-FTV', 3.0, 'Diesel', 'Toyota', 'Hilux', 2012, 2016, 'Disponible', 'Bodega Motores Rack 01', 'Motor nuevo de paquete 0 km garantizado para Hilux 2015'),
(7, '1KD-883922-HN', '1KD-FTV', 3.0, 'Diesel', 'Toyota', 'Hilux', 2012, 2016, 'Disponible', 'Bodega Motores Rack 02', 'Segundo motor nuevo disponible en bodega'),
(8, '2ZR-551240-HN', '2ZR-FE', 1.8, 'Gasolina', 'Toyota', 'Corolla', 2010, 2015, 'Disponible', 'Bodega Motores Rack 05', 'Motor compatible con Corolla, INCOMPATIBLE con Hilux');
GO

-- H. Orden de Trabajo Preconfigurada para el Caso Toyota Hilux 2015
INSERT INTO dbo.OrdenesTrabajo (
    NumeroOrden, IdCliente, IdVehiculo, KilometrajeIngreso, SintomasCliente, DiagnosticoPreliminar, Estado,
    SubtotalServicios, SubtotalRepuestos, Descuento, ImpuestoISV, TotalOrden, SaldoPendiente
) VALUES (
    'ORD-2026-00001', 1, 1, 185420, 
    'Sobrecalentamiento excesivo en carretera, pérdida severa de potencia, humo blanco por el escape y sonido metálico constante.',
    'Temperatura al límite, baja compresión en cilindros 2 y 4, evidencia de fisura de bloque y fundición interna.',
    'En diagnóstico',
    0.00, 0.00, 0.00, 0.00, 0.00, 0.00
);
GO

-- Asignación del Mecánico Líder a la Orden Hilux
INSERT INTO dbo.OrdenMecanicos (IdOrden, IdMecanico, EsResponsablePrincipal) VALUES 
(1, 1, 1),
(1, 2, 0);
GO

-- Diagnóstico Oficial Técnico registrado
INSERT INTO dbo.Diagnosticos (IdOrden, IdMecanico, HallazgosTecnicos, EvidenciasFotograficas, RequiereCambioMotor, AprobadoParaPresupuesto) VALUES 
(1, 1, 'Inspección endoscópica confirma deformación irrecuperable en cilindros y daño en cigüeñal por sobrecalentamiento extremo. Serie de motor dañado: 1KD-784219. Se requiere sustitución completa de motor.', 'img/evidencias/hilux_bloque_fisurado.jpg', 1, 1);
GO

PRINT '====================================================================';
PRINT '   BASE DE DATOS AutoTechDB Y OBJETOS CREADOS CORRECTAMENTE        ';
PRINT '   - 16 Tablas Normalizadas + Tablas Puente y Auditoría            ';
PRINT '   - Restricciones CHECK, FKs, PKs y Reglas de Negocio             ';
PRINT '   - 3 Vistas Administrativas                                      ';
PRINT '   - 2 Triggers de Auditoría                                       ';
PRINT '   - 3 Procedimientos Almacenados Transaccionales                  ';
PRINT '   - Datos semilla listos para la defensa práctica (Hilux 2015)    ';
PRINT '====================================================================';
GO
