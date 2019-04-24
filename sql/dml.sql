--import data into tables by inserting through import by right clicking the table->Import Data From File->CSV with a delimiter of ;

--*******1.1*******
--1.1.1 CUSTOMER FORM
--Add a customer to the customer table
--DROP FUNCTION insert_customer(fname VARCHAR, lname VARCHAR,str VARCHAR, t VARCHAR, pcode VARCHAR, emailaddr VARCHAR,telnumber VARCHAR);
CREATE OR REPLACE FUNCTION insert_customer(fname VARCHAR, lname VARCHAR,str VARCHAR, t VARCHAR, pcode VARCHAR, emailaddr VARCHAR,telnumber VARCHAR) returns integer
AS $$
DECLARE rand_id integer;
BEGIN
    SELECT (random()*200000)::int into rand_id;
    INSERT INTO customer (customer_id, first_name, last_name, street, town, postal_code, email, telephone_number) VALUES (rand_id, fname, lname, str, t, pcode, emailaddr, telnumber);
    RETURN rand_id;
END
$$ LANGUAGE plpgsql;
--CALL insert_customer('bob', 'smith', '402 wolf hall', 'university park', '16802', 'bobsmith2@gmail.com', '412-516-7255') as t;

--Edit a customer in the customer table
CREATE OR REPLACE PROCEDURE edit_customer(cust_id INTEGER, fname VARCHAR, lname VARCHAR,str VARCHAR, t VARCHAR, pcode VARCHAR, emailaddr VARCHAR,telnumber VARCHAR)
AS $$
BEGIN
    UPDATE  customer
    SET first_name = fname,
        last_name = lname,
        street = str,
        town = t,
        postal_code = pcode,
        email = emailaddr,
        telephone_number = telnumber
    WHERE cust_id = customer_id;
END;
$$ LANGUAGE plpgsql;
--CALL edit_customer(1706, 'nancy', 'd', '523 bigler hall', 'university park', '16802', 'nancysmith@gmail.com', '412-532-7255');

--View the list of customers
CREATE OR REPLACE FUNCTION view_customers() RETURNS SETOF customer AS $$
BEGIN
  RETURN QUERY
  SELECT * FROM customer;
END
$$ LANGUAGE plpgsql;
--SELECT * FROM view_customers();

--1.2 Finding A Trip Between Two Stations
--1.2.1 Single Route Trip Search; Find all routes that stop at a particular start station, a particular end station, and the time of day
--DROP FUNCTION single_route(integer,integer,character varying);
CREATE OR REPLACE FUNCTION single_route(start_station INTEGER, end_station INTEGER, weekday VARCHAR) RETURNS SETOF route
AS $$
  BEGIN
    --assumed that order does not matter; most likely need to add order to the route_stops/stations table
    CREATE TEMP TABLE IF NOT EXISTS start_routes AS SELECT route_id FROM route_stops WHERE route_stops.station_id = start_station;
    CREATE TEMP TABLE IF NOT EXISTS end_routes AS SELECT route_id FROM route_stops WHERE route_stops.station_id = end_station;
    CREATE TEMP TABLE IF NOT EXISTS has_both AS SELECT start_routes.route_id  FROM start_routes INNER JOIN end_routes ON start_routes.route_id = end_routes.route_id;
    RETURN QUERY
      SELECT DISTINCT has_both.route_id FROM has_both INNER JOIN schedule ON has_both.route_id = schedule.schedule_route_id WHERE schedule.day_of_week = weekday;
  END
$$ LANGUAGE plpgsql;
--SELECT * FROM single_route(1, 3, 'Wednesday');

--1.2.2 Combination Route Trip Search
CREATE OR REPLACE FUNCTION combination_route(start_station INTEGER, end_station INTEGER, weekday VARCHAR) RETURNS SETOF route
AS $$
  BEGIN
    --***NEED to add ORDER to the route_stations/route_stops for this query
    CREATE TEMP TABLE IF NOT EXISTS start_routes AS SELECT route_id FROM route_stops WHERE route_stops.station_id = start_station;
    CREATE TEMP TABLE IF NOT EXISTS end_routes AS SELECT route_id FROM route_stops WHERE route_stops.station_id = end_station;
    CREATE TEMP TABLE IF NOT EXISTS has_both AS SELECT start_routes.route_id  FROM start_routes INNER JOIN end_routes ON start_routes.route_id = end_routes.route_id;
    RETURN QUERY
      SELECT DISTINCT has_both.route_id FROM has_both INNER JOIN schedule ON has_both.route_id = schedule.schedule_route_id WHERE schedule.day_of_week = weekday;
  END
$$ LANGUAGE plpgsql;
--SELECT * FROM combination_route(2, 3, 'Saturday') as t;
--
/*
1.2.3 need to account for available seats of train (DONE)
1.2.4 show 10 results of the below:
1.2.4.1 fewest stops
1.2.4.2 run through most stations
1.2.4.3 lowest price
1.2.4.4 least total time
1.2.4.5 most total time
1.2.4.6 least total distance
1.2.4.7 most total distance
 */
--1.2.5 ADD RESERVATION given a route; this can be called numerous times in the JDBC for multiple routes for 1.2.2
CREATE OR REPLACE PROCEDURE add_reservation(routenum INTEGER, givenday VARCHAR)
AS $$
  BEGIN
      --book seat of train for customer
      UPDATE train
      SET num_seats = num_seats - 1
      WHERE train_id = (SELECT schedule_train_id FROM schedule WHERE schedule.schedule_route_id = routenum AND schedule.day_of_week = givenday LIMIT 1);
  END
$$ LANGUAGE plpgsql;
--CALL add_reservation(22);

--1.3 ********ADVANCED SEARCHES********
--1.3.1 Find all the trains that pass through a specific station at a specific day and time
CREATE OR REPLACE FUNCTION trains_specific_day_time(station INTEGER, day VARCHAR, time_day TIME) RETURNS TABLE(trains INTEGER)
AS $$
  BEGIN
    CREATE TEMP TABLE IF NOT EXISTS train_route_day_time AS SELECT schedule_train_id, schedule_route_id FROM schedule WHERE time_day < schedule.time_of_day AND schedule.day_of_week = day;
    CREATE TEMP TABLE IF NOT EXISTS route_station_trains AS SELECT route_stations.route_id, route_stations.station_id, train_route_day_time.schedule_train_id FROM train_route_day_time INNER JOIN route_stations ON train_route_day_time.schedule_route_id = route_stations.route_id;
    RETURN QUERY
    SELECT DISTINCT schedule_train_id FROM route_station_trains WHERE station_id = station;
  END
$$ LANGUAGE plpgsql;
--SELECT * FROM trains_specific_day_time(1,'Tuesday','03:00:00'::TIME) as t;

--1.3.2 Find the routes that travel through more than 1 rail line
CREATE OR REPLACE FUNCTION routes_more_than_one_rail_line() RETURNS SETOF route
AS $$
  BEGIN
    CREATE TEMP TABLE IF NOT EXISTS route_rail_pairs AS SELECT DISTINCT route_stations.route_id, rail_line_stations.rail_line_id FROM route_stations LEFT OUTER JOIN rail_line_stations ON route_stations.station_id = rail_line_stations.station_id ORDER BY route_stations.route_id;
    RETURN QUERY
      SELECT route_id FROM route_rail_pairs GROUP BY route_id HAVING route_rail_pairs.count > 1;
  END
$$ LANGUAGE plpgsql;
--SELECT * FROM routes_more_than_one_rail_line() as t;

--1.3.3 Find routes that pass through the same stations but do not have the same stops
CREATE OR REPLACE FUNCTION routes_same_stations_not_stops() RETURNS SETOF route
AS $$
  BEGIN
    RETURN QUERY
      SELECT DISTINCT a.route_id FROM route_stations a LEFT OUTER JOIN route_stations b on a.station_id = b.station_id AND a.route_id != b.route_id AND b.route_id IS NULL;
  END
$$ LANGUAGE plpgsql;
--SELECT * FROM routes_same_stations_not_stops();

--1.3.4 Find any stations through which all trains pass through
--DROP FUNCTION stations_all_trains_pass_through();
CREATE OR REPLACE FUNCTION stations_all_trains_pass_through() RETURNS TABLE (station_num INTEGER)
AS $$
  BEGIN
    --count must be 350 for all trains to pass through all stations
    CREATE TEMP TABLE IF NOT EXISTS station_train_list AS SELECT DISTINCT route_stops.station_id, schedule.schedule_train_id FROM route_stops FULL OUTER JOIN schedule ON route_stops.route_id = schedule.schedule_route_id;
    RETURN QUERY
      SELECT station_id FROM station_train_list GROUP BY station_id HAVING COUNT(*) = 350;
  END
$$ LANGUAGE plpgsql;
--SELECT * FROM stations_all_trains_pass_through();

--1.3.5 Find all the trains that do not stop at a specific station
--DROP FUNCTION trains_do_not_stop_at_station(integer);
CREATE OR REPLACE FUNCTION trains_do_not_stop_at_station(station INTEGER) RETURNS TABLE(t integer)
AS $$
  BEGIN
     CREATE TEMP TABLE IF NOT EXISTS routes_do_stop_here AS SELECT route_id FROM route_stops WHERE station_id = station;
     CREATE TEMP TABLE IF NOT EXISTS trains_that_stop_here AS SELECT schedule.schedule_train_id FROM schedule INNER JOIN routes_do_stop_here ON schedule.schedule_route_id = routes_do_stop_here.route_id;
     RETURN QUERY
        SELECT train.train_id FROM train LEFT OUTER JOIN trains_that_stop_here ON train.train_id = trains_that_stop_here.schedule_train_id WHERE trains_that_stop_here.schedule_train_id IS NULL;
      --WHERE NOT IN station
  END
$$ LANGUAGE plpgsql;
--SELECT * FROM trains_do_not_stop_at_station(48);

--1.3.6 Find routes that stop at least at XX% of the stations they visit:
--(e.g., if a route passes through 5 stations and stops at at least 3 of them, it will be returned as a result for a 50% search).
CREATE OR REPLACE FUNCTION routes_stop_at_XX(percent REAL) RETURNS SETOF route
AS $$
  BEGIN
    CREATE TEMP TABLE IF NOT EXISTS c1 AS SELECT route_id, COUNT(*) FROM route_stations GROUP BY route_id;
    CREATE TEMP TABLE IF NOT EXISTS c2 AS SELECT route_id,  COUNT(*)  FROM route_stops GROUP BY route_id;
    RETURN QUERY
      SELECT c1.route_id FROM c1 INNER JOIN c2 on c1.route_id = c2.route_id WHERE (c2.count/c1.count >= (percent/100.0));
  END
$$ LANGUAGE plpgsql; 
--SELECT * FROM routes_stop_at_XX(4);           
                 
--1.3.7 Display The Schedule Of A Route
CREATE OR REPLACE FUNCTION DisplayRouteSchedule(routeid INTEGER) RETURNS SETOF schedule AS $$
BEGIN
  RETURN QUERY
    SELECT * FROM schedule WHERE routeid = schedule.schedule_route_id;
END
$$ LANGUAGE plpgsql;
--SELECT * FROM DisplayRouteSchedule(1212) as t;

--1.3.8 Find the availability of a route at every stop on a specific day and time; returns available number of seats
--DROP FUNCTION route_train_availability_every_stop(INTEGER,VARCHAR, TIME);
CREATE OR REPLACE FUNCTION route_train_availability_every_stop(route INTEGER, day VARCHAR, time_day TIME) RETURNS TABLE(seats integer, station integer)
AS $$
  BEGIN
    --check availability of seats at every stop
    RETURN QUERY
    SELECT num_seats, station_id from train INNER JOIN schedule on schedule.day_of_week = day AND schedule.time_of_day = time_day  INNER JOIN route_stops on route_stops.route_id = route;
  END
$$ LANGUAGE plpgsql;
--SELECT * FROM route_train_availability_every_stop(22, 'Tuesday', '10:00:00');

--2.3 DROP ALL TABLES
--DROP TABLE IF EXISTS station CASCADE;DROP TABLE IF EXISTS rail_line CASCADE;DROP TABLE IF EXISTS route CASCADE;DROP TABLE IF EXISTS route_stations;DROP TABLE IF EXISTS rail_line_stations;DROP TABLE IF EXISTS route_stops;DROP TABLE IF EXISTS train CASCADE;DROP TABLE IF EXISTS schedule CASCADE;DROP TABLE IF EXISTS customer CASCADE;

--2.2 EXPORT
--COPY (SELECT * FROM customer) TO 'C:\database\customer.txt' WITH DELIMITER ';';
--COPY (SELECT * FROM rail_line) TO 'C:\database\rail_line.txt' WITH DELIMITER ';';
--COPY (SELECT * FROM rail_line_stations) TO 'C:\database\rail_line_stations.txt' WITH DELIMITER ';';
--COPY (SELECT * FROM route) TO 'C:\database\route.txt' WITH DELIMITER ';';
--COPY (SELECT * FROM route_stations) TO 'C:\database\route_stations.txt' WITH DELIMITER ';';
--COPY (SELECT * FROM route_stops) TO 'C:\database\route_stops.txt' WITH DELIMITER ';';
--COPY (SELECT * FROM schedule) TO 'C:\database\schedule.txt' WITH DELIMITER ';';
--COPY (SELECT * FROM station)TO 'C:\database\station.txt'WITH DELIMITER ';';
--COPY (SELECT * FROM train)TO 'C:\database\train.txt' WITH DELIMITER ';';

--2.1 IMPORT
--COPY customer FROM 'C:\database\customer.txt' WITH DELIMITER ';';
--COPY rail_line FROM 'C:\database\rail_line.txt' WITH DELIMITER ';';
--COPY route FROM 'C:\database\route.txt' WITH DELIMITER ';';
--COPY station FROM 'C:\database\station.txt' WITH DELIMITER ';';
--COPY train FROM 'C:\database\train.txt' WITH DELIMITER ';';
--COPY schedule FROM 'C:\database\schedule.txt' WITH DELIMITER ';';
--COPY rail_line_stations FROM 'C:\database\rail_line_stations.txt' WITH DELIMITER ';';
--COPY route_stations FROM 'C:\database\route_stations.txt' WITH DELIMITER ';';
--COPY route_stops FROM 'C:\database\route_stops.txt' WITH DELIMITER ';';



