package com.mertosi.customermanagement.model.dto.request;

import com.mertosi.customermanagement.model.TestDataBuilder;
import com.mertosi.customermanagement.model.dto.request.customer.CustomerRequest;

public class CustomerRequestBuilder extends TestDataBuilder<CustomerRequest> {

    public CustomerRequestBuilder() {
        super(CustomerRequest.class);
    }

    public static CustomerRequest getValidCustomerRequest() {
        return new CustomerRequestBuilder()
                .withEmail("abc@abc.com")
                .build();
    }

    public CustomerRequestBuilder withEmail(String email) {
        data.setEmail(email);
        return this;
    }
}
