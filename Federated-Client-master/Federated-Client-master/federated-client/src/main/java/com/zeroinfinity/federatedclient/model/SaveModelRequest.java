package com.zeroinfinity.federatedclient.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class SaveModelRequest {

	private String model_version;
	private List<MLModel> model;

	public String getModel_version() {
		return model_version;
	}

	public void setModel_version(String model_version) {
		this.model_version = model_version;
	}

	public List<MLModel> getModel() {
		return model;
	}

	public void setModel(List<MLModel> model) {
		this.model = model;
	}

	@Override
	public String toString() {
		return new com.google.gson.Gson().toJson(this);
	}

}
