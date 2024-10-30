package com.pms.booking.utils;

public  class PhoneFormateter {
    public static String unformatPhoneNumber(String phoneNumber) {
        if (phoneNumber == null) {
            return null;
        }
        return phoneNumber.replaceAll("[^0-9]", "");
    }

    public static String formatPhoneNumber(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.length() < 11) {
            return phoneNumber;
        }

        StringBuilder formattedNumber = new StringBuilder();

        if (phoneNumber.startsWith("55")) {
            formattedNumber.append("+").append(phoneNumber.substring(0, 2)).append(" ");

            formattedNumber.append("(").append(phoneNumber.substring(2, 4)).append(") ");

            formattedNumber.append(phoneNumber.substring(4, 9)).append("-");
            formattedNumber.append(phoneNumber.substring(9));
        } else {
            formattedNumber.append("+55 ");
            formattedNumber.append("(").append(phoneNumber.substring(0, 2)).append(") ");
            formattedNumber.append(phoneNumber.substring(2, 7)).append("-");
            formattedNumber.append(phoneNumber.substring(7));
        }
        return formattedNumber.toString();
    }
}
