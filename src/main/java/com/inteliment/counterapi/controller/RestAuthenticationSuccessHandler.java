package com.inteliment.counterapi.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.util.StringUtils;

/**
 * RestAuthenticationSuccessHandler will be called once the request is
 * authenticated. If it is not, the request will be redirected to authenticate
 * entry point
 * 
 * @author Anu Mohan Das
 * @version 1.0
 * @since 2017-04-16
 * 
 */
public class RestAuthenticationSuccessHandler extends
		SimpleUrlAuthenticationSuccessHandler {

	private static final Logger LOG = Logger
			.getLogger(RestAuthenticationSuccessHandler.class.getName());

	private static RequestCache requestCache = new HttpSessionRequestCache();

	/**
	 * onAuthenticationSuccess is called when the user is authenticated. This
	 * file can be used to further redirect request to a specific url
	 * 
	 * @param HttpServletRequest
	 *            request, HttpServletResponse response, Authentication
	 *            authentication
	 * @throws ServletException
	 *             ,IOException
	 */
	@Override
	public void onAuthenticationSuccess(final HttpServletRequest request,
			final HttpServletResponse response,
			final Authentication authentication) throws ServletException,
			IOException {

		LOG.info("Begin: processing ==============> onAuthenticationSuccess");
		final SavedRequest savedRequest = requestCache.getRequest(request,
				response);

		if (savedRequest == null) {
			clearAuthenticationAttributes(request);
			return;
		}
		final String targetUrl = getTargetUrlParameter();
		if (isAlwaysUseDefaultTargetUrl() || targetUrl != null
				&& StringUtils.hasText(request.getParameter(targetUrl))) {
			requestCache.removeRequest(request, response);
			clearAuthenticationAttributes(request);
			return;
		}
		clearAuthenticationAttributes(request);
		LOG.info("Finish: processing ==============> onAuthenticationSuccess");

	}

	/**
	 * This method sets the request cache
	 * 
	 * @param requestCache
	 */
	public void setRequestCache(final RequestCache requestCache) {
		this.requestCache = requestCache;
	}
}