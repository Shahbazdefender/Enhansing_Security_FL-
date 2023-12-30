package com.zeroinfinity.federatedclient.config;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zeroinfinity.federatedclient.model.Response;
import com.zeroinfinity.federatedclient.model.ValidationErrorResponse;
import com.zeroinfinity.federatedclient.util.CommonUtil;
import com.zeroinfinity.federatedclient.util.JwtUtil;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;

/*For any incoming request this Filter class gets executed.
 * It checks if the request has a valid JWT token.
 * If it has a valid JWT Token then it sets the Authentication in the context,
 * to specify that the current user is authenticated.
*/
@Component
public class JwtRequestFilter implements Filter {
	private static final Logger logger = LoggerFactory.getLogger(JwtRequestFilter.class);
	@Autowired
	private JwtUtil JwtUtil;

	@Autowired
	private CommonUtil util;

	@Autowired
	private UserAuthDetailsService userDetailsService;

	@Value("${allowedIps}")
	private String allowedIps;

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		HttpServletResponse httpServletResponse = (HttpServletResponse) response;
		HttpServletRequest httpServletRequest = (HttpServletRequest) request;

		if (!util.isIpAllowed(request.getRemoteAddr())) {
			handleException(httpServletResponse, HttpStatus.OK.value(), "----Access Denied: Invalid IP----");
		} else {
			final String authorizationHeader = httpServletRequest.getHeader("Authorization");
			String username = null;
			String jwt = null;
			UserDetails userDetails = null;
			try {
				if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {

					jwt = authorizationHeader.substring(7);
					username = JwtUtil.getUsernameFromToken(jwt);
				}
				if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

					userDetails = userDetailsService.loadUserByUsername(username);

					/*
					 * if token is valid configure Spring Security to manually set authentication
					 */

					if (JwtUtil.validateToken(jwt, userDetails)) {
						logger.trace("token successfully validated : User : " + username);
						UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
								userDetails, null, userDetails.getAuthorities());
						usernamePasswordAuthenticationToken
								.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));

						/*
						 * After setting the Authentication in the context, we specify that the current
						 * user is authenticated. So it passes the Spring Security Configurations
						 * successfully.
						 */

						SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

					}

				}
				chain.doFilter(httpServletRequest, httpServletResponse);

			} catch (ExpiredJwtException ex) {
				logger.warn("User:" + username + " token has expired");
				handleException(httpServletResponse, HttpStatus.OK.value(), "----Token Expired----");
			}

			catch (SignatureException e) {
				logger.error("JWT signature does not match locally computed signature");
				handleException(httpServletResponse, HttpStatus.OK.value(), "----Invalid Token signature----");
			} catch (MalformedJwtException e) {
				logger.error("JWT signature does not match locally computed signature");
				handleException(httpServletResponse, HttpStatus.OK.value(), "----Malformed Token ----");
			}

			catch (NullPointerException e) {
				logger.warn(e.getMessage());
				handleException(httpServletResponse, HttpStatus.BAD_REQUEST.value(), "----Generate New token----");
			}

		}

	}

	@ExceptionHandler
	public ResponseEntity<ValidationErrorResponse> handleException(HttpServletResponse res, int status, String error)
			throws IOException {
		logger.info("Exception occurred in JwtFilter : {}", error);
		ValidationErrorResponse errorResponse = new ValidationErrorResponse(new Response("068", error));
		// using objectMapperto overwrite the default response in catching exception &
		// setting our own

		String tokenResponse = new ObjectMapper().writeValueAsString(errorResponse);
		res.addHeader(HttpHeaders.CONTENT_TYPE, "application/json");
		res.getWriter().print(tokenResponse);
		res.setStatus(status);
		return new ResponseEntity<ValidationErrorResponse>(errorResponse, HttpStatus.OK);
	}

}
