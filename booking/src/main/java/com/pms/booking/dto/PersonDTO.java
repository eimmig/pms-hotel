package com.pms.booking.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.UUID;

public record PersonDTO(
        UUID id,
        @NotBlank(message = "O nome não pode estar em branco.")
        String name,

        @NotBlank(message = "O documento não pode estar em branco.")
        String document,

        @NotNull(message = "O tipo de documento não pode ser nulo.")
        Integer documentTypeId,

        @NotBlank(message = "O número de telefone não pode estar em branco.")
        String phoneNumber,

        @NotBlank(message = "O email não pode estar em branco.")
        @Email(message = "O email deve ser válido.")
        String email,

        @NotNull(message = "A data de nascimento não pode ser nula.")
        LocalDate birthDate
) {}
