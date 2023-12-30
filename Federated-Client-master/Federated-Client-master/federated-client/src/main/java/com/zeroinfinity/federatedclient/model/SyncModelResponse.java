package com.zeroinfinity.federatedclient.model;

import java.io.Serializable;

/*JwtResponse is used for creating a response
 *  containing the JWT, its validity Time and expiry date
 *  to be returned to the user.*/

public class SyncModelResponse implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6797158641082630527L;

	private String jwt;
	private String expiry;

	public SyncModelResponse(String jwt, String expiry) {
		this.jwt = jwt;
		this.expiry = expiry;
	}

	public String getToken() {
		return this.jwt;
	}

	public String getExpiry() {
		return expiry;

	}

}
