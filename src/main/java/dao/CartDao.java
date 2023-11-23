package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import models.Cart;
import models.Database;
import models.Product;

public class CartDao {
	public Cart getCart(int user_id) throws ClassNotFoundException, SQLException {
		String GET_CART = "SELECT C.*, P.sku, P.price, P.vendor, P.name, P.description, P.url\n" + "FROM Cart C\n"
				+ "JOIN Product P ON C.product_id = P.sku WHERE `user_id` = " + user_id + ";";

		ArrayList<Product> products = new ArrayList<>();

		try (Connection conn = Database.getConnection();
				Statement statement = conn.createStatement();
				ResultSet resultSet = statement.executeQuery(GET_CART)) {

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
		String ADD_PRODUCT_TO_CART = "INSERT INTO `Cart` (`user_id`, `product_id`, `quantity`)\n" + "VALUES (?,?,?);";

		int result = 0;

		Connection conn = Database.getConnection();

		PreparedStatement statement = conn.prepareStatement(ADD_PRODUCT_TO_CART);
		statement.setInt(1, user_id);
		statement.setString(2, product_id);
		statement.setInt(3, quantity);
		result = statement.executeUpdate();

		return result;
	}

	public int deleteProductFromCart(int user_id, String product_id) throws ClassNotFoundException, SQLException {
		String DELETE_PRODUCT_FROM_CART = "DELETE FROM `Cart`\n"
				+ "WHERE `user_id` = (SELECT `user_id` FROM `User` WHERE `user_id` = " + user_id + ")\n"
				+ "  AND `product_id` = (SELECT `sku` FROM `Product` WHERE `sku` = '" + product_id + "');";

		int result = 0;

		Connection conn = Database.getConnection();

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

		Connection conn = Database.getConnection();

		int result = 0;

		// Creating the order
		PreparedStatement preparedStatement = conn.prepareStatement(REMOVE_ALL_PRODUCTS);
		preparedStatement.setInt(1, user_id);

		result = preparedStatement.executeUpdate();

		return result;
	}

	public int updateProductQuantityInCart(int user_id, String sku, int quantity)
			throws SQLException, ClassNotFoundException {
		String UPDATE_PRODUCT_QUANTITY = "UPDATE cart SET quantity = (?) WHERE `user_id` = (?) AND `product_id` = (?);";

		Connection conn = Database.getConnection();

		int result = 0;

		PreparedStatement preparedStatement = conn.prepareStatement(UPDATE_PRODUCT_QUANTITY);
		preparedStatement.setInt(1, quantity);
		preparedStatement.setInt(2, user_id);
		preparedStatement.setString(3, sku);

		result = preparedStatement.executeUpdate();

		return result;
	}
}
