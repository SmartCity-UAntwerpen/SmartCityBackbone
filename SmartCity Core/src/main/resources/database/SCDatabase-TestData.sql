-- MySQL dump 10.13  Distrib 5.6.36, for Linux (x86_64)
--
-- Host: localhost    Database: core
-- ------------------------------------------------------
-- Server version	5.6.36

/*!40101 SET @OLD_CHARACTER_SET_CLIENT = @@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS = @@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION = @@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE = @@TIME_ZONE */;
/*!40103 SET TIME_ZONE = '+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS = @@UNIQUE_CHECKS, UNIQUE_CHECKS = 0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS = @@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS = 0 */;
/*!40101 SET @OLD_SQL_MODE = @@SQL_MODE, SQL_MODE = 'NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES = @@SQL_NOTES, SQL_NOTES = 0 */;

--
-- Current Database: `core`
--

-- CREATE DATABASE /*!32312 IF NOT EXISTS */ `core` /*!40100 DEFAULT CHARACTER SET latin1 */;

USE `core`;

--
-- Table structure for table `bot`
-- structure is already  created in SCDatabase-Schema, values should be created
--

DROP TABLE IF EXISTS `bot`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `bot` (
  `rid`                  BIGINT(20) NOT NULL AUTO_INCREMENT,
  `job_id`               BIGINT(20)          DEFAULT NULL,
  `percentage_completed` INT(11)             DEFAULT NULL,
  `state`                VARCHAR(255)        DEFAULT NULL,
  `vehicle_type`         VARCHAR(255)        DEFAULT NULL,
  `link_id`              BIGINT(20)          DEFAULT NULL,
  `x`                    FLOAT               DEFAULT NULL,
  `y`                    FLOAT               DEFAULT NULL,
  `z`                    FLOAT               DEFAULT NULL,
  `w`                    FLOAT               DEFAULT NULL,
  PRIMARY KEY (`rid`),
  KEY `FK_g2k7qbjgq85d7hmmov6r4benu` (`link_id`),
  CONSTRAINT `FK_g2k7qbjgq85d7hmmov6r4benu` FOREIGN KEY (`link_id`) REFERENCES `link` (`lid`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `link`
--

DROP TABLE IF EXISTS `link`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `link` (
  `lid`         BIGINT(20) NOT NULL AUTO_INCREMENT,
  `start_point` BIGINT(20)          DEFAULT NULL,
  `stop_point`  BIGINT(20)          DEFAULT NULL,
  `weight`      INT(11)             DEFAULT '1',
  `access`      VARCHAR(255)        DEFAULT NULL,
  `length`      BIGINT(11)          DEFAULT NULL,
  PRIMARY KEY (`lid`),
  KEY `fk_startpoint` (`start_point`),
  KEY `fk_stoppoint` (`stop_point`),
  CONSTRAINT `fk_startpoint` FOREIGN KEY (`start_point`) REFERENCES `point` (`pid`),
  CONSTRAINT `fk_stoppoint` FOREIGN KEY (`stop_point`) REFERENCES `point` (`pid`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 1045
  DEFAULT CHARSET = utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `link`
--

LOCK TABLES `link` WRITE;
/*!40000 ALTER TABLE `link`
  DISABLE KEYS */;
INSERT INTO `link` VALUES
  (1, 1, 2, 1, 'robot', 1),
  (2, 2, 1, 1, 'robot', 1),
  (3, 2, 3, 1, 'robot', 1),
  (4, 1, 3, 1, 'robot', 2),
  (5, 2, 5, 1, 'robot', 3),
  (6, 3, 1, 1, 'robot', 2),
  (7, 3, 7, 1, 'robot', 1),
  (8, 3, 2, 1, 'robot', 1),
  (9, 3, 4, 1, 'robot', 1),
  (10, 4, 3, 1, 'robot', 1),
  (11, 5, 1, 1, 'robot', 3),
  (12, 5, 6, 1, 'robot', 1),
  (13, 5, 10, 1, 'robot', 2),
  (15, 6, 5, 1, 'robot', 1),
  (16, 7, 8, 1, 'robot', 2),
  (18, 8, 9, 1, 'robot', 1),
  (19, 8, 2, 1, 'robot', 2),
  (22, 9, 14, 1, 'robot', 2),
  (23, 9, 8, 1, 'robot', 1),
  (24, 10, 5, 1, 'robot', 2),
  (25, 11, 13, 1, 'robot', 1),
  (27, 12, 9, 1, 'robot', 2),
  (28, 13, 11, 1, 'robot', 1),
  (29, 13, 12, 1, 'robot', 1),
  (30, 13, 14, 1, 'robot', 1),
  (31, 13, 15, 1, 'robot', 2),
  (33, 14, 13, 1, 'robot', 1),
  (34, 14, 15, 1, 'robot', 1),
  (35, 14, 16, 1, 'robot', 1),
  (36, 15, 13, 1, 'robot', 2),
  (37, 15, 14, 1, 'robot', 1),
  (38, 15, 17, 1, 'robot', 1),
  (39, 16, 14, 1, 'robot', 1),
  (40, 16, 17, 1, 'robot', 1),
  (41, 16, 19, 1, 'robot', 2),
  (42, 17, 15, 1, 'robot', 1),
  (43, 17, 16, 1, 'robot', 1),
  (45, 18, 19, 1, 'robot', 2),
  (47, 19, 18, 1, 'robot', 2),
  (48, 19, 17, 1, 'robot', 3),
  (49, 19, 20, 1, 'robot', 1),
  (50, 20, 19, 1, 'robot', 1),
  (51, 21, 22, 1, 'robot', 1),
  (52, 22, 21, 1, 'robot', 1),
  (53, 22, 23, 1, 'robot', 2),
  (54, 22, 25, 1, 'robot', 2),
  (55, 22, 24, 1, 'robot', 3),
  (56, 23, 22, 1, 'robot', 2),
  (57, 24, 22, 1, 'robot', 3),
  (58, 24, 25, 1, 'robot', 1),
  (59, 24, 26, 1, 'robot', 1),
  (60, 25, 22, 1, 'robot', 2),
  (61, 25, 27, 1, 'robot', 1),
  (62, 25, 24, 1, 'robot', 1),
  (63, 26, 24, 1, 'robot', 1),
  (64, 26, 27, 1, 'robot', 1),
  (65, 26, 28, 1, 'robot', 2),
  (66, 27, 25, 1, 'robot', 1),
  (67, 27, 26, 1, 'robot', 1),
  (68, 27, 28, 1, 'robot', 1),
  (69, 27, 30, 1, 'robot', 2),
  (70, 28, 27, 1, 'robot', 1),
  (71, 28, 26, 1, 'robot', 2),
  (72, 28, 40, 1, 'robot', 1),
  (73, 28, 29, 1, 'robot', 1),
  (74, 29, 28, 1, 'robot', 1),
  (75, 30, 31, 1, 'robot', 1),
  (76, 30, 27, 1, 'robot', 2),
  (78, 31, 30, 1, 'robot', 1),
  (79, 31, 35, 1, 'robot', 2),
  (81, 32, 33, 1, 'robot', 1),
  (82, 33, 32, 1, 'robot', 1),
  (83, 33, 34, 1, 'robot', 2),
  (84, 33, 35, 1, 'robot', 1),
  (85, 33, 39, 1, 'robot', 1),
  (86, 34, 33, 1, 'robot', 2),
  (87, 34, 35, 1, 'robot', 1),
  (88, 34, 36, 1, 'robot', 4),
  (89, 35, 33, 1, 'robot', 1),
  (90, 35, 34, 1, 'robot', 1),
  (91, 35, 36, 1, 'robot', 3),
  (92, 35, 31, 1, 'robot', 2),
  (93, 36, 37, 1, 'robot', 2),
  (94, 36, 38, 1, 'robot', 1),
  (95, 36, 34, 1, 'robot', 4),
  (96, 36, 35, 1, 'robot', 3),
  (97, 37, 36, 1, 'robot', 2),
  (98, 38, 36, 1, 'robot', 1),
  (99, 39, 31, 1, 'robot', 2),
  (100, 40, 30, 1, 'robot', 2),

  (102, 41, 44, 3, 'drone', NULL),
  (103, 41, 45, 3, 'drone', NULL),
  (104, 42, 44, 3, 'drone', NULL),
  (105, 42, 45, 3, 'drone', NULL),
  (106, 43, 44, 3, 'drone', NULL),
  (107, 43, 45, 3, 'drone', NULL),
  (108, 44, 41, 3, 'drone', NULL),
  (109, 44, 42, 3, 'drone', NULL),
  (110, 45, 42, 3, 'drone', NULL),
  (111, 45, 43, 3, 'drone', NULL),

  (112, 47, 46, 2, 'car', NULL),
  (113, 47, 48, 2, 'car', NULL),
  (114, 48, 47, 2, 'car', NULL),
  (115, 48, 49, 2, 'car', NULL),
  (116, 46, 47, 2, 'car', NULL),
  (117, 46, 49, 2, 'car', NULL),
  (118, 49, 46, 2, 'car', NULL),
  (119, 49, 48, 2, 'car', NULL),

  (945, 41, 44, 1, 'droneTop', 1),
  (946, 41, 45, 1, 'droneTop', 1),
  (947, 41, 6, 1, 'wait', 1),
  (948, 42, 44, 1, 'droneTop', 1),
  (949, 42, 45, 1, 'droneTop', 1),
  (950, 42, 10, 1, 'wait', 1),
  (951, 43, 44, 1, 'droneTop', 1),
  (952, 43, 45, 1, 'droneTop', 1),
  (953, 43, 47, 1, 'wait', 1),
  (954, 43, 11, 1, 'wait', 1),
  (955, 44, 41, 1, 'droneTop', 1),
  (956, 44, 42, 1, 'droneTop', 1),
  (957, 44, 46, 1, 'wait', 1),
  (958, 44, 32, 1, 'wait', 1),
  (959, 45, 42, 1, 'droneTop', 1),
  (960, 45, 43, 1, 'droneTop', 1),
  (961, 45, 21, 1, 'wait', 1),
  (962, 46, 47, 1, 'carTop', 1),
  (963, 46, 49, 1, 'carTop', 1),
  (964, 46, 44, 1, 'wait', 1),
  (965, 46, 32, 1, 'wait', 1),
  (966, 47, 46, 1, 'carTop', 1),
  (967, 47, 48, 1, 'carTop', 1),
  (968, 47, 43, 1, 'wait', 1),
  (969, 47, 11, 1, 'wait', 1),
  (970, 48, 47, 1, 'carTop', 1),
  (971, 48, 49, 1, 'carTop', 1),
  (972, 48, 20, 1, 'wait', 1),
  (973, 49, 46, 1, 'carTop', 1),
  (974, 49, 48, 1, 'carTop', 1),
  (975, 49, 29, 1, 'wait', 1),
  (976, 4, 6, 1, 'robotTop', 1),
  (977, 4, 10, 1, 'robotTop', 1),
  (978, 4, 11, 1, 'robotTop', 1),
  (979, 4, 18, 1, 'robotTop', 1),
  (980, 4, 20, 1, 'robotTop', 1),
  (981, 6, 10, 1, 'robotTop', 1),
  (982, 6, 11, 1, 'robotTop', 1),
  (983, 6, 18, 1, 'robotTop', 1),
  (984, 6, 20, 1, 'robotTop', 1),
  (985, 6, 4, 1, 'robotTop', 1),
  (986, 6, 41, 1, 'wait', 1),
  (987, 10, 6, 1, 'robotTop', 1),
  (988, 10, 11, 1, 'robotTop', 1),
  (989, 10, 18, 1, 'robotTop', 1),
  (990, 10, 20, 1, 'robotTop', 1),
  (991, 10, 4, 1, 'robotTop', 1),
  (992, 10, 42, 1, 'wait', 1),
  (993, 11, 6, 1, 'robotTop', 1),
  (994, 11, 10, 1, 'robotTop', 1),
  (995, 11, 4, 1, 'robotTop', 1),
  (996, 11, 18, 1, 'robotTop', 1),
  (997, 11, 20, 1, 'robotTop', 1),
  (998, 11, 43, 1, 'wait', 1),
  (999, 11, 47, 1, 'wait', 1),
  (1000, 18, 11, 1, 'robotTop', 1),
  (1001, 18, 6, 1, 'robotTop', 1),
  (1002, 18, 10, 1, 'robotTop', 1),
  (1003, 18, 4, 1, 'robotTop', 1),
  (1004, 18, 20, 1, 'robotTop', 1),
  (1005, 20, 11, 1, 'robotTop', 1),
  (1006, 20, 6, 1, 'robotTop', 1),
  (1007, 20, 10, 1, 'robotTop', 1),
  (1008, 20, 4, 1, 'robotTop', 1),
  (1009, 20, 18, 1, 'robotTop', 1),
  (1010, 20, 48, 1, 'wait', 1),
  (1011, 21, 23, 1, 'robotTop', 1),
  (1012, 21, 32, 1, 'robotTop', 1),
  (1013, 21, 37, 1, 'robotTop', 1),
  (1014, 21, 38, 1, 'robotTop', 1),
  (1015, 21, 29, 1, 'robotTop', 1),
  (1016, 21, 45, 1, 'wait', 1),
  (1017, 23, 21, 1, 'robotTop', 1),
  (1018, 23, 32, 1, 'robotTop', 1),
  (1019, 23, 37, 1, 'robotTop', 1),
  (1020, 23, 38, 1, 'robotTop', 1),
  (1021, 23, 29, 1, 'robotTop', 1),
  (1022, 29, 21, 1, 'robotTop', 1),
  (1023, 29, 23, 1, 'robotTop', 1),
  (1024, 29, 32, 1, 'robotTop', 1),
  (1025, 29, 37, 1, 'robotTop', 1),
  (1026, 29, 38, 1, 'robotTop', 1),
  (1027, 29, 49, 1, 'wait', 1),
  (1028, 32, 37, 1, 'robotTop', 1),
  (1029, 32, 38, 1, 'robotTop', 1),
  (1030, 32, 21, 1, 'robotTop', 1),
  (1031, 32, 23, 1, 'robotTop', 1),
  (1032, 32, 29, 1, 'robotTop', 1),
  (1033, 32, 44, 1, 'wait', 1),
  (1034, 32, 46, 1, 'wait', 1),
  (1035, 37, 38, 1, 'robotTop', 1),
  (1036, 37, 32, 1, 'robotTop', 1),
  (1037, 37, 21, 1, 'robotTop', 1),
  (1038, 37, 23, 1, 'robotTop', 1),
  (1039, 37, 29, 1, 'robotTop', 1),
  (1040, 38, 37, 1, 'robotTop', 1),
  (1041, 38, 32, 1, 'robotTop', 1),
  (1042, 38, 21, 1, 'robotTop', 1),
  (1043, 38, 23, 1, 'robotTop', 1),
  (1044, 38, 29, 1, 'robotTop', 1);
/*!40000 ALTER TABLE `link`
  ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `link_robot`
--

DROP TABLE IF EXISTS `link_robot`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `link_robot` (
  `lid`             BIGINT(20) NOT NULL,
  `start_direction` VARCHAR(255) DEFAULT NULL,
  `stop_direction`  VARCHAR(255) DEFAULT NULL,
  KEY `lid` (`lid`),
  CONSTRAINT `link_robot_ibfk_1` FOREIGN KEY (`lid`) REFERENCES `link` (`lid`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `link_robot`
-- Some numbers are missing, these are from deleted links
--

LOCK TABLES `link_robot` WRITE;
/*!40000 ALTER TABLE `link_robot`
  DISABLE KEYS */;
INSERT INTO `link_robot`
VALUES (1, 'E', 'W'), (2, 'W', 'E'), (3, 'Z', 'N'), (4, 'Z', 'W'), (5, 'N', 'Z'), (6, 'W', 'Z'), (7, 'E', 'W'),
  (8, 'N', 'Z'), (9, 'Z', 'N'), (10, 'N', 'Z'), (11, 'W', 'N'), (12, 'N', 'Z'), (13, 'E', 'W'), (15, 'Z', 'N'),
  (16, 'E', 'Z'), (18, 'N', 'Z'), (19, 'W', 'E'), (22, 'E', 'W'), (23, 'Z', 'N'), (24, 'W', 'E'), (25, 'Z', 'N'),
  (27, 'W', 'N'), (28, 'N', 'Z'), (29, 'W', 'E'), (30, 'Z', 'N'), (31, 'E', 'N'), (33, 'N', 'Z'), (34, 'E', 'W'),
  (35, 'Z', 'N'), (36, 'N', 'E'), (37, 'W', 'E'), (38, 'Z', 'N'), (39, 'N', 'Z'), (40, 'E', 'W'), (41, 'Z', 'N'),
  (42, 'N', 'Z'), (43, 'W', 'E'),  (45, 'E', 'W'),
  (47, 'W', 'E'), (48, 'E', 'Z'), (49, 'Z', 'N'), (50, 'N', 'Z'), (51, 'E', 'W'), (52, 'W', 'E'), (53, 'N', 'Z'),
  (54, 'E', 'W'), (55, 'Z', 'W'), (56, 'Z', 'N'), (57, 'W', 'Z'), (58, 'N', 'Z'), (59, 'E', 'W'), (60, 'W', 'E'),
  (61, 'E', 'W'), (62, 'Z', 'N'), (63, 'W', 'E'), (64, 'N', 'Z'), (65, 'E', 'Z'), (66, 'E', 'W'), (67, 'Z', 'N'),
  (68, 'E', 'W'), (69, 'N', 'Z'), (70, 'W', 'E'), (71, 'Z', 'E'), (72, 'N', 'Z'), (73, 'E', 'W'), (74, 'W', 'E'),
  (75, 'W', 'E'), (76, 'Z', 'N'), (78, 'E', 'W'), (79, 'N', 'Z'), (81, 'E', 'W'), (82, 'W', 'E'), (83, 'N', 'E'),
  (84, 'E', 'W'), (85, 'Z', 'N'), (86, 'E', 'N'), (87, 'Z', 'N'), (88, 'E', 'N'), (89, 'W', 'E'), (90, 'N', 'Z'),
  (91, 'E', 'W'), (92, 'Z', 'N'), (93, 'Z', 'N'), (94, 'E', 'W'), (95, 'N', 'E'), (96, 'W', 'E'), (97, 'N', 'Z'),
  (98, 'W', 'E'), (99, 'Z', 'W'), (100, 'N', 'E');
/*!40000 ALTER TABLE `link_robot`
  ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `point`
--

DROP TABLE IF EXISTS `point`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `point` (
  `pid`    BIGINT(20) NOT NULL AUTO_INCREMENT,
  `type`   VARCHAR(255)        DEFAULT NULL,
  `access` VARCHAR(255)        DEFAULT NULL,
  `xcoord` BIGINT(20)          DEFAULT NULL,
  `ycoord` BIGINT(20)          DEFAULT NULL,
  `hub`    BIGINT(20)          DEFAULT NULL,
  PRIMARY KEY (`pid`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 50
  DEFAULT CHARSET = utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `point`
--

LOCK TABLES `point` WRITE;
/*!40000 ALTER TABLE `point`
  DISABLE KEYS */;
INSERT INTO `point` VALUES
  -- Points for robots
  (1, 'INTERSECTION', 'robot', 5, 20, 0),
  (2, 'INTERSECTION', 'robot', 10, 20, 0),
  (3, 'INTERSECTION', 'robot', 10, 15, 0),
  (4, 'ENDPOINT', 'robot', 10, 10, 6),
  (5, 'INTERSECTION','robot', 10, 35, 0),
  (6, 'ENDPOINT', 'robot', 10, 40, 1),
  (7, 'LIGHT','robot', 15, 15, 0),
  (8, 'INTERSECTION', 'robot', 20, 20, 0),
  (9, 'INTERSECTION', 'robot', 20, 25, 0),
  (10, 'ENDPOINT', 'robot', 20, 35, 2),
  (11, 'ENDPOINT', 'robot', 30, 35, 3),
  (12, 'LIGHT', 'robot', 25, 30, 0),
  (13, 'INTERSECTION', 'robot', 30, 30, 0),
  (14, 'INTERSECTION', 'robot', 30, 25, 0),
  (15, 'INTERSECTION', 'robot', 35, 25, 0),
  (16, 'INTERSECTION', 'robot', 30, 20, 0),
  (17, 'INTERSECTION', 'robot', 35, 20, 0),
  (18, 'ENDPOINT', 'robot', 20, 10, 5),
  (19, 'INTERSECTION', 'robot', 30, 10, 0),
  (20, 'ENDPOINT', 'robot', 30, 5, 4),
  (21, 'ENDPOINT', 'robot', 90, 40, 9),
  (22, 'INTERSECTION', 'robot', 95, 40, 0),
  (23, 'ENDPOINT', 'robot', 95, 50, 8),
  (24, 'INTERSECTION','robot', 105, 40, 0),
  (25, 'INTERSECTION', 'robot', 105, 45, 0),
  (26, 'INTERSECTION', 'robot', 110, 40, 0),
  (27, 'INTERSECTION', 'robot', 110, 45, 0),
  (28, 'INTERSECTION', 'robot', 115, 45, 0),
  (29, 'ENDPOINT', 'robot', 125, 45, 10),
  (30, 'INTERSECTION', 'robot', 110, 55, 0),
  (31, 'INTERSECTION','robot', 105, 55, 0),
  (32, 'ENDPOINT', 'robot', 95, 65, 7),
  (33, 'INTERSECTION','robot', 100, 65, 0),
  (34, 'INTERSECTION', 'robot', 105, 70, 0),
  (35, 'INTERSECTION', 'robot', 105, 65, 0),
  (36, 'INTERSECTION', 'robot', 125, 65, 0),
  (37, 'ENDPOINT', 'robot', 125, 55, 11),
  (38, 'ENDPOINT', 'robot', 130, 65, 12),
  (39, 'LIGHT', 'robot', 100, 60, 0),
  (40, 'LIGHT', 'robot', 115, 50, 0),

  -- Points for drone
  (41, 'ENDPOINT', 'drone', 8, 42, 1),
  (42, 'ENDPOINT', 'drone', 18, 37, 2),
  (43, 'ENDPOINT', 'drone', 33, 37, 3),
  (44, 'ENDPOINT', 'drone', 93, 67, 7),
  (45, 'ENDPOINT', 'drone', 88, 47, 9),

  -- Points for car
  (46, 'ENDPOINT', 'car', 93, 63, 7),
  (47, 'ENDPOINT', 'car', 33, 42, 3),
  (48, 'ENDPOINT', 'car', 28, 3, 4),
  (49, 'ENDPOINT','car', 127, 43, 10);
/*!40000 ALTER TABLE `point`
  ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `point_car`
--

DROP TABLE IF EXISTS `point_car`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `point_car` (
  `pid` BIGINT(11) NOT NULL,
  `x`   FLOAT DEFAULT NULL,
  `y`   FLOAT DEFAULT NULL,
  `z`   FLOAT DEFAULT NULL,
  `w`   FLOAT DEFAULT NULL,
  KEY `pid` (`pid`),
  CONSTRAINT `point_car_ibfk_1` FOREIGN KEY (`pid`) REFERENCES `point` (`pid`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `point_car`
--
-- Should be used from VmapCar/ZmapCar

--
-- Table structure for table `point_drone`
--

DROP TABLE IF EXISTS `point_drone`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `point_drone` (
  `x`   FLOAT DEFAULT NULL,
  `y`   FLOAT DEFAULT NULL,
  `z`   FLOAT DEFAULT NULL,
  `pid` BIGINT(20) NOT NULL,
  PRIMARY KEY (`pid`),
  CONSTRAINT `FK_f3hwcki931hpvl65ffcjkampw` FOREIGN KEY (`pid`) REFERENCES `point` (`pid`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `point_drone`
--

LOCK TABLES `point_drone` WRITE;
/*!40000 ALTER TABLE `point_drone`
  DISABLE KEYS */;
INSERT INTO `point_drone`
VALUES
  (0, 0, 0, 41),
  (-17.0104, -18.4051, 0.000049199, 42),
  (-21.9166, -4.54533, 0.0000392945, 43),
  (17.1217, 14.9715, 0.0000405321, 44),
  (22.2837, 7.52421, 0.0000433797, 45);
/*!40000 ALTER TABLE `point_drone`
  ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `point_robot`
--

DROP TABLE IF EXISTS `point_robot`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `point_robot` (
  `pid`       BIGINT(20) NOT NULL,
  `rfid`      VARCHAR(255) DEFAULT NULL,
  `pointlock` INT(11)      DEFAULT NULL,
  KEY `pid` (`pid`),
  CONSTRAINT `point_robot_ibfk_1` FOREIGN KEY (`pid`) REFERENCES `point` (`pid`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `point_robot`
--

LOCK TABLES `point_robot` WRITE;
/*!40000 ALTER TABLE `point_robot`
  DISABLE KEYS */;
INSERT INTO `point_robot`
VALUES
  -- for real map
  (1, '04 97 36 A2 7F 22 80', 0),
  (2, '04 41 70 92 1E 25 80', 0),
  (3, '04 70 39 32 06 27 80', 0),
  (4, '04 26 3E 92 1E 25 80', 0),
  (5, '04 3C 67 9A F6 1F 80', 0),
  (6, '04 18 25 9A 7F 22 80', 0),
  (7, '04 C5 88 8A C8 48 80', 0),
  (8, '04 B3 88 8A C8 48 80', 0),
  (9, '04 8D 88 8A C8 48 80', 0),
  (10, '04 EC 88 8A C8 48 80', 0),
  (11, '04 AA 88 8A C8 48 80', 0),
  (12, '04 DA 88 8A C8 48 80', 0),
  (13, '04 E3 88 8A C8 48 80', 0),
  (14, '04 67 88 8A C8 48 80', 0),
  (15, '04 7B 88 8A C8 48 80', 0),
  (16, '04 BC 88 8A C8 48 80', 0),
  (17, '04 96 88 8A C8 48 80', 0),
  (18, '04 A1 88 8A C8 48 80', 0),
  (19, '04 C4 FD 12 A9 34 80', 0),
  (20, '04 86 04 22 A9 34 84', 0),

  -- for virtual map
  (21, 'a', 0),
  (22, 'b', 0),
  (23, 'c', 0),
  (24, 'd', 0),
  (25, 'e', 0),
  (26, 'f', 0),
  (27, 'g', 0),
  (28, 'h', 0),
  (29, 'i', 0),
  (30, 'j', 0),
  (31, 'k', 0),
  (32, 'l', 0),
  (33, 'm', 0),
  (34, 'n', 0),
  (35, 'o', 0),
  (36, 'p', 0),
  (37, 'q', 0),
  (38, 'r', 0),
  (39, 's', 0),
  (40, 't', 0);
/*!40000 ALTER TABLE `point_robot`
  ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `trafficlight`
--

DROP TABLE IF EXISTS `trafficlight`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `trafficlight` (
  `tlid`      BIGINT(20) NOT NULL AUTO_INCREMENT,
  `direction` VARCHAR(255)        DEFAULT NULL,
  `point_id`  BIGINT(20)          DEFAULT NULL,
  `state`     VARCHAR(255)        DEFAULT NULL,
  PRIMARY KEY (`tlid`),
  KEY `fk_pointid` (`point_id`),
  CONSTRAINT `FK_36q0ntiwsex3ooj744c0t9py1` FOREIGN KEY (`point_id`) REFERENCES `point` (`pid`),
  CONSTRAINT `fk_pointid` FOREIGN KEY (`point_id`) REFERENCES `point` (`pid`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `trafficlight`
--

LOCK TABLES `trafficlight` WRITE;
/*!40000 ALTER TABLE `trafficlight`
  DISABLE KEYS */;
/*!40000 ALTER TABLE `trafficlight`
  ENABLE KEYS */;
UNLOCK TABLES;