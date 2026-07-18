package com.example.easycode.util;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * @author wangyangyang
 * @Description: 日期工具类
 * @date 2026-03-28 13:30
 */
public class DateUtil {

    public static final String DEFAULT_DATE_PATTERN = "yyyy-MM-dd";

    public static final String DEFAULT_DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    private DateUtil() {
    }

    public static LocalDate today() {
        return LocalDate.now();
    }

    public static LocalDateTime now() {
        return LocalDateTime.now();
    }

    public static String format(LocalDate date) {
        return format(date, DEFAULT_DATE_PATTERN);
    }

    public static String format(LocalDate date, String pattern) {
        if (date == null) {
            return null;
        }
        return date.format(DateTimeFormatter.ofPattern(pattern));
    }

    public static String format(LocalDateTime dateTime) {
        return format(dateTime, DEFAULT_DATE_TIME_PATTERN);
    }

    public static String format(LocalDateTime dateTime, String pattern) {
        if (dateTime == null) {
            return null;
        }
        return dateTime.format(DateTimeFormatter.ofPattern(pattern));
    }

    public static LocalDate parseDate(String value) {
        return parseDate(value, DEFAULT_DATE_PATTERN);
    }

    public static LocalDate parseDate(String value, String pattern) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }
        return LocalDate.parse(value, DateTimeFormatter.ofPattern(pattern));
    }

    public static LocalDateTime parseDateTime(String value) {
        return parseDateTime(value, DEFAULT_DATE_TIME_PATTERN);
    }

    public static LocalDateTime parseDateTime(String value, String pattern) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }
        return LocalDateTime.parse(value, DateTimeFormatter.ofPattern(pattern));
    }

    public static LocalDateTime startOfDay(LocalDate date) {
        if (date == null) {
            return null;
        }
        return LocalDateTime.of(date, LocalTime.MIN);
    }

    public static LocalDateTime endOfDay(LocalDate date) {
        if (date == null) {
            return null;
        }
        return LocalDateTime.of(date, LocalTime.MAX);
    }

    public static long betweenDays(LocalDate startDate, LocalDate endDate) {
        AssertUtil.notNull(startDate, "startDate can not be null");
        AssertUtil.notNull(endDate, "endDate can not be null");
        return Duration.between(startDate.atStartOfDay(), endDate.atStartOfDay()).toDays();
    }
}
