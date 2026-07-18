package com.example.easycode.mode;

import com.example.easycode.util.AssertUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author wangyangyang
 * @Description: 责任链执行器
 * @date 2026-03-28 12:34
 */
public class HandlerChain<T, C> {

    /**
     * 处理器集合
     */
    private final List<HandlerNode<T, C>> handlers = new ArrayList<>();

    /**
     * 回调集合
     */
    private final List<ChainCallback<T, C>> callbacks = new ArrayList<>();

    /**
     * 异常处理器
     */
    private ChainExceptionHandler<T, C> exceptionHandler;

    /**
     * 添加处理器
     *
     * @param handler 处理器
     * @return 当前责任链
     */
    public HandlerChain<T, C> addHandler(ChainHandler<T, C> handler) {
        AssertUtil.notNull(handler, "handler can not be null");
        return addHandler(handler.name(), handler);
    }

    /**
     * 添加具名处理器
     *
     * @param name    处理器名称
     * @param handler 处理器
     * @return 当前责任链
     */
    public HandlerChain<T, C> addHandler(String name, ChainHandler<T, C> handler) {
        AssertUtil.notBlank(name, "handler name can not be blank");
        AssertUtil.notNull(handler, "handler can not be null");
        handlers.add(new HandlerNode<>(name, handler));
        return this;
    }

    /**
     * 添加链路回调
     *
     * @param callback 回调
     * @return 当前责任链
     */
    public HandlerChain<T, C> addCallback(ChainCallback<T, C> callback) {
        AssertUtil.notNull(callback, "callback can not be null");
        callbacks.add(callback);
        return this;
    }

    /**
     * 配置异常处理器
     *
     * @param exceptionHandler 异常处理器
     * @return 当前责任链
     */
    public HandlerChain<T, C> exceptionHandler(ChainExceptionHandler<T, C> exceptionHandler) {
        this.exceptionHandler = exceptionHandler;
        return this;
    }

    /**
     * 顺序处理完整链路
     *
     * @param source  原始数据
     * @param context 上下文
     * @return 最终结果
     */
    public T handle(T source, C context) {
        return execute(handlers, source, context);
    }

    /**
     * 只执行指定名称的处理器
     *
     * @param handlerName 处理器名称
     * @param source      原始数据
     * @param context     上下文
     * @return 处理结果
     */
    public T handleOne(String handlerName, T source, C context) {
        return execute(Collections.singletonList(getRequiredNode(handlerName)), source, context);
    }

    /**
     * 只执行指定下标的处理器
     *
     * @param index   处理器下标
     * @param source  原始数据
     * @param context 上下文
     * @return 处理结果
     */
    public T handleOne(int index, T source, C context) {
        if (index < 0 || index >= handlers.size()) {
            throw new IndexOutOfBoundsException("handler index out of range, index=" + index);
        }
        return execute(Collections.singletonList(handlers.get(index)), source, context);
    }

    /**
     * 从指定处理器开始继续执行链路
     *
     * @param handlerName 处理器名称
     * @param source      原始数据
     * @param context     上下文
     * @return 处理结果
     */
    public T handleFrom(String handlerName, T source, C context) {
        int index = getRequiredIndex(handlerName);
        return execute(copyFrom(index), source, context);
    }

    /**
     * 从指定处理器的下一个处理器开始继续执行链路
     *
     * @param handlerName 处理器名称
     * @param source      原始数据
     * @param context     上下文
     * @return 处理结果
     */
    public T handleAfter(String handlerName, T source, C context) {
        int index = getRequiredIndex(handlerName);
        return execute(copyFrom(index + 1), source, context);
    }

    /**
     * 当前处理器数量
     *
     * @return size
     */
    public int size() {
        return handlers.size();
    }

    private T execute(List<HandlerNode<T, C>> nodes, T source, C context) {
        beforeChain(source, context);
        T result = new Invocation(nodes, context).next(source);
        afterChain(source, context, result);
        return result;
    }

    private List<HandlerNode<T, C>> copyFrom(int index) {
        if (index >= handlers.size()) {
            return Collections.emptyList();
        }
        return new ArrayList<>(handlers.subList(index, handlers.size()));
    }

    private HandlerNode<T, C> getRequiredNode(String handlerName) {
        return handlers.get(getRequiredIndex(handlerName));
    }

    private int getRequiredIndex(String handlerName) {
        AssertUtil.notBlank(handlerName, "handler name can not be blank");
        for (int i = 0; i < handlers.size(); i++) {
            if (handlerName.equals(handlers.get(i).name)) {
                return i;
            }
        }
        throw new IllegalArgumentException("handler not found, name=" + handlerName);
    }

    private void beforeChain(T source, C context) {
        for (ChainCallback<T, C> callback : callbacks) {
            callback.beforeChain(source, context);
        }
    }

    private void afterChain(T source, C context, T result) {
        for (ChainCallback<T, C> callback : callbacks) {
            callback.afterChain(source, context, result);
        }
    }

    private void beforeHandler(String handlerName, ChainHandler<T, C> handler, T source, C context) {
        for (ChainCallback<T, C> callback : callbacks) {
            callback.beforeHandler(handlerName, handler, source, context);
        }
    }

    private void afterHandler(String handlerName, ChainHandler<T, C> handler, T source, C context, T result) {
        for (ChainCallback<T, C> callback : callbacks) {
            callback.afterHandler(handlerName, handler, source, context, result);
        }
    }

    private void onSkip(String handlerName, ChainHandler<T, C> handler, T source, C context) {
        for (ChainCallback<T, C> callback : callbacks) {
            callback.onSkip(handlerName, handler, source, context);
        }
    }

    private void onException(String handlerName, ChainHandler<T, C> handler, T source, C context, RuntimeException exception) {
        for (ChainCallback<T, C> callback : callbacks) {
            callback.onException(handlerName, handler, source, context, exception);
        }
    }

    private static class HandlerNode<T, C> {
        private final String name;
        private final ChainHandler<T, C> handler;

        private HandlerNode(String name, ChainHandler<T, C> handler) {
            this.name = name;
            this.handler = handler;
        }
    }

    private class Invocation implements ChainInvoker<T, C> {
        private final List<HandlerNode<T, C>> nodes;
        private final C context;
        private int index;

        private Invocation(List<HandlerNode<T, C>> nodes, C context) {
            this.nodes = nodes;
            this.context = context;
        }

        @Override
        public T next(T source) {
            if (index >= nodes.size()) {
                return source;
            }

            HandlerNode<T, C> node = nodes.get(index++);
            try {
                if (!node.handler.support(context)) {
                    onSkip(node.name, node.handler, source, context);
                    return next(source);
                }

                beforeHandler(node.name, node.handler, source, context);
                T result = node.handler.handle(source, context, this);
                afterHandler(node.name, node.handler, source, context, result);
                return result;
            } catch (RuntimeException e) {
                onException(node.name, node.handler, source, context, e);
                if (exceptionHandler == null) {
                    throw e;
                }
                return exceptionHandler.handle(node.name, node.handler, source, context, e, this);
            }
        }
    }
}
