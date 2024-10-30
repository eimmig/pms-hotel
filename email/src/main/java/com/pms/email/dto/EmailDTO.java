package com.pms.email.dto;

import java.util.UUID;

public record EmailDTO(UUID bookingId,
                       String emailTo,
                       String subject,
                       String message) {
}
