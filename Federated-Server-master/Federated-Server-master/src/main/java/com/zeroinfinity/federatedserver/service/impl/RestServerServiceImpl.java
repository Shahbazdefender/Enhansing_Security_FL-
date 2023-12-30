package com.zeroinfinity.federatedserver.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.zeroinfinity.federatedserver.entity.Model;
import com.zeroinfinity.federatedserver.entity.ModelDetails;
import com.zeroinfinity.federatedserver.entity.ModelVersion;
import com.zeroinfinity.federatedserver.model.JwtResponse;
import com.zeroinfinity.federatedserver.model.OutgoingMessage;
import com.zeroinfinity.federatedserver.repo.ModelDetailsRepository;
import com.zeroinfinity.federatedserver.repo.ModelRepository;
import com.zeroinfinity.federatedserver.repo.ModelVersionRepository;
import com.zeroinfinity.federatedserver.service.RestServerService;
import com.zeroinfinity.federatedserver.util.UnirestUtil;

@Service
public class RestServerServiceImpl implements RestServerService {

	private static final Logger LOG = LoggerFactory.getLogger(RestServerServiceImpl.class);
	
	@Autowired
	private UnirestUtil unirest;

	@Autowired
	private ModelRepository modelRepository;

	@Autowired
	private ModelVersionRepository modelVersionRepository;

	@Autowired
	private ModelDetailsRepository modelDetailsRepository;

	@Override
	public OutgoingMessage message(String message, String bankName) {
		LOG.info("Incoming signing request. Bank: {} & Message: {}", bankName, message);

		OutgoingMessage outgoingMessage = new OutgoingMessage();
		String signedMessage = "new message will be signed here.";

		try {
			outgoingMessage.setMessage(signedMessage);
			LOG.info("Incoming message signed successfully.");
		} catch (Exception e) {
			LOG.error("Cannot perform the signing process. error: {}", e);
		}

		return outgoingMessage;
	}

	@Override
	public List<Model> allModel() {
		LOG.info("Incoming request to pull all the model parameters.");
		List<Model> result = null;
		try {
			result = modelRepository.findByIsActive("Active");

		} catch (Exception e) {
			LOG.error("Inside method: allModel error: {}", e);
		}
		return result;
	}

	@Override
	public Object specificModel(String modelName) {
		LOG.info("Incoming request to pull the specific model parameters.");
		Object result = null;
		try {
			result = modelRepository.findByNameAndIsActive(modelName, "Active");

		} catch (Exception e) {
			LOG.error("Inside method: allModel error: {}", e);
		}
		return result;
	}

	@Override
	public Object addModel(String name, String description) {
		LOG.info("Incoming request to save the new model in database.");
		Object result = null;
		Model model = new Model();
		model.setName(name);
		model.setDescription(description);
		try {
			result = modelRepository.saveAndFlush(model);
		} catch (Exception e) {
			LOG.error("Inside method: addModel error: {}", e);
		}
		return result;
	}

	@Override
	public Object pullAndSyncModel() {
		LOG.info("Incoming request to pull models from clients.");

		String modelVersion = "Version1"; // just in case of no active version found.
		ModelVersion version = modelVersionRepository.findByStatus("Active");

		if (version != null) {
			modelVersion = version.getVersion();
		}

		List<String> responses = unirest.post(Arrays.asList("admin@123"));
		List<String> tokens = new ArrayList<>();

		int i = 1;
		for (String response : responses) {

			Gson g = new Gson();
			JsonObject jsonObject = JsonParser.parseString(response).getAsJsonObject();
			JwtResponse tokenResponse = g.fromJson(jsonObject, JwtResponse.class);
			LOG.info("client{} - token: {} - expiry: {}", i, tokenResponse.getToken(), tokenResponse.getExpiry());
			tokens.add(tokenResponse.getToken());
			i++;
		}

		List<String> postResponses = unirest.get("sync", modelVersion, tokens);

		List<ModelDetails> modelDetails = new ArrayList<>();

		i = 0;
		for (String response : postResponses) {

			Gson g = new Gson();
			JsonArray jsonArray = JsonParser.parseString(response).getAsJsonArray(); // yahan phata hy

			for (int j = 0; j < jsonArray.size(); j++) {
				JsonObject jsonObject = jsonArray.get(j).getAsJsonObject();
				ModelDetails modelDetail = g.fromJson(jsonObject, ModelDetails.class);
				modelDetails.add(modelDetail);
			}
			i++;
		}

		// plotting the model, run the saveAllAndFlush method of model_details
		// repository to save and flush

		List<ModelDetails> listModelDetails = new ArrayList<>();
		for (i = 0; i < modelDetails.size(); i++) {
			ModelDetails modelDetail = new ModelDetails();
			modelDetail.setAccuracy(modelDetails.get(i).getAccuracy());
			modelDetail.setBootstrap(modelDetails.get(i).getBootstrap());
			modelDetail.setMaxFeatures(modelDetails.get(i).getMaxFeatures());

			Model model = modelRepository.findByNameAndIsActive(modelDetails.get(i).getModel().getName(), "Active");
			modelDetail.setModel(model); // need to check this

			version = modelVersionRepository.findByVersionAndStatus(modelDetails.get(i).getModelVersion().getVersion(),
					"Active");
			modelDetail.setModelVersion(version); // need to check this

			modelDetail.setName(modelDetails.get(i).getName());
			modelDetail.setnEstimators(modelDetails.get(i).getnEstimators());
			modelDetail.setRandomState(modelDetails.get(i).getRandomState());
			modelDetail.setTestSize(modelDetails.get(i).getTestSize());
			modelDetail.setTotalDataSize(modelDetails.get(i).getTotalDataSize());
			modelDetail.setRequestType("Incoming");
			listModelDetails.add(modelDetail);
		}
		listModelDetails = modelDetailsRepository.saveAll(listModelDetails);

		return listModelDetails;
	}

	@Override
	public Object pullAndSyncModel(String modelVersion) {
		LOG.info("Incoming request to pull models for server. model version: {}", modelVersion);

		// check the model version present in the table with Active status
		ModelVersion version = null;
		version = modelVersionRepository.findByVersionAndStatus(modelVersion, "Active");
		if (version == null) {
			return "Requested " + modelVersion + ": is not Active.";
		}

		// if exist, then fetch the data from the model details table
		List<ModelDetails> modelDetails = modelDetailsRepository.findByModelVersionVersionAndRequestType(modelVersion,
				"Incoming");

		// response back
		return modelDetails;
	}

	@Override
	public ModelVersion checkModelVersion() {
		LOG.info("Incoming request to check the model version.");

		ModelVersion version = null;
		version = modelVersionRepository.findByStatus("Active");

		if (version == null) {
			LOG.info("No active version found");
		} else {
			LOG.info("Active version found: {}", version.getVersion());
		}

		return version;
	}

	@Override
	public List<ModelVersion> updateModelVersion(String newVersion) {
		LOG.info("Incoming request to update the model version.");

		// fetch the Active status model version
		ModelVersion version = null;
		version = modelVersionRepository.findByStatus("Active");

		if (version != null) {
			LOG.info("Active model version found. Changing the status from Active to InActive ");
			version.setStatus("InActive");
			try {
				version = modelVersionRepository.saveAndFlush(version);
			} catch (Exception e) {
				LOG.error("Inside method: checkModelVersion-1 error: {}", e);
			}
		}

		version = new ModelVersion(newVersion, "Active");
		try {
			version = modelVersionRepository.saveAndFlush(version);
		} catch (Exception e) {
			LOG.error("Inside method: checkModelVersion error: {}", e);
		}

		LOG.info("New Active version activated: {}", version.getVersion());

		// call client's update version api

		List<String> responses = unirest.post(Arrays.asList("admin@123"));
		List<String> tokens = new ArrayList<>();

		int i = 1;
		for (String response : responses) {

			Gson g = new Gson();
			JsonObject jsonObject = JsonParser.parseString(response).getAsJsonObject();
			JwtResponse tokenResponse = g.fromJson(jsonObject, JwtResponse.class);
			LOG.info("client{} - token: {} - expiry: {}", i, tokenResponse.getToken(), tokenResponse.getExpiry());
			tokens.add(tokenResponse.getToken());
			i++;
		}

		List<String> postResponses = unirest.get("version", version.getVersion(), tokens);

		List<ModelVersion> modelVersions = new ArrayList<>();

		i = 0;
		for (String response : postResponses) {

			Gson g = new Gson();
			JsonObject jsonObject = JsonParser.parseString(response).getAsJsonObject();
			ModelVersion modelVersion = g.fromJson(jsonObject, ModelVersion.class);
			modelVersions.add(modelVersion);

		}

		LOG.info("All client model version upgraded to {} successfully.", version.getVersion());

		return modelVersions;
	}

}
