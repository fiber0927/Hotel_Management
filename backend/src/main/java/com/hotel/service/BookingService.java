package com.hotel.service;

import com.hotel.entity.Booking;
import com.hotel.entity.HousekeepingTask;
import com.hotel.entity.Room;
import com.hotel.mapper.BookingMapper;
import com.hotel.mapper.HousekeepingTaskMapper;
import com.hotel.mapper.RoomMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class BookingService {
    private final BookingMapper bookingMapper;
    private final RoomMapper roomMapper;
    private final HousekeepingTaskMapper taskMapper;

    public BookingService(BookingMapper bookingMapper, RoomMapper roomMapper, HousekeepingTaskMapper taskMapper) {
        this.bookingMapper = bookingMapper;
        this.roomMapper = roomMapper;
        this.taskMapper = taskMapper;
    }

    public List<Booking> getAllBookings() {
        return bookingMapper.findAll();
    }

    public Booking getBookingById(Long id) {
        return bookingMapper.findById(id);
    }

    public List<Booking> getBookingsByGuestId(Long guestId) {
        return bookingMapper.findByGuestId(guestId);
    }

    public List<Booking> getBookingsByStatus(String status) {
        return bookingMapper.findByStatus(status);
    }

    // 预订时不锁定房间，保持可用
    public int addBooking(Booking booking) {
        return bookingMapper.insert(booking);
    }

    public int updateBooking(Booking booking) {
        return bookingMapper.update(booking);
    }

    // 取消预订
    @Transactional
    public int cancelBooking(Long id) {
        Booking booking = bookingMapper.findById(id);
        if (booking == null) {
            return 0;
        }
        int result = bookingMapper.updateStatus(id, "cancelled");
        return result;
    }

    public int completeBooking(Long id) {
        return bookingMapper.updateStatus(id, "completed");
    }

    // 拒绝预订
    @Transactional
    public int rejectBooking(Long id) {
        Booking booking = bookingMapper.findById(id);
        if (booking == null) {
            return 0;
        }
        return bookingMapper.updateStatus(id, "rejected");
    }

    // 确认预订 → 房间变为「已被预约」，自动创建清洁任务
    @Transactional
    public int confirmBooking(Long id) {
        Booking booking = bookingMapper.findById(id);
        if (booking == null) {
            throw new RuntimeException("预订不存在");
        }
        int result = bookingMapper.updateStatus(id, "confirmed");
        if (result > 0 && booking.getRoomId() != null) {
            // 房间变为已被预约
            roomMapper.updateStatus(booking.getRoomId(), "booked");
            // 自动创建清洁任务
            Room room = roomMapper.findById(booking.getRoomId());
            String priority = "normal";
            if (room != null && "suite".equals(room.getRoomType())) {
                priority = "high";
            }
            HousekeepingTask task = new HousekeepingTask();
            task.setRoomId(booking.getRoomId());
            task.setTaskType("cleaning");
            task.setPriority(priority);
            task.setStatus("pending");
            task.setNotes("预约入住前清洁");
            taskMapper.insert(task);
        }
        return result;
    }

    // 删除预订 → 仅删除记录，不改变房间状态
    public int deleteBooking(Long id) {
        Booking booking = bookingMapper.findById(id);
        if (booking == null) {
            return 0;
        }
        // 重要：顾客删除已确认的预订时，不释放房间
        // 房间只有通过「退房确认」流程才能恢复可用
        return bookingMapper.delete(id);
    }
}
