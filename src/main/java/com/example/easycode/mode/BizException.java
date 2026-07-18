package com.example.easycode.mode;

/**
 * @author wangyangyang
 * @Description: easyTools 通用异常
 * @date 2026-03-28 13:42
 */
public class BizException extends RuntimeException {

    private final String code;

    public BizException(String message) {
        this(DefaultResultCode.SYSTEM_ERROR.getCode(), message);
    }

    public BizException(ResultCode resultCode) {
        this(resultCode.getCode(), resultCode.getMessage());
    }

    public BizException(ResultCode resultCode, String message) {
        this(resultCode.getCode(), message);
    }

    public BizException(String code, String message) {
        super(message);
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
