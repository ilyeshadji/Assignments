package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import models.Order;
import models.Product;

public class OrderDao {
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

	public static String createOrder(int user_id, ArrayList<Product> products, String shipping_address)
			throws ClassNotFoundException, SQLException {
		String CREATE_ORDER = "INSERT INTO `order` (shipping_address, user_id)\n" + "VALUES (?, ?);";
		int order_id = 0;

		if (products == null) {
			return "No products in the cart, could not create order";
		}

		createDatabaseConnection();

		// Creating the order
		PreparedStatement preparedStatement = conn.prepareStatement(CREATE_ORDER, Statement.RETURN_GENERATED_KEYS);
		preparedStatement.setString(1, shipping_address);
		preparedStatement.setInt(2, user_id);

		int affectedRows = preparedStatement.executeUpdate();

		// Retrieve auto-generated keys
		if (affectedRows > 0) {
			ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
			if (generatedKeys.next()) {
				order_id = generatedKeys.getInt(1);
			} else {
				return "Something went wrong. Try again.";
			}
		} else {
			return "Could not create the order. Try again.";
		}

		int result = 0;
		// Adding the products to the Order
		for (Product product : products) {
			String ADD_PRODUCT_TO_ORDER = "INSERT INTO orderProducts (order_id, sku, quantity) VALUES (?,?, ?);";

			PreparedStatement statement = conn.prepareStatement(ADD_PRODUCT_TO_ORDER);
			statement.setInt(1, order_id);
			statement.setString(2, product.getSku());
			statement.setInt(3, product.getQuantity());

			result = statement.executeUpdate();
		}

		if (result > 0) {
			return "success";
		}

		return "Something went wrong. Please try again";
	}

	public int shipOrder(int order_id) throws ClassNotFoundException, SQLException {
		String SHIP_ORDER = "UPDATE `order` SET tracking_number = (?) WHERE order_id = (?);";
		boolean hasUniqueTrackingNumber = false;
		int result = 0;
		int trackingNumber = 0;

		createDatabaseConnection();

		// Check the uniqueness of the tracking_number
		do {
			Random random = new Random();
			trackingNumber = random.nextInt(99999 - 10000) + 10000;

			String FIND_SAME_TRACKING_NUMBER = "SELECT * FROM `order` WHERE tracking_number = (?)";

			PreparedStatement statement = conn.prepareStatement(FIND_SAME_TRACKING_NUMBER);
			statement.setInt(1, trackingNumber);

			ResultSet orders = statement.executeQuery();

			if (!orders.next()) {
				hasUniqueTrackingNumber = true;
			}
		} while (hasUniqueTrackingNumber == false);

		PreparedStatement statement = conn.prepareStatement(SHIP_ORDER);
		statement.setInt(1, trackingNumber);
		statement.setInt(2, order_id);

		result = statement.executeUpdate();

		return result;
	}

	public ArrayList<Order> getOrders(int user_id)
			throws ClassNotFoundException, SQLException, JsonMappingException, JsonProcessingException {
		String FETCH_ORDERS_FROM_USER = "SELECT u.user_id, u.email AS user_email, o.order_id,\n"
				+ "       JSON_ARRAYAGG(JSON_OBJECT('sku', p.sku, 'url', p.url, 'name', p.name, 'quantity', op.quantity)) AS products,\n"
				+ "       SUM(p.price * op.quantity) AS total_price, \n"
				+ "       o.shipping_address, o.tracking_number\n" + "FROM User u\n"
				+ "JOIN `Order` o ON u.user_id = o.user_id\n"
				+ "LEFT JOIN orderProducts op ON o.order_id = op.order_id\n" + "LEFT JOIN Product p ON op.sku = p.sku\n"
				+ "WHERE u.user_id = (?)\n" + "GROUP BY u.user_id, o.order_id;";

		createDatabaseConnection();

		ArrayList<Order> orders = new ArrayList<>();

		try {
			PreparedStatement statement = conn.prepareStatement(FETCH_ORDERS_FROM_USER);
			statement.setInt(1, user_id);

			ResultSet resultSet = statement.executeQuery();

			while (resultSet.next()) {
				int userIdResult = resultSet.getInt("user_id");
				String userEmail = resultSet.getString("user_email");
				int orderId = resultSet.getInt("order_id");
				String productsJson = resultSet.getString("products");
				double totalPrice = resultSet.getDouble("total_price");
				String shippingAddress = resultSet.getString("shipping_address");
				int trackingNumber = resultSet.getInt("tracking_number");

				ObjectMapper objectMapper = new ObjectMapper();

				List<Product> productsList = objectMapper.readValue(productsJson, new TypeReference<List<Product>>() {
				});

				ArrayList<Product> products = new ArrayList<>(productsList);

				Order order = new Order(userIdResult, userEmail, orderId, products, totalPrice, shippingAddress,
						trackingNumber);

				orders.add(order);
			}
		} catch (SQLException e) {
			System.out.println("SQLException: " + e.getMessage());
			System.out.println("SQLState: " + e.getSQLState());
			System.out.println("VendorError: " + e.getErrorCode());
		}

		return orders;
	}

	public ArrayList<Order> getOrders()
			throws ClassNotFoundException, SQLException, JsonMappingException, JsonProcessingException {
		String FETCH_ALL_ORDERS = "SELECT u.user_id, u.email AS user_email, o.order_id,\n"
				+ "       JSON_ARRAYAGG(JSON_OBJECT('sku', p.sku, 'url', p.url, 'name', p.name, 'quantity', op.quantity)) AS products,\n"
				+ "       SUM(p.price * op.quantity) AS total_price, \n"
				+ "       o.shipping_address, o.tracking_number\n" + "FROM User u\n"
				+ "JOIN `Order` o ON u.user_id = o.user_id\n"
				+ "LEFT JOIN orderProducts op ON o.order_id = op.order_id\n" + "LEFT JOIN Product p ON op.sku = p.sku\n"
				+ "GROUP BY u.user_id, o.order_id;";

		createDatabaseConnection();

		ArrayList<Order> orders = new ArrayList<>();

		try {
			PreparedStatement statement = conn.prepareStatement(FETCH_ALL_ORDERS);
			ResultSet resultSet = statement.executeQuery();

			while (resultSet.next()) {
				int userIdResult = resultSet.getInt("user_id");
				String userEmail = resultSet.getString("user_email");
				int orderId = resultSet.getInt("order_id");
				String productsJson = resultSet.getString("products");
				double totalPrice = resultSet.getDouble("total_price");
				String shippingAddress = resultSet.getString("shipping_address");
				int trackingNumber = resultSet.getInt("tracking_number");

				ObjectMapper objectMapper = new ObjectMapper();

				ArrayList<Product> products = objectMapper.readValue(productsJson,
						new TypeReference<ArrayList<Product>>() {
						});

				Order order = new Order(userIdResult, userEmail, orderId, products, totalPrice, shippingAddress,
						trackingNumber);

				orders.add(order);
			}
		} catch (SQLException e) {
			System.out.println("SQLException: " + e.getMessage());
			System.out.println("SQLState: " + e.getSQLState());
			System.out.println("VendorError: " + e.getErrorCode());
		}

		return orders;
	}

	public Order getOrder(int order_id)
			throws ClassNotFoundException, SQLException, JsonMappingException, JsonProcessingException {
		String FETCH_ORDER_BY_ID = "SELECT u.user_id, u.email AS user_email, o.order_id,\n"
				+ "       JSON_ARRAYAGG(JSON_OBJECT('sku', p.sku, 'url', p.url, 'name', p.name, 'quantity', op.quantity)) AS products,\n"
				+ "       SUM(p.price * op.quantity) AS total_price, \n"
				+ "       o.shipping_address, o.tracking_number\n" + "FROM User u\n"
				+ "JOIN `Order` o ON u.user_id = o.user_id\n"
				+ "LEFT JOIN orderProducts op ON o.order_id = op.order_id\n" + "LEFT JOIN Product p ON op.sku = p.sku\n"
				+ "WHERE o.order_id = (?)\n" + "GROUP BY u.user_id, o.order_id;";

		createDatabaseConnection();

		Order order = null;

		try {
			PreparedStatement statement = conn.prepareStatement(FETCH_ORDER_BY_ID);
			statement.setInt(1, order_id);

			ResultSet resultSet = statement.executeQuery();

			while (resultSet.next()) {
				int userIdResult = resultSet.getInt("user_id");
				String userEmail = resultSet.getString("user_email");
				int orderId = resultSet.getInt("order_id");
				String productsJson = resultSet.getString("products");
				double totalPrice = resultSet.getDouble("total_price");
				String shippingAddress = resultSet.getString("shipping_address");
				int trackingNumber = resultSet.getInt("tracking_number");

				ObjectMapper objectMapper = new ObjectMapper();

				ArrayList<Product> products = objectMapper.readValue(productsJson,
						new TypeReference<ArrayList<Product>>() {
						});

				order = new Order(userIdResult, userEmail, orderId, products, totalPrice, shippingAddress,
						trackingNumber);
			}
		} catch (SQLException e) {
			System.out.println("SQLException: " + e.getMessage());
			System.out.println("SQLState: " + e.getSQLState());
			System.out.println("VendorError: " + e.getErrorCode());
		}

		return order;
	}
}
