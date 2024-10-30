package com.pms.booking.enums;

public enum EDocumentType {
    CPF(1),  //Tipo de documento CPF
    CNPJ(2), // Tipo de documento CNPJ
    PASSPORT(3);   // Tipo de documento PASSAPORTE

    private final Integer code;

    EDocumentType(Integer code) {
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }

    public static EDocumentType fromCode(Integer code) {
        for (EDocumentType status : EDocumentType.values()) {
            if (status.getCode().equals(code)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Código inválido: " + code);
    }
}
