package com.hotel.mapper;

import com.hotel.entity.Bill;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface BillMapper {
    List<Bill> findAll();
    Bill findById(Long id);
    Bill findByCheckInId(Long checkInId);
    List<Bill> findByIsPaid(Integer isPaid);
    int insert(Bill bill);
    int update(Bill bill);
    int updateIsPaid(Long id, Integer isPaid);
}