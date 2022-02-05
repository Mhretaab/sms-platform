package com.haud.smsgateway.customer.controller;

import com.haud.smsgateway.customer.model.Customer;
import com.haud.smsgateway.customer.domain.CustomerDto;
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

	@GetMapping("/{uuid}")
	public ResponseEntity<Mono<Customer>> getCustomerByUuid(@PathVariable("uuid") String uuid) {
		Mono<Customer> customer = this.customerService.findCustomerByUuid(uuid);
		HttpStatus status = customer != null ? HttpStatus.OK : HttpStatus.NOT_FOUND;
		return new ResponseEntity<>(customer, status);
	}

	@RequestMapping(method = RequestMethod.GET, produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public Flux<Customer> getAllCustomers() {
		return this.customerService.findAllCustomers();
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Mono<Customer> createCustomer(@RequestBody CustomerDto customerDto) throws Exception {
		logger.info("creating customer: {}", customerDto);
		return this.customerService.createCustomer(customerDto);
	}
}
