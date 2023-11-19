package controllers;

import java.io.IOException;
import java.sql.SQLException;

import dao.DatabaseInitializationDao;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class DatabaseInitialization
 */
@WebServlet("/database-initialization")
public class DatabaseInitialization extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		DatabaseInitializationDao dbInit = new DatabaseInitializationDao();
		try {
			System.out.println("Init database");
			dbInit.initializeDatabase();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
