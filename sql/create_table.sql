
create database moviedb;
use moviedb;

CREATE TABLE `creditcards` (
  `id` varchar(20) NOT NULL,
  `firstName` varchar(50) NOT NULL,
  `lastName` varchar(50) NOT NULL,
  `expiration` date NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `movies` (
  `id` varchar(10) NOT NULL,
  `title` varchar(100) NOT NULL,
  `year` int(11) NOT NULL,
  `director` varchar(100) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `stars` (
  `id` varchar(10) NOT NULL,
  `name` varchar(100) NOT NULL,
  `birthYear` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `stars_in_movies` (
  `starId` varchar(10) NOT NULL,
  `movieId` varchar(10) NOT NULL,
  KEY `1_idx` (`starId`),
  KEY `2_idx` (`movieId`),
  CONSTRAINT `1` FOREIGN KEY (`starId`) REFERENCES `stars` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `2` FOREIGN KEY (`movieId`) REFERENCES `movies` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `genres` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(32) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=utf8;

CREATE TABLE `genres_in_movies` (
  `genreId` int(11) NOT NULL,
  `movieId` varchar(10) NOT NULL,
  KEY `3_idx` (`genreId`),
  KEY `4_idx` (`movieId`),
  CONSTRAINT `3` FOREIGN KEY (`genreId`) REFERENCES `genres` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `4` FOREIGN KEY (`movieId`) REFERENCES `movies` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `customers` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `firstName` varchar(50) NOT NULL,
  `lastName` varchar(50) NOT NULL,
  `ccId` varchar(20) NOT NULL,
  `address` varchar(200) NOT NULL,
  `email` varchar(50) NOT NULL,
  `password` varchar(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `7_idx` (`ccId`),
  CONSTRAINT `7` FOREIGN KEY (`ccId`) REFERENCES `creditcards` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=973021 DEFAULT CHARSET=utf8;

CREATE TABLE `sales` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `customerId` int(11) NOT NULL,
  `movieId` varchar(10) NOT NULL,
  `salesDate` date NOT NULL,
  PRIMARY KEY (`id`),
  KEY `5_idx` (`customerId`),
  KEY `6_idx` (`movieId`),
  CONSTRAINT `5` FOREIGN KEY (`customerId`) REFERENCES `customers` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `6` FOREIGN KEY (`movieId`) REFERENCES `movies` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=13561 DEFAULT CHARSET=utf8;

CREATE TABLE `ratings` (
  `movieId` varchar(10) NOT NULL,
  `rating` float NOT NULL,
  `numVotes` int(11) NOT NULL,
  KEY `8_idx` (`movieId`),
  CONSTRAINT `8` FOREIGN KEY (`movieId`) REFERENCES `movies` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;













