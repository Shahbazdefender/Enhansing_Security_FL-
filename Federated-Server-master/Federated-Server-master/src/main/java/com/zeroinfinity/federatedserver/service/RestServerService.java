package com.zeroinfinity.federatedserver.service;

import com.zeroinfinity.federatedserver.entity.ModelVersion;
import com.zeroinfinity.federatedserver.model.OutgoingMessage;

public interface RestServerService {
	OutgoingMessage message(String message, String bankName);

	Object allModel();

	Object specificModel(String modelName);

	Object addModel(String name, String description);

	Object pullAndSyncModel();

	Object pullAndSyncModel(String modelVersion);

	ModelVersion checkModelVersion();

	Object updateModelVersion(String version);
}
