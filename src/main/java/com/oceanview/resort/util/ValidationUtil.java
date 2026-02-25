package com.oceanview.resort.util;

import com.oceanview.resort.exception.ValidationException;

import java.time.LocalDate;
import java.util.Objects;
import java.util.regex.Pattern;

public final class ValidationUtil {

    private static final Pattern BASIC_EMAIL =
            Pattern.compile("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$");

    private ValidationUtil() { }

    public static <T> T requireNonNull(T value, String fieldName) {
        if (value == null) {
            throw new ValidationException(fieldName + " is required");
        }
        return value;
    }

    public static String requireNonBlank(String value, String fieldName) {
        if (value == null) {
            throw new ValidationException(fieldName + " is required");
        }
        String trimmed = value.trim();
        if (trimmed.isEmpty()) {
            throw new ValidationException(fieldName + " must not be blank");
        }
        return trimmed;
    }

    public static String requireMaxLength(String value, String fieldName, int maxLen) {
        String trimmed = requireNonBlank(value, fieldName);
        if (trimmed.length() > maxLen) {
            throw new ValidationException(fieldName + " must be <= " + maxLen + " chars");
        }
        return trimmed;
    }

    public static int requirePositive(int value, String fieldName) {
        if (value <= 0) {
            throw new ValidationException(fieldName + " must be > 0");
        }
        return value;
    }

    public static String requireEmail(String email, String fieldName) {
        String trimmed = requireNonBlank(email, fieldName);
        if (!BASIC_EMAIL.matcher(trimmed).matches()) {
            throw new ValidationException(fieldName + " is not a valid email");
        }
        return trimmed;
    }

    public static void requireDateRange(LocalDate checkIn, LocalDate checkOut) {
        Objects.requireNonNull(checkIn, "checkIn is required");
        Objects.requireNonNull(checkOut, "checkOut is required");
        if (!checkOut.isAfter(checkIn)) {
            throw new ValidationException("checkOut must be after checkIn");
        }
    }
}
