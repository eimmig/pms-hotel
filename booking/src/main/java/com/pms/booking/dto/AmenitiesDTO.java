package com.pms.booking.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public record AmenitiesDTO(
        UUID amenitieId,
        @NotBlank(message = "O nome do servico é obrigatório.")
        String name
) {
}
