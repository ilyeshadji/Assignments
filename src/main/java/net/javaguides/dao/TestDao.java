package net.javaguides.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import net.javaguides.model.Test;

public class TestDao {

	// TODO: write the data access stuff here
	static String url="jdbc:mysql://127.0.0.1:3306/Assignment1?serverTimezone=UTC";
	static String username="root";
	static String password="xxxx";
	
	static Connection conn = null;
	
	static void createDatabaseConnection() throws ClassNotFoundException, SQLException {
	    // Load the Oracle JDBC driver
	    Class.forName("com.mysql.cj.jdbc.Driver");

	    // Create a connection to the database
	    conn = DriverManager.getConnection(url, username, password);
	}
		
	public int registerTest(Test test) throws ClassNotFoundException, SQLException {
		String INSERT_TEST_SQL = "INSERT INTO test" + " (id) VALUES" + "(" + test.getId() + ");";
		
		int result = 0;
		
		createDatabaseConnection();
		
		try(
			PreparedStatement preparedStatement = conn.prepareStatement(INSERT_TEST_SQL);	
		){
			System.out.println("Inserted test with id -> " + test.getId());
			result = preparedStatement.executeUpdate();
		}
		catch(SQLException e) {
		    System.out.println("SQLException: " + e.getMessage());
		    System.out.println("SQLState: " + e.getSQLState());
		    System.out.println("VendorError: " + e.getErrorCode());		
		}
		
		return result;
	}
}
