package com.pms.booking.repository;

import com.pms.booking.dto.AmenitiesResponseDTO;
import com.pms.booking.model.AmenitiesModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;


public interface AmenitiesRepository extends JpaRepository<AmenitiesModel, UUID> {

    @Query(value = """
        SELECT 
            a.id AS id,
            a.name AS name,
            ar.id AS roomRateId,
            ar.monday_rate AS mondayRate,
            ar.tuesday_rate AS tuesdayRate,
            ar.wednesday_rate AS wednesdayRate,
            ar.thursday_rate AS thursdayRate,
            ar.friday_rate AS fridayRate,
            ar.saturday_rate AS saturdayRate,
            ar.sunday_rate AS sundayRate
        FROM amenities a
        INNER JOIN amenities_rate ar ON ar.amenities_id = a.id
        """, nativeQuery = true)
    List<AmenitiesResponseDTO> findAllWithValue();
}
