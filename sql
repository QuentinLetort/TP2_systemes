CREATE DATABASE tp2_sys DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;
CREATE TABLE  tp2_sys.Utilisateur (
 id INT( 11 ) NOT NULL AUTO_INCREMENT ,
 nom VARCHAR( 20 ) NOT NULL ,
 prenom VARCHAR( 20 ) NOT NULL ,
 sexe ENUM('h','f','o') NOT NULL ,
 pays VARCHAR( 20 ) NOT NULL ,
 adresse VARCHAR ( 100 ) NOT NULL ,
 email VARCHAR( 60 ) NOT NULL ,
 mot_de_passe CHAR( 56 ) NOT NULL ,
 date_inscription DATETIME NOT NULL ,
 PRIMARY KEY ( id ),
 UNIQUE ( email )
)
