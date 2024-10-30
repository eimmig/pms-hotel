package com.pms.booking.repository;

import com.pms.booking.model.BookingModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;


public interface BookingRepository extends JpaRepository<BookingModel, UUID> {

    @Query(value = "SELECT * FROM booking b " +
            "WHERE CURRENT_DATE = CAST(end_date AS DATE) " +
            "AND b.status = :status", nativeQuery = true)
    List<BookingModel> findByStatusEndDate(@Param("status") String status);

    @Query(value = "SELECT * FROM booking b " +
            "WHERE CURRENT_DATE = CAST(start_date AS DATE) " +
            "AND b.status = :status", nativeQuery = true)
    List<BookingModel> findByStatusStartDate(@Param("status") String status);


}
