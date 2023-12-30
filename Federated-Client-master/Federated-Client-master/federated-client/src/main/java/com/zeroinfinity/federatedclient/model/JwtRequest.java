package com.zeroinfinity.federatedclient.model;

import java.io.Serializable;

import javax.validation.constraints.NotEmpty;

import io.swagger.annotations.ApiModelProperty;

/*to store the user-name & password provided by the user while authentication */

public class JwtRequest implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@ApiModelProperty(position = 1)
	@NotEmpty(message = "Please provide username")
	private String username;

	@ApiModelProperty(position = 2)
	@NotEmpty(message = "Please provide password")
	private String password;

	// need default constructor for JSON Parsing
	public JwtRequest() {

	}

	public JwtRequest(String username, String password) {
		this.setUsername(username);
		this.setPassword(password);
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return new com.google.gson.Gson().toJson(this);
	}
}
