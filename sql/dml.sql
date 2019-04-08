--import data into tables by inserting through import by right clicking the table->Import Data From File->CSV with a delimiter of ;

--*******1.1*******
--1.1.1 CUSTOMER FORM
--Add a customer to the customer table
CREATE OR REPLACE PROCEDURE insert_customer(fname VARCHAR, lname VARCHAR,str VARCHAR, t VARCHAR, pcode VARCHAR, emailaddr VARCHAR,telnumber VARCHAR)
AS $$
DECLARE randid integer;
BEGIN
    SELECT (random()*200000)::int into randid;
    INSERT INTO customer (customer_id, first_name, last_name, street, town, postal_code, email, telephone_number) VALUES (randid, fname, lname, str, t, pcode, emailaddr, telnumber);
END
$$ LANGUAGE plpgsql;
--CALL insert_customer('bob', 'smith', '402 wolf hall', 'university park', '16802', 'bobsmith2@gmail.com', '412-516-7255');

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
--SELECT * FROM view_customers() as t;

--1.2 Finding A Trip Between Two Stations
--1.2.1 Single Route Trip Search
CREATE OR REPLACE PROCEDURE single_route(start_station INTEGER, end_station INTEGER, time_of_day VARCHAR)
AS $$
  BEGIN

  END
$$ LANGUAGE plpgsql;
--1.2.2 Combination Route Trip Search
CREATE OR REPLACE PROCEDURE combination_route(start_station INTEGER, end_station INTEGER, time_of_day VARCHAR)
AS $$
  BEGIN

  END
$$ LANGUAGE plpgsql;
/*
1.2.3 need to account for available seats of train
1.2.4 show 10 results of the below:
1.2.4.1 fewest stops
1.2.4.2 run through most stations
1.2.4.3 lowest price
1.2.4.4 least total time
1.2.4.5 most total time
1.2.4.6 least total distance
1.2.4.7 most total distance
 */
--1.2.5 ADD RESERVATION
CREATE OR REPLACE PROCEDURE add_reservation(start_station INTEGER, end_station INTEGER, time_of_day VARCHAR)
AS $$
  BEGIN
      --book seat of train for customer
      --num_seats -= 1;
  END
$$ LANGUAGE plpgsql;

--1.3 ********ADVANCED SEARCHES********
--1.3.1 Find all the trains that pass through a specific station at a specific day and time
CREATE OR REPLACE PROCEDURE trains_specific_day_time(station INTEGER, time_of_day TIME, day_of_week VARCHAR)
AS $$
  BEGIN

  END
$$ LANGUAGE plpgsql;
--1.3.2 Find the routes that travel through more than 1 rail line
CREATE OR REPLACE PROCEDURE routes_more_than_one_rail_line()
AS $$
  BEGIN

  END
$$ LANGUAGE plpgsql;
--1.3.3 Find routes that pass through the same stations but do not have the same stops
CREATE OR REPLACE PROCEDURE routes_same_stations_not_stops()
AS $$
  BEGIN

  END
$$ LANGUAGE plpgsql;
--1.3.4 Find any stations through which all trains pass through
CREATE OR REPLACE PROCEDURE stations_all_trains_pass_through()
AS $$
  BEGIN

  END
$$ LANGUAGE plpgsql;
--1.3.5 Find all the trains that do not stop at a specific station
CREATE OR REPLACE PROCEDURE trains_do_not_stop_at_station(station INTEGER)
AS $$
  BEGIN

  END
$$ LANGUAGE plpgsql;
--1.3.6 Find routes that stop at least at XX% of the stations they visit:
--(e.g., if a route passes through 5 stations and stops at at least 3 of them, it will be returned as a result for a 50% search).
CREATE OR REPLACE PROCEDURE routes_stop_at_XX(percent INTEGER)
AS $$
  BEGIN

  END
$$ LANGUAGE plpgsql;                                
--1.3.7 Display The Schedule Of A Route
CREATE OR REPLACE FUNCTION DisplayRouteSchedule(routeid INTEGER) RETURNS SETOF schedule AS $$
BEGIN
  RETURN QUERY
  SELECT * FROM schedule WHERE routeid = schedule.schedule_route_id;
END
$$ LANGUAGE plpgsql;
--SELECT * FROM DisplayRouteSchedule(134) as t;

--1.3.8 Find the availability of a route at every stop on a specific day and time; returns available number of seats
  CREATE OR REPLACE FUNCTION route_train_availability_every_stop(day_of_week VARCHAR, time_of_day TIME) RETURNS SETOF train AS $$
  BEGIN

  END
$$ LANGUAGE plpgsql;
-- Find the number of available seats at each stop of a route for the day and time given as parameters. 

