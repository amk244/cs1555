CREATE TABLE Station
(
	Station_number int
		CONSTRAINT Station_pk
			PRIMARY KEY,
	Hours_of_operation int,
	Address varchar(100)
);

CREATE TABLE Rail_Line
(
	Stations_on_path varchar(100)
		CONTSTRAINT Rail Line_pk
			PRIMARY KEY,
	Speed_limit int,
	Distance_btwn_stations int,
	CONSTRAINT Station_number_fk
		FOREIGN KEY(Station_number) REFERENCES Station(Station_number)
);

CREATE TABLE Route
(
	Path_of_route varchar(100)
		CONSTRAINT Route_pk
			PRIMARY KEY,
	CONSTRAINT Stations_on_path_fk
		FOREIGN KEY (Stations_on_path) REFERENCES Rail_Line(Stations_on_path)
);

CREATE TABLE Train
(
	Price_per_mile int,
	Num_seats int,
	Top_speed int,
	PRIMARY KEY(Price_per_mile, Num_Seats, Top_speed)
);

CREATE TABLE Schedule
(
	Route varchar(100),
	Day_of_week varchar(100),
	Times_of_day varchar(100),
	PRIMARY KEY(Route, Day_of_week, Times_of_day)
);

CREATE TABLE Passenger
(
	CustomerID int
		CONSTRAINT Route_pk
			PRIMARY KEY,
	First_name varchar(50),
	Last_name varchar(50),
	Address varchar(100),
	Email varchar(100),
	Telephone_number int
);
