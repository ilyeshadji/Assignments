package com.myapp.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.myapp.models.Cart;
import com.myapp.models.Product;

public class CartDao {
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

	public Cart getCart(int user_id) throws ClassNotFoundException, SQLException {
		String GET_CART = "SELECT C.*, P.sku, P.price, P.vendor, P.name, P.description, P.url\n" + "FROM Cart C\n"
				+ "JOIN Product P ON C.product_id = P.sku WHERE `user_id` = " + user_id + ";";

		createDatabaseConnection();

		ArrayList<Product> products = new ArrayList<>();

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

				products.add(product);
			}
		} catch (SQLException e) {
			System.out.println("SQLException: " + e.getMessage());
			System.out.println("SQLState: " + e.getSQLState());
			System.out.println("VendorError: " + e.getErrorCode());
		}

		Cart cart = new Cart(user_id, products);

		if (cart.getNumberOfProducts() < 1) {
			return null;
		}

		return cart;
	}

	public int addProductToCart(int user_id, String product_id, int quantity)
			throws ClassNotFoundException, SQLException {
		String ADD_PRODUCT_TO_CART = "INSERT INTO `Cart` (`user_id`, `product_id`, `quantity`)\n" + "VALUES (\n"
				+ "  (SELECT `user_id` FROM `User` WHERE `user_id` = " + user_id + "),\n"
				+ "  (SELECT `sku` FROM `Product` WHERE `sku` = '" + product_id + "'),\n" + " " + quantity + "\n"
				+ ");";

		int result = 0;
		String userRole = null;

		createDatabaseConnection();

		// to prevent adding products to a staff user
		String FETCH_USER = "SELECT * FROM user WHERE user_id = (?)";

		PreparedStatement preparedStatement = conn.prepareStatement(FETCH_USER);
		preparedStatement.setInt(1, user_id);

		ResultSet user = preparedStatement.executeQuery();

		while (user.next()) {
			userRole = user.getString("role");
		}

		if (userRole.equals("staff")) {
			return 0;
		}

		Statement statement = conn.createStatement();
		statement.executeUpdate(ADD_PRODUCT_TO_CART);
		result = 1;

		return result;
	}

	public int deleteProductFromCart(int user_id, String product_id) throws ClassNotFoundException, SQLException {
		String DELETE_PRODUCT_FROM_CART = "DELETE FROM `Cart`\n"
				+ "WHERE `user_id` = (SELECT `user_id` FROM `User` WHERE `user_id` = " + user_id + ")\n"
				+ "  AND `product_id` = (SELECT `sku` FROM `Product` WHERE `sku` = '" + product_id + "');";

		int result = 0;

		createDatabaseConnection();

		try {
			Statement statement = conn.createStatement();
			result = statement.executeUpdate(DELETE_PRODUCT_FROM_CART);
			result = 1;
		} catch (SQLException e) {
			System.out.println("SQLException: " + e.getMessage());
			System.out.println("SQLState: " + e.getSQLState());
			System.out.println("VendorError: " + e.getErrorCode());
		}

		return result;
	}

	public int clearCart(int user_id) throws ClassNotFoundException, SQLException {
		String REMOVE_ALL_PRODUCTS = "DELETE FROM Cart WHERE `user_id` = (?);";

		createDatabaseConnection();

		int result = 0;

		// Creating the order
		PreparedStatement preparedStatement = conn.prepareStatement(REMOVE_ALL_PRODUCTS);
		preparedStatement.setInt(1, user_id);

		result = preparedStatement.executeUpdate();

		return result;
	}

	public int updateProductQuantityInCart(int user_id, String sku, int quantity)
			throws SQLException, ClassNotFoundException {
		String UPDATE_PRODUCT_QUANTITY = "UPDATE cart SET quantity = (?) WHERE `user_id` = (?) AND `sku` = (?);";

		createDatabaseConnection();

		int result = 0;

		PreparedStatement preparedStatement = conn.prepareStatement(UPDATE_PRODUCT_QUANTITY);
		preparedStatement.setInt(1, quantity);
		preparedStatement.setInt(2, user_id);
		preparedStatement.setString(3, sku);

		result = preparedStatement.executeUpdate();

		return result;
	}
}
