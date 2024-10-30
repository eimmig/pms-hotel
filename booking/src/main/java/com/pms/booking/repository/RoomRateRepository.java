package com.pms.booking.repository;

import com.pms.booking.model.RateModel;
import com.pms.booking.model.RoomRateModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;


public interface RoomRateRepository extends JpaRepository<RoomRateModel, UUID> {
    Optional<RoomRateModel> findByRate(RateModel rate);
    Optional<RoomRateModel> findByRateId(UUID rateId);
}
