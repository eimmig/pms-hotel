package com.pms.booking.dto;

import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public record RoomListDTO(
        @NotNull(message = "O ID do quarto é obrigatório.")
        UUID roomId,
        List<AmenitiesDTO> amenities
) {
}
