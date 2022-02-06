package com.haud.spamfilter.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.haud.spamfilter.model.Sms;
import com.haud.spamfilter.utils.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.integration.annotation.Transformer;
import org.springframework.stereotype.Service;

@Service("inBoundSmsTransformer")
public class InBoundSmsTransformer {
	private static final Logger logger = LoggerFactory.getLogger(InBoundSmsTransformer.class);

	@Transformer
	public Sms transform(final Message message) throws JsonProcessingException {
		logger.info("transform: transaforming {}", message);
		final String payload = new String(message.getBody());
		return deSerializeSms(payload);
	}

	public static Sms deSerializeSms(final String sms) throws JsonProcessingException {
		logger.debug("deSerializeSms : Deserializing JSON sms to Sms object");
		return Constants.OBJECT_MAPPER.readValue(sms, Sms.class);
	}
}
