package com.example.easycode.convert;

import com.example.easycode.util.AssertUtil;

import java.util.Objects;

/**
 * @author wangyangyang
 * @Description: 转换器key
 * @date 2026-03-28 12:41
 */
public class ConverterKey {

    private final Class<?> sourceType;

    private final Class<?> targetType;

    public ConverterKey(Class<?> sourceType, Class<?> targetType) {
        AssertUtil.notNull(sourceType, "sourceType can not be null");
        AssertUtil.notNull(targetType, "targetType can not be null");
        this.sourceType = sourceType;
        this.targetType = targetType;
    }

    public Class<?> getSourceType() {
        return sourceType;
    }

    public Class<?> getTargetType() {
        return targetType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ConverterKey that = (ConverterKey) o;
        return Objects.equals(sourceType, that.sourceType) && Objects.equals(targetType, that.targetType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sourceType, targetType);
    }
}
