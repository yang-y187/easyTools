package com.example.easycode.mode;

/**
 * @author wangyangyang
 * @Description: 默认返回码
 * @date 2026-03-28 13:41
 */
public enum DefaultResultCode implements ResultCode {

    SUCCESS("0", "success"),
    FAIL("1", "fail"),
    VALIDATE_FAIL("400", "validate fail"),
    NOT_FOUND("404", "not found"),
    SYSTEM_ERROR("500", "system error");

    private final String code;

    private final String message;

    DefaultResultCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
