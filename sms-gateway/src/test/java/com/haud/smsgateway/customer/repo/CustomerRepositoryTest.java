package com.haud.smsgateway.customer.repo;

import com.haud.smsgateway.customer.model.Customer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataMongoTest
class CustomerRepositoryTest {

	@Autowired
	private CustomerRepository repoUnderTest;

	@AfterEach
	void tearDown() {
		repoUnderTest.deleteAll();
	}

	@Test
	void givenUuid_whenFindByUuid_thenFindCustomer(){
		//Given
		String uuid = UUID.randomUUID().toString();
		String email = "abraham.maru1@email.com";
		String phoneNumber = "+251913798645";
		Customer customer = new Customer("Abraham", "Maru", phoneNumber, email, uuid);

		repoUnderTest.save(customer).block();

		//When
		Mono<Customer> customerMono = repoUnderTest.findByUuid(Mono.just(uuid));

		//Then
		StepVerifier.create(customerMono)
				.assertNext(persistedCustomer -> {
					assertEquals(uuid, persistedCustomer.getUuid());
					assertEquals(email , persistedCustomer.getEmail());
					assertEquals(phoneNumber, persistedCustomer.getPhoneNumber());
					assertNotNull(persistedCustomer.getId());
				})
				.expectComplete()
				.verify();

	}

	@Test
	void givenEmail_whenFindByEmail_thenFindCustomer(){
		//Given
		String uuid = UUID.randomUUID().toString();
		String email = "abraham.maru2@email.com";
		String phoneNumber = "+251913798645";
		Customer customer = new Customer("Abraham", "Maru", phoneNumber, email, uuid);

		repoUnderTest.save(customer).block();

		//When
		Mono<Customer> customerMono = repoUnderTest.findByEmail(Mono.just(email));

		//Then
		StepVerifier.create(customerMono)
				.assertNext(persistedCustomer -> {
					assertEquals(uuid, persistedCustomer.getUuid());
					assertEquals(email , persistedCustomer.getEmail());
					assertEquals(phoneNumber, persistedCustomer.getPhoneNumber());
					assertNotNull(persistedCustomer.getId());
				})
				.expectComplete()
				.verify();

	}

	@Test
	void givenPhoneNumber_whenFindByPhoneNumber_thenFindCustomer(){
		//Given
		String uuid = UUID.randomUUID().toString();
		String email = "abraham.maru3@email.com";
		String phoneNumber = "+251913798645";
		Customer customer = new Customer("Abraham", "Maru", phoneNumber, email, uuid);

		repoUnderTest.save(customer).block();

		//When
		Mono<Customer> customerMono = repoUnderTest.findByPhoneNumber(Mono.just(phoneNumber));

		//Then
		StepVerifier.create(customerMono)
				.assertNext(persistedCustomer -> {
					assertEquals(uuid, persistedCustomer.getUuid());
					assertEquals(email , persistedCustomer.getEmail());
					assertEquals(phoneNumber, persistedCustomer.getPhoneNumber());
					assertNotNull(persistedCustomer.getId());
				})
				.expectComplete()
				.verify();

	}
}