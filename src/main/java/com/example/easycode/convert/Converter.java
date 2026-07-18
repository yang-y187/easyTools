package com.example.easycode.convert;

/**
 * @author wangyangyang
 * @Description: 转换器接口
 * @date 2026-03-28 12:40
 */
public interface Converter<S, T> {

    /**
     * 源类型
     *
     * @return 源类型
     */
    Class<S> sourceType();

    /**
     * 目标类型
     *
     * @return 目标类型
     */
    Class<T> targetType();

    /**
     * 执行转换
     *
     * @param source 源对象
     * @return 目标对象
     */
    T convert(S source);
}
