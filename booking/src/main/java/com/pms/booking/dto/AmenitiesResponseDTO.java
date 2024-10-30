package com.pms.booking.dto;

import java.util.UUID;

public interface AmenitiesResponseDTO {
    UUID getId();

    String getName();
    UUID getRoomRateId();
    double getMondayRate();
    double getTuesdayRate();
    double getWednesdayRate();
    double getThursdayRate();
    double getFridayRate();
    double getSaturdayRate();
    double getSundayRate();
}
