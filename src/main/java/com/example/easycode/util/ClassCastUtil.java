package com.example.easycode.util;

/**
 * @author wangyangyang137
 * @Description: 类型转换工具类
 * @date 2024-01-19 17:05
 */
public class ClassCastUtil {


    public static <T> T cast(Object source, Class<T> target) {
        return target.cast(source);
    }

}
