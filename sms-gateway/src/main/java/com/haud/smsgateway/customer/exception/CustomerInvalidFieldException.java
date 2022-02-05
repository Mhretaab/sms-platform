package com.haud.smsgateway.customer.exception;

public class CustomerInvalidFieldException extends RuntimeException{

	public CustomerInvalidFieldException() {
		super();
	}

	public CustomerInvalidFieldException(String message) {
		super(message);
	}
}
