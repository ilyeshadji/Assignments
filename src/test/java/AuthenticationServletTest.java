import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.User;

import org.junit.Before;
import org.junit.Test;

import controllers.AuthenticationServlet;
import dao.UserDao;

public class AuthenticationServletTest {

	private AuthenticationServlet servlet;
	private HttpServletRequest request;
	private HttpServletResponse response;
	private UserDao userDao;

	@Before
	public void setUp() {
		servlet = new AuthenticationServlet();
		request = mock(HttpServletRequest.class);
		response = mock(HttpServletResponse.class);
		userDao = mock(UserDao.class);
	}

	@Test
    public void testDoPut() throws ServletException, IOException, SQLException, ClassNotFoundException {
        // Set up request parameters
        when(request.getParameter("oldPassword")).thenReturn("oldPasswordValue");
        when(request.getParameter("newPassword")).thenReturn("newPasswordValue");

        // Mock UserDao behavior
        User mockUser = new User(3, "customer", "Password_1"); // Assuming User is your user class
        when(userDao.getUser("oldPasswordValue")).thenReturn(mockUser);
        when(userDao.updatePassword(anyInt(), eq("newPasswordValue"))).thenReturn(1); // Assuming update is successful

        // Set up response.getWriter()
        PrintWriter writer = new PrintWriter("dummyWriter");
        when(response.getWriter()).thenReturn(writer);

        // Call the method under test
        servlet.doPut(request, response);

        verify(response).getWriter();
        // Add additional verification based on your implementation

        // Example: Assert that the writer has received the expected response
        writer.flush();
        String responseContent = writer.toString().trim();
        assert(responseContent.equals("{\"message\":\"Successfully updated password\"}"));
    }
}
