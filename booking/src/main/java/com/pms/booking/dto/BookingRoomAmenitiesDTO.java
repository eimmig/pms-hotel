package com.pms.booking.dto;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record BookingRoomAmenitiesDTO(
        UUID id,
        @NotNull(message = "O UUID da booking_rate não pode estar nulo.")
        UUID bookingRate,
        @NotNull(message = "O UUID da amenities não pode estar nulo.")
        UUID amenities
) {
}
