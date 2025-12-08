CREATE database Prueba;
Use Prueba;
CREATE TABLE USUARIO (
    Id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    Username VARCHAR(40) NOT NULL,
    Email VARCHAR(100) NOT NULL UNIQUE,
    Contrasena VARCHAR(255) NOT NULL,
    Rol VARCHAR(20)     -- admin, socio, entrenador...
);
CREATE TABLE SOCIO (
    Id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    Nombres VARCHAR(40),
    Apellidos VARCHAR(40),
    Email VARCHAR(40),
    Telefono VARCHAR(20),
    Fecha_Nacimiento DATE,
    Fecha_Registro DATE,
    Estado BIT,
    Direccion VARCHAR(55),
    Foto_URL VARCHAR(255)
);
CREATE TABLE MEMBRESIA (
    Id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    Tipo VARCHAR(20),
    Precio DECIMAL(13, 2) CHECK (Precio >= 0),
    Duracion_Dias INT NOT NULL CHECK (Duracion_Dias >= 30)
);
CREATE TABLE INSTALACIONES (
    Id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    Nombre VARCHAR(40),
    Capacidad_Personas INT NOT NULL,
    Estado VARCHAR(15),
    Costo_Reserva DECIMAL(13, 2) CHECK (Costo_Reserva > 0)
);
CREATE TABLE DEPORTE (
    Id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    Nombre VARCHAR(25),
    Descripcion VARCHAR(80)
);
CREATE TABLE ENTRENADOR (
    Id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    Nombres VARCHAR(40),
    Apellidos VARCHAR(40),
    Email VARCHAR(40),
    Telefono VARCHAR(20),
    Especialidad VARCHAR(80),
    Fecha_Contratacion DATE,
    Estado BIT
);
CREATE TABLE SUSCRIPCION (
    Id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    Id_Socio INT NOT NULL,
    Id_Membresia INT NOT NULL,
    Renovacion_Automatica BIT,
    FOREIGN KEY (Id_Socio) REFERENCES SOCIO(Id),
    FOREIGN KEY (Id_Membresia) REFERENCES MEMBRESIA(Id)
);
CREATE TABLE RESERVA (
    Id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    Id_Socio INT NOT NULL,
    Id_Instalacion INT NOT NULL,
    Fecha DATE,
    Hora_Inicio TIME,
    Hora_Fin TIME,
    Estado VARCHAR(15),
    Tipo_de_Reserva VARCHAR(20),
    Costo DECIMAL(13, 2) CHECK (Costo > 0),
    Notas VARCHAR(80),
    FOREIGN KEY (Id_Socio) REFERENCES SOCIO(Id),
    FOREIGN KEY (Id_Instalacion) REFERENCES INSTALACIONES(Id)
);
CREATE TABLE PAGO (
    Id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    Id_Suscripcion INT,
    Id_Reserva INT,
    Concepto VARCHAR(50),
    Monto DECIMAL(13, 2) CHECK (Monto >= 0),
    Metodo_Pago VARCHAR(14),
    Estado VARCHAR(20),
    Comprobante_URL VARCHAR(255),
    FOREIGN KEY (Id_Suscripcion) REFERENCES SUSCRIPCION(Id),
    FOREIGN KEY (Id_Reserva) REFERENCES RESERVA(Id)
);
Show tables;
ALTER TABLE MEMBRESIA
ADD COLUMN Descripcion VARCHAR(100);
-- fecha inicio, fin y estado (sus)
ALTER TABLE USUARIO
DROP COLUMN Rol,
ADD COLUMN TipoUsuario ENUM('administrador', 'socio') NOT NULL AFTER Contrasena,
ADD COLUMN Estado BIT DEFAULT 1 AFTER TipoUsuario,
ADD COLUMN Fecha_Creacion DATETIME DEFAULT CURRENT_TIMESTAMP AFTER Estado,
ADD COLUMN Ultimo_Acceso DATETIME NULL AFTER Fecha_Creacion;




