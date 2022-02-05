package com.haud.smsgateway.sms.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.haud.smsgateway.customer.exception.CustomerNotFoundException;
import com.haud.smsgateway.customer.service.CustomerService;
import com.haud.smsgateway.sms.config.RabbitMQConfig;
import com.haud.smsgateway.sms.exception.SmsFieldInvalidException;
import com.haud.smsgateway.sms.model.Sms;
import com.haud.smsgateway.utils.Constants;
import com.haud.smsgateway.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SmsServiceImpl implements SmsService {
	private static final Logger logger = LoggerFactory.getLogger(SmsServiceImpl.class);

	private final CustomerService customerService;
	private final RabbitTemplate template;
	private final TopicExchange topic;

	@Autowired
	public SmsServiceImpl(CustomerService customerService, RabbitTemplate template, TopicExchange topic) {
		this.customerService = customerService;
		this.template = template;
		this.topic = topic;
	}

	@Override
	public void sendSms(final Sms sms) {
		logger.info("Sending sms: {}", sms);

		if (StringUtils.isNullOrEmpty(sms.getSenderPhone()))
			throw new SmsFieldInvalidException("Sender Phone Number");
		if (StringUtils.isNullOrEmpty(sms.getReceiverPhone()))
			throw new SmsFieldInvalidException("Receiver Phone Number");
		if (StringUtils.isNullOrEmpty(sms.getText()))
			throw new SmsFieldInvalidException("Text");

		this.customerService.findCustomerByPhoneNumber(sms.getSenderPhone()).subscribe(
				customer -> {
					if (customer == null) {
						throw new CustomerNotFoundException("Customer Not Found");
					} else {
						final MessageProperties messageProperties = new MessageProperties();
						messageProperties.setContentType(MessageProperties.CONTENT_TYPE_JSON);

						try {
							String smsString = Constants.OBJECT_MAPPER.writeValueAsString(sms);
							final Message amqpMessage = MessageBuilder.withBody(smsString.getBytes())
									.andProperties(messageProperties).build();
							template.convertAndSend(topic.getName(), RabbitMQConfig.SPAM_FILTER_BINDING_KEY,
									amqpMessage);
							template.convertAndSend(topic.getName(), RabbitMQConfig.CHARGING_MODULE_BINDING_KEY,
									amqpMessage);
						} catch (JsonProcessingException e) {
							logger.info("sendSms(): {}", e.getMessage());
						}
					}
				}
		);
	}
}
