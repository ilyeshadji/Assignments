package controllers;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import dao.CartDao;
import dao.OrderDao;
import dao.UserDao;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.Cart;
import models.Order;
import models.Product;
import models.ResponseJSON;
import models.User;

/**
 * Servlet implementation class OrderServlet
 */
@WebServlet(name = "OrderServlet", urlPatterns = { "/orders", "/orders/customer/*", "/orders/ship", "/orders/order",
		"/orders/no-customer", "/orders/claim" })
public class OrderServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private final ObjectMapper objectMapper = new ObjectMapper();
	private OrderDao orderDao = new OrderDao();
	private UserDao userDao = new UserDao();
	
	public OrderServlet() {
		
	}

	public OrderServlet(OrderDao orderDao, UserDao userDao) {
		this.orderDao = orderDao;
		this.userDao = userDao;
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String endpoint = request.getServletPath();
		String order_id = request.getParameter("order_id");

		// *********************************
		// Get all orders
		// *********************************
		if (endpoint.equals("/orders") && order_id == null) {
			List<Order> orders = new ArrayList<>();

			try {
				orders = this.orderDao.getOrders();
			} catch (ClassNotFoundException e) {
				response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				response.getWriter().write("Class not found");
				return;
			} catch (SQLException e) {
				response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				response.getWriter().write("Error in SQL query");
				return;
			}

			if (orders.size() < 1) {
				response.setStatus(HttpServletResponse.SC_NOT_FOUND);
				response.getWriter().write("There are no orders for this user.");
				return;
			}

			ResponseJSON.sendResponse(response, orders);

			// *********************************
			// Get all orders for a specific user
			// *********************************
		} else if (endpoint.contains("/orders/customer")) {
			String user_id = request.getParameter("user_id");
			List<Order> orders = new ArrayList<>();

			if (user_id == null || user_id.equals("null")) {
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				response.getWriter().write("To get an order, it needs to have a valid user id.");
				return;
			}

			int convertedUserId = Integer.parseInt(user_id);

			try {
				orders = this.orderDao.getOrders(convertedUserId);
			} catch (ClassNotFoundException e) {
				response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				response.getWriter().write("Class not found");
				return;
			} catch (SQLException e) {
				response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				response.getWriter().write("Error in SQL query");
				return;
			}

			if (orders.size() < 1) {
				response.setStatus(HttpServletResponse.SC_NOT_FOUND);
				response.getWriter().write("There are no orders for this user.");
				return;
			}

			ResponseJSON.sendResponse(response, orders);

			// *********************************
			// Get order by id
			// *********************************
		} else {
			Order order = null;

			if (order_id == null) {
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				response.getWriter().write("To get an order, it needs to have a valid order id.");
				return;
			}

			int convertedOrderId = Integer.parseInt(order_id);

			try {
				order = this.orderDao.getOrder(convertedOrderId);
			} catch (ClassNotFoundException e) {
				response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				response.getWriter().write("Class not found");
				return;
			} catch (SQLException e) {
				response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				response.getWriter().write("Error in SQL query");
				return;
			}

			if (order == null) {
				response.setStatus(HttpServletResponse.SC_NOT_FOUND);
				response.getWriter().write("There are no orders with this id.");
				return;
			}

			ResponseJSON.sendResponse(response, order);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		CartDao cartDao = new CartDao();

		String endpoint = request.getServletPath();

		// *********************************
		// Ship an order
		// *********************************
		if (endpoint.equals("/orders/ship")) {
			String order_id = request.getParameter("order_id");

			if (order_id == null) {
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				response.getWriter().write("To ship an order, it needs to have a valid order id.");
				return;
			}

			int convertedOrderId = Integer.parseInt(order_id);

			try {
				this.orderDao.shipOrder(convertedOrderId);
			} catch (ClassNotFoundException e) {
				response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				response.getWriter().write("Class not found");
				return;
			} catch (SQLException e) {
				response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				response.getWriter().write("Error in SQL query");
				return;
			}

			ResponseJSON.sendResponse(response, "Shipment successful.");

			// *********************************
			// Place an order with user id
			// *********************************
		} else if (endpoint.contains("/orders/customer")) {
			String user_id = request.getParameter("user_id");
			String shipping_address = request.getParameter("shipping_address");
			String result = "";

			if (user_id == null) {
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				response.getWriter().write("To send an order, a user needs a valid user id.");
				return;
			}

			int convertedUserId = Integer.parseInt(user_id);

			// Find cart of the user
			Cart cart = null;

			try {
				cart = cartDao.getCart(convertedUserId);
			} catch (ClassNotFoundException e) {
				response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				response.getWriter().write("Class not found");
				return;
			} catch (SQLException e) {
				response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				response.getWriter().write("Error in SQL query");
				return;
			}

			if (cart == null) {
				response.setStatus(HttpServletResponse.SC_NOT_FOUND);
				response.getWriter().write("There are no items in your cart, you cannot place an empty order.");
				return;
			}

			// Create an order from cart
			try {
				result = this.orderDao.createOrder(convertedUserId, cart.getProducts(), shipping_address);
			} catch (ClassNotFoundException e) {
				response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				response.getWriter().write("Class not found");
				return;
			} catch (SQLException e) {
				response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				response.getWriter().write("Error in SQL query");
				return;
			}

			if (!result.equals("success")) {
				response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				response.getWriter().write("Something went wrong while creating the order. Please try again.");
				return;
			}

			// Empty current cart
			try {
				cartDao.clearCart(convertedUserId);
			} catch (ClassNotFoundException e) {
				response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				response.getWriter().write("Class not found");
				return;
			} catch (SQLException e) {
				response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				response.getWriter().write("Error in SQL query");
				return;
			}

			ResponseJSON.sendResponse(response, "Successfully created order.");
		} else if (endpoint.contains("/orders/no-customer")) {

			ArrayList<Product> orderProducts = new ArrayList<>();
			String shippingAddress;
			String result = "";

			try {
				BufferedReader reader = request.getReader();
				StringBuilder sb = new StringBuilder();
				String line;

				while ((line = reader.readLine()) != null) {
					sb.append(line);
				}

				String jsonData = sb.toString();
				JsonNode jsonNode = objectMapper.readTree(jsonData);

				shippingAddress = jsonNode.get("shipping_address").asText();
				JsonNode productsNode = jsonNode.get("products");

				for (JsonNode productNode : productsNode) {
					String name = productNode.get("name").asText();
					String description = productNode.get("description").asText();
					String vendor = productNode.get("vendor").asText();
					String url = productNode.get("url").asText();
					String sku = productNode.get("sku").asText();
					double price = productNode.get("price").asDouble();
					int quantity = productNode.get("quantity").asInt();

					Product product = new Product(sku, name, description, vendor, url, price, quantity);

					orderProducts.add(product);
				}

				try {
					result = this.orderDao.createOrder(0, orderProducts, shippingAddress);
				} catch (ClassNotFoundException e) {
					response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
					response.getWriter().write("Class not found");
					return;
				} catch (SQLException e) {
					response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
					response.getWriter().write("Error in SQL query");
					return;
				}

				if (!result.equals("success")) {
					response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
					response.getWriter().write("Something went wrong while creating the order. Please try again.");
					return;
				}

				ResponseJSON.sendResponse(response, "Successfully processed order without customer.");

			} catch (Exception e) {
				// Handle JSON parsing or other exceptions
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				response.getWriter().write("Error processing order data.");
			}
		} else {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			response.getWriter().write("Route not found.");
			return;
		}
	}

	/**
	 * @see HttpServlet#doPut(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	public void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String order_id = request.getParameter("order_id");
		String user_id = request.getParameter("user_id");

		int result = 0;

		if (order_id == null || user_id == null) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().write("To claim an order, it needs to have a valid order id and user id.");
			return;
		}

		int convertedOrderId = Integer.parseInt(order_id);
		int convertedUserId = Integer.parseInt(user_id);

		Order order;
		try {
			order = this.orderDao.getOrder(convertedOrderId);
		} catch (ClassNotFoundException e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().write("Class not found");
			return;
		} catch (SQLException e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().write("Error in SQL query");
			return;
		}

		if (order == null) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			response.getWriter().write("Something went wrong, we could not find the order.");
			return;
		}

		if (order.getUser_id() != 0) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().write("Cannot claim an order that was already claimed.");
			return;
		}

		User user;
		try {
			user = this.userDao.getUserById(convertedUserId);
		} catch (SQLException e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().write("Error in SQL query");
			return;
		}

		if (user == null) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			response.getWriter().write("Something went wrong, we could not find the user.");
			return;
		}

		try {
			result = this.orderDao.updateOrder(order.getOrder_id(), user.getUser_id());
		} catch (SQLException e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().write("Error in SQL query");
			return;
		}

		if (result == 0) {
			return;
		}

		ResponseJSON.sendResponse(response, "Successfully updated order");
	}
}
