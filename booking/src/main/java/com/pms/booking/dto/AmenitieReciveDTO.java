package com.pms.booking.dto;

import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

public record AmenitieReciveDTO(UUID amenitieId,
                                @NotBlank(message = "O nome do servico é obrigatório.")
                                String amenitieName) {
}
