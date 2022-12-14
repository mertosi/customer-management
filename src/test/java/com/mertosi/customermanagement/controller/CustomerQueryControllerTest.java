package com.mertosi.customermanagement.controller;

import com.mertosi.customermanagement.common.exception.NotFoundException;
import com.mertosi.customermanagement.controller.customer.CustomerQueryController;
import com.mertosi.customermanagement.model.entity.Customer;
import com.mertosi.customermanagement.model.entity.CustomerBuilder;
import com.mertosi.customermanagement.service.customer.CustomerQueryService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.when;

@WebFluxTest(controllers = CustomerQueryController.class)
class CustomerQueryControllerTest extends AbstractControllerTest {
    @MockBean
    private CustomerQueryService customerQueryService;

    private static final String CUSTOMER_ENDPOINT = "/api/v1/customer";
    private static final String GET_BY_ID_ENDPOINT = CUSTOMER_ENDPOINT + "/{id}";

    @Test
    void happyPath_getAll() {
        List<Customer> customerEntities = Collections.singletonList(CustomerBuilder.getValidCustomer());
        when(customerQueryService.getAll()).thenReturn(customerEntities);

        client.get()
                .uri(CUSTOMER_ENDPOINT)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(System.out::println)
                .jsonPath("$.success").isEqualTo(true)
                .jsonPath("$.errorMessage").isEmpty()
                .jsonPath("$.data").isNotEmpty();
    }

    @Test
    void givenValidId_whenGetCustomer_thenReturnCustomerResponse() {
        Customer customer = CustomerBuilder.getValidCustomer();
        when(customerQueryService.getById(customer.getId())).thenReturn(customer);

        client.get()
                .uri(GET_BY_ID_ENDPOINT, customer.getId())
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(System.out::println)
                .jsonPath("$.success").isEqualTo(true)
                .jsonPath("$.errorMessage").isEmpty()
                .jsonPath("$.data").isNotEmpty();
    }

    @Test
    void givenInvalidId_whenGetCustomer_thenThrowNotFoundException() {
        Long id = 2L;
        when(customerQueryService.getById(id)).thenThrow(new NotFoundException(id.toString()));

        client.get()
                .uri(GET_BY_ID_ENDPOINT, id)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.NOT_ACCEPTABLE)
                .expectBody()
                .consumeWith(System.out::println)
                .jsonPath("$.success").isEqualTo(false)
                .jsonPath("$.errorMessage").isNotEmpty()
                .jsonPath("$.data").isEmpty();
    }
}
