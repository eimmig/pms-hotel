package com.pms.booking.repository;

import com.pms.booking.model.AmenitiesModel;
import com.pms.booking.model.AmenitiesRateModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;


public interface AmenitiesRateRepository extends JpaRepository<AmenitiesRateModel, UUID> {
    Optional<AmenitiesRateModel> findByAmenities(AmenitiesModel amenities);
    Optional<AmenitiesRateModel> findByAmenitiesId(UUID amenitiesId);

}
