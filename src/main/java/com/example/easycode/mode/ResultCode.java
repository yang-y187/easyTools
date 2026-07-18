package com.example.easycode.mode;

/**
 * @author wangyangyang
 * @Description: 返回码抽象
 * @date 2026-03-28 13:40
 */
public interface ResultCode {

    /**
     * 返回码
     *
     * @return code
     */
    String getCode();

    /**
     * 返回描述
     *
     * @return message
     */
    String getMessage();
}
