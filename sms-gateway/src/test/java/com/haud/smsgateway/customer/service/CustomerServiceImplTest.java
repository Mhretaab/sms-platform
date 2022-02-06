package com.haud.smsgateway.customer.service;

import com.haud.smsgateway.customer.domain.CustomerDto;
import com.haud.smsgateway.customer.exception.CustomerInvalidFieldException;
import com.haud.smsgateway.customer.model.Customer;
import com.haud.smsgateway.customer.repo.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import reactor.core.publisher.Mono;

import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

@SpringBootTest
class CustomerServiceImplTest {

	@MockBean
	private CustomerRepository customerRepository;
	@Autowired
	private CustomerService customerServiceUnderTest;

	@Test
	void givenUuid_whenFindByUuid_thenFindCustomer() {
		// given
		String uuid = UUID.randomUUID().toString();
		Customer customer = new Customer("Abraham", "Maru", "+251912663409", "abraham@email.com", uuid);
		Mono<String> uuidMono = Mono.just(uuid);
		when(customerRepository.findByUuid(uuidMono)).thenReturn(Mono.just(customer));

		// when
		customerServiceUnderTest.findCustomerByUuid(uuid);

		// then
		verify(customerRepository).findByUuid(ArgumentMatchers.refEq(uuidMono));
	}

	@Test
	void givenEmail_whenFindByEmail_thenFindCustomer() {
		// given
		String email = "abraham@email.com";
		Customer customer = new Customer("Abraham", "Maru", "+251912663409", email, UUID.randomUUID().toString());
		Mono<String> emailMono = Mono.just(email);
		when(customerRepository.findByEmail(emailMono)).thenReturn(Mono.just(customer));

		// when
		customerServiceUnderTest.findCustomerByEmail(email);

		// then
		verify(customerRepository).findByEmail(ArgumentMatchers.refEq(emailMono));
	}

	@Test
	void givenPhoneNumber_whenFindByPhoneNumber_thenFindCustomer() {
		// given
		String phoneNumber = "+251912663409";
		Customer customer = new Customer("Abraham", "Maru", phoneNumber, "abraham@email.com", UUID.randomUUID().toString());
		Mono<String> phoneNumberMono = Mono.just(phoneNumber);
		when(customerRepository.findByPhoneNumber(phoneNumberMono)).thenReturn(Mono.just(customer));

		// when
		customerServiceUnderTest.findCustomerByPhoneNumber(phoneNumber);

		// then
		verify(customerRepository).findByPhoneNumber(ArgumentMatchers.refEq(phoneNumberMono));
	}

	@Test
	void whenFIndAllCustomers_thenFindAllCustomers() {
		// when
		customerServiceUnderTest.findAllCustomers();

		// then
		verify(customerRepository).findAll();
	}

	@Test
	void givenCustomer_whenSave_thenSave() throws Exception {
		// given
		Customer customer = new Customer("Abraham", "Maru", "+251912663409", "abraham@email.com", UUID.randomUUID().toString());
		CustomerDto customerDto = new CustomerDto("Abraham", "Maru", "+251912663409", "abraham@email.com");

		when(customerRepository.save(customer)).thenReturn(Mono.just(customer));

		// when
		customerServiceUnderTest.createCustomer(customerDto);

		// then
		ArgumentCaptor<Customer> customerArgumentCaptor =
				ArgumentCaptor.forClass(Customer.class);
		verify(customerRepository).save(customerArgumentCaptor.capture());

		Customer capturedCustomer = customerArgumentCaptor.getValue();

		assertThat(capturedCustomer.getEmail()).isEqualTo(customerDto.getEmail());
		assertThat(capturedCustomer.getPhoneNumber()).isEqualTo(customerDto.getPhoneNumber());
		assertThat(capturedCustomer.getFirstName()).isEqualTo(customerDto.getFirstName());
		assertThat(capturedCustomer.getLastName()).isEqualTo(customerDto.getLastName());
	}

	@Test
	void givenInvalidEmail_whenSave_thenThrowCustomerInvalidFieldException() throws Exception {
		// when
		CustomerDto customerDto1 = new CustomerDto("Abraham", "Maru", "+251912663409", "");
		CustomerDto customerDto2 = new CustomerDto("Abraham", "Maru", "+251912663409", "		");
		CustomerDto customerDto3 = new CustomerDto("Abraham", "Maru", "+251912663409", null);

		// then
		assertThrows(CustomerInvalidFieldException.class, () -> {
			customerServiceUnderTest.createCustomer(customerDto1);
		});

		assertThrows(CustomerInvalidFieldException.class, () -> {
			customerServiceUnderTest.createCustomer(customerDto2);
		});

		assertThrows(CustomerInvalidFieldException.class, () -> {
			customerServiceUnderTest.createCustomer(customerDto3);
		});
	}

	@Test
	void givenInvalidPhoneNumber_whenSave_thenThrowCustomerInvalidFieldException() throws Exception {
		// when
		CustomerDto customerDto1 = new CustomerDto("Abraham", "Maru", "", "abrahame@email.com");
		CustomerDto customerDto2 = new CustomerDto("Abraham", "Maru", "	", "abrahame@email.com");
		CustomerDto customerDto3 = new CustomerDto("Abraham", "Maru", null, "abrahame@email.com");

		// then
		assertThrows(CustomerInvalidFieldException.class, () -> {
			customerServiceUnderTest.createCustomer(customerDto1);
		});

		assertThrows(CustomerInvalidFieldException.class, () -> {
			customerServiceUnderTest.createCustomer(customerDto2);
		});

		assertThrows(CustomerInvalidFieldException.class, () -> {
			customerServiceUnderTest.createCustomer(customerDto3);
		});
	}

	@Test
	void givenInvalidFirstName_whenSave_thenThrowCustomerInvalidFieldException() throws Exception {
		// when
		CustomerDto customerDto1 = new CustomerDto("", "Maru", "+251912663409", "abrahame@email.com");
		CustomerDto customerDto2 = new CustomerDto("	", "Maru", "+251912663409", "abrahame@email.com");
		CustomerDto customerDto3 = new CustomerDto(null, "Maru", "+251912663409", "abrahame@email.com");

		// then
		assertThrows(CustomerInvalidFieldException.class, () -> {
			customerServiceUnderTest.createCustomer(customerDto1);
		});

		assertThrows(CustomerInvalidFieldException.class, () -> {
			customerServiceUnderTest.createCustomer(customerDto2);
		});

		assertThrows(CustomerInvalidFieldException.class, () -> {
			customerServiceUnderTest.createCustomer(customerDto3);
		});
	}

	@Test
	void givenInvalidLastName_whenSave_thenThrowCustomerInvalidFieldException() throws Exception {
		// when
		CustomerDto customerDto1 = new CustomerDto("Abraham", "", "+251912663409", "abrahame@email.com");
		CustomerDto customerDto2 = new CustomerDto("Abraham", "	", "+251912663409", "abrahame@email.com");
		CustomerDto customerDto3 = new CustomerDto("Abraham", null, "+251912663409", "abrahame@email.com");

		// then
		assertThrows(CustomerInvalidFieldException.class, () -> {
			customerServiceUnderTest.createCustomer(customerDto1);
		});

		assertThrows(CustomerInvalidFieldException.class, () -> {
			customerServiceUnderTest.createCustomer(customerDto2);
		});

		assertThrows(CustomerInvalidFieldException.class, () -> {
			customerServiceUnderTest.createCustomer(customerDto3);
		});
	}
}