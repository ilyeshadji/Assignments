package controllers;

import java.io.IOException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.io.DecodingException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.Actor;
import models.Customer;
import models.Staff;
import models.User;

@WebFilter("/*")
public class HeaderCheckFilter implements Filter {
	
	public HeaderCheckFilter() {
		
	}
	
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
			if (token != null && !token.equals("null") && !token.equals("undefined")
					&& !token.equals("{{AUTH_TOKEN}}")) {
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
