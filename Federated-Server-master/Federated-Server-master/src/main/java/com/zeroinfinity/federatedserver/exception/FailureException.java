package com.zeroinfinity.federatedserver.exception;

public class FailureException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private final String resCode;
	private final String resDesc;
	private final String rrn;
	private final String stan;

	public FailureException(String resCode, String resDesc, String rrn, String stan) {
		super(resDesc);
		this.resCode = resCode;
		this.resDesc = resDesc;
		this.rrn = rrn;
		this.stan = stan;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getResCode() {
		return resCode;
	}

	public String getResDesc() {
		return resDesc;
	}

	public String getRrn() {
		return rrn;
	}

	public String getStan() {
		return stan;
	}

}
