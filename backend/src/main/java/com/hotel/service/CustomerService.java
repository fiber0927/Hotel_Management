package com.hotel.service;

import com.hotel.entity.Customer;
import com.hotel.mapper.CustomerMapper;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {
    private final CustomerMapper customerMapper;

    public CustomerService(CustomerMapper customerMapper) {
        this.customerMapper = customerMapper;
    }

    public Customer login(String username, String password) {
        Customer customer = customerMapper.findByUsername(username);
        if (customer != null && customer.getPassword().equals(password)) {
            return customer;
        }
        return null;
    }

    public Customer getCustomerById(Long id) {
        return customerMapper.findById(id);
    }

    public Customer findByUsername(String username) {
        return customerMapper.findByUsername(username);
    }

    public int register(Customer customer) {
        return customerMapper.insert(customer);
    }
}