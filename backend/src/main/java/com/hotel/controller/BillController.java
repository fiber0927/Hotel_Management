package com.hotel.controller;

import com.hotel.entity.Bill;
import com.hotel.service.BillService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/bills")
public class BillController {
    private final BillService billService;

    public BillController(BillService billService) {
        this.billService = billService;
    }

    @GetMapping
    public List<Bill> getAllBills() {
        return billService.getAllBills();
    }

    @GetMapping("/{id}")
    public Bill getBillById(@PathVariable Long id) {
        return billService.getBillById(id);
    }

    @GetMapping("/checkin/{checkInId}")
    public Bill getBillByCheckInId(@PathVariable Long checkInId) {
        return billService.getBillByCheckInId(checkInId);
    }

    @GetMapping("/paid/{isPaid}")
    public List<Bill> getBillsByIsPaid(@PathVariable Integer isPaid) {
        return billService.getBillsByIsPaid(isPaid);
    }

    @PostMapping
    public int createBill(@RequestBody Bill bill) {
        return billService.createBill(bill);
    }

    @PutMapping("/{id}")
    public int updateBill(@PathVariable Long id, @RequestBody Bill bill) {
        bill.setId(id);
        return billService.updateBill(bill);
    }

    @PatchMapping("/{id}/pay")
    public int payBill(@PathVariable Long id, @RequestBody Bill bill) {
        return billService.payBill(id, bill.getPaymentMethod());
    }
}