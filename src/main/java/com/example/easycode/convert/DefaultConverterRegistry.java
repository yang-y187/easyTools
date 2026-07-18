package com.example.easycode.convert;

import com.example.easycode.util.AssertUtil;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

/**
 * @author wangyangyang
 * @Description: 默认转换器注册器
 * @date 2026-03-28 12:43
 */
public class DefaultConverterRegistry {

    private final Map<ConverterKey, Converter<?, ?>> converterMap = new ConcurrentHashMap<>();

    public DefaultConverterRegistry register(Converter<?, ?> converter) {
        AssertUtil.notNull(converter, "converter can not be null");
        converterMap.put(new ConverterKey(converter.sourceType(), converter.targetType()), converter);
        return this;
    }

    public <S, T> DefaultConverterRegistry register(Class<S> sourceType, Class<T> targetType, Function<S, T> converter) {
        return register(new FunctionConverter<>(sourceType, targetType, converter));
    }

    public boolean contains(Class<?> sourceType, Class<?> targetType) {
        if (sourceType == null || targetType == null) {
            return false;
        }
        return converterMap.containsKey(new ConverterKey(sourceType, targetType));
    }

    @SuppressWarnings("unchecked")
    public <S, T> T convert(S source, Class<T> targetType) {
        if (source == null) {
            return null;
        }
        AssertUtil.notNull(targetType, "targetType can not be null");
        Converter<S, T> converter = (Converter<S, T>) getConverter(source.getClass(), targetType);
        return converter.convert(source);
    }

    public int size() {
        return converterMap.size();
    }

    private Converter<?, ?> getConverter(Class<?> sourceType, Class<?> targetType) {
        Converter<?, ?> directConverter = converterMap.get(new ConverterKey(sourceType, targetType));
        if (directConverter != null) {
            return directConverter;
        }
        for (Map.Entry<ConverterKey, Converter<?, ?>> entry : converterMap.entrySet()) {
            ConverterKey key = entry.getKey();
            if (key.getSourceType().isAssignableFrom(sourceType) && key.getTargetType().equals(targetType)) {
                return entry.getValue();
            }
        }
        throw new IllegalArgumentException("converter not found, sourceType=" + sourceType.getName() + ", targetType=" + targetType.getName());
    }
}
