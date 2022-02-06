package com.haud.smsgateway.sms.controller;

import com.haud.smsgateway.sms.model.Sms;
import com.haud.smsgateway.sms.service.SmsService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
@WebFluxTest(controllers = SmsRestController.class)
class SmsRestControllerTest {

	@Autowired
	private WebTestClient webTestClient;
	@MockBean
	private SmsService smsService;

	@Test
	void givenSms_whenSendSms_thenShouldSendSms() {
		Sms sms = new Sms("+251978667733", "+251978667732", "Hi");

		webTestClient
				.post().uri("/sms/send")
				.bodyValue(sms)
				.exchange()
				.expectStatus()
				.isOk();

		verify(smsService, times(1)).sendSms(ArgumentMatchers.refEq(sms));
	}
}