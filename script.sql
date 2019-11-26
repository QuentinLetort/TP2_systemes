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
);

CREATE TABLE  tp2_sys.Itineraire (
 id INT( 11 ) NOT NULL AUTO_INCREMENT ,
 id_conducteur INT( 11 ) NOT NULL ,
 date DATE NOT NULL ,
 heure TIME NOT NULL ,
 destination VARCHAR( 20 ) NOT NULL ,
 origine VARCHAR( 20 ) NOT NULL,
 nb_places INT( 1 ) NOT NULL ,
 prix FLOAT ( 8 ) NOT NULL ,
 taille_baggage ENUM('Petit','Moyen','Gros')  NOT NULL ,
 date_annonce DATETIME NOT NULL ,
 PRIMARY KEY ( id ),
 CONSTRAINT fk_id_conducteur
    FOREIGN KEY (id_conducteur)
    REFERENCES Utilisateur(id)
);

CREATE TABLE  tp2_sys.Reservation (
 id INT( 11 ) NOT NULL AUTO_INCREMENT ,
 id_client INT( 11 ) NOT NULL ,
 id_itineraire INT( 11 ) NOT NULL ,
 nb_sieges INT ( 1 ) NOT NULL ,
 date_reservation DATETIME NOT NULL ,
 PRIMARY KEY ( id ),
 CONSTRAINT fk_id_client
     FOREIGN KEY (id_client)
     REFERENCES Utilisateur(id) ,
  CONSTRAINT fk_id_itineraire
      FOREIGN KEY (id_itineraire)
      REFERENCES Itineraire(id)
);