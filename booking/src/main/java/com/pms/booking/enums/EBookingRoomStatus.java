package com.pms.booking.enums;

import lombok.Getter;

@Getter
public enum EBookingRoomStatus {
    EXECUTED("E"),  // Reserva Quarto Efetivado
    CANCELLED("C"), // Reserva Quarto cancelado
    PENDING("P");   // Reserva Quarto pendente

    private final String code;

    EBookingRoomStatus(String code) {
        this.code = code;
    }

    public static EBookingRoomStatus fromCode(String code) {
        for (EBookingRoomStatus status : EBookingRoomStatus.values()) {
            if (status.getCode().equalsIgnoreCase(code)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Código inválido: " + code);
    }
}
