package com.pms.booking.dto;

import java.util.UUID;

public record BookingEmailDTO(UUID bookingId,
                              String emailTo,
                              String subject,
                              String message
) {
}
