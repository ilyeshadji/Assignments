package controllers;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.sql.SQLException;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import dao.UserDao;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.ResponseJSON;
import models.User;

/**
 * Servlet implementation class AuthenticationServlet
 */
@WebServlet(name = "AuthenticationServlet", urlPatterns = { "/authentication/login", "/authentication/signup",
		"/authentication/update-password" })
public class AuthenticationServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String PASSCODE_REGEX = "^(?=.*[0-9a-zA-Z]).{4,}$";
	private UserDao userDao = new UserDao();

	public AuthenticationServlet() {
		
	}
	
	public AuthenticationServlet(UserDao userDao) {
		this.userDao = userDao;
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String endpoint = request.getServletPath();

		if (endpoint.equals("/authentication/login")) {
			String password = request.getParameter("password");

			User user;

			try {
				user = this.userDao.getUser(password);
			} catch (ClassNotFoundException e) {
				response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				response.getWriter().write("Something went wrong. We could not find the user.");
				return;
			} catch (SQLException e) {
				response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				response.getWriter().write("Something went wrong. Please try again later.");
				return;
			}

			if (user == null || password.equals("")) {
				response.setStatus(HttpServletResponse.SC_NOT_FOUND);
				response.getWriter().write("Wrong credentials.");
				return;
			}

			PrivateKey privateKey = null;

			try {
				privateKey = getPrivateKey();
			} catch (NoSuchAlgorithmException e) {
				response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				response.getWriter().write("Something went wrong. We could not authenticate you.");
				return;
			} catch (InvalidKeySpecException e) {
				response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				response.getWriter().write("Something went wrong. We could not authenticate you.");
				return;
			}

			/**
			 * N.B. The token does not have an expiration date for the sake of testing. This
			 * should always be implemented by adding the function setExpiration for
			 * security.
			 */
			Instant now = Instant.now();
			String jwtToken = Jwts.builder().claim("role", user.getRole()).claim("user_id", user.getUser_id())
					.setId(UUID.randomUUID().toString()).setIssuedAt(Date.from(now)).signWith(privateKey).compact();

			ResponseJSON.sendResponse(response, jwtToken);

		} else if (endpoint.equals("/authentication/signup")) {
			String password = request.getParameter("password");
			String role = request.getParameter("role");

			int result = 0;

			if (!passwordValid(password)) {
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				response.getWriter().write(
						"Wrong password format. Make sure that the password is alphanumeric and contains at least four characters");
				return;
			}

			try {
				result = this.userDao.createUser(role, password);
			} catch (ClassNotFoundException e) {
				response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				response.getWriter().write("Something went wrong. We could not create a user for you.");
				return;
			} catch (SQLException e) {
				response.setStatus(HttpServletResponse.SC_FORBIDDEN);
				response.getWriter().write("This address is already used by another account. Please log in.");
				return;
			}

			if (result != 1) {
				response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				response.getWriter().write("Something went wrong, please try again.");
				return;
			}

			ResponseJSON.sendResponse(response, "Successfully created the account.");
		} else {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			response.getWriter().write("Route not found");
		}
	}

	/**
	 * @see HttpServlet#doPut(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	public void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String oldPassword = request.getParameter("oldPassword");
		String newPassword = request.getParameter("newPassword");

		// find the user
		User user;

		int result = 0;

		try {
			user = this.userDao.getUser(oldPassword);
		} catch (ClassNotFoundException e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().write("Something went wrong. We could not find the user.");
			return;
		} catch (SQLException e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().write("Something went wrong. Please try again later.");
			return;
		}

		if (user == null || oldPassword.equals("")) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			response.getWriter().write("Wrong credentials.");
			return;
		}

		if (!passwordValid(newPassword)) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().write(
					"Wrong password format. Make sure that the password is alphanumeric and contains at least four characters");
			return;
		}

		// update password
		try {
			result = this.userDao.updatePassword(user.getUser_id(), newPassword);
		} catch (SQLException e) {
			e.printStackTrace();
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().write("Something went wrong. Please try again later.");
			return;
		}

		if (result == 0) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().write("Something went wrong. Please try again later.");
			return;
		}

		ResponseJSON.sendResponse(response, "Successfully updated password");
	}

	public static PrivateKey getPrivateKey() throws NoSuchAlgorithmException, InvalidKeySpecException {
		String keyFilePath = System.getenv("SRC_PATH") + "/key.pem";
		String rsaPrivateKey = "";

		try (BufferedReader reader = new BufferedReader(new FileReader(keyFilePath))) {
			StringBuilder pemContent = new StringBuilder();
			String line;
			while ((line = reader.readLine()) != null) {
				pemContent.append(line);
			}

			rsaPrivateKey = pemContent.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}

		rsaPrivateKey = rsaPrivateKey.replace("-----BEGIN RSA PRIVATE KEY-----", "");
		rsaPrivateKey = rsaPrivateKey.replace("-----END RSA PRIVATE KEY-----", "");

		PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(rsaPrivateKey));
		KeyFactory kf = KeyFactory.getInstance("RSA");
		PrivateKey privKey = kf.generatePrivate(keySpec);
		return privKey;
	}

	public static boolean passwordValid(String password) {
		Pattern pattern = Pattern.compile(PASSCODE_REGEX);
		Matcher matcher = pattern.matcher(password);

		if (!matcher.matches()) {
			return false;
		}

		return true;
	}
}
