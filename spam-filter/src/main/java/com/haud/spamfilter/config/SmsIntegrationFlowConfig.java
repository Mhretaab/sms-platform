package com.haud.spamfilter.config;

import com.haud.spamfilter.model.Sms;
import com.haud.spamfilter.service.SmsDelivery;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.amqp.dsl.Amqp;
import org.springframework.integration.core.GenericSelector;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.Transformers;

@Configuration
public class SmsIntegrationFlowConfig {
	@Autowired
	private SimpleMessageListenerContainer workListenerContainer;

	@Autowired
	private SmsDelivery smsDelivery;

	@Bean
	public IntegrationFlow inboundFlow() {
		return IntegrationFlows.from(
						Amqp.inboundAdapter(workListenerContainer))
				.transform(Transformers.fromJson(Sms.class))
				.log()
				.filter(onlyToEthiopia())
				.handle(smsDelivery)
				.get();
	}

	@Bean
	public GenericSelector<Sms> onlyToEthiopia() {
		//TODO: The Filter logic goes here
		return sms -> sms.getReceiverPhone().startsWith("+251") || sms.getReceiverPhone().startsWith("00251");
	}

}