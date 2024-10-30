package com.pms.booking.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record TotalValueDTO(
        @NotNull(message = "Dados da comodidade são obrigatórios.")
        @Valid
        BigDecimal bookingRoom,

        @NotNull(message = "Dados da tarifa da comodidade são obrigatórios.")
        @Valid
        BigDecimal amenities
) {
}
