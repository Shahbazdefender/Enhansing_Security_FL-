package com.zeroinfinity.federatedclient.controller;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.zeroinfinity.federatedclient.model.JwtRequest;
import com.zeroinfinity.federatedclient.model.JwtResponse;
import com.zeroinfinity.federatedclient.util.JwtUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(tags = "Authentication")
public class JwtAuthenticationController {
	private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationController.class);

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtUtil JwtUtil;

	@Value("${allowedIps}")
	private String allowedIps;

	@Value("${secret}")
	private String secret;

	/*
	 * this end-point authenticate the user, on successful authentication return the
	 * token for security purposes to access other end-points required: user-name &
	 * password
	 */

	@ApiOperation(value = "Get Access Token")
	@PostMapping("/authenticate")
	public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest,
			HttpServletRequest request) throws Exception {

		logger.info("Authenticating : User : " + authenticationRequest.getUsername());

		authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());

		/* fetching token, its validity period & exact expiration time */
		String Token = JwtUtil.generateToken(authenticationRequest.getUsername() + "|" + secret);
		String expiry = JwtUtil.getExpirationInEpochs(Token);
		logger.trace("Token Sent : User: " + authenticationRequest.getUsername());

		return ResponseEntity.ok(new JwtResponse(Token, expiry));
	}

	/*
	 * Using Spring Authentication Manager we authenticate the user-name and
	 * password.If the credentials are valid, only a JWT token is generated &
	 * provided to the client.
	 */

	private void authenticate(String username, String password) throws Exception {
		authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		logger.trace("User: " + username + " authentication successful");
	}

}
