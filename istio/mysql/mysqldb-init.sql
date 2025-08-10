# Initialize a mysql db with a 'test' db and be able test productpage with it.
# mysql -h 127.0.0.1 -p password < mysqldb-init.sql

CREATE DATABASE test;
USE test;

CREATE TABLE `ratings` (
  `ReviewID` INT NOT NULL,
  `Rating` INT,
  PRIMARY KEY (`ReviewID`)
);
INSERT INTO ratings (ReviewID, Rating) VALUES (1, 10);
INSERT INTO ratings (ReviewID, Rating) VALUES (2, 11);
