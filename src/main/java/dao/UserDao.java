package dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import models.Database;
import models.User;

public class UserDao {
	public static int createUser(String role, String email, String password)
			throws ClassNotFoundException, SQLException {
		String CREATE_USER = "INSERT INTO user (role, email, password)\n" + "VALUES ('" + role + "', '" + email + "', '"
				+ password + "');";

		int result = 0;

		Connection conn = Database.getConnection();

		Statement statement = conn.createStatement();
		statement.executeUpdate(CREATE_USER);
		result = 1;

		return result;
	}

	public static User getUser(String email, String password) throws ClassNotFoundException, SQLException {
		String FIND_USER = "SELECT * FROM User WHERE `email` = '" + email + "';";

		User user = null;

		Connection conn = Database.getConnection();

		try {
			Statement statement = conn.createStatement();
			ResultSet resultSet = statement.executeQuery(FIND_USER);

			if (resultSet.next()) {
				int user_id = Integer.parseInt(resultSet.getString("user_id"));
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
