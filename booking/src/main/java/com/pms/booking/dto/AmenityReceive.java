package com.pms.booking.dto;

import jakarta.validation.constraints.NotBlank;
import java.util.UUID;

public record AmenityReceive(
        @NotBlank(message = "O ID da amenidade não pode estar em branco.")
        UUID amenitieId,

        @NotBlank(message = "O nome da amenidade não pode estar em branco.")
        String amenitieName
) {}

