package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import controllers.AuthenticationServlet;

public class DatabaseInitializationDao {

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

	public int initializeDatabase() throws ClassNotFoundException, SQLException {
		String FETCH_PRODUCTS = "SELECT * FROM product;";
		String FETCH_CARTS = "SELECT * FROM cart;";
		String FETCH_USERS = "SELECT * FROM user";
		String FETCH_ORDERS = "SELECT * FROM `order`";
		String FETCH_ORDERS_PRODUCTS = "SELECT * FROM orderProducts";

		String CREATE_TABLE_PRODUCT = "CREATE TABLE `Assignments`.`Product` (\n" + "  `name` VARCHAR(33) NULL,\n"
				+ "  `description` LONGTEXT NULL,\n" + "  `vendor` VARCHAR(45) NULL,\n" + "  `url` VARCHAR(45) NULL,\n"
				+ "  `sku` VARCHAR(45) NOT NULL,\n" + "  `price` DECIMAL(10,2) NULL,\n" + "  PRIMARY KEY (`sku`));";

		String CREATE_TABLE_USER = "CREATE TABLE `Assignments`.`User` (\n" + "  `user_id` INT AUTO_INCREMENT,\n"
				+ "  `role` VARCHAR(10) NOT NULL,\n" + " `email` VARCHAR(100) NOT NULL UNIQUE,\n"
				+ "`password` VARCHAR(300) NOT NULL,\n" + "  PRIMARY KEY (`user_id`));";

		String CREATE_TABLE_CART = "CREATE TABLE `Assignments`.`Cart` (" + "`user_id` INT NOT NULL,\n"
				+ "`product_id` VARCHAR(45) NOT NULL, \n" + "`quantity` INT NOT NULL,"
				+ "PRIMARY KEY (user_id, product_id)," + "FOREIGN KEY (user_id) REFERENCES User(user_id),"
				+ "FOREIGN KEY (product_id) REFERENCES Product(sku)" + ");";

		String CREATE_TABLE_ORDER = "CREATE TABLE `Assignments`.`Order` (" + "`order_id` INT AUTO_INCREMENT,\n"
				+ "`shipping_address` VARCHAR(100) NOT NULL,\n"
				+ "`tracking_number` INT UNIQUE, `user_id` INT NOT NULL, FOREIGN KEY (user_id) REFERENCES User(user_id),\n"
				+ "PRIMARY KEY (`order_id`)) AUTO_INCREMENT = 10000;";

		String CREATE_TABLE_ORDER_PRODUCTS = "CREATE TABLE `Assignments`.`orderProducts` ("
				+ "`order_id` INT NOT NULL,\n" + "`sku` VARCHAR(45) NOT NULL,\n" + "`quantity` INT NOT NULL,"
				+ "PRIMARY KEY (order_id, sku)," + "FOREIGN KEY (order_id) REFERENCES `Order` (order_id),"
				+ " FOREIGN KEY (sku) REFERENCES `Product` (sku));";

		int result = 0;

		createDatabaseConnection();

		this.tableInitialization(FETCH_PRODUCTS, CREATE_TABLE_PRODUCT);
		this.tableInitialization(FETCH_USERS, CREATE_TABLE_USER);
		this.tableInitialization(FETCH_ORDERS, CREATE_TABLE_ORDER);
		this.tableInitialization(FETCH_ORDERS_PRODUCTS, CREATE_TABLE_ORDER_PRODUCTS);
		result = this.tableInitialization(FETCH_CARTS, CREATE_TABLE_CART);

		return result;
	}

	public int tableInitialization(String fetch, String create) throws ClassNotFoundException, SQLException {
		int result = 0;

		ResultSet response = null;

		try {
			Statement statement = conn.createStatement();
			response = statement.executeQuery(fetch);

		} catch (SQLException e) {
			System.out.println("SQLException: " + e.getMessage());
			System.out.println("SQLState: " + e.getSQLState());
			System.out.println("VendorError: " + e.getErrorCode());
		}

		if (response == null) {
			try {
				Statement productStatement = conn.createStatement();
				result = productStatement.executeUpdate(create);

			} catch (SQLException e) {
				System.out.println("SQLException: " + e.getMessage());
				System.out.println("SQLState: " + e.getSQLState());
				System.out.println("VendorError: " + e.getErrorCode());
			}

			// Creating the staff account
			if (fetch.equals("SELECT * FROM user")) {
				UserDao.createUser("staff", "staff@myapp.com", AuthenticationServlet.hashPassword("secret"));
			}
		}

		return result;
	}
}
