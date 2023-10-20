package net.javaguides.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import net.javaguides.model.Product;

public class CartDao {
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

	public ArrayList<Product> getCart(String user_id) throws ClassNotFoundException, SQLException {
		String GET_CART = "SELECT C.*, P.sku, P.price, P.vendor, P.name, P.description, P.url\n" + "FROM Cart C\n"
				+ "JOIN Product P ON C.product_id = P.sku;";

		createDatabaseConnection();

		ArrayList<Product> cart = new ArrayList<>();

		try {
			Statement statement = conn.createStatement();
			ResultSet resultSet = statement.executeQuery(GET_CART);

			while (resultSet.next()) {
				String sku = resultSet.getString("sku");
				String name = resultSet.getString("name");
				String description = resultSet.getString("description");
				String vendor = resultSet.getString("vendor");
				String url = resultSet.getString("url");

				double price = resultSet.getDouble("price");
				int quantity = resultSet.getInt("quantity");

				Product product = new Product(sku, name, description, vendor, url, price, quantity);

				cart.add(product);
			}
		} catch (SQLException e) {
			System.out.println("SQLException: " + e.getMessage());
			System.out.println("SQLState: " + e.getSQLState());
			System.out.println("VendorError: " + e.getErrorCode());
		}

		return cart;
	}

	public int addProductToCart(String user_id, String product_id, int quantity)
			throws ClassNotFoundException, SQLException {
		String ADD_PRODUCT_TO_CART = "INSERT INTO `Cart` (`user_id`, `product_id`, `quantity`)\n" + "VALUES (\n"
				+ "  (SELECT `user_id` FROM `User` WHERE `user_id` = '" + user_id + "'),\n"
				+ "  (SELECT `sku` FROM `Product` WHERE `sku` = '" + product_id + "'),\n" + " " + quantity + "\n"
				+ ");";

		int result = 0;

		createDatabaseConnection();

		Statement statement = conn.createStatement();
		statement.executeUpdate(ADD_PRODUCT_TO_CART);
		result = 1;

		return result;
	}

	public int deleteProductFromCart(String user_id, String product_id) throws ClassNotFoundException, SQLException {
		String DELETE_PRODUCT_FROM_CART = "DELETE FROM `Cart`\n"
				+ "WHERE `user_id` = (SELECT `user_id` FROM `User` WHERE `user_id` = '" + user_id + "')\n"
				+ "  AND `product_id` = (SELECT `sku` FROM `Product` WHERE `sku` = '" + product_id + "');";

		int result = 0;

		createDatabaseConnection();

		try {
			Statement statement = conn.createStatement();
			result = statement.executeUpdate(DELETE_PRODUCT_FROM_CART);

		} catch (SQLException e) {
			System.out.println("SQLException: " + e.getMessage());
			System.out.println("SQLState: " + e.getSQLState());
			System.out.println("VendorError: " + e.getErrorCode());
		}

		return result;
	}
}
