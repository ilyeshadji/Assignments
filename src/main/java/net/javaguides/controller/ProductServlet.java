package net.javaguides.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.javaguides.dao.ProductDao;
import net.javaguides.model.Product;
import net.javaguides.model.ResponseJSON;

/**
 * Servlet implementation class ProductsServlet
 */
@WebServlet("/product/*")
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
		// TODO Auto-generated method stub
		String endpoint = ProductServlet.getEndpoint(request);

		String skuIdRegex = "^[a-zA-Z0-9]+$";
		Pattern skuIdPattern = Pattern.compile(skuIdRegex);

		String slugRegex = "^(http(s)?:\\/\\/)?[a-zA-Z0-9.-]+(\\.[a-zA-Z]{2,})?(:[0-9]+)?(\\/[^?#\\s]*)?(\\?[^#\\s]*)?(#.*)?$";
		Pattern slugPattern = Pattern.compile(slugRegex);

		Product product = null;
		ArrayList<Product> productList = null;

		if (request.getRequestURL().toString().endsWith("product")) {

			// Get products list
			productList = ProductServlet.getProductList(response);

			if (productList.size() < 1) {
				response.setStatus(HttpServletResponse.SC_NOT_FOUND);
				response.getWriter().write("Could not find the products.");

				return;
			}

			ResponseJSON.sendResponse(response, productList);

		} else if (skuIdPattern.matcher(endpoint).matches()) {

			// Get product by sku id
			product = ProductServlet.getProduct(response, endpoint);

			if (product == null) {
				response.setStatus(HttpServletResponse.SC_NOT_FOUND);
				response.getWriter().write("Could not find the product.");

				return;
			}

			ResponseJSON.sendResponse(response, product);

		} else if (slugPattern.matcher(endpoint).matches()) {

			// Get product by slug
			product = ProductServlet.getProductBySlug(response, endpoint);

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
		// TODO Auto-generated method stub
		String skuId = ProductServlet.getEndpoint(request);

		String regex = "^[a-zA-Z0-9]+$";
		Pattern pattern = Pattern.compile(regex);

		if (pattern.matcher(skuId).matches() && skuId.length() > 0) {
			ProductServlet.updateProduct(request, response, skuId);
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

		String endpoint = ProductServlet.getEndpoint(request);

		switch (endpoint) {
		case "create":
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

		System.out.println("yo");

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

	static String getEndpoint(HttpServletRequest request) {
		int lastIndex = request.getRequestURL().lastIndexOf("/");
		return request.getRequestURL().substring(lastIndex + 1);
	}

}
