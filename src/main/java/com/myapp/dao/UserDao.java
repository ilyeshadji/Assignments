package com.myapp.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.myapp.models.User;

public class UserDao {
	static String url = System.getenv("DB_URL");
	static String username = System.getenv("DB_USERNAME");
	static String password = System.getenv("DB_PASSWORD");

	static Connection conn = null;

	static void createDatabaseConnection() throws ClassNotFoundException, SQLException {
		// Load the Oracle JDBC driver
		Class.forName("com.mysql.cj.jdbc.Driver");

		// Create a connection to the database
		conn = DriverManager.getConnection(url, username, password);
	}

	public static int createUser(String role, String email, String password)
			throws ClassNotFoundException, SQLException {
		String CREATE_USER = "INSERT INTO user (role, email, password)\n" + "VALUES ('" + role + "', '" + email + "', '"
				+ password + "');";

		int result = 0;

		createDatabaseConnection();

		Statement statement = conn.createStatement();
		statement.executeUpdate(CREATE_USER);
		result = 1;

		return result;
	}

	public static User getUser(String email, String password) throws ClassNotFoundException, SQLException {
		String FIND_USER = "SELECT * FROM User WHERE `email` = '" + email + "';";

		User user = null;

		createDatabaseConnection();

		try {
			Statement statement = conn.createStatement();
			ResultSet resultSet = statement.executeQuery(FIND_USER);

			if (resultSet.next()) {
				String user_id = resultSet.getString("user_id");
				String role = resultSet.getString("role");
				String userPassword = resultSet.getString("password");

				user = new User(user_id, role, userPassword);
			}

		} catch (SQLException e) {
			System.out.println("SQLException: " + e.getMessage());
			System.out.println("SQLState: " + e.getSQLState());
			System.out.println("VendorError: " + e.getErrorCode());
		}

		return user;
	}
}
