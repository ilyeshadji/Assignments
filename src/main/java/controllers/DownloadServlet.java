package controllers;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import dao.ProductDao;
import models.Product;

/**
 * Servlet implementation class DownloadServlet
 */
@WebServlet("/download/*")
public class DownloadServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	public DownloadServlet() {
		
	}

	/**
	 * @see HttpServlet#doGet( HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String endpoint = DownloadServlet.getEndpoint(request);

		ProductDao productDao = new ProductDao();

		switch (endpoint) {
		case "products":
			ArrayList<Product> productList = null;
			String filePath = "products-list.txt";

			try {
				productList = productDao.getProductList();

				if (productList.size() < 1) {
					response.setStatus(HttpServletResponse.SC_NOT_FOUND);
					response.getWriter().write("Could not find items in your cart");
				}

				DownloadServlet.createProductsFile(response, productList, filePath);

				File productsList = new File(filePath);

				DownloadServlet.downloadFile(response, productsList);
			} catch (ClassNotFoundException e) {
				response.setStatus(HttpServletResponse.SC_NOT_FOUND);
				response.getWriter().write("Class not found");
			} catch (SQLException e) {
				response.setStatus(HttpServletResponse.SC_PRECONDITION_FAILED);
				response.getWriter().write("Invalid query");
			}

			break;
		default:
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			response.getWriter().write("Route not found");
		}
	}

	static void createProductsFile(HttpServletResponse response, ArrayList<Product> productList, String filePath)
			throws IOException {
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
			// Loop through the productList and write product data to the file
			for (Product product : productList) {
				writer.write("Product sku: " + product.getSku());
				writer.newLine();
				writer.write("Product name: " + product.getName());
				writer.newLine();
				writer.write("Product description: " + product.getDescription());
				writer.newLine();
				writer.write("Product vendor: " + product.getVendor());
				writer.newLine();
				writer.write("Product url: " + product.getUrl());
				writer.newLine();
				writer.write("Product Price: " + product.getPrice());
				writer.newLine();
				writer.newLine();
				writer.write("************************************************");
				writer.newLine();
				writer.newLine();
			}
		} catch (IOException e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().write("Something went wrong with the creation of the file.");
		}

	}

	static void downloadFile(HttpServletResponse response, File file) throws IOException {
		if (file.exists() && file.isFile()) {
			response.setHeader("Content-Disposition", "attachment; filename=\"" + file.getName());
			response.setContentType("text/plain");

			try (FileInputStream fileInputStream = new FileInputStream(file)) {
				byte[] buffer = new byte[1024];
				int bytesRead;
				while ((bytesRead = fileInputStream.read(buffer)) != -1) {
					response.getOutputStream().write(buffer, 0, bytesRead);
				}
			} catch (IOException e) {
				response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				response.getWriter().write("Something went wrong with the creation of the file.");
			}
		} else {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			response.getWriter().write("Something went wrong with the download of the file.");
		}
	}

	static String getEndpoint(HttpServletRequest request) {
		int lastIndex = request.getRequestURL().lastIndexOf("/");
		return request.getRequestURL().substring(lastIndex + 1);
	}
}
