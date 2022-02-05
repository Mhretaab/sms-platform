package com.haud.smsgateway.sms.exception;

public class SmsFieldInvalidException  extends RuntimeException{

	public SmsFieldInvalidException() {
		super();
	}

	public SmsFieldInvalidException(String message) {
		super(message);
	}
}
