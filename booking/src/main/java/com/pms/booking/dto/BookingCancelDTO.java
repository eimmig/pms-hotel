package com.pms.booking.dto;

import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public record BookingCancelDTO(@NotNull(message = "UUID da reserva não informado.")
                               UUID bookingId,
                               @NotNull(message = "Lista de quartos não informada")
                               List<UUID> roomList) {
}
