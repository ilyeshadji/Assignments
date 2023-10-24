package com.myapp.controllers;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.myapp.models.Customer;
import com.myapp.models.ResponseJSON;
import com.myapp.models.Staff;

/**
 * Servlet implementation class Permissions
 */
@WebServlet("/permissions")
public class Permissions extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Permissions() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String[] permissions = null;

		if (request.getParameter("actor").equals("customer")) {
			Customer customer = new Customer(Customer.permissions);

			permissions = customer.getPermissions();
		} else if (request.getParameter("actor").equals("staff")) {
			Customer staff = new Customer(Staff.permissions);

			permissions = staff.getPermissions();
		}

		ResponseJSON.sendResponse(response, permissions);
	}
}
