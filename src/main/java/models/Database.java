package models;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {

	public static Connection getConnection() throws SQLException {
		Connection conn = null;

		try {
			// Load the SQLite JDBC driver
			Class.forName("org.sqlite.JDBC");

			// Construct the database URL
			String url = "jdbc:sqlite:" + System.getenv("SRC_PATH") + "/database/Assignments.db";

			// Obtain a connection
			conn = DriverManager.getConnection(url);
		} catch (ClassNotFoundException e) {
			System.out.println("SQLite JDBC driver not found.");
			e.printStackTrace();
		}

		return conn;
	}
}
