package com.hotel.mapper;

import com.hotel.entity.Booking;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface BookingMapper {
    List<Booking> findAll();
    Booking findById(Long id);
    List<Booking> findByGuestId(Long guestId);
    List<Booking> findByStatus(String status);
    int insert(Booking booking);
    int update(Booking booking);
    int updateStatus(Long id, String status);
    int delete(Long id);
}