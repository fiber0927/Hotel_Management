package com.hotel.mapper;

import com.hotel.entity.Guest;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface GuestMapper {
    List<Guest> findAll();
    Guest findById(Long id);
    Guest findByPhone(String phone);
    void insert(Guest guest);
    int update(Guest guest);
    int delete(Long id);
}