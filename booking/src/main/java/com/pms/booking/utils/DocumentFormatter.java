package com.pms.booking.utils;

import com.pms.booking.enums.EDocumentType;

public class DocumentFormatter {
    public static String unformatDocument(String document, Integer documentTypeId) {
        if (document == null) {
            throw new IllegalArgumentException("Documento não pode ser nulo");
        }

        EDocumentType documentType = EDocumentType.fromCode(documentTypeId);

        return switch (documentType) {
            case CPF -> document.replaceAll("[.\\-]", ""); // Remove pontos e traço do CPF
            case CNPJ -> document.replaceAll("[./\\-]", ""); // Remove pontos, barra e traço do CNPJ
            case PASSPORT -> document;
            default -> throw new IllegalArgumentException("Tipo de documento não suportado: " + documentType);
        };
    }
    public static String formatDocument(String document, Integer documentTypeId) {
        if (document == null) {
            throw new IllegalArgumentException("Documento não pode ser nulo");
        }

        EDocumentType  documentType = EDocumentType.fromCode(documentTypeId);

        return switch (documentType) {
            case CPF -> document.replaceAll("(\\d{3})(\\d{3})(\\d{3})(\\d{2})", "$1.$2.$3-$4"); // Formata o CPF
            case CNPJ ->
                    document.replaceAll("(\\d{2})(\\d{3})(\\d{3})(\\d{4})(\\d{2})", "$1.$2.$3/$4-$5"); // Formata o CNPJ
            case PASSPORT -> document; // Passaporte não tem formatação específica
            default -> throw new IllegalArgumentException("Tipo de documento não suportado: " + documentType);
        };
    }
}
