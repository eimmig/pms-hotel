package com.pms.booking.enums;

import lombok.Getter;

@Getter
public enum ERoomStatus {
    AVAILABLE("A"),  // Quarto disponível
    OCCUPIED("O"),   // Quarto ocupado
    UNDER_MAINTENANCE("M"), // Quarto em manutenção
    INACTIVE("I"); // Quarto inativo

    private final String code;

    ERoomStatus(String code) {
        this.code = code;
    }

    public static ERoomStatus fromCode(String code) {
        for (ERoomStatus status : ERoomStatus.values()) {
            if (status.getCode().equalsIgnoreCase(code)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Código inválido: " + code);
    }
}
