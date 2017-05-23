-- SmartCity Database Schema Version 0.0.1, 21/07/2016
--
-- Database: smartcitydb - 2016 UAntwerpen
-- ----------------------------------------------------
-- Server version   5.6.29

CREATE DATABASE  IF NOT EXISTS `smartcitydb` /*!40100 DEFAULT CHARACTER SET latin1 */;
USE `smartcitydb`;

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

DROP TABLE IF EXISTS `point`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `point` (
  `pid` bigint(20) NOT NULL AUTO_INCREMENT,
  `type` varchar(255) DEFAULT NULL,
  `access` varchar(255) DEFAULT NULL,
  `hub` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`pid`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `point`
--

LOCK TABLES `point` WRITE;
/*!40000 ALTER TABLE `point` DISABLE KEYS */;
INSERT INTO `point` VALUES (1,'ENDPOINT','robot',1),(2,'ENDPOINT','robot',2),(3,'CROSSING','robot',0),(4,'CROSSING','robot',0),(5,'ENDPOINT','drone',1),(6,'ENDPOINT','drone',2),(7,'ENDPOINT','drone',3),(8,'ENDPOINT','car',1),(9,'CROSSING','car',NULL),(10,'ENDPOINT','car',2);
/*!40000 ALTER TABLE `point` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;


DROP TABLE IF EXISTS `point_car`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `point_car` (
  `cid` int(11) NOT NULL,
  `x` float DEFAULT NULL,
  `y` float DEFAULT NULL,
  `z` float DEFAULT NULL,
  `w` float DEFAULT NULL,
  PRIMARY KEY (`cid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `point_car`
--

LOCK TABLES `point_car` WRITE;
/*!40000 ALTER TABLE `point_car` DISABLE KEYS */;
INSERT INTO `point_car` VALUES (8,1,2,1,1),(9,5,4,1,1),(10,7,2,1,1);
/*!40000 ALTER TABLE `point_car` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

DROP TABLE IF EXISTS `point_drone`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `point_drone` (
  `did` bigint(20) NOT NULL,
  `x` float DEFAULT NULL,
  `y` float DEFAULT NULL,
  `z` float DEFAULT NULL,
  PRIMARY KEY (`did`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `point_drone`
--

LOCK TABLES `point_drone` WRITE;
/*!40000 ALTER TABLE `point_drone` DISABLE KEYS */;
INSERT INTO `point_drone` VALUES (5,1,3,5),(6,6,1,2),(7,3,7,1);
/*!40000 ALTER TABLE `point_drone` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

DROP TABLE IF EXISTS `point_robot`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `point_robot` (
  `prid` bigint(20) NOT NULL,
  `rfid` varchar(255) DEFAULT NULL,
  `pointlock` int(11) DEFAULT NULL,
  PRIMARY KEY (`prid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `point_robot`
--

LOCK TABLES `point_robot` WRITE;
/*!40000 ALTER TABLE `point_robot` DISABLE KEYS */;
INSERT INTO `point_robot` VALUES (1,'4e',0),(2,'3r',0),(3,'9d',0),(4,'1y',0);
/*!40000 ALTER TABLE `point_robot` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;
--
-- Table structure for table `trafficlight`
--



--
-- Table structure for table `link`
--

DROP TABLE IF EXISTS `link`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `link` (
  `lid` bigint(20) NOT NULL AUTO_INCREMENT,
  `start_point` bigint(20) DEFAULT NULL,
  `stop_point` bigint(20) DEFAULT NULL,
  `weight` int(11) DEFAULT '1',
  `access` varchar(255) DEFAULT NULL,
  `length` bigint(11) DEFAULT NULL,
  PRIMARY KEY (`lid`),
  KEY `fk_startpoint` (`start_point`),
  KEY `fk_stoppoint` (`stop_point`),
  CONSTRAINT `fk_startpoint` FOREIGN KEY (`start_point`) REFERENCES `point` (`pid`),
  CONSTRAINT `fk_stoppoint` FOREIGN KEY (`stop_point`) REFERENCES `point` (`pid`)
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `link`
--

LOCK TABLES `link` WRITE;
/*!40000 ALTER TABLE `link` DISABLE KEYS */;
INSERT INTO `link` VALUES (1,1,3,1,'robot',1),(2,3,1,1,'robot',1),(3,3,4,1,'robot',1),(4,4,3,1,'robot',1),(5,4,2,1,'robot',1),(6,2,4,1,'robot',1),(7,5,6,2,'drone',10),(8,6,5,2,'drone',10),(9,6,7,2,'drone',10),(10,7,6,2,'drone',10),(11,5,7,2,'drone',10),(12,7,5,2,'drone',10),(13,8,9,1,'car',5),(14,9,8,1,'car',5),(15,9,10,1,'car',5),(16,10,9,1,'car',5),(17,1,5,3,'wait',1),(18,5,1,3,'wait',1),(19,1,8,3,'wait',1),(20,8,1,3,'wait',1),(21,5,8,3,'wait',1),(22,8,5,3,'wait',1),(23,2,10,3,'wait',1),(24,10,2,3,'wait',1);
/*!40000 ALTER TABLE `link` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;


DROP TABLE IF EXISTS `link_robot`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `link_robot` (
  `lrid` bigint(20) NOT NULL,
  `start_direction` varchar(255) DEFAULT NULL,
  `stop_direction` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`lrid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `link_robot`
--

LOCK TABLES `link_robot` WRITE;
/*!40000 ALTER TABLE `link_robot` DISABLE KEYS */;
INSERT INTO `link_robot` VALUES (1,'N','Z'),(2,'Z','N'),(3,'N','Z'),(4,'Z','N'),(5,'W','O'),(6,'O','W');
/*!40000 ALTER TABLE `link_robot` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

--
-- Table structure for table `point`
--


DROP TABLE IF EXISTS `trafficlight`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `trafficlight` (
  `tlid` bigint(20) NOT NULL AUTO_INCREMENT,
  `direction` varchar(255) DEFAULT NULL,
  `point_id` bigint(20) DEFAULT NULL,
  `state` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`tlid`),
  KEY `fk_pointid` (`point_id`),
  CONSTRAINT `FK_36q0ntiwsex3ooj744c0t9py1` FOREIGN KEY (`point_id`) REFERENCES `point` (`pid`),
  CONSTRAINT `fk_pointid` FOREIGN KEY (`point_id`) REFERENCES `point` (`pid`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;



DROP TABLE IF EXISTS `bot`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `bot` (
  `rid` bigint(20) NOT NULL AUTO_INCREMENT,
  `job_id` bigint(20) DEFAULT NULL,
  `percentage_completed` int(11) DEFAULT NULL,
  `state` varchar(255) DEFAULT NULL,
  `link_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`rid`),
  KEY `FK_g2k7qbjgq85d7hmmov6r4benu` (`link_id`),
  CONSTRAINT `FK_g2k7qbjgq85d7hmmov6r4benu` FOREIGN KEY (`link_id`) REFERENCES `link` (`lid`)
) ENGINE=InnoDB AUTO_INCREMENT=260 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;



/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
