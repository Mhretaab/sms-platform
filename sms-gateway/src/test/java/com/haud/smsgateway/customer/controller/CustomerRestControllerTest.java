package com.haud.smsgateway.customer.controller;

import com.haud.smsgateway.customer.domain.CustomerDto;
import com.haud.smsgateway.customer.model.Customer;
import com.haud.smsgateway.customer.service.CustomerService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@WebFluxTest(controllers = CustomerRestController.class)
class CustomerRestControllerTest {

	@Autowired
	private WebTestClient webTestClient;

	@MockBean
	private CustomerService customerService;

	@Test
	void givenCustomerDto_whenSave_thenSave() throws Exception {
		// When
		CustomerDto customerDto = new CustomerDto("Abraham", "Maru", "+251912663409", "abraham@email.com");
		Customer customer = new Customer("Abraham", "Maru", "+251912663409", "abraham@email.com",
				UUID.randomUUID().toString());

		when(customerService.createCustomer(any(CustomerDto.class))).thenReturn(Mono.just(customer));

		//Then
		webTestClient
				.post().uri("/customers")
				.bodyValue(customerDto)
				.exchange()
				.expectStatus()
				.isCreated()
				.expectBody(Customer.class)
				.isEqualTo(customer);
	}

	@Test
	void givenUuid_whenFindByUuid_thenFindCustomer() {
		// When
		String uuid = UUID.randomUUID().toString();
		Customer customer = new Customer("Abraham", "Maru", "+251912663409", "abraham@email.com", uuid);

		when(customerService.findCustomerByUuid(uuid)).thenReturn(Mono.just(customer));

		//Then
		webTestClient
				.get().uri("/customers/uuid/" + uuid)
				.exchange()
				.expectStatus()
				.isOk()
				.expectBody(Customer.class)
				.isEqualTo(customer);
	}

	@Test
	void givenPhoneNumber_whenFindByPhoneNumber_thenFindCustomer() {
		// When
		String uuid = UUID.randomUUID().toString();
		String phoneNumber = "+251912663409";

		Customer customer = new Customer("Abraham", "Maru", phoneNumber, "abraham@email.com", uuid);

		when(customerService.findCustomerByPhoneNumber(phoneNumber)).thenReturn(Mono.just(customer));

		//Then
		webTestClient
				.get().uri("/customers/phonenumber/" + phoneNumber)
				.exchange()
				.expectStatus()
				.isOk()
				.expectBody(Customer.class)
				.isEqualTo(customer);
	}

	@Test
	void givenEmail_whenFindByEmail_thenFindCustomer() {
		// When
		String uuid = UUID.randomUUID().toString();
		String phoneNumber = "+251912663409";
		String email = "abraham@email.com";
		Customer customer = new Customer("Abraham", "Maru", phoneNumber, email, uuid);

		when(customerService.findCustomerByEmail(email)).thenReturn(Mono.just(customer));

		//Then
		webTestClient
				.get().uri("/customers/email/" + email)
				.exchange()
				.expectStatus()
				.isOk()
				.expectBody(Customer.class)
				.isEqualTo(customer);
	}

	@Test
	void whenFIndAllCustomers_thenFindAllCustomers() {
		// When
		Customer customer1 = new Customer("Abraham", "Maru", "+251912663409", "abraham@email.com",
				UUID.randomUUID().toString());
		Customer customer2 = new Customer("Abraham", "Maru", "+251912663409", "abraham@email.com",
				UUID.randomUUID().toString());

		when(customerService.findAllCustomers()).thenReturn(Flux.just(customer1, customer2));

		//Then
		webTestClient
				.get().uri("/customers")
				.exchange()
				.expectStatus()
				.isOk()
				.expectBodyList(Customer.class)
				.isEqualTo(Arrays.asList(customer1, customer2));
	}
}