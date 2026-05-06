package com.hotel.mapper;

import com.hotel.entity.CheckInRecord;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface CheckInRecordMapper {
    List<CheckInRecord> findAll();
    CheckInRecord findById(Long id);
    List<CheckInRecord> findByStatus(String status);
    CheckInRecord findByBookingId(Long bookingId);
    int insert(CheckInRecord record);
    int update(CheckInRecord record);
}