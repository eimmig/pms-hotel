package com.pms.booking.repository;

import com.pms.booking.model.BookingRoomAmenitiesModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;


public interface BookingRoomAmenitiesRepository extends JpaRepository<BookingRoomAmenitiesModel, UUID> {
    List<BookingRoomAmenitiesModel> findByBookingRoomId(UUID bookingRoomId);
}
