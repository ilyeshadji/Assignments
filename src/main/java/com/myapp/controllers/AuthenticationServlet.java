package com.myapp.controllers;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.mindrot.jbcrypt.BCrypt;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.sql.SQLException;
import java.time.Instant;

import com.myapp.dao.UserDao;
import com.myapp.models.ResponseJSON;
import com.myapp.models.User;

import io.jsonwebtoken.Jwts;

/**
 * Servlet implementation class AuthenticationServlet
 */
@WebServlet(name = "AuthenticationServlet", urlPatterns = { "/authentication/login", "/authentication/signup" })
public class AuthenticationServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String endpoint = request.getServletPath();

		if (endpoint.equals("/authentication/login")) {
			String email = request.getParameter("email");
			String password = request.getParameter("password");

			User user;

			try {
				user = UserDao.getUser(email, password);
			} catch (ClassNotFoundException e) {
				response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				response.getWriter().write("Something went wrong. We could not find the user.");
				return;
			} catch (SQLException e) {
				response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				response.getWriter().write("Something went wrong. Please try again later.");
				return;
			}

			if (user == null) {
				response.setStatus(HttpServletResponse.SC_NOT_FOUND);
				response.getWriter()
						.write("We could not find user with this address. Please create an account before logging in.");
				return;
			}

			if (AuthenticationServlet.checkPassword(password, user.getPassword()) == false) {
				response.setStatus(HttpServletResponse.SC_FORBIDDEN);
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
			String email = request.getParameter("email");
			String password = request.getParameter("password");
			String role = "customer";

			int result = 0;

			try {
				result = UserDao.createUser(role, email, AuthenticationServlet.hashPassword(password));
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

			ResponseJSON.sendResponse(response, "Successfuly created the account.");
		} else {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			response.getWriter().write("Route not found");
		}
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

	public static String hashPassword(String password) {
		int workFactor = 12;

		return BCrypt.hashpw(password, BCrypt.gensalt(workFactor));
	}

	public static boolean checkPassword(String password, String hashedPassword) {
		return BCrypt.checkpw(password, hashedPassword);
	}
}
