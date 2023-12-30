package com.zeroinfinity.federatedclient.service;

import com.zeroinfinity.federatedclient.entity.ModelVersion;
import com.zeroinfinity.federatedclient.model.OutgoingMessage;
import com.zeroinfinity.federatedclient.model.SaveModelRequest;

public interface RestServerService {
	OutgoingMessage message(String message, String bankName);

	Object allModel();

	Object specificModel(String modelName);

	Object addModel(String name, String description);

	Object pullAndSyncModel();

	Object pullAndSyncModel(String modelVersion);

	ModelVersion checkModelVersion();

	Object saveModelDetail(SaveModelRequest request);

	ModelVersion updateModelVersion(String version);
}
