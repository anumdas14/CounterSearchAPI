package com.inteliment.counterapi.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

/**
 * This entry point is called once the request missing the authentication but if
 * the request dosn't have the cookie then we send the unauthorized response.
 * 
 * @author Anu Mohan Das
 * @version 1.0
 * @since 2017-04-16
 * 
 */
@Component
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {
	/**
	 * set the logger
	 */
	private static final Logger LOG = Logger
			.getLogger(RestAuthenticationEntryPoint.class.getName());

	/**
	 * commence will be called if the user fails authentication with forbidden
	 * message
	 * 
	 * @param count
	 * @return File
	 * @throws IOException
	 */
	@Override
	public void commence(final HttpServletRequest arg0, final HttpServletResponse arg1,
			final AuthenticationException arg2) throws IOException, ServletException {
		LOG.info("Begin: processing ===========> commence as entry point reference");
		arg1.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");

	}

}