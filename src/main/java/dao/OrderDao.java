package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import models.Database;
import models.Order;
import models.Product;

public class OrderDao {
	public static String createOrder(int user_id, ArrayList<Product> products, String shipping_address)
			throws ClassNotFoundException, SQLException {
		String CREATE_ORDER = "INSERT INTO `order` (shipping_address, user_id)\n" + "VALUES (?, ?);";
		int order_id = 0;

		if (products == null) {
			return "No products in the cart, could not create order";
		}

		Connection conn = Database.getConnection();

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

		Database.CloseConnection(conn);

		return "Something went wrong. Please try again";
	}

	public int shipOrder(int order_id) throws ClassNotFoundException, SQLException {
		String SHIP_ORDER = "UPDATE `order` SET tracking_number = (?) WHERE order_id = (?);";
		boolean hasUniqueTrackingNumber = false;
		int result = 0;
		int trackingNumber = 0;

		Connection conn = Database.getConnection();

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

		Database.CloseConnection(conn);

		return result;
	}

	public List<Order> getOrders(int user_id)
			throws ClassNotFoundException, SQLException, JsonMappingException, JsonProcessingException {
		String FETCH_ORDERS_FROM_USER = "SELECT\n" + "    u.user_id,\n" + "    o.order_id,\n"
				+ "    '[' || GROUP_CONCAT('{\"sku\": \"' || p.sku || '\", \"url\": \"' || p.url || '\", \"name\": \"' || REPLACE(p.name, '\"', '\\\"') || '\", \"quantity\": ' || op.quantity || ', \"price\": ' || p.price || '}') || ']' AS products,\n"
				+ "    SUM(p.price * op.quantity) AS total_price,\n" + "    o.shipping_address,\n"
				+ "    o.tracking_number\n" + "FROM\n" + "    User u\n" + "JOIN `Order` o ON u.user_id = o.user_id\n"
				+ "LEFT JOIN orderProducts op ON o.order_id = op.order_id\n" + "LEFT JOIN Product p ON op.sku = p.sku\n"
				+ "WHERE\n" + "    u.user_id = ?\n" + "GROUP BY\n" + "    u.user_id, o.order_id;";

		Connection conn = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;

		List<Order> orders = new ArrayList<>();
		Map<Integer, Order> orderMap = new HashMap<>();

		try {
			conn = Database.getConnection();
			statement = conn.prepareStatement(FETCH_ORDERS_FROM_USER);

			statement.setInt(1, user_id);

			resultSet = statement.executeQuery();

			while (resultSet.next()) {
				int orderId = resultSet.getInt("order_id");
				String shippingAddress = resultSet.getString("shipping_address");
				int trackingNumber = resultSet.getInt("tracking_number");

				String productsJson = resultSet.getString("products");

				ObjectMapper objectMapper = new ObjectMapper();

				List<Product> productsList = objectMapper.readValue(productsJson, new TypeReference<List<Product>>() {
				});

				// Iterate through products and add them to the order
				for (Product product : productsList) {
					// Check if the order already exists in the map
					if (orderMap.containsKey(orderId)) {
						// If yes, add the product to the existing order
						Order existingOrder = orderMap.get(orderId);
						existingOrder.addProduct(product);
					} else {
						// If no, create a new order and add it to the map
						Order order = new Order(orderId, shippingAddress, trackingNumber,
								resultSet.getDouble("total_price"));
						order.addProduct(product);
						orderMap.put(orderId, order);
					}
				}
			}
		} catch (

		SQLException e) {
			e.printStackTrace();
		} finally {
			Database.CloseConnection(conn);
		}

		// Convert the map values to a list of orders
		orders.addAll(orderMap.values());

		return orders;
	}

	public List<Order> getOrders()
			throws ClassNotFoundException, SQLException, JsonMappingException, JsonProcessingException {
		String FETCH_ORDERS_WITH_PRODUCTS = "SELECT " + "o.order_id, " + "p.name AS product_name, "
				+ "p.description AS product_description, " + "p.vendor AS product_vendor, " + "p.url AS product_url, "
				+ "p.sku AS product_sku, " + "op.quantity AS product_quantity, " + "p.price AS product_price, "
				+ "(p.price * op.quantity) AS total_product_price, " + "o.shipping_address, " + "o.tracking_number "
				+ "FROM " + "`Order` o " + "JOIN orderProducts op ON o.order_id = op.order_id "
				+ "JOIN Product p ON op.sku = p.sku";

		return OrderDao.getAllOrders(FETCH_ORDERS_WITH_PRODUCTS, 0);
	}

	public Order getOrder(int order_id)
			throws ClassNotFoundException, SQLException, JsonMappingException, JsonProcessingException {
		String FETCH_ORDER_BY_ID = "SELECT\n" + "    COALESCE(u.user_id, 'unknown') AS user_id,\n" + "    o.order_id,\n"
				+ "    JSON_GROUP_ARRAY(\n" + "        JSON_OBJECT(\n" + "            'sku', p.sku,\n"
				+ "            'url', p.url,\n" + "            'name', p.name,\n"
				+ "            'quantity', op.quantity\n" + "        )\n" + "    ) AS products,\n"
				+ "    SUM(p.price * op.quantity) AS total_price,\n" + "    o.shipping_address,\n"
				+ "    o.tracking_number\n" + "FROM \"Order\" o\n" + "LEFT JOIN \"User\" u ON u.user_id = o.user_id\n"
				+ "LEFT JOIN orderProducts op ON o.order_id = op.order_id\n" + "LEFT JOIN Product p ON op.sku = p.sku\n"
				+ "WHERE o.order_id = ?\n" + "GROUP BY COALESCE(u.user_id, 'unknown'), o.order_id;\n" + "";

		Connection conn = Database.getConnection();

		Order order = null;

		try {
			PreparedStatement statement = conn.prepareStatement(FETCH_ORDER_BY_ID);
			statement.setInt(1, order_id);

			ResultSet resultSet = statement.executeQuery();

			while (resultSet.next()) {
				int userIdResult = resultSet.getInt("user_id");
				int orderId = resultSet.getInt("order_id");
				String productsJson = resultSet.getString("products");
				double totalPrice = resultSet.getDouble("total_price");
				String shippingAddress = resultSet.getString("shipping_address");
				int trackingNumber = resultSet.getInt("tracking_number");

				ObjectMapper objectMapper = new ObjectMapper();

				ArrayList<Product> products = objectMapper.readValue(productsJson,
						new TypeReference<ArrayList<Product>>() {
						});

				order = new Order(userIdResult, orderId, products, totalPrice, shippingAddress, trackingNumber);
			}
		} catch (SQLException e) {
			System.out.println("SQLException: " + e.getMessage());
			System.out.println("SQLState: " + e.getSQLState());
			System.out.println("VendorError: " + e.getErrorCode());
		}

		Database.CloseConnection(conn);

		return order;
	}

	public static List<Order> getAllOrders(String query, int userId) throws SQLException {
		Connection conn = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;

		List<Order> orders = new ArrayList<>();
		Map<Integer, Order> orderMap = new HashMap<>();

		try {
			conn = Database.getConnection();
			statement = conn.prepareStatement(query);

			if (userId != 0) {
				statement.setInt(1, userId);
			}

			resultSet = statement.executeQuery();

			while (resultSet.next()) {
				int orderId = resultSet.getInt("order_id");
				String shippingAddress = resultSet.getString("shipping_address");
				int trackingNumber = resultSet.getInt("tracking_number");

				int productQuantity = resultSet.getInt("product_quantity");
				double productPrice = resultSet.getDouble("product_price");
				double totalProductPrice = resultSet.getDouble("total_product_price");

				String productName = resultSet.getString("product_name");
				String productDescription = resultSet.getString("product_description");
				String productVendor = resultSet.getString("product_vendor");
				String productUrl = resultSet.getString("product_url");
				String productSku = resultSet.getString("product_sku");

				Product product = new Product(productSku, productName, productDescription, productVendor, productUrl,
						productPrice, productQuantity);

				// Check if the order already exists in the map
				if (orderMap.containsKey(orderId)) {
					// If yes, add the product to the existing order
					Order existingOrder = orderMap.get(orderId);
					existingOrder.addProduct(product);
				} else {
					// If no, create a new order and add it to the map
					Order order = new Order(orderId, shippingAddress, trackingNumber, totalProductPrice);
					order.addProduct(product);
					orderMap.put(orderId, order);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			Database.CloseConnection(conn);
		}

		// Convert the map values to a list of orders
		orders.addAll(orderMap.values());

		return orders;
	}
}
