package com.zeroinfinity.federatedserver.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CommonUtil {
	private static final Logger logger = LoggerFactory.getLogger(CommonUtil.class);

	private static int counter = (new Random()).nextInt(100);
	static final int RETRIEVAL_REFERENCE_LENGTH = 12;
	private static String sformat = "ddHHmms";

	@Value("${allowedIps}")
	private String allowedIps;

	public String generateTicketNo() {
		logger.info("METHOD:: generateTicketNo");
		if (counter < 9999) {
			counter++;
		} else {
			counter = 0;
		}
		return org.apache.commons.lang3.StringUtils.leftPad((getDateTime() + String.valueOf(counter)),
				RETRIEVAL_REFERENCE_LENGTH, '0');
	}

	public static String getDateTime() {
		DateFormat dateFormat = new SimpleDateFormat(sformat);
		return dateFormat.format(new Date());
	}

	public Boolean isIpAllowed(String clientIp) {
		Boolean allow = false;
		if (allowedIps.equals("*"))
			return true;
		List<String> allowedIpsList = Arrays.asList(allowedIps.split(","));
		allow = allowedIpsList.contains(clientIp);
		return allow;

	}

}
