package com.haud.smsgateway.customer.exception;

import com.haud.smsgateway.common.error.CustomErrorType;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import reactor.core.publisher.Mono;

@RestControllerAdvice
public class CustomerExceptionHandler {

    @ExceptionHandler(CustomerNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Mono<CustomErrorType> handleCustomerNotFoundException(CustomerNotFoundException cne){
        CustomErrorType customErrorType = new CustomErrorType(
                cne.getMessage(),
                String.valueOf(HttpStatus.NOT_FOUND.value()),
                "Customer not found",
                HttpStatus.NOT_FOUND.name(),
                ""
        );

        return Mono.just(customErrorType);
    }

    @ExceptionHandler(CustomerInvalidFieldException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Mono<CustomErrorType> handleCustomerInvalidFieldException(CustomerInvalidFieldException cne){
        CustomErrorType customErrorType = new CustomErrorType(
                cne.getMessage(),
                String.valueOf(HttpStatus.BAD_REQUEST.value()),
                "Field not valid",
                HttpStatus.BAD_REQUEST.name(),
                ""
        );

        return Mono.just(customErrorType);
    }
}