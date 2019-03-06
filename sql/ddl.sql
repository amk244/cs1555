CREATE TABLE station(
	station_number INTEGER NOT NULL,
	hours_of_operation INTEGER,
	Address VARCHAR(100),
	CONSTRAINT station_pk
		PRIMARY KEY(station_number)
);

CREATE TABLE railline
(
	speed_limit INTEGER,
	distance_btwn_stations REAL,
	station_a INTEGER NOT NULL,
	station_b INTEGER NOT NULL,
	CONSTRAINT railline_pk
		PRIMARY KEY(station_a, station b),
	CONSTRAINT railline_fk_a
		FOREIGN KEY(station_a) REFERENCES station(station_number),
	CONSTRAINT railline_fk_b
		FOREIGN KEY(station_b) REFERENCES station(station_number)
);

CREATE TABLE route
(
	starting_rail_line_station INTEGER NOT NULL,
	ending_rail_line_station INTEGER NOT NULL,
	CONSTRAINT route_pk
		PRIMARY KEY(starting_rail_line_station, ending_rail_line_station),
	CONSTRAINT route_fk_a
		FOREIGN KEY(starting_rail_line_station) REFERENCES station(station_number),
	CONSTRAINT route_fk_b
		FOREIGN KEY(ending_rail_line_station) REFERENCES station(station_number)
);

CREATE TABLE train
(
	price_per_mile REAL NOT NULL,
	num_seats INTEGER NOT NULL,
	top_speed REAL NOT NULL,
	CONSTRAINT 
		PRIMARY KEY(price_per_mile, num_seats, top_speed)
);

CREATE TABLE schedule
(
	routeID VARCHAR(100) NOT NULL,
	day_of_week VARCHAR(100) NOT NULL,
	times_of_day VARCHAR(100),
	trainID VARCHAR(100),
	CONSTRAINT 
		PRIMARY KEY(route, day_of_week, times_of_day),
	CONSTRAINT
		FOREIGN KEY(trainID) REFERENCES train(price_per_mile, num_seats, top_speed),
	CONSTRAINT
		FOREIGN KEY(routeID) REFERENCES route(starting_rail_line_station, ending_rail_line_station)
);

CREATE TABLE passenger
(
	first_name VARCHAR(50),
	last_name VARCHAR(50),
	address VARCHAR(100),
	email VARCHAR(100),
	telephone_number INTEGER,
	customerid INTEGER NOT NULL, 
	CONSTRAINT 
		PRIMARY KEY(customerid)
);