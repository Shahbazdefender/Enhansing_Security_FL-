package com.zeroinfinity.federatedclient.model;

public class ValidationErrorResponse {

	private Response response;

	public ValidationErrorResponse(Response response) {
		this.response = response;
	}

	public Response getResponse() {
		return response;
	}

	public void setResponse(Response response) {
		this.response = response;
	}
	
	
}
