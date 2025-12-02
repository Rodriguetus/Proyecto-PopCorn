
CREATE DATABASE IF NOT EXISTS popcorn;
USE popcorn;
-- MySQL dump 10.13  Distrib 8.0.42, for Win64 (x86_64)
--
-- Host: localhost    Database: popcorn
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
  `idAlquiler` int NOT NULL AUTO_INCREMENT,
  `idUsuario` int NOT NULL,
  `idPelicula` int NOT NULL,
  `FechaAlquiler` date NOT NULL,
  `FechaDevolucion` date DEFAULT NULL,
  PRIMARY KEY (`idAlquiler`,`idUsuario`,`idPelicula`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `alquiler`
--

LOCK TABLES `alquiler` WRITE;
/*!40000 ALTER TABLE `alquiler` DISABLE KEYS */;
INSERT INTO `alquiler` VALUES (1,1,6,'2025-11-30','2025-12-07'),(2,1,9,'2025-11-30','2025-12-07'),(3,1,20,'2025-11-30','2025-12-07'),(4,1,22,'2025-11-30','2025-12-07'),(5,1,26,'2025-11-30','2025-12-07'),(6,1,38,'2025-11-30','2025-12-07');
/*!40000 ALTER TABLE `alquiler` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `compra`
--

DROP TABLE IF EXISTS `compra`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `compra` (
  `idCompra` int NOT NULL,
  `idPeliculas` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`idCompra`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `compra`
--

LOCK TABLES `compra` WRITE;
/*!40000 ALTER TABLE `compra` DISABLE KEYS */;
/*!40000 ALTER TABLE `compra` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `favoritos`
--

DROP TABLE IF EXISTS `favoritos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `favoritos` (
  `idUsuario` int NOT NULL,
  `idPelicula` int NOT NULL,
  PRIMARY KEY (`idUsuario`,`idPelicula`),
  CONSTRAINT `favoritos_ibfk_1` FOREIGN KEY (`idUsuario`) REFERENCES `usuario` (`idUsuario`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `favoritos`
--

LOCK TABLES `favoritos` WRITE;
/*!40000 ALTER TABLE `favoritos` DISABLE KEYS */;
INSERT INTO `favoritos` VALUES (1,3),(1,6),(1,11),(1,18),(1,22),(1,29),(1,34),(1,50);
/*!40000 ALTER TABLE `favoritos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `pedido`
--

DROP TABLE IF EXISTS `pedido`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `pedido` (
  `idPedido` int NOT NULL AUTO_INCREMENT,
  `FechaCompra` date NOT NULL,
  `FechaLlegada` date NOT NULL,
  `Estado` varchar(45) NOT NULL,
  `idPelicula` int NOT NULL,
  `Direccion` varchar(45) NOT NULL,
  PRIMARY KEY (`idPedido`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pedido`
--

LOCK TABLES `pedido` WRITE;
/*!40000 ALTER TABLE `pedido` DISABLE KEYS */;
INSERT INTO `pedido` VALUES (1,'2025-11-30','2025-12-08','De camino',6,'ew');
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
  `Descripcion` varchar(1000) DEFAULT NULL,
  PRIMARY KEY (`idPelicula`)
) ENGINE=InnoDB AUTO_INCREMENT=54 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pelicula`
--

LOCK TABLES `pelicula` WRITE;
/*!40000 ALTER TABLE `pelicula` DISABLE KEYS */;
INSERT INTO `pelicula` VALUES (3,'El Padrino',14.99,20,1972,'Paramount Pictures','Blu-ray','Crimen, Drama','/Imagenes/Elpadrino.jpg','Una épica historia de la familia Corleone y el ascenso del crimen organizado en EE. UU.'),(5,'Pulp Fiction',12.99,30,1994,'Miramax','Blu-ray','Crimen, Drama','/Imagenes/PulpFiction.jpg','Historias entrelazadas de criminales en Los Ángeles, llenas de humor negro y acción.'),(6,'El Señor de los Anillos: La Comunidad del Anillo',19.99,15,2001,'New Line Cinema','4K UHD','Fantasía, Aventura','/Imagenes/SenosDeLosAnillos.jpg','Frodo inicia la misión de destruir el Anillo Único en una aventura épica.'),(7,'The Dark Knight',18.99,25,2008,'Warner Bros.','Blu-ray','Acción, Crimen','/Imagenes/TheDarkKnight.jpg','Batman enfrenta al Joker en una lucha moral y psicológica por Gotham.'),(8,'Forrest Gump',10.99,40,1994,'Paramount Pictures','DVD','Drama, Romance','/Imagenes/forrestGump.jpg','La inspiradora historia de un hombre especial que vive momentos históricos sin quererlo.'),(9,'Matrix',15.99,22,1999,'Warner Bros.','4K UHD','Acción, Ciencia ficción','/Imagenes/Matrix.jpg','Un hacker descubre la verdad sobre su realidad y la lucha contra las máquinas.'),(10,'Interstellar',17.99,18,2014,'Paramount Pictures','Blu-ray','Ciencia ficción, Drama','/Imagenes/Interstellar.jpg','Un grupo de astronautas viaja por un agujero de gusano en busca de un nuevo hogar.'),(11,'El silencio de los corderos',11.99,35,1991,'Orion Pictures','DVD','Suspense, Crimen','/Imagenes/Elsilenciodeloscorderos.jpg','Una agente del FBI pide ayuda a un asesino serial para atrapar a otro criminal.'),(13,'Gladiator',13.99,28,2000,'DreamWorks','Blu-ray','Acción, Drama','/Imagenes/Gladiator.jpg','Un general romano convertido en esclavo busca venganza en la arena del Coliseo.'),(14,'Parásitos',16.99,12,2019,'CJ Entertainment','Blu-ray','Suspense, Comedia','/Imagenes/Parasitos.jpg','Una familia pobre se infiltra en un hogar adinerado desatando consecuencias inesperadas.'),(15,'La La Land',12.99,20,2016,'Lionsgate','Blu-ray','Musical, Romance','/Imagenes/LaLaLand.jpg','Un pianista y una actriz luchan por sus sueños y su amor en Los Ángeles.'),(16,'Django sin cadenas',14.99,17,2012,'The Weinstein Company','Blu-ray','Western, Drama','/Imagenes/DjangoSinCadenas.jpg','Un esclavo liberado busca rescatar a su esposa con ayuda de un cazarrecompensas.'),(17,'El viaje de Chihiro',19.99,10,2001,'Studio Ghibli','Blu-ray','Animación, Fantasía','/Imagenes/ElViajeDeChihiro.jpg','Una niña queda atrapada en un mundo mágico lleno de espíritus y criaturas.'),(18,'El club de la lucha',11.99,33,1999,'20th Century Fox','DVD','Drama','/Imagenes/Elclubdelalucha.jpg','Un hombre crea un club clandestino para liberar su frustración y rebelarse.'),(20,'Blade Runner 2049',17.99,14,2017,'Sony Pictures','4K UHD','Ciencia ficción, Suspense','/Imagenes/BladeRunner2049.jpg','Un blade runner descubre un secreto que puede desencadenar el caos mundial.'),(21,'Amélie',9.99,30,2001,'UGC-Fox Distribution','DVD','Comedia, Romance','/Imagenes/Amelie.jpg','La historia de una joven que decide cambiar la vida de las personas que la rodean.'),(22,'Mad Max: Furia en la carretera',16.99,20,2015,'Warner Bros.','4K UHD','Acción, Ciencia ficción','/Imagenes/MadMaxFuriaenlacarretera.jpg','Una persecución frenética en un mundo postapocalíptico dominado por la violencia.'),(23,'El Padrino: Parte II',14.99,15,1974,'Paramount Pictures','Blu-ray','Crimen, Drama','/Imagenes/ElPadrinoParteII.jpg','Michael Corleone expande el imperio familiar mientras se revelan los orígenes de Vito.'),(24,'Inception',16.99,18,2010,'Warner Bros.','Blu-ray','Ciencia ficción, Suspense','/Imagenes/Inception.jpg','Un ladrón capaz de entrar en los sueños debe realizar el atraco más complejo: implantar una idea.'),(25,'Titanic',12.50,40,1997,'20th Century Fox','DVD','Drama, Romance','/Imagenes/Titanic.jpg','Una historia de amor épica durante el trágico viaje del Titanic.'),(26,'Avatar',18.99,25,2009,'20th Century Fox','4K UHD','Ciencia ficción, Aventura','/Imagenes/Avatar.jpg','Un exmarine se une a una batalla que decidirá el destino de Pandora.'),(27,'Terminator 2: Judgment Day',14.99,20,1991,'TriStar Pictures','Blu-ray','Acción, Ciencia ficción','/Imagenes/Terminator2.jpg','Un androide viaja al pasado para proteger al futuro líder de la resistencia humana.'),(28,'Shutter Island',11.99,15,2010,'Paramount Pictures','Blu-ray','Suspense, Thriller','/Imagenes/ShutterIsland.jpg','Dos agentes investigan una desaparición en un hospital psiquiátrico.'),(29,'Toy Story',10.99,35,1995,'Pixar','DVD','Animación, Familiar','/Imagenes/ToyStory.jpg','Juguetes que cobran vida y viven aventuras inolvidables.'),(30,'Avengers: Endgame',19.50,28,2019,'Marvel Studios','4K UHD','Acción, Superhéroes','/Imagenes/Endgame.jpg','Los Vengadores enfrentan su misión final contra Thanos.'),(31,'Harry Potter y la Piedra Filosofal',13.50,30,2001,'Warner Bros.','Blu-ray','Fantasía, Aventura','/Imagenes/HarryPotter1.jpg','El inicio de la saga del joven mago.'),(32,'Jurassic Park',14.99,22,1993,'Universal Pictures','Blu-ray','Ciencia ficción, Aventura','/Imagenes/JurassicPark.jpg','El parque de dinosaurios fuera de control.'),(33,'Whiplash',12.99,16,2014,'Sony Pictures','Blu-ray','Drama, Música','/Imagenes/Whiplash.jpg','Un baterista lucha por alcanzar la perfección bajo un maestro implacable.'),(34,'El Rey León',11.99,40,1994,'Disney','DVD','Animación, Aventura','/Imagenes/ElReyLeon.jpg','La historia de Simba y su destino como rey.'),(35,'The Social Network',13.99,18,2010,'Columbia Pictures','Blu-ray','Drama, Biografía','/Imagenes/TheSocialNetwork.jpg','El nacimiento de Facebook.'),(36,'Star Wars: Episodio IV',14.99,25,1977,'Lucasfilm','Blu-ray','Ciencia ficción, Aventura','/Imagenes/StarWarsIV.jpg','La batalla de la Rebelión contra el Imperio.'),(37,'El Gran Hotel Budapest',13.50,20,2014,'Fox Searchlight','Blu-ray','Comedia, Aventura','/Imagenes/GranHotel.jpg','Las aventuras del conserje Gustave H.'),(38,'Joker',15.99,22,2019,'Warner Bros.','4K UHD','Drama, Crimen','/Imagenes/Joker.jpg','El origen del icónico villano.'),(39,'Coco',12.50,30,2017,'Pixar','Blu-ray','Animación, Aventura','/Imagenes/Coco.jpg','Un niño viaja al mundo de los muertos.'),(40,'La Lista de Schindler',14.99,18,1993,'Universal Pictures','DVD','Drama, Historia','/Imagenes/Schindler.jpg','La historia real de Oskar Schindler.'),(41,'Spider-Man: No Way Home',19.99,25,2021,'Marvel Studios','4K UHD','Acción, Superhéroes','/Imagenes/NoWayHome.jpg','Peter Parker abre el multiverso.'),(42,'IT',11.50,20,2017,'Warner Bros.','Blu-ray','Terror, Suspense','/Imagenes/IT.jpg','El payaso Pennywise aterroriza a Derry.'),(43,'Wall-E',12.99,28,2008,'Pixar','DVD','Animación, Ciencia ficción','/Imagenes/WallE.jpg','Un robot solitario cambia el futuro de la humanidad.'),(44,'La Vida es Bella',12.99,20,1997,'Miramax','DVD','Drama, Comedia','/Imagenes/LaVidaEsBella.jpg','Un padre usa el humor para proteger a su hijo en un campo de concentración.'),(45,'Los Increíbles',11.99,30,2004,'Pixar','DVD','Animación, Acción','/Imagenes/LosIncreibles.jpg','Familia de superhéroes vuelve a la acción.'),(46,'Los Juegos del Hambre',13.50,25,2012,'Lionsgate','Blu-ray','Acción, Ciencia ficción','/Imagenes/JuegosDelHambre.jpg','Katniss lucha por sobrevivir.'),(47,'Batman Begins',14.99,22,2005,'Warner Bros.','Blu-ray','Acción, Superhéroes','/Imagenes/BatmanBegins.jpg','El origen de Batman.'),(48,'La Forma del Agua',12.50,15,2017,'Fox Searchlight','Blu-ray','Romance, Fantasía','/Imagenes/LaFormaDelAgua.jpg','Una historia de amor con una criatura acuática.'),(49,'Soul',11.50,35,2020,'Pixar','4K UHD','Animación, Fantasía','/Imagenes/Soul.jpg','Un músico descubre el sentido de la vida.'),(50,'El Francotirador',13.99,18,2014,'Warner Bros.','Blu-ray','Acción, Drama','/Imagenes/ElFrancotirador.jpg','Historia del francotirador Chris Kyle.'),(51,'Alien: El Octavo Pasajero',14.50,16,1979,'20th Century Fox','Blu-ray','Terror, Ciencia ficción','/Imagenes/Alien.jpg','La tripulación lucha contra una criatura mortal.'),(52,'The Irishman',15.99,20,2019,'Netflix','Blu-ray','Drama, Crimen','/Imagenes/TheIrishman.jpg','Un asesino a sueldo reflexiona sobre su vida.'),(53,'La Llegada',12.99,22,2016,'Paramount Pictures','Blu-ray','Ciencia ficción, Drama','/Imagenes/LaLlegada.jpg','Una lingüista debe comunicarse con extraterrestres.');
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
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `usuario`
--

LOCK TABLES `usuario` WRITE;
/*!40000 ALTER TABLE `usuario` DISABLE KEYS */;
INSERT INTO `usuario` VALUES (1,'1','1','1',5),(3,'1','1','1',NULL);
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

-- Dump completed on 2025-11-30 22:07:05
