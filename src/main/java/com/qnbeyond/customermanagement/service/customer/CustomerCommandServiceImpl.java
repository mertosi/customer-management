package com.qnbeyond.customermanagement.service.customer;

import com.qnbeyond.customermanagement.common.mapper.CustomerRequestMapper;
import com.qnbeyond.customermanagement.model.dto.request.customer.CustomerRequest;
import com.qnbeyond.customermanagement.model.entity.Customer;
import com.qnbeyond.customermanagement.repository.CustomerCommandRepository;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerCommandServiceImpl implements CustomerCommandService {

    private final CustomerCommandRepository customerCommandRepository;
    private final CustomerQueryService customerQueryService;
    private final PasswordEncoder passwordEncoder;

    private final CustomerRequestMapper mapper = Mappers.getMapper(CustomerRequestMapper.class);

    @Override
    public Customer create(CustomerRequest request) {
        request.setPassword(passwordEncoder.encode(request.getPassword()));

        Customer customer = mapper.map(request);
        return customerCommandRepository.save(customer);
    }

    @Override
    public Customer update(Long id, CustomerRequest request) {
        request.setPassword(passwordEncoder.encode(request.getPassword()));

        Customer customerToUpdated = customerQueryService.getById(id);
        mapper.requestToEntity(request, customerToUpdated);

        return customerCommandRepository.save(customerToUpdated);
    }

    @Override
    public Customer delete(Long id) {
        Customer customerToDeleted = customerQueryService.getById(id);
        customerCommandRepository.delete(customerToDeleted);

        return customerToDeleted;
    }
}
