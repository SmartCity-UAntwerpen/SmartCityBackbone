-- SmartCity Database Test Data Version 0.0.1, 21/07/2016
--
-- Database: smartcitydb - 2016 UAntwerpen
-- ----------------------------------------------------
-- Server version   5.6.29

DROP TABLE IF EXISTS core.point;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE core.point (
  `pid` bigint(20) NOT NULL AUTO_INCREMENT,
  `type` varchar(255) DEFAULT NULL,
  `access` varchar(255) DEFAULT NULL,
  `xcoord` bigint(20) DEFAULT NULL,
  `ycoord` bigint(20) DEFAULT NULL,
  `hub` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`pid`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `point`
--


/*!40000 ALTER TABLE core.point DISABLE KEYS */;
INSERT INTO core.point VALUES (1,'ENDPOINT','robot',0,0,1),(2,'ENDPOINT','robot',0,1,2),(3,'CROSSING','robot',0,2,0),(4,'CROSSING','robot',0,3,0),(5,'ENDPOINT','drone',1,0,1),(6,'ENDPOINT','drone',1,2,2),(7,'ENDPOINT','drone',1,3,3),(8,'ENDPOINT','car',2,2,1),(9,'CROSSING','car',2,3,0),(10,'ENDPOINT','car',2,4,2),(11,'ENDPOINT','car',5,5,4);
/*!40000 ALTER TABLE core.point ENABLE KEYS */;

/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

DROP TABLE IF EXISTS core.point_car;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE core.point_car (
  `pid` int(11) NOT NULL,
  `x` float DEFAULT NULL,
  `y` float DEFAULT NULL,
  `z` float DEFAULT NULL,
  `w` float DEFAULT NULL,
  FOREIGN KEY (`pid`) REFERENCES core.point (`pid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `point_car`
--


/*!40000 ALTER TABLE core.point_car DISABLE KEYS */;
INSERT INTO core.point_car VALUES (8,0.5,0,-1,0.02),(9,-13.4,-0.53,0.71,0.71),(10,-27.14,-1.11,-0.3,0.95),(11,-28.25,-9.19,-0.71,0.71);
/*!40000 ALTER TABLE core.point_car ENABLE KEYS */;

/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

DROP TABLE IF EXISTS core.point_drone;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE core.point_drone (
  `pid` bigint(20) NOT NULL,
  `x` float DEFAULT NULL,
  `y` float DEFAULT NULL,
  `z` float DEFAULT NULL,
  FOREIGN KEY (`pid`) REFERENCES core.point (`pid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `point_drone`
--

/*!40000 ALTER TABLE core.point_drone DISABLE KEYS */;
INSERT INTO core.point_drone VALUES (5,1,3,5),(6,6,1,2),(7,3,7,1);
/*!40000 ALTER TABLE core.point_drone ENABLE KEYS */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

DROP TABLE IF EXISTS core.point_robot;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE core.point_robot (
  `pid` bigint(20) NOT NULL,
  `rfid` varchar(255) DEFAULT NULL,
  `pointlock` int(11) DEFAULT NULL,
  FOREIGN KEY (`pid`) REFERENCES core.point (`pid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `point_robot`
--

/*!40000 ALTER TABLE core.point_robot DISABLE KEYS */;
INSERT INTO core.point_robot VALUES (1,'4e',0),(2,'3r',0),(3,'9d',0),(4,'1y',0);
/*!40000 ALTER TABLE core.point_robot ENABLE KEYS */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

DROP TABLE IF EXISTS core.link;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE core.link (
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

/*!40000 ALTER TABLE core.link DISABLE KEYS */;
INSERT INTO core.link VALUES (1,1,3,1,'robot',1),(2,3,1,1,'robot',1),(3,3,4,1,'robot',1),(4,4,3,1,'robot',1),(5,4,2,1,'robot',1),(6,2,4,1,'robot',1),(7,5,6,2,'drone',10),(8,6,5,2,'drone',10),(9,6,7,2,'drone',10),(10,7,6,2,'drone',10),(11,5,7,2,'drone',10),(12,7,5,2,'drone',10),(13,8,9,1,'car',5),(14,9,8,1,'car',5),(15,9,10,1,'car',5),(16,10,9,1,'car',5);
/*!40000 ALTER TABLE core.link ENABLE KEYS */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

DROP TABLE IF EXISTS core.link_robot;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE core.link_robot (
  `lid` bigint(20) NOT NULL,
  `start_direction` varchar(255) DEFAULT NULL,
  `stop_direction` varchar(255) DEFAULT NULL,
  FOREIGN KEY (`lid`) REFERENCES core.link (`lid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `link_robot`
--

/*!40000 ALTER TABLE core.link_robot DISABLE KEYS */;
INSERT INTO core.link_robot VALUES (1,'N','Z'),(2,'Z','N'),(3,'N','Z'),(4,'Z','N'),(5,'W','O'),(6,'O','W');
/*!40000 ALTER TABLE core.link_robot ENABLE KEYS */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

DROP TABLE IF EXISTS core.bot;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE core.bot (
  `rid` bigint(20) NOT NULL AUTO_INCREMENT,
  `job_id` bigint(20) DEFAULT NULL,
  `percentage_completed` int(11) DEFAULT NULL,
  `state` varchar(255) DEFAULT NULL,
  `vehicle_type` varchar(255) DEFAULT NULL,
  `link_id` bigint(20) DEFAULT NULL,
  `x` float DEFAULT NULL,
  `y` float DEFAULT NULL,
  `z` float DEFAULT NULL,
  `w` float DEFAULT NULL,
  PRIMARY KEY (`rid`),
  KEY `FK_g2k7qbjgq85d7hmmov6r4benu` (`link_id`),
  CONSTRAINT `FK_g2k7qbjgq85d7hmmov6r4benu` FOREIGN KEY (`link_id`) REFERENCES core.link (`lid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

/*!40000 ALTER TABLE core.bot DISABLE KEYS */;
/*!40000 ALTER TABLE core.bot ENABLE KEYS */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;


DROP TABLE IF EXISTS core.trafficlight;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE core.trafficlight (
  `tlid` bigint(20) NOT NULL AUTO_INCREMENT,
  `direction` varchar(255) DEFAULT NULL,
  `point_id` bigint(20) DEFAULT NULL,
  `state` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`tlid`),
  KEY `fk_pointid` (`point_id`),
  CONSTRAINT `FK_36q0ntiwsex3ooj744c0t9py1` FOREIGN KEY (`point_id`) REFERENCES core.point (`pid`),
  CONSTRAINT `fk_pointid` FOREIGN KEY (`point_id`) REFERENCES core.point (`pid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;