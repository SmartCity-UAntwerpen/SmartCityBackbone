
DROP TABLE IF EXISTS core.point_car;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE core.point_car (
  `pid` bigint(11) NOT NULL,
  `x` float DEFAULT NULL,
  `y` float DEFAULT NULL,
  `z` float DEFAULT NULL,
  `w` float DEFAULT NULL,
  `map` VARCHAR(255) DEFAULT NULL,
  KEY `pid` (`pid`),
  FOREIGN KEY (`pid`) REFERENCES core.point (`pid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `point_car`
--
;
/*!40000 ALTER TABLE core.point_car DISABLE KEYS */;
INSERT INTO core.point_car VALUES
  (46,0.5,0,-1,0.02, 'z'),
  (47,-13.4,-0.53,0.71,0.71, 'z'),
  (48,-27.14,-1.11,-0.3,0.95, 'z'),
  (49,-28.25,-9.19,-0.71,0.71, 'z');
/*!40000 ALTER TABLE core.point_car ENABLE KEYS */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;