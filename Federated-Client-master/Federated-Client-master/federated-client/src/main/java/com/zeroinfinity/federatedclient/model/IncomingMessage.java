package com.zeroinfinity.federatedclient.model;

public class IncomingMessage {

	private String bankName;
	private String message;

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "IncomingMessage [bankName=" + bankName + ", message=" + message + "]";
	}

}
