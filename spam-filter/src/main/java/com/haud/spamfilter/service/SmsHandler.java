package com.haud.spamfilter.service;

import com.haud.spamfilter.model.Sms;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service("smsHandler")
public class SmsHandler {
	private static final Logger logger = LoggerFactory.getLogger(SmsHandler.class);

	public void send(Sms sms) {
		logger.info("Sending sms to telecom delivery service");
		logger.info("sent: {}", sms);
	}
}