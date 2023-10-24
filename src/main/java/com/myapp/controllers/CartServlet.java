package com.myapp.controllers;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.myapp.dao.CartDao;
import com.myapp.models.Product;
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

		ArrayList<Product> cart = new ArrayList<>();

		try {
			cart = cartDao.getCart(user_id);
		} catch (ClassNotFoundException e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().write("Class not found");
		} catch (SQLException e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().write("Error in SQL query");
		}

		if (cart.size() < 1) {
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

		try {
			result = cartDao.addProductToCart(user_id, product_id, quantity);
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

		try {
			result = cartDao.deleteProductFromCart(user_id, product_id);
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
}
