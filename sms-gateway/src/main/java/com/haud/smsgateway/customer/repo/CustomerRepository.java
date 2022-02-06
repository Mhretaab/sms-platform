package com.haud.smsgateway.customer.repo;

import com.haud.smsgateway.customer.model.Customer;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface CustomerRepository extends ReactiveMongoRepository<Customer, String> {
	Mono<Customer> findByUuid(Mono<String> uuid);

	Mono<Customer> findByPhoneNumber(Mono<String> phoneNumber);

	Mono<Customer> findByEmail(Mono<String> email);
}
