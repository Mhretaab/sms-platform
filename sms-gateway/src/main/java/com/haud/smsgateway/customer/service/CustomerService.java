package com.haud.smsgateway.customer.service;

import com.haud.smsgateway.customer.model.Customer;
import com.haud.smsgateway.customer.domain.CustomerDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CustomerService {
	Mono<Customer> findCustomerByUuid(String uuid);

	Mono<Customer> findCustomerByEmail(String email);

	Mono<Customer> findCustomerByPhoneNumber(String phoneNumber);

	Flux<Customer> findAllCustomers();

	Mono<Customer> createCustomer(CustomerDto customerDto) throws Exception;
}
