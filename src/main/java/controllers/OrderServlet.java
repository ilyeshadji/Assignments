package controllers;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.CartDao;
import dao.OrderDao;
import models.Cart;
import models.Order;
import models.ResponseJSON;

/**
 * Servlet implementation class OrderServlet
 */
@WebServlet(name = "OrderServlet", urlPatterns = { "/orders", "/orders/customer/*", "/orders/ship", "/orders/order" })
public class OrderServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String endpoint = request.getServletPath();
		String order_id = request.getParameter("order_id");
		OrderDao orderDao = new OrderDao();

		// *********************************
		// Get all orders
		// *********************************
		if (endpoint.equals("/orders") && order_id == null) {
			ArrayList<Order> orders = new ArrayList<>();

			try {
				orders = orderDao.getOrders();
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
			ArrayList<Order> orders = new ArrayList<>();

			if (user_id == null || user_id.equals("null")) {
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				response.getWriter().write("To ship an order, it needs to have a valid user id.");
				return;
			}

			int convertedUserId = Integer.parseInt(user_id);

			try {
				orders = orderDao.getOrders(convertedUserId);
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
				order = orderDao.getOrder(convertedOrderId);
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
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		CartDao cartDao = new CartDao();
		OrderDao orderDao = new OrderDao();

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
				orderDao.shipOrder(convertedOrderId);
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
			// Place an order
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
				result = OrderDao.createOrder(convertedUserId, cart.getProducts(), shipping_address);
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
		} else {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			response.getWriter().write("Route not found.");
			return;
		}
	}

}
