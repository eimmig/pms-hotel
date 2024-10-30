package com.pms.booking.dto;

import java.util.UUID;

public interface RateResponseDTO{
    UUID getId();

    String getName();
    UUID getRoomRateId();
    boolean isGarageIncluded();
    double getMondayRate();
    double getTuesdayRate();
    double getWednesdayRate();
    double getThursdayRate();
    double getFridayRate();
    double getSaturdayRate();
    double getSundayRate();
}
