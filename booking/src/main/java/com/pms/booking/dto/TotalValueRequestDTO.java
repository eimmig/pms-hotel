package com.pms.booking.dto;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.UUID;

public record TotalValueRequestDTO(
        UUID bookingRoomId,
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDateTime startDate,
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDateTime endDate,
        UUID rateId
) {}
