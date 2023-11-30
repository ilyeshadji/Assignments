package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import models.Database;
import models.User;

public class UserDao {
	public int createUser(String role, String password) throws ClassNotFoundException, SQLException {
		String CREATE_USER = "INSERT INTO user (role, password)\n" + "VALUES (?, ?);";

		int result = 0;

		Connection conn = Database.getConnection();

		PreparedStatement statement = conn.prepareStatement(CREATE_USER);
		statement.setString(1, role);
		statement.setString(2, password);

		result = statement.executeUpdate();

		Database.CloseConnection(conn);

		return result;
	}

	public User getUser(String password) throws ClassNotFoundException, SQLException {
		String FIND_USER = "SELECT * FROM User WHERE `password` = '" + password + "';";

		User user = null;

		Connection conn = Database.getConnection();

		try {
			Statement statement = conn.createStatement();
			ResultSet resultSet = statement.executeQuery(FIND_USER);

			if (resultSet.next()) {
				String role = resultSet.getString("role");
				String userPassword = resultSet.getString("password");
				String user_id = resultSet.getString("user_id");

				int convertedUserId = Integer.parseInt(user_id);

				user = new User(convertedUserId, role, userPassword);
			}

		} catch (SQLException e) {
			System.out.println("SQLException: " + e.getMessage());
			System.out.println("SQLState: " + e.getSQLState());
			System.out.println("VendorError: " + e.getErrorCode());
		}

		Database.CloseConnection(conn);

		return user;
	}

	public ArrayList<User> getUsers() throws SQLException {
		String GET_USERS = "SELECT * FROM user;";

		ArrayList<User> userList = new ArrayList<>();

		Connection conn = Database.getConnection();

		try {
			Statement statement = conn.createStatement();
			ResultSet resultSet = statement.executeQuery(GET_USERS);

			while (resultSet.next()) {
				int user_id = resultSet.getInt("user_id");
				String role = resultSet.getString("role");

				User user = new User(user_id, role);

				userList.add(user);
			}
		} catch (SQLException e) {
			System.out.println("SQLException: " + e.getMessage());
			System.out.println("SQLState: " + e.getSQLState());
			System.out.println("VendorError: " + e.getErrorCode());
		}

		Database.CloseConnection(conn);

		return userList;
	}

	public int updatePassword(int userId, String password) throws SQLException {
		String UPDATE_PASSWORD = "UPDATE User SET `password` = (?) WHERE `user_id` = (?);";

		int result = 0;

		Connection conn = Database.getConnection();

		PreparedStatement statement = conn.prepareStatement(UPDATE_PASSWORD);
		statement.setString(1, password);
		statement.setInt(2, userId);
		result = statement.executeUpdate();

		Database.CloseConnection(conn);

		return result;
	}

	public int updateRole(int user_id, String role) throws SQLException {
		String UPDATE_ORDER = "UPDATE User SET `role` = (?) WHERE `user_id` = (?);";

		int result = 0;

		Connection conn = Database.getConnection();

		PreparedStatement statement = conn.prepareStatement(UPDATE_ORDER);
		statement.setString(1, role);
		statement.setInt(2, user_id);
		result = statement.executeUpdate();

		Database.CloseConnection(conn);

		return result;
	}
}
