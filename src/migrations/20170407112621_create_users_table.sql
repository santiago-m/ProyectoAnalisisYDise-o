DROP TABLE IF EXISTS `users`;

CREATE TABLE users (
  id  int(11) auto_increment PRIMARY KEY,
  username  VARCHAR(128),
  password  VARCHAR(128),
  puntaje int(11),
  created_at DATETIME,
  updated_at DATETIME
)ENGINE=InnoDB;


DROP TABLE IF EXISTS `admins`;

CREATE TABLE admins (
  id  int(11) auto_increment PRIMARY KEY,
  username  VARCHAR(128),
  password  VARCHAR(128),
  puntaje int(11)
)ENGINE=InnoDB;

DROP TABLE IF EXISTS `questions`;

CREATE TABLE questions (
  id  int(11) auto_increment PRIMARY KEY,
  leido  BIT,
  creador  VARCHAR(128),
  pregunta  VARCHAR(128),
  respuestaCorrecta  VARCHAR(128),
  wrong1  VARCHAR(128),
  wrong2  VARCHAR(128),
  wrong3  VARCHAR(128),
  created_at DATETIME,
  updated_at DATETIME
)ENGINE=InnoDB;

INSERT INTO admins (id, username, password, puntaje) VALUES
 (1, 'sajoma', '13794682', 0),
 (2, 'matesuiso', 'asd', 0);

INSERT INTO questions 
 (id, leido, creador, pregunta, respuestaCorrecta, wrong1, wrong2, wrong3) VALUES
 (1, 0, 'sajoma', '¿A cuantos grados hierve el agua?', '100°C', '80°C', '120°C', '150°C'),
 (2, 0, 'sajoma', '¿un elefante recien nacido, pesa mas o menos de 500 kilos?', 'Menos de 500Kg', 'Mas de 500Kg', NULL, NULL),
 (3, 0, 'sajoma', '¿Que se mide con un nigrómetro?', 'La oscuridad del ambiente', 'La cantidad de pigmentos en la piel', 'La densidad de la tinta', NULL),
 (4, 0, 'sajoma', '¿Como se llaman las fotografias obtenidas con la luz de un rayo laser?', 'Holografias', 'Fotolitos', 'Laserografias', NULL),
  (5, 0, 'sajoma', 'Los crateres de la luna, ¿son volcanicos?', 'No', 'Si', NULL, NULL),
  (6, 0, 'sajoma', '¿Cuantas vueltas alrededor del sol da la luna en un año?', 'Una', 'Dos y media', 'Dos', 'Media');
