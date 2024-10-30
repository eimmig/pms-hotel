package com.pms.booking.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public record BookingRoomDTO(@NotNull(message = "UUID da reserva não informado.")
                             UUID bookingId,
                             @NotNull (message = "UUID do quarto não informado")
                             UUID roomId,
                             @NotBlank(message = "Status do reserva quarto não informado")
                             String status,
                             Date startDate,
                             Date endDate,
                             List<AmenitiesDTO> amenities) {
}
