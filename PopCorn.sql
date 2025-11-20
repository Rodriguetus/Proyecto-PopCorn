

DROP TABLE IF EXISTS `alquiler`;
CREATE TABLE `alquiler` (
  `idAlquiler` int NOT NULL AUTO_INCREMENT,
  `Estado` varchar(45) NOT NULL,
  `FechaDevolucion` date NOT NULL,
  `FechaArquiler` date NOT NULL,
  PRIMARY KEY (`idAlquiler`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `administrador`;
CREATE TABLE `administrador` (
  `idAdmin` int NOT NULL AUTO_INCREMENT,
  `Correo` varchar(45) NOT NULL,
  `Nombre` varchar(45) NOT NULL,
  `Contrasena` varchar(45) NOT NULL,
  PRIMARY KEY (`idAdmin`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `pedido`;
CREATE TABLE `pedido` (
  `idPedido` int NOT NULL AUTO_INCREMENT,
  `FechaCompra` date NOT NULL,
  `FechaLlegada` date NOT NULL,
  `Estado` varchar(45) NOT NULL,
  `idPelicula` int NOT NULL,
  `Direccion` varchar(45) NOT NULL,
  PRIMARY KEY (`idPedido`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `pelicula`;
CREATE TABLE `pelicula` (
  `idPelicula` int NOT NULL AUTO_INCREMENT,
  `Nombre` varchar(255) NOT NULL,
  `Precio` decimal(5,2) NOT NULL,
  `Stock` int NOT NULL,
  `AnoSalida` int NOT NULL,
  `Proveedor` varchar(255) NOT NULL,
  `Formato` varchar(50) NOT NULL,
  `Genero` varchar(100) NOT NULL,
  `Imagen` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`idPelicula`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `usuario`;
CREATE TABLE `usuario` (
  `idUsuario` int NOT NULL AUTO_INCREMENT,
  `Correo` varchar(45) NOT NULL,
  `Nombre` varchar(45) NOT NULL,
  `Contrasena` varchar(45) NOT NULL,
  PRIMARY KEY (`idUsuario`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- -----------------------------
-- INSERTS
-- -----------------------------

INSERT INTO administrador (Correo, Nombre, Contrasena) VALUES
('admin1@popcorn.com', 'Administrador Uno', 'admin123'),
('admin2@popcorn.com', 'Administrador Dos', 'admin456');

INSERT INTO usuario (Correo, Nombre, Contrasena) VALUES
('usuario1@correo.com', 'Usuario Uno', 'user123'),
('usuario2@correo.com', 'Usuario Dos', 'user456');