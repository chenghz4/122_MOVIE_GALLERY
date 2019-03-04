CREATE TABLE `employees` (
  `email` varchar(50) NOT NULL,
  `password` varchar(20) NOT NULL,
  `fullname` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO employees VALUES('classta@email.edu','classta','TA CS122B');