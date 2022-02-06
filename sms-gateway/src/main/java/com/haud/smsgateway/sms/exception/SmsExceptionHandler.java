package com.haud.smsgateway.sms.exception;

import com.haud.smsgateway.common.error.CustomErrorType;
import com.haud.smsgateway.customer.exception.CustomerInvalidFieldException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import reactor.core.publisher.Mono;

@RestControllerAdvice
public class SmsExceptionHandler {

	@ExceptionHandler(SmsFieldInvalidException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public Mono<CustomErrorType> handleSmsFieldInvalidException(SmsFieldInvalidException sne){
		CustomErrorType customErrorType = new CustomErrorType(
				sne.getMessage(),
				String.valueOf(HttpStatus.BAD_REQUEST.value()),
				"Field not valid",
				HttpStatus.BAD_REQUEST.name(),
				""
		);

		return Mono.just(customErrorType);
	}

}
