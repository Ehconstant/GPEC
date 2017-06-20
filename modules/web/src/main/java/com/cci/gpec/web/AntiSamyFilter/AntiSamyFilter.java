package com.cci.gpec.web.AntiSamyFilter;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletRequestWrapper;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.owasp.validator.html.AntiSamy;
import org.owasp.validator.html.CleanResults;
import org.owasp.validator.html.Policy;
import org.owasp.validator.html.PolicyException;

public class AntiSamyFilter implements Filter {

	private static final Logger LOG = Logger.getLogger(AntiSamyFilter.class);

	/**
	 * AntiSamy is unfortunately not immutable, but is threadsafe if we only
	 * call {@link AntiSamy#scan(String taintedHTML, int scanType)}
	 */
	private final AntiSamy antiSamy;

	public AntiSamyFilter() {
		try {
			URL url = this.getClass().getClassLoader()
					.getResource("antisamy.xml");
			Policy policy = Policy.getInstance(url.getFile());
			antiSamy = new AntiSamy(policy);
		} catch (PolicyException e) {
			throw new IllegalStateException(e.getMessage(), e);
		}
	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {

		HttpSession session = ((HttpServletRequest) request).getSession();
		String logged = (String) session.getAttribute("logged");

		String referrer = ((HttpServletRequest) request).getHeader("referer"); // Yes,
																				// with
																				// the
																				// legendary
																				// misspelling.

		if (logged == null) {
			session.setAttribute("logged", "no");
		}
		logged = (String) session.getAttribute("logged");
		String uri = ((HttpServletRequest) request).getRequestURI();

		if (logged.equals("no") && uri.endsWith("versionEssai.xhtml")) {
			((HttpServletResponse) response)
					.sendRedirect(((HttpServletRequest) request)
							.getContextPath() + "/gpec/versionEssaiForm.xhtml");
			return;
		}
		if (logged.equals("no") && !uri.contains("login")
				&& !uri.contains("versionEssai") && uri.endsWith(".xhtml")) {
			((HttpServletResponse) response)
					.sendRedirect(((HttpServletRequest) request)
							.getContextPath() + "/gpec/login.xhtml");
			return;
		}

		if (request instanceof HttpServletRequest) {
			if (StringUtils.isNotBlank(referrer) && referrer.contains("login.xhtml")) {
				chain.doFilter(request, response);
			} else {
				CleanServletRequest cleanRequest = new CleanServletRequest((HttpServletRequest) request, antiSamy);
				chain.doFilter(cleanRequest, response);
			}
		} else {
			chain.doFilter(request, response);
		}
	}

	public void init(FilterConfig filterConfig) throws ServletException {
	}

	public void destroy() {
	}

	/**
	 * Wrapper for a {@link HttpServletRequest} that returns 'safe' parameter
	 * values by passing the raw request parameters through the anti-samy
	 * filter. Should be private
	 */
	public static class CleanServletRequest extends HttpServletRequestWrapper {

		private final AntiSamy antiSamy;

		private CleanServletRequest(HttpServletRequest request,
				AntiSamy antiSamy) {
			super(request);
			this.antiSamy = antiSamy;
		}

		/**
		 * overriding getParameter functions in {@link ServletRequestWrapper}
		 */
		@Override
		public String[] getParameterValues(String name) {
			String[] originalValues = super.getParameterValues(name);
			if (originalValues == null) {
				return null;
			}
			List<String> newValues = new ArrayList<String>(
					originalValues.length);
			for (String value : originalValues) {
				newValues.add(filterString(value));
			}
			return newValues.toArray(new String[newValues.size()]);
		}

		@Override
		@SuppressWarnings("unchecked")
		public Map getParameterMap() {
			Map<String, String[]> originalMap = super.getParameterMap();
			Map<String, String[]> filteredMap = new ConcurrentHashMap<String, String[]>(
					originalMap.size());
			for (String name : originalMap.keySet()) {
				filteredMap.put(name, getParameterValues(name));
			}
			return Collections.unmodifiableMap(filteredMap);
		}

		@Override
		public String getParameter(String name) {
			String potentiallyDirtyParameter = super.getParameter(name);
			return filterString(potentiallyDirtyParameter);
		}

		/**
		 * This is only here so we can see what the original parameters were,
		 * you should delete this method!
		 * 
		 * @return original unwrapped request
		 */
		@Deprecated
		public HttpServletRequest getOriginalRequest() {
			return (HttpServletRequest) super.getRequest();
		}

		/**
		 * @param potentiallyDirtyParameter
		 *            string to be cleaned
		 * @return a clean version of the same string
		 */
		private String filterString(String potentiallyDirtyParameter) {
			if (potentiallyDirtyParameter == null) {
				return null;
			}

			try {
				CleanResults cr = antiSamy.scan(potentiallyDirtyParameter,
						AntiSamy.DOM);
				if (cr.getNumberOfErrors() > 0) {
					LOG.warn("antisamy encountered problem with input: "
							+ cr.getErrorMessages());
				}
				return StringEscapeUtils.unescapeHtml(cr.getCleanHTML());
			} catch (Exception e) {
				throw new IllegalStateException(e.getMessage(), e);
			}
		}
	}
}