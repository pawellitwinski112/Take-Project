--==================
--  Przykladowe dane
--==================

-- Dodajemy lotniska (tabela: airports)
INSERT INTO airports (icao_code, airport_name, country, city) VALUES ('EPWA', 'Lotnisko Chopina', 'Polska', 'Warszawa');
INSERT INTO airports (icao_code, airport_name, country, city) VALUES ('EPKK', 'Balice', 'Polska', 'Krakow');
INSERT INTO airports (icao_code, airport_name, country, city) VALUES ('EPGD', 'Lotnisko im. Lecha Walesy', 'Polska', 'Gdansk');
INSERT INTO airports (icao_code, airport_name, country, city) VALUES ('EDDF', 'Frankfurt Airport', 'Niemcy', 'Frankfurt');
INSERT INTO airports (icao_code, airport_name, country, city) VALUES ('LFPG', 'Charles de Gaulle', 'Francja', 'Paryz');
INSERT INTO airports (icao_code, airport_name, country, city) VALUES ('EGLL', 'Heathrow', 'Wielka Brytania', 'Londyn');
INSERT INTO airports (icao_code, airport_name, country, city) VALUES ('KDCA', 'Washington DC', 'Stany Zjednoczone', 'Waszyngton');
INSERT INTO airports (icao_code, airport_name, country, city) VALUES ('LIRF', 'Fiumicino - Leonardo da Vinci', 'Włochy', 'Rzym');
INSERT INTO airports (icao_code, airport_name, country, city) VALUES ('LEMD', 'Barajas', 'Hiszpania', 'Madryt');
INSERT INTO airports (icao_code, airport_name, country, city) VALUES ('EFHK', 'Vantaa', 'Finlandia', 'Helsinki');
INSERT INTO airports (icao_code, airport_name, country, city) VALUES ('OMDB', 'Dubai International', 'Zjednoczone Emiraty Arabskie', 'Dubaj');

-- Dodajemy linie lotnicze (tabela: airline)
INSERT INTO airline (name, icao_name, helpdesk_number) VALUES ('LOT Polish Airlines', 'LOT', 123456789);
INSERT INTO airline (name, icao_name, helpdesk_number) VALUES ('Ryanair', 'RYR', 987654321);
INSERT INTO airline (name, icao_name, helpdesk_number) VALUES ('Wizz Air', 'WZZ', 800123456);
INSERT INTO airline (name, icao_name, helpdesk_number) VALUES ('Lufthansa', 'DLH', 496928600);
INSERT INTO airline (name, icao_name, helpdesk_number) VALUES ('British Airways', 'BAW', 864839801);
INSERT INTO airline (name, icao_name, helpdesk_number) VALUES ('American Airlines', 'AAL', 846762001);
INSERT INTO airline (name, icao_name, helpdesk_number) VALUES ('Air France', 'AFR', 794420912);
INSERT INTO airline (name, icao_name, helpdesk_number) VALUES ('Finnair', 'FIN', 940905231);
INSERT INTO airline (name, icao_name, helpdesk_number) VALUES ('Emirates', 'UAE', 884932047);
INSERT INTO airline (name, icao_name, helpdesk_number) VALUES ('Qatar Airways', 'QTR', 173889194);

-- Dodajemy samoloty (tabela: airplanes)
INSERT INTO airplanes (seats, producent, model, max_range, production_date, registration) 
VALUES (189, 'Boeing', '737-800', 5765.0, '2015-06-01T12:00:00', 'SP-LWA');
INSERT INTO airplanes (seats, producent, model, max_range, production_date, registration) 
VALUES (180, 'Airbus', 'A320', 6100.0, '2018-03-15T09:00:00', 'SP-ENA');
INSERT INTO airplanes (seats, producent, model, max_range, production_date, registration) 
VALUES (118, 'Embraer', 'E195', 4360.0, '2019-11-20T10:30:00', 'SP-LNA');
INSERT INTO airplanes (seats, producent, model, max_range, production_date, registration) 
VALUES (102, 'Embraer', 'E190', 4020.0, '2016-12-16T16:50:00', 'SP-TUL');
INSERT INTO airplanes (seats, producent, model, max_range, production_date, registration) 
VALUES (89, 'Embraer', 'E175', 3480.0, '2014-03-09T06:30:00', 'SP-TRJ');
INSERT INTO airplanes (seats, producent, model, max_range, production_date, registration) 
VALUES (290, 'Boeing', '787-9', 14180.0, '2021-02-10T08:00:00', 'SP-LSA');
INSERT INTO airplanes (seats, producent, model, max_range, production_date, registration) 
VALUES (310, 'Boeing', '747', 16140.0, '2012-07-10T03:00:00', 'SP-JJT');
INSERT INTO airplanes (seats, producent, model, max_range, production_date, registration) 
VALUES (276, 'Boeing', '777X', 11120.0, '2021-01-20T08:00:00', 'SP-TBY');
INSERT INTO airplanes (seats, producent, model, max_range, production_date, registration) 
VALUES (267, 'Boeing', '737 MAX', 9200.0, '2019-05-17T17:00:00', 'SP-XHY');
INSERT INTO airplanes (seats, producent, model, max_range, production_date, registration) 
VALUES (220, 'Airbus', 'A321neo', 7400.0, '2022-07-05T14:00:00', 'SP-PTA');
INSERT INTO airplanes (seats, producent, model, max_range, production_date, registration) 
VALUES (270, 'Airbus', 'A330', 8500.0, '2021-04-19T11:00:00', 'SP-ARA');
INSERT INTO airplanes (seats, producent, model, max_range, production_date, registration) 
VALUES (300, 'Airbus', 'A350', 15320.0, '2018-11-28T22:00:00', 'SP-GBT');

-- Dodajemy loty (tabela: flights)
INSERT INTO flights (airplane, dep_airport, arr_airport, dep_time, arr_time, flight_lane)
VALUES (1, 1, 4, '2026-07-01T08:00:00', '2026-07-01T09:30:00', 1);
INSERT INTO flights (airplane, dep_airport, arr_airport, dep_time, arr_time, flight_lane)
VALUES (2, 2, 6, '2026-07-01T10:15:00', '2026-07-01T12:45:00', 2);
INSERT INTO flights (airplane, dep_airport, arr_airport, dep_time, arr_time, flight_lane)
VALUES (3, 1, 2, '2026-07-02T06:30:00', '2026-07-02T07:35:00', 1);
INSERT INTO flights (airplane, dep_airport, arr_airport, dep_time, arr_time, flight_lane)
VALUES (4, 4, 5, '2026-07-02T13:00:00', '2026-07-02T14:20:00', 4);
INSERT INTO flights (airplane, dep_airport, arr_airport, dep_time, arr_time, flight_lane)
VALUES (5, 3, 1, '2026-07-03T07:00:00', '2026-07-03T07:55:00', 3);
INSERT INTO flights (airplane, dep_airport, arr_airport, dep_time, arr_time, flight_lane)
VALUES (1, 6, 1, '2026-07-03T16:00:00', '2026-07-03T18:30:00', 1);
INSERT INTO flights (airplane, dep_airport, arr_airport, dep_time, arr_time, flight_lane)
VALUES (8, 8, 4, '2026-08-03T16:00:00', '2026-08-03T18:30:00', 8);
INSERT INTO flights (airplane, dep_airport, arr_airport, dep_time, arr_time, flight_lane)
VALUES (9, 9, 6, '2026-09-03T16:00:00', '2026-09-03T18:30:00', 9);
INSERT INTO flights (airplane, dep_airport, arr_airport, dep_time, arr_time, flight_lane)
VALUES (5, 7, 5, '2026-09-12T16:00:00', '2026-09-12T19:30:00', 6);
INSERT INTO flights (airplane, dep_airport, arr_airport, dep_time, arr_time, flight_lane)
VALUES (7, 5, 9, '2026-09-19T09:00:00', '2026-09-19T14:30:00', 7);
INSERT INTO flights (airplane, dep_airport, arr_airport, dep_time, arr_time, flight_lane)
VALUES (6, 1, 11, '2026-10-01T22:00:00', '2026-10-02T07:30:00', 9);
INSERT INTO flights (airplane, dep_airport, arr_airport, dep_time, arr_time, flight_lane)
VALUES (10, 2, 8, '2026-10-05T11:00:00', '2026-10-05T13:10:00', 1);
INSERT INTO flights (airplane, dep_airport, arr_airport, dep_time, arr_time, flight_lane)
VALUES (11, 4, 11, '2026-10-08T20:00:00', '2026-10-09T05:45:00', 4);
INSERT INTO flights (airplane, dep_airport, arr_airport, dep_time, arr_time, flight_lane)
VALUES (3, 10, 1, '2026-10-10T09:00:00', '2026-10-10T10:35:00', 8);
INSERT INTO flights (airplane, dep_airport, arr_airport, dep_time, arr_time, flight_lane)
VALUES (12, 9, 6, '2026-10-15T14:00:00', '2026-10-15T16:20:00', 5);

-- Dodajemy pasazerow (tabela: passengers)
INSERT INTO passengers (name, surname, mail, phone_num) VALUES ('Jan', 'Kowalski', 'jan.kowalski@example.com', 600100200);
INSERT INTO passengers (name, surname, mail, phone_num) VALUES ('Anna', 'Nowak', 'anna.nowak@example.com', 600200300);
INSERT INTO passengers (name, surname, mail, phone_num) VALUES ('Piotr', 'Wisniewski', 'piotr.wisniewski@example.com', 600300400);
INSERT INTO passengers (name, surname, mail, phone_num) VALUES ('Jakub', 'Tywonek', 'jt311437@student.polsl.pl', 749263749);
INSERT INTO passengers (name, surname, mail, phone_num) VALUES ('Pawel', 'Litwinski', 'pl311395@student.polsl.pl', 372839021);
INSERT INTO passengers (name, surname, mail, phone_num) VALUES ('Dominika', 'Mackowiak', 'dm311397@student.polsl.pl', 738909631);
INSERT INTO passengers (name, surname, mail, phone_num) VALUES ('Maria', 'Wojcik', 'maria.wojcik@example.com', 600400500);
INSERT INTO passengers (name, surname, mail, phone_num) VALUES ('Tomasz', 'Kaminski', 'tomasz.kaminski@example.com', 600500600);
INSERT INTO passengers (name, surname, mail, phone_num) VALUES ('Katarzyna', 'Lewandowska', 'katarzyna.lewandowska@example.com', 600600700);
INSERT INTO passengers (name, surname, mail, phone_num) VALUES ('Magdalena', 'Zielinska', 'magdalena.zielinska@example.com', 600700800);
INSERT INTO passengers (name, surname, mail, phone_num) VALUES ('Krzysztof', 'Szymanski', 'krzysztof.szymanski@example.com', 600800900);
INSERT INTO passengers (name, surname, mail, phone_num) VALUES ('Aleksandra', 'Dabrowska', 'aleksandra.dabrowska@example.com', 600900100);
INSERT INTO passengers (name, surname, mail, phone_num) VALUES ('Michal', 'Kowalczyk', 'michal.kowalczyk@example.com', 601000200);
INSERT INTO passengers (name, surname, mail, phone_num) VALUES ('Natalia', 'Krawczyk', 'natalia.krawczyk@example.com', 601100300);
INSERT INTO passengers (name, surname, mail, phone_num) VALUES ('Wojciech', 'Piotrowski', 'wojciech.piotrowski@example.com', 601200400);

-- Dodajemy karty pokladowe (tabela: boarding_pass)
INSERT INTO boarding_pass (flight, passenger, class, seat) VALUES (1, 1, 'Economy', 12);
INSERT INTO boarding_pass (flight, passenger, class, seat) VALUES (1, 2, 'Business', 2);
INSERT INTO boarding_pass (flight, passenger, class, seat) VALUES (2, 3, 'Economy', 15);
INSERT INTO boarding_pass (flight, passenger, class, seat) VALUES (3, 4, 'Economy', 7);
INSERT INTO boarding_pass (flight, passenger, class, seat) VALUES (4, 5, 'First', 1);
INSERT INTO boarding_pass (flight, passenger, class, seat) VALUES (4, 6, 'Economy', 22);
INSERT INTO boarding_pass (flight, passenger, class, seat) VALUES (5, 1, 'Economy', 9);
INSERT INTO boarding_pass (flight, passenger, class, seat) VALUES (6, 3, 'Business', 3);
INSERT INTO boarding_pass (flight, passenger, class, seat) VALUES (7, 7, 'Business', 53);
INSERT INTO boarding_pass (flight, passenger, class, seat) VALUES (7, 3, 'Business', 51);
INSERT INTO boarding_pass (flight, passenger, class, seat) VALUES (7, 2, 'Business', 32);
INSERT INTO boarding_pass (flight, passenger, class, seat) VALUES (8, 3, 'Economy', 53);
INSERT INTO boarding_pass (flight, passenger, class, seat) VALUES (8, 7, 'Economy', 51);
INSERT INTO boarding_pass (flight, passenger, class, seat) VALUES (8, 8, 'Business', 52);
INSERT INTO boarding_pass (flight, passenger, class, seat) VALUES (9, 4, 'Business', 12);
INSERT INTO boarding_pass (flight, passenger, class, seat) VALUES (9, 6, 'Economy', 71);
INSERT INTO boarding_pass (flight, passenger, class, seat) VALUES (9, 3, 'Business', 42);
INSERT INTO boarding_pass (flight, passenger, class, seat) VALUES (11, 10, 'Business', 4);
INSERT INTO boarding_pass (flight, passenger, class, seat) VALUES (11, 11, 'Economy', 45);
INSERT INTO boarding_pass (flight, passenger, class, seat) VALUES (11, 9, 'First', 1);
INSERT INTO boarding_pass (flight, passenger, class, seat) VALUES (12, 12, 'Economy', 18);
INSERT INTO boarding_pass (flight, passenger, class, seat) VALUES (12, 2, 'Economy', 19);
INSERT INTO boarding_pass (flight, passenger, class, seat) VALUES (13, 13, 'Business', 6);
INSERT INTO boarding_pass (flight, passenger, class, seat) VALUES (13, 14, 'Economy', 33);
INSERT INTO boarding_pass (flight, passenger, class, seat) VALUES (14, 15, 'Economy', 10);
INSERT INTO boarding_pass (flight, passenger, class, seat) VALUES (14, 5, 'Economy', 11);
INSERT INTO boarding_pass (flight, passenger, class, seat) VALUES (15, 10, 'Economy', 27);
INSERT INTO boarding_pass (flight, passenger, class, seat) VALUES (15, 4, 'Business', 3);
INSERT INTO boarding_pass (flight, passenger, class, seat) VALUES (10, 12, 'Economy', 60);