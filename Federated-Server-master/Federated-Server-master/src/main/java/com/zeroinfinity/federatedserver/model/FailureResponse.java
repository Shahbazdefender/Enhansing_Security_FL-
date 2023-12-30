package com.zeroinfinity.federatedserver.model;

public class FailureResponse {

	private Response response;
	private String rrn;
	private String stan;

	public FailureResponse(Response response, String rrn, String stan) {
		this.response = response;
		this.rrn = rrn;
		this.stan = stan;
	}

	public Response getResponse() {
		return response;
	}

	public void setResponse(Response response) {
		this.response = response;
	}

	public String getRrn() {
		return rrn;
	}

	public void setRrn(String rrn) {
		this.rrn = rrn;
	}

	public String getStan() {
		return stan;
	}

	public void setStan(String stan) {
		this.stan = stan;
	}

}
