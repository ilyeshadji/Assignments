package com.myapp.controllers;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.myapp.dao.CartDao;
import com.myapp.models.Cart;
import com.myapp.models.ResponseJSON;

/**
 * Servlet implementation class CartServlet
 */
@WebServlet("/cart")
public class CartServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		CartDao cartDao = new CartDao();

		String user_id = request.getParameter("user_id");

		if (user_id == null) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().write("To send an order, a user needs a valid user id.");
			return;
		}

		int convertedUserId = Integer.parseInt(user_id);

		Cart cart = null;

		try {
			cart = cartDao.getCart(convertedUserId);
		} catch (ClassNotFoundException e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().write("Class not found");
		} catch (SQLException e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().write("Error in SQL query");
		}

		if (cart == null || cart.getProducts().size() < 1) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			response.getWriter().write("Could not fetch the cart. Your cart might be empty.");
			return;
		}

		ResponseJSON.sendResponse(response, cart);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		CartDao cartDao = new CartDao();

		String user_id = request.getParameter("user_id");
		String product_id = request.getParameter("product_id");
		int quantity = Integer.parseInt(request.getParameter("quantity"));

		int result = 0;
		String message = "";

		if (user_id == null) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().write("To send an order, a user needs a valid user id.");
			return;
		}

		int convertedUserId = Integer.parseInt(user_id);

		try {
			result = cartDao.addProductToCart(convertedUserId, product_id, quantity);
		} catch (ClassNotFoundException e) {
		} catch (SQLException e) {
			if (e.getMessage().contains("Duplicate entry")) {
				message = "You already have this item in your cart.";
			}
		}

		if (result != 1) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().write("Could not add item to the cart. " + message);
			return;
		}

		ResponseJSON.sendResponse(response, "success");
	}

	/**
	 * @see HttpServlet#doDelete(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doDelete(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		CartDao cartDao = new CartDao();

		String user_id = request.getParameter("user_id");
		String product_id = request.getParameter("product_id");

		int result = 0;

		if (user_id == null) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().write("To send an order, a user needs a valid user id.");
			return;
		}

		int convertedUserId = Integer.parseInt(user_id);

		try {
			result = cartDao.deleteProductFromCart(convertedUserId, product_id);
		} catch (ClassNotFoundException e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().write("Class not found");
		} catch (SQLException e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().write("Error in SQL query");
		}

		if (result != 1) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().write("Could not delete item from the cart.");
			return;
		}

		ResponseJSON.sendResponse(response, "success");
	}

	protected void doPut(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		CartDao cartDao = new CartDao();

		String user_id = request.getParameter("user_id");
		String sku = request.getParameter("sku");
		String quantity = request.getParameter("quantity");

		if (sku == null || user_id == null || quantity == null) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().write("To update your cart, a user needs a valid user id, sku and quantity.");
			return;
		}

		int result = 0;

		int convertedUserId = Integer.parseInt(user_id);
		int convertedQuantity = Integer.parseInt(quantity);

		try {
			result = cartDao.updateProductQuantityInCart(convertedUserId, sku, convertedQuantity);
		} catch (ClassNotFoundException e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().write("Class not found");
		} catch (SQLException e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().write("Error in SQL query");
		}

		if (result != 1) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().write("Could not update item from the cart.");
			return;
		}

		ResponseJSON.sendResponse(response, "success");
	}
}
