package controllers;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import dao.ProductDao;
import models.Product;
import models.ResponseJSON;

/**
 * Servlet implementation class ProductsServlet
 */
@WebServlet(name = "ProductServlet", urlPatterns = { "/product", "/product-list", "/product/create" })
public class ProductServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ProductServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String endpoint = request.getServletPath();

		Product product = null;
		ArrayList<Product> productList = null;

		if (endpoint.equals("/product-list")) {

			// Get products list
			productList = ProductServlet.getProductList(response);

			if (productList == null || productList.size() < 1) {
				response.setStatus(HttpServletResponse.SC_NOT_FOUND);
				response.getWriter().write("Could not find the products.");

				return;
			}

			ResponseJSON.sendResponse(response, productList);

		} else if (endpoint.equals("/product")) {
			String sku = request.getParameter("sku");

			// Get product by sku id
			product = ProductServlet.getProduct(response, sku);

			if (product == null) {
				response.setStatus(HttpServletResponse.SC_NOT_FOUND);
				response.getWriter().write("Could not find the product.");

				return;
			}

			ResponseJSON.sendResponse(response, product);

		} else {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			response.getWriter().write("Route not found");
		}

	}

	/**
	 * @see HttpServlet#doPut(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPut(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String endpoint = request.getServletPath();

		if (endpoint.contains("/product")) {
			String sku = request.getParameter("sku");

			ProductServlet.updateProduct(request, response, sku);
		} else {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			response.getWriter().write("Not found - Invalid product Id");
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String endpoint = request.getServletPath();

		switch (endpoint) {
		case "/product/create":
			ProductServlet.createProduct(request, response);
			break;
		default:
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			response.getWriter().write("Route not found");
		}
	}

	static void createProduct(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String name = request.getParameter("name");
		String description = request.getParameter("description");
		String vendor = request.getParameter("vendor");
		String url = request.getParameter("url");
		String sku = request.getParameter("sku");
		String price = request.getParameter("price");

		ProductDao productDao = new ProductDao();

		int result = 0;
		String message = "";

		Double priceValue = Double.parseDouble(price);

		String regexPattern = "^[a-z0-9-]{1,100}$";

		Pattern pattern = Pattern.compile(regexPattern);
		Matcher matcher = pattern.matcher(sku);

		if (!matcher.matches()) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().write(
					"Invalid Sku. It is limited to 100 characters and may only contain lowercase letters, numbers or dashes.");

			return;
		}

		try {
			result = productDao.createProduct(name, description, vendor, url, sku, priceValue);
		} catch (ClassNotFoundException e) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			response.getWriter().write("Class not found");
		} catch (SQLException e) {
			if (e.getMessage().contains("Duplicate entry")) {
				message = "This item has an id that already exists.";
			}
		}

		if (result != 1) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().write("Could not add product. " + message);

			return;
		}

		ResponseJSON.sendResponse(response, "success");
	}

	static void updateProduct(HttpServletRequest request, HttpServletResponse response, String sku) throws IOException {
		ProductDao productDao = new ProductDao();

		int result = 0;

		try {
			result = productDao.updateProduct(request, sku);
		} catch (ClassNotFoundException e) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			response.getWriter().write("Class not found");
		} catch (SQLException e) {
			response.setStatus(HttpServletResponse.SC_PRECONDITION_FAILED);
			response.getWriter().write("Invalid query");
		}

		if (result != 1) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().write("Could not update product. Please make sure that you entered a valid sku id.");

			return;
		}

		ResponseJSON.sendResponse(response, "success");
	}

	static Product getProduct(HttpServletResponse response, String sku) throws IOException {
		ProductDao productDao = new ProductDao();

		Product product = null;

		try {
			product = productDao.getProduct(sku);
		} catch (ClassNotFoundException e) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			response.getWriter().write("Class not found");
		} catch (SQLException e) {
			response.setStatus(HttpServletResponse.SC_PRECONDITION_FAILED);
			response.getWriter().write("Invalid query");
		}

		return product;
	}

	static Product getProductBySlug(HttpServletResponse response, String slug) throws IOException {
		ProductDao productDao = new ProductDao();

		Product product = null;

		try {
			productDao.getProductBySlug(slug);
		} catch (ClassNotFoundException e) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			response.getWriter().write("Class not found");
		} catch (SQLException e) {
			response.setStatus(HttpServletResponse.SC_PRECONDITION_FAILED);
			response.getWriter().write("Invalid query");
		}

		return product;
	}

	static ArrayList<Product> getProductList(HttpServletResponse response) throws IOException {
		ProductDao productDao = new ProductDao();

		ArrayList<Product> productList = null;

		try {
			productList = productDao.getProductList();
		} catch (ClassNotFoundException e) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			response.getWriter().write("Class not found");
		} catch (SQLException e) {
			response.setStatus(HttpServletResponse.SC_PRECONDITION_FAILED);
			response.getWriter().write("Invalid query");
		}

		return productList;
	}
}
