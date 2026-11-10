# Despliegue: Azure SQL (base de datos) + Render (pagina)

## 1. Azure SQL
1. Crear una base SQL en Azure llamada AutoTechDB (servidor nuevo, nivel gratuito o Basic).
2. En el servidor: Redes -> permitir "servicios de Azure" y agregar tu IP.
3. Ejecutar database/AutoTech_Schema_Azure.sql (Azure Data Studio o SSMS) conectado a AutoTechDB.

## 2. GitHub
Subir este proyecto (sin la carpeta target) a un repositorio.

## 3. Render
1. New -> Web Service -> conectar el repositorio -> Runtime: Docker.
2. Variables de entorno:
   - DB_URL  = jdbc:sqlserver://TU-SERVIDOR.database.windows.net:1433;databaseName=AutoTechDB;encrypt=true;trustServerCertificate=false;loginTimeout=30
   - DB_USER = usuario del servidor SQL
   - DB_PASS = clave
   - PORT    = 8080
3. Deploy. Probar https://TU-APP.onrender.com/health
