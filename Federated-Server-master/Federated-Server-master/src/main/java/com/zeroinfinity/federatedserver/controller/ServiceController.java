package com.zeroinfinity.federatedserver.controller;

import org.apache.http.HttpHeaders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zeroinfinity.federatedserver.service.RestServerService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;

@RestController
@RequestMapping(value = "${service.endpoint}")
@Api(tags = "Service Controller")
public class ServiceController {

	private static final Logger logger = LoggerFactory.getLogger(ServiceController.class);

	@Autowired
	private RestServerService restServerService;

	@ApiOperation(value = "Get all the machine learning models", notes = "Get all the machine learning models", authorizations = {
			@Authorization(value = "jwtToken") })
	@GetMapping("/models")
	public ResponseEntity<Object> allModels() throws Exception {
		logger.info("Request received to get all the machine learning models @ V1");

		try {

			Object result = restServerService.allModel();

			if (result == null) {
				return ResponseEntity.badRequest().header(HttpHeaders.CONTENT_TYPE, "application/json")
						.body("Some internal error occured, please try again later.");
			} else {
				return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, "application/json").body(result);
			}

		} catch (Exception ex) {
			logger.error("exception - method: allModels -> {} ", ex);
		}

		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}

	@ApiOperation(value = "Get the specific machine learning model", notes = "Get the specific machine learning model", authorizations = {
			@Authorization(value = "jwtToken") })
	@GetMapping("/model/{modelName}")
	public ResponseEntity<Object> model(@PathVariable String modelName) throws Exception {
		logger.info("Request received to get the specific machine learning model @ V1");

		try {

			Object result = restServerService.specificModel(modelName);

			if (result == null) {
				return ResponseEntity.badRequest().header(HttpHeaders.CONTENT_TYPE, "application/json")
						.body("Some internal error occured, please try again later.");
			} else {
				return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, "application/json").body(result);
			}

		} catch (Exception ex) {
			logger.error("exception - method: allModels -> {} ", ex);
		}

		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}

	@ApiOperation(value = "Add new machine learning models", notes = "Add new machine learning models", authorizations = {
			@Authorization(value = "jwtToken") })
	@PostMapping("/model/add/{name}/{description}")
	public ResponseEntity<Object> addModel(@PathVariable String name, @PathVariable String description)
			throws Exception {
		logger.info("Request received to add new machine learning model @ V1");

		try {

			Object result = restServerService.addModel(name, description);

			if (result == null) {
				return ResponseEntity.badRequest().header(HttpHeaders.CONTENT_TYPE, "application/json")
						.body("Some internal error occured, please try again later.");
			} else {
				return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, "application/json").body(result);
			}

		} catch (Exception ex) {
			logger.error("exception - method: addModel -> {} ", ex);
		}

		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}

	@ApiOperation(value = "Pull and sync machine learning models from clients", notes = "Pull and sync machine learning models from clients", authorizations = {
			@Authorization(value = "jwtToken") })
	@GetMapping("/model/sync")
	public ResponseEntity<Object> syncModel() throws Exception {
		logger.info("Request received to pull and sync machine learning model from clients @ V1");

		try {

			Object result = restServerService.pullAndSyncModel();

			if (result == null) {
				return ResponseEntity.badRequest().header(HttpHeaders.CONTENT_TYPE, "application/json")
						.body("Some internal error occured, please try again later.");
			} else {
				return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, "application/json").body(result);
			}

		} catch (Exception ex) {
			logger.error("exception - method: syncModel -> {} ", ex);
		}

		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}

	@ApiOperation(value = "Pull and sync machine learning models for client", notes = "Pull and sync machine learning models for client", authorizations = {
			@Authorization(value = "jwtToken") })
	@GetMapping("/model/sync/{modelVersion}")
	public ResponseEntity<Object> syncModel(@PathVariable String modelVersion) throws Exception {
		logger.info("Request received to pull and sync machine learning model for client @ V1");

		try {

			Object result = restServerService.pullAndSyncModel(modelVersion);

			if (result == null) {
				return ResponseEntity.badRequest().header(HttpHeaders.CONTENT_TYPE, "application/json")
						.body("Some internal error occured, please try again later.");
			} else {
				return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, "application/json").body(result);
			}

		} catch (Exception ex) {
			logger.error("exception - method: syncModel -> {} ", ex);
		}

		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}

	@ApiOperation(value = "Check a model version for client-server sync-up", notes = "Check a model version for client-server sync-up", authorizations = {
			@Authorization(value = "jwtToken") })
	@GetMapping("/model/version")
	public ResponseEntity<Object> checkVersion() throws Exception {
		logger.info("Request received to check model version @ V1");

		try {

			Object result = restServerService.checkModelVersion();

			if (result == null) {
				return ResponseEntity.badRequest().header(HttpHeaders.CONTENT_TYPE, "application/json")
						.body("Some internal error occured, please try again later.");
			} else {
				return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, "application/json").body(result);
			}

		} catch (Exception ex) {
			logger.error("exception - method: checkVersion -> {} ", ex);
		}

		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}

	@ApiOperation(value = "Update a model version for client-server sync-up", notes = "Update a model version for client-server sync-up", authorizations = {
			@Authorization(value = "jwtToken") })
	@GetMapping("/model/update/{version}")
	public ResponseEntity<Object> updateVersion(@PathVariable String version) throws Exception {
		logger.info("Request received to update model version @ V1");

		try {

			Object result = restServerService.updateModelVersion(version);

			if (result == null) {
				return ResponseEntity.badRequest().header(HttpHeaders.CONTENT_TYPE, "application/json")
						.body("Some internal error occured, please try again later.");
			} else {
				return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, "application/json").body(result);
			}

		} catch (Exception ex) {
			logger.error("exception - method: updateVersion -> {} ", ex);
		}

		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}

}
