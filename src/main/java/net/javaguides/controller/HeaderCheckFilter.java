package net.javaguides.controller;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
			HttpServletRequest request = (HttpServletRequest) servletRequest;

			String headerValue = request.getHeader("Customer-Role");

			if (headerValue != null) {
				String customerAccess = "customer";
				String staffAccess = "staff";
				String endpoint = HeaderCheckFilter.getEndpoint(request);

				// *********************************
				// Customer accesses
				// *********************************
				if (customerAccess.equals(headerValue.toLowerCase())) {
					if (endpoint.contains("download") || endpoint.contains("product/create")
							|| (endpoint.contains("product") && request.getMethod().toLowerCase().equals("put"))) {
						servletResponse.getWriter()
								.write("Unauthorized. Only staff members have access to this feature.");
						((HttpServletResponse) servletResponse).setStatus(HttpServletResponse.SC_UNAUTHORIZED);

						return;
					}

					filterChain.doFilter(servletRequest, servletResponse);
				}

				// *********************************
				// Staff accesses
				// *********************************
				if (staffAccess.equals(headerValue.toLowerCase())) {
					if (endpoint.contains("cart")) {
						servletResponse.getWriter().write("Unauthorized. Only customers have access to this feature.");
						((HttpServletResponse) servletResponse).setStatus(HttpServletResponse.SC_UNAUTHORIZED);

						return;
					}

					filterChain.doFilter(servletRequest, servletResponse);

				}
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
