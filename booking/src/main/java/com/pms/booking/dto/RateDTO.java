package com.pms.booking.dto;

import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

public record RateDTO(
        UUID id,
        @NotBlank(message = "O nome da comodidade não pode estar em branco.")
        String name
) {
}
