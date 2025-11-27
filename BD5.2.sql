-- MySQL dump 10.13  Distrib 8.0.42, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: popcorn
-- ------------------------------------------------------
-- Server version	8.0.42

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `administrador`
--

DROP TABLE IF EXISTS `administrador`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `administrador` (
  `idAdmin` int NOT NULL,
  `Correo` varchar(45) NOT NULL,
  `Nombre` varchar(45) NOT NULL,
  `Contrasena` varchar(45) NOT NULL,
  PRIMARY KEY (`idAdmin`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `administrador`
--

LOCK TABLES `administrador` WRITE;
/*!40000 ALTER TABLE `administrador` DISABLE KEYS */;
INSERT INTO `administrador` VALUES (1,'admin@gmail.com','Admin','admin1234');
/*!40000 ALTER TABLE `administrador` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `alquiler`
--

DROP TABLE IF EXISTS `alquiler`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `alquiler` (
  `idAlquiler` int NOT NULL,
  `Estado` varchar(45) NOT NULL,
  `FechaDevolucion` date NOT NULL,
  `FechaArquiler` date NOT NULL,
  PRIMARY KEY (`idAlquiler`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `alquiler`
--

LOCK TABLES `alquiler` WRITE;
/*!40000 ALTER TABLE `alquiler` DISABLE KEYS */;
/*!40000 ALTER TABLE `alquiler` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `pedido`
--

DROP TABLE IF EXISTS `pedido`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `pedido` (
  `idPedido` int NOT NULL,
  `FechaCompra` date NOT NULL,
  `FechaLlegada` date NOT NULL,
  `Estado` varchar(45) NOT NULL,
  `idPelicula` int NOT NULL,
  `Direccion` varchar(45) NOT NULL,
  PRIMARY KEY (`idPedido`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pedido`
--

LOCK TABLES `pedido` WRITE;
/*!40000 ALTER TABLE `pedido` DISABLE KEYS */;
/*!40000 ALTER TABLE `pedido` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `pelicula`
--

DROP TABLE IF EXISTS `pelicula`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `pelicula` (
  `idPelicula` int NOT NULL AUTO_INCREMENT,
  `Nombre` varchar(255) NOT NULL,
  `Precio` decimal(5,2) NOT NULL,
  `Stock` int NOT NULL,
  `AnoSalida` int NOT NULL,
  `Proveedor` varchar(255) NOT NULL,
  `Formato` varchar(50) NOT NULL,
  `Genero` varchar(100) NOT NULL,
  `Imagen` varchar(45) DEFAULT NULL,
  `Descripcion` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`idPelicula`)
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pelicula`
--

LOCK TABLES `pelicula` WRITE;
/*!40000 ALTER TABLE `pelicula` DISABLE KEYS */;
INSERT INTO `pelicula` VALUES (3,'El Padrino',14.99,20,1972,'Paramount Pictures','Blu-ray','Crimen, Drama','/Imagenes/Elpadrino.jpg',NULL),(5,'Pulp Fiction',12.99,30,1994,'Miramax','Blu-ray','Crimen, Drama','/Imagenes/PulpFiction.jpg',NULL),(6,'El Señor de los Anillos: La Comunidad del Anillo',19.99,15,2001,'New Line Cinema','4K UHD','Fantasía, Aventura','/Imagenes/SenosDeLosAnillos.jpg',NULL),(7,'The Dark Knight',18.99,25,2008,'Warner Bros.','Blu-ray','Acción, Crimen','/Imagenes/TheDarkKnight.jpg',NULL),(8,'Forrest Gump',10.99,40,1994,'Paramount Pictures','DVD','Drama, Romance','/Imagenes/forrestGump.jpg',NULL),(9,'Matrix',15.99,22,1999,'Warner Bros.','4K UHD','Acción, Ciencia ficción','/Imagenes/Matrix.jpg',NULL),(10,'Interstellar',17.99,18,2014,'Paramount Pictures','Blu-ray','Ciencia ficción, Drama','/Imagenes/Interstellar.jpg',NULL),(11,'El silencio de los corderos',11.99,35,1991,'Orion Pictures','DVD','Suspense, Crimen','/Imagenes/Elsilenciodeloscorderos.jpg',NULL),(13,'Gladiator',13.99,28,2000,'DreamWorks','Blu-ray','Acción, Drama','/Imagenes/Gladiator.jpg',NULL),(14,'Parásitos',16.99,12,2019,'CJ Entertainment','Blu-ray','Suspense, Comedia','/Imagenes/Parasitos.jpg',NULL),(15,'La La Land',12.99,20,2016,'Lionsgate','Blu-ray','Musical, Romance','/Imagenes/LaLaLand.jpg',NULL),(16,'Django sin cadenas',14.99,17,2012,'The Weinstein Company','Blu-ray','Western, Drama','/Imagenes/DjangoSinCadenas.jpg',NULL),(17,'El viaje de Chihiro',19.99,10,2001,'Studio Ghibli','Blu-ray','Animación, Fantasía','/Imagenes/ElViajeDeChihiro.jpg',NULL),(18,'El club de la lucha',11.99,33,1999,'20th Century Fox','DVD','Drama','/Imagenes/Elclubdelalucha.jpg',NULL),(20,'Blade Runner 2049',17.99,14,2017,'Sony Pictures','4K UHD','Ciencia ficción, Suspense','/Imagenes/BladeRunner2049.jpg',NULL),(21,'Amélie',9.99,30,2001,'UGC-Fox Distribution','DVD','Comedia, Romance','/Imagenes/Amelie.jpg',NULL),(22,'Mad Max: Furia en la carretera',16.99,20,2015,'Warner Bros.','4K UHD','Acción, Ciencia ficción','/Imagenes/MadMaxFuriaenlacarretera.jpg',NULL),(23,'El Padrino: Parte II',14.99,15,1974,'Paramount Pictures','Blu-ray','Crimen, Drama','/Imagenes/ElPadrinoParteII.jpg',NULL);
/*!40000 ALTER TABLE `pelicula` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `usuario`
--

DROP TABLE IF EXISTS `usuario`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `usuario` (
  `idUsuario` int NOT NULL AUTO_INCREMENT,
  `Correo` varchar(45) NOT NULL,
  `Nombre` varchar(45) NOT NULL,
  `Contrasena` varchar(45) NOT NULL,
  `Fav` int DEFAULT NULL,
  PRIMARY KEY (`idUsuario`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `usuario`
--

LOCK TABLES `usuario` WRITE;
/*!40000 ALTER TABLE `usuario` DISABLE KEYS */;
/*!40000 ALTER TABLE `usuario` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-11-27 12:25:04
