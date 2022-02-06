package com.haud.spamfilter.service;

import com.haud.spamfilter.model.Sms;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.stereotype.Service;

@Service("smsDelivery")
public class SmsDelivery {
	private static final Logger logger = LoggerFactory.getLogger(SmsDelivery.class);

	@ServiceActivator
	public void send(Sms sms) {
		logger.info("Sending sms to telecom delivery service");
		//TODO: Logic to send to telecom infrastructure
		logger.info("sent: {}", sms);
	}
}