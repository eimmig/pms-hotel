package com.pms.booking.repository;

import com.pms.booking.dto.RateResponseDTO;
import com.pms.booking.model.RateModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Objects;
import java.util.UUID;


public interface RateRepository extends JpaRepository<RateModel, UUID> {

    @Query(value = """
        SELECT 
            r.id AS id,
            r.name AS name,
            rr.id AS roomRateId,
            rr.garage_included AS garageIncluded,
            rr.monday_rate AS mondayRate,
            rr.tuesday_rate AS tuesdayRate,
            rr.wednesday_rate AS wednesdayRate,
            rr.thursday_rate AS thursdayRate,
            rr.friday_rate AS fridayRate,
            rr.saturday_rate AS saturdayRate,
            rr.sunday_rate AS sundayRate
        FROM rate r
        INNER JOIN room_rate rr ON rr.rate_id = r.id
        """, nativeQuery = true)
    List<RateResponseDTO> findAllWithValue();
}
