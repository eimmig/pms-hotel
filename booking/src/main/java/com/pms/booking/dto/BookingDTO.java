package com.pms.booking.dto;

import com.pms.booking.enums.EBookingStatus;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public record BookingDTO(
        UUID id,
        Long bookingNumber,

        @NotNull(message = "A data de início é obrigatória.")
        @FutureOrPresent(message = "A data de início deve estar no presente ou no futuro.")
        Date startDate,

        @NotNull(message = "A data de término é obrigatória.")
        @Future(message = "A data de termino deve estar no futuro.")
        Date endDate,

        @NotNull(message = "O ID da pessoa é obrigatório.")
        UUID personId,

        @NotNull(message = "O status da reserva é obrigatório.")
        String status,

        @NotNull(message = "A lista de IDs de quartos não pode ser nula.")
        @NotEmpty(message = "A lista de IDs de quartos não pode estar vazia.")
        @Valid
        List<RoomListDTO> roomList
) {
}
