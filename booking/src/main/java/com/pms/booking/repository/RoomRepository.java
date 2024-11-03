package com.pms.booking.repository;

import com.pms.booking.model.RoomModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;


public interface RoomRepository extends JpaRepository<RoomModel, UUID> {

    @Query(value = "SELECT r.* FROM room r " +
            "WHERE r.id NOT IN (" +
            "SELECT br.room_id FROM booking_room br " +
            "WHERE br.start_date < :endDate " +
            "AND br.end_date > :startDate) " +
            "OR r.id IN (" +
            "SELECT br.room_id FROM booking_room br " +
            "WHERE br.booking_id = :bookingId)",
            nativeQuery = true)
    List<RoomModel> findAllWithDate(LocalDate startDate, LocalDate endDate, UUID bookingId);
}
