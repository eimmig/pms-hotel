package com.pms.booking.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record RoomTypeDTO(
        @NotBlank(message = "O nome não pode estar em branco.")
        String name,
        @NotNull(message = "O número máximo de pessoas não pode ser nulo.")
        Integer maxPersons,
        @NotBlank(message = "A abreviação não pode estar em branco.")
        String abbreviation,
        @NotNull(message = "Tarifa não informada.")
        UUID rateId) {
}
