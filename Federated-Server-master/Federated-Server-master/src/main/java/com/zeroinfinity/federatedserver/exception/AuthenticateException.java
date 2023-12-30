package com.zeroinfinity.federatedserver.exception;

@SuppressWarnings("serial")
public class AuthenticateException extends RuntimeException {

	public AuthenticateException() {

	}

	public AuthenticateException(String message) {
		super(message);
	}

	public AuthenticateException(Throwable cause) {
		super(cause);
	}

	public AuthenticateException(String message, Throwable cause) {
		super(message, cause);
	}

	public AuthenticateException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
