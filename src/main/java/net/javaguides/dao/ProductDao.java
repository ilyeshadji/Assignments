package net.javaguides.dao;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.javaguides.model.Product;

public class ProductDao {
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

	public int createProduct(HttpServletResponse response, String name, String description, String vendor, String url,
			String sku, double price) throws ClassNotFoundException, SQLException, IOException {
		String CREATE_PRODUCT = "INSERT INTO product (name, description, vendor, url, sku, price)\n" + "VALUES ('"
				+ name + "', '" + description + "', '" + vendor + "', '" + url + "', '" + sku + "', " + price + ");";

		int result = 0;

		createDatabaseConnection();

		try {
			Statement statement = conn.createStatement();
			statement.executeUpdate(CREATE_PRODUCT);
			result = 1;

		} catch (SQLException e) {
			System.out.println("SQLException: " + e.getMessage());
			System.out.println("SQLState: " + e.getSQLState());
			System.out.println("VendorError: " + e.getErrorCode());

			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().write("Could not create product.");
		}

		return result;
	}

	public int updateProduct(HttpServletResponse response, HttpServletRequest request, String sku)
			throws ClassNotFoundException, SQLException, IOException {
		String CREATE_PRODUCT = "UPDATE product \nSET ";

		Enumeration<String> updates = request.getParameterNames();

		while (updates.hasMoreElements()) {
			String paramName = updates.nextElement();
			String value = request.getParameter(paramName);

			if (paramName.equals("price")) {
				CREATE_PRODUCT += "`" + paramName + "` = " + value + ", ";

			}

			CREATE_PRODUCT += "`" + paramName + "` = '" + value + "', ";
		}

		CREATE_PRODUCT = CREATE_PRODUCT.substring(0, CREATE_PRODUCT.length() - 2) + "\nWHERE (`sku` = '" + sku + "');";

		int result = 0;

		createDatabaseConnection();

		try {
			Statement statement = conn.createStatement();
			statement.executeUpdate(CREATE_PRODUCT);
			result = 1;

		} catch (SQLException e) {
			System.out.println("SQLException: " + e.getMessage());
			System.out.println("SQLState: " + e.getSQLState());
			System.out.println("VendorError: " + e.getErrorCode());

			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().write("Could not update product.");
		}

		return result;
	}

	public Product getProduct(HttpServletResponse response, String sku)
			throws ClassNotFoundException, SQLException, IOException {
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

			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			response.getWriter().write("Could not find product.");
		}

		return product;
	}

	public Product getProductBySlug(HttpServletResponse response, String slug)
			throws ClassNotFoundException, SQLException, IOException {
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

			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			response.getWriter().write("Could not find product.");
		}

		return product;
	}

	public ArrayList<Product> getProductList(HttpServletResponse response)
			throws ClassNotFoundException, SQLException, IOException {
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

			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			response.getWriter().write("Could not find product list.");
		}

		return productList;
	}
}
