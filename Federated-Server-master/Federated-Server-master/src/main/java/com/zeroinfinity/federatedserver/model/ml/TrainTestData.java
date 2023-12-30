package com.zeroinfinity.federatedserver.model.ml;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class TrainTestData {

	private String test_size;
	private String random_state;

	public String getTest_size() {
		return test_size;
	}

	public void setTest_size(String test_size) {
		this.test_size = test_size;
	}

	public String getRandom_state() {
		return random_state;
	}

	public void setRandom_state(String random_state) {
		this.random_state = random_state;
	}

	@Override
	public String toString() {
		return new com.google.gson.Gson().toJson(this);
	}

}
