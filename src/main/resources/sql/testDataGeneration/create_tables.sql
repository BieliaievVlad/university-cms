CREATE TABLE IF NOT EXISTS university.randomNames (
	id SERIAL PRIMARY KEY,
	first_name VARCHAR(50) NOT NULL,
	last_name VARCHAR(50) NOT NULL
	);
INSERT INTO university.randomNames (id, first_name, last_name)
VALUES
(1,'Vladimir', 'Lem'),
(2,'Max', 'Payne'),
(3,'Mona', 'Sax'),
(4,'Jack', 'Lupino'),
(5,'Nicole', 'Horne'),
(6,'Vincent', 'Vega'),
(7,'Jules', 'Winnfield'),
(8,'Mia', 'Wallace'),
(9,'Esmeralda', 'Villalobos'),
(10,'Leon', 'Kennedy'),
(11,'Christofer', 'Redfield'),
(12,'Ada', 'Wong'),
(13,'Albert', 'Wesker'),
(14,'Sherry', 'Birkin'),
(15,'Isaac', 'Clarke'),
(16,'Nolan', 'Stross'),
(17,'Ellie', 'Langford'),
(18,'Johnatan', 'Carver'),
(19,'Peter', 'Parker'),
(20,'Soap', 'MacTavish');