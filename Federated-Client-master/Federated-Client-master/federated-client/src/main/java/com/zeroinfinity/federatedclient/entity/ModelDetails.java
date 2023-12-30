package com.zeroinfinity.federatedclient.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Entity
@Table(name = "model_details")
@JsonInclude(Include.NON_NULL)
public class ModelDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "model_id")
	private Model model;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "model_version_id")
	private ModelVersion modelVersion;

	@Column(name = "test_size", length = 50)
	private String testSize;

	@Column(name = "random_state", length = 50)
	private String randomState;

	@Column(name = "total_data_size", length = 50)
	private String totalDataSize;

	@Column(name = "accuracy", length = 50)
	private String accuracy;

	@Column(name = "n_estimators", length = 50)
	private String nEstimators;

	@Column(name = "bootstrap", length = 50)
	private String bootstrap;

	@Column(name = "max_features", length = 50)
	private String maxFeatures;

	@Column(name = "name", length = 50)
	private String name;

	@Column(name = "request_type", length = 50)
	private String requestType;

	public ModelDetails() {
	}

	public ModelDetails(Model model, ModelVersion modelVersion, String testSize, String randomState,
			String totalDataSize, String accuracy, String nEstimators, String bootstrap, String maxFeatures,
			String name, String requestType) {
		this.model = model;
		this.modelVersion = modelVersion;
		this.testSize = testSize;
		this.randomState = randomState;
		this.totalDataSize = totalDataSize;
		this.accuracy = accuracy;
		this.nEstimators = nEstimators;
		this.bootstrap = bootstrap;
		this.maxFeatures = maxFeatures;
		this.name = name;
		this.requestType = requestType;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Model getModel() {
		return model;
	}

	public void setModel(Model model) {
		this.model = model;
	}

	public ModelVersion getModelVersion() {
		return modelVersion;
	}

	public void setModelVersion(ModelVersion modelVersion) {
		this.modelVersion = modelVersion;
	}

	public String getTestSize() {
		return testSize;
	}

	public void setTestSize(String testSize) {
		this.testSize = testSize;
	}

	public String getRandomState() {
		return randomState;
	}

	public void setRandomState(String randomState) {
		this.randomState = randomState;
	}

	public String getTotalDataSize() {
		return totalDataSize;
	}

	public void setTotalDataSize(String totalDataSize) {
		this.totalDataSize = totalDataSize;
	}

	public String getAccuracy() {
		return accuracy;
	}

	public void setAccuracy(String accuracy) {
		this.accuracy = accuracy;
	}

	public String getnEstimators() {
		return nEstimators;
	}

	public void setnEstimators(String nEstimators) {
		this.nEstimators = nEstimators;
	}

	public String getBootstrap() {
		return bootstrap;
	}

	public void setBootstrap(String bootstrap) {
		this.bootstrap = bootstrap;
	}

	public String getMaxFeatures() {
		return maxFeatures;
	}

	public void setMaxFeatures(String maxFeatures) {
		this.maxFeatures = maxFeatures;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRequestType() {
		return requestType;
	}

	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}

	@Override
	public String toString() {
		return new com.google.gson.Gson().toJson(this);
	}

}
