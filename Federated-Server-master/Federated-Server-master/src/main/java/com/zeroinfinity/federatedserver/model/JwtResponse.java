package com.zeroinfinity.federatedserver.model;

import java.io.Serializable;

/*JwtResponse is used for creating a response
 *  containing the JWT, its validity Time and expiry date
 *  to be returned to the user.*/

public class JwtResponse implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6797158641082630527L;

	private String token;
	private String expiry;

	public JwtResponse(String token, String expiry) {
		this.token = token;
		this.expiry = expiry;
	}

	public String getToken() {
		return token;
	}

	public String getExpiry() {
		return expiry;

	}

	@Override
	public String toString() {
		return new com.google.gson.Gson().toJson(this);
	}

}
