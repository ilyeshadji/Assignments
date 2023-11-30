import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
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
		userDao = mock(UserDao.class);
		servlet = new AuthenticationServlet(userDao);
		request = mock(HttpServletRequest.class);
		response = mock(HttpServletResponse.class);
	}

	@Test
    public void testUpdatePassword() throws ServletException, IOException, SQLException, ClassNotFoundException {
        when(request.getParameter("oldPassword")).thenReturn("oldPasswordValue");
        when(request.getParameter("newPassword")).thenReturn("newPasswordValue");

        User mockUser = new User(3, "customer", "Password_1"); 
        when(userDao.getUser("oldPasswordValue")).thenReturn(mockUser);
        when(userDao.updatePassword(anyInt(), eq("newPasswordValue"))).thenReturn(1); 

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        servlet.doPut(request, response);

        verify(response).getWriter();

        writer.flush();
        String responseContent = stringWriter.toString().trim();
        assert(responseContent.equals("\"Successfully updated password\""));
    }

	@Test
    public void shouldNotUpdatePasswordBecauseNoUserFound() throws ServletException, IOException, SQLException, ClassNotFoundException {
        when(request.getParameter("oldPassword")).thenReturn("oldPasswordValue");
        when(request.getParameter("newPassword")).thenReturn("newPasswordValue");

        when(userDao.getUser("oldPasswordValue")).thenReturn(null);

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        servlet.doPut(request, response);

        verify(response).getWriter();

        writer.flush();
        String responseContent = stringWriter.toString().trim();
        assert(responseContent.equals("Wrong credentials."));
    }

	@Test
    public void shouldNotUpdatePasswordBecauseNoOldPassword() throws ServletException, IOException, SQLException, ClassNotFoundException {
        when(request.getParameter("oldPassword")).thenReturn("");
        when(request.getParameter("newPassword")).thenReturn("newPasswordValue");

        User mockUser = new User(3, "customer", "Password_1"); 
        when(userDao.getUser("oldPasswordValue")).thenReturn(mockUser);
        
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        servlet.doPut(request, response);

        verify(response).getWriter();

        writer.flush();
        String responseContent = stringWriter.toString().trim();
        assert(responseContent.equals("Wrong credentials."));
    }

	@Test
    public void shouldNotUpdatePasswordBecauseNewPasswordNotValid() throws ServletException, IOException, SQLException, ClassNotFoundException {
        when(request.getParameter("oldPassword")).thenReturn("oldPasswordValue");
        when(request.getParameter("newPassword")).thenReturn("a");

        User mockUser = new User(3, "customer", "Password_1"); // Assuming User is your user class
        when(userDao.getUser("oldPasswordValue")).thenReturn(mockUser);
        
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        servlet.doPut(request, response);

        verify(response).getWriter();

        writer.flush();
        String responseContent = stringWriter.toString().trim();
        assert(responseContent.equals("Wrong password format. Make sure that the password is alphanumeric and contains at least four characters"));
    }

	@Test
    public void setPassword() throws ServletException, IOException, SQLException, ClassNotFoundException {        when(request.getServletPath()).thenReturn("/authentication/signup");
        when(request.getParameter("password")).thenReturn("testPassword");
        when(request.getParameter("role")).thenReturn("customer");

        when(userDao.createUser("customer", "testPassword")).thenReturn(1);

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        servlet.doPost(request, response);

        writer.flush();
        String responseContent = stringWriter.toString().trim();
        assert(responseContent.equals("\"Successfully created the account.\""));
    }

	@Test
    public void shouldNotSetPasswordIfNotValid() throws ServletException, IOException, SQLException, ClassNotFoundException {
        when(request.getServletPath()).thenReturn("/authentication/signup");
        when(request.getParameter("password")).thenReturn("a");
        when(request.getParameter("role")).thenReturn("customer");

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        servlet.doPost(request, response);

        writer.flush();
        String responseContent = stringWriter.toString().trim();
        assert(responseContent.equals("Wrong password format. Make sure that the password is alphanumeric and contains at least four characters"));
    }
}
