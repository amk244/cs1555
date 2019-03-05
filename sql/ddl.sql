CREATE TABLE station(
	station_number INTEGER,
	hours_of_operation INTEGER,
	Address VARCHAR(100),
	CONSTRAINT station_pk
		PRIMARY KEY(station_number)
);

CREATE TABLE railline
(
	speed_limit INTEGER,
	distance_btwn_stations REAL,
	stations_on_railine VARCHAR(100),
	station_num_railline INTEGER,
	CONSTRAINT railline_pk
		PRIMARY KEY(stations_on_railine),
	CONSTRAINT railline_fk
		FOREIGN KEY(station_num_railline) REFERENCES station(station_number)
);

CREATE TABLE route
(
	path_of_route VARCHAR(100),
	route_station_num INTEGER,
	CONSTRAINT route_pk
		PRIMARY KEY(path_of_route),
	CONSTRAINT route_station_num_fk
		FOREIGN KEY(route_station_num) REFERENCES station(station_number)
);

CREATE TABLE train
(
	price_per_mile REAL,
	num_seats INTEGER,
	top_speed REAL,
	CONSTRAINT 
		PRIMARY KEY(price_per_mile, num_seats, top_speed)
);

CREATE TABLE schedule
(
	route VARCHAR(100),
	day_of_week VARCHAR(100),
	times_of_day VARCHAR(100),
	CONSTRAINT 
		PRIMARY KEY(route, day_of_week, times_of_day)
);

CREATE TABLE passenger
(
	first_name VARCHAR(50),
	last_name VARCHAR(50),
	address VARCHAR(100),
	email VARCHAR(100),
	telephone_number INTEGER,
	customerid INTEGER, 
	CONSTRAINT 
		PRIMARY KEY(customerid)
);