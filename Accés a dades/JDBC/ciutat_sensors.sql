-- =====================================================================
-- Projecte BA1 - JDBC : SABADELL SMART CITY
-- Script de creació de la base de dades ciutat_sensors (MySQL / MariaDB)
-- =====================================================================

DROP DATABASE IF EXISTS ciutat_sensors;
CREATE DATABASE ciutat_sensors CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE ciutat_sensors;

-- ---------------------------------------------------------------------
-- ZONES: barris de la ciutat on hi ha desplegats sensors
-- ---------------------------------------------------------------------
CREATE TABLE zones (
    id          INT AUTO_INCREMENT PRIMARY KEY,
    nom         VARCHAR(100) NOT NULL,
    descripcio  VARCHAR(255),
    poblacio    INT
);

-- ---------------------------------------------------------------------
-- TIPUS_SENSORS: categories de sensor
-- ---------------------------------------------------------------------
CREATE TABLE tipus_sensors (
    id              INT AUTO_INCREMENT PRIMARY KEY,
    nom             VARCHAR(50) NOT NULL,
    unitat_mesura   VARCHAR(20) NOT NULL,
    descripcio      VARCHAR(255)
);

-- ---------------------------------------------------------------------
-- SENSORS: dispositius instal·lats, amb posició en una quadrícula local
-- (pos_x, pos_y en metres respecte l'origen de la ciutat; altura en metres)
-- ---------------------------------------------------------------------
CREATE TABLE sensors (
    id                  INT AUTO_INCREMENT PRIMARY KEY,
    nom                 VARCHAR(100) NOT NULL,
    zona_id             INT NOT NULL,
    tipus_sensor_id     INT NOT NULL,
    pos_x               DECIMAL(10,2) NOT NULL,
    pos_y               DECIMAL(10,2) NOT NULL,
    altura              DECIMAL(6,2) NOT NULL DEFAULT 3.00,
    orientacio          INT NOT NULL DEFAULT 0,          -- graus (0-359)
    actiu               BOOLEAN NOT NULL DEFAULT TRUE,
    data_instalacio     DATE NOT NULL,
    FOREIGN KEY (zona_id) REFERENCES zones(id),
    FOREIGN KEY (tipus_sensor_id) REFERENCES tipus_sensors(id)
);

-- ---------------------------------------------------------------------
-- LECTURES: mesures enregistrades pels sensors
-- ---------------------------------------------------------------------
CREATE TABLE lectures (
    id              INT AUTO_INCREMENT PRIMARY KEY,
    sensor_id       INT NOT NULL,
    valor           DECIMAL(10,2) NOT NULL,
    data_lectura    DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    qualitat        ENUM('OK', 'DUBTOSA', 'ERROR') NOT NULL DEFAULT 'OK',
    FOREIGN KEY (sensor_id) REFERENCES sensors(id) ON DELETE CASCADE
);

-- =====================================================================
-- DADES
-- =====================================================================

INSERT INTO zones (nom, descripcio, poblacio) VALUES
('Centre',        'Nucli antic i zona comercial',            14200),
('Eixample',      'Barri residencial dens',                   26500),
('Creu Alta',     'Barri al nord del centre',                 18300),
('Gràcia',        'Zona residencial amb parcs',               21800),
('Can Feu',       'Barri amb zona industrial',                12700),
('Covadonga',     'Barri de tradició obrera',                 16400),
('Ca n''Oriac',   'Barri del nord amb gran activitat',        23100),
('La Serra',      'Zona propera al parc natural',              7900),
('Torre-romeu',   'Barri a la riba del riu Ripoll',           11500),
('Merinals',      'Zona propera a la Ronda Oest',              9800);

INSERT INTO tipus_sensors (nom, unitat_mesura, descripcio) VALUES
('Temperatura',   '°C',      'Temperatura ambient'),
('Humitat',       '%',       'Humitat relativa de l''aire'),
('Soroll',        'dB',      'Nivell de soroll ambiental'),
('CO2',           'ppm',     'Concentració de diòxid de carboni'),
('PM2.5',         'µg/m³',   'Partícules en suspensió fines'),
('Trànsit',       'veh/min', 'Comptador de vehicles'),
('Lluminositat',  'lux',     'Nivell de llum ambient'),
('Aparcament',    'places',  'Places lliures en un aparcament'),
('Pluviòmetre',   'mm',      'Precipitació acumulada'),
('Vent',          'km/h',    'Velocitat del vent');

INSERT INTO sensors (nom, zona_id, tipus_sensor_id, pos_x, pos_y, altura, orientacio, actiu, data_instalacio) VALUES
('TEMP-CENTRE-01',   1, 1,  120.50,  340.00, 4.00,   0, TRUE,  '2024-03-12'),
('HUM-CENTRE-01',    1, 2,  122.00,  342.50, 4.00,   0, TRUE,  '2024-03-12'),
('SOROLL-CENTRE-01', 1, 3,  210.00,  310.00, 5.50,  90, TRUE,  '2024-05-20'),
('TRANSIT-EIX-01',   2, 6,  880.00,  455.00, 6.00, 180, TRUE,  '2023-11-02'),
('CO2-EIX-01',       2, 4,  902.25,  470.00, 3.00,   0, FALSE, '2023-11-02'),
('PM25-EIX-01',      2, 5,  905.00,  472.00, 3.00,   0, TRUE,  '2024-01-15'),
('LLUM-GRACIA-01',   4, 7,  650.00, 1200.00, 8.00, 270, TRUE,  '2024-06-30'),
('APARC-GRACIA-01',  4, 8,  700.00, 1150.00, 2.50,   0, TRUE,  '2024-06-30'),
('PLUJA-SERRA-01',   8, 9, 1500.00, 2300.00, 2.00,   0, TRUE,  '2022-09-10'),
('VENT-SERRA-01',    8, 10, 1520.00, 2310.00, 10.00, 45, FALSE, '2022-09-10'),
('TEMP-CANFEU-01',   5, 1,  300.00,  900.00, 4.00,   0, TRUE,  '2024-02-01'),
('TRANSIT-CANFEU-01',5, 6,  320.00,  950.00, 6.00,  90, TRUE,  '2024-02-01'),
('CO2-CANFEU-01',     5, 4,  325.00,  955.00, 3.00,   0, TRUE,  '2026-08-28'),   -- actiu i sense cap lectura
('SOROLL-COVA-01',    6, 3,  410.00, 1600.00, 5.50, 180, TRUE,  '2025-10-01');   -- actiu, només lectures antigues

INSERT INTO lectures (sensor_id, valor, data_lectura, qualitat) VALUES
( 1, 21.40, '2026-09-08 08:00:00', 'OK'),
( 1, 24.10, '2026-09-08 12:00:00', 'OK'),
( 1, 19.80, '2026-09-08 20:00:00', 'DUBTOSA'),
( 2, 61.00, '2026-09-08 08:00:00', 'OK'),
( 2, 48.50, '2026-09-08 12:00:00', 'OK'),
( 3, 58.30, '2026-09-08 09:00:00', 'OK'),
( 3, 74.90, '2026-09-08 18:00:00', 'OK'),
( 3, 99.00, '2026-09-08 23:00:00', 'ERROR'),
( 4, 32.00, '2026-09-08 08:30:00', 'OK'),
( 4, 41.00, '2026-09-08 18:30:00', 'OK'),
( 5, 780.00, '2026-09-08 08:00:00', 'OK'),
( 6, 14.20, '2026-09-08 08:00:00', 'OK'),
( 6, 22.70, '2026-09-08 18:00:00', 'DUBTOSA'),
( 7, 12000.00, '2026-09-08 13:00:00', 'OK'),
( 8, 37.00, '2026-09-08 09:00:00', 'OK'),
( 8, 4.00, '2026-09-08 19:00:00', 'OK'),
(11, 20.90, '2026-09-08 08:00:00', 'OK'),
(12, 55.00, '2026-09-08 08:30:00', 'OK'),
(12, 12.00, '2026-09-08 22:30:00', 'OK'),
-- Lectures antigues (2025): serveixen per provar desactivaSensorsSilenciosos i netejaLecturesError
( 9, 12.50, '2025-10-14 08:00:00', 'OK'),
( 9,  3.20, '2025-10-14 20:00:00', 'OK'),
( 9, 99.00, '2025-12-03 04:00:00', 'ERROR'),
(14, 62.10, '2025-11-05 09:00:00', 'OK'),
(14, 71.40, '2025-11-05 22:00:00', 'OK'),
(14, 130.00, '2025-12-03 03:00:00', 'ERROR');
