package dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import controllers.AuthenticationServlet;
import models.Database;

public class DatabaseInitializationDao {
	public int initializeDatabase() throws ClassNotFoundException, SQLException {
		String CREATE_TABLE_PRODUCT = "CREATE TABLE IF NOT EXISTS Product (\n" + "  `name` VARCHAR(33) NULL,\n"
				+ "  `description` LONGTEXT NULL,\n" + "  `vendor` VARCHAR(45) NULL,\n" + "  `url` VARCHAR(45) NULL,\n"
				+ "  `sku` VARCHAR(45) NOT NULL,\n" + "  `price` DECIMAL(10,2) NULL,\n" + "  PRIMARY KEY (`sku`));";

		String CREATE_TABLE_USER = "CREATE TABLE IF NOT EXISTS User (\n" + "  `user_id` INT AUTO_INCREMENT,\n"
				+ "  `role` VARCHAR(10) NOT NULL,\n" + " `email` VARCHAR(100) NOT NULL UNIQUE,\n"
				+ "`password` VARCHAR(300) NOT NULL,\n" + "  PRIMARY KEY (`user_id`));";

		String CREATE_TABLE_CART = "CREATE TABLE IF NOT EXISTS Cart (" + "`user_id` INT NOT NULL,\n"
				+ "`product_id` VARCHAR(45) NOT NULL, \n" + "`quantity` INT NOT NULL,"
				+ "PRIMARY KEY (user_id, product_id)," + "FOREIGN KEY (user_id) REFERENCES User(user_id),"
				+ "FOREIGN KEY (product_id) REFERENCES Product(sku)" + ");";

		String CREATE_TABLE_ORDER = "CREATE TABLE IF NOT EXISTS `Order` (\n"
				+ "    `order_id` INTEGER PRIMARY KEY AUTOINCREMENT,\n"
				+ "    `shipping_address` VARCHAR(100) NOT NULL,\n" + "    `tracking_number` INTEGER UNIQUE,\n"
				+ "    `user_id` INTEGER NOT NULL,\n" + "    FOREIGN KEY (`user_id`) REFERENCES `User`(`user_id`)\n"
				+ ");";

		String INSERT_FIRST_ORDER_EXAMPLE = "INSERT INTO `Order` (`shipping_address`, `tracking_number`, `user_id`)\n"
				+ "VALUES ('Sample Address', 123456, 10000);";

		String CREATE_TABLE_ORDER_PRODUCTS = "CREATE TABLE IF NOT EXISTS orderProducts (" + "`order_id` INT NOT NULL,\n"
				+ "`sku` VARCHAR(45) NOT NULL,\n" + "`quantity` INT NOT NULL," + "PRIMARY KEY (order_id, sku),"
				+ "FOREIGN KEY (order_id) REFERENCES `Order` (order_id),"
				+ " FOREIGN KEY (sku) REFERENCES `Product` (sku));";

		int result = 0;

		this.tableInitialization(CREATE_TABLE_PRODUCT, false);
		this.tableInitialization(CREATE_TABLE_USER, false);
		this.tableInitialization(CREATE_TABLE_ORDER, false);
		this.tableInitialization(INSERT_FIRST_ORDER_EXAMPLE, true);
		this.tableInitialization(CREATE_TABLE_ORDER_PRODUCTS, false);
		result = this.tableInitialization(CREATE_TABLE_CART, false);

		return result;
	}

	public int tableInitialization(String create, boolean ignoreError) throws ClassNotFoundException, SQLException {
		int result = 0;

		try (Connection conn = Database.getConnection(); Statement stmt = conn.createStatement()) {
			// create a new table
			stmt.execute(create);
		} catch (SQLException e) {
			if (!ignoreError) {
				System.out.println(e.getMessage());
			}
		}
		return result;
	}
}
