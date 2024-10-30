package com.pms.booking.repository;

import com.pms.booking.model.RoomTypeModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RoomTypeRepository extends JpaRepository<RoomTypeModel, UUID> {
}
