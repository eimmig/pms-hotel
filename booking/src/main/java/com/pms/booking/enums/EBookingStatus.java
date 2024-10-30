package com.pms.booking.enums;

import lombok.Getter;

@Getter
public enum EBookingStatus {
    EXECUTED("E"),  // Reserva Efetivada
    CANCELLED("C"), // Reserva cancela
    PENDING("P"), // Reserva pendente
    FINISHED("F"); // Reserva finalizada

    private final String code;

    EBookingStatus(String code) {
        this.code = code;
    }

    public static EBookingStatus fromCode(String code) {
        for (EBookingStatus status : EBookingStatus.values()) {
            if (status.getCode().equalsIgnoreCase(code)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Código inválido: " + code);
    }
}
