package com.example.easycode.mode;

/**
 * @author wangyangyang
 * @Description: 可控制后续步骤的责任链处理器
 * @date 2026-03-28 12:38
 */
@FunctionalInterface
public interface ChainAroundHandler<T, C> extends ChainHandler<T, C> {

    /**
     * Around 处理器以三参数 handle 为主，这里默认不改变数据。
     *
     * @param source  原始数据
     * @param context 上下文
     * @return 原始数据
     */
    @Override
    default T handle(T source, C context) {
        return source;
    }

    @Override
    T handle(T source, C context, ChainInvoker<T, C> chain);
}
