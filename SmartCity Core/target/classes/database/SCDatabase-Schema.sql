-- SmartCity Database Schema Version 0.0.1, 21/07/2016
--
-- Database: core - 2016 UAntwerpen
-- ----------------------------------------------------
-- Server version   5.6.29

-- CREATE DATABASE  IF NOT EXISTS `core` /*!40100 DEFAULT CHARACTER SET latin1 */;
USE `backbone`;

DROP TABLE IF EXISTS backbone.backend_info;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE backbone.backend_info (
  `map_id` int NOT NULL AUTO_INCREMENT,
  `hostname` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `port` int DEFAULT NULL,
  PRIMARY KEY (`map_id`)
) ENGINE=InnoDB AUTO_INCREMENT=50 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;
