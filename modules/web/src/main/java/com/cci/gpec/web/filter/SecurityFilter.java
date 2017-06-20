package com.cci.gpec.web.filter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class SecurityFilter implements Filter {

	private FilterConfig config;

	/** Creates new SessionFilter */
	public SecurityFilter() {
	}

	public void init(FilterConfig filterConfig) throws ServletException {

		this.config = filterConfig;
	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws java.io.IOException, ServletException {

		HttpSession session = ((HttpServletRequest) request).getSession();
		ServletContext context = config.getServletContext();
		String logged = (String) session.getAttribute("logged");
		if (logged == null) {
			session.setAttribute("logged", "no");
		}

		logged = (String) session.getAttribute("logged");
		String uri = ((HttpServletRequest) request).getRequestURI();
		if (logged.equals("no") && !uri.contains("login")
				&& uri.endsWith(".xhtml")) {
			((HttpServletResponse) response)
					.sendRedirect(((HttpServletRequest) request)
							.getContextPath() + "/gpec/login.xhtml");

		}

		// chain.doFilter(new XSSRequestWrapper((HttpServletRequest) request),
		// response);
		chain.doFilter(request, response);
	}

	public void destroy() {
		/*
		 * called before the Filter instance is removed from service by the web
		 * container
		 */
	}
}