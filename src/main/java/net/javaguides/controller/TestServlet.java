package net.javaguides.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.javaguides.model.Test;
import net.javaguides.dao.TestDao;

/**
 * Servlet implementation class TestServlet
 */
@WebServlet("/register")
public class TestServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private TestDao testDao = new TestDao();

	/**
	 * Default constructor.
	 */
	public TestServlet() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		int id =  Integer.parseInt(request.getParameter("id"));
		Test test = new Test();
		test.setId(id);
		int result = 0;
		
		try {
			result = testDao.registerTest(test);
			
			response.setContentType("text/plain");
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		if(result == 1) {
			PrintWriter out = response.getWriter();
			out.write("Success");
			out.flush();
			out.close(); 
		}
		else {
			response.sendError(500, "Oops Something went wrong");
		}
	
	}
}
