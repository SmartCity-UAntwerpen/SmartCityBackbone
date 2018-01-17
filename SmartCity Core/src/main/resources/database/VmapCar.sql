
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
  (46,-6.1,-28.78,0.73,0.69, 'v'),
  (47,-6.47,-21.69,0.66,0.75, 'v'),
  (48,-5.91,-1.03,0.52,0.85, 'v'),
  (49,6.09,0.21,-0.04,1.00, 'v');
/*!40000 ALTER TABLE core.point_car ENABLE KEYS */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;