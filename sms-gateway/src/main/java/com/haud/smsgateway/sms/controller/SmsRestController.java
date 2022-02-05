package com.haud.smsgateway.sms.controller;

import com.haud.smsgateway.sms.model.Sms;
import com.haud.smsgateway.sms.service.SmsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sms")
public class SmsRestController {
	private static final Logger logger = LoggerFactory.getLogger(SmsRestController.class);

	private final SmsService smsService;

	@Autowired
	public SmsRestController(SmsService smsService) {
		this.smsService = smsService;
	}

	@PostMapping(value = "/send", consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public void sendSms(@RequestBody Sms sms){
		logger.info("Sending sms");
		this.smsService.sendSms(sms);
	}
}
