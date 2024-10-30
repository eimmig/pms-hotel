package com.pms.booking.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record PersonReciveDTO(
        @NotNull
        UUID personId,
        @NotBlank
        String personName
) {
}
