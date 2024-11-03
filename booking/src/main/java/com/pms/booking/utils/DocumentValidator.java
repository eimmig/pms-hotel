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

    public static boolean validateCPF(String cpf) {
        cpf = cpf.replaceAll("[^0-9]", "");

        if (cpf.length() != 11) {
            return false;
        }

        if (cpf.chars().distinct().count() == 1) {
            return false;
        }

        int firstDigit = calculateDigit(cpf, 9);
        if (firstDigit != Character.getNumericValue(cpf.charAt(9))) {
            return false;
        }

        int secondDigit = calculateDigit(cpf, 10);
        return secondDigit == Character.getNumericValue(cpf.charAt(10));
    }

    private static int calculateDigit(String cpf, int length) {
        int sum = 0;
        for (int i = 0; i < length; i++) {
            sum += Character.getNumericValue(cpf.charAt(i)) * (length + 1 - i);
        }
        int remainder = sum % 11;
        return remainder < 2 ? 0 : 11 - remainder;
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
