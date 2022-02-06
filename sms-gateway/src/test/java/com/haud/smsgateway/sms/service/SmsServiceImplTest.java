package com.haud.smsgateway.sms.service;

import com.haud.smsgateway.customer.model.Customer;
import com.haud.smsgateway.customer.service.CustomerService;
import com.haud.smsgateway.sms.config.RabbitMQConfig;
import com.haud.smsgateway.sms.model.Sms;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import reactor.core.publisher.Mono;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class SmsServiceImplTest {

	@Autowired
	private SmsService smsService;
	@MockBean
	private CustomerService customerService;
	@MockBean
	private RabbitTemplate rabbitTemplate;
	@MockBean
	private TopicExchange topic;

	@Test
	void givenSms_whenSendSms_thenShouldSendSms() {
		//Given
		String senderPhone = "+251978667733";
		Sms sms = new Sms(senderPhone, "+251978667732", "Hi");
		Customer customer = new Customer("Abraham", "Maru", senderPhone, "abraham@email.com", UUID.randomUUID().toString());
		final ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
		when(customerService.findCustomerByPhoneNumber(senderPhone)).thenReturn(Mono.just(customer));
		when(topic.getName()).thenReturn(RabbitMQConfig.TOPIC_EXCHANGE_NAME);

		//When
		smsService.sendSms(sms);

		//Then
		verify(customerService, times(1)).findCustomerByPhoneNumber(senderPhone);
		verify(rabbitTemplate, times(2)).convertAndSend(captor.capture(), captor.capture(), any(Message.class));
	}
}