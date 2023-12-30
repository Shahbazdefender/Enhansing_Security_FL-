package com.zeroinfinity.federatedserver.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import com.zeroinfinity.federatedserver.model.FailureResponse;
import com.zeroinfinity.federatedserver.model.Response;
import com.zeroinfinity.federatedserver.model.ValidationErrorResponse;

@ControllerAdvice
public class RestExceptionHandler {

	private static final Logger logger = LoggerFactory.getLogger(RestExceptionHandler.class);

	@ExceptionHandler
	public ResponseEntity<ValidationErrorResponse> handleException(AuthenticateException exc) {
		logger.error("AuthenticateException Handler envoked");
		ValidationErrorResponse errorResponse = new ValidationErrorResponse(new Response("068", exc.getMessage()));
		return new ResponseEntity<ValidationErrorResponse>(errorResponse, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler
	public ResponseEntity<ValidationErrorResponse> handleException(BadCredentialsException exc) {
		logger.error("invalid user exception Handler envoked");
		ValidationErrorResponse errorResponse = new ValidationErrorResponse(
				new Response("068", "invalid username/pass"));
		return new ResponseEntity<ValidationErrorResponse>(errorResponse, HttpStatus.UNAUTHORIZED);
	}

	@ExceptionHandler
	public ResponseEntity<ValidationErrorResponse> handleException(AccessDeniedException exc) {
		logger.error("Access Denied exception Handler envoked");
		ValidationErrorResponse errorResponse = new ValidationErrorResponse(new Response("068", exc.getMessage()));
		return new ResponseEntity<ValidationErrorResponse>(errorResponse, HttpStatus.FORBIDDEN);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	protected ResponseEntity<ValidationErrorResponse> handleGlobalExceptions(MethodArgumentNotValidException ex,
			WebRequest request) {
		logger.error("MethodArgumentNotValidException Handler envoked");
		ValidationErrorResponse errorResponse = new ValidationErrorResponse(
				new Response("068", ex.getBindingResult().getFieldErrors().stream().map(err -> err.getDefaultMessage())
						.collect(java.util.stream.Collectors.joining(","))));

		return new ResponseEntity<ValidationErrorResponse>(errorResponse, HttpStatus.OK);
	}

	@ExceptionHandler
	public ResponseEntity<FailureResponse> handleException(FailureException e) {
		logger.error("failure Exception Handler envoked");
		FailureResponse failureResponse = new FailureResponse(new Response(e.getResCode(), e.getResDesc()), e.getRrn(),
				e.getStan());
		return new ResponseEntity<FailureResponse>(failureResponse, HttpStatus.OK);
	}

	// generic Exception Handler for any exception
	@ExceptionHandler
	public ResponseEntity<ValidationErrorResponse> handleException(Exception exc) {
		logger.error("Generic Exception Handler envoked");
		ValidationErrorResponse errorResponse = new ValidationErrorResponse(new Response("068", exc.getMessage()));
		return new ResponseEntity<ValidationErrorResponse>(errorResponse, HttpStatus.BAD_REQUEST);

	}

}
