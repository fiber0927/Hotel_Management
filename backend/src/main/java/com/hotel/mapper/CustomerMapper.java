package com.hotel.mapper;

import com.hotel.entity.Customer;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CustomerMapper {
    Customer findByUsername(String username);
    Customer findById(Long id);
    int insert(Customer customer);
}