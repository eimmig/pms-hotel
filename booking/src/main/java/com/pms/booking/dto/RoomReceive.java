package com.pms.booking.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

public record RoomReceive(
        @NotBlank(message = "O ID do quarto não pode estar em branco.")
        UUID roomId,

        @NotBlank(message = "O número do quarto não pode estar em branco.")
        String roomNumber,

        @NotNull(message = "A lista de amenidades não pode ser nula.")
        List<AmenityReceive> amenities
) {}
