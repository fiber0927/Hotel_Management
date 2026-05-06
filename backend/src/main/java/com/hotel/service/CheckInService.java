package com.hotel.service;

import com.hotel.entity.CheckInRecord;
import com.hotel.mapper.CheckInRecordMapper;
import com.hotel.mapper.RoomMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class CheckInService {
    private final CheckInRecordMapper checkInRecordMapper;
    private final RoomMapper roomMapper;

    public CheckInService(CheckInRecordMapper checkInRecordMapper, RoomMapper roomMapper) {
        this.checkInRecordMapper = checkInRecordMapper;
        this.roomMapper = roomMapper;
    }

    public List<CheckInRecord> getAllRecords() {
        return checkInRecordMapper.findAll();
    }

    public CheckInRecord getRecordById(Long id) {
        return checkInRecordMapper.findById(id);
    }

    public List<CheckInRecord> getRecordsByStatus(String status) {
        return checkInRecordMapper.findByStatus(status);
    }

    public CheckInRecord getRecordByBookingId(Long bookingId) {
        return checkInRecordMapper.findByBookingId(bookingId);
    }

    // 入住登记 → 房间变为「已占用」
    @Transactional
    public int checkIn(CheckInRecord record) {
        record.setCheckInTime(LocalDateTime.now());
        record.setStatus("checked_in");
        int result = checkInRecordMapper.insert(record);
        if (result > 0 && record.getRoomId() != null) {
            roomMapper.updateStatus(record.getRoomId(), "occupied");
        }
        return result;
    }

    // 退房 → 房间变为「已占用」(打扫中)
    @Transactional
    public int checkOut(Long id) {
        CheckInRecord record = checkInRecordMapper.findById(id);
        if (record != null) {
            record.setCheckOutTime(LocalDateTime.now());
            record.setStatus("checked_out");
            int result = checkInRecordMapper.update(record);
            if (result > 0 && record.getRoomId() != null) {
                // 退房后进入打扫流程，状态仍为「已占用」
                roomMapper.updateStatus(record.getRoomId(), "occupied");
            }
            return result;
        }
        return 0;
    }

    // 管理员确认退房 → 房间恢复「可用」
    @Transactional
    public int confirmCheckout(Long id) {
        CheckInRecord record = checkInRecordMapper.findById(id);
        if (record != null && record.getRoomId() != null) {
            roomMapper.updateStatus(record.getRoomId(), "available");
            return 1;
        }
        return 0;
    }
}
