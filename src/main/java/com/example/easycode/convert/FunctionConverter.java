package com.example.easycode.convert;

import com.example.easycode.util.AssertUtil;

import java.util.function.Function;

/**
 * @author wangyangyang
 * @Description: 函数式转换器
 * @date 2026-03-28 12:42
 */
public class FunctionConverter<S, T> implements Converter<S, T> {

    private final Class<S> sourceType;

    private final Class<T> targetType;

    private final Function<S, T> converter;

    public FunctionConverter(Class<S> sourceType, Class<T> targetType, Function<S, T> converter) {
        AssertUtil.notNull(sourceType, "sourceType can not be null");
        AssertUtil.notNull(targetType, "targetType can not be null");
        AssertUtil.notNull(converter, "converter can not be null");
        this.sourceType = sourceType;
        this.targetType = targetType;
        this.converter = converter;
    }

    @Override
    public Class<S> sourceType() {
        return sourceType;
    }

    @Override
    public Class<T> targetType() {
        return targetType;
    }

    @Override
    public T convert(S source) {
        return converter.apply(source);
    }
}
