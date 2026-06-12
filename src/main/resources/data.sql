-- Dodajemy lotniska (tabela: airports)
INSERT INTO airports (icao_code, airport_name, country, city) VALUES ('EPWA', 'Lotnisko Chopina', 'Polska', 'Warszawa');
INSERT INTO airports (icao_code, airport_name, country, city) VALUES ('EPKK', 'Balice', 'Polska', 'Krakow');

-- Dodajemy linie lotnicze (tabela: airline)
INSERT INTO airline (name, icao_name, helpdesk_number) VALUES ('LOT Polish Airlines', 'LOT', 123456789);
INSERT INTO airline (name, icao_name, helpdesk_number) VALUES ('Ryanair', 'RYR', 987654321);

-- Dodajemy samoloty (tabela: airplanes)
INSERT INTO airplanes (seats, producent, model, max_range, production_date, registration) 
VALUES (189, 'Boeing', '737-800', 5765.0, '2015-06-01T12:00:00', 'SP-LWA');