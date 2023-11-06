package com.myapp.controllers;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
			Claims claims = null;
			String endpoint = HeaderCheckFilter.getEndpoint(request);

			// *********************************
			// Token verifications
			// *********************************
			if (authorization != null) {
				String token = authorization.replace("Bearer ", "");

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

			if (claims != null) {
				String customerAccess = "customer";
				String staffAccess = "staff";
				String role = claims.get("role", String.class);

				// *********************************
				// Customer accesses
				// *********************************
				if (customerAccess.equals(role.toLowerCase())) {
					if (endpoint.contains("download") || endpoint.contains("product/create")
							|| (endpoint.contains("product") && request.getMethod().toLowerCase().equals("put"))) {
						servletResponse.getWriter()
								.write("Unauthorized. Only staff members have access to this feature.");
						((HttpServletResponse) servletResponse).setStatus(HttpServletResponse.SC_UNAUTHORIZED);

						response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
						response.getWriter().write("Unauthorized");

						return;
					}
				}

				// *********************************
				// Staff accesses
				// *********************************
				if (staffAccess.equals(role.toLowerCase())) {
					if (endpoint.contains("cart")) {
						servletResponse.getWriter().write("Unauthorized. Only customers have access to this feature.");
						((HttpServletResponse) servletResponse).setStatus(HttpServletResponse.SC_UNAUTHORIZED);

						response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
						response.getWriter().write("Unauthorized");

						return;
					}

				}

				filterChain.doFilter(servletRequest, servletResponse);

				return;
			}

			// *********************************
			// Random user accesses
			// *********************************
			if ((endpoint.contains("product-list") && request.getMethod().toLowerCase().equals("get"))
					|| endpoint.contains("database-initialization") || endpoint.contains("authentication")) {
				filterChain.doFilter(servletRequest, servletResponse);

				return;
			}

			// *********************************
			// Random user tries to access anything that they do not have the permission
			// *********************************
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			response.getWriter().write("Unauthorized");

			return;
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
