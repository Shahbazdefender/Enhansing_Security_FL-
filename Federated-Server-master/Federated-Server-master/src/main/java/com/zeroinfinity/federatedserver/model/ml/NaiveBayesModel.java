package com.zeroinfinity.federatedserver.model.ml;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class NaiveBayesModel {

	private String name;
	private TrainTestData train_test_data;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public TrainTestData getTrain_test_data() {
		return train_test_data;
	}

	public void setTrain_test_data(TrainTestData train_test_data) {
		this.train_test_data = train_test_data;
	}

	@Override
	public String toString() {
		return new com.google.gson.Gson().toJson(this);
	}

}
