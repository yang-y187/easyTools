package com.example.easycode.util;

/**
 * @author wangyangyang
 * @Description: 脱敏工具类
 * @date 2026-03-28 13:33
 */
public class MaskUtil {

    private MaskUtil() {
    }

    public static String mask(String source, int prefixLength, int suffixLength) {
        return mask(source, prefixLength, suffixLength, '*');
    }

    public static String mask(String source, int prefixLength, int suffixLength, char maskChar) {
        if (source == null || source.isEmpty()) {
            return source;
        }
        if (prefixLength < 0 || suffixLength < 0) {
            throw new IllegalArgumentException("prefixLength and suffixLength can not be less than 0");
        }
        if (prefixLength + suffixLength >= source.length()) {
            return source;
        }
        StringBuilder builder = new StringBuilder();
        builder.append(source, 0, prefixLength);
        for (int index = 0; index < source.length() - prefixLength - suffixLength; index++) {
            builder.append(maskChar);
        }
        builder.append(source.substring(source.length() - suffixLength));
        return builder.toString();
    }

    public static String maskMobile(String mobile) {
        return mask(mobile, 3, 4);
    }

    public static String maskEmail(String email) {
        if (email == null || email.isEmpty()) {
            return email;
        }
        int splitIndex = email.indexOf('@');
        if (splitIndex <= 1) {
            return email;
        }
        return mask(email.substring(0, splitIndex), 1, 0) + email.substring(splitIndex);
    }

    public static String maskIdCard(String idCard) {
        return mask(idCard, 4, 4);
    }
}
