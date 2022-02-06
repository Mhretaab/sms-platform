package com.haud.smsgateway.customer.controller;

import com.haud.smsgateway.customer.domain.CustomerDto;
import com.haud.smsgateway.customer.model.Customer;
import com.haud.smsgateway.customer.service.CustomerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/customers")
public class CustomerRestController {
	private static final Logger logger = LoggerFactory.getLogger(CustomerRestController.class);

	private final CustomerService customerService;

	@Autowired
	public CustomerRestController(CustomerService customerService) {
		this.customerService = customerService;
	}

	@GetMapping("/uuid/{uuid}")
	public ResponseEntity<Mono<Customer>> getCustomerByUuid(@PathVariable("uuid") String uuid) {
		Mono<Customer> customer = this.customerService.findCustomerByUuid(uuid);
		HttpStatus status = customer != null ? HttpStatus.OK : HttpStatus.NOT_FOUND;
		return new ResponseEntity<>(customer, status);
	}

	@GetMapping("/phonenumber/{phoneNumber}")
	public ResponseEntity<Mono<Customer>> getCustomerByPhoneNumber(@PathVariable("phoneNumber") String phoneNumber) {
		Mono<Customer> customer = this.customerService.findCustomerByPhoneNumber(phoneNumber);
		HttpStatus status = customer != null ? HttpStatus.OK : HttpStatus.NOT_FOUND;
		return new ResponseEntity<>(customer, status);
	}

	@GetMapping("/email/{email}")
	public ResponseEntity<Mono<Customer>> getCustomerByEmail(@PathVariable("email") String email) {
		Mono<Customer> customer = this.customerService.findCustomerByEmail(email);
		HttpStatus status = customer != null ? HttpStatus.OK : HttpStatus.NOT_FOUND;
		return new ResponseEntity<>(customer, status);
	}

	@RequestMapping(method = RequestMethod.GET, produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public Flux<Customer> getAllCustomers() {
		return this.customerService.findAllCustomers();
	}

	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.CREATED)
	public Mono<Customer> createCustomer(@RequestBody CustomerDto customerDto) throws Exception {
		logger.info("creating customer: {}", customerDto);
		return this.customerService.createCustomer(customerDto);
	}
}
