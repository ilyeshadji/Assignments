package dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import models.Database;

public class DatabaseInitializationDao {
	public int initializeDatabase() throws ClassNotFoundException, SQLException {
		String CREATE_TABLE_PRODUCT = "CREATE TABLE IF NOT EXISTS Product (\n" + "  `name` VARCHAR(33) NULL,\n"
				+ "  `description` LONGTEXT NULL,\n" + "  `vendor` VARCHAR(45) NULL,\n" + "  `url` VARCHAR(45) NULL,\n"
				+ "  `sku` VARCHAR(45) NOT NULL,\n" + "  `price` DECIMAL(10,2) NULL,\n" + "  PRIMARY KEY (`sku`));";

		String CREATE_TABLE_USER = "CREATE TABLE IF NOT EXISTS User (\n"
				+ "  `user_id` INTEGER PRIMARY KEY AUTOINCREMENT,\n" + "  `role` VARCHAR(10) NOT NULL,\n"
				+ "`password` VARCHAR(300) NOT NULL);";

		String CREATE_TABLE_CART = "CREATE TABLE IF NOT EXISTS Cart (" + "`user_id` INTEGER NOT NULL,\n"
				+ "`product_id` VARCHAR(45) NOT NULL, \n" + "`quantity` INTEGER NOT NULL,"
				+ "PRIMARY KEY (user_id, product_id)," + "FOREIGN KEY (user_id) REFERENCES User(user_id),"
				+ "FOREIGN KEY (product_id) REFERENCES Product(sku)" + ");";

		String CREATE_TABLE_ORDER = "CREATE TABLE IF NOT EXISTS `Order` (\n"
				+ "    `order_id` INTEGER PRIMARY KEY AUTOINCREMENT,\n"
				+ "    `shipping_address` VARCHAR(100) NOT NULL,\n" + "    `tracking_number` INTEGER UNIQUE,\n"
				+ "    `user_id` INTEGER NOT NULL,\n" + "    FOREIGN KEY (`user_id`) REFERENCES `User`(`user_id`)\n"
				+ ");";

		String CREATE_TABLE_ORDER_PRODUCTS = "CREATE TABLE IF NOT EXISTS orderProducts ("
				+ "`order_id` INTEGER NOT NULL,\n" + "`sku` VARCHAR(45) NOT NULL,\n" + "`quantity` INTEGER NOT NULL,"
				+ "PRIMARY KEY (order_id, sku)," + "FOREIGN KEY (order_id) REFERENCES `Order` (order_id),"
				+ " FOREIGN KEY (sku) REFERENCES `Product` (sku));";

		String INSERT_FIRST_ORDER_EXAMPLE = "INSERT INTO `Order` (`shipping_address`, `tracking_number`, `user_id`)\n"
				+ "VALUES ('Sample Address', 123456, 10000);";

		String INSERT_FIRT_STAFF_MEMBER = "INSERT INTO `User` (`user_id`, `role`, `password`) VALUES (1, 'staff', 'secret')";
		String INSERT_NOT_LOGGED_IN_USER = "INSERT INTO `User` (`user_id`, `role`, `password`) VALUES (0, 'customer', '')";

		int result = 0;

		this.tableInitialization(CREATE_TABLE_PRODUCT, false);
		this.tableInitialization(CREATE_TABLE_USER, false);
		this.tableInitialization(CREATE_TABLE_ORDER, false);
		this.tableInitialization(INSERT_FIRST_ORDER_EXAMPLE, true);
		this.tableInitialization(INSERT_FIRT_STAFF_MEMBER, true);
		this.tableInitialization(INSERT_NOT_LOGGED_IN_USER, true);
		this.tableInitialization(CREATE_TABLE_ORDER_PRODUCTS, false);
		result = this.tableInitialization(CREATE_TABLE_CART, false);

		return result;
	}

	public int tableInitialization(String create, boolean ignoreError) throws ClassNotFoundException, SQLException {
		int result = 0;

		try (Connection conn = Database.getConnection(); Statement stmt = conn.createStatement()) {
			stmt.execute(create);
		} catch (SQLException e) {
			if (!ignoreError) {
				System.out.println(e.getMessage());
			}
		}

		return result;
	}
}
