package com.haud.smsgateway.customer.service;

import com.haud.smsgateway.customer.domain.CustomerDto;
import com.haud.smsgateway.customer.exception.CustomerInvalidFieldException;
import com.haud.smsgateway.customer.model.Customer;
import com.haud.smsgateway.customer.repo.CustomerRepository;
import com.haud.smsgateway.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
public class CustomerServiceImpl implements CustomerService {
	private static final Logger logger = LoggerFactory.getLogger(CustomerServiceImpl.class);

	private final CustomerRepository customerRepository;

	@Autowired
	public CustomerServiceImpl(CustomerRepository customerRepository) {
		this.customerRepository = customerRepository;
	}

	@Override
	public Mono<Customer> findCustomerByUuid(String uuid) {
		return this.customerRepository.findByUuid(uuid);
	}

	@Override
	public Mono<Customer> findCustomerByEmail(String email) {
		return this.customerRepository.findByEmail(email);
	}

	@Override
	public Mono<Customer> findCustomerByPhoneNumber(String phoneNumber) {
		return this.customerRepository.findByPhoneNumber(phoneNumber);
	}

	@Override
	public Flux<Customer> findAllCustomers() {
		return this.customerRepository.findAll();
	}

	@Override
	public Mono<Customer>createCustomer(CustomerDto customerDto) throws Exception {
		logger.info("Creating customer: {}", customerDto);

		if (StringUtils.isNullOrEmpty(customerDto.getEmail()))
			throw new CustomerInvalidFieldException("Email");
		if (StringUtils.isNullOrEmpty(customerDto.getFirstName()))
			throw new CustomerInvalidFieldException("First Name");
		if (StringUtils.isNullOrEmpty(customerDto.getLastName()))
			throw new CustomerInvalidFieldException("Last Name");
		if (StringUtils.isNullOrEmpty(customerDto.getPhoneNumber()))
			throw new CustomerInvalidFieldException("Phone Number");

		Customer customer = new Customer(
				customerDto.getFirstName(),
				customerDto.getLastName(),
				customerDto.getPhoneNumber(),
				customerDto.getEmail(),
				UUID.randomUUID().toString()
		);

		Mono<Customer> persistedCustomer = this.customerRepository.save(customer);

		logger.info("customer created");

		return persistedCustomer;
	}
}
