package com.pms.booking.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

public record BookingReceive(
        UUID id,

        @NotBlank(message = "A data de início não pode estar em branco.")
        String startDate,

        @NotBlank(message = "A data de término não pode estar em branco.")
        String endDate,

        @NotBlank(message = "O ID da pessoa não pode estar em branco.")
        UUID personId,

        @NotBlank(message = "O nome da pessoa não pode estar em branco.")
        String personName,

        @NotBlank(message = "O status não pode estar em branco.")
        String status,

        @NotBlank(message = "O nome do status não pode estar em branco.")
        String statusName,

        @NotNull(message = "A lista de quartos não pode ser nula.")
        List<RoomReceive> roomList
) {}
