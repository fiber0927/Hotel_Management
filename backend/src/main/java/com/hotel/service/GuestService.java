package com.hotel.service;

import com.hotel.entity.Guest;
import com.hotel.mapper.GuestMapper;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class GuestService {
    private final GuestMapper guestMapper;

    public GuestService(GuestMapper guestMapper) {
        this.guestMapper = guestMapper;
    }

    public List<Guest> getAllGuests() {
        return guestMapper.findAll();
    }

    public Guest getGuestById(Long id) {
        return guestMapper.findById(id);
    }

    public Long addGuest(Guest guest) {
        // Check if guest with same phone already exists
        Guest existing = guestMapper.findByPhone(guest.getPhone());
        if (existing != null) {
            return existing.getId();
        }
        // Insert new guest
        guestMapper.insert(guest);
        return guest.getId();
    }

    public int updateGuest(Guest guest) {
        return guestMapper.update(guest);
    }

    public int deleteGuest(Long id) {
        return guestMapper.delete(id);
    }
}