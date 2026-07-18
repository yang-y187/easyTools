package com.example.easycode.util;

import com.example.easycode.mode.BizException;
import com.example.easycode.mode.DefaultResultCode;

import java.util.Collection;

/**
 * @author wangyangyang
 * @Description: 业务校验工具类
 * @date 2026-03-28 14:11
 */
public class ValidatorUtil {

    private ValidatorUtil() {
    }

    public static void notNull(Object value, String message) {
        if (value == null) {
            throw new BizException(DefaultResultCode.VALIDATE_FAIL, message);
        }
    }

    public static void notBlank(String value, String message) {
        if (value == null || value.trim().isEmpty()) {
            throw new BizException(DefaultResultCode.VALIDATE_FAIL, message);
        }
    }

    public static void notEmpty(Collection<?> values, String message) {
        if (values == null || values.isEmpty()) {
            throw new BizException(DefaultResultCode.VALIDATE_FAIL, message);
        }
    }

    public static void isTrue(boolean expression, String message) {
        if (!expression) {
            throw new BizException(DefaultResultCode.VALIDATE_FAIL, message);
        }
    }

    public static void positive(long value, String message) {
        if (value <= 0) {
            throw new BizException(DefaultResultCode.VALIDATE_FAIL, message);
        }
    }

    public static void between(long value, long min, long max, String message) {
        if (value < min || value > max) {
            throw new BizException(DefaultResultCode.VALIDATE_FAIL, message);
        }
    }
}
