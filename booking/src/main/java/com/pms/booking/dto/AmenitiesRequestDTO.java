package com.pms.booking.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.UUID;

public record AmenitiesRequestDTO(
        UUID id,

        @NotBlank(message = "O nome da comodidade não pode estar em branco.")
        String name,

        UUID roomRateId,

        @NotNull(message = "A tarifa de segunda-feira não pode ser nula.")
        @Positive(message = "A tarifa de segunda-feira deve ser um valor positivo.")
        BigDecimal mondayRate,

        @NotNull(message = "A tarifa de terça-feira não pode ser nula.")
        @Positive(message = "A tarifa de terça-feira deve ser um valor positivo.")
        BigDecimal tuesdayRate,

        @NotNull(message = "A tarifa de quarta-feira não pode ser nula.")
        @Positive(message = "A tarifa de quarta-feira deve ser um valor positivo.")
        BigDecimal wednesdayRate,

        @NotNull(message = "A tarifa de quinta-feira não pode ser nula.")
        @Positive(message = "A tarifa de quinta-feira deve ser um valor positivo.")
        BigDecimal thursdayRate,

        @NotNull(message = "A tarifa de sexta-feira não pode ser nula.")
        @Positive(message = "A tarifa de sexta-feira deve ser um valor positivo.")
        BigDecimal fridayRate,

        @NotNull(message = "A tarifa de sábado não pode ser nula.")
        @Positive(message = "A tarifa de sábado deve ser um valor positivo.")
        BigDecimal saturdayRate,

        @NotNull(message = "A tarifa de domingo não pode ser nula.")
        @Positive(message = "A tarifa de domingo deve ser um valor positivo.")
        BigDecimal sundayRate
) {}
