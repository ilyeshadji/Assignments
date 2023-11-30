import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.SQLException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.Order;
import models.User;

import org.junit.Before;
import org.junit.Test;
import dao.OrderDao;
import dao.UserDao;
import controllers.OrderServlet;

public class OrderServletTest {

	private OrderServlet servlet;
	private HttpServletRequest request;
	private HttpServletResponse response;
	private OrderDao orderDao;
	private UserDao userDao;

	@Before
	public void setUp() throws Exception {
		orderDao = mock(OrderDao.class);
		userDao = mock(UserDao.class);
		servlet = new OrderServlet(orderDao, userDao);
		request = mock(HttpServletRequest.class);
		response = mock(HttpServletResponse.class);
	}

	@Test
	public void testSetOrderOwner() throws SQLException, IOException, ServletException, ClassNotFoundException {
        when(request.getParameter("order_id")).thenReturn("3");
        when(request.getParameter("user_id")).thenReturn("3");

        User mockUser = new User(3, "customer", "Password_1"); 
        Order mockOrder = new Order(0, 3,20.00, "mockedAddress", 10000);
        when(orderDao.updateOrder(mockUser.getUser_id(), mockOrder.getOrder_id())).thenReturn(1);
        when(orderDao.getOrder(mockOrder.getOrder_id())).thenReturn(mockOrder);
        when(userDao.getUserById(mockUser.getUser_id())).thenReturn(mockUser);

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        servlet.doPut(request, response);

        verify(response).getWriter();

        writer.flush();
        String responseContent = stringWriter.toString().trim();
        assert(responseContent.equals("\"Successfully updated order\""));
	}

	@Test
	public void shouldNotSetNewOwnerSinceAlreadyClaimed() throws SQLException, IOException, ServletException, ClassNotFoundException {
        when(request.getParameter("order_id")).thenReturn("3");
        when(request.getParameter("user_id")).thenReturn("3");

        User mockUser = new User(3, "customer", "Password_1"); 
        Order mockOrder = new Order(2, 3,20.00, "mockedAddress", 10000);
        when(orderDao.updateOrder(mockUser.getUser_id(), mockOrder.getOrder_id())).thenReturn(1);
        when(orderDao.getOrder(mockOrder.getOrder_id())).thenReturn(mockOrder);

        
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        servlet.doPut(request, response);

        verify(response).getWriter();

        writer.flush();
        String responseContent = stringWriter.toString().trim();
        assert(responseContent.equals("Cannot claim an order that was already claimed."));
	}

	@Test
	public void shouldNotSetOrderOwnerBecauseNoOrderIdGiven() throws SQLException, IOException, ServletException {
        when(request.getParameter("order_id")).thenReturn(null);
        when(request.getParameter("user_id")).thenReturn("3");
        
        User mockUser = new User(3, "customer", "Password_1"); 
        Order mockOrder = new Order(0, 3,20.00, "mockedAddress", 10000);
        when(orderDao.updateOrder(mockUser.getUser_id(), mockOrder.getOrder_id())).thenReturn(1);

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        servlet.doPut(request, response);

        verify(response).getWriter();

        writer.flush();
        String responseContent = stringWriter.toString().trim();
        assert(responseContent.equals("To claim an order, it needs to have a valid order id and user id."));
	}

	@Test
	public void shouldNotSetOrderOwnerBecauseNoUserIdGiven() throws SQLException, IOException, ServletException {
        when(request.getParameter("order_id")).thenReturn("3");
        when(request.getParameter("user_id")).thenReturn(null);
        
        User mockUser = new User(3, "customer", "Password_1"); 
        Order mockOrder = new Order(0, 3,20.00, "mockedAddress", 10000);
        when(orderDao.updateOrder(mockUser.getUser_id(), mockOrder.getOrder_id())).thenReturn(1);

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        servlet.doPut(request, response);

        verify(response).getWriter();

        writer.flush();
        String responseContent = stringWriter.toString().trim();
        assert(responseContent.equals("To claim an order, it needs to have a valid order id and user id."));
	}

	@Test
	public void shouldNotSetOrderOwnerBecauseNoOrderWithThisId() throws SQLException, IOException, ServletException, ClassNotFoundException {
        when(request.getParameter("order_id")).thenReturn("3");
        when(request.getParameter("user_id")).thenReturn("3");

        Order mockOrder = new Order(0, 3,20.00, "mockedAddress", 10000);
        when(orderDao.getOrder(mockOrder.getOrder_id())).thenReturn(null);
        
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        servlet.doPut(request, response);

        verify(response).getWriter();

        writer.flush();
        String responseContent = stringWriter.toString().trim();
        assert(responseContent.equals("Something went wrong, we could not find the order."));
	}

	@Test
	public void shouldNotSetOrderOwnerBecauseNoUserWithThisId() throws SQLException, IOException, ServletException, ClassNotFoundException {
        when(request.getParameter("order_id")).thenReturn("3");
        when(request.getParameter("user_id")).thenReturn("3");

        User mockUser = new User(3, "customer", "Password_1"); 
        Order mockOrder = new Order(0, 3,20.00, "mockedAddress", 10000);
        when(orderDao.getOrder(mockOrder.getOrder_id())).thenReturn(mockOrder);
        when(userDao.getUserById(mockUser.getUser_id())).thenReturn(null);
        
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        servlet.doPut(request, response);

        verify(response).getWriter();

        writer.flush();
        String responseContent = stringWriter.toString().trim();
        assert(responseContent.equals("Something went wrong, we could not find the user."));
	}

}
