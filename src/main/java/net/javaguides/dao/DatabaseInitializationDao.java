package net.javaguides.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseInitializationDao {

	// TODO: write the data access stuff here
	static String url = "jdbc:mysql://127.0.0.1:3306/Assignment1?serverTimezone=UTC";
	static String username = "root";
	static String password = "xxxx";

	static Connection conn = null;

	static void createDatabaseConnection() throws ClassNotFoundException, SQLException {
		// Load the Oracle JDBC driver
		Class.forName("com.mysql.cj.jdbc.Driver");

		// Create a connection to the database
		conn = DriverManager.getConnection(url, username, password);
	}

	public int initializeDatabase() throws ClassNotFoundException, SQLException {
		String FETCH_DATABASE = "SELECT * FROM product;";

		String CREATE_TABLE_PRODUCT = "CREATE TABLE `Assignment1`.`Product` (\n" + "  `name` VARCHAR(33) NULL,\n"
				+ "  `description` VARCHAR(45) NULL,\n" + "  `vendor` VARCHAR(45) NULL,\n"
				+ "  `url` VARCHAR(45) NULL,\n" + "  `sku` VARCHAR(45) NOT NULL,\n" + "  `price` DECIMAL(10,2) NULL,\n"
				+ "  PRIMARY KEY (`sku`));";

		int result = 0;

		createDatabaseConnection();

		ResultSet response = null;

		try {
			Statement statement = conn.createStatement();
			response = statement.executeQuery(FETCH_DATABASE);

		} catch (SQLException e) {
			System.out.println("SQLException: " + e.getMessage());
			System.out.println("SQLState: " + e.getSQLState());
			System.out.println("VendorError: " + e.getErrorCode());
		}

		if (response == null) {
			try {
				Statement productStatement = conn.createStatement();
				result = productStatement.executeUpdate(CREATE_TABLE_PRODUCT);

			} catch (SQLException e) {
				System.out.println("SQLException: " + e.getMessage());
				System.out.println("SQLState: " + e.getSQLState());
				System.out.println("VendorError: " + e.getErrorCode());
			}

		}

		return result;
	}
}
