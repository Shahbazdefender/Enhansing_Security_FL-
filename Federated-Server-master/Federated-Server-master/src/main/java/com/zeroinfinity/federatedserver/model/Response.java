package com.zeroinfinity.federatedserver.model;

public class Response {

	private String response_code;
	private String response_desc;
	
	public Response(String response_code, String response_desc) {
		this.response_code = response_code;
		this.response_desc = response_desc;
	}
	
	public String getResponse_code() {
		return response_code;
	}
	public void setResponse_code(String response_code) {
		this.response_code = response_code;
	}
	public String getResponse_desc() {
		return response_desc;
	}
	public void setResponse_desc(String response_desc) {
		this.response_desc = response_desc;
	}
	
	
}
