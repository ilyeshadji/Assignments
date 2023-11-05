package com.myapp.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import com.myapp.models.Product;

public class ProductDao {
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

	public int createProduct(String name, String description, String vendor, String url, String sku, double price)
			throws ClassNotFoundException, SQLException {
		String CREATE_PRODUCT = "INSERT INTO product (name, description, vendor, url, sku, price)\n" + "VALUES ('"
				+ name + "', '" + description + "', '" + vendor + "', '" + url + "', '" + sku + "', " + price + ");";

		int result = 0;

		createDatabaseConnection();

		Statement statement = conn.createStatement();
		statement.executeUpdate(CREATE_PRODUCT);
		result = 1;

		return result;
	}

	public int updateProduct(HttpServletRequest request, String sku) throws ClassNotFoundException, SQLException {
		String UPDATE_PRODUCT = "UPDATE product \nSET ";

		Enumeration<String> updates = request.getParameterNames();

		while (updates.hasMoreElements()) {
			String paramName = updates.nextElement();
			String value = request.getParameter(paramName);

			if (paramName.equals("price")) {
				UPDATE_PRODUCT += "`" + paramName + "` = " + value + ", ";

			}

			UPDATE_PRODUCT += "`" + paramName + "` = '" + value + "', ";
		}

		UPDATE_PRODUCT = UPDATE_PRODUCT.substring(0, UPDATE_PRODUCT.length() - 2) + "\nWHERE (`sku` = '" + sku + "');";

		createDatabaseConnection();

		int result = 0;

		try {
			Statement statement = conn.createStatement();
			result = statement.executeUpdate(UPDATE_PRODUCT);
		} catch (SQLException e) {
			System.out.println("SQLException: " + e.getMessage());
			System.out.println("SQLState: " + e.getSQLState());
			System.out.println("VendorError: " + e.getErrorCode());
		}

		return result;
	}

	public Product getProduct(String sku) throws ClassNotFoundException, SQLException {
		String GET_PRODUCT = "SELECT * FROM product \nWHERE `sku` = '" + sku + "';";

		Product product = null;

		createDatabaseConnection();

		try {
			Statement statement = conn.createStatement();
			ResultSet resultSet = statement.executeQuery(GET_PRODUCT);

			if (resultSet.next()) {
				String name = resultSet.getString("name");
				String description = resultSet.getString("description");
				String vendor = resultSet.getString("vendor");
				String url = resultSet.getString("url");
				double price = resultSet.getDouble("price");

				product = new Product(sku, name, description, vendor, url, price);
			}

		} catch (SQLException e) {
			System.out.println("SQLException: " + e.getMessage());
			System.out.println("SQLState: " + e.getSQLState());
			System.out.println("VendorError: " + e.getErrorCode());
		}

		return product;
	}

	public Product getProductBySlug(String slug) throws ClassNotFoundException, SQLException {
		String GET_PRODUCT = "SELECT * FROM product \nWHERE `slug` = '" + slug + "';";

		Product product = null;

		createDatabaseConnection();

		try {
			Statement statement = conn.createStatement();
			ResultSet resultSet = statement.executeQuery(GET_PRODUCT);

			if (resultSet.next()) {
				String sku = resultSet.getString("sku");
				String name = resultSet.getString("name");
				String description = resultSet.getString("description");
				String vendor = resultSet.getString("vendor");
				double price = resultSet.getDouble("price");

				product = new Product(sku, name, description, vendor, slug, price);
			}

		} catch (SQLException e) {
			System.out.println("SQLException: " + e.getMessage());
			System.out.println("SQLState: " + e.getSQLState());
			System.out.println("VendorError: " + e.getErrorCode());
		}

		return product;
	}

	public ArrayList<Product> getProductList() throws ClassNotFoundException, SQLException {
		String GET_PRODUCT = "SELECT * FROM product;";

		ArrayList<Product> productList = new ArrayList<>();

		createDatabaseConnection();

		try {
			Statement statement = conn.createStatement();
			ResultSet resultSet = statement.executeQuery(GET_PRODUCT);

			while (resultSet.next()) {
				String sku = resultSet.getString("sku");
				String name = resultSet.getString("name");
				String description = resultSet.getString("description");
				String vendor = resultSet.getString("vendor");
				String url = resultSet.getString("url");

				double price = resultSet.getDouble("price");

				Product product = new Product(sku, name, description, vendor, url, price);

				productList.add(product);
			}
		} catch (SQLException e) {
			System.out.println("SQLException: " + e.getMessage());
			System.out.println("SQLState: " + e.getSQLState());
			System.out.println("VendorError: " + e.getErrorCode());
		}

		return productList;
	}
}
