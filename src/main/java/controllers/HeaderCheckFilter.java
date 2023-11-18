package controllers;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.Actor;
import models.Customer;
import models.Staff;
import models.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.io.DecodingException;
import io.jsonwebtoken.security.SignatureException;

import java.io.IOException;

@WebFilter("/*")
public class HeaderCheckFilter implements Filter {
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// Initialization code (if needed)
	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
			throws IOException, ServletException {

		if (servletRequest instanceof HttpServletRequest) {
			HttpServletResponse response = (HttpServletResponse) servletResponse;
			HttpServletRequest request = (HttpServletRequest) servletRequest;

			String authorization = request.getHeader("Authorization");
			String token = null;

			if (authorization != null) {
				token = authorization.replace("Bearer ", "");
			}

			Claims claims = null;
			String endpoint = request.getServletPath();

			// *********************************
			// Token verifications
			// *********************************
			if (token != null && !token.equals("null") && !token.equals("undefined")) {
				try {
					claims = Jwts.parser().setSigningKey(AuthenticationServlet.getPrivateKey()).parseClaimsJws(token)
							.getBody();

				} catch (SignatureException e) {
					response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
					response.getWriter().write("Unauthorized.");

					return;
				} catch (DecodingException e) {
					response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
					response.getWriter().write("Unauthorized.");

					return;
				} catch (MalformedJwtException e) {
					response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
					response.getWriter().write("Unauthorized.");

					return;
				} catch (Exception e) {
					response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
					response.getWriter().write("Unauthorized.");

					return;
				}
			}

			// *********************************
			// Random user accesses
			// *********************************
			Actor actor = new Actor();

			if (claims != null) {
				String customerAccess = "customer";
				String staffAccess = "staff";
				String role = claims.get("role", String.class);

				// *********************************
				// Customer accesses
				// *********************************
				if (customerAccess.equals(role.toLowerCase())) {
					actor = new Customer();
				}

				// *********************************
				// Staff accesses
				// *********************************
				else if (staffAccess.equals(role.toLowerCase())) {
					actor = new Staff();
				}
			}

			User user = new User(actor.getPermissions());

			if (!user.can(endpoint, request.getMethod().toLowerCase())) {
				response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				response.getWriter().write("Unauthorized");

				return;
			}

			filterChain.doFilter(servletRequest, servletResponse);
		}
	}

	@Override
	public void destroy() {
		// Cleanup code (if needed)
	}

	static String getEndpoint(HttpServletRequest request) {
		int lastIndex = request.getRequestURL().lastIndexOf("Assignment1");
		return request.getRequestURL().substring(lastIndex + 1);
	}
}
