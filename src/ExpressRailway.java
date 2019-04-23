import java.sql.*;  //import the file containing definitions for the parts
import java.text.ParseException;
                    //needed by java for database connection and manipulation
import java.util.Scanner; //reading user input to 

public class ExpressRailway {
    private static Connection connection; //used to hold the jdbc connection to the DB
    private Statement statement; //used to create an instance of the connection
    private PreparedStatement prepStatement; //used to create a prepared statement, that will be later reused
    private ResultSet resultSet; //used to hold the result of your query (if one
    // exists)
    private String query;  //this will hold the query we are using
    
    
    public ExpressRailway(int example_no) {
	
	switch ( example_no) {
	case 0:
		Option125();
		break;
	case 1:
	    Option111();
	    break;
	case 2:
	    Option112();
	    break;
	case 3:
	    Option113();
	    break;
	case 4:
		Option121();
		break;
	case 5:
		Option122();
		break;
	case 6:
		Option131();
		break;
	case 7:
		Option132();
		break;
	case 8:
		Option133();
		break;
	case 9:
		Option134();
		break;
	case 10:
		Option135();
		break;
	case 11:
		Option136();
		break;
	case 12:
		Option137();
		break;
	case 13:
		Option138();
		break;
	case 14:
		Option21();
	    break;
	case 15:
		Option22();
	    break;
	case 16:
		Option23();
	    break;
	default:
	    System.out.println("Example not found for your entry: " + example_no);
	    try {
		connection.close();
	    }
	    catch(Exception Ex)  {
		System.out.println("Error connecting to database.  Machine Error: " +
				   Ex.toString());
	    }
	    break;
	}
			
    }

    // **************OPTIONS 1.1 CUSTOMER LIST OPERATIONS**************
    ///////////////////Option 1.1.1 - ADD A Customer////////////////////////
    public void Option111() {
	try {
		
		Scanner in = new Scanner(System.in);
		System.out.println("Enter the first name of the customer: ");
        String firstName = in.nextLine();
        System.out.println("Enter the last name of the customer: ");
        String lastName = in.nextLine();
        System.out.println("Enter the street address of the customer: ");
        String street = in.nextLine();
        System.out.println("Enter the city of the customer: ");
        String town = in.nextLine();
        System.out.println("Enter the zip code of the customer: ");
        String zip = in.nextLine();
        System.out.println("Enter the email of the customer: ");
        String email = in.nextLine();
        System.out.println("Enter the phone number of the customer: ");
        String phone = in.nextLine();

        query = "SELECT * FROM insert_customer(?,?,?,?,?,?,?)";
	    prepStatement = connection.prepareStatement(query);

	    // You need to specify which question mark to replace with a value.
	    // They are numbered 1 2 3 etc..
	    prepStatement.setString(1, firstName); 
	    prepStatement.setString(2, lastName); 
	    prepStatement.setString(3, street); 
	    prepStatement.setString(4, town); 
	    prepStatement.setString(5, zip); 
	    prepStatement.setString(6, email); 
	    prepStatement.setString(7, phone); 
	    // Now that the statement is ready. Let's execute it. Note the use of 
	    // executeUpdate for insertions and updates instead of executeQuery for 
	    // selections.
	    resultSet = prepStatement.executeQuery();

	    //confirm addition of new customer by displaying ID on screen
	    
	    while(resultSet.next()) //this not only keeps track of if another record
		//exists but moves us forward to the first record
	    {
	    	System.out.println("Customer has been inserted for: " + firstName + " and the ID:\n");
		   System.out.println(resultSet.getInt(1) + "\n");
		}
	    //query = "CALL insert_customer('bob', 'smith', '402 wolf hall', 'university park', '16802', 'bobsmith2@gmail.com', '412-516-7255');"; 
	    
	     resultSet.close();
	   
	}	
	catch(Exception Ex)  
	{
		System.out.println("Machine Error: " +
				   Ex.toString());
	}
	finally{
		try {
			if (statement!=null) statement.close();
			if (prepStatement != null) prepStatement.close();
		} catch (SQLException e) {
			System.out.println("Cannot close Statement. Machine error: "+e.toString());
		}
	}
	
    }

    ///////////////////Option 1.1.2 - Edit A Customer Based on ID////////////////////////
    public void Option112() {
	try {
		
		Scanner in = new Scanner(System.in);
		System.out.println("Enter the ID of the customer to be edited: ");
        int id = in.nextInt();
        query = "SELECT * FROM customer WHERE customer_id = ?";
        prepStatement = connection.prepareStatement(query);
        prepStatement.setInt(1, id);
        resultSet = prepStatement.executeQuery();
        if(!resultSet.next())
        {
        	System.out.println("INVALID customer ID ");
        	System.exit(0);
        }

        in.nextLine(); //skip new line
		System.out.println("Enter the first name of the customer: ");
        String firstName = in.nextLine();
        System.out.println("Enter the last name of the customer: ");
        String lastName = in.nextLine();
        System.out.println("Enter the street address of the customer: ");
        String street = in.nextLine();
        System.out.println("Enter the city of the customer: ");
        String town = in.nextLine();
        System.out.println("Enter the zip code of the customer: ");
        String zip = in.nextLine();
        System.out.println("Enter the email of the customer: ");
        String email = in.nextLine();
        System.out.println("Enter the phone number of the customer: ");
        String phone = in.nextLine();

        query = "CALL edit_customer(?,?,?,?,?,?,?,?)";
	    prepStatement = connection.prepareCall(query);

	    // You need to specify which question mark to replace with a value.
	    // They are numbered 1 2 3 etc..
	    prepStatement.setInt(1, id);
	    prepStatement.setString(2, firstName); 
	    prepStatement.setString(3, lastName); 
	    prepStatement.setString(4, street); 
	    prepStatement.setString(5, town); 
	    prepStatement.setString(6, zip); 
	    prepStatement.setString(7, email); 
	    prepStatement.setString(8, phone); 
	    // Now that the statement is ready. Let's execute it. Note the use of 
	    // executeUpdate for insertions and updates instead of executeQuery for 
	    // selections.
	    prepStatement.execute();

	    //confirm edit of customer by displaying ID on screen
	    System.out.println("Customer has been edited for the ID: " + id);
	}	
	catch(Exception Ex)  
	{
		System.out.println("Machine Error: " +
				   Ex.toString());
	}
	finally{
		try {
			if (prepStatement != null) prepStatement.close();
		} catch (SQLException e) {
			System.out.println("Cannot close Statement. Machine error: "+e.toString());
		}
	}
	
    }

    ///////////////////Option 1.1.3 - View A Customer////////////////////////
    public void Option113() {
	
	int counter = 1;

	try{
	    statement = connection.createStatement(); //create an instance
	    String selectQuery = "SELECT * FROM view_customers();"; //query
	    
	    resultSet = statement.executeQuery(selectQuery); //run the query on the DB table
	   
	    /*the results in resultSet have an odd quality. The first row in result
	      set is not relevant data, but rather a place holder.  This enables us to
	      use a while loop to go through all the records.  We must move the pointer
	      forward once using resultSet.next() or you will get errors*/

	    while (resultSet.next()) //this not only keeps track of if another record
		//exists but moves us forward to the first record
	    {
		   System.out.println("Customer " + ": " +
		      resultSet.getLong(1) + ", " + //since the first item was of type
		      //string, we use getString of the
		      //resultSet class to access it.
		      //Notice the one, that is the
		      //position of the answer in the
		      //resulting table
		      resultSet.getString(2) + ", " +   //since second item was number(10),
		      //we use getLong to access it
		      //resultSet.getDate(3)); //since type date, getDate.
		      resultSet.getString(3) + ", " +
		      resultSet.getString(4) + ", " +
		      resultSet.getString(5) + ", " +
		      resultSet.getString(6) + ", " +
		      resultSet.getString(7) + ", " +
		      resultSet.getString(8));

		  counter++;
	    }

	     resultSet.close();
	}
	catch(SQLException Ex) {
	    System.out.println("Error viewing the customer data.  Machine Error: " +
			       Ex.toString());
	}
	finally{
		try {
			if (statement != null) statement.close();
		} catch (SQLException e) {
			System.out.println("Cannot close Statement. Machine error: "+e.toString());
		}
	}
 }

 ///////////////////Option 1.2.1 - Find a Single Route Trip between two stations on a specified day of the week////////////////////////
    public void Option121() {
	try {
		
		Scanner in = new Scanner(System.in);
		System.out.println("Enter the start station id: ");
        int start = in.nextInt();
        System.out.println("Enter the end station id: ");
        int stop = in.nextInt();
        System.out.println("Enter the weekday: ");
        in.nextLine(); //eat new line character
        String day = in.nextLine();
        query = "SELECT * FROM single_route(?, ?, ?)";
        prepStatement = connection.prepareStatement(query);
        prepStatement.setInt(1, start);
        prepStatement.setInt(2, stop);
        prepStatement.setString(3, day);
        resultSet = prepStatement.executeQuery();

       while (resultSet.next())
	   {
			System.out.println("RouteID:" + resultSet.getInt(1));
	   }

	   System.out.println("The folowing sorting options are available, choose one of the following: ");
	   System.out.println("Type 1 to sort by Fewest Stops (Option 1.2.4.1)");
	   System.out.println("Type 2 to sort by Runs through the most stations (Option 1.2.4.2)");
	   System.out.println("Type 3 to sort by Lowest Price (Option 1.2.4.3)");
	   System.out.println("Type 4 to sort by Highest Price (Option 1.2.4.4)");
	   System.out.println("Type 5 to sort by Least Total Time (Option 1.2.4.5)");
	   System.out.println("Type 6 to sort by Most Total Time (Option 1.2.4.6)");
	   System.out.println("Type 7 to sort by Least Total Distance (Option 1.2.4.7)");
	   System.out.println("Type 8 to sort by Most Total Distance (Option 1.2.4.8)");
       int option = in.nextInt();

	    resultSet.close();
	}	
	catch(Exception Ex)  
	{
		System.out.println("Machine Error: " +
				   Ex.toString());
	}
	finally{
		try {
			if (prepStatement != null) prepStatement.close();
		} catch (SQLException e) {
			System.out.println("Cannot close Statement. Machine error: "+e.toString());
		}
	}
	
    }

    ///////////////////Option 1.2.2 - Find a Combination Route Trip between two stations on a specified day of the week////////////////////////
    public void Option122() {
	try {
		
		Scanner in = new Scanner(System.in);
		System.out.println("Enter the start station id: ");
        int start = in.nextInt();
        System.out.println("Enter the end station id: ");
        int stop = in.nextInt();
        System.out.println("Enter the weekday: ");
        in.nextLine(); //eat new line character
        String day = in.nextLine();
        query = "SELECT * FROM combination_route(?, ?, ?)";
        prepStatement = connection.prepareStatement(query);
        prepStatement.setInt(1, start);
        prepStatement.setInt(2, stop);
        prepStatement.setString(3, day);
        resultSet = prepStatement.executeQuery();

       while (resultSet.next())
	   {
			System.out.println("RouteID:" + resultSet.getInt(1));
	   }

	   System.out.println("The folowing sorting options are available, choose one of the following: ");
	   System.out.println("Type 1 to sort by Fewest Stops (Option 1.2.4.1)");
	   System.out.println("Type 2 to sort by Runs through the most stations (Option 1.2.4.2)");
	   System.out.println("Type 3 to sort by Lowest Price (Option 1.2.4.3)");
	   System.out.println("Type 4 to sort by Highest Price (Option 1.2.4.4)");
	   System.out.println("Type 5 to sort by Least Total Time (Option 1.2.4.5)");
	   System.out.println("Type 6 to sort by Most Total Time (Option 1.2.4.6)");
	   System.out.println("Type 7 to sort by Least Total Distance (Option 1.2.4.7)");
	   System.out.println("Type 8 to sort by Most Total Distance (Option 1.2.4.8)");
       int option = in.nextInt();

	    resultSet.close();
	}	
	catch(Exception Ex)  
	{
		System.out.println("Machine Error: " +
				   Ex.toString());
	}
	finally{
		try {
			if (prepStatement != null) prepStatement.close();
		} catch (SQLException e) {
			System.out.println("Cannot close Statement. Machine error: "+e.toString());
		}
	}
	
    }

    //////////////////Option 1.2.5 - Add a reservation for a route ////////////////////////
    public void Option125() {
	try {
		
		Scanner in = new Scanner(System.in);
		System.out.println("Enter the route id: ");
        int id = in.nextInt();
        System.out.println("Enter the given day: ");
        in.nextLine(); // eat newline
        String day = in.nextLine();
        query = "CALL add_reservation(?, ?)";
        prepStatement = connection.prepareCall(query);
        prepStatement.setInt(1, id);
        prepStatement.setString(2, day);
        prepStatement.execute();
	}	
	catch(Exception Ex)  
	{
		System.out.println("Machine Error: " +
				   Ex.toString());
	}
	finally{
		try {
			if (prepStatement != null) prepStatement.close();
		} catch (SQLException e) {
			System.out.println("Cannot close Statement. Machine error: "+e.toString());
		}
	}
	
    }

    /////////////////////////// **1.3 ADVANCED SEARCHES** ///////////////////////////

    //////////////////Option 1.3.1 - Find all the trains that pass through a specific station at a specific day/time ////////////////////////
    public void Option131() {
	try {
		
		Scanner in = new Scanner(System.in);
		System.out.println("Enter the station id: ");
		int num = in.nextInt();
		System.out.println("Enter the day: ");
		in.nextLine(); //eat new line
        String day = in.nextLine();
        System.out.println("Enter the time: ");
        String time = in.nextLine(); 
        query = "CALL trains_specific_day_time(?, ?, ?)";
        prepStatement = connection.prepareCall(query);
        prepStatement.setInt(1, num);
        prepStatement.setString(2, day);
        prepStatement.setTime(3, java.sql.Time.valueOf(time));
        prepStatement.execute();
	}	
	catch(Exception Ex)  
	{
		System.out.println("Machine Error: " +
				   Ex.toString());
	}
	finally{
		try {
			if (prepStatement != null) prepStatement.close();
		} catch (SQLException e) {
			System.out.println("Cannot close Statement. Machine error: "+e.toString());
		}
	}
	
    }

    //////////////////Option 1.3.2 - Find the routes that travel more than one rail line ////////////////////////
    public void Option132() {
	try {
		statement = connection.createStatement(); //create an instance
	    String selectQuery = "SELECT * routes_more_than_one_rail_line()"; //query
        resultSet = statement.executeQuery(selectQuery);

        while (resultSet.next()) //this not only keeps track of if another record
		//exists but moves us forward to the first record
	    {
	    }
	}	
	catch(Exception Ex)  
	{
		System.out.println("Machine Error: " +
				   Ex.toString());
	}
	finally{
		try {
			if (prepStatement != null) prepStatement.close();
		} catch (SQLException e) {
			System.out.println("Cannot close Statement. Machine error: "+e.toString());
		}
	}
	
    }

    //////////////////Option 1.3.3 - Find the routes that pass through the same stations but don't have the same stops ////////////////////////
    public void Option133() {
	try {
		statement = connection.createStatement(); //create an instance
	    String selectQuery = "SELECT * routes_same_stations_not_stops()"; //query
        resultSet = statement.executeQuery(selectQuery);

        while (resultSet.next()) //this not only keeps track of if another record
		//exists but moves us forward to the first record
	    {
	    }
	}	
	catch(Exception Ex)  
	{
		System.out.println("Machine Error: " +
				   Ex.toString());
	}
	finally{
		try {
			if (prepStatement != null) prepStatement.close();
		} catch (SQLException e) {
			System.out.println("Cannot close Statement. Machine error: "+e.toString());
		}
	}
	
    }

    //////////////////Option 1.3.4 - Find any stations through which all trains pass through ////////////////////////
    public void Option134() {
	try {
		statement = connection.createStatement(); //create an instance
	    String selectQuery = "SELECT * stations_all_trains_pass_through()"; //query
        resultSet = statement.executeQuery(selectQuery);

        while (resultSet.next()) //this not only keeps track of if another record
		//exists but moves us forward to the first record
	    {
	    }
	}	
	catch(Exception Ex)  
	{
		System.out.println("Machine Error: " +
				   Ex.toString());
	}
	finally{
		try {
			if (prepStatement != null) prepStatement.close();
		} catch (SQLException e) {
			System.out.println("Cannot close Statement. Machine error: "+e.toString());
		}
	}
	
    }

    //////////////////Option 1.3.5 - Find all trains that do not stop at a specific station ////////////////////////
    public void Option135() {
	try {
		
		Scanner in = new Scanner(System.in);
		System.out.println("Enter the station id: ");
		int num = in.nextInt();
        query = "SELECT * trains_do_not_stop_at_station(?)";
        prepStatement = connection.prepareStatement(query);
        prepStatement.setInt(1, num);
        resultSet = prepStatement.executeQuery();
        while (resultSet.next()) //this not only keeps track of if another record
		//exists but moves us forward to the first record
	    {
	    }
	}	
	catch(Exception Ex)  
	{
		System.out.println("Machine Error: " +
				   Ex.toString());
	}
	finally{
		try {
			if (prepStatement != null) prepStatement.close();
		} catch (SQLException e) {
			System.out.println("Cannot close Statement. Machine error: "+e.toString());
		}
	}
	
    }

    //////////////////Option 1.3.6 - Find routes that stop at at least XX% of the Stations they visit ////////////////////////
    public void Option136() {
	try {
		
		Scanner in = new Scanner(System.in);
		System.out.println("Enter the percentage of stop/stations: ");
		int num = in.nextInt();
        query = "SELECT * routes_stop_at_XX(?)";
        prepStatement = connection.prepareStatement(query);
        prepStatement.setInt(1, num);
        resultSet = prepStatement.executeQuery();
        while (resultSet.next()) //this not only keeps track of if another record
		//exists but moves us forward to the first record
	    {
	    }
	}	
	catch(Exception Ex)  
	{
		System.out.println("Machine Error: " +
				   Ex.toString());
	}
	finally{
		try {
			if (prepStatement != null) prepStatement.close();
		} catch (SQLException e) {
			System.out.println("Cannot close Statement. Machine error: "+e.toString());
		}
	}
	
    }

    //////////////////Option 1.3.7 - Find the schedule of a specific route ////////////////////////
    public void Option137() {
	try {
		
		Scanner in = new Scanner(System.in);
		System.out.println("Enter the route id to display the schedule: ");
		int num = in.nextInt();
        query = "SELECT * FROM DisplayRouteSchedule(?)";
        prepStatement = connection.prepareStatement(query);
        prepStatement.setInt(1, num);
        resultSet = prepStatement.executeQuery();
        while (resultSet.next()) //this not only keeps track of if another record
		//exists but moves us forward to the first record
	    {
	    	System.out.println("Route ID " + resultSet.getInt(1) + ": " +
		      resultSet.getString(2) + ", " + 
		      resultSet.getTime(3) + ", " +   
		      resultSet.getInt(4));
	    }
	}	
	catch(Exception Ex)  
	{
		System.out.println("Machine Error: " +
				   Ex.toString());
	}
	finally{
		try {
			if (prepStatement != null) prepStatement.close();
		} catch (SQLException e) {
			System.out.println("Cannot close Statement. Machine error: "+e.toString());
		}
	}
	
    }

    //////////////////Option 1.3.8 - Find availability of a route at every stop on a specific day and time ////////////////////////
    public void Option138() {
	try {
		
		Scanner in = new Scanner(System.in);
		System.out.println("Enter the day: ");
		in.nextLine(); //eat new line
        String day = in.nextLine();
        System.out.println("Enter the time: ");
        String time = in.nextLine(); 
        query = "SELECT * FROM route_train_availability_every_stop(?, ?)";
        prepStatement = connection.prepareStatement(query);
        prepStatement.setString(1, day);
        prepStatement.setTime(2, java.sql.Time.valueOf(time));
        resultSet = prepStatement.executeQuery();
        while (resultSet.next()) //this not only keeps track of if another record
		//exists but moves us forward to the first record
	    {
	    }
	}	
	catch(Exception Ex)  
	{
		System.out.println("Machine Error: " +
				   Ex.toString());
	}
	finally{
		try {
			if (prepStatement != null) prepStatement.close();
		} catch (SQLException e) {
			System.out.println("Cannot close Statement. Machine error: "+e.toString());
		}
	}
	
    }

 ///////////////////////////// 2 -  DATABASE ADMINISTRATION /////////////////////////////

 ///////////////////Option 2.1 - Export Data From The Database////////////////////////
    public void Option21() {
	
	try{
		System.out.println("Enter the filepath with the datafiles to be exported (i.e. C:\\temp: )");
		Scanner in = new Scanner(System.in);
	    String filepath = in.nextLine();
	    statement = connection.createStatement(); //create an instance
	    String selectQuery = "COPY customer FROM '" + filepath + "\\customer.txt' WITH DELIMITER ';';COPY rail_line FROM '" + filepath + "\\rail_line.txt' WITH DELIMITER ';';COPY route FROM '" + filepath + "\\route.txt' WITH DELIMITER ';';COPY station FROM '" + filepath + "\\station.txt' WITH DELIMITER ';';COPY train FROM '" + filepath + "\\train.txt' WITH DELIMITER ';';COPY schedule FROM '" + filepath + "\\schedule.txt' WITH DELIMITER ';';COPY rail_line_stations FROM '" + filepath + "\\rail_line_stations.txt' WITH DELIMITER ';';COPY route_stations FROM '" + filepath + "\\route_stations.txt' WITH DELIMITER ';';COPY route_stops FROM '" + filepath + "\\route_stops.txt' WITH DELIMITER ';';";
	    statement.execute(selectQuery);
	}
	catch(SQLException Ex) {
	    System.out.println("Error exporting data.  Machine Error: " +
			       Ex.toString());
	}
	finally{
		try {
			if (statement != null) statement.close();
		} catch (SQLException e) {
			System.out.println("Cannot close Statement. Machine error: "+e.toString());
		}
	}
 }

 ///////////////////Option 2.2 - Import Data To The Database////////////////////////
    public void Option22() {
	
	try{
		System.out.println("Enter the filepath with the datafiles to be imported: (i.e. C:\\temp: )");
		Scanner in = new Scanner(System.in);
	    String filepath = in.nextLine();
	    statement = connection.createStatement(); //create an instance
	  	String selectQuery = "COPY (SELECT * FROM customer) TO '" + filepath + "\\customer.txt' WITH DELIMITER ';';COPY (SELECT * FROM rail_line) TO '" + filepath + "\\rail_line.txt' WITH DELIMITER ';';COPY (SELECT * FROM rail_line_stations) TO '" + filepath + "\\rail_line_stations.txt' WITH DELIMITER ';';COPY (SELECT * FROM route) TO '" + filepath + "\\route.txt' WITH DELIMITER ';';COPY (SELECT * FROM route_stations) TO '" + filepath + "\\route_stations.txt' WITH DELIMITER ';';COPY (SELECT * FROM route_stops) TO '" + filepath + "\\route_stops.txt' WITH DELIMITER ';';COPY (SELECT * FROM schedule) TO '" + filepath + "\\schedule.txt' WITH DELIMITER ';';COPY (SELECT * FROM station)TO '" + filepath + "\\station.txt'WITH DELIMITER ';';COPY (SELECT * FROM train)TO '" + filepath + "\\train.txt' WITH DELIMITER ';';";
	    statement.execute(selectQuery);
	}
	catch(SQLException Ex) {
	    System.out.println("Error importing data  Machine Error: " +
			       Ex.toString());
	}
	finally{
		try {
			if (statement != null) statement.close();
		} catch (SQLException e) {
			System.out.println("Cannot close Statement. Machine error: "+e.toString());
		}
	}
 }

 ///////////////////Option 2.3 - Delete The Database////////////////////////
    public void Option23() {
	
	try{
	    System.out.println("Are you sure you want to delete the database (type 'yes' for confirmation)");
	    Scanner in = new Scanner(System.in);
	    String answer = in.nextLine();
	    if(answer.equals("yes"))
	    {
	    	statement = connection.createStatement(); //create an instance
	    	String selectQuery = "DROP TABLE IF EXISTS station CASCADE;DROP TABLE IF EXISTS rail_line CASCADE;DROP TABLE IF EXISTS route CASCADE;DROP TABLE IF EXISTS route_stations;DROP TABLE IF EXISTS rail_line_stations;DROP TABLE IF EXISTS route_stops;DROP TABLE IF EXISTS train CASCADE;DROP TABLE IF EXISTS schedule CASCADE;DROP TABLE IF EXISTS customer CASCADE;"; //sample query
	    	statement.execute(selectQuery);
	    }else{
	    	System.out.println("Confirmation failed. The database was not deleted.\n");
	    }
	}
	catch(SQLException Ex) {
	    System.out.println("Error deleting the database.  Machine Error: " +
			       Ex.toString());
	}
	finally{
		try {
			if (statement != null) statement.close();
		} catch (SQLException e) {
			System.out.println("Cannot close Statement. Machine error: "+e.toString());
		}
	}
 }
    
   /////////////     MAIN       ///////////////
  public static void main(String args[]) throws SQLException
  {
    /* Making a connection to a DB causes certain exceptions.  In order to handle
	   these, you either put the DB stuff in a try block or have your function
	   throw the Exceptions and handle them later.  For this demo I will use the
	   try blocks */

    String username, password;
	username = "postgres"; //This is your username in the PostgreSQL server
	password = "postgres"; //This is your password in the PostgreSQL server
	
	try{
	    // Register the PostgreSQL driver.  
        Class.forName("org.postgresql.Driver");
	    
	    // Provide the location of the database
	    String url = "jdbc:postgresql://localhost:5432/"; 
	    
	    //create a connection to DB on class3.cs.pitt.edu
	    connection = DriverManager.getConnection(url, username, password); 
	    System.out.println("Welcome to ExpressRailway. Please choose the options presented below: ");
	    System.out.println("Type 1 to add a Customer (option 1.1.1) ");
	    System.out.println("Type 2 to edit a Customer based on ID number (option 1.1.2) ");
	    System.out.println("Type 3 to view all the Customers (option 1.1.3) ");
	    System.out.println("Type 4 to find a single route trip (option 1.2.1) ");
	    System.out.println("Type 5 to find a combination route trip (option 1.2.2) ");
	    //1.2.3 - ACCOUNT FOR AVAILABLE SEATS
	    //sorting options allowed
	    /*
			1.2.4.1. Fewest stops   
			1.2.4.2. Run through most stations   
			1.2.4.3. Lowest price  
			1.2.4.4. Highest price  
			1.2.4.5. Least total time  
			1.2.4.6. Most total time  
			1.2.4.7. Least total distance  
			1.2.4.8. Most total distance
	    */
		//1.2.5 - REVERVE/BOOK A SPECIFIED CUSTOMER ALONG ALL LEGS OF THE ROUTE
		System.out.println("Type 0 to REVERVE/BOOK A SPECIFIED CUSTOMER ALONG ALL LEGS OF A ROUTE (option 1.2.5) ");
		System.out.println("Type 6 to find all the trains that pass through a specific station at a specific day/time combination (option 1.3.1) ");
		System.out.println("Type 7 to find the routes that travel more than one rail line (option 1.3.2)");
		System.out.println("Type 8 to find the routes that pass through the same stations but don't have the same stops (option 1.3.3)");
		System.out.println("Type 9 to find any stations through which all trains pass through (option 1.3.4)");
		System.out.println("Type 10 to find all trains that do not stop at a specific station (option 1.3.5)");
		System.out.println("Type 11 to find routes that stop at at least XX% of the Stations they visit (option 1.3.6)");
		System.out.println("Type 12 to find the schedule of a root (option 1.3.7)");
		System.out.println("Type 13 to find availability of a route at every stop on a specific day and time (option 1.3.8)");
		System.out.println("Type 14 to import delimiter ';'' separated text files into the database (Option 2.1)");
		System.out.println("Type 15 to export data from the tables in the database (Option 2.2)");
		System.out.println("Type 16 to DELETE all the rows from the ENTIRE database (Option 2.3)");
		System.out.println("Type 17 to EXIT the program. ");
	    Scanner in = new Scanner(System.in);
        int choice = in.nextInt();
        switch(choice){
			case 0:
			case 1:
			case 2:
			case 3:
			case 4:
			case 5:
			case 6:
			case 7:
			case 8:
			case 9:
			case 10:
			case 11:
			case 12:
			case 13:
			case 14:
			case 15:
			case 16:
	    		ExpressRailway demo = new ExpressRailway(choice);
	    		break;
			case 17:
				System.out.println("EXITING program.....");
	    		System.exit(0);
			default:
	    		System.out.println("Invalid choice enetered: " + choice);
	    		break;
		}    
	}
	catch(Exception Ex)  {
	    System.out.println("Error connecting to database.  Machine Error: " +
			       Ex.toString());
	}
	finally
	{
		connection.close();
	}
  }
}
