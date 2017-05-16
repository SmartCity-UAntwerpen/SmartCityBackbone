-- SmartCity Database Test Data Version 0.0.1, 21/07/2016
--
-- Database: smartcitydb - 2016 UAntwerpen
-- ----------------------------------------------------
-- Server version   5.6.29

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

--
-- Test data for table `point`
--

DROP TABLE IF EXISTS smartcitydb.point;
CREATE TABLE smartcitydb.point (
  `pid` bigint(20) NOT NULL AUTO_INCREMENT,
  `type` varchar(255) DEFAULT NULL,
  `acces` varchar(255) DEFAULT NULL,
  `hub` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`pid`)
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS smartcitydb.point_robot;
CREATE TABLE smartcitydb.point_robot (
  `prid` bigint(20) NOT NULL,
  `rfid` varchar(255) DEFAULT NULL,
  `pointlock` int(11) DEFAULT '1',
  PRIMARY KEY (`prid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Table attributes for Point
-- (Id, RFID, Type, Pointlock)
--
/*!40101 LOCK TABLES smartcitydb.point WRITE; */
/*!40000 ALTER TABLE smartcitydb.point DISABLE KEYS */;

--
-- Test data for table `link`
--

DROP TABLE IF EXISTS smartcitydb.link;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE smartcitydb.link (
  `lid` bigint(20) NOT NULL AUTO_INCREMENT,
  `start_point` bigint(20) DEFAULT NULL,
  `stop_point` bigint(20) DEFAULT NULL,
  `weight` int(11) DEFAULT '1',
  `acces` varchar(255) DEFAULT NULL,
  `length` bigint(11) DEFAULT NULL,
  PRIMARY KEY (`lid`),
  KEY `fk_startpoint` (`start_point`),
  KEY `fk_stoppoint` (`stop_point`),
  CONSTRAINT `fk_startpoint` FOREIGN KEY (`start_point`) REFERENCES smartcitydb.point (`pid`),
  CONSTRAINT `fk_stoppoint` FOREIGN KEY (`stop_point`) REFERENCES smartcitydb.point (`pid`)
) ENGINE=InnoDB AUTO_INCREMENT=57 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

DROP TABLE IF EXISTS smartcitydb.link_robot;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE smartcitydb.link_robot (
  `lrid` bigint(20) NOT NULL,
  `start_direction` varchar(255) DEFAULT NULL,
  `stop_direction` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`lrid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table attributes for Link
-- (Id, length, startDirection, startPoint, stopDirection, stopPoint, weight)
--
/*!40101 LOCK TABLES smartcitydb.link WRITE; */
/*!40000 ALTER TABLE smartcitydb.link DISABLE KEYS */;

--
-- Test data for table `bot`
--

DROP TABLE IF EXISTS smartcitydb.bot;
CREATE TABLE smartcitydb.bot (
  `rid` bigint(20) NOT NULL AUTO_INCREMENT,
  `job_id` bigint(20) DEFAULT NULL,
  `percentage_completed` int(11) DEFAULT NULL,
  `state` varchar(255) DEFAULT NULL,
  `link_id` bigint(20) DEFAULT NULL,
  `vehicle_type` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`rid`),
  KEY `FK_g2k7qbjgq85d7hmmov6r4benu` (`link_id`),
  CONSTRAINT `FK_g2k7qbjgq85d7hmmov6r4benu` FOREIGN KEY (`link_id`) REFERENCES `link` (`lid`)
) ENGINE=InnoDB AUTO_INCREMENT=260 DEFAULT CHARSET=utf8;

/*!40101 LOCK TABLES smartcitydb.bot WRITE; */
/*!40000 ALTER TABLE smartcitydb.bot DISABLE KEYS */;
/* INSERT INTO smartcitydb.bot VALUES */;
/*!40000 ALTER TABLE smartcitydb.bot ENABLE KEYS */;
/*!40101 UNLOCK TABLES; */

--
-- Test data for table `trafficlight`
--
