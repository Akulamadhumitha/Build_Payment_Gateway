package com.gateway.utils;

import java.time.YearMonth;
import java.util.Random;
import java.util.regex.Pattern;

public class PaymentValidationUtil {

    private static final Pattern VPA_PATTERN =
            Pattern.compile("^[a-zA-Z0-9._-]+@[a-zA-Z0-9]+$");

    public static boolean isValidVpa(String vpa) {
        return vpa != null && VPA_PATTERN.matcher(vpa).matches();
    }

    public static boolean isValidCardNumber(String number) {
        String clean = number.replaceAll("[ -]", "");
        if (!clean.matches("\\d{13,19}")) return false;

        int sum = 0;
        boolean alternate = false;
        for (int i = clean.length() - 1; i >= 0; i--) {
            int n = clean.charAt(i) - '0';
            if (alternate) {
                n *= 2;
                if (n > 9) n -= 9;
            }
            sum += n;
            alternate = !alternate;
        }
        return sum % 10 == 0;
    }

    public static String detectNetwork(String number) {
        String n = number.replaceAll("[ -]", "");
        if (n.startsWith("4")) return "visa";
        if (n.matches("^5[1-5].*")) return "mastercard";
        if (n.startsWith("34") || n.startsWith("37")) return "amex";
        if (n.startsWith("60") || n.startsWith("65") || n.matches("^8[1-9].*")) return "rupay";
        return "unknown";
    }

    public static boolean isValidExpiry(String month, String year) {
        int m = Integer.parseInt(month);
        if (m < 1 || m > 12) return false;

        int y = year.length() == 2 ? 2000 + Integer.parseInt(year) : Integer.parseInt(year);
        YearMonth expiry = YearMonth.of(y, m);
        return !expiry.isBefore(YearMonth.now());
    }

    public static boolean randomOutcome(double successRate) {
        return new Random().nextDouble() < successRate;
    }
}
