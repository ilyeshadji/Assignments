package controllers;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import dao.UserDao;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.ResponseJSON;
import models.User;

/**
 * Servlet implementation class UserServlet
 */
@WebServlet(name = "UserServlet", urlPatterns = { "/users", "/user" })
public class UserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		UserDao userDao = new UserDao();
		ArrayList<User> userList = null;

		try {
			userList = userDao.getUsers();
		} catch (SQLException e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().write("Error in SQL query");
			return;
		}

		/*
		 * Keeping these two users out of the list to not give the impression that they
		 * can be modified
		 */
		User unmutableCustomer = findUserById(userList, 0);
		User unmutableStaff = findUserById(userList, 1);

		int unmutableCustomerIndex = userList.indexOf(unmutableCustomer);
		userList.remove(unmutableCustomerIndex);

		int unmutableStaffIndex = userList.indexOf(unmutableStaff);
		userList.remove(unmutableStaffIndex);

		ResponseJSON.sendResponse(response, userList);
	}

	/**
	 * @see HttpServlet#doPut(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPut(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		UserDao userDao = new UserDao();
		String user_id = request.getParameter("user_id");
		String role = request.getParameter("role");

		if (user_id == null || user_id.equals("null")) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().write("To update the user, it needs to have a valid user id.");
			return;
		}

		int convertedUserId = Integer.parseInt(user_id);

		if (convertedUserId == 1 || convertedUserId == 0) {
			response.setStatus(HttpServletResponse.SC_FORBIDDEN);
			response.getWriter().write("Modifying these users is strictly prohibited.");
			return;
		}

		int result = 0;

		try {
			result = userDao.updateRole(convertedUserId, role);
		} catch (SQLException e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().write("Error in SQL query");
			return;
		}

		if (result == 0) {
			return;
		}

		ResponseJSON.sendResponse(response, "Successfully changed user role");
	}

	// Function to find a user by id
	public static User findUserById(ArrayList<User> userList, int userId) {
		for (User user : userList) {
			if (user.getUser_id() == userId) {
				return user;
			}
		}
		return null;
	}

}
