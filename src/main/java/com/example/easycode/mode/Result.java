package com.example.easycode.mode;

/**
 * @author wangyangyang
 * @Description: 通用返回结果
 * @date 2026-03-28 13:43
 */
public class Result<T> {

    private boolean success;

    private String code;

    private String message;

    private T data;

    public static <T> Result<T> success() {
        return success(null);
    }

    public static <T> Result<T> success(T data) {
        return build(true, DefaultResultCode.SUCCESS.getCode(), DefaultResultCode.SUCCESS.getMessage(), data);
    }

    public static <T> Result<T> fail(String message) {
        return build(false, DefaultResultCode.FAIL.getCode(), message, null);
    }

    public static <T> Result<T> fail(ResultCode resultCode) {
        return build(false, resultCode.getCode(), resultCode.getMessage(), null);
    }

    public static <T> Result<T> fail(String code, String message) {
        return build(false, code, message, null);
    }

    public static <T> Result<T> build(boolean success, String code, String message, T data) {
        Result<T> result = new Result<>();
        result.setSuccess(success);
        result.setCode(code);
        result.setMessage(message);
        result.setData(data);
        return result;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
