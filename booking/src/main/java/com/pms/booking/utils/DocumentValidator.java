package com.pms.booking.utils;

import com.pms.booking.enums.EDocumentType;

public class DocumentValidator {
    public static boolean validateDocument(String document, EDocumentType documentType) {
        if (document == null || document.isEmpty()) {
            throw new IllegalArgumentException("Documento não pode ser nulo ou vazio");
        }

        return switch (documentType) {
            case CPF -> validateCPF(document);
            case CNPJ -> validateCNPJ(document);
            case PASSPORT -> true;
            default -> throw new IllegalArgumentException("Tipo de documento não suportado: " + documentType);
        };
    }

    // Validação de CPF
    private static boolean validateCPF(String cpf) {
        if (cpf.length() != 11 || !cpf.matches("\\d{11}")) {
            return false;
        }

        int sum = 0;
        int weight = 10;

        // Validação do primeiro dígito verificador
        for (int i = 0; i < 9; i++) {
            sum += (cpf.charAt(i) - '0') * weight--;
        }
        int firstDigit = (sum * 10) % 11;
        if (firstDigit == 10) firstDigit = 0;
        if (firstDigit != (cpf.charAt(9) - '0')) return false;

        // Validação do segundo dígito verificador
        sum = 0;
        weight = 11;
        for (int i = 0; i < 10; i++) {
            sum += (cpf.charAt(i) - '0') * weight--;
        }
        int secondDigit = (sum * 10) % 11;
        if (secondDigit == 10) secondDigit = 0;
        return secondDigit == (cpf.charAt(10) - '0');
    }

    private static boolean validateCNPJ(String cnpj) {
        if (cnpj.length() != 14 || !cnpj.matches("\\d{14}")) {
            return false;
        }
        int sum = 0;
        int weight;

        // Validação do primeiro dígito verificador
        weight = 5;
        for (int i = 0; i < 12; i++) {
            sum += (cnpj.charAt(i) - '0') * weight--;
            if (weight < 2) weight = 9;
        }
        int firstDigit = (sum % 11 < 2) ? 0 : 11 - (sum % 11);
        if (firstDigit != (cnpj.charAt(12) - '0')) return false;

        // Validação do segundo dígito verificador
        sum = 0;
        weight = 6;
        for (int i = 0; i < 13; i++) {
            sum += (cnpj.charAt(i) - '0') * weight--;
            if (weight < 2) weight = 9;
        }
        int secondDigit = (sum % 11 < 2) ? 0 : 11 - (sum % 11);
        return secondDigit == (cnpj.charAt(13) - '0');
    }
}
