package com.pms.booking.dto;

import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

public record AmenitiesDTO(
        UUID amenitieId,
        @NotBlank(message = "O nome do servico é obrigatório.")
        String name
) {
}
