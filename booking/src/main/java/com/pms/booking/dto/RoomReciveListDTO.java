package com.pms.booking.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record RoomReciveListDTO(
        @NotNull
        UUID roomId,
        @NotBlank
        String roomNumber
) {
}
