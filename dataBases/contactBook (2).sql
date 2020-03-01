-- MySQL dump 10.13  Distrib 8.0.19, for Win64 (x86_64)
--
-- Host: localhost    Database: contactbook
-- ------------------------------------------------------
-- Server version	8.0.19

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
-- Table structure for table `addresses`
--

DROP TABLE IF EXISTS `addresses`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `addresses` (
  `idaddress` int NOT NULL AUTO_INCREMENT,
  `country` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `city` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `street` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `house` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `flat` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `index` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  PRIMARY KEY (`idaddress`),
  UNIQUE KEY `idaddress_UNIQUE` (`idaddress`)
) ENGINE=InnoDB AUTO_INCREMENT=35 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `addresses`
--

LOCK TABLES `addresses` WRITE;
/*!40000 ALTER TABLE `addresses` DISABLE KEYS */;
INSERT INTO `addresses` VALUES (1,'Belarus','Minsk','Marks\'','12A','108b','220022'),(2,'Belarus','Minsk','Niamiha','6','12','226622'),(3,'uzbekistan','ajzejbardzhan','tbilisi','12','32','some index...');
/*!40000 ALTER TABLE `addresses` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `birthdays`
--

DROP TABLE IF EXISTS `birthdays`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `birthdays` (
  `idDate` int NOT NULL,
  `day` int NOT NULL,
  `month` int NOT NULL,
  `year` int NOT NULL,
  PRIMARY KEY (`idDate`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `birthdays`
--

LOCK TABLES `birthdays` WRITE;
/*!40000 ALTER TABLE `birthdays` DISABLE KEYS */;
INSERT INTO `birthdays` VALUES (1,24,5,2001),(2,22,1,1992),(3,1,1,201);
/*!40000 ALTER TABLE `birthdays` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `persons`
--

DROP TABLE IF EXISTS `persons`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `persons` (
  `idPerson` int NOT NULL AUTO_INCREMENT,
  `name` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `lastName` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `patronymic` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `website` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `email` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `job` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `comment` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `familyStatus` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `gender` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `nationality` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `idBirthDate` int DEFAULT NULL,
  `idAddress` int DEFAULT NULL,
  PRIMARY KEY (`idPerson`),
  UNIQUE KEY `idPerson_UNIQUE` (`idPerson`),
  KEY `idAddress_idx` (`idAddress`),
  KEY `idBirthDate_idx` (`idBirthDate`),
  CONSTRAINT `birth` FOREIGN KEY (`idBirthDate`) REFERENCES `birthdays` (`idDate`) ON DELETE SET NULL ON UPDATE SET NULL,
  CONSTRAINT `idAddress` FOREIGN KEY (`idAddress`) REFERENCES `addresses` (`idaddress`) ON DELETE SET NULL ON UPDATE SET NULL
) ENGINE=InnoDB AUTO_INCREMENT=928 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `persons`
--

LOCK TABLES `persons` WRITE;
/*!40000 ALTER TABLE `persons` DISABLE KEYS */;
INSERT INTO `persons` VALUES (1,'ivan','ivanow','ivanavich','ivan.com','ivan@mail.com','worker','some comment...','unmarried','male','belarusian',1,1),(2,'Barys','Barysaw','Barysavich','barys.com','barys@mail.com',NULL,NULL,'married','male','belarusian',2,2),(3,'Maksim','Bogdan',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'uzbek',3,3);
/*!40000 ALTER TABLE `persons` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `telephones`
--

DROP TABLE IF EXISTS `telephones`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `telephones` (
  `telephoneId` int NOT NULL AUTO_INCREMENT,
  `countryNumber` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `operatorNumber` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `number` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `typeOfTelephone` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `personId` int DEFAULT NULL,
  PRIMARY KEY (`telephoneId`),
  UNIQUE KEY `telephoneId_UNIQUE` (`telephoneId`),
  KEY `personId_idx` (`personId`),
  CONSTRAINT `personId` FOREIGN KEY (`personId`) REFERENCES `persons` (`idPerson`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=59 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `telephones`
--

LOCK TABLES `telephones` WRITE;
/*!40000 ALTER TABLE `telephones` DISABLE KEYS */;
INSERT INTO `telephones` VALUES (1,'375','44','473-37-11','mobile',1),(2,'375','44','474-37-11','home',1);
/*!40000 ALTER TABLE `telephones` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2020-03-01 22:42:04
