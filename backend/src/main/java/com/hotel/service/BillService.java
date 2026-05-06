package com.hotel.service;

import com.hotel.entity.Bill;
import com.hotel.mapper.BillMapper;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class BillService {
    private final BillMapper billMapper;

    public BillService(BillMapper billMapper) {
        this.billMapper = billMapper;
    }

    public List<Bill> getAllBills() {
        return billMapper.findAll();
    }

    public Bill getBillById(Long id) {
        return billMapper.findById(id);
    }

    public Bill getBillByCheckInId(Long checkInId) {
        return billMapper.findByCheckInId(checkInId);
    }

    public List<Bill> getBillsByIsPaid(Integer isPaid) {
        return billMapper.findByIsPaid(isPaid);
    }

    public int createBill(Bill bill) {
        bill.setIsPaid(0);
        return billMapper.insert(bill);
    }

    public int updateBill(Bill bill) {
        return billMapper.update(bill);
    }

    public int payBill(Long id, String paymentMethod) {
        Bill bill = billMapper.findById(id);
        if (bill != null) {
            bill.setIsPaid(1);
            bill.setPaymentMethod(paymentMethod);
            bill.setPaidAt(LocalDateTime.now());
            return billMapper.update(bill);
        }
        return 0;
    }

    public BigDecimal calculateRoomCharge(int nights, BigDecimal pricePerNight) {
        return pricePerNight.multiply(BigDecimal.valueOf(nights));
    }
}