CREATE DATABASE ManagerSportClub;
USE ManagerSportClub;

-- Tabla SOCIO
CREATE TABLE SOCIO (
    Id INT PRIMARY KEY AUTO_INCREMENT,
    Nombres VARCHAR(100) NOT NULL,
    Apellidos VARCHAR(100) NOT NULL,
    Email VARCHAR(150) UNIQUE NOT NULL,
    Telefono VARCHAR(20),
    Fecha_Nacimiento DATE,
    Fecha_Registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    Estado BOOLEAN DEFAULT TRUE,
    Direccion VARCHAR(255),
    Foto_URL VARCHAR(255)
);

-- Tabla USUARIO
CREATE TABLE USUARIO (
    Id INT PRIMARY KEY AUTO_INCREMENT,
    Username VARCHAR(50) UNIQUE NOT NULL,
    Email VARCHAR(150) UNIQUE NOT NULL,
    Contrasena VARCHAR(255) NOT NULL,
    TipoUsuario VARCHAR(20) NOT NULL,
    Id_Socio INT,
    Estado BOOLEAN DEFAULT TRUE,
    Fecha_Creacion DATETIME DEFAULT CURRENT_TIMESTAMP,
    Ultimo_Acceso DATETIME,
    FOREIGN KEY (Id_Socio) REFERENCES SOCIO(Id) ON DELETE SET NULL
);

-- Tabla MEMBRESIA
CREATE TABLE MEMBRESIA (
    Id INT PRIMARY KEY AUTO_INCREMENT,
    Tipo VARCHAR(50) NOT NULL,
    Precio DECIMAL(10,2) NOT NULL,
    Duracion_Dias INT NOT NULL,
    Descripcion TEXT
);

-- Tabla SUSCRIPCION
CREATE TABLE SUSCRIPCION (
    Id INT PRIMARY KEY AUTO_INCREMENT,
    Id_Socio INT NOT NULL,
    Id_Membresia INT NOT NULL,
    Fecha_Inicio DATE NOT NULL, -- Sin DEFAULT, se inserta manualmente
    Fecha_Fin DATE,
    Duracion_Meses INT,
    Renovacion_Automatica BOOLEAN DEFAULT FALSE,
    Estado VARCHAR(20) DEFAULT 'Activa',
    FOREIGN KEY (Id_Socio) REFERENCES SOCIO(Id) ON DELETE CASCADE,
    FOREIGN KEY (Id_Membresia) REFERENCES MEMBRESIA(Id)
);

-- Tabla INSTALACIONES
CREATE TABLE INSTALACIONES (
    Id INT PRIMARY KEY AUTO_INCREMENT,
    Nombre VARCHAR(100) NOT NULL,
    Tipo VARCHAR(50),
    Capacidad_Personas INT,
    Estado VARCHAR(20) DEFAULT 'Disponible',
    Costo_Reserva DECIMAL(10,2),
    Descripcion TEXT
);

-- Tabla RESERVA
CREATE TABLE RESERVA (
    Id INT PRIMARY KEY AUTO_INCREMENT,
    Id_Socio INT NOT NULL,
    Id_Instalacion INT NOT NULL,
    Fecha DATE NOT NULL,
    Hora_Inicio TIME NOT NULL,
    Hora_Fin TIME NOT NULL,
    Estado VARCHAR(20) DEFAULT 'Confirmada',
    Tipo_de_Reserva VARCHAR(50),
    Costo DECIMAL(10,2),
    Notas TEXT,
    Fecha_Creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (Id_Socio) REFERENCES SOCIO(Id) ON DELETE CASCADE,
    FOREIGN KEY (Id_Instalacion) REFERENCES INSTALACIONES(Id)
);

-- Tabla PAGO
CREATE TABLE PAGO (
    Id INT PRIMARY KEY AUTO_INCREMENT,
    Id_Suscripcion INT,
    Id_Reserva INT,
    Concepto VARCHAR(255) NOT NULL,
    Monto DECIMAL(10,2) NOT NULL,
    Metodo_Pago VARCHAR(50),
    Estado VARCHAR(20) DEFAULT 'pendiente',
    Fecha_Pago DATETIME DEFAULT CURRENT_TIMESTAMP,
    Comprobante_URL VARCHAR(255),
    FOREIGN KEY (Id_Suscripcion) REFERENCES SUSCRIPCION(Id),
    FOREIGN KEY (Id_Reserva) REFERENCES RESERVA(Id)
);

-- Tabla ENTRENADOR
CREATE TABLE ENTRENADOR (
    Id INT PRIMARY KEY AUTO_INCREMENT,
    Nombres VARCHAR(100) NOT NULL,
    Apellidos VARCHAR(100) NOT NULL,
    Email VARCHAR(150) UNIQUE NOT NULL,
    Telefono VARCHAR(20),
    Especialidad VARCHAR(100),
    Fecha_Contratacion DATE, -- Sin DEFAULT
    Estado BOOLEAN DEFAULT TRUE,
    Horario TEXT
);

-- Tabla DEPORTE
CREATE TABLE DEPORTE (
    Id INT PRIMARY KEY AUTO_INCREMENT,
    Nombre VARCHAR(100) NOT NULL UNIQUE,
    Descripcion TEXT,
    Estado BOOLEAN DEFAULT TRUE
);

INSERT INTO SOCIO (Nombres, Apellidos, Email, Telefono, Fecha_Nacimiento, Direccion, Foto_URL)
VALUES
('Carlos', 'García', 'carlos@gmail.com', '809-555-1234', '1995-02-10', 'Santo Domingo', 'img/carlos.jpg'),
('María', 'López', 'maria@gmail.com', '809-222-8899', '1998-09-20', 'Santiago', 'img/maria.jpg'),
('Juan', 'Martínez', 'juan@gmail.com', '809-111-4567', '1992-01-05', 'La Vega', 'img/juan.jpg');

INSERT INTO USUARIO (Username, Email, Contrasena, TipoUsuario, Id_Socio)
VALUES
('cgarcia', 'carlos@gmail.com', '123456', 'Socio', 1),
('mlopez', 'maria@gmail.com', '123456', 'Socio', 2),
('admin', 'admin@club.com', 'admin123', 'Admin', NULL);

INSERT INTO MEMBRESIA (Tipo, Precio, Duracion_Dias, Descripcion)
VALUES
('Mensual', 1500.00, 30, 'Acceso completo por 30 días'),
('Trimestral', 3900.00, 90, 'Acceso completo por 3 meses'),
('Anual', 12000.00, 365, 'Acceso completo por un año');

INSERT INTO SUSCRIPCION (Id_Socio, Id_Membresia, Fecha_Inicio, Fecha_Fin, Duracion_Meses, Renovacion_Automatica)
VALUES
(1, 1, '2025-01-01', '2025-01-31', 1, TRUE),
(2, 2, '2025-01-10', '2025-04-10', 3, FALSE),
(3, 1, '2025-02-01', '2025-02-28', 1, TRUE);

INSERT INTO INSTALACIONES (Nombre, Tipo, Capacidad_Personas, Costo_Reserva, Descripcion)
VALUES
('Cancha de Baloncesto', 'Deporte', 10, 500.00, 'Cancha techada'),
('Piscina', 'Recreación', 30, 350.00, 'Piscina olímpica'),
('Gimnasio', 'Fitness', 25, 0.00, 'Incluido en la membresía');

INSERT INTO RESERVA (Id_Socio, Id_Instalacion, Fecha, Hora_Inicio, Hora_Fin, Tipo_de_Reserva, Costo, Notas)
VALUES
(1, 1, '2025-01-15', '09:00:00', '11:00:00', 'Privada', 500.00, 'Juego amistoso'),
(2, 2, '2025-01-20', '13:00:00', '15:00:00', 'Libre', 350.00, 'Entrenamiento'),
(1, 3, '2025-01-10', '08:00:00', '10:00:00', 'Gimnasio', 0.00, 'Rutina diaria');

INSERT INTO PAGO (Id_Suscripcion, Id_Reserva, Concepto, Monto, Metodo_Pago, Estado, Comprobante_URL)
VALUES
(1, NULL, 'Pago mensual', 1500.00, 'Tarjeta', 'completado', 'recibos/1.pdf'),
(NULL, 1, 'Reserva cancha', 500.00, 'Efectivo', 'completado', 'recibos/2.pdf'),
(2, NULL, 'Pago trimestral', 3900.00, 'Transferencia', 'pendiente', NULL);

INSERT INTO ENTRENADOR (Nombres, Apellidos, Email, Telefono, Especialidad, Fecha_Contratacion, Horario)
VALUES
('Pedro', 'Ramírez', 'pedro@club.com', '809-333-7788', 'Fitness', '2024-11-01', 'Lunes-Viernes 8am-5pm'),
('Laura', 'Torres', 'laura@club.com', '809-444-2299', 'Natación', '2025-01-05', 'Lunes-Jueves 2pm-7pm');

INSERT INTO DEPORTE (Nombre, Descripcion)
VALUES
('Baloncesto', 'Juego de equipo, 5 jugadores por lado'),
('Natación', 'Entrenamiento acuático'),
('Fútbol', 'Deporte en césped');
