package com.pms.booking.repository;

import com.pms.booking.model.BookingModel;
import com.pms.booking.model.BookingRoomModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BookingRoomRepository extends JpaRepository<BookingRoomModel, UUID> {
   Optional<BookingRoomModel> findByBookingIdAndRoomId(UUID bookingId, UUID roomId);
   List<BookingRoomModel> findAllByBooking(BookingModel booking);

   @Query(value = "SELECT COUNT(br.id) AS value, rt.name AS type " +
           "FROM booking_room br " +
           "JOIN room r ON r.id = br.room_id " +
           "JOIN room_type rt ON rt.id = r.room_type_id " +
           "WHERE DATE(CURRENT_DATE) BETWEEN DATE(br.start_date) AND (DATE(br.end_date) - INTERVAL '1 DAY') " +
           "GROUP BY rt.name",
           nativeQuery = true)
   List<Object[]> countBookingsByRoomType();

   @Query(value = "WITH date_range AS ( " +
           "    SELECT generate_series( " +
           "        CURRENT_DATE - interval '4 days',  " +
           "        CURRENT_DATE,   " +
           "        interval '1 day' " +
           "    ) AS date " +
           "), " +
           "expanded_bookings AS ( " +
           "    SELECT " +
           "        b.id, " +
           "        b.start_date, " +
           "        b.end_date, " +
           "        d.date AS booking_date " +
           "    FROM " +
           "        booking_room b " +
           "    JOIN date_range d " +
           "        ON d.date::DATE >= b.start_date::DATE AND d.date::DATE < b.end_date::DATE " +
           ") " +
           "SELECT " +
           "    TO_CHAR(booking_date, 'DD/MM/YYYY') AS date, " +
           "    COUNT(id) AS total_reservas " +
           "FROM " +
           "    expanded_bookings " +
           "GROUP BY " +
           "    booking_date " +
           "ORDER BY " +
           "    booking_date",
           nativeQuery = true)
   List<Object[]> countBookingsForOccupation();

   @Query(value = "SELECT COUNT(*) AS bookins " +
           "FROM booking_room " +
           "WHERE start_date::DATE = CURRENT_DATE",
           nativeQuery = true)
   Long countCheckinsToday();

   @Query(value = "SELECT COUNT(*) AS bookins " +
           "FROM booking_room " +
           "WHERE end_date::DATE = CURRENT_DATE",
           nativeQuery = true)
   Long countCheckoutsToday();

   @Query(value = "SELECT " +
           "    CASE " +
           "        WHEN COUNT(*) = 0 THEN 0 " +
           "        ELSE COALESCE(SUM(booking_value + amenities_value), 0) / COUNT(*) " +
           "    END AS ticket_medio " +
           "FROM " +
           "    booking_room " +
           "WHERE " +
           "    start_date::DATE = CURRENT_DATE;",
           nativeQuery = true)
   Long getRevenueToday();

    List<BookingRoomModel> findAllByBookingId(UUID booking_id);
}
