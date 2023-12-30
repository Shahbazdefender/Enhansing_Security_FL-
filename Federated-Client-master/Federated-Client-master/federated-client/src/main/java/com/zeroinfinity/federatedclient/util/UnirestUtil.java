package com.zeroinfinity.federatedclient.util;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.apache.http.HttpHeaders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.zeroinfinity.federatedclient.model.JwtRequest;

@Component
public class UnirestUtil {
	private static final Logger LOG = LoggerFactory.getLogger(UnirestUtil.class);

	@Value("${client.username:my default value}")
	private String[] username;

	@Value("${client.auth.urls:my default value}")
	private String[] authUrls;

	@Value("${client.sync.urls:my default value}")
	private String[] syncUrls;

	@Value("${client.version.urls:my default value}")
	private String[] versionUrls;

	private String[] callUrl;

	public List<String> get(String type, String modelVersion, List<String> params) {
		LOG.info("GET: params: {}", params);

		List<String> responses = new ArrayList<>();
		List<Future<HttpResponse<String>>> futures = new ArrayList<>();

		if (type.equalsIgnoreCase("sync")) {
			callUrl = syncUrls;
		} else if (type.equalsIgnoreCase("version")) {
			callUrl = versionUrls;
		}

		int i = 0;
		for (String url : callUrl) {
			Future<HttpResponse<String>> future = Unirest.get(url + '/' + modelVersion)
					.header(HttpHeaders.CONTENT_TYPE, "application/json")
					.header(HttpHeaders.AUTHORIZATION, "Bearer " + params.get(i)).asStringAsync();
			futures.add(future);
			i++;
		}

		try {
			for (Future<HttpResponse<String>> future : futures) {
				HttpResponse<String> response = future.get();
				responses.add(response.getBody());
			}
		} catch (InterruptedException e) {
			LOG.error("Inside UnirestUtil: get InterruptedException error: {}", e);
		} catch (ExecutionException e) {
			LOG.error("Inside UnirestUtil: get ExecutionException error: {}", e);
		}

		return responses;
	}

	public List<String> post(List<String> params) {
		LOG.info("POST: params: {}", params);

		List<String> responses = new ArrayList<>();
		List<Future<HttpResponse<String>>> futures = new ArrayList<>();

		JwtRequest tokenRequest = new JwtRequest();
		tokenRequest.setPassword(params.get(0));

		int i = 0;
		for (String url : authUrls) {
			tokenRequest.setUsername(username[i]);
			Future<HttpResponse<String>> future = Unirest.post(url).header(HttpHeaders.CONTENT_TYPE, "application/json")
					.body(tokenRequest.toString()).asStringAsync();
			futures.add(future);
			i++;
		}

		try {
			for (Future<HttpResponse<String>> future : futures) {
				HttpResponse<String> response = future.get();
				responses.add(response.getBody());
			}
		} catch (InterruptedException e) {
			LOG.error("Inside UnirestUtil: get InterruptedException error: {}", e);
		} catch (ExecutionException e) {
			LOG.error("Inside UnirestUtil: get ExecutionException error: {}", e);
		}

		return responses;
	}
}
