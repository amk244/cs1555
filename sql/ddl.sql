--DATA DEFINITION LANGUAGE FILE

DROP TABLE IF EXISTS station CASCADE;
CREATE TABLE station(
	station_id INTEGER NOT NULL,
	start_time TIME NOT NULL,
	end_time TIME NOT NULL,
	street VARCHAR(100) NOT NULL,
	town VARCHAR(100) NOT NULL,
	postal_code VARCHAR(20) NOT NULL,
	CONSTRAINT station_pk
		PRIMARY KEY(station_id)
);

DROP TABLE IF EXISTS rail_line CASCADE;
CREATE TABLE rail_line
(
  rail_line_id INTEGER NOT NULL,
	speed_limit INTEGER NOT NULL,
	CONSTRAINT rail_line_pk
		PRIMARY KEY(rail_line_id)
);

DROP TABLE IF EXISTS route CASCADE;
CREATE TABLE route
(
  route_id INTEGER NOT NULL,
	CONSTRAINT route_pk
		PRIMARY KEY(route_id)
);

DROP TABLE IF EXISTS route_stations;
CREATE TABLE route_stations(
  route_id int references route,
	station_id int references station,
	primary key(station_id, route_id)
);

DROP TABLE IF EXISTS rail_line_stations;
CREATE TABLE rail_line_stations(
	rail_line_id int references rail_line,
	station_id int references station,
	distance_from_prev_station int,
	primary key(station_id, rail_line_id)
);

DROP TABLE IF EXISTS route_stops;
CREATE TABLE route_stops(
  route_id int references route,
	station_id int references station,
	primary key(station_id, route_id)
);

DROP TABLE IF EXISTS train CASCADE;
CREATE TABLE train
(
  train_id INTEGER NOT NULL,
	num_seats INTEGER NOT NULL,
	top_speed REAL NOT NULL,
	price_per_mile REAL NOT NULL,
	CONSTRAINT train_pk
		PRIMARY KEY(train_id)
);

DROP TABLE IF EXISTS schedule CASCADE;
CREATE TABLE schedule
(
	schedule_route_id INTEGER NOT NULL,
	day_of_week VARCHAR(100) NOT NULL,
	time_of_day TIME NOT NULL,
	schedule_train_id INTEGER NOT NULL,
	CONSTRAINT schedule_pk
		PRIMARY KEY(schedule_route_id, day_of_week, time_of_day),
    CONSTRAINT route_num_fk
        FOREIGN KEY(schedule_route_id) REFERENCES route(route_id),
    CONSTRAINT schedule_train_id_fk
        FOREIGN KEY(schedule_train_id) REFERENCES train(train_id)
);

DROP TABLE IF EXISTS customer CASCADE;
CREATE TABLE customer
(
  customer_id INTEGER NOT NULL,
	first_name VARCHAR(50),
	last_name VARCHAR(50),
	street VARCHAR(100),
	town VARCHAR(100),
	postal_code VARCHAR(20),
	email VARCHAR(100),
	telephone_number VARCHAR(20),
	CONSTRAINT customer_pk
		PRIMARY KEY(customer_id)
);