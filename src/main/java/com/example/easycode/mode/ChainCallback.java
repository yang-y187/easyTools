package com.example.easycode.mode;

/**
 * @author wangyangyang
 * @Description: 责任链回调
 * @date 2026-03-28 12:36
 */
public interface ChainCallback<T, C> {

    default void beforeChain(T source, C context) {
    }

    default void afterChain(T source, C context, T result) {
    }

    default void beforeHandler(String handlerName, ChainHandler<T, C> handler, T source, C context) {
    }

    default void afterHandler(String handlerName, ChainHandler<T, C> handler, T source, C context, T result) {
    }

    default void onSkip(String handlerName, ChainHandler<T, C> handler, T source, C context) {
    }

    default void onException(String handlerName, ChainHandler<T, C> handler, T source, C context, RuntimeException exception) {
    }
}

