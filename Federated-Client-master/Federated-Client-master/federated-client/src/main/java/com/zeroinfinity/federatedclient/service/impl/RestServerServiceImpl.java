package com.zeroinfinity.federatedclient.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.zeroinfinity.federatedclient.entity.Model;
import com.zeroinfinity.federatedclient.entity.ModelDetails;
import com.zeroinfinity.federatedclient.entity.ModelVersion;
import com.zeroinfinity.federatedclient.model.JwtResponse;
import com.zeroinfinity.federatedclient.model.MLModel;
import com.zeroinfinity.federatedclient.model.OutgoingMessage;
import com.zeroinfinity.federatedclient.model.SaveModelRequest;
import com.zeroinfinity.federatedclient.repo.ModelDetailsRepository;
import com.zeroinfinity.federatedclient.repo.ModelRepository;
import com.zeroinfinity.federatedclient.repo.ModelVersionRepository;
import com.zeroinfinity.federatedclient.service.RestServerService;
import com.zeroinfinity.federatedclient.util.UnirestUtil;

@Service
public class RestServerServiceImpl implements RestServerService {

	private static final Logger LOG = LoggerFactory.getLogger(RestServerServiceImpl.class);

	@Value("${jwt.user:my default value}")
	private String clientName;

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
	public Object allModel() {
		LOG.info("Incoming request to pull all the model parameters.");
		Object result = null;
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
			result = modelRepository.findByNameAndIsActive(modelName, "isActive");

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
		LOG.info("Incoming request to pull models from server.");

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
		List<ModelDetails> modelDetails = modelDetailsRepository
				.findByModelVersionVersionAndRequestTypeIsNull(modelVersion);

		// response back
		return modelDetails;
	}

	@Override
	public ModelVersion checkModelVersion() {
		LOG.info("Incoming request to check the model version.");

		ModelVersion version = null;
		version = modelVersionRepository.findByStatus("Active");

		if (version == null) {
			version = new ModelVersion("version1", "Active");
			try {
				version = modelVersionRepository.saveAndFlush(version);
			} catch (Exception e) {
				LOG.error("Inside method: checkModelVersion error: {}", e);
			}
		} else {
			LOG.info("Active version found: {}", version.getVersion());
		}

		return version;
	}

	@Override
	public Object saveModelDetail(SaveModelRequest request) {
		LOG.info("Incoming request to save the model details.");

		// fetch the Active status model version
		ModelVersion version = null;
		version = modelVersionRepository.findByVersionAndStatus(request.getModel_version(), "Active");

		LOG.info("Active model version {} found", version.getVersion());

		// version. store the model_version_id in model_details table
		List<ModelDetails> listModelDetails = new ArrayList<>();

		// fetch the model from the table using request's model name - it is necessary
		// to store the model's id in the model_details table
		List<String> modelNames = new ArrayList<>();
		for (MLModel model : request.getModel()) {
			modelNames.add(model.getName());
		}
		Optional<List<Model>> listOfModels = modelRepository.findByNameInAndIsActive(modelNames, "Active");
		HashMap<String, Model> modelMap = new HashMap<>();
		if (listOfModels.isPresent()) {
			for (int i = 0; i < listOfModels.get().size(); i++) {
				modelMap.put(listOfModels.get().get(i).getName(), listOfModels.get().get(i));
			}
		}

		// fetch the request, plot in the model
		for (int i = 0; i < request.getModel().size(); i++) {
			ModelDetails modelDetails = new ModelDetails();
			modelDetails.setModelVersion(version);
			modelDetails.setAccuracy(null);
			modelDetails.setBootstrap(null);
			modelDetails.setMaxFeatures(null);
			modelDetails.setModel(modelMap.get(request.getModel().get(i).getName()));
			modelDetails.setnEstimators(null);
			modelDetails.setRandomState(request.getModel().get(i).getTrain_test_data().getRandom_state());
			modelDetails.setTestSize(request.getModel().get(i).getTrain_test_data().getTest_size());
			modelDetails.setTotalDataSize(null);
			modelDetails.setName(clientName);
			listModelDetails.add(modelDetails);
		}

		// plotting the model, run the saveAllAndFlush method of model_details
		// repository to save and flush
		List<ModelDetails> response = modelDetailsRepository.saveAll(listModelDetails);

		// it will return an saving object, return it to the requester
		return response;
	}

	@Override
	public ModelVersion updateModelVersion(String newVersion) {
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
			LOG.error("Inside method: checkModelVersion-2 error: {}", e);
		}

		return version;
	}

}
